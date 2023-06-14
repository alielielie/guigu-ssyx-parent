package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.controller
 * @Author: zt
 * @CreateTime: 2023-06-08  22:45
 * @Description:
 */

@RestController
@RequestMapping("/admin/acl/permission")
@Api(tags = "菜单管理")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    //1 查询所有的菜单
    @ApiOperation("查询所有的菜单")
    @GetMapping
    public Result list() {
        List<Permission> list = permissionService.queryAllPermission();
        return Result.ok(list);
    }

    //2 添加菜单
    @ApiOperation("添加菜单")
    @PostMapping("/save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok(null);
    }

    //3 修改菜单
    @ApiOperation("修改菜单")
    @PutMapping("/update")
    public Result update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok(null);
    }

    //4 递归删除菜单
    @ApiOperation("递归删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        permissionService.removeChildById(id);
        return Result.ok(null);
    }

    //5 获取所有的菜单（树形），根据角色id查询角色已经分配的菜单列表
    @ApiOperation("获取角色菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        //返回的map中包含两部分数据：所有菜单 和 为角色分配的菜单列表
//        Map<String, Object> map = permissionService.getPermissionByRoleId(roleId);
        List<Permission> list = permissionService.getPermissionByRoleId(roleId);
        return Result.ok(list);
    }

}
