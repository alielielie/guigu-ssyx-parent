package com.atguigu.ssyx;

import io.swagger.models.auth.In;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx
 * @Author: zt
 * @CreateTime: 2023-06-25  15:19
 * @Description:
 */

//串行化
public class CompletableFutureDemo4 {

    public static void main(String[] args) {
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        //1 任务1 返回结果1024
        CompletableFuture<Integer> futureA = CompletableFuture.supplyAsync(() -> {
            int value = 1024;
            System.out.println("任务1:" + value);
            return value;
        }, executorService);
        //2 任务2 获取任务1的返回结果
        CompletableFuture<Integer> futureB = futureA.thenApplyAsync((res) -> {
            System.out.println("任务2:" + res);
            return res;
        }, executorService);
        //3 任务3 往下执行
        CompletableFuture<Void> futureC = futureA.thenRunAsync(() -> {
            System.out.println("任务3：");
        }, executorService);
    }

}
