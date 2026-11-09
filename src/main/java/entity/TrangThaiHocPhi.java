package entity;

public enum TrangThaiHocPhi {
    CHUA_DONG("Chua dong"),
    DONG_MOT_PHAN("Dong mot phan"),
    DA_DONG("Da dong du");

    private final String tenHienThi;

    TrangThaiHocPhi(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
