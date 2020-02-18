package pl.edu.agh.pierogi.dao;

import java.util.List;

public interface GenericDAO<T> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    void delete(Long id);

    void deleteInBatch(List<T> entities);

    T find(Long id);

    List<T> findAll();
}