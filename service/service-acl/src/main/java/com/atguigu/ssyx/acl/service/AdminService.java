package com.atguigu.ssyx.acl.service;

import com.atguigu.ssyx.model.acl.Admin;
import com.atguigu.ssyx.vo.acl.AdminQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.acl.service
 * @Author: zt
 * @CreateTime: 2023-06-08  16:21
 * @Description:
 */
public interface AdminService extends IService<Admin> {
    //1 用户列表
    IPage<Admin> selectPageUser(Page<Admin> pageParam, AdminQueryVo adminQueryVo);
}
