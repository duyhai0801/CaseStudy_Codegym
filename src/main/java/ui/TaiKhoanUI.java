package ui;

import entity.User;
import entity.VaiTro;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import lib.errors.UserIsExist;
import service.UserService;
import validator.CommonValidator;
import validator.UserValidator;

import java.util.List;

public class TaiKhoanUI {
    private static final UserService userService = UserService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY TAI KHOAN ======");
            System.out.println("1. Cap tai khoan moi (giang vien / hoc vien)");
            System.out.println("2. Danh sach tai khoan");
            System.out.println("3. Dat lai mat khau");
            System.out.println("4. Xoa tai khoan");
            System.out.println("5. Xem quyen han cua tung vai tro");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
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
                    case 5:
                        xemQuyenHanUI();
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

    public static void capTaiKhoanUI() {
        System.out.println("====== Cap tai khoan moi ======");
        System.out.println("Tai khoan nay danh cho:");
        System.out.println("  1. Giang vien");
        System.out.println("  2. Hoc vien");
        System.out.println("  0. Quay lai");
        int luaChon = CommonValidator.inputIntInRange(1, 2, null);
        VaiTro vaiTro;
        if (luaChon == 1) {
            vaiTro = VaiTro.GIANG_VIEN;
        } else {
            vaiTro = VaiTro.HOC_VIEN;
        }
        String tenVaiTro = vaiTro.getTenHienThi().toLowerCase();
        System.out.println("Nguoi dung tai khoan:");
        System.out.println("  1. Tao " + tenVaiTro + " moi");
        System.out.println("  2. Chon " + tenVaiTro + " da co trong danh sach");
        System.out.println("  0. Quay lai");
        int cachChon = CommonValidator.inputIntInRange(1, 2, null);
        if (cachChon == 1) {
            if (vaiTro == VaiTro.GIANG_VIEN) {
                GiangVienUI.addUI();
            } else {
                HocVienUI.addUI();
            }
            return;
        }
        long nguoiId;
        if (vaiTro == VaiTro.GIANG_VIEN) {
            System.out.println("--- Chon giang vien ---");
            nguoiId = GiangVienUI.chonGiangVienId(null);
        } else {
            System.out.println("--- Chon hoc vien ---");
            nguoiId = HocVienUI.chonHocVienId();
        }
        try {
            userService.kiemTraCoTheCapTaiKhoan(vaiTro, nguoiId);
        } catch (NghiepVuException e) {
            System.out.println("Khong the cap tai khoan: " + e.getMessage());
            return;
        }
        luuTaiKhoan(nhapTaiKhoan(vaiTro, nguoiId));
    }

    public static User nhapTaiKhoan(VaiTro vaiTro, long nguoiId) {
        System.out.println("--- Tao tai khoan dang nhap ---");
        System.out.println("Nhap username (5-20 ky tu, chi gom chu, so, dau gach duoi): ");
        String username = UserValidator.inputUsername();
        while (userService.daCoUsername(username)) {
            System.out.println("Username '" + username + "' da ton tai, nhap username khac: ");
            username = UserValidator.inputUsername();
        }
        System.out.println("Nhap password (6-20 ky tu, co it nhat 1 chu va 1 so): ");
        String password = UserValidator.inputPassword();
        return new User(userService.taoIdMoi(), username, password, vaiTro, nguoiId);
    }

    public static void luuTaiKhoan(User user) {
        try {
            userService.taoTaiKhoan(user);
            System.out.println("Da tao tai khoan cho " + user.getVaiTro().getTenHienThi().toLowerCase() + " "
                    + layTenNguoiDung(user) + ".");
            System.out.println("Hay gui cho ho: username = " + user.getUsername() + " | password = " + user.getPassword());
            inQuyenHan(user.getVaiTro());
        } catch (UserIsExist e) {
            System.out.println("Username '" + user.getUsername() + "' da ton tai, chua tao duoc tai khoan.");
        } catch (NghiepVuException e) {
            System.out.println("Khong the tao tai khoan: " + e.getMessage());
        }
    }

    public static void xemQuyenHanUI() {
        System.out.println("====== Quyen han theo vai tro ======");
        for (VaiTro vaiTro : VaiTro.values()) {
            inQuyenHan(vaiTro);
            System.out.println();
        }
    }

    public static void inQuyenHan(VaiTro vaiTro) {
        System.out.println("Quyen han cua tai khoan " + vaiTro.getTenHienThi().toUpperCase() + ":");
        switch (vaiTro) {
            case ADMIN:
                System.out.println("  - Toan quyen: quan ly khoa hoc, giang vien, lop hoc, hoc vien");
                System.out.println("  - Dang ky hoc, thu hoc phi, xem thong ke");
                System.out.println("  - Xem diem danh va chuyen can moi lop (khong diem danh)");
                System.out.println("  - Cap, dat lai mat khau, xoa tai khoan giang vien va hoc vien");
                break;
            case GIANG_VIEN:
                System.out.println("  - Xem thong tin ca nhan va cac lop minh day");
                System.out.println("  - Xem hoc vien va tinh hinh dong hoc phi cua lop minh");
                System.out.println("  - Diem danh, cap nhat diem danh, xem chuyen can lop minh");
                System.out.println("  - Bat dau lop khi den ngay khai giang, ket thuc lop minh day");
                System.out.println("  - Khong duoc them / xoa du lieu, khong thu hoc phi");
                break;
            case HOC_VIEN:
                System.out.println("  - Xem thong tin ca nhan, cac lop dang hoc");
                System.out.println("  - Xem hoc phi va chuyen can cua chinh minh");
                System.out.println("  - Xem cac lop dang mo, tu dang ky / huy dang ky lop cho chinh minh");
                System.out.println("  - Khong duoc dong hoc phi qua chuong trinh, khong sua du lieu khac");
                break;
            default:
                break;
        }
        System.out.println("  - Doi mat khau cua chinh minh");
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
