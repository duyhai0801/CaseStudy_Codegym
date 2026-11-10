package ui;

import entity.GiangVien;
import entity.User;
import entity.VaiTro;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.GiangVienService;
import validator.NguoiValidator;

import java.util.List;

public class GiangVienUI {
    private static final GiangVienService giangVienService = GiangVienService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY GIANG VIEN ======");
            inDanhSach(giangVienService.getAll());
            System.out.println("1. Them giang vien");
            System.out.println("2. Sua thong tin giang vien");
            System.out.println("3. Xoa giang vien");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        addUI();
                        break;
                    case 2:
                        editUI();
                        break;
                    case 3:
                        deleteUI();
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

    public static long addUI() {
        System.out.println("====== Them giang vien ======");
        long id = giangVienService.taoIdMoi();
        System.out.println("Nhap ho ten: ");
        String hoTen = NguoiValidator.inputHoTen(null);
        System.out.println("Nhap so dien thoai: ");
        String soDienThoai = NguoiValidator.inputSoDienThoai(null);
        System.out.println("Nhap email: ");
        String email = NguoiValidator.inputEmail(null);
        System.out.println("Nhap chuyen mon (vd IELTS, TOEIC, Giao tiep): ");
        String chuyenMon = NguoiValidator.inputChuyenMon(null);
        System.out.println("Nhap so nam kinh nghiem (1-50): ");
        int soNamKinhNghiem = NguoiValidator.inputSoNamKinhNghiem(null);
        User taiKhoan = TaiKhoanUI.nhapTaiKhoan(VaiTro.GIANG_VIEN, id);
        giangVienService.add(new GiangVien(id, hoTen, soDienThoai, email, chuyenMon, soNamKinhNghiem));
        System.out.println("Da them giang vien, id = " + id);
        TaiKhoanUI.luuTaiKhoan(taiKhoan);
        return id;
    }

    public static void editUI() {
        System.out.println("====== Sua thong tin giang vien ======");
        System.out.println("Nhap id giang vien can sua: ");
        long id = InputHelper.inputLong();
        try {
            GiangVien cu = giangVienService.findById(id);
            System.out.println("(Nhan Enter de giu gia tri cu)");
            System.out.println("Ho ten [" + cu.getHoTen() + "]: ");
            String hoTen = NguoiValidator.inputHoTen(cu.getHoTen());
            System.out.println("So dien thoai [" + cu.getSoDienThoai() + "]: ");
            String soDienThoai = NguoiValidator.inputSoDienThoai(cu.getSoDienThoai());
            System.out.println("Email [" + cu.getEmail() + "]: ");
            String email = NguoiValidator.inputEmail(cu.getEmail());
            System.out.println("Chuyen mon [" + cu.getChuyenMon() + "]: ");
            String chuyenMon = NguoiValidator.inputChuyenMon(cu.getChuyenMon());
            System.out.println("So nam kinh nghiem [" + cu.getSoNamKinhNghiem() + "]: ");
            int soNamKinhNghiem = NguoiValidator.inputSoNamKinhNghiem(cu.getSoNamKinhNghiem());
            giangVienService.update(id, new GiangVien(id, hoTen, soDienThoai, email, chuyenMon, soNamKinhNghiem));
            System.out.println("Da cap nhat thong tin giang vien!");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay giang vien id " + id);
        }
    }

    public static void deleteUI() {
        System.out.println("====== Xoa giang vien ======");
        System.out.println("Nhap id giang vien can xoa: ");
        long id = InputHelper.inputLong();
        try {
            GiangVien giangVien = giangVienService.findById(id);
            System.out.println("Ban co chac muon xoa giang vien '" + giangVien.getHoTen() + "'? (y/n)");
            if (InputHelper.inputYesNo()) {
                giangVienService.delete(id);
                System.out.println("Da xoa giang vien va tai khoan dang nhap cua giang vien nay!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay giang vien id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    public static void inDanhSach(List<GiangVien> list) {
        if (list.isEmpty()) {
            System.out.println("(Danh sach trong)");
            return;
        }
        String format = "%-11s| %-20s| %-11s| %-24s| %-12s| %s%n";
        System.out.printf(format, "ID", "Ho ten", "SDT", "Email", "Chuyen mon", "Kinh nghiem");
        FormatHelper.printLine(102);
        for (GiangVien item : list) {
            System.out.printf(format, item.getId(), item.getHoTen(), item.getSoDienThoai(), item.getEmail(),
                    item.getChuyenMon(), item.getSoNamKinhNghiem() + " nam");
        }
    }

    public static long chonGiangVienId(Long giaTriCu) {
        inDanhSach(giangVienService.getAll());
        System.out.println("Nhap id giang vien: ");
        while (true) {
            long id = InputHelper.inputLong(giaTriCu);
            try {
                giangVienService.findById(id);
                return id;
            } catch (DataNotFoundException e) {
                System.out.println("Id giang vien khong ton tai, nhap lai: ");
            }
        }
    }

    public static String layTenGiangVien(long id) {
        try {
            return giangVienService.findById(id).getHoTen();
        } catch (DataNotFoundException e) {
            return "(khong ton tai)";
        }
    }
}
