package service;

import entity.DiemDanh;
import entity.LopHoc;
import entity.TrangThaiDiemDanh;
import entity.TrangThaiLop;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.DiemDanhRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Nghiệp vụ điểm danh: lưu / cập nhật điểm danh một buổi học, thống kê chuyên cần.
public class DiemDanhService implements IService<DiemDanh> {
    // Học viên đi học dưới mức này thì bị đánh dấu cần nhắc nhở
    public static final int TI_LE_CHUYEN_CAN_TOI_THIEU = 80;

    private DiemDanhRepository diemDanhRepository = new DiemDanhRepository();
    private List<DiemDanh> list;
    private static final DiemDanhService diemDanhService = new DiemDanhService();

    private DiemDanhService() {
        list = diemDanhRepository.read();
    }

    public static DiemDanhService getObject() {
        return diemDanhService;
    }

    // id = số giây hiện tại, trùng thì cộng 1 (giống các service khác)
    public long taoIdMoi() {
        long id = System.currentTimeMillis() / 1000;
        while (daCoId(id)) {
            id++;
        }
        return id;
    }

    private boolean daCoId(long id) {
        for (DiemDanh item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(DiemDanh diemDanh) {
        this.list.add(diemDanh);
        this.diemDanhRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        this.list.remove(index);
        this.diemDanhRepository.write(list);
    }

    @Override
    public void update(long id, DiemDanh diemDanh) {
        int index = this.findIndexById(id);
        this.list.set(index, diemDanh);
        this.diemDanhRepository.write(list);
    }

    @Override
    public List<DiemDanh> getAll() {
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
    public DiemDanh findById(long id) {
        return this.list.get(this.findIndexById(id));
    }

    /*
    Kiểm tra trước khi điểm danh:
    - Lớp phải đang học
    - Ngày điểm danh không ở tương lai và không trước ngày khai giảng
    - Lớp phải có học viên
     */
    public void kiemTraCoTheDiemDanh(LopHoc lop, LocalDate ngay) {
        if (lop.getTrangThai() != TrangThaiLop.DANG_HOC) {
            throw new NghiepVuException("Chi diem danh duoc lop dang hoc (lop " + lop.getTenLop() + " dang o trang thai '"
                    + lop.getTrangThai().getTenHienThi() + "').");
        }
        if (ngay.isAfter(LocalDate.now())) {
            throw new NghiepVuException("Khong the diem danh cho ngay trong tuong lai.");
        }
        if (ngay.isBefore(lop.getNgayKhaiGiang())) {
            throw new NghiepVuException("Ngay diem danh truoc ngay khai giang cua lop.");
        }
        if (DangKyService.getObject().demSoHocVienCuaLop(lop.getId()) == 0) {
            throw new NghiepVuException("Lop chua co hoc vien nao.");
        }
    }

    public boolean daDiemDanh(long lopHocId, LocalDate ngay) {
        return !layDiemDanhBuoi(lopHocId, ngay).isEmpty();
    }

    public List<DiemDanh> layDiemDanhBuoi(long lopHocId, LocalDate ngay) {
        List<DiemDanh> ketQua = new ArrayList<>();
        for (DiemDanh item : list) {
            if (item.getLopHocId() == lopHocId && item.getNgay().equals(ngay)) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    // Lưu điểm danh 1 buổi. Nếu buổi đó đã điểm danh rồi thì xóa bản cũ rồi lưu bản mới (= cập nhật điểm danh).
    public void luuDiemDanhBuoi(LopHoc lop, LocalDate ngay, List<DiemDanh> danhSachMoi) {
        kiemTraCoTheDiemDanh(lop, ngay);
        // Duyệt ngược để xóa phần tử trong lúc lặp mà không bị lệch chỉ số
        for (int i = list.size() - 1; i >= 0; i--) {
            DiemDanh item = list.get(i);
            if (item.getLopHocId() == lop.getId() && item.getNgay().equals(ngay)) {
                list.remove(i);
            }
        }
        for (DiemDanh item : danhSachMoi) {
            item.setId(taoIdMoi());
            list.add(item);
        }
        diemDanhRepository.write(list);
    }

    // Các ngày lớp đã được điểm danh, sắp xếp tăng dần, mỗi ngày chỉ xuất hiện 1 lần
    public List<LocalDate> layCacNgayDaDiemDanh(long lopHocId) {
        List<LocalDate> cacNgay = new ArrayList<>();
        for (DiemDanh item : list) {
            if (item.getLopHocId() == lopHocId && !cacNgay.contains(item.getNgay())) {
                cacNgay.add(item.getNgay());
            }
        }
        cacNgay.sort((a, b) -> a.compareTo(b));
        return cacNgay;
    }

    public List<DiemDanh> layDiemDanhCuaHocVien(long lopHocId, long hocVienId) {
        List<DiemDanh> ketQua = new ArrayList<>();
        for (DiemDanh item : list) {
            if (item.getLopHocId() == lopHocId && item.getHocVienId() == hocVienId) {
                ketQua.add(item);
            }
        }
        ketQua.sort((a, b) -> a.getNgay().compareTo(b.getNgay()));
        return ketQua;
    }

    public int demTheoTrangThai(long lopHocId, long hocVienId, TrangThaiDiemDanh trangThai) {
        int dem = 0;
        for (DiemDanh item : layDiemDanhCuaHocVien(lopHocId, hocVienId)) {
            if (item.getTrangThai() == trangThai) {
                dem++;
            }
        }
        return dem;
    }

    // Tỉ lệ có mặt (%) của học viên trong lớp, tính trên số buổi lớp đã điểm danh
    public double tinhTiLeChuyenCan(long lopHocId, long hocVienId) {
        int soBuoi = layCacNgayDaDiemDanh(lopHocId).size();
        if (soBuoi == 0) {
            return 100;
        }
        int coMat = demTheoTrangThai(lopHocId, hocVienId, TrangThaiDiemDanh.CO_MAT);
        return coMat * 100.0 / soBuoi;
    }
}
