package com.atguigu.ssyx.payment.service.impl;

import com.atguigu.ssyx.common.constant.RedisConst;
import com.atguigu.ssyx.model.order.PaymentInfo;
import com.atguigu.ssyx.payment.service.PaymentInfoService;
import com.atguigu.ssyx.payment.service.WeixinService;
import com.atguigu.ssyx.payment.utils.ConstantPropertiesUtils;
import com.atguigu.ssyx.payment.utils.HttpClient;
import com.atguigu.ssyx.vo.user.UserLoginVo;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.payment.service.impl
 * @Author: zt
 * @CreateTime: 2023-07-11  16:50
 * @Description:
 */

@Service
public class WeixinServiceImpl implements WeixinService {

    @Resource
    private PaymentInfoService paymentInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    //调用微信支付系统生成预付单
    @Override
    public Map<String, String> createJsapi(String orderNo) {
        //1 向payment_info支付记录表添加记录，目前支付状态：正在支付中
        PaymentInfo paymentInfo = paymentInfoService.getPaymentInfoByOrderNo(orderNo);
        if(paymentInfo == null) {
            paymentInfo = paymentInfoService.savePaymentInfo(orderNo);
        }
        //2 封装微信支付系统接口需要参数
        Map<String, String> paramMap = new HashMap<>();
        //1、设置参数
        paramMap.put("appid", ConstantPropertiesUtils.APPID);
        paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        paramMap.put("body", paymentInfo.getSubject());
        paramMap.put("out_trade_no", paymentInfo.getOrderNo());
        int totalFee = paymentInfo.getTotalAmount().multiply(new BigDecimal(100)).intValue();
        paramMap.put("total_fee", String.valueOf(totalFee));
        paramMap.put("spbill_create_ip", "127.0.0.1");
        paramMap.put("notify_url", ConstantPropertiesUtils.NOTIFYURL);
        paramMap.put("trade_type", "JSAPI");
        //openid
        UserLoginVo userLoginVo = (UserLoginVo) redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + paymentInfo.getUserId());
        if(null != userLoginVo && !StringUtils.isEmpty(userLoginVo.getOpenId())) {
            paramMap.put("openid", userLoginVo.getOpenId());
        } else {
            paramMap.put("openid", "oD7av4igt-00GI8PqsIlg5FROYnI");
        }
        //3 使用HttpClient调用微信支付系统接口
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        //设置参数，xml格式
        try {
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //4 调用微信支付系统接口之后，返回结果
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //5 封装需要数据，包含预付单标识 prepay_id
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", ConstantPropertiesUtils.APPID);
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, ConstantPropertiesUtils.PARTNERKEY);
            //6 返回结果
            Map<String, String> result = new HashMap();
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //调用微信支付系统的接口查询订单支付状态
    @Override
    public Map<String, String> queryStatus(String orderNo) {
        //1 封装数据
        Map paramMap = new HashMap();
        paramMap.put("appid", ConstantPropertiesUtils.APPID);
        paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
        paramMap.put("out_trade_no", orderNo);
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //2 设置请求
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        try {
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            client.setHttps(true);
            client.post();
            //3 得到返回结果
            String xml = client.getContent();
            Map<String, String> stringMap = WXPayUtil.xmlToMap(xml);
            return stringMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
