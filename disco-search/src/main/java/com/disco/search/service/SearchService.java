package com.disco.search.service;

import com.disco.pojo.*;
import com.disco.search.client.BrandClient;
import com.disco.search.client.CategoryClient;
import com.disco.search.client.GoodsClient;
import com.disco.search.client.SpecificationClient;
import com.disco.search.pojo.Goods;
import com.disco.search.pojo.SearchRequest;
import com.disco.search.pojo.SearchResult;
import com.disco.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: SearchService
 * @Description: 搜索服务层
 * @date: 2023/4/30
 * @author zhb
 */
@Service
public class SearchService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Resource
    private GoodsClient goodsClient;

    @Resource
    private CategoryClient categoryClient;

    @Resource
    private BrandClient brandClient;

    @Resource
    private SpecificationClient specificationClient;

    @Resource
    private GoodsRepository goodsRepository;

    /**
     * 条件查询商品实体类
     * @param searchRequest 接收查询条件
     * @return
     */
    public SearchResult searchGoods(SearchRequest searchRequest) {
        if(StringUtils.isBlank(searchRequest.getKey())){
            return null;
        }
        // 1.自定义查询构建起
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 2.添加查询条件
        // MatchQueryBuilder basicQueryBuilder = QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND);
        BoolQueryBuilder basicQueryBuilder = buildBoolQueryBuilder(searchRequest);

        builder.withQuery(basicQueryBuilder);
        // 3.添加分页规则
        builder.withPageable(PageRequest.of(searchRequest.getPage() - 1,searchRequest.getSize()));
        // 4.添加过滤的字段
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        // 添加聚合查询
        String categoryAggName = "categories";
        String brandAggName = "brands";
        builder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        builder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));


        // 5.执行查询
        AggregatedPage<Goods> results = (AggregatedPage<Goods>)this.goodsRepository.search(builder.build());
        //解析聚合查询结果集
        List<Map<String,Object>> categories = getCategoryAggResult(results.getAggregation(categoryAggName));
        List<Brand> brands = getBrandAggResult(results.getAggregation(brandAggName));

        // 获取规格参数聚合结果集
        List<Map<String,Object>> specs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categories) && categories.size() == 1){
            specs = getSpecAggResult((Long)categories.get(0).get("id"),basicQueryBuilder);
        }
        // 6. 封装返回结果
        return new SearchResult(results.getTotalElements(),results.getTotalPages(),results.getContent(),categories,brands,specs);
    }

    /**
     * 创建基础查询，将searchRequest中的key和filter条件，封装到查询条件中
     * 条件： goods中的all字段中包含key，并且，goods中的规格参数值也命中的
     * @param searchRequest
     * @return
     */
    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest searchRequest) {
        // 创建布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 添加all字段的分词查询
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));
        // 添加用户选择的过滤条件
        for (Map.Entry<String, Object> entry : searchRequest.getFilter().entrySet()) {
            String key = entry.getKey();
            if (StringUtils.equals(key,"品牌")){
                key = "brandId";
            }else if(StringUtils.equals(key,"分类")){
                key = "cid3";
            }else{
                key = "specs." + key + ".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termsQuery(key,entry.getValue()));
        }
        return boolQueryBuilder;
    }

    /**
     *
     * @param cid 分类id
     * @param basicQueryBuilder
     * @return
     */
    private List<Map<String, Object>> getSpecAggResult(Long cid, QueryBuilder basicQueryBuilder) {
        //自定义查询对象构造
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本查询条件
        queryBuilder.withQuery(basicQueryBuilder);
        //查询要聚合的规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, cid, null, true);
        //添加规格参数的聚合
        params.forEach(param -> {
            queryBuilder.addAggregation(AggregationBuilders.terms(param.getName()).field("specs." + param.getName() + ".keyword"));
        });
        //添加结果集的过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));
        //执行聚合查询
        AggregatedPage<Goods> search = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());
        //解析聚合结果集
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, Aggregation> entry : search.getAggregations().asMap().entrySet()) {
            Map<String, Object> specMap = new HashMap<>();
            specMap.put("k",entry.getKey());
            List<String> specList = new ArrayList<>();
            StringTerms stringTerms = (StringTerms) entry.getValue();
            for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
                specList.add(bucket.getKeyAsString());
            }
            specMap.put("options",specList);
            resultList.add(specMap);
        }
        return resultList;
    }

    /**
     * 解析拼盘聚合结果集
     * @param aggregation
     * @return
     */
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms)aggregation;

        return terms.getBuckets().stream().map(bucket -> {
            long brandId = bucket.getKeyAsNumber().longValue();
            return brandClient.queryBrandById(brandId);
        }).collect(Collectors.toList());
    }

    /**
     * 解析分类聚合结果集
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms)aggregation;
        return terms.getBuckets().stream().map(bucket -> {
            Map<String,Object> map = new HashMap<>();
            Long id = bucket.getKeyAsNumber().longValue();
            List<String> categoryNames = categoryClient.queryNamesByIds(Arrays.asList(id));
            map.put("id",id);
            map.put("name",categoryNames.get(0));
            return map;
        }).collect(Collectors.toList());
    }

    /**
     * 根据spu构建goods实体
     * @param spu
     * @return
     */
    public Goods buildGoods(Spu spu) throws IOException {
        Goods goods = new Goods();

        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());

        //获取spu下的所有sku必要字段集合
        List<Sku> skus = goodsClient.querySkusBySpuId(spu.getId());
        List<Map<String,Object>> skuMapList = new ArrayList<>();
        List<Long> prices = new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());

            HashMap<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());//skuId
            map.put("title",sku.getTitle());//sku的标题
            map.put("price",sku.getPrice());//sku的价格
            //sku的第一张图片
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            skuMapList.add(map);
        });

        //根据spuId查询分类名称
        List<String> categoryNames = categoryClient.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        //根据spu的brandId查询品牌名称
        Brand brandName = brandClient.queryBrandById(spu.getBrandId());

        /*
         * 拼装spu的所有规格参数
         * */
        // 根据spu的三级分类id查询所有的规格参数
        List<SpecParam> specParams = specificationClient.queryParams(null, spu.getCid3(), null, true);
        // 根据spuId查询spuDetail
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        // 把通用的规格参数反序列化
        Map<String,Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {});
        // 把特殊的规格参数反序列化
        Map<String,List<Object>> specialSpecMap  = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<Object>>>() {});
        Map<String,Object> specs = new HashMap<>();
        specParams.forEach(param -> {
            if(param.getGeneric()){
                // 如果是通用规格类型，就从genericSpecMap中获取通用规格参数值
                String value = genericSpecMap.get(param.getId().toString()).toString();
                //如果参数值时数字类型，需要判断数字的区间
                if (param.getNumeric()){
                    value  = chooseSegment(value, param);
                }
                specs.put(param.getName(),value);
            }else {
                //如果是特殊规格类型，就从specialSpecMap获取特殊规格参数值
                List<Object> value = specialSpecMap.get(param.getId().toString());
                specs.put(param.getName(),value);
            }
        });


        //获取spu下的所有sku集合，并转化为json字符串
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        //获取spu下所有sku的价格集合
        goods.setPrice(prices);
        //拼接all字段，需要标题，分类名称和品牌名称
        goods.setAll(spu.getTitle() + " " + StringUtils.join(categoryNames," ") + " " + brandName);
        //获取所有查询的规格参数 {key：value} {规格参数名：规格参数值}
        goods.setSpecs(specs);
        return goods;

    }

    /**
     * 根据数字类型的参数值，和参数对象，返回参数值的区间描述
     * @param value 数值类型的参数值
     * @param p 参数对象
     * @return 参数值的区间描述
     */
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    public void saveGoods(Long id) throws IOException {
        Spu spu = this.goodsClient.querySpuById(id);
        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }
}

