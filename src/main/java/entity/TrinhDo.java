package entity;

// Trình độ của khóa học và trình độ đầu vào của học viên
public enum TrinhDo {
    CO_BAN("Co ban"),
    TRUNG_CAP("Trung cap"),
    NANG_CAO("Nang cao");

    private final String tenHienThi;

    TrinhDo(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
