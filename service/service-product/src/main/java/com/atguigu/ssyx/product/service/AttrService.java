package com.atguigu.ssyx.product.service;

import com.atguigu.ssyx.model.product.Attr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-11
 */
public interface AttrService extends IService<Attr> {

    //1 根据平台属性分组id查询
    List<Attr> getAttrListByGroupId(Long groupId);
}
