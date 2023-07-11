package com.atguigu.ssyx.payment.service;

import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.payment.service
 * @Author: zt
 * @CreateTime: 2023-07-11  16:49
 * @Description:
 */

public interface WeixinService {
    //调用微信支付系统生成预付单
    Map<String, String> createJsapi(String orderNo);

    //调用微信支付系统的接口查询订单支付状态
    Map<String, String> queryStatus(String orderNo);
}
