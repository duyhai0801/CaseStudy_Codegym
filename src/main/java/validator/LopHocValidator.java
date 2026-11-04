package validator;

import entity.TrangThaiLop;
import lib.InputHelper;

import java.time.LocalDate;

// Kiểm tra dữ liệu nhập cho LopHoc
public class LopHocValidator {
    // vd "IELTS55-K02"
    private static final String TEN_LOP_REGEX = "^(?=.*[a-zA-Z])[a-zA-Z0-9À-ỹ .\\-]{2,30}$";
    // vd "T2-T4-T6 18:00-19:30" - không cho dấu phẩy vì file CSV tách cột bằng dấu phẩy
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

    public static TrangThaiLop inputTrangThai(TrangThaiLop giaTriCu) {
        TrangThaiLop[] cacTrangThai = TrangThaiLop.values();
        for (int i = 0; i < cacTrangThai.length; i++) {
            System.out.println("  " + (i + 1) + ". " + cacTrangThai[i].getTenHienThi());
        }
        Integer luaChonCu = null;
        if (giaTriCu != null) {
            luaChonCu = giaTriCu.ordinal() + 1;
        }
        int luaChon = CommonValidator.inputIntInRange(1, cacTrangThai.length, luaChonCu);
        return cacTrangThai[luaChon - 1];
    }
}
