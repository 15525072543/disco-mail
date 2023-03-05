package com.disco.item.service.impl;

import com.disco.item.mapper.BrandMapper;
import com.disco.item.service.BrandService;
import com.disco.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.naming.Name;
import java.util.List;

/**
 * @ClassName: BrandServiceImpl
 * @Description: 品牌相关服务层实现类
 * @date: 2022/5/5
 * @author zhb
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        // 根据首字母 或 名称模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name","%" + key +  "%").orEqualTo("letter",key);
        }
        // 添加分页条件
        PageHelper.startPage(page,rows);
        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        //包装返回对象
        List<Brand> brands = this.brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // 新增品牌
        this.brandMapper.insertSelective(brand);
        // 新增品牌分类中间表
        cids.forEach(cid -> {
            this.brandMapper.insertCategoryAndBrand(brand.getId(),cid);
        });

    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.queryBrandsByCid(cid);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }


}
