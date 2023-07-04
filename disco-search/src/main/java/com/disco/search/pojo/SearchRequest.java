package com.disco.search.pojo;

import java.util.Map;

/**
 * @ClassName: SearchRequest
 * @Description: 搜索参数接收实体类
 * @date: 2023/5/22
 * @author zhb
 */
public class SearchRequest {
    private String key;// 搜索条件

    private Integer page;// 当前页

    private Map<String,Object> filter;// 页面过滤条件

    private static final Integer DEFAULT_SIZE = 20;// 每页大小，不从页面接收，而是固定大小
    private static final Integer DEFAULT_PAGE = 1;// 默认页

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page == null){
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }
}
