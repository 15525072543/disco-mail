package com.disco.discoGoodsWeb.service.impl;

import com.disco.discoGoodsWeb.client.BrandClient;
import com.disco.discoGoodsWeb.client.CategoryClient;
import com.disco.discoGoodsWeb.client.GoodsClient;
import com.disco.discoGoodsWeb.client.SpecificationClient;
import com.disco.pojo.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName: GoodsServiceImpl
 * @Description:
 * @date: 2023/7/8
 * @author zhb
 */
@Service
public class GoodsServiceImpl {

    @Resource
    private BrandClient brandClient;

    @Resource
    private CategoryClient categoryClient;

    @Resource
    private GoodsClient goodsClient;

    @Resource
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model = new HashMap<>();

        //根据spuId查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);

        //组装分类集合 【{“id”：1，“name”：“手机”}】
        List<Map<String,Object>> categories = new ArrayList<>();
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> categoryNames = this.categoryClient.queryNamesByIds(cids);
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",categoryNames.get(i));
            categories.add(map);
        }

        //根据品牌id查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        //根据spuId查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        //根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        //根据spu的cid3查询组信息
        List<SpecGroup> groups = this.specificationClient.queryGroupsWithParam(spu.getCid3());

        //查询该三级分类下的所有特殊规格参数
        Map<Long,String> paramMap = new HashMap<>();
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        params.forEach(param -> {
            paramMap.put(param.getId(),param.getName());
        });

        model.put("spu",spu);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("spuDetail",spuDetail);
        model.put("groups",groups);
        model.put("paramMap",paramMap);
        return model;
    }
}
