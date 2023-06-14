package com.atguigu.ssyx.activity.mapper;

import com.atguigu.ssyx.model.activity.ActivityInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 活动表 Mapper 接口
 * </p>
 *
 * @author zt
 * @since 2023-06-12
 */
public interface ActivityInfoMapper extends BaseMapper<ActivityInfo> {

    //如果之前参加过活动，而且活动正在进行中，排除商品
    List<Long> selectSkuIdListExist(@Param("skuIdList") List<Long> skuIdList);
}
