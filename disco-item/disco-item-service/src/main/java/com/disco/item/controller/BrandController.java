package com.disco.item.controller;

import com.disco.item.service.BrandService;
import com.disco.pojo.Brand;
import com.leyou.common.pojo.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: BrandController
 * @Description: 品牌相关实体类
 * @date: 2022/5/5
 * @author zhb
 */
@Controller
@RequestMapping("brand")
public class BrandController {

    @Resource
    private BrandService brandService;

    /**
     * 分页查询品牌信息
     * @param key 模糊查询条件
     * @param page 页码
     * @param rows 每页显示条数
     * @param sortBy 排序字段
     * @param desc 升/降 序
     * @return 分页实体类
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandsByPage(
        @RequestParam(value = "key", required = false)String key,
        @RequestParam(value = "page", required = false)Integer page,
        @RequestParam(value = "rows", required = false)Integer rows,
        @RequestParam(value = "sortBy", required = false)String sortBy,
        @RequestParam(value = "desc", required = false)Boolean desc
    ){
        PageResult<Brand> pageResult = this.brandService.queryBrandsByPage(key,page,rows,sortBy,desc);
        if (pageResult == null || CollectionUtils.isEmpty(pageResult.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 新增品牌
     * @param brand 品牌实体类
     * @param cids 分类id
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cids){
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
