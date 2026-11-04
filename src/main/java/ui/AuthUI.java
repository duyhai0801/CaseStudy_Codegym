package ui;

import entity.User;
import lib.InputHelper;
import service.UserService;
import validator.UserValidator;

// Màn hình đầu tiên: chỉ có ĐĂNG NHẬP. Không cho tự đăng ký - tài khoản do admin cấp.
// Đăng nhập xong thì chuyển tới menu tương ứng với vai trò của tài khoản.
public class AuthUI {
    private static final UserService userService = UserService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== TRUNG TAM TIENG ANH ======");
            System.out.println("1. Dang nhap");
            System.out.println("0. Thoat chuong trinh");
            System.out.println("(Chua co tai khoan? Lien he quan tri vien de duoc cap tai khoan)");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    loginUI();
                    break;
                case 0:
                    System.out.println("Tam biet!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void loginUI() {
        System.out.println("=========== Dang nhap ==========");
        // Đăng nhập không kiểm tra regex (vd mật khẩu admin mặc định là "admin"), chỉ so khớp với dữ liệu
        System.out.println("Nhap username: ");
        String username = InputHelper.inputString();
        System.out.println("Nhap password: ");
        String password = InputHelper.inputString();
        User user = userService.login(username, password);
        if (user == null) {
            System.out.println("Sai username hoac password");
            return;
        }
        System.out.println("Dang nhap thanh cong! Vai tro: " + user.getVaiTro().getTenHienThi());
        switch (user.getVaiTro()) {
            case ADMIN:
                MainUI.showMainMenu(user);
                break;
            case GIANG_VIEN:
                GiangVienMenuUI.showMainMenu(user);
                break;
            case HOC_VIEN:
                HocVienMenuUI.showMainMenu(user);
                break;
            default:
                System.out.println("Vai tro khong hop le");
        }
    }

    // Dùng chung cho cả 3 vai trò
    public static void doiMatKhauUI(User user) {
        System.out.println("====== Doi mat khau ======");
        System.out.println("Nhap mat khau hien tai: ");
        String matKhauCu = InputHelper.inputString();
        if (!matKhauCu.equals(user.getPassword())) {
            System.out.println("Mat khau hien tai khong dung.");
            return;
        }
        System.out.println("Nhap mat khau moi (6-20 ky tu, co it nhat 1 chu va 1 so): ");
        String matKhauMoi = UserValidator.inputPassword();
        System.out.println("Nhap lai mat khau moi: ");
        String xacNhan = InputHelper.inputString();
        if (!matKhauMoi.equals(xacNhan)) {
            System.out.println("Mat khau nhap lai khong khop, chua doi mat khau.");
            return;
        }
        userService.doiMatKhau(user.getId(), matKhauMoi);
        System.out.println("Da doi mat khau!");
    }
}
