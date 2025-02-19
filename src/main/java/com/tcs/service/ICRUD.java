package com.tcs.service;

import java.util.List;

public interface ICRUD <T, I>{
    T save(T t);
    T update(I id, T t);
    List<T> findAll();
    T findById(I id, String method);
    void delete(I id);
}
