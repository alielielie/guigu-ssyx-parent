package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.PermissionMapper;
import com.atguigu.ssyx.acl.service.PermissionService;
import com.atguigu.ssyx.acl.service.RolePermissionService;
import com.atguigu.ssyx.acl.utils.PermissionHelper;
import com.atguigu.ssyx.model.acl.Permission;
import com.atguigu.ssyx.model.acl.RolePermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.service.impl
 * @Author: zt
 * @CreateTime: 2023-06-08  22:51
 * @Description:
 */

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private RolePermissionService  rolePermissionService;

    //1 查询所有的菜单
    @Override
    public List<Permission> queryAllPermission() {
        //1 查询出所有的菜单
        List<Permission> allPermissionList = baseMapper.selectList(null);
        //2 转换数据格式
        List<Permission> result = PermissionHelper.buildPermission(allPermissionList);
        return result;
    }

    //4 递归删除菜单
    @Override
    public void removeChildById(Long id) {
        //1 创建idList保存所有要删除的菜单的id
        List<Long> idList = new ArrayList<>();
        //根据当前菜单id获取到当前菜单下面的子菜单的id，如果子菜单下面还有子菜单，还要获取到
        //重点：递归找当前菜单下面的子菜单
        this.getAllPermissionId(id, idList);
        //设置当前菜单id
        idList.add(id);
        //调用方法根据多个菜单id删除
        baseMapper.deleteBatchIds(idList);
    }

    //5 获取所有的菜单（树形），根据角色id查询角色已经分配的菜单列表
    @Override
    public List<Permission> getPermissionByRoleId(Long roleId) {
        //查询所有的菜单
        List<Permission> permissionList = baseMapper.selectList(null);
        //查询roleId这个角色分配的菜单
        LambdaQueryWrapper<RolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissionList = rolePermissionService.list(wrapper);
        List<Long> permissionIds = rolePermissionList.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        //分配过的菜单就设置isSelect字段为true
        for (Permission permission : permissionList) {
            if(permissionIds.contains(permission.getId())){
                permission.setSelect(true);
            }
        }
        return permissionList;
    }

    //重点：递归找当前菜单下面所有的子菜单
    //第一个参数是当前菜单id
    //第二个参数是最终封装的list集合，包含所有菜单的id
    private void getAllPermissionId(Long id, List<Long> idList) {
        //根据当前菜单id查询它下面的子菜单
        LambdaQueryWrapper<Permission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Permission::getPid, id);
        List<Permission> childList = baseMapper.selectList(wrapper);
        //递归查询是否还有子菜单，有就继续递归查询
        childList.forEach(item -> {
            //封装菜单id到idList
            idList.add(item.getId());
            //递归
            this.getAllPermissionId(item.getId(), idList);
        });
    }
}
