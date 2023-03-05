package com.disco.item.controller;

import com.disco.item.service.CategoryService;
import com.disco.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.List;

/**
 * @ClassName: CategoryController
 * @Description: 商品分类controller
 * @date: 2022/4/18
 * @author zhb
 */
@Controller
@RequestMapping("category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 根据父id查询商品分类
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid){
        if (pid == null || pid < 0){
            // 400参数不合法
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoryByPid(pid);
        if (CollectionUtils.isEmpty(categories)){
            // 404 资源服务器未找到
            return ResponseEntity.notFound().build();
        }
        // 200 查询成功
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据id集合查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }
}
