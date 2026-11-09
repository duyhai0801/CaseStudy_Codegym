package service;

import java.util.List;

public interface IService<T> {
    void add(T t);

    void delete(long id);

    void update(long id, T t);

    List<T> getAll();

    int findIndexById(long id);

    T findById(long id);
}
