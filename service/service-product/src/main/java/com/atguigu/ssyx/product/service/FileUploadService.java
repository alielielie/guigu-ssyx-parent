package com.atguigu.ssyx.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.product.service
 * @Author: zt
 * @CreateTime: 2023-06-11  20:12
 * @Description:
 */
public interface FileUploadService {
    //图片上传的方法
    String uploadFile(MultipartFile file);
}
