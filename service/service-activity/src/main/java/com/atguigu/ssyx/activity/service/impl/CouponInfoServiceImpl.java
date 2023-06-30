package com.atguigu.ssyx.activity.service.impl;

import com.atguigu.ssyx.activity.mapper.CouponInfoMapper;
import com.atguigu.ssyx.activity.mapper.CouponRangeMapper;
import com.atguigu.ssyx.activity.service.CouponInfoService;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.enums.CouponRangeType;
import com.atguigu.ssyx.model.activity.CouponInfo;
import com.atguigu.ssyx.model.activity.CouponRange;
import com.atguigu.ssyx.model.order.CartInfo;
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
import java.math.BigDecimal;
import java.util.*;
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

    //获取购物车可以使用优惠券列表
    @Override
    public List<CouponInfo> findCartCouponInfo(List<CartInfo> cartInfoList, Long userId) {
        //1 根据userId获取用户全部的优惠券
        List<CouponInfo> userAllCouponInfoList = baseMapper.selectCartCouponInfoList(userId);
        if(CollectionUtils.isEmpty(userAllCouponInfoList)) {
            return new ArrayList<>();
        }
        //2 从第一步返回的list集合中获取所有优惠券id列表
        List<Long> couponIdList = userAllCouponInfoList.stream().map(CouponInfo::getId).collect(Collectors.toList());
        //3 查询优惠券对应的范围
        LambdaQueryWrapper<CouponRange> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CouponRange::getCouponId, couponIdList);
        List<CouponRange> couponRangeList = couponRangeMapper.selectList(wrapper);
        //4 获取优惠券id对应的skuId列表
        Map<Long, List<Long>> couponIdToSkuIdMap = this.findCouponIdToSkuIdMap(cartInfoList, couponRangeList);
        //5 遍历全部优惠券集合，判断优惠券类型
        //全场通用 sku 分类
        BigDecimal reduceAmount = new BigDecimal(0);
        CouponInfo optimalCouponInfo = null;
        for (CouponInfo couponInfo : userAllCouponInfoList) {
            //全场通用
            if(CouponRangeType.ALL == couponInfo.getRangeType()) {
                //判断是否满足优惠使用门槛,并且使用后总金额不能小于0
                //计算购物车商品的总价
                BigDecimal totalAmount = computeTotalAmount(cartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0
                        && totalAmount.subtract(couponInfo.getAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }else {
                //根据优惠券id获取对应的skuId列表
                List<Long> skuIdList = couponIdToSkuIdMap.get(couponInfo.getId());
                //满足使用范围的购物项
                List<CartInfo> currentCartInfoList = cartInfoList.stream()
                        .filter(cartInfo -> skuIdList.contains(cartInfo.getSkuId()))
                        .collect(Collectors.toList());
                BigDecimal totalAmount = computeTotalAmount(currentCartInfoList);
                if(totalAmount.subtract(couponInfo.getConditionAmount()).doubleValue() >= 0
                        && totalAmount.subtract(couponInfo.getAmount()).doubleValue() >= 0){
                    couponInfo.setIsSelect(1);
                }
            }
            if (couponInfo.getIsSelect() == 1 && couponInfo.getAmount().subtract(reduceAmount).doubleValue() > 0) {
                reduceAmount = couponInfo.getAmount();
                optimalCouponInfo = couponInfo;
            }
        }
        //6 返回List<CouponInfo>
        if(null != optimalCouponInfo) {
            optimalCouponInfo.setIsOptimal(1);
        }
        return userAllCouponInfoList;
    }

    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal("0");
        for (CartInfo cartInfo : cartInfoList) {
            //是否选中
            if(cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemTotal);
            }
        }
        return total;
    }

    //获取优惠券id对应的skuId列表
    private Map<Long, List<Long>> findCouponIdToSkuIdMap(List<CartInfo> cartInfoList, List<CouponRange> couponRangeList) {
        Map<Long, List<Long>> couponIdToSkuIdMap = new HashMap<>();
        //couponRangeList数据处理，根据优惠券id分组
        Map<Long, List<CouponRange>> couponRangeToRangeListMap = couponRangeList.stream().collect(Collectors.groupingBy(
                CouponRange::getCouponId
        ));
        //遍历map集合
        Iterator<Map.Entry<Long, List<CouponRange>>> iterator = couponRangeToRangeListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, List<CouponRange>> entry = iterator.next();
            Long couponId = entry.getKey();
            List<CouponRange> rangeList = entry.getValue();
            //创建集合set
            Set<Long> skuIdSet = new HashSet<>();
            for (CartInfo cartInfo : cartInfoList) {
                for (CouponRange couponRange : rangeList) {
                    //判断
                    if(couponRange.getRangeType() == CouponRangeType.SKU
                            && couponRange.getRangeId().longValue() == cartInfo.getSkuId().longValue()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    }else if(couponRange.getRangeType() == CouponRangeType.CATEGORY
                                && couponRange.getRangeId().longValue() == cartInfo.getCategoryId()) {
                        skuIdSet.add(cartInfo.getSkuId());
                    }
                }
            }
            couponIdToSkuIdMap.put(couponId, new ArrayList<>(skuIdSet));
        }
        return couponIdToSkuIdMap;
    }
}
