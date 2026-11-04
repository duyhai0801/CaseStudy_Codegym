package entity;

import java.time.LocalDate;

// Một lượt đăng ký: "học viên X học lớp Y".
// Đây là entity trung gian cho quan hệ n-n giữa HocVien và LopHoc.
public class DangKy {
    private long id;
    private long hocVienId;
    private long lopHocId;
    private LocalDate ngayDangKy;
    private long hocPhiPhaiDong; // chốt lúc đăng ký (đã trừ giảm giá), khóa có đổi giá cũng không ảnh hưởng
    private long soTienDaDong;

    public DangKy() {
    }

    public DangKy(long id, long hocVienId, long lopHocId, LocalDate ngayDangKy,
                  long hocPhiPhaiDong, long soTienDaDong) {
        this.id = id;
        this.hocVienId = hocVienId;
        this.lopHocId = lopHocId;
        this.ngayDangKy = ngayDangKy;
        this.hocPhiPhaiDong = hocPhiPhaiDong;
        this.soTienDaDong = soTienDaDong;
    }

    public long tinhSoTienConNo() {
        return hocPhiPhaiDong - soTienDaDong;
    }

    // Trạng thái không lưu vào file mà tính ra từ 2 con số, nên luôn đúng với số tiền thực tế
    public TrangThaiHocPhi layTrangThaiHocPhi() {
        if (soTienDaDong == 0) {
            return TrangThaiHocPhi.CHUA_DONG;
        }
        if (soTienDaDong < hocPhiPhaiDong) {
            return TrangThaiHocPhi.DONG_MOT_PHAN;
        }
        return TrangThaiHocPhi.DA_DONG;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHocVienId() {
        return hocVienId;
    }

    public void setHocVienId(long hocVienId) {
        this.hocVienId = hocVienId;
    }

    public long getLopHocId() {
        return lopHocId;
    }

    public void setLopHocId(long lopHocId) {
        this.lopHocId = lopHocId;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public long getHocPhiPhaiDong() {
        return hocPhiPhaiDong;
    }

    public void setHocPhiPhaiDong(long hocPhiPhaiDong) {
        this.hocPhiPhaiDong = hocPhiPhaiDong;
    }

    public long getSoTienDaDong() {
        return soTienDaDong;
    }

    public void setSoTienDaDong(long soTienDaDong) {
        this.soTienDaDong = soTienDaDong;
    }
}
