package com.disco.search.pojo;

import com.disco.pojo.Brand;
import com.leyou.common.pojo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: SearchResult
 * @Description: 搜索返回页面实体类
 * @date: 2023/6/30
 * @author zhb
 */
public class SearchResult extends PageResult<Goods> {

    // 分类的聚合结果集{id：xxx,name: xxx}
    private List<Map<String,Object>> categories;

    // 品牌的聚合结果集
    private List<Brand> brands;

    // 规格参数结果集
    /*
    [
        {
            "k":"cpu核数"，
            "options": [
                    "四核","八核","16核"
                ]
        }
    ]*/
    private List<Map<String,Object>> specs;

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
