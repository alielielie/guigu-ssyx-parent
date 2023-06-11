package com.atguigu.ssyx.product.service;


import com.atguigu.ssyx.model.product.SkuImage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品图片 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-11
 */
public interface SkuImageService extends IService<SkuImage> {

    //根据id查询商品图片列表信息
    List<SkuImage> getImageListBySkuId(Long id);
}
