package ui;

import entity.DangKy;
import entity.HocVien;
import entity.KhoaHoc;
import entity.LopHoc;
import entity.User;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.HocVienService;
import service.KhoaHocService;
import service.LopHocService;

import java.util.ArrayList;
import java.util.List;

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
            System.out.println("2. Danh sach lop toi dang hoc");
            System.out.println("3. Xem hoc phi cua toi");
            System.out.println("4. Xem chuyen can cua toi");
            System.out.println("5. Dang ky / huy dang ky lop");
            System.out.println("6. Doi mat khau");
            System.out.println("0. Dang xuat");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
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
                        dangKyHuyLopUI(hocVienId);
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
            } catch (HuyThaoTacException e) {
                System.out.println("Da huy thao tac.");
            }
        } while (choice != 0);
    }

    public static void dangKyHuyLopUI(long hocVienId) {
        int choice;
        do {
            System.out.println();
            System.out.println("====== DANG KY / HUY DANG KY LOP ======");
            System.out.println("--- Lop dang mo ban co the dang ky ---");
            LopHocUI.inDanhSach(layLopCoTheDangKy(hocVienId));
            System.out.println("--- Lop ban da dang ky ---");
            DangKyUI.inDanhSach(dangKyService.layDangKyCuaHocVien(hocVienId));
            System.out.println("1. Dang ky lop");
            System.out.println("2. Huy dang ky lop");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        dangKyLopUI(hocVienId);
                        break;
                    case 2:
                        huyDangKyUI(hocVienId);
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Lua chon khong hop le");
                }
            } catch (HuyThaoTacException e) {
                System.out.println("Da huy thao tac.");
            }
        } while (choice != 0);
    }

    private static List<LopHoc> layLopCoTheDangKy(long hocVienId) {
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaHocVien(hocVienId);
        List<LopHoc> ketQua = new ArrayList<>();
        for (LopHoc lop : lopHocService.locLopConCho()) {
            boolean daDangKy = false;
            for (DangKy item : cacDangKy) {
                if (item.getLopHocId() == lop.getId()) {
                    daDangKy = true;
                }
            }
            if (!daDangKy) {
                ketQua.add(lop);
            }
        }
        return ketQua;
    }

    public static void dangKyLopUI(long hocVienId) {
        if (layLopCoTheDangKy(hocVienId).isEmpty()) {
            System.out.println("Hien khong co lop nao de dang ky.");
            return;
        }
        System.out.println("Nhap id lop muon dang ky: ");
        long lopHocId = InputHelper.inputLong();
        System.out.println("Xac nhan dang ky lop " + LopHocUI.layTenLop(lopHocId) + "? (y/n)");
        if (!InputHelper.inputYesNo()) {
            return;
        }
        try {
            DangKy dangKy = dangKyService.dangKyVaoLop(hocVienId, lopHocId);
            LopHoc lopHoc = lopHocService.findById(lopHocId);
            KhoaHoc khoaHoc = KhoaHocService.getObject().findById(lopHoc.getKhoaHocId());
            System.out.println("Dang ky thanh cong! Ma dang ky: " + dangKy.getId());
            System.out.println("Hoc phi phai dong: " + FormatHelper.formatMoney(dangKy.getHocPhiPhaiDong()));
            if (dangKy.getHocPhiPhaiDong() < khoaHoc.getHocPhi()) {
                System.out.println("(Ban duoc giam " + DangKyService.PHAN_TRAM_GIAM_HOC_VIEN_CU
                        + "% vi la hoc vien cu, gia goc " + FormatHelper.formatMoney(khoaHoc.getHocPhi()) + ")");
            }
            System.out.println("Vui long dong hoc phi tai quay cua trung tam truoc ngay khai giang "
                    + FormatHelper.formatDate(lopHoc.getNgayKhaiGiang()) + ".");
        } catch (NghiepVuException e) {
            System.out.println("Khong the dang ky: " + e.getMessage());
        }
    }

    public static void huyDangKyUI(long hocVienId) {
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaHocVien(hocVienId);
        if (cacDangKy.isEmpty()) {
            System.out.println("Ban chua dang ky lop nao.");
            return;
        }
        System.out.println("Nhap ma dang ky muon huy: ");
        long dangKyId = InputHelper.inputLong();
        DangKy canHuy = null;
        for (DangKy item : cacDangKy) {
            if (item.getId() == dangKyId) {
                canHuy = item;
            }
        }
        if (canHuy == null) {
            System.out.println("Ma dang ky khong nam trong danh sach cua ban.");
            return;
        }
        System.out.println("Xac nhan huy dang ky lop " + LopHocUI.layTenLop(canHuy.getLopHocId()) + "? (y/n)");
        if (!InputHelper.inputYesNo()) {
            return;
        }
        try {
            DangKy daHuy = dangKyService.huyDangKy(dangKyId);
            System.out.println("Da huy dang ky!");
            if (daHuy.getSoTienDaDong() > 0) {
                System.out.println("Ban se duoc hoan lai " + FormatHelper.formatMoney(daHuy.getSoTienDaDong())
                        + ", vui long lien he quay cua trung tam.");
            }
        } catch (NghiepVuException e) {
            System.out.println("Khong the huy: " + e.getMessage());
        }
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
        System.out.println("====== Danh sach lop toi dang hoc ======");
        List<LopHoc> cacLop = new ArrayList<>();
        for (DangKy item : dangKyService.layDangKyCuaHocVien(hocVienId)) {
            try {
                cacLop.add(lopHocService.findById(item.getLopHocId()));
            } catch (DataNotFoundException e) {
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
