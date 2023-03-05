package com.disco.elasticsearch;

import com.disco.elasticsearch.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ClassName: ItemTepository
 * @Description:
 * @date: 2022/12/22
 * @author zhb
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

    List<Item> findByTitle(String s);

    List<Item> findByPriceBetween(Double d1,Double d2);
}
