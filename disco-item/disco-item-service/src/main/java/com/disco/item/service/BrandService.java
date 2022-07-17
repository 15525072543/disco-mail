package com.disco.item.service;

import com.disco.pojo.Brand;
import com.leyou.common.pojo.PageResult;

import java.util.List;

/**
 * @ClassName: BrandService
 * @Description: 品牌相关接口
 * @date: 2022/5/5
 * @author zhb
 */
public interface BrandService {

    /**
     * 分页查询品牌信息
     * @param key 模糊查询条件
     * @param page 页码
     * @param rows 每页显示条数
     * @param sortBy 排序字段
     * @param desc 升/降 序
     * @return 分页实体类
     */
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌
     * @param brand 品牌实体类
     * @param cids 分类id
     */
    void saveBrand(Brand brand, List<Long> cids);


}
