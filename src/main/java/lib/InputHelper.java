package lib;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

// Bọc MỘT Scanner duy nhất cho cả chương trình. Mọi hàm đều đọc bằng nextLine() rồi mới parse,
// nhập sai định dạng thì báo lỗi và bắt nhập lại (tránh lỗi trôi lệnh của nextInt()).
// Các hàm có tham số giaTriCu: dùng khi SỬA, người dùng nhấn Enter thì giữ nguyên giá trị cũ.
public class InputHelper {
    private static final Scanner input = new Scanner(System.in);

    // "uuuu" + STRICT để từ chối ngày không tồn tại như 31/02/2026
    public static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static String inputString() {
        return input.nextLine().trim();
    }

    public static int inputInt() {
        return inputInt(null);
    }

    public static int inputInt(Integer giaTriCu) {
        while (true) {
            String line = inputString();
            if (line.isEmpty() && giaTriCu != null) {
                return giaTriCu;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Gia tri khong hop le, vui long nhap so nguyen:");
            }
        }
    }

    public static long inputLong() {
        return inputLong(null);
    }

    public static long inputLong(Long giaTriCu) {
        while (true) {
            String line = inputString();
            if (line.isEmpty() && giaTriCu != null) {
                return giaTriCu;
            }
            try {
                return Long.parseLong(line);
            } catch (NumberFormatException e) {
                System.out.println("Gia tri khong hop le, vui long nhap so nguyen:");
            }
        }
    }

    public static LocalDate inputDate(LocalDate giaTriCu) {
        while (true) {
            String line = inputString();
            if (line.isEmpty() && giaTriCu != null) {
                return giaTriCu;
            }
            try {
                return LocalDate.parse(line, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("Ngay khong hop le, nhap theo dang dd/MM/yyyy (vd 15/11/2026):");
            }
        }
    }

    // Hỏi có/không, trả về true nếu người dùng nhập y hoặc Y
    public static boolean inputYesNo() {
        String answer = inputString();
        return answer.equalsIgnoreCase("y");
    }
}
