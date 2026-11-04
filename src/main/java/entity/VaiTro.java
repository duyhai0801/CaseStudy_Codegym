package entity;

// Vai trò của tài khoản đăng nhập, quyết định người dùng thấy menu nào
public enum VaiTro {
    ADMIN("Quan tri vien"),
    GIANG_VIEN("Giang vien"),
    HOC_VIEN("Hoc vien");

    private final String tenHienThi;

    VaiTro(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
