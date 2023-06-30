package com.atguigu.ssyx.cart.controller;

import com.atguigu.ssyx.activity.client.ActivityFeignClient;
import com.atguigu.ssyx.cart.service.CartInfoService;
import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.order.OrderConfirmVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.cart.controller
 * @Author: zt
 * @CreateTime: 2023-06-28  17:35
 * @Description:
 */

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Resource
    private CartInfoService cartInfoService;

    @Resource
    private ActivityFeignClient activityFeignClient;

    //添加商品到购物车
    //添加内容：当前登录用户id,skuId,商品数量
    @GetMapping("/addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable Long skuId, @PathVariable Integer skuNum) {
        //获取当前登录用户的id
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.addToCart(userId, skuId, skuNum);
        return Result.ok(null);
    }

    //根据skuId删除购物车中的商品内容
    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable Long skuId) {
        //获取当前登录用户的id
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(skuId, userId);
        return Result.ok(null);
    }

    //清空购物车
    @DeleteMapping("/deleteAllCart")
    public Result deleteAllCart(){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok(null);
    }

    //批量删除购物车 多个skuId
    @DeleteMapping("/batchDeleteCart")
    public Result batchDeleteCart(@RequestBody List<Long> skuIdList){
        // 如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchDeleteCart(skuIdList, userId);
        return Result.ok(null);
    }

    //购物车列表
    @GetMapping("/cartList")
    public Result cartList() {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        return Result.ok(cartInfoList);
    }

    //查询带优惠券的购物车
    @GetMapping("/activityCartList")
    public Result activityCartList() {
        // 获取用户Id
        Long userId = AuthContextHolder.getUserId();
        List<CartInfo> cartInfoList = cartInfoService.getCartList(userId);
        OrderConfirmVo orderTradeVo = activityFeignClient.findCartActivityAndCoupon(cartInfoList, userId);
        return Result.ok(orderTradeVo);
    }

    //根据skuId进行选中
    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable(value = "skuId") Long skuId,
                            @PathVariable(value = "isChecked") Integer isChecked) {
        //获取用户Id
        Long userId = AuthContextHolder.getUserId();
        //调用更新方法
        cartInfoService.checkCart(userId, isChecked, skuId);
        return Result.ok(null);
    }

    //全选
    @GetMapping("/checkAllCart/{isChecked}")
    public Result checkAllCart(@PathVariable(value = "isChecked") Integer isChecked) {
        //获取用户Id
        Long userId = AuthContextHolder.getUserId();
        //调用更新方法
        cartInfoService.checkAllCart(userId, isChecked);
        return Result.ok(null);
    }

    //批量选中
    @PostMapping("/batchCheckCart/{isChecked}")
    public Result batchCheckCart(@RequestBody List<Long> skuIdList, @PathVariable(value = "isChecked") Integer isChecked){
        //如何获取userId
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchCheckCart(skuIdList, userId, isChecked);
        return Result.ok(null);
    }

}
