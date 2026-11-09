package service;

import entity.KhoaHoc;
import lib.errors.DataNotFoundException;
import lib.errors.NghiepVuException;
import repository.KhoaHocRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KhoaHocService implements IService<KhoaHoc> {
    private KhoaHocRepository khoaHocRepository = new KhoaHocRepository();
    private List<KhoaHoc> list;
    private static final KhoaHocService khoaHocService = new KhoaHocService();

    private KhoaHocService() {
        list = khoaHocRepository.read();
    }

    public static KhoaHocService getObject() {
        return khoaHocService;
    }

    public long taoIdMoi() {
        long id = System.currentTimeMillis() / 1000;
        while (daCoId(id)) {
            id++;
        }
        return id;
    }

    private boolean daCoId(long id) {
        for (KhoaHoc item : list) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(KhoaHoc newKhoaHoc) {
        this.list.add(newKhoaHoc);
        this.khoaHocRepository.write(list);
    }

    @Override
    public void delete(long id) {
        int index = this.findIndexById(id);
        if (LopHocService.getObject().coLopThuocKhoaHoc(id)) {
            throw new NghiepVuException("Khoa hoc dang co lop hoc, khong the xoa. Hay xoa cac lop cua khoa nay truoc.");
        }
        this.list.remove(index);
        this.khoaHocRepository.write(list);
    }

    @Override
    public void update(long id, KhoaHoc khoaHoc) {
        int index = this.findIndexById(id);
        this.list.set(index, khoaHoc);
        this.khoaHocRepository.write(list);
    }

    @Override
    public List<KhoaHoc> getAll() {
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
    public KhoaHoc findById(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }

    public List<KhoaHoc> timTheoTen(String keyword) {
        List<KhoaHoc> listFound = new ArrayList<>();
        for (KhoaHoc item : list) {
            if (item.getTenKhoaHoc().toLowerCase().contains(keyword.toLowerCase())) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    public List<KhoaHoc> sapXepTheoHocPhi(boolean tangDan) {
        List<KhoaHoc> ketQua = new ArrayList<>(list);
        ketQua.sort((a, b) -> Long.compare(a.getHocPhi(), b.getHocPhi()));
        if (!tangDan) {
            Collections.reverse(ketQua);
        }
        return ketQua;
    }
}
