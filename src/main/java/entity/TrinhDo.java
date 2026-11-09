package entity;

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
