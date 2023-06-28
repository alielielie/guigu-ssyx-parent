package com.atguigu.ssyx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx
 * @Author: zt
 * @CreateTime: 2023-06-25  15:19
 * @Description:
 */

//组合
public class CompletableFutureDemo5 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //1 任务1 返回结果1024
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ":begin");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            int value = 1024;
            System.out.println("任务1:" + value);
            System.out.println(Thread.currentThread().getName() + ":end");
            return value;
        }, executorService);

        //2 任务2 返回结果200
        CompletableFuture<Integer> futureB = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + ":begin");
            int value = 200;
            System.out.println("任务2:" + value);
            System.out.println(Thread.currentThread().getName() + ":end");
            return value;
        }, executorService);

        CompletableFuture<Void> all = CompletableFuture.allOf(futureA, futureB);
        all.get();
        System.out.println("over");

    }

}
