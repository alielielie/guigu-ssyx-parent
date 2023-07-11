package com.atguigu.ssyx.product.api;

import com.atguigu.ssyx.model.product.Category;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.service.CategoryService;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.atguigu.ssyx.vo.product.SkuStockLockVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.product.api
 * @Author: zt
 * @CreateTime: 2023-06-12  13:50
 * @Description:
 */

@RestController
@RequestMapping("/api/product")
public class ProductInnerController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private SkuInfoService skuInfoService;

    //1 根据分类id获取分类信息
    @GetMapping("/inner/getCategory/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId) {
        return categoryService.getById(categoryId);
    }

    //2 根据skuid获取sku信息
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable Long skuId) {
        return skuInfoService.getById(skuId);
    }

    //3 根据skuId列表得到sku信息列表
    @PostMapping("/inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> skuIdList) {
        System.out.println(skuIdList);
        return skuInfoService.findSkuInfoList(skuIdList);
    }

    //4 根据关键字匹配sku列表
    @GetMapping("/inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoByKeyword(@PathVariable("keyword") String keyword) {
        return skuInfoService.findSkuInfoByKeyword(keyword);
    }

    //5 根据分类id列表获取分类信息
    @PostMapping("/inner/findCategoryList")
    public List<Category> findCategoryList(@RequestBody List<Long> categoryIdList) {
        return categoryService.listByIds(categoryIdList);
    }

    //6 获取所有分类
    @GetMapping("/inner/findAllCategoryList")
    public List<Category> findAllCategoryList() {
        return categoryService.list();
    }

    //7 获取新人专享商品
    @GetMapping("/inner/findNewPersonSkuInfoList")
    public List<SkuInfo> findNewPersonSkuInfoList() {
        List<SkuInfo> list = skuInfoService.findNewPersonSkuInfoList();
        return list;
    }

    //8 根据skuId获取sku信息
    @GetMapping("/inner/getSkuInfoVo/{skuId}")
    public SkuInfoVo getSkuInfoVo(@PathVariable Long skuId) {
        return skuInfoService.getSkuInfoVo(skuId);
    }

    //9 验证库存并且锁定库存，保证具备原子性
    @PostMapping("/inner/checkAndLock/{orderNo}")
    public Boolean checkAndLock(@RequestBody List<SkuStockLockVo> skuStockLockVoList, @PathVariable String orderNo) {
        return skuInfoService.checkAndLock(skuStockLockVoList, orderNo);
    }

}
