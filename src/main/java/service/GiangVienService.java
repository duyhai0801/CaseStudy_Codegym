package service;

import entity.GiangVien;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.GiangVienRepository;

import java.util.List;

// Nghiệp vụ của giảng viên: CRUD. Singleton giống các service khác.
public class GiangVienService implements IService<GiangVien> {
    private GiangVienRepository giangVienRepository = new GiangVienRepository();
    private List<GiangVien> list;
    private static final GiangVienService giangVienService = new GiangVienService();

    private GiangVienService() {
        list = giangVienRepository.read();
    }

    public static GiangVienService getObject() {
        return giangVienService;
    }

    // id = số giây hiện tại (giống bài mẫu: currentTimeMillis() / 1000).
    // Bài mẫu bị trùng id nếu thêm 2 bản ghi trong cùng 1 giây, nên ở đây gặp id đã có thì cộng thêm 1.
    public long taoIdMoi() {
        long id = System.currentTimeMillis() / 1000;
        while (daCoId(id)) {
            id++;
        }
        return id;
    }

    private boolean daCoId(long id) {
        for (GiangVien item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(GiangVien newGiangVien) {
        this.list.add(newGiangVien);
        this.giangVienRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        if (LopHocService.getObject().coLopCuaGiangVien(id)) {
            throw new NghiepVuException("Giang vien dang phu trach lop hoc, khong the xoa.");
        }
        if (UserService.getObject().coTaiKhoanCua(VaiTro.GIANG_VIEN, id)) {
            throw new NghiepVuException("Giang vien dang co tai khoan dang nhap. Hay xoa tai khoan truoc.");
        }
        this.list.remove(index);
        this.giangVienRepository.write(list);
    }

    @Override
    public void update(long id, GiangVien giangVien) {
        int index = this.findIndexById(id);
        this.list.set(index, giangVien);
        this.giangVienRepository.write(list);
    }

    @Override
    public List<GiangVien> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException();
    }

    @Override
    public GiangVien findById(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
