package com.beehive.riki.common;

import java.util.List;

public abstract class ControllerFactoryAdapter<T, U> implements ControllerFactory<T, U> {
    @Override
    public List<T> index() {
        return null;
    }

    @Override
    public T findById(U id) {
        return null;
    }

    @Override
    public void create(T t) {

    }
}
