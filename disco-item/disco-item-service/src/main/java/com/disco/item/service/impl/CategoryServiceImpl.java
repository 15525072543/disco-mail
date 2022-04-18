package com.disco.item.service.impl;

import com.disco.item.mapper.CategoryMapper;
import com.disco.item.service.CategoryService;
import com.disco.pojo.Category;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: CategoryServiceImpl
 * @Description: 商品分类服务层实现类
 * @date: 2022/4/18
 * @author zhb
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }
}
