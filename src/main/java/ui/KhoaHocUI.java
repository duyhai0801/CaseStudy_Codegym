package ui;

import entity.KhoaHoc;
import entity.TrinhDo;
import lib.FormatHelper;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import service.KhoaHocService;
import validator.CommonValidator;
import validator.KhoaHocValidator;

import java.util.List;

// Menu quản lý khóa học: thêm, sửa, xóa, xem, tìm theo tên, sắp xếp theo học phí
public class KhoaHocUI {
    private static final KhoaHocService khoaHocService = KhoaHocService.getObject();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println();
            System.out.println("====== QUAN LY KHOA HOC ======");
            System.out.println("1. Them khoa hoc");
            System.out.println("2. Sua khoa hoc");
            System.out.println("3. Xoa khoa hoc");
            System.out.println("4. Xem danh sach");
            System.out.println("5. Tim theo ten");
            System.out.println("6. Sap xep theo hoc phi");
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
                    timTheoTenUI();
                    break;
                case 6:
                    sapXepTheoHocPhiUI();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lua chon khong hop le");
            }
        } while (choice != 0);
    }

    public static void addUI() {
        System.out.println("====== Them khoa hoc ======");
        long id = khoaHocService.taoIdMoi();
        System.out.println("Nhap ten khoa hoc (vd IELTS 5.5): ");
        String tenKhoaHoc = KhoaHocValidator.inputTenKhoaHoc(null);
        System.out.println("Chon trinh do: ");
        TrinhDo trinhDo = CommonValidator.inputTrinhDo(null);
        System.out.println("Nhap so buoi (1-200): ");
        int soBuoi = KhoaHocValidator.inputSoBuoi(null);
        System.out.println("Nhap hoc phi (100,000 - 100,000,000 VND): ");
        long hocPhi = KhoaHocValidator.inputHocPhi(null);
        khoaHocService.add(new KhoaHoc(id, tenKhoaHoc, trinhDo, soBuoi, hocPhi));
        System.out.println("Da them khoa hoc, id = " + id);
    }

    public static void editUI() {
        System.out.println("====== Sua khoa hoc ======");
        System.out.println("Nhap id khoa hoc can sua: ");
        long id = InputHelper.inputLong();
        try {
            KhoaHoc cu = khoaHocService.findById(id);
            System.out.println("(Nhan Enter de giu gia tri cu)");
            System.out.println("Ten khoa hoc [" + cu.getTenKhoaHoc() + "]: ");
            String tenKhoaHoc = KhoaHocValidator.inputTenKhoaHoc(cu.getTenKhoaHoc());
            System.out.println("Trinh do [" + cu.getTrinhDo().getTenHienThi() + "]: ");
            TrinhDo trinhDo = CommonValidator.inputTrinhDo(cu.getTrinhDo());
            System.out.println("So buoi [" + cu.getSoBuoi() + "]: ");
            int soBuoi = KhoaHocValidator.inputSoBuoi(cu.getSoBuoi());
            System.out.println("Hoc phi [" + cu.getHocPhi() + "]: ");
            long hocPhi = KhoaHocValidator.inputHocPhi(cu.getHocPhi());
            // Tạo object mới rồi mới update, để nếu service từ chối thì dữ liệu cũ không bị đổi dở dang
            khoaHocService.update(id, new KhoaHoc(id, tenKhoaHoc, trinhDo, soBuoi, hocPhi));
            System.out.println("Da cap nhat khoa hoc! (Cac dang ky cu van giu muc hoc phi da chot)");
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay khoa hoc id " + id);
        }
    }

    public static void deleteUI() {
        System.out.println("====== Xoa khoa hoc ======");
        System.out.println("Nhap id khoa hoc can xoa: ");
        long id = InputHelper.inputLong();
        try {
            KhoaHoc khoaHoc = khoaHocService.findById(id);
            System.out.println("Ban co chac muon xoa khoa hoc '" + khoaHoc.getTenKhoaHoc() + "'? (y/n)");
            if (InputHelper.inputYesNo()) {
                khoaHocService.delete(id);
                System.out.println("Da xoa khoa hoc!");
            }
        } catch (DataNotFoundException e) {
            System.out.println("Khong tim thay khoa hoc id " + id);
        } catch (NghiepVuException e) {
            System.out.println("Khong the xoa: " + e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("====== Danh sach khoa hoc ======");
        inDanhSach(khoaHocService.getAll());
    }

    public static void timTheoTenUI() {
        System.out.println("Nhap tu khoa: ");
        String keyword = InputHelper.inputString();
        inDanhSach(khoaHocService.timTheoTen(keyword));
    }

    public static void sapXepTheoHocPhiUI() {
        System.out.println("1. Tang dan   2. Giam dan");
        int luaChon = CommonValidator.inputIntInRange(1, 2, null);
        inDanhSach(khoaHocService.sapXepTheoHocPhi(luaChon == 1));
    }

    public static void inDanhSach(List<KhoaHoc> list) {
        if (list.isEmpty()) {
            System.out.println("(Danh sach trong)");
            return;
        }
        String format = "%-11s| %-22s| %-10s| %-8s| %16s%n";
        System.out.printf(format, "ID", "Ten khoa hoc", "Trinh do", "So buoi", "Hoc phi");
        FormatHelper.printLine(77);
        for (KhoaHoc item : list) {
            System.out.printf(format, item.getId(), item.getTenKhoaHoc(), item.getTrinhDo().getTenHienThi(),
                    item.getSoBuoi(), FormatHelper.formatMoney(item.getHocPhi()));
        }
    }

    // Giống ProductUI.inputCategoryId() của bài mẫu: in danh sách rồi bắt nhập đến khi đúng id.
    // giaTriCu khác null khi đang sửa lớp học (Enter = giữ khóa học cũ).
    public static long chonKhoaHocId(Long giaTriCu) {
        inDanhSach(khoaHocService.getAll());
        System.out.println("Nhap id khoa hoc: ");
        while (true) {
            long id = InputHelper.inputLong(giaTriCu);
            try {
                khoaHocService.findById(id);
                return id;
            } catch (DataNotFoundException e) {
                System.out.println("Id khoa hoc khong ton tai, nhap lai: ");
            }
        }
    }

    // Lấy tên để in ra bảng; nếu dữ liệu lỗi thì in "(khong ton tai)" thay vì làm crash chương trình
    public static String layTenKhoaHoc(long id) {
        try {
            return khoaHocService.findById(id).getTenKhoaHoc();
        } catch (DataNotFoundException e) {
            return "(khong ton tai)";
        }
    }
}
