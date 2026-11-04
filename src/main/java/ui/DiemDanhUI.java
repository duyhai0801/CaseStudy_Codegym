package ui;

import entity.DangKy;
import entity.DiemDanh;
import entity.LopHoc;
import entity.TrangThaiDiemDanh;
import entity.TrangThaiLop;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.DiemDanhService;
import service.LopHocService;
import validator.CommonValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
Các màn hình điểm danh, dùng chung cho ADMIN và GIẢNG VIÊN:
- giangVienId = null  -> admin, được làm việc với MỌI lớp
- giangVienId = số    -> giảng viên, chỉ được chọn các lớp mình dạy
 */
public class DiemDanhUI {
    private static final DiemDanhService diemDanhService = DiemDanhService.getObject();
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    // Menu riêng cho admin
    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== DIEM DANH & CHUYEN CAN ======");
            System.out.println("1. Diem danh / cap nhat diem danh mot buoi");
            System.out.println("2. Xem diem danh mot buoi");
            System.out.println("3. Tong hop chuyen can cua lop");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    diemDanhUI(null);
                    break;
                case 2:
                    xemBuoiHocUI(null);
                    break;
                case 3:
                    xemChuyenCanLopUI(null);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    // Các lớp người dùng được phép thao tác
    public static List<LopHoc> layLopDuocQuanLy(Long giangVienId, boolean chiLopDangHoc) {
        List<LopHoc> cacLop;
        if (giangVienId == null) {
            cacLop = lopHocService.getAll();
        } else {
            cacLop = lopHocService.locTheoGiangVien(giangVienId);
        }
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc item : cacLop) {
            if (!chiLopDangHoc || item.getTrangThai() == TrangThaiLop.DANG_HOC) {
                ketQua.add(item);
            }
        }
        return ketQua;
    }

    // In danh sách lớp được phép rồi bắt chọn đúng 1 lớp trong đó. Không có lớp nào thì trả về null.
    public static LopHoc chonLop(Long giangVienId, boolean chiLopDangHoc) {
        List<LopHoc> cacLop = layLopDuocQuanLy(giangVienId, chiLopDangHoc);
        if (cacLop.isEmpty()) {
            if (chiLopDangHoc) {
                System.out.println("Khong co lop nao dang hoc.");
            } else {
                System.out.println("Khong co lop nao.");
            }
            return null;
        }
        LopHocUI.inDanhSach(cacLop);
        System.out.println("Nhap id lop: ");
        while (true) {
            long id = InputHelper.inputLong();
            for (LopHoc item : cacLop) {
                if (item.getId() == id) {
                    return item;
                }
            }
            System.out.println("Chi duoc chon lop trong danh sach tren, nhap lai: ");
        }
    }

    public static void diemDanhUI(Long giangVienId) {
        System.out.println("====== Diem danh ======");
        LopHoc lop = chonLop(giangVienId, true);
        if (lop == null) {
            return;
        }
        System.out.println("Nhap ngay diem danh dd/MM/yyyy (Enter = hom nay " + FormatHelper.formatDate(LocalDate.now()) + "): ");
        LocalDate ngay = InputHelper.inputDate(LocalDate.now());
        try {
            diemDanhService.kiemTraCoTheDiemDanh(lop, ngay);
        } catch (NghiepVuException e) {
            System.out.println("Khong the diem danh: " + e.getMessage());
            return;
        }
        if (diemDanhService.daDiemDanh(lop.getId(), ngay)) {
            System.out.println("Buoi " + FormatHelper.formatDate(ngay) + " da duoc diem danh. Cap nhat lai? (y/n)");
            if (!InputHelper.inputYesNo()) {
                return;
            }
        }

        System.out.println("Chon trang thai cho tung hoc vien: 1. Co mat | 2. Vang co phep | 3. Vang khong phep");
        System.out.println("(Nhan Enter = Co mat)");
        TrangThaiDiemDanh[] cacTrangThai = TrangThaiDiemDanh.values();
        List<DiemDanh> danhSach = new ArrayList<>();
        for (DangKy item : dangKyService.layDangKyCuaLop(lop.getId())) {
            System.out.println("- " + HocVienUI.layTenHocVien(item.getHocVienId()) + ": ");
            int luaChon = CommonValidator.inputIntInRange(1, cacTrangThai.length, 1);
            danhSach.add(new DiemDanh(0, lop.getId(), item.getHocVienId(), ngay, cacTrangThai[luaChon - 1]));
        }
        try {
            diemDanhService.luuDiemDanhBuoi(lop, ngay, danhSach);
            int coMat = 0;
            for (DiemDanh item : danhSach) {
                if (item.getTrangThai() == TrangThaiDiemDanh.CO_MAT) {
                    coMat++;
                }
            }
            System.out.println("Da luu diem danh lop " + lop.getTenLop() + " ngay " + FormatHelper.formatDate(ngay)
                    + ": " + coMat + "/" + danhSach.size() + " co mat.");
        } catch (NghiepVuException e) {
            System.out.println("Khong the luu: " + e.getMessage());
        }
    }

    public static void xemBuoiHocUI(Long giangVienId) {
        System.out.println("====== Xem diem danh mot buoi ======");
        LopHoc lop = chonLop(giangVienId, false);
        if (lop == null) {
            return;
        }
        List<LocalDate> cacNgay = diemDanhService.layCacNgayDaDiemDanh(lop.getId());
        if (cacNgay.isEmpty()) {
            System.out.println("Lop " + lop.getTenLop() + " chua co buoi nao duoc diem danh.");
            return;
        }
        System.out.println("Cac buoi da diem danh:");
        for (int i = 0; i < cacNgay.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + FormatHelper.formatDate(cacNgay.get(i)));
        }
        System.out.println("Chon buoi (Enter = buoi gan nhat): ");
        int luaChon = CommonValidator.inputIntInRange(1, cacNgay.size(), cacNgay.size());
        LocalDate ngay = cacNgay.get(luaChon - 1);

        System.out.println("Lop " + lop.getTenLop() + " - buoi " + FormatHelper.formatDate(ngay) + ":");
        String format = "%-11s| %-20s| %s%n";
        System.out.printf(format, "ID HV", "Hoc vien", "Trang thai");
        FormatHelper.printLine(55);
        for (DiemDanh item : diemDanhService.layDiemDanhBuoi(lop.getId(), ngay)) {
            System.out.printf(format, item.getHocVienId(), HocVienUI.layTenHocVien(item.getHocVienId()),
                    item.getTrangThai().getTenHienThi());
        }
    }

    public static void xemChuyenCanLopUI(Long giangVienId) {
        System.out.println("====== Tong hop chuyen can ======");
        LopHoc lop = chonLop(giangVienId, false);
        if (lop == null) {
            return;
        }
        int soBuoi = diemDanhService.layCacNgayDaDiemDanh(lop.getId()).size();
        System.out.println("Lop " + lop.getTenLop() + " - da diem danh " + soBuoi + " buoi:");
        String format = "%-11s| %-20s| %-7s| %-8s| %-8s| %-7s| %s%n";
        System.out.printf(format, "ID HV", "Hoc vien", "Co mat", "Vang CP", "Vang KP", "Ti le", "Ghi chu");
        FormatHelper.printLine(100);
        for (DangKy item : dangKyService.layDangKyCuaLop(lop.getId())) {
            long hocVienId = item.getHocVienId();
            double tiLe = diemDanhService.tinhTiLeChuyenCan(lop.getId(), hocVienId);
            String ghiChu = "";
            if (soBuoi > 0 && tiLe < DiemDanhService.TI_LE_CHUYEN_CAN_TOI_THIEU) {
                ghiChu = "Vang nhieu - can nhac nho";
            }
            System.out.printf(format, hocVienId, HocVienUI.layTenHocVien(hocVienId),
                    diemDanhService.demTheoTrangThai(lop.getId(), hocVienId, TrangThaiDiemDanh.CO_MAT),
                    diemDanhService.demTheoTrangThai(lop.getId(), hocVienId, TrangThaiDiemDanh.VANG_CO_PHEP),
                    diemDanhService.demTheoTrangThai(lop.getId(), hocVienId, TrangThaiDiemDanh.VANG_KHONG_PHEP),
                    hienThiTiLe(soBuoi, tiLe), ghiChu);
        }
    }

    // Lớp chưa điểm danh buổi nào thì in "-" thay vì tỉ lệ
    private static String hienThiTiLe(int soBuoi, double tiLe) {
        if (soBuoi == 0) {
            return "-";
        }
        return String.format("%.0f%%", tiLe);
    }

    // Dành cho học viên: chuyên cần của chính mình ở tất cả các lớp đã đăng ký
    public static void xemChuyenCanCuaHocVienUI(long hocVienId) {
        System.out.println("====== Chuyen can cua toi ======");
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaHocVien(hocVienId);
        if (cacDangKy.isEmpty()) {
            System.out.println("Ban chua dang ky lop nao.");
            return;
        }
        String format = "%-14s| %-8s| %-7s| %-8s| %-8s| %s%n";
        System.out.printf(format, "Lop", "So buoi", "Co mat", "Vang CP", "Vang KP", "Ti le");
        FormatHelper.printLine(65);
        for (DangKy item : cacDangKy) {
            long lopHocId = item.getLopHocId();
            int soBuoi = diemDanhService.layCacNgayDaDiemDanh(lopHocId).size();
            System.out.printf(format, LopHocUI.layTenLop(lopHocId), soBuoi,
                    diemDanhService.demTheoTrangThai(lopHocId, hocVienId, TrangThaiDiemDanh.CO_MAT),
                    diemDanhService.demTheoTrangThai(lopHocId, hocVienId, TrangThaiDiemDanh.VANG_CO_PHEP),
                    diemDanhService.demTheoTrangThai(lopHocId, hocVienId, TrangThaiDiemDanh.VANG_KHONG_PHEP),
                    hienThiTiLe(soBuoi, diemDanhService.tinhTiLeChuyenCan(lopHocId, hocVienId)));
        }

        System.out.println();
        System.out.println("Cac buoi vang:");
        boolean coBuoiVang = false;
        for (DangKy item : cacDangKy) {
            for (DiemDanh diemDanh : diemDanhService.layDiemDanhCuaHocVien(item.getLopHocId(), hocVienId)) {
                if (diemDanh.getTrangThai() != TrangThaiDiemDanh.CO_MAT) {
                    System.out.println("  - " + FormatHelper.formatDate(diemDanh.getNgay()) + " | "
                            + LopHocUI.layTenLop(item.getLopHocId()) + " | " + diemDanh.getTrangThai().getTenHienThi());
                    coBuoiVang = true;
                }
            }
        }
        if (!coBuoiVang) {
            System.out.println("  (Khong vang buoi nao)");
        }
    }
}
