package com.atguigu.ssyx.activity.service;

import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    //根据skuId和userId查询优惠券信息
    List<CouponInfo> findCouponInfoList(Long skuId, Long userId);

    //3 获取购物车可以使用优惠券列表
    List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId);

    //获取购物车对应优惠券
    CouponInfo findRangeSkuIdList(List<CartInfo> cartInfoList, Long couponId);

    //更新优惠券使用状态
    void updateCouponInfoUseStatus(Long couponId, Long userId, Long orderId);
}
