package entity;

// Tình trạng đóng học phí của một lượt đăng ký (không lưu vào file, được tính ra từ số tiền)
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
