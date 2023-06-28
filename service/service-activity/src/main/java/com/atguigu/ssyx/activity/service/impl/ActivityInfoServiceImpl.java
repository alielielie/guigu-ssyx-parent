package com.atguigu.ssyx.activity.service.impl;

import com.atguigu.ssyx.activity.mapper.ActivityInfoMapper;
import com.atguigu.ssyx.activity.mapper.ActivityRuleMapper;
import com.atguigu.ssyx.activity.mapper.ActivitySkuMapper;
import com.atguigu.ssyx.activity.service.ActivityInfoService;
import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.enums.ActivityType;
import com.atguigu.ssyx.model.activity.ActivityInfo;
import com.atguigu.ssyx.model.activity.ActivityRule;
import com.atguigu.ssyx.model.activity.ActivitySku;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {

    @Resource
    private ActivityRuleMapper activityRuleMapper;

    @Resource
    private ActivitySkuMapper activitySkuMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private CouponInfoService couponInfoService;

    @Override
    public IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam) {
        IPage<ActivityInfo> activityInfoIPage = baseMapper.selectPage(pageParam, null);
        //从分页查询对象中获取列表数据
        List<ActivityInfo> activityInfoList = activityInfoIPage.getRecords();
        //遍历activityInfoList集合，得到每个ActivityInfo，封装活动类型到activityTypeString属性
        activityInfoList.stream().forEach(item -> {
            item.setActivityTypeString(item.getActivityType().getComment());
        });
        return activityInfoIPage;
    }

    //1 根据活动id获取活动规则数据
    @Override
    public Map<String, Object> findActivityRuleList(Long id) {
        Map<String, Object> result = new HashMap<>();
        //1 根据活动id查询规则列表 activity_rule
        LambdaQueryWrapper<ActivityRule> wrapperActivityRule = new LambdaQueryWrapper<>();
        wrapperActivityRule.eq(ActivityRule::getActivityId, id);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(wrapperActivityRule);
        result.put("activityRuleList", activityRuleList);
        //2 根据活动id查询使用规则商品skuId列表 activity_sku
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(
                new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, id)
        );
        //获取所有的skuId
        List<Long> skuIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        //2.1 通过远程调用service-product模块接口，根据skuId列表得到商品信息
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuIdList);//远程调用得到 skuInfoList
        System.out.println(skuInfoList);
        result.put("skuInfoList", skuInfoList);
        System.out.println(result);
        return result;
    }

    //2 在活动里添加规则数据
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        Long activityId = activityRuleVo.getActivityId();
        //1 根据活动id先删除之前的规则数据
        //ActivityRule数据删除
        activityRuleMapper.delete(
                new LambdaQueryWrapper<ActivityRule>().eq(ActivityRule::getActivityId, activityId)
        );
        //ActivitySku数据删除
        activitySkuMapper.delete(
                new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, activityId)
        );
        //2 获取规则列表数据
        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        ActivityInfo activityInfo = baseMapper.selectById(activityId);
        for (ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityId);//活动id
            activityRule.setActivityType(activityInfo.getActivityType());//活动类型
            activityRuleMapper.insert(activityRule);
        }
        //3 获取规则范围数据
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        for (ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityId);
            activitySkuMapper.insert(activitySku);
        }
    }

    //3 根据关键字查询匹配sku信息
    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        //1 根据输入的关键字查询sku中匹配的内容列表
        //service-product模块创建接口，根据关键字查询sku匹配内容列表
        //service-activity远程调用得到sku内容列表
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoByKeyword(keyword);
        //如果根据关键字查询不到匹配内容，直接返回空集合
        if(CollectionUtils.isEmpty(skuInfoList)) {
            return skuInfoList;
        }
        //从skuInfoList获取所有sku的id
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        //2 判断添加的商品之前是否参加过活动，如果之前参加过活动，而且活动正在进行中，排除商品
        //查询两张表判断，activity-info和activity-sku，编写sql语句实现
        //判断existSkuIdList是否为空，如果是空的就不执行下面的sql语句，不然报错
        List<Long> existSkuIdList = baseMapper.selectSkuIdListExist(skuIdList);
        //判断逻辑处理：排除已经参加活动的商品
        List<SkuInfo> findSkuList = new ArrayList<>();
        //遍历全部sku列表
        for (SkuInfo skuInfo : skuInfoList) {
            if(!existSkuIdList.contains(skuInfo.getId())) {
                findSkuList.add(skuInfo);
            }
        }
        return findSkuList;
    }

    //根据skuId列表获取促销信息
    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result = new HashMap<>();
        //skuIdList遍历，得到每个skuId
        skuIdList.forEach(skuId -> {
            //根据skuId进行查询，查询sku对应的活动里面的规则列表
            List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
            //数据封装，规则的名称
            if(!CollectionUtils.isEmpty(activityRuleList)){
                List<String> ruleList = new ArrayList<>();
                //把规则的名称处理
                for (ActivityRule activityRule : activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId, ruleList);
            }
        });
        return result;
    }

    //根据skuId获取营销数据和优惠券数据
    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
        Map<String, Object> map = new HashMap<>();
        //1 根据skuId获取sku营销活动，一个活动有多个规则
        List<ActivityRule> activityRuleList = this.findActivityRuleBySkuId(skuId);
        //2 根据skuId和userId查询优惠券信息
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfoList(skuId, userId);
        if(CollectionUtils.isEmpty(couponInfoList)) {
            map.put("couponInfoList", Collections.emptyList());
        }else {
            map.put("couponInfoList", couponInfoList);
        }
        //3 封装到map集合，返回
        map.put("activityRuleList", activityRuleList);
        return map;
    }

    //根据skuId获取活动规则数据
    @Override
    public List<ActivityRule> findActivityRuleBySkuId(Long skuId) {
        List<ActivityRule> activityRuleList = baseMapper.findActivityRule(skuId);
        for (ActivityRule activityRule : activityRuleList) {
            String ruleDesc = this.getRuleDesc(activityRule);
            activityRule.setRuleDesc(ruleDesc);
        }
        return activityRuleList;
    }

    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuffer ruleDesc = new StringBuffer();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }


}
