package com.atguigu.ssyx.activity.mapper;

import com.atguigu.ssyx.model.activity.CouponInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
public interface CouponInfoMapper extends BaseMapper<CouponInfo> {

    //根据skuId和分类id和userId查询优惠券信息
    List<CouponInfo> selectCouponInfoList(@Param("skuId") Long skuId, @Param("categoryId") Long categoryId, @Param("userId") Long userId);

    //根据userId获取用户全部的优惠券
    List<CouponInfo> selectCartCouponInfoList(@Param("userId") Long userId);
}
