package com.atguigu.ssyx.search.repository;

import com.atguigu.ssyx.model.search.SkuEs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @BelongsProject: guigu-ssyx-parent
 * @BelongsPackage: com.atguigu.ssyx.search.repository
 * @Author: zt
 * @CreateTime: 2023-06-12  13:42
 * @Description:
 */
public interface SkuRepository extends ElasticsearchRepository<SkuEs, Long> {
}
