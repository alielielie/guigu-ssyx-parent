package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.controller
 * @Author: zt
 * @CreateTime: 2023-06-07  17:56
 * @Description:
 */

@Api(tags = "登录接口")
@RestController
@RequestMapping("/admin/acl/index")
@CrossOrigin //跨域
public class IndexController {

    //1 login 登录
    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login() {
        //返回token值
        Map<String, String> map = new HashMap<>();
        map.put("token", "token-admin");
        return Result.ok(map);
    }

    //2 getInfo 获取信息
    @ApiOperation("获取信息")
    @GetMapping("/info")
    public Result info() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "admin");
        map.put("avator", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return Result.ok(map);
    }

    //3 logout 退出
    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result logout() {
        return Result.ok(null);
    }

}
