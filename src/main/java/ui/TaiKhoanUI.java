package ui;

import entity.User;
import entity.VaiTro;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import lib.errors.UserIsExist;
import service.UserService;
import validator.CommonValidator;
import validator.UserValidator;

import java.util.List;

// Menu quản lý tài khoản (chỉ ADMIN vào được): cấp tài khoản cho giảng viên / học viên,
// xem danh sách, đặt lại mật khẩu, xóa tài khoản
public class TaiKhoanUI {
    private static final UserService userService = UserService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY TAI KHOAN ======");
            System.out.println("1. Cap tai khoan moi (giang vien / hoc vien)");
            System.out.println("2. Xem danh sach tai khoan");
            System.out.println("3. Dat lai mat khau");
            System.out.println("4. Xoa tai khoan");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    capTaiKhoanUI();
                    break;
                case 2:
                    getAllUI();
                    break;
                case 3:
                    datLaiMatKhauUI();
                    break;
                case 4:
                    deleteUI();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void capTaiKhoanUI() {
        System.out.println("====== Cap tai khoan moi ======");
        System.out.println("Tai khoan nay danh cho:");
        System.out.println("  1. Giang vien");
        System.out.println("  2. Hoc vien");
        int luaChon = CommonValidator.inputIntInRange(1, 2, null);
        VaiTro vaiTro;
        long nguoiId;
        if (luaChon == 1) {
            vaiTro = VaiTro.GIANG_VIEN;
            System.out.println("--- Chon giang vien ---");
            nguoiId = GiangVienUI.chonGiangVienId(null);
        } else {
            vaiTro = VaiTro.HOC_VIEN;
            System.out.println("--- Chon hoc vien ---");
            nguoiId = HocVienUI.chonHocVienId();
        }
        // Kiểm tra trước (người này đã có tài khoản chưa) để khỏi bắt nhập username/password vô ích
        try {
            userService.kiemTraCoTheCapTaiKhoan(vaiTro, nguoiId);
        } catch (NghiepVuException e) {
            System.out.println("Khong the cap tai khoan: " + e.getMessage());
            return;
        }
        System.out.println("Nhap username (5-20 ky tu, chi gom chu, so, dau gach duoi): ");
        String username = UserValidator.inputUsername();
        System.out.println("Nhap password (6-20 ky tu, co it nhat 1 chu va 1 so): ");
        String password = UserValidator.inputPassword();
        User user = new User(userService.taoIdMoi(), username, password, vaiTro, nguoiId);
        try {
            userService.taoTaiKhoan(user);
            System.out.println("Da cap tai khoan cho " + vaiTro.getTenHienThi().toLowerCase() + " "
                    + layTenNguoiDung(user) + ".");
            System.out.println("Hay gui cho ho: username = " + username + " | password = " + password);
        } catch (UserIsExist e) {
            System.out.println("Username '" + username + "' da ton tai, hay chon username khac.");
        } catch (NghiepVuException e) {
            System.out.println("Khong the cap tai khoan: " + e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach tai khoan ======");
        inDanhSach(userService.getAll());
    }

    public static void datLaiMatKhauUI() {
        System.out.println("====== Dat lai mat khau ======");
        inDanhSach(userService.getAll());
        System.out.println("Nhap id tai khoan: ");
        long id = InputHelper.inputLong();
        try {
            User user = userService.findById(id);
            System.out.println("Nhap mat khau moi cho '" + user.getUsername() + "' (6-20 ky tu, co chu va so): ");
            String matKhauMoi = UserValidator.inputPassword();
            userService.doiMatKhau(id, matKhauMoi);
            System.out.println("Da dat lai mat khau. Hay gui mat khau moi cho nguoi dung.");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay tai khoan id " + id);
        }
    }

    public static void deleteUI() {
        System.out.println("====== Xoa tai khoan ======");
        inDanhSach(userService.getAll());
        System.out.println("Nhap id tai khoan can xoa: ");
        long id = InputHelper.inputLong();
        try {
            User user = userService.findById(id);
            System.out.println("Ban co chac muon xoa tai khoan '" + user.getUsername() + "'? (y/n)");
            if (InputHelper.inputYesNo()) {
                userService.xoaTaiKhoan(id);
                System.out.println("Da xoa tai khoan!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay tai khoan id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    // Không in mật khẩu ra màn hình
    public static void inDanhSach(List<User> list) {
        String format = "%-11s| %-20s| %-14s| %s%n";
        System.out.printf(format, "ID", "Username", "Vai tro", "Nguoi su dung");
        FormatHelper.printLine(70);
        for (User item : list) {
            System.out.printf(format, item.getId(), item.getUsername(), item.getVaiTro().getTenHienThi(),
                    layTenNguoiDung(item));
        }
    }

    public static String layTenNguoiDung(User user) {
        switch (user.getVaiTro()) {
            case GIANG_VIEN:
                return GiangVienUI.layTenGiangVien(user.getNguoiId());
            case HOC_VIEN:
                return HocVienUI.layTenHocVien(user.getNguoiId());
            default:
                return "-";
        }
    }
}
