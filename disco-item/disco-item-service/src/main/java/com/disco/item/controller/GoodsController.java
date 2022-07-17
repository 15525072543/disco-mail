package com.disco.item.controller;

import com.disco.item.service.GoodsService;
import com.disco.pojo.SpuBo;
import com.leyou.common.pojo.PageResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @ClassName: GoodsController
 * @Description: 商品视图层
 * @date: 2022/7/17
 * @author zhb
 */
@Controller
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 根据条件分页查询spu
     * key 查询条件 主题
     * saleable 是否上架
     * page 页数
     * rows 条数
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
        @RequestParam(value = "key",required = false) String key,
        @RequestParam(value = "saleable",required = false) Boolean saleable,
        @RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "rows",defaultValue = "5") Integer rows
    ){
        PageResult<SpuBo> result = goodsService.querySpuByPage(key,saleable,page,rows);
        if (result == null || CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
