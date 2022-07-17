package com.disco.item.mapper;

import com.disco.pojo.Brand;
import com.disco.pojo.Category;
import org.apache.ibatis.annotations.Insert;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName: BrandMapper
 * @Description: 品牌相关Mapper
 * @date: 2022/5/5
 * @author zhb
 */
public interface BrandMapper extends Mapper<Brand>{

    /**
     * 新增品牌分类中间表
     * @param bid 品牌id
     * @param cid 分类id
     */
    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    void insertCategoryAndBrand(Long bid, Long cid);
}
