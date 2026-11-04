package lib.errors;

// Ném ra khi một thao tác vi phạm quy tắc nghiệp vụ (lớp đã đủ sĩ số, xóa dữ liệu đang được dùng...).
// message chứa lý do để UI in ra cho người dùng.
public class NghiepVuException extends RuntimeException {
    public NghiepVuException(String message) {
        super(message);
    }
}
