package ui;

import entity.LopHoc;
import entity.TrangThaiLop;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.HuyThaoTacException;
import lib.errors.NghiepVuException;
import service.DangKyService;
import service.LopHocService;
import validator.LopHocValidator;

import java.time.LocalDate;
import java.util.List;

public class LopHocUI {
    private static final LopHocService lopHocService = LopHocService.getObject();
    private static final DangKyService dangKyService = DangKyService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY LOP HOC ======");
            System.out.println("1. Cap nhat lop");
            System.out.println("2. Danh sach lop hoc");
            System.out.println("3. Danh sach hoc vien theo lop");
            System.out.println("4. Danh sach lop con cho trong");
            System.out.println("0. Quay lai");
            System.out.println("Nhap lua chon: ");
            choice = InputHelper.inputLuaChon();
            try {
                switch (choice) {
                    case 1:
                        capNhatLopUI();
                        break;
                    case 2:
                        getAllUI();
                        break;
                    case 3:
                        xemHocVienCuaLopUI();
                        break;
                    case 4:
                        System.out.println("====== Danh sach lop con cho trong ======");
                        inDanhSach(lopHocService.locLopConCho());
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

    public static void capNhatLopUI() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== CAP NHAT LOP HOC ======");
            inDanhSach(lopHocService.getAll());
            System.out.println("1. Mo lop moi");
            System.out.println("2. Sua lop");
            System.out.println("3. Xoa lop");
            System.out.println("4. Bat dau lop");
            System.out.println("5. Ket thuc lop");
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
                        batDauLopUI();
                        break;
                    case 5:
                        ketThucLopUI();
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
        try {
            lopHocService.add(new LopHoc(id, tenLop, khoaHocId, giangVienId, lichHoc,
                    ngayKhaiGiang, siSoToiDa, TrangThaiLop.SAP_KHAI_GIANG));
            System.out.println("Da mo lop, id = " + id + " (trang thai: Sap khai giang)");
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
            lopHocService.update(id, new LopHoc(id, tenLop, khoaHocId, giangVienId, lichHoc,
                    ngayKhaiGiang, siSoToiDa, cu.getTrangThai()));
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

    public static void batDauLopUI() {
        System.out.println("====== Bat dau lop ======");
        System.out.println("Nhap id lop can bat dau: ");
        long id = InputHelper.inputLong();
        try {
            lopHocService.batDauLop(id);
            System.out.println("Lop " + layTenLop(id) + " da chuyen sang 'Dang hoc'. Giang vien co the diem danh.");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay lop id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the bat dau: " + e.getMessage());
        }
    }

    public static void ketThucLopUI() {
        System.out.println("====== Ket thuc lop ======");
        System.out.println("Nhap id lop can ket thuc: ");
        long id = InputHelper.inputLong();
        try {
            LopHoc lop = lopHocService.findById(id);
            if (lop.getTrangThai() != TrangThaiLop.DANG_HOC) {
                System.out.println("Lop " + lop.getTenLop() + " dang o trang thai '" + lop.getTrangThai().getTenHienThi()
                        + "', chi ket thuc duoc lop dang hoc.");
                return;
            }
            System.out.println("Ket thuc lop " + lop.getTenLop() + "? Sau khi ket thuc se khong diem danh duoc nua. (y/n)");
            if (!InputHelper.inputYesNo()) {
                return;
            }
            lopHocService.ketThucLop(id);
            System.out.println("Lop " + lop.getTenLop() + " da ket thuc.");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay lop id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the ket thuc: " + e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach lop hoc ======");
        inDanhSach(lopHocService.getAll());
    }

    public static void xemHocVienCuaLopUI() {
        System.out.println("====== Danh sach hoc vien theo lop ======");
        long lopHocId = chonLopHocId();
        System.out.println("Hoc vien cua lop " + layTenLop(lopHocId) + ":");
        DangKyUI.inDanhSach(dangKyService.layDangKyCuaLop(lopHocId));
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
