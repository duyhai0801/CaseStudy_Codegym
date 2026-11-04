package service;

import entity.DangKy;
import entity.GiangVien;
import entity.KhoaHoc;
import entity.LopHoc;
import entity.TrangThaiLop;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Tính các số liệu thống kê. Không giữ dữ liệu riêng mà lấy từ các service khác.
public class ThongKeService {
    private static final ThongKeService thongKeService = new ThongKeService();

    private ThongKeService() {
    }

    public static ThongKeService getObject() {
        return thongKeService;
    }

    public long tinhTongDaThu() {
        long tong = 0;
        for (DangKy item : DangKyService.getObject().getAll()) {
            tong += item.getSoTienDaDong();
        }
        return tong;
    }

    public long tinhTongConNo() {
        long tong = 0;
        for (DangKy item : DangKyService.getObject().getAll()) {
            tong += item.tinhSoTienConNo();
        }
        return tong;
    }

    // Map: hocVienId -> tổng tiền còn nợ (một học viên có thể nợ ở nhiều lớp), sắp xếp nợ giảm dần
    public List<Map.Entry<Long, Long>> thongKeNoTheoHocVien() {
        Map<Long, Long> noTheoHocVien = new LinkedHashMap<>();
        for (DangKy item : DangKyService.getObject().getAll()) {
            long conNo = item.tinhSoTienConNo();
            if (conNo > 0) {
                long noCu = noTheoHocVien.getOrDefault(item.getHocVienId(), 0L);
                noTheoHocVien.put(item.getHocVienId(), noCu + conNo);
            }
        }
        List<Map.Entry<Long, Long>> ketQua = new ArrayList<>(noTheoHocVien.entrySet());
        ketQua.sort((a, b) -> Long.compare(b.getValue(), a.getValue()));
        return ketQua;
    }

    public double tinhTiLeLapDay(LopHoc lop) {
        int siSo = DangKyService.getObject().demSoHocVienCuaLop(lop.getId());
        return siSo * 100.0 / lop.getSiSoToiDa();
    }

    // Map: khoaHocId -> số lượt đăng ký của tất cả các lớp thuộc khóa đó, sắp xếp giảm dần
    public List<Map.Entry<Long, Integer>> xepHangKhoaHocTheoSoHocVien() {
        Map<Long, Integer> soHocVienTheoKhoa = new LinkedHashMap<>();
        for (KhoaHoc item : KhoaHocService.getObject().getAll()) {
            soHocVienTheoKhoa.put(item.getId(), 0);
        }
        for (DangKy item : DangKyService.getObject().getAll()) {
            LopHoc lop = LopHocService.getObject().findById(item.getLopHocId());
            long khoaHocId = lop.getKhoaHocId();
            soHocVienTheoKhoa.put(khoaHocId, soHocVienTheoKhoa.getOrDefault(khoaHocId, 0) + 1);
        }
        List<Map.Entry<Long, Integer>> ketQua = new ArrayList<>(soHocVienTheoKhoa.entrySet());
        ketQua.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return ketQua;
    }

    // Map: giangVienId -> số lớp CHƯA KẾT THÚC mà giảng viên đó đang phụ trách
    public List<Map.Entry<Long, Integer>> demSoLopDangDayTheoGiangVien() {
        Map<Long, Integer> soLopTheoGiangVien = new LinkedHashMap<>();
        for (GiangVien item : GiangVienService.getObject().getAll()) {
            soLopTheoGiangVien.put(item.getId(), 0);
        }
        for (LopHoc item : LopHocService.getObject().getAll()) {
            if (item.getTrangThai() != TrangThaiLop.DA_KET_THUC) {
                long giangVienId = item.getGiangVienId();
                soLopTheoGiangVien.put(giangVienId, soLopTheoGiangVien.getOrDefault(giangVienId, 0) + 1);
            }
        }
        List<Map.Entry<Long, Integer>> ketQua = new ArrayList<>(soLopTheoGiangVien.entrySet());
        ketQua.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return ketQua;
    }
}
