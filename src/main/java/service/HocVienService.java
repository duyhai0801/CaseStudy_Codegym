package service;

import entity.HocVien;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.HocVienRepository;

import java.util.ArrayList;
import java.util.List;

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
        this.list.remove(index);
        this.hocVienRepository.write(list);
        UserService.getObject().xoaTaiKhoanCua(VaiTro.HOC_VIEN, id);
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
