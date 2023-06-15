package com.atguigu.ssyx.user.service;

import com.atguigu.ssyx.model.user.User;
import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import com.atguigu.ssyx.vo.user.UserLoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.user.service
 * @Author: zt
 * @CreateTime: 2023-06-15  12:14
 * @Description:
 */
public interface UserService extends IService<User> {

    //判断是否是第一次使用微信授权登录，根据open_id进行判断
    User getUserByOpenId(String openid);

    //5 根据userId查询提货点和团长信息
    LeaderAddressVo getLeaderAddressByUserId(Long userId);

    //获取当前登录用户的信息
    UserLoginVo getUserLoginVo(Long id);
}
