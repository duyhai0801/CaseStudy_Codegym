package ui;

import entity.DangKy;
import entity.GiangVien;
import entity.HocVien;
import entity.LopHoc;
import entity.TrangThaiHocPhi;
import entity.TrangThaiLop;
import entity.User;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.GiangVienService;
import service.HocVienService;
import service.LopHocService;

import java.util.ArrayList;
import java.util.List;

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
            System.out.println("2. Danh sach lop toi day");
            System.out.println("3. Danh sach hoc vien va hoc phi theo lop");
            System.out.println("4. Diem danh / cap nhat diem danh");
            System.out.println("5. Xem lich su buoi hoc");
            System.out.println("6. Tong hop chuyen can cua lop");
            System.out.println("7. Bat dau lop");
            System.out.println("8. Ket thuc lop");
            System.out.println("9. Doi mat khau");
            System.out.println("0. Dang xuat");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        xemThongTinUI(giangVien);
                        break;
                    case 2:
                        System.out.println("====== Danh sach lop toi day ======");
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
                        batDauLopUI(giangVienId);
                        break;
                    case 8:
                        ketThucLopUI(giangVienId);
                        break;
                    case 9:
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

    public static void xemHocVienVaHocPhiUI(long giangVienId) {
        System.out.println("====== Danh sach hoc vien va hoc phi theo lop ======");
        LopHoc lop = DiemDanhUI.chonLop(giangVienId, false);
        if (lop == null) {
            return;
        }
        List<DangKy> cacDangKy = dangKyService.layDangKyCuaLop(lop.getId());
        System.out.println("Lop " + lop.getTenLop() + ":");
        if (cacDangKy.isEmpty()) {
            System.out.println("(Lop chua co hoc vien)");
            return;
        }
        String format = "%-11s| %-20s| %-11s| %-24s| %16s | %16s | %s%n";
        System.out.printf(format, "ID HV", "Hoc vien", "SDT", "Email", "Da dong", "Con no", "Hoc phi");
        FormatHelper.printLine(125);
        for (DangKy item : cacDangKy) {
            String hoTen = "(khong ton tai)";
            String soDienThoai = "-";
            String email = "-";
            try {
                HocVien hocVien = HocVienService.getObject().findById(item.getHocVienId());
                hoTen = hocVien.getHoTen();
                soDienThoai = hocVien.getSoDienThoai();
                email = hocVien.getEmail();
            } catch (DataNotFoundException e) {
            }
            System.out.printf(format, item.getHocVienId(), hoTen, soDienThoai, email,
                    FormatHelper.formatMoney(item.getSoTienDaDong()), FormatHelper.formatMoney(item.tinhSoTienConNo()),
                    item.layTrangThaiHocPhi().getTenHienThi());
        }
        int daDongDu = 0;
        for (DangKy item : cacDangKy) {
            if (item.layTrangThaiHocPhi() == TrangThaiHocPhi.DA_DONG) {
                daDongDu++;
            }
        }
        System.out.println("=> " + daDongDu + "/" + cacDangKy.size() + " hoc vien da dong du hoc phi.");
    }

    private static LopHoc chonLopTheoTrangThai(long giangVienId, TrangThaiLop trangThai) {
        List<LopHoc> cacLop = new ArrayList<>();
        for (LopHoc item : lopHocService.locTheoGiangVien(giangVienId)) {
            if (item.getTrangThai() == trangThai) {
                cacLop.add(item);
            }
        }
        if (cacLop.isEmpty()) {
            System.out.println("Ban khong co lop nao o trang thai '" + trangThai.getTenHienThi() + "'.");
            return null;
        }
        LopHocUI.inDanhSach(cacLop);
        System.out.println("Nhap id lop: ");
        while (true) {
            long id = InputHelper.inputLong();
            for (LopHoc item : cacLop) {
                if (item.getId() == id) {
                    return item;
                }
            }
            System.out.println("Chi duoc chon lop trong danh sach tren, nhap lai: ");
        }
    }

    public static void batDauLopUI(long giangVienId) {
        System.out.println("====== Bat dau lop ======");
        LopHoc lop = chonLopTheoTrangThai(giangVienId, TrangThaiLop.SAP_KHAI_GIANG);
        if (lop == null) {
            return;
        }
        try {
            lopHocService.batDauLop(lop.getId());
            System.out.println("Lop " + lop.getTenLop() + " da chuyen sang 'Dang hoc'. Gio co the diem danh.");
        } catch (NghiepVuException e) {
            System.out.println("Khong the bat dau: " + e.getMessage());
        }
    }

    public static void ketThucLopUI(long giangVienId) {
        System.out.println("====== Ket thuc lop ======");
        LopHoc lop = chonLopTheoTrangThai(giangVienId, TrangThaiLop.DANG_HOC);
        if (lop == null) {
            return;
        }
        System.out.println("Ket thuc lop " + lop.getTenLop() + "? Sau khi ket thuc se khong diem danh duoc nua. (y/n)");
        if (!InputHelper.inputYesNo()) {
            return;
        }
        try {
            lopHocService.ketThucLop(lop.getId());
            System.out.println("Lop " + lop.getTenLop() + " da ket thuc.");
        } catch (NghiepVuException e) {
            System.out.println("Khong the ket thuc: " + e.getMessage());
        }
    }
}
