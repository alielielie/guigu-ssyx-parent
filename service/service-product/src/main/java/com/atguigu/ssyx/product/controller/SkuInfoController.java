package com.atguigu.ssyx.product.controller;


import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.product.SkuInfo;
import com.atguigu.ssyx.product.service.SkuInfoService;
import com.atguigu.ssyx.vo.product.SkuInfoQueryVo;
import com.atguigu.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author zt
 * @since 2023-06-11
 */

//LTAI5tBozTBe2deUEyAsGns6
//        lcWQPQyIytbFbK75ZNg0ya6XgR6yUB

@Api(tags = "sku信息接口")
@RestController
@RequestMapping("/admin/product/skuInfo")
public class SkuInfoController {

    @Resource
    private SkuInfoService skuInfoService;

    @ApiOperation("sku列表")
    @GetMapping("/{page}/{limit}")
    public Result list (@PathVariable Long page, @PathVariable Long limit, SkuInfoQueryVo skuInfoQueryVo) {
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> pageModel = skuInfoService.selectPageSkuInfo(pageParam, skuInfoQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("添加商品sku信息")
    @PostMapping("/save")
    public Result save(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation("根据id获取sku信息")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        SkuInfoVo skuInfoVo = skuInfoService.getSkuInfo(id);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation("修改sku信息")
    @PutMapping("/update")
    public Result update(@RequestBody SkuInfoVo skuInfoVo){
        skuInfoService.updateSkuInfo(skuInfoVo);
        return Result.ok(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        skuInfoService.removeById(id);
        return Result.ok(null);
    }

    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        skuInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    @ApiOperation("商品审核")
    @GetMapping("/check/{skuId}/{status}")
    public Result check(@PathVariable Long skuId, @PathVariable Integer status) {
        skuInfoService.check(skuId, status);
        return Result.ok(null);
    }

    @ApiOperation("商品上下架")
    @GetMapping("/publish/{skuId}/{status}")
    public Result publish(@PathVariable Long skuId, @PathVariable Integer status) {
        skuInfoService.publish(skuId, status);
        return Result.ok(null);
    }

    @ApiOperation("新人专享")
    @GetMapping("isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable("skuId") Long skuId,
                              @PathVariable("status") Integer status) {
        skuInfoService.isNewPerson(skuId, status);
        return Result.ok(null);
    }

}

