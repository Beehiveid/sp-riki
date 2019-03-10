package com.beehive.riki.common;

import java.util.List;

public interface ServiceFactory<T, U> {
    T findById(U id);
    List<T> findAll();
    void save(T t);
}
