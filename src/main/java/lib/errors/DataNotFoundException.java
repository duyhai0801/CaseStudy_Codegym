package lib.errors;

// Ném ra khi tìm theo id mà không thấy (Service ném, UI bắt) - giống bài mẫu
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException() {
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
