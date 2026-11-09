package validator;

import lib.InputHelper;

import java.time.LocalDate;
import java.time.Period;

public class NguoiValidator {
    private static final String HO_TEN_REGEX = "^(?=.*[a-zA-ZÀ-ỹ])[a-zA-ZÀ-ỹ ]{2,50}$";
    private static final String SO_DIEN_THOAI_REGEX = "^0\\d{9}$";
    private static final String EMAIL_REGEX = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$";
    private static final String CHUYEN_MON_REGEX = "^(?=.*[a-zA-ZÀ-ỹ])[a-zA-Z0-9À-ỹ .+\\-]{2,30}$";

    public static String inputHoTen(String giaTriCu) {
        return CommonValidator.inputByRegex(HO_TEN_REGEX,
                "Ho ten khong hop le (2-50 ky tu, chi gom chu cai va khoang trang), nhap lai:", giaTriCu);
    }

    public static String inputSoDienThoai(String giaTriCu) {
        return CommonValidator.inputByRegex(SO_DIEN_THOAI_REGEX,
                "So dien thoai khong hop le (10 chu so, bat dau bang 0), nhap lai:", giaTriCu);
    }

    public static String inputEmail(String giaTriCu) {
        return CommonValidator.inputByRegex(EMAIL_REGEX,
                "Email khong hop le (vd: ten@gmail.com), nhap lai:", giaTriCu);
    }

    public static String inputChuyenMon(String giaTriCu) {
        return CommonValidator.inputByRegex(CHUYEN_MON_REGEX,
                "Chuyen mon khong hop le (2-30 ky tu, khong chua dau phay), nhap lai:", giaTriCu);
    }

    public static int inputSoNamKinhNghiem(Integer giaTriCu) {
        return CommonValidator.inputIntInRange(0, 50, giaTriCu);
    }

    public static LocalDate inputNgaySinh(LocalDate giaTriCu) {
        while (true) {
            LocalDate ngaySinh = InputHelper.inputDate(giaTriCu);
            if (!ngaySinh.isBefore(LocalDate.now())) {
                System.out.println("Ngay sinh phai o qua khu, nhap lai:");
                continue;
            }
            int tuoi = Period.between(ngaySinh, LocalDate.now()).getYears();
            if (tuoi < 6 || tuoi > 100) {
                System.out.println("Hoc vien phai tu 6 den 100 tuoi (hien tai: " + tuoi + "), nhap lai:");
                continue;
            }
            return ngaySinh;
        }
    }
}
