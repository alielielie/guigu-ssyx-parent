package com.atguigu.ssyx.search.service;

import com.atguigu.ssyx.model.search.SkuEs;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.service
 * @Author: zt
 * @CreateTime: 2023-06-12  13:41
 * @Description:
 */
public interface SkuService {

    //上架
    void upperSku(Long skuId);

    //下架
    void lowerSku(Long skuId);

    //获取爆款商品
    List<SkuEs> findHotSkuList();
}
