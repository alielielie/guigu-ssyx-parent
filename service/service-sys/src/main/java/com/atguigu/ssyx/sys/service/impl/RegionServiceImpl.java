package com.atguigu.ssyx.sys.service.impl;

import com.atguigu.ssyx.model.sys.Region;
import com.atguigu.ssyx.sys.mapper.RegionMapper;
import com.atguigu.ssyx.sys.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author zt
 * @since 2023-06-09
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    //1 根据区域的关键字查询区域列表信息
    @Override
    public List<Region> getRegionByKeyword(String keyword) {
        LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Region::getName, keyword);
        List<Region> list = baseMapper.selectList(wrapper);
        return list;
    }
}
