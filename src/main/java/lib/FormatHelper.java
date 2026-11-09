package lib;

import java.time.LocalDate;

public class FormatHelper {
    public static String formatMoney(long soTien) {
        return String.format("%,d VND", soTien);
    }

    public static String formatDate(LocalDate ngay) {
        return ngay.format(InputHelper.DATE_FORMAT);
    }

    public static void printLine(int doDai) {
        System.out.println("-".repeat(doDai));
    }
}
