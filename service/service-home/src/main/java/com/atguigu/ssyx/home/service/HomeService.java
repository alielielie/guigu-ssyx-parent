package com.atguigu.ssyx.home.service;

import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.home.service
 * @Author: zt
 * @CreateTime: 2023-06-15  16:54
 * @Description:
 */
public interface HomeService {

    //首页数据显示接口
    Map<String, Object> homeData(Long userId);

}
