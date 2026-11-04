package entity;

// Khóa học = chương trình học (vd "IELTS 5.5"). Một khóa học có thể mở nhiều lớp.
public class KhoaHoc {
    private long id;
    private String tenKhoaHoc;
    private TrinhDo trinhDo;
    private int soBuoi;
    private long hocPhi; // VND

    public KhoaHoc() {
    }

    public KhoaHoc(long id, String tenKhoaHoc, TrinhDo trinhDo, int soBuoi, long hocPhi) {
        this.id = id;
        this.tenKhoaHoc = tenKhoaHoc;
        this.trinhDo = trinhDo;
        this.soBuoi = soBuoi;
        this.hocPhi = hocPhi;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    public TrinhDo getTrinhDo() {
        return trinhDo;
    }

    public void setTrinhDo(TrinhDo trinhDo) {
        this.trinhDo = trinhDo;
    }

    public int getSoBuoi() {
        return soBuoi;
    }

    public void setSoBuoi(int soBuoi) {
        this.soBuoi = soBuoi;
    }

    public long getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(long hocPhi) {
        this.hocPhi = hocPhi;
    }
}
