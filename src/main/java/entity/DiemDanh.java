package entity;

import java.time.LocalDate;

// Một dòng điểm danh: học viên X, ở lớp Y, vào ngày Z, có mặt hay vắng
public class DiemDanh {
    private long id;
    private long lopHocId;
    private long hocVienId;
    private LocalDate ngay;
    private TrangThaiDiemDanh trangThai;

    public DiemDanh() {
    }

    public DiemDanh(long id, long lopHocId, long hocVienId, LocalDate ngay, TrangThaiDiemDanh trangThai) {
        this.id = id;
        this.lopHocId = lopHocId;
        this.hocVienId = hocVienId;
        this.ngay = ngay;
        this.trangThai = trangThai;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLopHocId() {
        return lopHocId;
    }

    public void setLopHocId(long lopHocId) {
        this.lopHocId = lopHocId;
    }

    public long getHocVienId() {
        return hocVienId;
    }

    public void setHocVienId(long hocVienId) {
        this.hocVienId = hocVienId;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public void setNgay(LocalDate ngay) {
        this.ngay = ngay;
    }

    public TrangThaiDiemDanh getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiDiemDanh trangThai) {
        this.trangThai = trangThai;
    }
}
