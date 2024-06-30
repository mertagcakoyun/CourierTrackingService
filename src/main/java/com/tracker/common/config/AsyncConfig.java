package com.tracker.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for enabling asynchronous processing and configuring a thread pool executor.
 * Defines a ThreadPoolTaskExecutor bean with specified core pool size, max pool size, and queue capacity.
 * This setup allows for efficient handling of asynchronous tasks in the application.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 5;
    private static final int QUEUE_CAPACITY = 200;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
