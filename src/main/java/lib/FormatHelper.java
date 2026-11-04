package lib;

import java.time.LocalDate;

// Định dạng dữ liệu để in ra màn hình cho dễ đọc
public class FormatHelper {

    // 3600000 -> "3,600,000 VND"
    public static String formatMoney(long soTien) {
        return String.format("%,d VND", soTien);
    }

    // 2026-11-15 -> "15/11/2026"
    public static String formatDate(LocalDate ngay) {
        return ngay.format(InputHelper.DATE_FORMAT);
    }

    // In một dòng gạch ngang để ngăn cách bảng
    public static void printLine(int doDai) {
        System.out.println("-".repeat(doDai));
    }
}
