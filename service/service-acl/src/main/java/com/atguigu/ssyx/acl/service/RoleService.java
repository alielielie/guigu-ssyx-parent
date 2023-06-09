package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.service
 * @Author: zt
 * @CreateTime: 2023-06-08  14:39
 * @Description:
 */
public interface RoleService extends IService<Role> {
    //1 角色列表（条件分页查询）
    IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo);

    //7 获取所有的角色，根据用户id查询用户分配角色列表
    Map<String, Object> getRoleByAdminId(Long adminId);

    //8 为用户分配角色
    void saveAdminRole(Long adminId, Long[] roleIds);
}
