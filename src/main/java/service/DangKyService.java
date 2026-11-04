package service;

import entity.DangKy;
import entity.HocVien;
import entity.KhoaHoc;
import entity.LopHoc;
import entity.TrangThaiLop;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.DangKyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Nghiệp vụ chính của trung tâm: đăng ký học viên vào lớp, hủy đăng ký, đóng học phí.
public class DangKyService implements IService<DangKy> {
    // Học viên cũ (đã từng đăng ký ít nhất 1 lớp) được giảm 10% khi đăng ký lớp mới
    public static final int PHAN_TRAM_GIAM_HOC_VIEN_CU = 10;

    private DangKyRepository dangKyRepository = new DangKyRepository();
    private List<DangKy> list;
    private static final DangKyService dangKyService = new DangKyService();

    private DangKyService() {
        list = dangKyRepository.read();
    }

    public static DangKyService getObject() {
        return dangKyService;
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
        for (DangKy item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(DangKy newDangKy) {
        this.list.add(newDangKy);
        this.dangKyRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        this.list.remove(index);
        this.dangKyRepository.write(list);
    }

    @Override
    public void update(long id, DangKy dangKy) {
        int index = this.findIndexById(id);
        this.list.set(index, dangKy);
        this.dangKyRepository.write(list);
    }

    @Override
    public List<DangKy> getAll() {
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
    public DangKy findById(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }

    /*
    Đăng ký học viên vào lớp. Kiểm tra lần lượt:
    1. Học viên và lớp có tồn tại
    2. Lớp chưa kết thúc
    3. Lớp chưa đủ sĩ số
    4. Học viên chưa đăng ký lớp này
    Đạt hết thì tính học phí (giảm cho học viên cũ) rồi lưu lượt đăng ký.
     */
    public DangKy dangKyVaoLop(long hocVienId, long lopHocId) {
        HocVien hocVien;
        LopHoc lopHoc;
        try {
            hocVien = HocVienService.getObject().findById(hocVienId);
        } catch (DataNotFoundException e) {
            throw new NghiepVuException("Hoc vien id " + hocVienId + " khong ton tai.");
        }
        try {
            lopHoc = LopHocService.getObject().findById(lopHocId);
        } catch (DataNotFoundException e) {
            throw new NghiepVuException("Lop hoc id " + lopHocId + " khong ton tai.");
        }

        if (lopHoc.getTrangThai() == TrangThaiLop.DA_KET_THUC) {
            throw new NghiepVuException("Lop " + lopHoc.getTenLop() + " da ket thuc, khong the dang ky.");
        }

        int siSo = demSoHocVienCuaLop(lopHocId);
        if (siSo >= lopHoc.getSiSoToiDa()) {
            throw new NghiepVuException("Lop " + lopHoc.getTenLop() + " da du si so ("
                    + siSo + "/" + lopHoc.getSiSoToiDa() + ").");
        }

        if (daDangKy(hocVienId, lopHocId)) {
            throw new NghiepVuException("Hoc vien " + hocVien.getHoTen() + " da dang ky lop "
                    + lopHoc.getTenLop() + " roi.");
        }

        KhoaHoc khoaHoc = KhoaHocService.getObject().findById(lopHoc.getKhoaHocId());
        long hocPhi = khoaHoc.getHocPhi();
        if (demSoDangKyCuaHocVien(hocVienId) > 0) {
            hocPhi = hocPhi * (100 - PHAN_TRAM_GIAM_HOC_VIEN_CU) / 100;
        }

        DangKy dangKy = new DangKy(taoIdMoi(), hocVienId, lopHocId, LocalDate.now(), hocPhi, 0);
        this.add(dangKy);
        return dangKy;
    }

    // Chỉ được hủy khi lớp chưa khai giảng. Trả về lượt đăng ký đã hủy để UI báo số tiền cần hoàn.
    public DangKy huyDangKy(long dangKyId) {
        DangKy dangKy = this.findById(dangKyId);
        LopHoc lopHoc = LopHocService.getObject().findById(dangKy.getLopHocId());
        if (lopHoc.getTrangThai() != TrangThaiLop.SAP_KHAI_GIANG) {
            throw new NghiepVuException("Lop " + lopHoc.getTenLop() + " da khai giang, khong the huy dang ky.");
        }
        this.delete(dangKyId);
        return dangKy;
    }

    public DangKy dongHocPhi(long dangKyId, long soTien) {
        DangKy dangKy = this.findById(dangKyId);
        long conNo = dangKy.tinhSoTienConNo();
        if (conNo == 0) {
            throw new NghiepVuException("Luot dang ky nay da dong du hoc phi.");
        }
        if (soTien <= 0) {
            throw new NghiepVuException("So tien phai lon hon 0.");
        }
        if (soTien > conNo) {
            throw new NghiepVuException("So tien vuot qua so con no (" + conNo + ").");
        }
        dangKy.setSoTienDaDong(dangKy.getSoTienDaDong() + soTien);
        this.dangKyRepository.write(list);
        return dangKy;
    }

    public List<DangKy> layDangKyCuaLop(long lopHocId) {
        List<DangKy> ketQua = new ArrayList<>();
        for (DangKy item : list) {
            if (item.getLopHocId() == lopHocId) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public List<DangKy> layDangKyCuaHocVien(long hocVienId) {
        List<DangKy> ketQua = new ArrayList<>();
        for (DangKy item : list) {
            if (item.getHocVienId() == hocVienId) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public int demSoHocVienCuaLop(long lopHocId) {
        return layDangKyCuaLop(lopHocId).size();
    }

    public int demSoDangKyCuaHocVien(long hocVienId) {
        return layDangKyCuaHocVien(hocVienId).size();
    }

    public boolean coDangKyCuaHocVien(long hocVienId) {
        return demSoDangKyCuaHocVien(hocVienId) > 0;
    }

    public boolean daDangKy(long hocVienId, long lopHocId) {
        for (DangKy item : list) {
            if (item.getHocVienId() == hocVienId && item.getLopHocId() == lopHocId) {
                return true;
            }
        }
        return false;
    }
}
