package com.atguigu.ssyx.payment.service;

import com.atguigu.ssyx.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.payment.service
 * @Author: zt
 * @CreateTime: 2023-07-11  16:51
 * @Description:
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    //根据orderNo查询支付记录
    PaymentInfo getPaymentInfoByOrderNo(String orderNo);

    //添加支付记录
    PaymentInfo savePaymentInfo(String orderNo);

    //支付成功，更改订单状态，处理支付结果
    void paySuccess(String out_trade_no, Map<String, String> resultMap);
}
