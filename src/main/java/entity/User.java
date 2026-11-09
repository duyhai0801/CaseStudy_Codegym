package entity;

public class User {
    private long id;
    private String username;
    private String password;
    private VaiTro vaiTro;
    private long nguoiId;

    public User() {
    }

    public User(long id, String username, String password, VaiTro vaiTro, long nguoiId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.vaiTro = vaiTro;
        this.nguoiId = nguoiId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(VaiTro vaiTro) {
        this.vaiTro = vaiTro;
    }

    public long getNguoiId() {
        return nguoiId;
    }

    public void setNguoiId(long nguoiId) {
        this.nguoiId = nguoiId;
    }
}
