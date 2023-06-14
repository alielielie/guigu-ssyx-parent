package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-11
 */
public interface SkuInfoService extends IService<SkuInfo> {

    IPage<SkuInfo> selectPageSkuInfo(Page<SkuInfo> pageParam, SkuInfoQueryVo skuInfoQueryVo);

    void saveSkuInfo(SkuInfoVo skuInfoVo);

    //根据id获取sku信息
    SkuInfoVo getSkuInfo(Long id);

    //修改sku信息
    void updateSkuInfo(SkuInfoVo skuInfoVo);

    //商品审核
    void check(Long skuId, Integer status);

    //商品上下架
    void publish(Long skuId, Integer status);

    //新人专享
    void isNewPerson(Long skuId, Integer status);

    //根据skuId列表得到sku信息列表
    List<SkuInfo> findSkuInfoList(List<Long> skuIdList);

    //根据关键字匹配sku列表
    List<SkuInfo> findSkuInfoByKeyword(String keyword);
}
