package com.disco.item.service.impl;

import com.disco.item.mapper.*;
import com.disco.item.service.BrandService;
import com.disco.item.service.CategoryService;
import com.disco.item.service.GoodsService;
import com.disco.pojo.*;
import com.disco.bo.SpuBo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
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
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper stockMapper;

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

    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 新增spu表
        spuBo.setId(null);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);
        // 新增spu详情表
        SpuDetail record = spuBo.getSpuDetail();
        record.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(record);
        // 新增sku和stock
        saveSkuAndStock(spuBo);
    }

    /**
     * 新增sku和stock
     * @param spuBo
     */
    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku表
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);
            // 新增库存表
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        // 根据spuId查询要删除的sku
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            // 删除stock
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });
        // 删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        this.skuMapper.delete(sku);
        // 新增sku和stock
        saveSkuAndStock(spuBo);
        // 更新spu和spuDetail
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
    }
}
