package repository;

import java.util.List;

// Mọi repository đều có 2 việc: đọc file thành List và ghi List ngược lại vào file (giống bài mẫu)
public interface IRepository<T> {
    void write(List<T> list);

    List<T> read();
}
