package service;

import entity.GiangVien;
import entity.VaiTro;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.GiangVienRepository;

import java.util.List;

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
        this.list.remove(index);
        this.giangVienRepository.write(list);
        UserService.getObject().xoaTaiKhoanCua(VaiTro.GIANG_VIEN, id);
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
