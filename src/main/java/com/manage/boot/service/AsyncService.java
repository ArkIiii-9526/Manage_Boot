package com.manage.boot.service;

public interface AsyncService {
    public void executeWithCorePool(Runnable task);

    public void executeWithCommonPool(Runnable task);
}
