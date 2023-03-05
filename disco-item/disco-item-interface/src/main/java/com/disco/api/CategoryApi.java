package com.disco.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.ws.RequestWrapper;
import java.util.List;

/**
 * @ClassName: CategoryApi
 * @Description: 商品分类api
 * @date: 2023/3/5
 * @author zhb
 */
@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据id集合查询分类名称
     * @param ids
     * @return
     */
    @GetMapping("names")
    public List<String> queryNamesByIds(@RequestParam("ids") List<Long> ids);
}
