package com.atguigu.ssyx.payment.controller;

import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.payment.service.PaymentInfoService;
import com.atguigu.ssyx.payment.service.WeixinService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.payment.controller
 * @Author: zt
 * @CreateTime: 2023-07-11  16:49
 * @Description:
 */

@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/payment/weixin")
public class WeixinController {

    @Resource
    private WeixinService weixinService;

    @Resource
    private PaymentInfoService paymentInfoService;

    //调用微信支付系统生成预付单
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@PathVariable("orderNo") String orderNo) {
        Map<String, String> map = weixinService.createJsapi(orderNo);
        return Result.ok(map);
    }

    //查询订单的支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable("orderNo") String orderNo) {
        //1 调用微信支付系统的接口查询订单支付状态
        Map<String, String> resultMap = weixinService.queryStatus(orderNo);
        //2 微信支付系统返回值为null，支付失败
        if(resultMap == null) {
            return Result.build(null, ResultCodeEnum.PAYMENT_FAIL);
        }
        //3 如果微信支付系统返回值，判断支付成功
        if("SUCCESS".equals(resultMap.get("trade_state"))){
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            paymentInfoService.paySuccess(out_trade_no, resultMap);
            return Result.ok(null);
        }
        //4 支付中，等待
        return Result.build(null, ResultCodeEnum.PAYMENT_WAITING);
    }

}
