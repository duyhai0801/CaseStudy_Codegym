package service;

import entity.KhoaHoc;
import entity.LopHoc;
import entity.TrangThaiLop;
import entity.TrinhDo;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.LopHocRepository;

import java.util.ArrayList;
import java.util.List;

// Nghiệp vụ của lớp học: CRUD có kiểm tra khóa ngoại, lọc lớp, sắp xếp theo ngày khai giảng.
public class LopHocService implements IService<LopHoc> {
    private LopHocRepository lopHocRepository = new LopHocRepository();
    private List<LopHoc> list;
    private static final LopHocService lopHocService = new LopHocService();

    private LopHocService() {
        list = lopHocRepository.read();
    }

    public static LopHocService getObject() {
        return lopHocService;
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
        for (LopHoc item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    // khoaHocId và giangVienId phải trỏ tới dữ liệu có thật
    private void kiemTraKhoaNgoai(LopHoc lop) {
        try {
            KhoaHocService.getObject().findById(lop.getKhoaHocId());
        } catch (DataNotFoundException e) {
            throw new NghiepVuException("Khoa hoc id " + lop.getKhoaHocId() + " khong ton tai.");
        }
        try {
            GiangVienService.getObject().findById(lop.getGiangVienId());
        } catch (DataNotFoundException e) {
            throw new NghiepVuException("Giang vien id " + lop.getGiangVienId() + " khong ton tai.");
        }
    }

    @Override
    public void add(LopHoc newLopHoc) {
        kiemTraKhoaNgoai(newLopHoc);
        this.list.add(newLopHoc);
        this.lopHocRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        if (DangKyService.getObject().demSoHocVienCuaLop(id) > 0) {
            throw new NghiepVuException("Lop da co hoc vien dang ky, khong the xoa.");
        }
        this.list.remove(index);
        this.lopHocRepository.write(list);
    }

    @Override
    public void update(long id, LopHoc lopMoi) {
        int index = this.findIndexById(id);
        kiemTraKhoaNgoai(lopMoi);
        LopHoc lopCu = this.list.get(index);
        int siSoHienTai = DangKyService.getObject().demSoHocVienCuaLop(id);
        if (lopMoi.getSiSoToiDa() < siSoHienTai) {
            throw new NghiepVuException("Lop dang co " + siSoHienTai + " hoc vien, si so toi da khong duoc nho hon.");
        }
        // Học phí của các đăng ký đã chốt theo khóa cũ, nên lớp có học viên rồi thì không cho đổi khóa
        if (siSoHienTai > 0 && lopCu.getKhoaHocId() != lopMoi.getKhoaHocId()) {
            throw new NghiepVuException("Lop da co hoc vien dang ky, khong the doi sang khoa hoc khac.");
        }
        this.list.set(index, lopMoi);
        this.lopHocRepository.write(list);
    }

    @Override
    public List<LopHoc> getAll() {
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
    public LopHoc findById(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }

    public boolean coLopThuocKhoaHoc(long khoaHocId) {
        for (LopHoc item : list) {
            if (item.getKhoaHocId() == khoaHocId) {
                return true;
            }
        }
        return false;
    }

    public boolean coLopCuaGiangVien(long giangVienId) {
        for (LopHoc item : list) {
            if (item.getGiangVienId() == giangVienId) {
                return true;
            }
        }
        return false;
    }

    public int tinhSoChoConLai(LopHoc lop) {
        return lop.getSiSoToiDa() - DangKyService.getObject().demSoHocVienCuaLop(lop.getId());
    }

    // Lớp còn nhận học viên: chưa kết thúc và chưa đủ sĩ số
    public List<LopHoc> locLopConCho() {
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc item : list) {
            if (item.getTrangThai() != TrangThaiLop.DA_KET_THUC && tinhSoChoConLai(item) > 0) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public List<LopHoc> locTheoGiangVien(long giangVienId) {
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc item : list) {
            if (item.getGiangVienId() == giangVienId) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public List<LopHoc> locTheoKhoaHoc(long khoaHocId) {
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc item : list) {
            if (item.getKhoaHocId() == khoaHocId) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    // Trình độ nằm ở KhoaHoc, nên phải đi từ lớp -> khóa học rồi mới so sánh
    public List<LopHoc> locTheoTrinhDo(TrinhDo trinhDo) {
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc item : list) {
            KhoaHoc khoaHoc = KhoaHocService.getObject().findById(item.getKhoaHocId());
            if (khoaHoc.getTrinhDo() == trinhDo) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public List<LopHoc> sapXepTheoNgayKhaiGiang() {
        List<LopHoc> ketQua = new ArrayList<>(list);
        ketQua.sort((a, b) -> a.getNgayKhaiGiang().compareTo(b.getNgayKhaiGiang()));
        return ketQua;
    }
}
