package service;

import entity.DangKy;
import entity.DiemDanh;
import entity.LopHoc;
import entity.TrangThaiDiemDanh;
import entity.TrangThaiLop;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.DiemDanhRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiemDanhService implements IService<DiemDanh> {
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

    public void kiemTraCoTheDiemDanh(LopHoc lop, LocalDate ngay) {
        if (lop.getTrangThai() != TrangThaiLop.DANG_HOC) {
            throw new NghiepVuException("Chi diem danh duoc lop dang hoc (lop " + lop.getTenLop() + " dang o trang thai '"
                    + lop.getTrangThai().getTenHienThi() + "').");
        }
        if (ngay.isBefore(lop.getNgayKhaiGiang())) {
            throw new NghiepVuException("Ngay diem danh truoc ngay khai giang cua lop.");
        }
        if (layHocVienCanDiemDanh(lop.getId(), ngay).isEmpty()) {
            throw new NghiepVuException("Lop chua co hoc vien nao dang ky tu ngay nay tro ve truoc.");
        }
    }

    public List<DangKy> layHocVienCanDiemDanh(long lopHocId, LocalDate ngay) {
        List<DangKy> ketQua = new ArrayList<>();
        for (DangKy item : DangKyService.getObject().layDangKyCuaLop(lopHocId)) {
            if (!item.getNgayDangKy().isAfter(ngay)) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    public List<DayOfWeek> layCacThuCoLich(LopHoc lop) {
        List<DayOfWeek> cacThu = new ArrayList<>();
        String[] cacPhan = lop.getLichHoc().toUpperCase().split("[^A-Z0-9]+");
        for (String phan : cacPhan) {
            DayOfWeek thu = null;
            switch (phan) {
                case "T2":
                    thu = DayOfWeek.MONDAY;
                    break;
                case "T3":
                    thu = DayOfWeek.TUESDAY;
                    break;
                case "T4":
                    thu = DayOfWeek.WEDNESDAY;
                    break;
                case "T5":
                    thu = DayOfWeek.THURSDAY;
                    break;
                case "T6":
                    thu = DayOfWeek.FRIDAY;
                    break;
                case "T7":
                    thu = DayOfWeek.SATURDAY;
                    break;
                case "CN":
                    thu = DayOfWeek.SUNDAY;
                    break;
                default:
                    break;
            }
            if (thu != null && !cacThu.contains(thu)) {
                cacThu.add(thu);
            }
        }
        return cacThu;
    }

    public boolean laNgayCoLich(LopHoc lop, LocalDate ngay) {
        List<DayOfWeek> cacThu = layCacThuCoLich(lop);
        if (cacThu.isEmpty()) {
            return true;
        }
        return cacThu.contains(ngay.getDayOfWeek());
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

    public void luuDiemDanhBuoi(LopHoc lop, LocalDate ngay, List<DiemDanh> danhSachMoi) {
        kiemTraCoTheDiemDanh(lop, ngay);
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

    public int demSoBuoiCuaHocVien(long lopHocId, long hocVienId) {
        return layDiemDanhCuaHocVien(lopHocId, hocVienId).size();
    }

    public double tinhTiLeChuyenCan(long lopHocId, long hocVienId) {
        int soBuoi = demSoBuoiCuaHocVien(lopHocId, hocVienId);
        if (soBuoi == 0) {
            return 100;
        }
        int coMat = demTheoTrangThai(lopHocId, hocVienId, TrangThaiDiemDanh.CO_MAT);
        return coMat * 100.0 / soBuoi;
    }
}
