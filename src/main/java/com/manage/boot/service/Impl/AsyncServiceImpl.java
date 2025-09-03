package com.manage.boot.service.Impl;

import com.manage.boot.service.AsyncService;
import org.springframework.beans.factory.annotation.Qualifier;


import java.util.concurrent.Executor;

public class AsyncServiceImpl implements AsyncService {
    private final Executor coreTaskExecutor;
    private final Executor commonTaskExecutor;

    public AsyncServiceImpl(
            @Qualifier("coreTaskExecutor") Executor coreTaskExecutor,
            @Qualifier("commonTaskExecutor") Executor commonTaskExecutor){
        this.coreTaskExecutor =coreTaskExecutor;
        this.commonTaskExecutor = commonTaskExecutor;
    }

    @Override
    public void executeWithCorePool(Runnable task) {
        coreTaskExecutor.execute(task);
    }

    @Override
    public void executeWithCommonPool(Runnable task) {
        commonTaskExecutor.execute(task);
    }
}
