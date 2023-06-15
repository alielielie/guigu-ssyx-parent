package com.atguigu.ssyx.client.user;

import com.atguigu.ssyx.vo.user.LeaderAddressVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.client.user
 * @Author: zt
 * @CreateTime: 2023-06-15  17:17
 * @Description:
 */

@FeignClient(value = "service-user")
public interface UserFeignClient {

    //5 根据userId查询提货点和团长信息
    @ApiOperation("提货点地址信息")
    @GetMapping("/api/user/leader/inner/getUserAddressByUserId/{userId}")
    public LeaderAddressVo getLeaderAddressVoById(@PathVariable("userId") Long userId);

}
