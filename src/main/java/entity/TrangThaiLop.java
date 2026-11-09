package entity;

public enum TrangThaiLop {
    SAP_KHAI_GIANG("Sap khai giang"),
    DANG_HOC("Dang hoc"),
    DA_KET_THUC("Da ket thuc");

    private final String tenHienThi;

    TrangThaiLop(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
