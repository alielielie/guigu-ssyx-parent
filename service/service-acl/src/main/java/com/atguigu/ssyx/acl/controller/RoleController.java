package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.controller
 * @Author: zt
 * @CreateTime: 2023-06-08  14:36
 * @Description:
 */

@Api(tags = "角色接口")
@RestController
@RequestMapping("/admin/acl/role")
@CrossOrigin
public class RoleController {

    @Resource
    private RoleService roleService;

    //1 角色列表（条件分页查询）
    @ApiOperation("角色条件分页查询")
    @GetMapping("/{current}/{limit}")
    public Result pageList(@PathVariable Long current, @PathVariable Long limit, RoleQueryVo roleQueryVo) {
        //创建page对象，传递当前页和每页记录数
        Page<Role> pageParam = new Page<>(current, limit);
        //调用service方法实现分页查询，返回查询对象
        IPage<Role> pageModel = roleService.selectRolePage(pageParam, roleQueryVo);
        return Result.ok(pageModel);
    }

    //2 根据id查询角色
    @ApiOperation("根据id查询角色")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    //3 添加角色
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody Role role) {
        boolean isSuccess = roleService.save(role);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //4 修改角色
    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result update(@RequestBody Role role){
        boolean isSuccess = roleService.updateById(role);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //5 根据id删除角色
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/remove/{id}")
    public Result remove (@PathVariable Long id) {
        boolean isSuccess = roleService.removeById(id);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //6 批量删除角色
    @ApiOperation("批量删除角色")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = roleService.removeByIds(idList);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

}
