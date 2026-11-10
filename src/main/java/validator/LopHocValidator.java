package validator;

import lib.InputHelper;

import java.time.LocalDate;

public class LopHocValidator {
    private static final String TEN_LOP_REGEX = "^(?=.*[a-zA-Z])[a-zA-Z0-9À-ỹ .\\-]{2,30}$";
    private static final String LICH_HOC_REGEX = "^[a-zA-Z0-9À-ỹ :\\-/]{3,40}$";

    public static String inputTenLop(String giaTriCu) {
        return CommonValidator.inputByRegex(TEN_LOP_REGEX,
                "Ten lop khong hop le (2-30 ky tu, vd IELTS55-K02), nhap lai:", giaTriCu);
    }

    public static String inputLichHoc(String giaTriCu) {
        return CommonValidator.inputByRegex(LICH_HOC_REGEX,
                "Lich hoc khong hop le (vd T2-T4-T6 18:00-19:30, khong dung dau phay), nhap lai:", giaTriCu);
    }

    public static int inputSiSoToiDa(Integer giaTriCu) {
        return CommonValidator.inputIntInRange(5, 30, giaTriCu);
    }

    public static LocalDate inputNgayKhaiGiang(LocalDate giaTriCu) {
        return InputHelper.inputDate(giaTriCu);
    }
}
