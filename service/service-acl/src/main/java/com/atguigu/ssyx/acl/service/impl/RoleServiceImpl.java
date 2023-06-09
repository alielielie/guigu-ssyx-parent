package com.atguigu.ssyx.acl.service.impl;

import com.atguigu.ssyx.acl.mapper.RoleMapper;
import com.atguigu.ssyx.acl.service.AdminRoleService;
import com.atguigu.ssyx.acl.service.RoleService;
import com.atguigu.ssyx.model.acl.AdminRole;
import com.atguigu.ssyx.model.acl.Role;
import com.atguigu.ssyx.vo.acl.RoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
 * @CreateTime: 2023-06-08  14:40
 * @Description:
 */

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private AdminRoleService adminRoleService;

    //1 角色列表（条件分页查询）
    @Override
    public IPage<Role> selectRolePage(Page<Role> pageParam, RoleQueryVo roleQueryVo) {
        //获取条件值
        String roleName = roleQueryVo.getRoleName();
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        //判断条件值是否为空，不为空封装查询条件
        if(!StringUtils.isEmpty(roleName)) {
            wrapper.like(Role::getRoleName, roleName);
        }
        //调用方法实现条件查询分页
        IPage<Role> rolePage = baseMapper.selectPage(pageParam, wrapper);
        //返回分页对象
        return rolePage;
    }

    //7 获取所有的角色，根据用户id查询用户分配角色列表
    @Override
    public Map<String, Object> getRoleByAdminId(Long adminId) {
        //1 查询所有的角色
        List<Role> allRoleList = baseMapper.selectList(null);
        //2 根据用户id查询用户分配角色列表
        //2.1 根据用户id查询用户角色关系表，查询用户已经分配的角色id列表
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        List<AdminRole> adminRoleList = adminRoleService.list(wrapper);
        //2.2 通过第一步返回的集合获取到分配的所有角色的id列表
        List<Long> roleIdList = adminRoleList.stream().map(AdminRole::getRoleId).collect(Collectors.toList());
        //2.3 创建新的List集合，存储用户配置角色
        List<Role> assignRoleList = new ArrayList<>();
        //2.4 遍历所有角色的列表，得到每个角色
        //判断所有角色里是否包含已经分配的角色id，封装到2.3的新的集合
        for (Role role : allRoleList) {
            if(roleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }
        }
        //封装到map，进行返回
        Map<String, Object> result = new HashMap<>();
        //所有角色
        result.put("allRolesList", allRoleList);
        //用户已经分配的角色列表
        result.put("assignRoles", assignRoleList);
        return result;
    }

    //8 为用户分配角色
    @Override
    public void saveAdminRole(Long adminId, Long[] roleIds) {
        //1 删除用户已经分配过的角色的数据
        //根据用户id删除admin_role表里对应的数据
        LambdaQueryWrapper<AdminRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminRole::getAdminId, adminId);
        adminRoleService.remove(wrapper);
        //2 重新分配
        //遍历多个角色id，拿着每个角色id和用户id添加进入admin_role表
//        for (Long roleId : roleIds) {
//            AdminRole adminRole = new AdminRole();
//            adminRole.setAdminId(adminId);
//            adminRole.setRoleId(roleId);
//            adminRoleService.save(adminRole);
//        }
        List<AdminRole> list = new ArrayList<>();
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            list.add(adminRole);
        }
        adminRoleService.saveBatch(list);
    }
}
