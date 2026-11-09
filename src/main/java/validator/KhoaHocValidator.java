package validator;

public class KhoaHocValidator {
    private static final String TEN_KHOA_HOC_REGEX = "^(?=.*[a-zA-ZÀ-ỹ])[a-zA-Z0-9À-ỹ .+\\-]{3,40}$";

    public static String inputTenKhoaHoc(String giaTriCu) {
        return CommonValidator.inputByRegex(TEN_KHOA_HOC_REGEX,
                "Ten khoa hoc khong hop le (3-40 ky tu, khong chua dau phay), nhap lai:", giaTriCu);
    }

    public static int inputSoBuoi(Integer giaTriCu) {
        return CommonValidator.inputIntInRange(1, 200, giaTriCu);
    }

    public static long inputHocPhi(Long giaTriCu) {
        return CommonValidator.inputLongInRange(100_000, 100_000_000, giaTriCu);
    }
}
