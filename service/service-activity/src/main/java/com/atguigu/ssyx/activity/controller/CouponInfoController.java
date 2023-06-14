package com.atguigu.ssyx.activity.controller;


import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Resource
    private CouponInfoService couponInfoService;

    //1 优惠券分页查询
    @GetMapping("/{page}/{limit}")
    public Result list (@PathVariable Long page, @PathVariable Long limit) {
        IPage<CouponInfo> pageModel = couponInfoService.selectPageCouponInfo(page, limit);
        return Result.ok(pageModel);
    }

    //2 添加优惠券
    @PostMapping("/save")
    public Result save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok(null);
    }

    //3 根据id查询优惠券
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        CouponInfo couponInfo = couponInfoService.getCouponInfo(id);
        return Result.ok(couponInfo);
    }

    //4 根据优惠券id查询规则数据
    @GetMapping("/findCouponRuleList/{id}")
    public Result findCouponRuleList(@PathVariable Long id) {
        Map<String, Object> map = couponInfoService.findCouponRuleList(id);
        return Result.ok(map);
    }

    //5 添加优惠券规则数据
    @PostMapping("/saveCouponRule")
    public Result saveCouponRule(@RequestBody CouponRuleVo couponRuleVo) {
        couponInfoService.saveCouponRule(couponRuleVo);
        return Result.ok(null);
    }


}

