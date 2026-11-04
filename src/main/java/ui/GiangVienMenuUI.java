package ui;

import entity.DangKy;
import entity.GiangVien;
import entity.LopHoc;
import entity.TrangThaiHocPhi;
import entity.TrangThaiLop;
import entity.User;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.DangKyService;
import service.GiangVienService;
import service.LopHocService;

import java.util.List;

// Menu của tài khoản GIẢNG VIÊN: chỉ thao tác trên các lớp mình dạy
// (xem lớp, xem học viên và tình hình đóng học phí, điểm danh, xem chuyên cần)
public class GiangVienMenuUI {
    private static final GiangVienService giangVienService = GiangVienService.getObject();
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    public static void showMainMenu(User user) {
        GiangVien giangVien;
        try {
            giangVien = giangVienService.findById(user.getNguoiId());
        } catch (DataNotFoundException e) {
            System.out.println("Tai khoan nay khong gan voi giang vien nao. Lien he quan tri vien.");
            return;
        }
        long giangVienId = giangVien.getId();
        int choice;
        do {
            System.out.println();
            System.out.println("====== GIANG VIEN: " + giangVien.getHoTen() + " ======");
            System.out.println("1. Xem thong tin ca nhan");
            System.out.println("2. Xem cac lop toi day");
            System.out.println("3. Xem hoc vien va hoc phi cua lop");
            System.out.println("4. Diem danh / cap nhat diem danh");
            System.out.println("5. Xem diem danh mot buoi");
            System.out.println("6. Tong hop chuyen can cua lop");
            System.out.println("7. Doi mat khau");
            System.out.println("0. Dang xuat");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    xemThongTinUI(giangVien);
                    break;
                case 2:
                    LopHocUI.inDanhSach(lopHocService.locTheoGiangVien(giangVienId));
                    break;
                case 3:
                    xemHocVienVaHocPhiUI(giangVienId);
                    break;
                case 4:
                    DiemDanhUI.diemDanhUI(giangVienId);
                    break;
                case 5:
                    DiemDanhUI.xemBuoiHocUI(giangVienId);
                    break;
                case 6:
                    DiemDanhUI.xemChuyenCanLopUI(giangVienId);
                    break;
                case 7:
                    AuthUI.doiMatKhauUI(user);
                    break;
                case 0:
                    System.out.println("Da dang xuat.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void xemThongTinUI(GiangVien giangVien) {
        int soLopDangDay = 0;
        List<LopHoc> cacLop = lopHocService.locTheoGiangVien(giangVien.getId());
        for (LopHoc item : cacLop) {
            if (item.getTrangThai() != TrangThaiLop.DA_KET_THUC) {
                soLopDangDay++;
            }
        }
        System.out.println("====== Thong tin ca nhan ======");
        System.out.println("Ho ten          : " + giangVien.getHoTen());
        System.out.println("Vai tro         : " + giangVien.layVaiTro());
        System.out.println("So dien thoai   : " + giangVien.getSoDienThoai());
        System.out.println("Email           : " + giangVien.getEmail());
        System.out.println("Chuyen mon      : " + giangVien.getChuyenMon());
        System.out.println("Kinh nghiem     : " + giangVien.getSoNamKinhNghiem() + " nam");
        System.out.println("Lop dang phu trach: " + soLopDangDay + " (tong cong " + cacLop.size() + " lop)");
    }

    // Giảng viên xem được ai đã đóng / chưa đóng học phí trong lớp mình dạy
    public static void xemHocVienVaHocPhiUI(long giangVienId) {
        System.out.println("====== Hoc vien va hoc phi ======");
        LopHoc lop = DiemDanhUI.chonLop(giangVienId, false);
        if (lop == null) {
            return;
        }
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaLop(lop.getId());
        System.out.println("Lop " + lop.getTenLop() + ":");
        DangKyUI.inDanhSach(cacDangKy);
        int daDongDu = 0;
        for (DangKy item : cacDangKy) {
            if (item.layTrangThaiHocPhi() == TrangThaiHocPhi.DA_DONG) {
                daDongDu++;
            }
        }
        System.out.println("=> " + daDongDu + "/" + cacDangKy.size() + " hoc vien da dong du hoc phi.");
    }
}
