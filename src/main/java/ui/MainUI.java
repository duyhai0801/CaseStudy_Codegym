package ui;

import entity.User;
import lib.InputHelper;
import lib.errors.HuyThaoTacException;

public class MainUI {
    public static void showMainMenu(User user) {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN TRI VIEN (xin chao " + user.getUsername() + ") ======");
            System.out.println("1. Quan ly khoa hoc");
            System.out.println("2. Quan ly giang vien");
            System.out.println("3. Quan ly lop hoc");
            System.out.println("4. Quan ly hoc vien");
            System.out.println("5. Dang ky & hoc phi");
            System.out.println("6. Thong ke");
            System.out.println("7. Xem diem danh & chuyen can");
            System.out.println("8. Quan ly tai khoan");
            System.out.println("9. Doi mat khau");
            System.out.println("0. Dang xuat");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        KhoaHocUI.showMainMenu();
                        break;
                    case 2:
                        GiangVienUI.showMainMenu();
                        break;
                    case 3:
                        LopHocUI.showMainMenu();
                        break;
                    case 4:
                        HocVienUI.showMainMenu();
                        break;
                    case 5:
                        DangKyUI.showMainMenu();
                        break;
                    case 6:
                        ThongKeUI.showMainMenu();
                        break;
                    case 7:
                        DiemDanhUI.showMainMenu();
                        break;
                    case 8:
                        TaiKhoanUI.showMainMenu();
                        break;
                    case 9:
                        AuthUI.doiMatKhauUI(user);
                        break;
                    case 0:
                        System.out.println("Da dang xuat.");
                        break;
                    default:
                        System.out.println("Lua chon khong hop le");
                }
            } catch (HuyThaoTacException e) {
                System.out.println("Da huy thao tac.");
            }
        } while (choice != 0);
    }
}
