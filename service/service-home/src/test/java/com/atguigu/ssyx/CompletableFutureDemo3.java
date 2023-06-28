package com.atguigu.ssyx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx
 * @Author: zt
 * @CreateTime: 2023-06-25  15:11
 * @Description:
 */

//计算完成时回调方法
public class CompletableFutureDemo3 {

    public static void main(String[] args) {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("main begin...");
        //CompletableFuture创建异步对象
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("当前线程:" + Thread.currentThread().getName());
            int value = 1024 / 0;
            System.out.println("value = " + value);
            return value;
        }, executorService).whenComplete((rs, exception) -> {
            System.out.println("whenComplete:" + rs);
            System.out.println("exception:" + exception);
        });
        System.out.println("main over...");
    }


}
