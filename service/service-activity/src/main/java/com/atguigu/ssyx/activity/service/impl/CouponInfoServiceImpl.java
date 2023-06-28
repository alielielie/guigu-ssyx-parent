package com.atguigu.ssyx.activity.service.impl;

import com.atguigu.ssyx.activity.mapper.CouponInfoMapper;
import com.atguigu.ssyx.activity.mapper.CouponRangeMapper;
import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.enums.CouponRangeType;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.activity.CouponRange;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.vo.activity.CouponRuleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Resource
    private CouponRangeMapper couponRangeMapper;

    @Resource
    private ProductFeignClient productFeignClient;

    //1 优惠券分页查询
    @Override
    public IPage<CouponInfo> selectPageCouponInfo(Long page, Long limit) {
        Page<CouponInfo> pageParam = new Page<>(page, limit);
        IPage<CouponInfo> couponInfoIPage = baseMapper.selectPage(pageParam, null);
        List<CouponInfo> couponInfoList = couponInfoIPage.getRecords();
        couponInfoList.stream().forEach(
                item -> {
                    item.setCouponTypeString(item.getCouponType().getComment());
                    CouponRangeType rangeType = item.getRangeType();
                    if(rangeType != null) {
                        item.setRangeTypeString(rangeType.getComment());
                    }
                }
        );
        return couponInfoIPage;
    }

    //3 根据id查询优惠券
    @Override
    public CouponInfo getCouponInfo(Long id) {
        CouponInfo couponInfo = baseMapper.selectById(id);
        couponInfo.setCouponTypeString(couponInfo.getCouponType().getComment());
        CouponRangeType rangeType = couponInfo.getRangeType();
        if(rangeType != null) {
            couponInfo.setRangeTypeString(rangeType.getComment());
        }
        return couponInfo;
    }

    //4 根据优惠券id查询规则数据
    @Override
    public Map<String, Object> findCouponRuleList(Long id) {
        //根据优惠券id查询优惠券信息 coupon_info表
        CouponInfo couponInfo = baseMapper.selectById(id);
        //根据优惠券id查询coupon_range,查询里面对应range_id
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(
                new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, id)
        );
        //couponRangeList获取所有range_id
        //如果规则类型为SKU,那么range_id就是skuId值
        //如果规则类型为CATEGORY,那么range_id就是分类Id值
        List<Long> rangeIdList = couponRangeList.stream().map(CouponRange::getRangeId).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        //分别判断封装不同数据
        if(!CollectionUtils.isEmpty(rangeIdList)) {
            if(couponInfo.getRangeType() == CouponRangeType.SKU) {
                //如果规则的类型是sku，得到skuid值，通过远程调用根据多个skuId值获取对应sku信息
                List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(rangeIdList);
                result.put("skuInfoList", skuInfoList);
            }else if(couponInfo.getRangeType() == CouponRangeType.CATEGORY) {
                //如果规则的类型是CATEGORY,得到分类id,通过远程调用根据多个分类id值获取对应CATEGORY信息
                List<Category> categoryList = productFeignClient.findCategoryList(rangeIdList);
                result.put("categoryList", categoryList);
            }
        }
        return result;
    }

    //5 添加优惠券规则数据
    @Override
    public void saveCouponRule(CouponRuleVo couponRuleVo) {
        //根据优惠券Id删除规则数据
        couponRangeMapper.delete(
                new LambdaQueryWrapper<CouponRange>().eq(CouponRange::getCouponId, couponRuleVo.getCouponId())
        );
        //更新优惠券基本信息
        CouponInfo couponInfo = baseMapper.selectById(couponRuleVo.getCouponId());
        couponInfo.setRangeType(couponRuleVo.getRangeType());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setAmount(couponRuleVo.getAmount());
        couponInfo.setConditionAmount(couponRuleVo.getConditionAmount());
        couponInfo.setRangeDesc(couponRuleVo.getRangeDesc());
        baseMapper.updateById(couponInfo);
        //添加优惠券新的规则数据
        List<CouponRange> couponRangeList = couponRuleVo.getCouponRangeList();
        for (CouponRange couponRange : couponRangeList) {
            //设置优惠券id
            couponRange.setCouponId(couponRuleVo.getCouponId());
            //添加
            couponRangeMapper.insert(couponRange);
        }
    }

    //根据skuId和userId查询优惠券信息
    @Override
    public List<CouponInfo> findCouponInfoList(Long skuId, Long userId) {
        //远程调用根据skuId获取到skuInfo信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        //根据条件查询：skuId+分类id+userId
        List<CouponInfo> couponInfoList = baseMapper.selectCouponInfoList(skuId, skuInfo.getCategoryId(), userId);
        return couponInfoList;
    }
}
