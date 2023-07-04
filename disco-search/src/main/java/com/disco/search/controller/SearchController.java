package com.disco.search.controller;

import com.disco.search.pojo.Goods;
import com.disco.search.pojo.SearchRequest;
import com.disco.search.pojo.SearchResult;
import com.disco.search.service.SearchService;
import com.leyou.common.pojo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName: SearchController
 * @Description: 搜索视图层
 * @date: 2023/5/22
 * @author zhb
 */
@RestController
public class SearchController {

    @Resource
    private SearchService searchService;

    /**
     * 根据条件查询商品
     * @param searchRequest 接收参数实体类
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> searchGoods(@RequestBody SearchRequest searchRequest){
        SearchResult pageResult = this.searchService.searchGoods(searchRequest);
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }
}
