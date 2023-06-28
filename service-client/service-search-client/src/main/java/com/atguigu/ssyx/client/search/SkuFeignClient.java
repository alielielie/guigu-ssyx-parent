package com.atguigu.ssyx.client.search;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.client.search
 * @Author: zt
 * @CreateTime: 2023-06-15  20:42
 * @Description:
 */
@FeignClient("service-search")
public interface SkuFeignClient {

    //获取爆款商品
    @GetMapping("/api/search/sku/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList();

    //更新商品的热度
    @GetMapping("/api/search/sku/inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable Long skuId);

}
