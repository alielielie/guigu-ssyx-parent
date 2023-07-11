package com.atguigu.ssyx.payment.service.impl;

import com.atguigu.ssyx.common.exception.SsyxException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.enums.PaymentStatus;
import com.atguigu.ssyx.enums.PaymentType;
import com.atguigu.ssyx.model.order.OrderInfo;
import com.atguigu.ssyx.model.order.PaymentInfo;
import com.atguigu.ssyx.mq.constant.MqConst;
import com.atguigu.ssyx.mq.service.RabbitService;
import com.atguigu.ssyx.order.client.OrderFeignClient;
import com.atguigu.ssyx.payment.mapper.PaymentInfoMapper;
import com.atguigu.ssyx.payment.service.PaymentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.payment.service.impl
 * @Author: zt
 * @CreateTime: 2023-07-11  16:51
 * @Description:
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Resource
    private OrderFeignClient orderFeignClient;

    @Resource
    private RabbitService rabbitService;

    //根据orderNo查询支付记录
    @Override
    public PaymentInfo getPaymentInfoByOrderNo(String orderNo) {
        PaymentInfo paymentInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>()
                        .eq(PaymentInfo::getOrderNo, orderNo)
        );
        return paymentInfo;
    }

    //添加支付记录
    @Override
    public PaymentInfo savePaymentInfo(String orderNo) {
        //远程调用，根据orderNo查询订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderNo);
        if(orderInfo == null) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        //封装到PaymentInfo对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(PaymentType.WEIXIN);
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setOrderNo(orderInfo.getOrderNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
        String subject = "userId:" + orderInfo.getUserId() + "下订单";
        paymentInfo.setSubject(subject);
        //paymentInfo.setTotalAmount(order.getTotalAmount());
        //TODO 为了测试
        paymentInfo.setTotalAmount(new BigDecimal("0.01"));
        //调用方法实现添加
        baseMapper.insert(paymentInfo);
        return paymentInfo;
    }

    //支付成功，更改订单状态，处理支付结果
    @Override
    public void paySuccess(String orderNo, Map<String, String> resultMap) {
        //1 查询当前订单支付记录表状态是否是已经支付
        PaymentInfo paymentInfo = baseMapper.selectOne(
                new LambdaQueryWrapper<PaymentInfo>()
                        .eq(PaymentInfo::getOrderNo, orderNo)
        );
        if(paymentInfo.getPaymentStatus() != PaymentStatus.UNPAID) {
            return;
        }
        //2 如果支付记录表中的支付状态是没有支付，更新
        paymentInfo.setPaymentStatus(PaymentStatus.PAID);
        paymentInfo.setTradeNo(resultMap.get("transaction_id"));
        paymentInfo.setCallbackContent(resultMap.toString());
        baseMapper.updateById(paymentInfo);
        //3 整合mq实现 修改订单记录已经支付，库存扣减
        rabbitService.sendMessage(MqConst.EXCHANGE_PAY_DIRECT, MqConst.ROUTING_PAY_SUCCESS, orderNo);
    }
}
