package com.manage.boot.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
public class ThreadPoolMonitor {
    private final Executor coreTaskExecutor;
    private final Executor commonTaskExecutor;

    public ThreadPoolMonitor(
            @Qualifier("coreTaskExecutor") Executor coreTaskExecutor,
            @Qualifier("commonTaskExecutor") Executor commonTaskExecutor) {
        this.coreTaskExecutor = coreTaskExecutor;
        this.commonTaskExecutor = commonTaskExecutor;
    }

    @PostConstruct
    public void init() {
        new Thread(this::monitorThreadPools).start();
    }

    private void monitorThreadPools() {
        while (true) {
            try {
                Thread.sleep(30000);

                if (coreTaskExecutor instanceof ThreadPoolTaskExecutor) {
                    ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) coreTaskExecutor;
                    logThreadPoolStatus("CoreThreadPool", executor);
                }

                if (commonTaskExecutor instanceof ThreadPoolTaskExecutor) {
                    ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) commonTaskExecutor;
                    logThreadPoolStatus("CoreThreadPool", executor);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void logThreadPoolStatus(String poolName, ThreadPoolTaskExecutor executor) {
        ThreadPoolExecutor pool = executor.getThreadPoolExecutor();
        log.info("ThreadPool Status:活动线程：{},核心线程：{},最大线程：{},队列大小：{},完成任务：{}",poolName,pool.getActiveCount(),pool.getCorePoolSize(),pool.getQueue().size() + "/" + executor.getQueueCapacity(),pool.getCompletedTaskCount());
    }

}
