package ui;

import entity.DangKy;
import entity.KhoaHoc;
import entity.LopHoc;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.KhoaHocService;
import service.LopHocService;

import java.util.List;

public class DangKyUI {
    private static final DangKyService dangKyService = DangKyService.getObject();
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final KhoaHocService khoaHocService = KhoaHocService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== DANG KY & HOC PHI ======");
            System.out.println("1. Dang ky hoc vien vao lop");
            System.out.println("2. Huy dang ky");
            System.out.println("3. Dong hoc phi");
            System.out.println("4. Danh sach luot dang ky");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        dangKyUI();
                        break;
                    case 2:
                        huyDangKyUI();
                        break;
                    case 3:
                        dongHocPhiUI();
                        break;
                    case 4:
                        getAllUI();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le");
                }
            } catch (HuyThaoTacException e) {
                System.out.println("Da huy thao tac.");
            }
        } while (choice != 0);
    }

    public static void dangKyUI() {
        System.out.println("====== Dang ky hoc vien vao lop ======");
        System.out.println("--- Chon hoc vien ---");
        long hocVienId = HocVienUI.chonHocVienId();
        System.out.println("--- Cac lop con cho trong ---");
        LopHocUI.inDanhSach(lopHocService.locLopConCho());
        System.out.println("Nhap id lop muon dang ky: ");
        long lopHocId = InputHelper.inputLong();
        try {
            DangKy dangKy = dangKyService.dangKyVaoLop(hocVienId, lopHocId);
            LopHoc lopHoc = lopHocService.findById(lopHocId);
            KhoaHoc khoaHoc = khoaHocService.findById(lopHoc.getKhoaHocId());
            System.out.println("Dang ky thanh cong! Ma dang ky: " + dangKy.getId());
            System.out.println("Hoc phi phai dong: " + FormatHelper.formatMoney(dangKy.getHocPhiPhaiDong()));
            if (dangKy.getHocPhiPhaiDong() < khoaHoc.getHocPhi()) {
                System.out.println("(Da giam " + DangKyService.PHAN_TRAM_GIAM_HOC_VIEN_CU
                        + "% cho hoc vien cu, gia goc " + FormatHelper.formatMoney(khoaHoc.getHocPhi()) + ")");
            }
        } catch (NghiepVuException e) {
            System.out.println("Khong the dang ky: " + e.getMessage());
        }
    }

    public static void huyDangKyUI() {
        System.out.println("====== Huy dang ky ======");
        DangKy dangKy = chonDangKyCuaHocVien();
        if (dangKy == null) {
            return;
        }
        System.out.println("Ban co chac muon huy dang ky lop " + LopHocUI.layTenLop(dangKy.getLopHocId()) + "? (y/n)");
        if (!InputHelper.inputYesNo()) {
            return;
        }
        try {
            DangKy daHuy = dangKyService.huyDangKy(dangKy.getId());
            System.out.println("Da huy dang ky!");
            if (daHuy.getSoTienDaDong() > 0) {
                System.out.println("Can hoan tra cho hoc vien: " + FormatHelper.formatMoney(daHuy.getSoTienDaDong()));
            }
        } catch (NghiepVuException e) {
            System.out.println("Khong the huy: " + e.getMessage());
        }
    }

    public static void dongHocPhiUI() {
        System.out.println("====== Dong hoc phi ======");
        DangKy dangKy = chonDangKyCuaHocVien();
        if (dangKy == null) {
            return;
        }
        System.out.println("Phai dong: " + FormatHelper.formatMoney(dangKy.getHocPhiPhaiDong())
                + " | Da dong: " + FormatHelper.formatMoney(dangKy.getSoTienDaDong())
                + " | Con no: " + FormatHelper.formatMoney(dangKy.tinhSoTienConNo()));
        if (dangKy.tinhSoTienConNo() == 0) {
            System.out.println("Luot dang ky nay da dong du hoc phi.");
            return;
        }
        System.out.println("Nhap so tien dong: ");
        long soTien = InputHelper.inputLong();
        try {
            DangKy daCapNhat = dangKyService.dongHocPhi(dangKy.getId(), soTien);
            System.out.println("Da ghi nhan " + FormatHelper.formatMoney(soTien) + ". Con no: "
                    + FormatHelper.formatMoney(daCapNhat.tinhSoTienConNo())
                    + " (" + daCapNhat.layTrangThaiHocPhi().getTenHienThi() + ")");
        } catch (NghiepVuException e) {
            System.out.println("Khong the dong: " + e.getMessage());
        }
    }

    private static DangKy chonDangKyCuaHocVien() {
        System.out.println("--- Chon hoc vien ---");
        long hocVienId = HocVienUI.chonHocVienId();
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaHocVien(hocVienId);
        if (cacDangKy.isEmpty()) {
            System.out.println("Hoc vien nay chua dang ky lop nao.");
            return null;
        }
        inDanhSach(cacDangKy);
        System.out.println("Nhap ma dang ky: ");
        long dangKyId = InputHelper.inputLong();
        try {
            DangKy dangKy = dangKyService.findById(dangKyId);
            if (dangKy.getHocVienId() != hocVienId) {
                System.out.println("Ma dang ky nay khong thuoc hoc vien da chon.");
                return null;
            }
            return dangKy;
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay ma dang ky " + dangKyId);
            return null;
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach luot dang ky ======");
        inDanhSach(dangKyService.getAll());
    }

    public static void inDanhSach(List<DangKy> list) {
        if (list.isEmpty()) {
            System.out.println("(Danh sach trong)");
            return;
        }
        String format = "%-11s| %-20s| %-14s| %-11s| %16s | %16s | %16s | %s%n";
        System.out.printf(format, "Ma DK", "Hoc vien", "Lop", "Ngay DK", "Phai dong", "Da dong", "Con no", "Trang thai");
        FormatHelper.printLine(132);
        for (DangKy item : list) {
            System.out.printf(format, item.getId(), HocVienUI.layTenHocVien(item.getHocVienId()),
                    LopHocUI.layTenLop(item.getLopHocId()), FormatHelper.formatDate(item.getNgayDangKy()),
                    FormatHelper.formatMoney(item.getHocPhiPhaiDong()), FormatHelper.formatMoney(item.getSoTienDaDong()),
                    FormatHelper.formatMoney(item.tinhSoTienConNo()), item.layTrangThaiHocPhi().getTenHienThi());
        }
    }
}
