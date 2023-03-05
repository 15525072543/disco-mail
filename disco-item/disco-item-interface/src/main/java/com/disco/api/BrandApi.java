package com.disco.api;

import com.disco.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: BrandApi
 * @Description: 品牌api
 * @date: 2023/3/5
 * @author zhb
 */
@RequestMapping("brand")
public interface BrandApi {

    /**
     * 根据id查询品牌信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id);
}
