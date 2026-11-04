package ui;

import entity.LopHoc;
import lib.FormatHelper;
import lib.InputHelper;
import service.DangKyService;
import service.HocVienService;
import service.LopHocService;
import service.ThongKeService;

import java.util.List;
import java.util.Map;

// Menu thống kê: tổng học phí, học viên còn nợ, sĩ số các lớp, xếp hạng khóa học, số lớp của giảng viên
public class ThongKeUI {
    private static final ThongKeService thongKeService = ThongKeService.getObject();
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();
    private static final HocVienService hocVienService = HocVienService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== THONG KE ======");
            System.out.println("1. Tong quan hoc phi");
            System.out.println("2. Danh sach hoc vien con no");
            System.out.println("3. Si so va ti le lap day tung lop");
            System.out.println("4. Xep hang khoa hoc theo so hoc vien");
            System.out.println("5. So lop dang day cua tung giang vien");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    tongQuanHocPhiUI();
                    break;
                case 2:
                    hocVienConNoUI();
                    break;
                case 3:
                    siSoTungLopUI();
                    break;
                case 4:
                    xepHangKhoaHocUI();
                    break;
                case 5:
                    soLopCuaGiangVienUI();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void tongQuanHocPhiUI() {
        long daThu = thongKeService.tinhTongDaThu();
        long conNo = thongKeService.tinhTongConNo();
        long tongPhaiThu = daThu + conNo;
        double tiLe = 0;
        if (tongPhaiThu > 0) {
            tiLe = daThu * 100.0 / tongPhaiThu;
        }
        System.out.println("====== Tong quan hoc phi ======");
        System.out.println("Tong hoc phi phai thu : " + FormatHelper.formatMoney(tongPhaiThu));
        System.out.println("Da thu                : " + FormatHelper.formatMoney(daThu));
        System.out.println("Con no                : " + FormatHelper.formatMoney(conNo));
        System.out.printf("Ti le da thu          : %.1f%%%n", tiLe);
    }

    public static void hocVienConNoUI() {
        System.out.println("====== Hoc vien con no hoc phi (no nhieu nhat o tren) ======");
        List<Map.Entry<Long, Long>> danhSach = thongKeService.thongKeNoTheoHocVien();
        if (danhSach.isEmpty()) {
            System.out.println("Khong co hoc vien nao con no.");
            return;
        }
        String format = "%-11s| %-20s| %-11s| %16s%n";
        System.out.printf(format, "ID", "Hoc vien", "SDT", "Con no");
        FormatHelper.printLine(67);
        for (Map.Entry<Long, Long> entry : danhSach) {
            long hocVienId = entry.getKey();
            String soDienThoai = hocVienService.findById(hocVienId).getSoDienThoai();
            System.out.printf(format, hocVienId, HocVienUI.layTenHocVien(hocVienId), soDienThoai,
                    FormatHelper.formatMoney(entry.getValue()));
        }
    }

    public static void siSoTungLopUI() {
        System.out.println("====== Si so va ti le lap day ======");
        String format = "%-14s| %-18s| %-15s| %-6s| %s%n";
        System.out.printf(format, "Lop", "Khoa hoc", "Trang thai", "Si so", "Ti le lap day");
        FormatHelper.printLine(92);
        for (LopHoc item : lopHocService.getAll()) {
            int siSo = dangKyService.demSoHocVienCuaLop(item.getId());
            double tiLe = thongKeService.tinhTiLeLapDay(item);
            System.out.printf(format, item.getTenLop(), KhoaHocUI.layTenKhoaHoc(item.getKhoaHocId()),
                    item.getTrangThai().getTenHienThi(), siSo + "/" + item.getSiSoToiDa(),
                    veThanhTienDo(tiLe) + String.format(" %.0f%%", tiLe));
        }
    }

    // Vẽ thanh tiến độ 20 ô, vd 50% -> [##########----------]
    private static String veThanhTienDo(double phanTram) {
        int soO = (int) Math.round(phanTram / 5);
        if (soO > 20) {
            soO = 20;
        }
        return "[" + "#".repeat(soO) + "-".repeat(20 - soO) + "]";
    }

    public static void xepHangKhoaHocUI() {
        System.out.println("====== Xep hang khoa hoc theo so luot dang ky ======");
        String format = "%-6s| %-22s| %s%n";
        System.out.printf(format, "Hang", "Khoa hoc", "So luot dang ky");
        FormatHelper.printLine(50);
        int hang = 1;
        for (Map.Entry<Long, Integer> entry : thongKeService.xepHangKhoaHocTheoSoHocVien()) {
            System.out.printf(format, hang, KhoaHocUI.layTenKhoaHoc(entry.getKey()), entry.getValue());
            hang++;
        }
    }

    public static void soLopCuaGiangVienUI() {
        System.out.println("====== So lop dang day (chua ket thuc) cua tung giang vien ======");
        String format = "%-11s| %-20s| %s%n";
        System.out.printf(format, "ID", "Giang vien", "So lop dang day");
        FormatHelper.printLine(52);
        for (Map.Entry<Long, Integer> entry : thongKeService.demSoLopDangDayTheoGiangVien()) {
            System.out.printf(format, entry.getKey(), GiangVienUI.layTenGiangVien(entry.getKey()), entry.getValue());
        }
    }
}
