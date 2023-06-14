package com.atguigu.ssyx.activity.service;

import com.atguigu.ssyx.model.activity.ActivityInfo;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
public interface ActivityInfoService extends IService<ActivityInfo> {

    IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam);

    //1 根据活动id获取活动规则数据
    Map<String, Object> findActivityRuleList(Long id);

    //2 在活动里添加规则数据
    void saveActivityRule(ActivityRuleVo activityRuleVo);

    //3 根据关键字查询匹配sku信息
    List<SkuInfo> findSkuInfoByKeyword(String keyword);
}
