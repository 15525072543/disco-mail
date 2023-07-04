package com.disco.elasticsearch.test;

import com.disco.bo.SpuBo;
import com.disco.search.SearchServiceApplication;
import com.disco.search.client.GoodsClient;
import com.disco.search.pojo.Goods;
import com.disco.search.repository.GoodsRepository;
import com.disco.search.service.SearchService;
import com.leyou.common.pojo.PageResult;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: ElasticsearchTest
 * @Description:
 * @date: 2023/5/3
 * @author zhb
 */
@SpringBootTest(classes = SearchServiceApplication.class)
@RunWith(SpringRunner.class)
public class ElasticsearchTest {

    @Resource
    private GoodsClient goodsClient;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private SearchService searchService;

    @Resource
    private GoodsRepository goodsRepository;

    @Test
    public void test(){
        //创建索引和映射
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);

        //分页查询spu
        Integer page = 1;
        Integer rows = 100;
        do {
            PageResult<SpuBo> spuList = goodsClient.querySpuByPage(null, null, page, rows);
            List<SpuBo> items = spuList.getItems();
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    Goods goods = searchService.buildGoods(spuBo);
                    return goods;
                } catch (IOException e) {
                    return null;
                }
            }).collect(Collectors.toList());
            //添加进es
            goodsRepository.saveAll(goodsList);
            goodsList.forEach(goods -> {
                System.out.println(goods);
            });
            rows = items.size();
            page++;
        }while (rows == 100);

    }
}
