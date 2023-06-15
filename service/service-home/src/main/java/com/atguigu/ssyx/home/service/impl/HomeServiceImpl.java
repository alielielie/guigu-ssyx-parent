package com.atguigu.ssyx.home.service.impl;

import com.atguigu.ssyx.client.product.ProductFeignClient;
import com.atguigu.ssyx.client.search.SkuFeignClient;
import com.atguigu.ssyx.client.user.UserFeignClient;
import com.atguigu.ssyx.home.service.HomeService;
import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.home.service.impl
 * @Author: zt
 * @CreateTime: 2023-06-15  16:54
 * @Description:
 */

@Service
public class HomeServiceImpl implements HomeService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private ProductFeignClient productFeignClient;

    @Resource
    private SkuFeignClient skuFeignClient;

    //首页数据显示接口
    @Override
    public Map<String, Object> homeData(Long userId) {
        Map<String, Object> result = new HashMap<>();
        //1 根据userId获取当前登录用户提货地址信息
        //远程调用service-user模块接口获取需要的数据
        System.out.println("userId = " + userId);
        //这里注意如果请求的时候userId值为空的话会导致远程调用的路径错误，feign报404错误
        LeaderAddressVo leaderAddressVo = userFeignClient.getLeaderAddressVoById(userId);
        result.put("leaderAddressVo", leaderAddressVo);
        //2 获取所有分类
        //远程调用service-product模块接口
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        result.put("categoryList", categoryList);
        //3 获取新人专享商品
        //远程调用service-product模块接口
        List<SkuInfo> newPersonSkuInfoList = productFeignClient.findNewPersonSkuInfoList();
        result.put("newPersonSkuInfoList", newPersonSkuInfoList);
        //4 获取爆款产品
        ////远程调用service-search模块接口
        //es中hotScore评分降序排序
        List<SkuEs> hotSkuList = skuFeignClient.findHotSkuList();
        result.put("hotSkuList", hotSkuList);
        //5 封装获取的数据到map集合，返回
        return result;
    }

}
