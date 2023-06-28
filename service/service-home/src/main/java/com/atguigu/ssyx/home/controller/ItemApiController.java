package com.atguigu.ssyx.home.controller;

import com.atguigu.ssyx.common.auth.AuthContextHolder;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.home.service.ItemService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.home.controller
 * @Author: zt
 * @CreateTime: 2023-06-26  13:23
 * @Description:
 */

@Api(tags = "商品详情")
@RestController
@RequestMapping("api/home")
public class ItemApiController {

    @Resource
    private ItemService itemService;

    @GetMapping("/item/{id}")
    public Result index(@PathVariable Long id) {
        Long userId = AuthContextHolder.getUserId();
        System.out.println("id = " + id);
        System.out.println("userId = " + userId);
        Map<String, Object> map = itemService.item(id, userId);
        return Result.ok(map);
    }


}
