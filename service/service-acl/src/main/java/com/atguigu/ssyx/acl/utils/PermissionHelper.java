package com.atguigu.ssyx.acl.utils;

import com.atguigu.ssyx.model.acl.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.utils
 * @Author: zt
 * @CreateTime: 2023-06-09  11:35
 * @Description:
 */

public class PermissionHelper {
    public static List<Permission> buildPermission(List<Permission> allPermissionList) {
        //创建最终数据封装List集合
        List<Permission> trees = new ArrayList<>();
        //遍历所有菜单list集合，得到第一层的数据，pid=0
        for (Permission permission : allPermissionList) {
            //判断pid=0，就是第一层
            if (permission.getPid() == 0) {
                permission.setLevel(1);
                //调用方法，从第一层开始往下找
                trees.add(findChildren(permission, allPermissionList));
            }
        }
        return trees;
    }

    //递归往下去找子节点
    //permission上层节点，从这里往下去找
    //allPermissionList所有菜单
    private static Permission findChildren(Permission permission, List<Permission> allPermissionList) {
        permission.setChildren(new ArrayList<>());
        //遍历allPermissionList所有菜单数据
        //判断:当前节点id = pid 是否一样，是的话封装，递归往下找
        for (Permission it : allPermissionList) {
            if(permission.getId().longValue() == it.getPid().longValue()) {
                int level = permission.getLevel() + 1;
                it.setLevel(level);
                if(permission.getChildren() == null) {
                    permission.setChildren(new ArrayList<>());
                }
                //封装下一层的数据
                permission.getChildren().add(findChildren(it, allPermissionList));
            }
        }
        return permission;
    }
}
