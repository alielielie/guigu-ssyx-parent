package com.atguigu.ssyx.search.controller;

import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.search.SkuEs;
import com.atguigu.ssyx.search.service.SkuService;
import com.atguigu.ssyx.vo.search.SkuEsQueryVo;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.controller
 * @Author: zt
 * @CreateTime: 2023-06-12  13:40
 * @Description:
 */

@RestController
@RequestMapping("api/search/sku")
public class SkuApiController {

    @Resource
    private SkuService skuService;

    //1 上架
    @GetMapping("/inner/upperSku/{skuId}")
    public Result upperSku(@PathVariable Long skuId) {
        skuService.upperSku(skuId);
        return Result.ok(null);
    }

    //2 下架
    @GetMapping("/inner/lowerSku/{skuId}")
    public Result lowerSku(@PathVariable Long skuId) {
        skuService.lowerSku(skuId);
        return Result.ok(null);
    }

    //获取爆款商品
    @GetMapping("/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList() {
        return skuService.findHotSkuList();
    }

    //查询分类商品
    @GetMapping("/{page}/{limit}")
    public Result listSku(@PathVariable Integer page, @PathVariable Integer limit, SkuEsQueryVo skuEsQueryVo) {
        //创建pageable对象，0代表第一页
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<SkuEs> pageModel = skuService.search(pageable, skuEsQueryVo);
        return Result.ok(pageModel);
    }

    //更新商品的热度
    @GetMapping("/inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable Long skuId) {
        skuService.incrHotScore(skuId);
        return true;
    }

}
