package com.disco.item.service;

import com.disco.item.mapper.CategoryMapper;
import com.disco.pojo.Category;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: CategoryService
 * @Description: 商品分类服务层接口
 * @date: 2022/4/18
 * @author zhb
 */
public interface CategoryService {

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return 子节点集合
     */
    List<Category> queryCategoryByPid(Long pid);

}
