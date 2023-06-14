package com.atguigu.ssyx.acl.controller;

import com.atguigu.ssyx.acl.service.AdminService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.common.result.Result;
import com.atguigu.ssyx.common.utils.MD5;
import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @CreateTime: 2023-06-08  16:19
 * @Description:
 */

@Api(tags = "用户接口")
@RestController
@RequestMapping("/admin/acl/user")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private RoleService roleService;

    //1 用户列表
    @ApiOperation("用户列表")
    @GetMapping("/{current}/{limit}")
    public Result list(@PathVariable Long current, @PathVariable Long limit, AdminQueryVo adminQueryVo) {
        Page<Admin> pageParam = new Page<>(current, limit);
        IPage<Admin> pageModel = adminService.selectPageUser(pageParam, adminQueryVo);
        return Result.ok(pageModel);
    }

    //2 根据id查询用户
    @ApiOperation("根据id查询用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }

    //3 添加用户
    @ApiOperation("添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin) {
        //获取到输入的密码
        String password = admin.getPassword();
        //对输入的密码进行加密MD5
        String passwordMD5 = MD5.encrypt(password);
        //加密后的密码设置到admin对象里
        admin.setPassword(passwordMD5);
        //调用方法添加
        boolean isSuccess = adminService.save(admin);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //4 修改用户
    @ApiOperation("修改用户")
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        boolean isSuccess = adminService.updateById(admin);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //5 id删除
    @ApiOperation("根据id删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean isSuccess = adminService.removeById(id);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //6 批量删除
    @ApiOperation("批量删除用户")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean isSuccess = adminService.removeByIds(idList);
        if(isSuccess){
            return Result.ok(null);
        }else {
            return Result.fail(null);
        }
    }

    //7 获取所有的角色，根据用户id查询用户已经分配角色列表
    @ApiOperation("获取用户角色")
    @GetMapping("/toAssign/{adminId}")
    public Result toAssign(@PathVariable Long adminId) {
        //返回的map中包含两部分数据：所有角色 和 为用户分配的角色列表
        Map<String, Object> map = roleService.getRoleByAdminId(adminId);
        return Result.ok(map);
    }

    //8 为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestParam Long adminId, @RequestParam Long[] roleId) {
        roleService.saveAdminRole(adminId, roleId);
        return Result.ok(null);
    }

}
