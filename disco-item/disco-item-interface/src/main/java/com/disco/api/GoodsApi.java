package com.disco.api;

import com.disco.bo.SpuBo;
import com.disco.pojo.Sku;
import com.disco.pojo.Spu;
import com.disco.pojo.SpuDetail;
import com.leyou.common.pojo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: GoodsApi
 * @Description: 商品api
 * @date: 2023/3/5
 * @author zhb
 */
public interface GoodsApi {

    /**
     * 根据条件分页查询spu
     * key 查询条件 主题
     * saleable 是否上架
     * page 页数
     * rows 条数
     * @return
     */
    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
        @RequestParam(value = "key", required = false) String key,
        @RequestParam(value = "saleable", required = false) Boolean saleable,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "rows", defaultValue = "5") Integer rows
    );

    /**
     * 根据spuId获取spuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据spuId获取sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    /**
     * 根据skuId获取sku
     * @param skuId
     * @return
     */
    @GetMapping("sku/{skuId}")
    public Sku querySkuBySkuId(@PathVariable("skuId") Long skuId);

}
