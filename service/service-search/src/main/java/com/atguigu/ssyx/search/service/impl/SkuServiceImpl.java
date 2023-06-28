package com.atguigu.ssyx.search.service.impl;

import com.atguigu.ssyx.activity.client.ActivityFeignClient;
import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.enums.SkuType;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.search.repository.SkuRepository;
import com.atguigu.ssyx.search.service.SkuService;
import com.atguigu.ssyx.vo.search.SkuEsQueryVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.service.impl
 * @Author: zt
 * @CreateTime: 2023-06-12  13:41
 * @Description:
 */

@Service
public class SkuServiceImpl implements SkuService {

    @Resource
    private SkuRepository skuRepository;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private ActivityFeignClient activityFeignClient;

    @Resource
    private RedisTemplate redisTemplate;

    //上架
    @Override
    public void upperSku(Long skuId) {
        //1 通过远程调用,根据skuId获取相关信息
        SkuInfo skuInfo = productFeignClient.getSkuInfo(skuId);
        if(skuInfo == null) {
            return;
        }
        Category category = productFeignClient.getCategory(skuInfo.getCategoryId());
        //2 获取数据封装SkuEs对象
        SkuEs skuEs = new SkuEs();
        //封装分类
        if(category != null) {
            skuEs.setCategoryId(category.getId());
            skuEs.setCategoryName(category.getName());
        }
        //封装sku信息
        skuEs.setId(skuInfo.getId());
        skuEs.setKeyword(skuInfo.getSkuName()+","+skuEs.getCategoryName());
        skuEs.setWareId(skuInfo.getWareId());
        skuEs.setIsNewPerson(skuInfo.getIsNewPerson());
        skuEs.setImgUrl(skuInfo.getImgUrl());
        skuEs.setTitle(skuInfo.getSkuName());
        if(skuInfo.getSkuType() == SkuType.COMMON.getCode()) {//普通商品
            skuEs.setSkuType(0);
            skuEs.setPrice(skuInfo.getPrice().doubleValue());
            skuEs.setStock(skuInfo.getStock());
            skuEs.setSale(skuInfo.getSale());
            skuEs.setPerLimit(skuInfo.getPerLimit());
        }
        //3 调用方法添加ES
        skuRepository.save(skuEs);
    }

    //下架
    @Override
    public void lowerSku(Long skuId) {
        skuRepository.deleteById(skuId);
    }

    //获取爆款商品
    @Override
    public List<SkuEs> findHotSkuList() {
        //find read get开头
        //关联条件关键字
        //0代表第一页
        Pageable pageable = PageRequest.of(0, 10);
        Page<SkuEs> pageModel = skuRepository.findByOrderByHotScoreDesc(pageable);
        List<SkuEs> skuEsList = pageModel.getContent();
        return skuEsList;
    }

    //查询分类商品
    @Override
    public Page<SkuEs> search(Pageable pageable, SkuEsQueryVo skuEsQueryVo) {
        //1 向skuEsQueryVo设置wareId，当前登录用户的仓库id
        skuEsQueryVo.setWareId(AuthContextHolder.getWareId());
        Page<SkuEs> pageModel = null;
        //2 调用skuRepository方法，根据springData命名规则定义方法进行条件查询
        String keyword = skuEsQueryVo.getKeyword();
        if(StringUtils.isEmpty(keyword)){
            //判断keyword是否为空，如果为空，根据仓库id + 分类id查询
            pageModel = skuRepository.findByCategoryIdAndWareId(skuEsQueryVo.getCategoryId(), skuEsQueryVo.getWareId(), pageable);
        }else {
            //判断keyword不为空，如果不为空，根据仓库id + keyword查询
            pageModel = skuRepository.findByKeywordAndWareId(skuEsQueryVo.getKeyword(), skuEsQueryVo.getWareId(), pageable);
        }
        //3 查询商品参加优惠活动
        List<SkuEs> skuEsList = pageModel.getContent();
        if(!CollectionUtils.isEmpty(skuEsList)){
            //遍历skuEsList，得到所有skuId
            List<Long> skuIdList = skuEsList.stream().map(SkuEs::getId).collect(Collectors.toList());
            //根据skuIdList进行远程调用，调用service-activity里面的接口得到数据
            //返回Map<Long, List<String>>
            //map集合中的key就是skuId值，Long类型
            //map集合中的value是list集合，sku参与活动里面有多个规则
            //一个商品参加一个活动，一个活动里面可以有多个规则
            //比如：中秋节满减活动
            //一个活动可以有多个规则：中秋节满减活动有两个规则：满20减1，满58减5
            Map<Long, List<String>> skuIdToRuleListMap = activityFeignClient.findActivity(skuIdList);//远程调用
            //封装获取数据到SkuEs
            if(skuIdToRuleListMap != null) {
                skuEsList.forEach(skuEs -> {
                    skuEs.setRuleList(skuIdToRuleListMap.get(skuEs.getId()));
                });
            }
        }
        return pageModel;
    }

    //更新商品的热度
    @Override
    public void incrHotScore(Long skuId) {
        String key = "hotScore";
        //redis保存数据，每次+1
        Double hotScore = redisTemplate.opsForZSet().incrementScore(key, "skuId:" + skuId, 1);
        //规则
        if(hotScore % 10 == 0) {
            //更新es
            Optional<SkuEs> optional = skuRepository.findById(skuId);
            SkuEs skuEs = optional.get();
            skuEs.setHotScore(Math.round(hotScore));
            //save方法传的有id值就是更新，没有就是添加
            skuRepository.save(skuEs);
        }
    }
}
