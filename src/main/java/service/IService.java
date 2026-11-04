package service;

import java.util.List;

/*
Bộ hàm CRUD chung để các class Service cùng triển khai (giống bài mẫu):
+ KhoaHocService, GiangVienService, HocVienService, LopHocService, DangKyService
 */
public interface IService<T> {
    void add(T t);

    void delete(long id);

    void update(long id, T t);

    List<T> getAll();

    int findIndexById(long id);

    T findById(long id);
}
