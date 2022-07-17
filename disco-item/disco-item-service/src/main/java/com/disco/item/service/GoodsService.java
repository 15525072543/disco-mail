package com.disco.item.service;

import com.disco.pojo.SpuBo;
import com.leyou.common.pojo.PageResult;

/**
 * @ClassName: GoodsService
 * @Description: 商品服务层接口
 * @date: 2022/7/17
 * @author zhb
 */
public interface GoodsService {

    /**
     * 根据条件分页查询spu
     * key 查询条件 主题
     * saleable 是否上架
     * page 页数
     * rows 条数
     * @return
     */
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);
}
