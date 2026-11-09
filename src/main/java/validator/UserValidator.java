package validator;

import lib.InputHelper;

import java.util.regex.Pattern;

public class UserValidator {
    public static String inputUsername() {
        String usernameRegex = "^[A-Za-z0-9_]{5,20}$";
        while (true) {
            String username = InputHelper.inputString();
            if (Pattern.matches(usernameRegex, username)) {
                return username;
            }
            System.out.println("Username khong hop le: 5-20 ky tu, chi gom chu, so va dau gach duoi.");
        }
    }

    public static String inputPassword() {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9_@#$%^&*.]{6,20}$";
        while (true) {
            String password = InputHelper.inputString();
            if (Pattern.matches(passwordRegex, password)) {
                return password;
            }
            System.out.println("Password khong hop le: 6-20 ky tu, co it nhat 1 chu va 1 so.");
        }
    }
}
