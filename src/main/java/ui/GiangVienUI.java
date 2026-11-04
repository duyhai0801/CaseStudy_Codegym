package ui;

import entity.GiangVien;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import service.GiangVienService;
import validator.NguoiValidator;

import java.util.List;

// Menu quản lý giảng viên: thêm, sửa, xóa, xem danh sách
public class GiangVienUI {
    private static final GiangVienService giangVienService = GiangVienService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY GIANG VIEN ======");
            System.out.println("1. Them giang vien");
            System.out.println("2. Sua giang vien");
            System.out.println("3. Xoa giang vien");
            System.out.println("4. Xem danh sach");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
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
                case 4:
                    getAllUI();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void addUI() {
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
        System.out.println("Nhap so nam kinh nghiem (0-50): ");
        int soNamKinhNghiem = NguoiValidator.inputSoNamKinhNghiem(null);
        giangVienService.add(new GiangVien(id, hoTen, soDienThoai, email, chuyenMon, soNamKinhNghiem));
        System.out.println("Da them giang vien, id = " + id);
    }

    public static void editUI() {
        System.out.println("====== Sua giang vien ======");
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
            System.out.println("Da cap nhat giang vien!");
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
                System.out.println("Da xoa giang vien!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay giang vien id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach giang vien ======");
        inDanhSach(giangVienService.getAll());
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
