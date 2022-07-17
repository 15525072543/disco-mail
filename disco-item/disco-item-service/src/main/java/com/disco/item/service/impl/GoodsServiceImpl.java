package com.disco.item.service.impl;

import com.disco.item.mapper.BrandMapper;
import com.disco.item.mapper.SpuMapper;
import com.disco.item.service.BrandService;
import com.disco.item.service.CategoryService;
import com.disco.item.service.GoodsService;
import com.disco.pojo.Brand;
import com.disco.pojo.Spu;
import com.disco.pojo.SpuBo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: GoodsServiceImpl
 * @Description: 商品服务层实现类
 * @date: 2022/7/17
 * @author zhb
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private BrandMapper brandMapper;

    @Resource
    private BrandService brandService;

    @Resource
    private CategoryService categoryService;

    public GoodsServiceImpl() {
    }

    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 添加主题查询条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%" + key + "%");
        }
        // 添加上下架查询条件
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        // 添加分页
        PageHelper.startPage(page, rows);
        // 执行查询 获取spu集合
        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        // 将spu集合组装成spuBo集合
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
            // 查询品牌名称
            Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            // 查询分类名称
            List<String> cnames = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            String cname = StringUtils.join(cnames, "-");
            spuBo.setCname(cname);
            return spuBo;
        }).collect(Collectors.toList());
        // 返回pageResule<spuBo>
        return new PageResult<>(spuPageInfo.getTotal(),spuBos);
    }
}
