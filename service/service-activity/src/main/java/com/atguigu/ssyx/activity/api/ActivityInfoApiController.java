package com.atguigu.ssyx.activity.api;

import com.atguigu.ssyx.activity.service.ActivityInfoService;
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

}
