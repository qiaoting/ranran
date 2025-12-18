package com.ranran.framework.config;

import org.springframework.context.annotation.Configuration;

/**
 * @author ranran
 * 线程池配置，默认没有开启，可以自定义设置参数并开启
 */
@Configuration
public class TaskPoolConfig {
    // ==================== 1. 定时任务线程池 ====================
//    @Bean
//    public ThreadPoolTaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(10);
//        scheduler.setThreadNamePrefix("Ranran-task-");
//        scheduler.initialize();
//        return scheduler;
//    }

    // ==================== 2. 异步任务线程池 ====================
//    @Bean
//    public Executor asyncTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(8);
//        executor.setMaxPoolSize(20);
//        executor.setQueueCapacity(100);
//        executor.setKeepAliveSeconds(60);
//        executor.setThreadNamePrefix("Ranran-Async-");
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
//        return executor;
//    }

}
