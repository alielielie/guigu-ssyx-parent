package com.atguigu.ssyx.activity.api;

import com.atguigu.ssyx.activity.service.ActivityInfoService;
import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.order.CartInfo;
import com.atguigu.ssyx.vo.order.CartInfoVo;
import com.atguigu.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.activity.api
 * @Author: zt
 * @CreateTime: 2023-06-16  12:45
 * @Description:
 */

@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {

    @Resource
    private ActivityInfoService activityInfoService;

    @Resource
    private CouponInfoService couponInfoService;

    @ApiOperation(value = "根据skuId列表获取促销信息")
    @PostMapping("/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList) {
        return activityInfoService.findActivity(skuIdList);
    }

    @ApiOperation("根据skuId获取营销数据和优惠券数据")
    @GetMapping("/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable Long skuId, @PathVariable Long userId) {
        return activityInfoService.findActivityAndCoupon(skuId, userId);
    }

    //获取购物车里满足条件的优惠券和活动的信息
    @PostMapping("/inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable Long userId) {
        return activityInfoService.findCartActivityAndCoupon(cartInfoList, userId);
    }

    //获取购物车对应规则数据
    @PostMapping("/inner/findCartActivityList")
    public List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList){
        return activityInfoService.findCartActivityList(cartInfoList);
    }

    //获取购物车对应优惠券
    @PostMapping("/inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList, @PathVariable Long couponId) {
        return couponInfoService.findRangeSkuIdList(cartInfoList, couponId);
    }

    //更新优惠券使用状态
    @GetMapping("/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable Long couponId, @PathVariable Long userId, @PathVariable Long orderId){
        couponInfoService.updateCouponInfoUseStatus(couponId, userId, orderId);
        return true;
    }


}
