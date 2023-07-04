package com.disco.search.repository;

import com.disco.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @ClassName: GoodsRepository
 * @Description: 商品es查询接口
 * @date: 2023/5/3
 * @author zhb
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
