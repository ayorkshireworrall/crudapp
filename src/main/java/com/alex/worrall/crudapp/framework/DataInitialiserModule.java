package com.alex.worrall.crudapp.framework;

public interface DataInitialiserModule {

    public default boolean runInAllEnvs() {
        return false;
    }

    public default String getName() {
        return this.getClass().getName();
    }

    public void initialiseData();
}
