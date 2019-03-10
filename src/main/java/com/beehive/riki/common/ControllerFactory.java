package com.beehive.riki.common;

import java.util.List;

public interface ControllerFactory<T, U> {
    List<T> index();
    T findById(U id);
    void create(T t);
}
