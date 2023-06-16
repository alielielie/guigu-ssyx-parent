package com.atguigu.ssyx.search.repository;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.repository
 * @Author: zt
 * @CreateTime: 2023-06-12  13:42
 * @Description:
 */
@Repository
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {

    //获取爆款商品
    Page<SkuEs> findByOrderByHotScoreDesc(Pageable pageable);

    //判断keyword是否为空，如果为空，根据仓库id + 分类id查询
    Page<SkuEs> findByCategoryIdAndWareId(Long categoryId, Long wareId, Pageable pageable);

    //判断keyword不为空，如果不为空，根据仓库id + keyword查询
    Page<SkuEs> findByKeywordAndWareId(String keyword, Long wareId, Pageable pageable);
}
