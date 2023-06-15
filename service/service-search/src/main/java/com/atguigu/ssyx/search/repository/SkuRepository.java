package com.atguigu.ssyx.search.repository;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.repository
 * @Author: zt
 * @CreateTime: 2023-06-12  13:42
 * @Description:
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {

    //获取爆款商品
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

}
