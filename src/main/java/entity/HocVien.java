package entity;

import java.time.LocalDate;
import java.time.Period;

// Học viên của trung tâm, kế thừa các thông tin chung từ Nguoi
public class HocVien extends Nguoi {
    private LocalDate ngaySinh;
    private TrinhDo trinhDoDauVao;

    public HocVien() {
    }

    public HocVien(long id, String hoTen, String soDienThoai, String email,
                   LocalDate ngaySinh, TrinhDo trinhDoDauVao) {
        super(id, hoTen, soDienThoai, email);
        this.ngaySinh = ngaySinh;
        this.trinhDoDauVao = trinhDoDauVao;
    }

    @Override
    public String layVaiTro() {
        return "Hoc vien";
    }

    public int tinhTuoi() {
        return Period.between(ngaySinh, LocalDate.now()).getYears();
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public TrinhDo getTrinhDoDauVao() {
        return trinhDoDauVao;
    }

    public void setTrinhDoDauVao(TrinhDo trinhDoDauVao) {
        this.trinhDoDauVao = trinhDoDauVao;
    }
}
