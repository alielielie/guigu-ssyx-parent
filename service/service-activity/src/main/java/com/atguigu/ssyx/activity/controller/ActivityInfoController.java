package com.atguigu.ssyx.activity.controller;


import com.atguigu.ssyx.activity.service.ActivityInfoService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.activity.ActivityInfo;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */

@RestController
@RequestMapping("/admin/activity/activityInfo")
public class ActivityInfoController {

    @Resource
    private ActivityInfoService activityInfoService;

    //1 列表
    @GetMapping("/{page}/{limit}")
    public Result list (@PathVariable Long page, @PathVariable Long limit) {
        Page<ActivityInfo> pageParam = new Page<>(page, limit);
        IPage<ActivityInfo> pageModel = activityInfoService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //2 添加活动
    @PostMapping("/save")
    public Result save (@RequestBody ActivityInfo activityInfo) {
        activityInfoService.save(activityInfo);
        return Result.ok(null);
    }

    //3 根据id查
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Long id) {
        ActivityInfo activityInfo = activityInfoService.getById(id);
        activityInfo.setActivityTypeString(activityInfo.getActivityType().getComment());
        return Result.ok(activityInfo);
    }

    //4 根据id修改
    @PutMapping("/update")
    public Result update(@RequestBody ActivityInfo activityInfo){
        activityInfoService.updateById(activityInfo);
        return Result.ok(null);
    }

    //5 根据id删除
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        activityInfoService.removeById(id);
        return Result.ok(null);
    }

    //6 批量删除
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Integer> idList){
        activityInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    //营销活动规则相关接口

    //1 根据活动id获取活动规则数据
    @GetMapping("/findActivityRuleList/{id}")
    public Result findActivityRuleList(@PathVariable Long id) {
        Map<String, Object> activityRuleMap = activityInfoService.findActivityRuleList(id);
        return Result.ok(activityRuleMap);
    }

    //2 在活动里添加规则数据
    @PostMapping("/saveActivityRule")
    public Result saveActivityRule(@RequestBody ActivityRuleVo activityRuleVo) {
        activityInfoService.saveActivityRule(activityRuleVo);
        return Result.ok(null);
    }

    //3 根据关键字查询匹配sku信息
    @GetMapping("/findSkuInfoByKeyword/{keyword}")
    public Result findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        List<SkuInfo> list = activityInfoService.findSkuInfoByKeyword(keyword);
        return Result.ok(list);
    }

}

