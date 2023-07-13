package com.disco.item.service;

import com.disco.bo.SpuBo;
import com.disco.pojo.Sku;
import com.disco.pojo.Spu;
import com.disco.pojo.SpuDetail;
import com.leyou.common.pojo.PageResult;

import java.util.List;

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

    /**
     * 新增商品
     * @param spuBo 商品实体类
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据spuId获取spuDetail
     * @param spuId
     * @return
     */
    SpuDetail querySpuDetailBySpuId(Long spuId);

    /**
     * 根据spuId获取sku集合
     * @param spuId
     * @return
     */
    List<Sku> querySkusBySpuId(Long spuId);

    /**
     * 修改商品
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    Spu querySpuById(Long id);
}
