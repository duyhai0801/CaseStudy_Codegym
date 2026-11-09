package entity;

public enum TrangThaiDiemDanh {
    CO_MAT("Co mat"),
    VANG_CO_PHEP("Vang co phep"),
    VANG_KHONG_PHEP("Vang khong phep");

    private final String tenHienThi;

    TrangThaiDiemDanh(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
