package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.service
 * @Author: zt
 * @CreateTime: 2023-06-08  22:51
 * @Description:
 */
public interface PermissionService extends IService<Permission> {

    //1 查询所有的菜单
    List<Permission> queryAllPermission();

    //4 递归删除菜单
    void removeChildById(Long id);

    //5 获取所有的菜单（树形），根据角色id查询角色已经分配的菜单列表
    List<Permission> getPermissionByRoleId(Long roleId);
}
