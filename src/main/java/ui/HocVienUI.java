package ui;

import entity.HocVien;
import entity.TrinhDo;
import entity.User;
import entity.VaiTro;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.HocVienService;
import validator.CommonValidator;
import validator.NguoiValidator;

import java.time.LocalDate;
import java.util.List;

public class HocVienUI {
    private static final HocVienService hocVienService = HocVienService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY HOC VIEN ======");
            System.out.println("1. Them hoc vien");
            System.out.println("2. Sua thong tin hoc vien");
            System.out.println("3. Xoa hoc vien");
            System.out.println("4. Danh sach hoc vien");
            System.out.println("5. Tim theo ten");
            System.out.println("6. Tim theo so dien thoai");
            System.out.println("7. Danh sach hoc vien theo ten");
            System.out.println("8. Danh sach lop theo hoc vien");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        addUI();
                        break;
                    case 2:
                        editUI();
                        break;
                    case 3:
                        deleteUI();
                        break;
                    case 4:
                        getAllUI();
                        break;
                    case 5:
                        timTheoTenUI();
                        break;
                    case 6:
                        timTheoSoDienThoaiUI();
                        break;
                    case 7:
                        System.out.println("====== Danh sach hoc vien theo ten ======");
                        inDanhSach(hocVienService.sapXepTheoTen());
                        break;
                    case 8:
                        xemLopCuaHocVienUI();
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

    public static long addUI() {
        System.out.println("====== Them hoc vien ======");
        long id = hocVienService.taoIdMoi();
        System.out.println("Nhap ho ten: ");
        String hoTen = NguoiValidator.inputHoTen(null);
        System.out.println("Nhap so dien thoai: ");
        String soDienThoai = nhapSoDienThoaiKhongTrung(null, id);
        System.out.println("Nhap email: ");
        String email = NguoiValidator.inputEmail(null);
        System.out.println("Nhap ngay sinh (dd/MM/yyyy): ");
        LocalDate ngaySinh = NguoiValidator.inputNgaySinh(null);
        System.out.println("Chon trinh do dau vao: ");
        TrinhDo trinhDo = CommonValidator.inputTrinhDo(null);
        User taiKhoan = TaiKhoanUI.nhapTaiKhoan(VaiTro.HOC_VIEN, id);
        try {
            hocVienService.add(new HocVien(id, hoTen, soDienThoai, email, ngaySinh, trinhDo));
            System.out.println("Da them hoc vien, id = " + id);
            TaiKhoanUI.luuTaiKhoan(taiKhoan);
            return id;
        } catch (NghiepVuException e) {
            System.out.println("Khong the them: " + e.getMessage());
            return -1;
        }
    }

    public static void editUI() {
        System.out.println("====== Sua thong tin hoc vien ======");
        inDanhSach(hocVienService.getAll());
        System.out.println("Nhap id hoc vien can sua: ");
        long id = InputHelper.inputLong();
        try {
            HocVien cu = hocVienService.findById(id);
            System.out.println("(Nhan Enter de giu gia tri cu)");
            System.out.println("Ho ten [" + cu.getHoTen() + "]: ");
            String hoTen = NguoiValidator.inputHoTen(cu.getHoTen());
            System.out.println("So dien thoai [" + cu.getSoDienThoai() + "]: ");
            String soDienThoai = nhapSoDienThoaiKhongTrung(cu.getSoDienThoai(), id);
            System.out.println("Email [" + cu.getEmail() + "]: ");
            String email = NguoiValidator.inputEmail(cu.getEmail());
            System.out.println("Ngay sinh [" + FormatHelper.formatDate(cu.getNgaySinh()) + "]: ");
            LocalDate ngaySinh = NguoiValidator.inputNgaySinh(cu.getNgaySinh());
            System.out.println("Trinh do dau vao [" + cu.getTrinhDoDauVao().getTenHienThi() + "]: ");
            TrinhDo trinhDo = CommonValidator.inputTrinhDo(cu.getTrinhDoDauVao());
            hocVienService.update(id, new HocVien(id, hoTen, soDienThoai, email, ngaySinh, trinhDo));
            System.out.println("Da cap nhat thong tin hoc vien!");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay hoc vien id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the cap nhat: " + e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("====== Xoa hoc vien ======");
        inDanhSach(hocVienService.getAll());
        System.out.println("Nhap id hoc vien can xoa: ");
        long id = InputHelper.inputLong();
        try {
            HocVien hocVien = hocVienService.findById(id);
            System.out.println("Ban co chac muon xoa hoc vien '" + hocVien.getHoTen() + "'? (y/n)");
            if (InputHelper.inputYesNo()) {
                hocVienService.delete(id);
                System.out.println("Da xoa hoc vien va tai khoan dang nhap cua hoc vien nay!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay hoc vien id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    private static String nhapSoDienThoaiKhongTrung(String giaTriCu, long hocVienId) {
        while (true) {
            String soDienThoai = NguoiValidator.inputSoDienThoai(giaTriCu);
            try {
                hocVienService.kiemTraTrungSoDienThoai(soDienThoai, hocVienId);
                return soDienThoai;
            } catch (NghiepVuException e) {
                System.out.println(e.getMessage() + " Nhap so khac:");
            }
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach hoc vien ======");
        inDanhSach(hocVienService.getAll());
    }

    public static void timTheoTenUI() {
        System.out.println("====== Tim hoc vien theo ten ======");
        System.out.println("Nhap tu khoa (mot phan cua ho ten): ");
        String keyword = InputHelper.inputString();
        inDanhSach(hocVienService.timTheoTen(keyword));
    }

    public static void timTheoSoDienThoaiUI() {
        System.out.println("====== Tim hoc vien theo so dien thoai ======");
        System.out.println("Nhap so dien thoai (hoac mot phan): ");
        String keyword = InputHelper.inputString();
        inDanhSach(hocVienService.timTheoSoDienThoai(keyword));
    }

    public static void xemLopCuaHocVienUI() {
        System.out.println("====== Danh sach lop theo hoc vien ======");
        long hocVienId = chonHocVienId();
        System.out.println("Cac lop cua hoc vien " + layTenHocVien(hocVienId) + ":");
        DangKyUI.inDanhSach(dangKyService.layDangKyCuaHocVien(hocVienId));
    }

    public static void inDanhSach(List<HocVien> list) {
        if (list.isEmpty()) {
            System.out.println("(Danh sach trong)");
            return;
        }
        String format = "%-11s| %-20s| %-11s| %-24s| %-11s| %-5s| %s%n";
        System.out.printf(format, "ID", "Ho ten", "SDT", "Email", "Ngay sinh", "Tuoi", "Trinh do");
        FormatHelper.printLine(102);
        for (HocVien item : list) {
            System.out.printf(format, item.getId(), item.getHoTen(), item.getSoDienThoai(), item.getEmail(),
                    FormatHelper.formatDate(item.getNgaySinh()), item.tinhTuoi(),
                    item.getTrinhDoDauVao().getTenHienThi());
        }
    }

    public static long chonHocVienId() {
        inDanhSach(hocVienService.getAll());
        System.out.println("Nhap id hoc vien: ");
        while (true) {
            long id = InputHelper.inputLong();
            try {
                hocVienService.findById(id);
                return id;
            } catch (DataNotFoundException e) {
                System.out.println("Id hoc vien khong ton tai, nhap lai: ");
            }
        }
    }

    public static String layTenHocVien(long id) {
        try {
            return hocVienService.findById(id).getHoTen();
        } catch (DataNotFoundException e) {
            return "(khong ton tai)";
        }
    }
}
