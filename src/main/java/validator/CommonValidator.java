package validator;

import entity.TrinhDo;
import lib.InputHelper;

import java.util.regex.Pattern;

// Các hàm kiểm tra dùng chung cho nhiều validator.
// Tham số giaTriCu: khác null khi đang SỬA, người dùng nhấn Enter thì giữ giá trị cũ.
public class CommonValidator {

    // Nhập chuỗi đến khi khớp regex thì thôi (giống ProductValidator.inputName() của bài mẫu)
    public static String inputByRegex(String regex, String thongBaoLoi, String giaTriCu) {
        while (true) {
            String value = InputHelper.inputString();
            if (value.isEmpty() && giaTriCu != null) {
                return giaTriCu;
            }
            if (Pattern.matches(regex, value)) {
                return value;
            }
            System.out.println(thongBaoLoi);
        }
    }

    public static int inputIntInRange(int min, int max, Integer giaTriCu) {
        while (true) {
            int value = InputHelper.inputInt(giaTriCu);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Gia tri phai tu " + min + " den " + max + ", nhap lai:");
        }
    }

    public static long inputLongInRange(long min, long max, Long giaTriCu) {
        while (true) {
            long value = InputHelper.inputLong(giaTriCu);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Gia tri phai tu " + min + " den " + max + ", nhap lai:");
        }
    }

    // In danh sách trình độ đánh số 1, 2, 3 rồi cho người dùng chọn số
    public static TrinhDo inputTrinhDo(TrinhDo giaTriCu) {
        TrinhDo[] cacTrinhDo = TrinhDo.values();
        for (int i = 0; i < cacTrinhDo.length; i++) {
            System.out.println("  " + (i + 1) + ". " + cacTrinhDo[i].getTenHienThi());
        }
        Integer luaChonCu = null;
        if (giaTriCu != null) {
            luaChonCu = giaTriCu.ordinal() + 1; // ordinal() là vị trí của hằng trong enum, bắt đầu từ 0
        }
        int luaChon = inputIntInRange(1, cacTrinhDo.length, luaChonCu);
        return cacTrinhDo[luaChon - 1];
    }
}
