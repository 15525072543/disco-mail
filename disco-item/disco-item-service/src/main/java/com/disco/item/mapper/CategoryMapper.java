package com.disco.item.mapper;

import com.disco.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ClassName: CategoryMapper
 * @Description: 商品分类Mapper
 * @date: 2022/4/18
 * @author zhb
 */
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}
