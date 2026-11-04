package service;

import entity.HocVien;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.HocVienRepository;

import java.util.ArrayList;
import java.util.List;

// Nghiệp vụ của học viên: CRUD, chặn trùng số điện thoại, tìm kiếm, sắp xếp theo tên.
public class HocVienService implements IService<HocVien> {
    private HocVienRepository hocVienRepository = new HocVienRepository();
    private List<HocVien> list;
    private static final HocVienService hocVienService = new HocVienService();

    private HocVienService() {
        list = hocVienRepository.read();
    }

    public static HocVienService getObject() {
        return hocVienService;
    }

    // id = số giây hiện tại (giống bài mẫu: currentTimeMillis() / 1000).
    // Bài mẫu bị trùng id nếu thêm 2 bản ghi trong cùng 1 giây, nên ở đây gặp id đã có thì cộng thêm 1.
    public long taoIdMoi() {
        long id = System.currentTimeMillis() / 1000;
        while (daCoId(id)) {
            id++;
        }
        return id;
    }

    private boolean daCoId(long id) {
        for (HocVien item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // Số điện thoại không được trùng với học viên KHÁC (idBoQua = id của chính học viên đang sửa)
    private void kiemTraTrungSoDienThoai(String soDienThoai, long idBoQua) {
        for (HocVien item : list) {
            if (item.getId() != idBoQua && item.getSoDienThoai().equals(soDienThoai)) {
                throw new NghiepVuException("So dien thoai " + soDienThoai + " da duoc dung boi hoc vien "
                        + item.getHoTen() + " (id " + item.getId() + ").");
            }
        }
    }

    @Override
    public void add(HocVien newHocVien) {
        kiemTraTrungSoDienThoai(newHocVien.getSoDienThoai(), newHocVien.getId());
        this.list.add(newHocVien);
        this.hocVienRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        if (DangKyService.getObject().coDangKyCuaHocVien(id)) {
            throw new NghiepVuException("Hoc vien dang co dang ky hoc, khong the xoa. Hay huy cac dang ky truoc.");
        }
        if (UserService.getObject().coTaiKhoanCua(VaiTro.HOC_VIEN, id)) {
            throw new NghiepVuException("Hoc vien dang co tai khoan dang nhap. Hay xoa tai khoan truoc.");
        }
        this.list.remove(index);
        this.hocVienRepository.write(list);
    }

    @Override
    public void update(long id, HocVien hocVien) {
        int index = this.findIndexById(id);
        kiemTraTrungSoDienThoai(hocVien.getSoDienThoai(), id);
        this.list.set(index, hocVien);
        this.hocVienRepository.write(list);
    }

    @Override
    public List<HocVien> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException();
    }

    @Override
    public HocVien findById(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }

    public List<HocVien> timTheoTen(String keyword) {
        List<HocVien> listFound = new ArrayList<>();
        for (HocVien item : list) {
            if (item.getHoTen().toLowerCase().contains(keyword.toLowerCase())) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    public List<HocVien> timTheoSoDienThoai(String keyword) {
        List<HocVien> listFound = new ArrayList<>();
        for (HocVien item : list) {
            if (item.getSoDienThoai().contains(keyword)) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    // Sắp xếp kiểu người Việt: so sánh TÊN (chữ cuối) trước, trùng tên thì so cả họ tên
    public List<HocVien> sapXepTheoTen() {
        List<HocVien> ketQua = new ArrayList<>(list);
        ketQua.sort((a, b) -> {
            int soSanhTen = a.layTen().compareToIgnoreCase(b.layTen());
            if (soSanhTen != 0) {
                return soSanhTen;
            }
            return a.getHoTen().compareToIgnoreCase(b.getHoTen());
        });
        return ketQua;
    }
}
