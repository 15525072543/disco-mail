import com.disco.elasticsearch.ElasticsearchApplication;
import com.disco.elasticsearch.ItemRepository;
import com.disco.elasticsearch.pojo.Item;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.Interval;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DocumentTest
 * @Description: 文档测试
 * @date: 2022/12/22
 * @author zhb
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class DocumentTest {

    @Resource
    private ItemRepository itemRepository;

    @Test
    public void create() {
        // 新增单个数据
        // Item item = new Item(1L, "小米手机7", " 手机",
        //     "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        // itemRepository.save(item);

        // 批量新增
        List<Item> list = new ArrayList<>();
        Item item1 = new Item(3L, "小米手机7", " 手机",
            "大米", 3499.00, "http://image.leyou.com/13123.jpg");
        Item item2 = new Item(4L, "小米手机8", " 手机",
            "华为米", 3699.00, "http://image.leyou.com/13123.jpg");
        list.add(item1);
        list.add(item2);
        itemRepository.saveAll(list);
    }

    @Test
    public void update() {
        // 新增id相同的数据，就会修改
        Item item = new Item(1L, "小米手机8", " 手机",
            "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        itemRepository.save(item);
    }

    @Test
    public void delete() {
        Item item = new Item(1L, "小米手机8", " 手机",
            "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        itemRepository.delete(item);
    }

    @Test
    public void query() {
        // 按照价格降序排列
        Iterable<Item> price = itemRepository.findAll(Sort.by("price").descending());
        price.forEach(System.out::println);

        System.out.println("---------------");
        List<Item> byTitle = itemRepository.findByTitle("8");
        byTitle.forEach(System.out::println);

        System.out.println("---------------");
        List<Item> byPriceBetween = itemRepository.findByPriceBetween(1000d, 3500d);
        byPriceBetween.forEach(System.out::println);
    }

    @Test
    public void testQuery() {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "8");
        Iterable<Item> search = this.itemRepository.search(queryBuilder);
        search.forEach(System.out::println);
    }

    @Test
    public void testNativeQuery() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "8"));
        Page<Item> search = this.itemRepository.search(queryBuilder.build());
        search.getContent().forEach(System.out::println);
    }

    @Test
    public void testNativeQueryByPage() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "手机"));
        // 查询第一页，每页展示3条
        int page = 0;
        int size = 1;
        queryBuilder.withPageable(PageRequest.of(page, size));
        Page<Item> items = this.itemRepository.search(queryBuilder.build());
        System.out.println("总条数：" + items.getTotalElements());
        System.out.println("总页数：" + items.getTotalPages());
        System.out.println("每页大小：" + items.getSize());
        items.getContent().forEach(System.out::println);

    }

    @Test
    public void testNativeQueryByOrder() {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        Page<Item> search = this.itemRepository.search(nativeSearchQueryBuilder.build());
        search.forEach(System.out::println);
    }

    @Test
    public void testAggs() {
        // 初始化自定义查询器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand"));
        // 添加聚合过滤，不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        // 执行聚合查询
        AggregatedPage<Item> itemAggregatedPage = (AggregatedPage<Item>)this.itemRepository.search(queryBuilder.build());
        // 解析聚合结果集，根据聚合的类型和字段类型进行强转，brand-是字符串类型的，聚合类型-词条类型，brandAgg-通过聚合名称获取聚合对象
        StringTerms brandAgg = (StringTerms)itemAggregatedPage.getAggregation("brandAgg");
        // 获取桶的合集
        brandAgg.getBuckets().forEach(bucket ->{
            System.out.println("品牌----" + bucket.getKeyAsString());
            System.out.println("数量---" + bucket.getDocCount());
        });
    }

    @Test
    public void testAggsAvg() {
        // 初始化自定义查询器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand")
            .subAggregation(AggregationBuilders.avg("price_avg").field("price")));
        // 添加聚合过滤，不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        // 执行聚合查询
        AggregatedPage<Item> itemAggregatedPage = (AggregatedPage<Item>)this.itemRepository.search(queryBuilder.build());
        // 解析聚合结果集，根据聚合的类型和字段类型进行强转，brand-是字符串类型的，聚合类型-词条类型，brandAgg-通过聚合名称获取聚合对象
        StringTerms brandAgg = (StringTerms)itemAggregatedPage.getAggregation("brandAgg");
        // 获取桶的合集
        brandAgg.getBuckets().forEach(bucket ->{
            System.out.println("品牌----" + bucket.getKeyAsString());
            System.out.println("数量---" + bucket.getDocCount());
            // 获取子集合的map集合：key-聚合名称，value-对应的子聚合对象
            Map<String, Aggregation> aggregationMap = bucket.getAggregations().asMap();
            InternalAvg priceAvg = (InternalAvg)aggregationMap.get("price_avg");
            System.out.println(priceAvg.getValue());
        });
    }

}
