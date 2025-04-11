package ru.edme.service;

import java.util.List;

public interface AllService<K, T> {

    T save(T entity);

    T findById(K id);

    List<T> findAll();

    T update(T entity);

    boolean delete(K id);
}
