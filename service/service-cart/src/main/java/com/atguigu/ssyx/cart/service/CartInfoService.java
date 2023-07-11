package com.atguigu.ssyx.cart.service;

import com.atguigu.ssyx.model.order.CartInfo;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.cart.service
 * @Author: zt
 * @CreateTime: 2023-06-28  17:36
 * @Description:
 */
public interface CartInfoService {

    //添加商品到购物车
    void addToCart(Long userId, Long skuId, Integer skuNum);

    //根据skuId删除购物车中的商品内容
    void deleteCart(Long skuId, Long userId);

    //清空购物车
    void deleteAllCart(Long userId);

    //批量删除购物车 多个skuId
    void batchDeleteCart(List<Long> skuIdList, Long userId);

    //购物车列表
    List<CartInfo> getCartList(Long userId);

    //根据skuId进行选中
    void checkCart(Long userId, Integer isChecked, Long skuId);

    //全选
    void checkAllCart(Long userId, Integer isChecked);

    //批量选中
    void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked);

    //获取当前用户购物车里选中的购物项
    List<CartInfo> getCartCheckedList(Long userId);

    //根据用户id删除选中的购物车中的记录
    void deleteCartChecked(Long userId);
}
