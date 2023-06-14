package com.atguigu.ssyx.activity.service;

import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
public interface CouponInfoService extends IService<CouponInfo> {

    //1 优惠券分页查询
    IPage<CouponInfo> selectPageCouponInfo(Long page, Long limit);

    //3 根据id查询优惠券
    CouponInfo getCouponInfo(Long id);

    //4 根据优惠券id查询规则数据
    Map<String, Object> findCouponRuleList(Long id);

    //5 添加优惠券规则数据
    void saveCouponRule(CouponRuleVo couponRuleVo);
}
