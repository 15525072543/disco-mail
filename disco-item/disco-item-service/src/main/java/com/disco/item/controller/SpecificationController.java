package com.disco.item.controller;

import com.disco.item.service.impl.SpecificationServiceImpl;
import com.disco.pojo.SpecGroup;
import com.disco.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName: SpecificationController
 * @Description: 规格参数服务层Controller
 * @date: 2022/7/12
 * @author zhb
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {

    @Resource
    private SpecificationServiceImpl specificationService;

    /**
     * 根据分类id查询规格参数组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据组id 查询规格分类
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(@RequestParam("gid")Long gid){
        List<SpecParam> params = this.specificationService.queryParams(gid);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }
}
