package com.atguigu.ssyx.order.client;

import com.atguigu.ssyx.model.order.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.order.client
 * @Author: zt
 * @CreateTime: 2023-07-11  17:42
 * @Description:
 */

@FeignClient("service-order")
public interface OrderFeignClient {

    //根据orderNo查询订单信息
    @GetMapping("/api/order/inner/getOrderInfo/{orderNo}")
    public OrderInfo getOrderInfo(@PathVariable String orderNo);

}
