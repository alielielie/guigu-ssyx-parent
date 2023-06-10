package com.atguigu.ssyx.sys.service.impl;

import com.atguigu.ssyx.common.exception.SsyxException;
import com.atguigu.ssyx.common.result.ResultCodeEnum;
import com.atguigu.ssyx.model.sys.RegionWare;
import com.atguigu.ssyx.sys.mapper.RegionWareMapper;
import com.atguigu.ssyx.sys.service.RegionWareService;
import com.atguigu.ssyx.vo.sys.RegionWareQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 城市仓库关联表 服务实现类
 * </p>
 *
 * @author zt
 * @since 2023-06-09
 */
@Service
public class RegionWareServiceImpl extends ServiceImpl<RegionWareMapper, RegionWare> implements RegionWareService {

    //1 开通区域列表
    @Override
    public IPage<RegionWare> selectPageRegionWare(Page<RegionWare> pageParam, RegionWareQueryVo regionWareQueryVo) {
        //1 获取查询条件值
        String keyword = regionWareQueryVo.getKeyword();
        //2 判断条件值是否为空，不为空封装条件
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        if(!StringUtils.isEmpty(keyword)) {
            //条件封装
            //根据区域名称，或者仓库名称进行查询
            wrapper.like(RegionWare::getRegionName, keyword)
                    .or()
                    .like(RegionWare::getWareName, keyword);
        }
        //3 调用方法实现分页查询
        IPage<RegionWare> regionWareIPage = baseMapper.selectPage(pageParam, wrapper);
        //4 返回
        return regionWareIPage;
    }

    //2 添加开通区域
    @Override
    public void saveRegionWare(RegionWare regionWare) {
        //判断区域是否已经开通
        LambdaQueryWrapper<RegionWare> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RegionWare::getRegionId, regionWare.getRegionId());
        Integer count = baseMapper.selectCount(wrapper);
        //已经存在
        if(count > 0) {
            //抛出异常
            throw new SsyxException(ResultCodeEnum.REGION_OPEN);
        }
        baseMapper.insert(regionWare);
    }

    //4 取消开通区域
    @Override
    public void updateStatus(Long id, Integer status) {
        RegionWare regionWare = baseMapper.selectById(id);
        regionWare.setStatus(status);
        baseMapper.updateById(regionWare);
    }
}
