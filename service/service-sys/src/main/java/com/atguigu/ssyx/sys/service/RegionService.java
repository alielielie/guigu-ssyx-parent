package com.atguigu.ssyx.sys.service;

import com.atguigu.ssyx.model.sys.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地区表 服务类
 * </p>
 *
 * @author zt
 * @since 2023-06-09
 */
public interface RegionService extends IService<Region> {

    //1 根据区域的关键字查询区域列表信息
    List<Region> getRegionByKeyword(String keyword);
}
