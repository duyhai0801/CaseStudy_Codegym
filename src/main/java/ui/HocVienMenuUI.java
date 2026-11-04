package ui;

import entity.DangKy;
import entity.HocVien;
import entity.LopHoc;
import entity.User;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.DangKyService;
import service.HocVienService;
import service.LopHocService;

import java.util.ArrayList;
import java.util.List;

// Menu của tài khoản HỌC VIÊN: chỉ XEM dữ liệu của chính mình (lớp, học phí, chuyên cần)
// và các lớp đang mở. Không được sửa gì.
public class HocVienMenuUI {
    private static final HocVienService hocVienService = HocVienService.getObject();
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    public static void showMainMenu(User user) {
        HocVien hocVien;
        try {
            hocVien = hocVienService.findById(user.getNguoiId());
        } catch (DataNotFoundException e) {
            System.out.println("Tai khoan nay khong gan voi hoc vien nao. Lien he quan tri vien.");
            return;
        }
        long hocVienId = hocVien.getId();
        int choice;
        do {
            System.out.println();
            System.out.println("====== HOC VIEN: " + hocVien.getHoTen() + " ======");
            System.out.println("1. Xem thong tin ca nhan");
            System.out.println("2. Xem cac lop toi dang hoc");
            System.out.println("3. Xem hoc phi cua toi");
            System.out.println("4. Xem chuyen can cua toi");
            System.out.println("5. Xem cac lop dang mo dang ky");
            System.out.println("6. Doi mat khau");
            System.out.println("0. Dang xuat");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    xemThongTinUI(hocVien);
                    break;
                case 2:
                    xemLopCuaToiUI(hocVienId);
                    break;
                case 3:
                    xemHocPhiUI(hocVienId);
                    break;
                case 4:
                    DiemDanhUI.xemChuyenCanCuaHocVienUI(hocVienId);
                    break;
                case 5:
                    System.out.println("====== Cac lop con cho ======");
                    LopHocUI.inDanhSach(lopHocService.locLopConCho());
                    System.out.println("(Muon dang ky, vui long lien he quay tu van cua trung tam)");
                    break;
                case 6:
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

    public static void xemThongTinUI(HocVien hocVien) {
        System.out.println("====== Thong tin ca nhan ======");
        System.out.println("Ho ten          : " + hocVien.getHoTen());
        System.out.println("Vai tro         : " + hocVien.layVaiTro());
        System.out.println("So dien thoai   : " + hocVien.getSoDienThoai());
        System.out.println("Email           : " + hocVien.getEmail());
        System.out.println("Ngay sinh       : " + FormatHelper.formatDate(hocVien.getNgaySinh())
                + " (" + hocVien.tinhTuoi() + " tuoi)");
        System.out.println("Trinh do dau vao: " + hocVien.getTrinhDoDauVao().getTenHienThi());
    }

    public static void xemLopCuaToiUI(long hocVienId) {
        System.out.println("====== Cac lop cua toi ======");
        List<LopHoc> cacLop = new ArrayList<>();
        for (DangKy item : dangKyService.layDangKyCuaHocVien(hocVienId)) {
            try {
                cacLop.add(lopHocService.findById(item.getLopHocId()));
            } catch (DataNotFoundException e) {
                // lớp đã bị xóa thì bỏ qua
            }
        }
        LopHocUI.inDanhSach(cacLop);
    }

    public static void xemHocPhiUI(long hocVienId) {
        System.out.println("====== Hoc phi cua toi ======");
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaHocVien(hocVienId);
        DangKyUI.inDanhSach(cacDangKy);
        long tongConNo = 0;
        for (DangKy item : cacDangKy) {
            tongConNo += item.tinhSoTienConNo();
        }
        if (tongConNo > 0) {
            System.out.println("=> Ban con no tong cong " + FormatHelper.formatMoney(tongConNo)
                    + ". Vui long dong tai quay cua trung tam.");
        } else if (!cacDangKy.isEmpty()) {
            System.out.println("=> Ban da dong du hoc phi. Cam on!");
        }
    }
}
