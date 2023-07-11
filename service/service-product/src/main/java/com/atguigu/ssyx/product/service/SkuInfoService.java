package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.product.SkuStockLockVo;
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

    //7 获取新人专享商品
    List<SkuInfo> findNewPersonSkuInfoList();

    //8 根据skuId获取sku信息
    SkuInfoVo getSkuInfoVo(Long skuId);

    //9 验证库存并且锁定库存，保证具备原子性
    Boolean checkAndLock(List<SkuStockLockVo> skuStockLockVoList, String orderNo);

    //扣减库存
    void minusStock(String orderNo);
}
