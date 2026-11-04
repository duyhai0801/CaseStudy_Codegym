package entity;

// Lớp cha trừu tượng của GiangVien và HocVien: gom các thuộc tính chung của một người
public abstract class Nguoi {
    private long id;
    private String hoTen;
    private String soDienThoai;
    private String email;

    public Nguoi() {
    }

    public Nguoi(long id, String hoTen, String soDienThoai, String email) {
        this.id = id;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // Mỗi lớp con tự trả về vai trò của mình (đa hình)
    public abstract String layVaiTro();

    // Lấy "tên" theo kiểu người Việt: chữ cuối của họ tên. Vd "Nguyen Van An" -> "An"
    public String layTen() {
        String[] cacChu = hoTen.trim().split(" ");
        return cacChu[cacChu.length - 1];
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
