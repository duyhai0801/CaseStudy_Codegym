package ui;

import entity.LopHoc;
import entity.TrangThaiLop;
import entity.TrinhDo;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.LopHocService;
import validator.CommonValidator;
import validator.LopHocValidator;

import java.time.LocalDate;
import java.util.List;

// Menu quản lý lớp học: CRUD, xem học viên của lớp, lọc lớp, sắp xếp theo ngày khai giảng
public class LopHocUI {
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY LOP HOC ======");
            System.out.println("1. Mo lop moi");
            System.out.println("2. Sua lop");
            System.out.println("3. Xoa lop");
            System.out.println("4. Xem danh sach");
            System.out.println("5. Xem hoc vien cua mot lop");
            System.out.println("6. Loc lop con cho trong");
            System.out.println("7. Loc lop theo khoa hoc");
            System.out.println("8. Loc lop theo trinh do");
            System.out.println("9. Sap xep theo ngay khai giang");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputInt();
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
                    xemHocVienCuaLopUI();
                    break;
                case 6:
                    inDanhSach(lopHocService.locLopConCho());
                    break;
                case 7:
                    locTheoKhoaHocUI();
                    break;
                case 8:
                    locTheoTrinhDoUI();
                    break;
                case 9:
                    inDanhSach(lopHocService.sapXepTheoNgayKhaiGiang());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void addUI() {
        System.out.println("====== Mo lop moi ======");
        long id = lopHocService.taoIdMoi();
        System.out.println("Nhap ten lop (vd IELTS55-K03): ");
        String tenLop = LopHocValidator.inputTenLop(null);
        System.out.println("--- Chon khoa hoc ---");
        long khoaHocId = KhoaHocUI.chonKhoaHocId(null);
        System.out.println("--- Chon giang vien ---");
        long giangVienId = GiangVienUI.chonGiangVienId(null);
        System.out.println("Nhap lich hoc (vd T2-T4-T6 18:00-19:30): ");
        String lichHoc = LopHocValidator.inputLichHoc(null);
        System.out.println("Nhap ngay khai giang (dd/MM/yyyy): ");
        LocalDate ngayKhaiGiang = LopHocValidator.inputNgayKhaiGiang(null);
        System.out.println("Nhap si so toi da (5-30): ");
        int siSoToiDa = LopHocValidator.inputSiSoToiDa(null);
        System.out.println("Chon trang thai: ");
        TrangThaiLop trangThai = LopHocValidator.inputTrangThai(null);
        try {
            lopHocService.add(new LopHoc(id, tenLop, khoaHocId, giangVienId, lichHoc,
                    ngayKhaiGiang, siSoToiDa, trangThai));
            System.out.println("Da mo lop, id = " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the mo lop: " + e.getMessage());
        }
    }

    public static void editUI() {
        System.out.println("====== Sua lop ======");
        System.out.println("Nhap id lop can sua: ");
        long id = InputHelper.inputLong();
        try {
            LopHoc cu = lopHocService.findById(id);
            System.out.println("(Nhan Enter de giu gia tri cu)");
            System.out.println("Ten lop [" + cu.getTenLop() + "]: ");
            String tenLop = LopHocValidator.inputTenLop(cu.getTenLop());
            System.out.println("--- Khoa hoc [" + KhoaHocUI.layTenKhoaHoc(cu.getKhoaHocId()) + "] ---");
            long khoaHocId = KhoaHocUI.chonKhoaHocId(cu.getKhoaHocId());
            System.out.println("--- Giang vien [" + GiangVienUI.layTenGiangVien(cu.getGiangVienId()) + "] ---");
            long giangVienId = GiangVienUI.chonGiangVienId(cu.getGiangVienId());
            System.out.println("Lich hoc [" + cu.getLichHoc() + "]: ");
            String lichHoc = LopHocValidator.inputLichHoc(cu.getLichHoc());
            System.out.println("Ngay khai giang [" + FormatHelper.formatDate(cu.getNgayKhaiGiang()) + "]: ");
            LocalDate ngayKhaiGiang = LopHocValidator.inputNgayKhaiGiang(cu.getNgayKhaiGiang());
            System.out.println("Si so toi da [" + cu.getSiSoToiDa() + "]: ");
            int siSoToiDa = LopHocValidator.inputSiSoToiDa(cu.getSiSoToiDa());
            System.out.println("Trang thai [" + cu.getTrangThai().getTenHienThi() + "]: ");
            TrangThaiLop trangThai = LopHocValidator.inputTrangThai(cu.getTrangThai());
            lopHocService.update(id, new LopHoc(id, tenLop, khoaHocId, giangVienId, lichHoc,
                    ngayKhaiGiang, siSoToiDa, trangThai));
            System.out.println("Da cap nhat lop!");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay lop id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the cap nhat: " + e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("====== Xoa lop ======");
        System.out.println("Nhap id lop can xoa: ");
        long id = InputHelper.inputLong();
        try {
            LopHoc lopHoc = lopHocService.findById(id);
            System.out.println("Ban co chac muon xoa lop '" + lopHoc.getTenLop() + "'? (y/n)");
            if (InputHelper.inputYesNo()) {
                lopHocService.delete(id);
                System.out.println("Da xoa lop!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay lop id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach lop hoc ======");
        inDanhSach(lopHocService.getAll());
    }

    public static void xemHocVienCuaLopUI() {
        long lopHocId = chonLopHocId();
        System.out.println("Hoc vien cua lop " + layTenLop(lopHocId) + ":");
        DangKyUI.inDanhSach(dangKyService.layDangKyCuaLop(lopHocId));
    }

    public static void locTheoKhoaHocUI() {
        long khoaHocId = KhoaHocUI.chonKhoaHocId(null);
        inDanhSach(lopHocService.locTheoKhoaHoc(khoaHocId));
    }

    public static void locTheoTrinhDoUI() {
        System.out.println("Chon trinh do: ");
        TrinhDo trinhDo = CommonValidator.inputTrinhDo(null);
        inDanhSach(lopHocService.locTheoTrinhDo(trinhDo));
    }

    public static void inDanhSach(List<LopHoc> list) {
        if (list.isEmpty()) {
            System.out.println("(Danh sach trong)");
            return;
        }
        String format = "%-11s| %-14s| %-18s| %-16s| %-21s| %-11s| %-6s| %s%n";
        System.out.printf(format, "ID", "Ten lop", "Khoa hoc", "Giang vien", "Lich hoc",
                "Khai giang", "Si so", "Trang thai");
        FormatHelper.printLine(124);
        for (LopHoc item : list) {
            String siSo = dangKyService.demSoHocVienCuaLop(item.getId()) + "/" + item.getSiSoToiDa();
            System.out.printf(format, item.getId(), item.getTenLop(), KhoaHocUI.layTenKhoaHoc(item.getKhoaHocId()),
                    GiangVienUI.layTenGiangVien(item.getGiangVienId()), item.getLichHoc(),
                    FormatHelper.formatDate(item.getNgayKhaiGiang()), siSo, item.getTrangThai().getTenHienThi());
        }
    }

    public static long chonLopHocId() {
        inDanhSach(lopHocService.getAll());
        System.out.println("Nhap id lop: ");
        while (true) {
            long id = InputHelper.inputLong();
            try {
                lopHocService.findById(id);
                return id;
            } catch (DataNotFoundException e) {
                System.out.println("Id lop khong ton tai, nhap lai: ");
            }
        }
    }

    public static String layTenLop(long id) {
        try {
            return lopHocService.findById(id).getTenLop();
        } catch (DataNotFoundException e) {
            return "(khong ton tai)";
        }
    }
}
