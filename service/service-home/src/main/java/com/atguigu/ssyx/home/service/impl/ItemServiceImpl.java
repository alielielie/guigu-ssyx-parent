package com.atguigu.ssyx.home.service.impl;

import com.atguigu.ssyx.activity.client.ActivityFeignClient;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.client.search.SkuFeignClient;
import com.atguigu.ssyx.home.service.ItemService;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.home.service.impl
 * @Author: zt
 * @CreateTime: 2023-06-26  13:23
 * @Description:
 */

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private SkuFeignClient skuFeignClient;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    //sku的详情
    @Override
    public Map<String, Object> item(Long skuId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        //根据skuId查询信息
        CompletableFuture<SkuInfoVo> skuInfoCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //远程调用获取sku对应数据
            SkuInfoVo skuInfoVo = productFeignClient.getSkuInfoVo(skuId);
            result.put("skuInfoVo", skuInfoVo);
            return skuInfoVo;
        }, threadPoolExecutor);
        //sku对应的优惠券信息
        CompletableFuture<Void> activityCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用获取优惠券
            Map<String, Object> activityMap = activityFeignClient.findActivityAndCoupon(skuId, userId);
            result.putAll(activityMap);
        }, threadPoolExecutor);
        //更新商品的热度
        CompletableFuture<Void> hotCompletableFuture = CompletableFuture.runAsync(() -> {
            //远程调用更新热度
            skuFeignClient.incrHotScore(skuId);
        }, threadPoolExecutor);
        //任务组合
        CompletableFuture.allOf(skuInfoCompletableFuture, activityCompletableFuture, hotCompletableFuture).join();
        return result;
    }

}
