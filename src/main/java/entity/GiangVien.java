package entity;

public class GiangVien extends Nguoi {
    private String chuyenMon;
    private int soNamKinhNghiem;

    public GiangVien() {
    }

    public GiangVien(long id, String hoTen, String soDienThoai, String email,
                     String chuyenMon, int soNamKinhNghiem) {
        super(id, hoTen, soDienThoai, email);
        this.chuyenMon = chuyenMon;
        this.soNamKinhNghiem = soNamKinhNghiem;
    }

    @Override
    public String layVaiTro() {
        return "Giang vien";
    }

    public String getChuyenMon() {
        return chuyenMon;
    }

    public void setChuyenMon(String chuyenMon) {
        this.chuyenMon = chuyenMon;
    }

    public int getSoNamKinhNghiem() {
        return soNamKinhNghiem;
    }

    public void setSoNamKinhNghiem(int soNamKinhNghiem) {
        this.soNamKinhNghiem = soNamKinhNghiem;
    }
}
