package com.sxs.spring.web.reactive;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/**
 * @ClassName: FutureBlockingDataLoader
 * @Description: FutureBlockingDataLoader等待程序运行结果
 * @Author: sxs
 * @CreateDate: 2019/02/13 14:42
 * @Version: 1.0
 */
public class FutureBlockingDataLoader extends DataLoader {
    protected void doLoad() {
        ExecutorService executorService = Executors.newFixedThreadPool(3); // 创建线程池
        runCompletely(executorService.submit(super::loadConfigurations));
        runCompletely(executorService.submit(super::loadUsers));
        runCompletely(executorService.submit(super::loadOrders));
        executorService.shutdown();
    }

    private void runCompletely(Future<?> future) {
        try {
            future.get();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        new FutureBlockingDataLoader().load();
    }
}