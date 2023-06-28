package com.atguigu.ssyx.home.service;

import java.util.Map;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.home.service
 * @Author: zt
 * @CreateTime: 2023-06-26  13:23
 * @Description:
 */
public interface ItemService {
    //sku的详情
    Map<String, Object> item(Long id, Long userId);
}
