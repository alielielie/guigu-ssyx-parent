package com.atguigu.ssyx.cart.client;

import com.atguigu.ssyx.model.order.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.cart.client
 * @Author: zt
 * @CreateTime: 2023-07-08  12:02
 * @Description:
 */

@FeignClient("service-cart")
public interface CartFeignClient {

    //获取当前用户购物车里选中的购物项
    @GetMapping("/api/cart/inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId);

}
