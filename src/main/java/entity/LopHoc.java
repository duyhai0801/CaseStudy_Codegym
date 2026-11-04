package entity;

import java.time.LocalDate;

// Lớp học = một đợt mở cụ thể của một khóa học, do một giảng viên dạy.
// Liên kết sang KhoaHoc và GiangVien bằng id (giống Product giữ categoryId trong bài mẫu).
public class LopHoc {
    private long id;
    private String tenLop;
    private long khoaHocId;
    private long giangVienId;
    private String lichHoc;
    private LocalDate ngayKhaiGiang;
    private int siSoToiDa;
    private TrangThaiLop trangThai;

    public LopHoc() {
    }

    public LopHoc(long id, String tenLop, long khoaHocId, long giangVienId, String lichHoc,
                  LocalDate ngayKhaiGiang, int siSoToiDa, TrangThaiLop trangThai) {
        this.id = id;
        this.tenLop = tenLop;
        this.khoaHocId = khoaHocId;
        this.giangVienId = giangVienId;
        this.lichHoc = lichHoc;
        this.ngayKhaiGiang = ngayKhaiGiang;
        this.siSoToiDa = siSoToiDa;
        this.trangThai = trangThai;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public long getKhoaHocId() {
        return khoaHocId;
    }

    public void setKhoaHocId(long khoaHocId) {
        this.khoaHocId = khoaHocId;
    }

    public long getGiangVienId() {
        return giangVienId;
    }

    public void setGiangVienId(long giangVienId) {
        this.giangVienId = giangVienId;
    }

    public String getLichHoc() {
        return lichHoc;
    }

    public void setLichHoc(String lichHoc) {
        this.lichHoc = lichHoc;
    }

    public LocalDate getNgayKhaiGiang() {
        return ngayKhaiGiang;
    }

    public void setNgayKhaiGiang(LocalDate ngayKhaiGiang) {
        this.ngayKhaiGiang = ngayKhaiGiang;
    }

    public int getSiSoToiDa() {
        return siSoToiDa;
    }

    public void setSiSoToiDa(int siSoToiDa) {
        this.siSoToiDa = siSoToiDa;
    }

    public TrangThaiLop getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiLop trangThai) {
        this.trangThai = trangThai;
    }
}
