package com.disco.api;

import com.disco.pojo.SpecGroup;
import com.disco.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @ClassName: SpecificationApi
 * @Description: 规格分类api
 * @date: 2023/3/6
 * @author zhb
 */
@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据组id 查询规格分类
     * @param gid
     * @return
     */
    @GetMapping("params")
    public List<SpecParam> queryParams(
        @RequestParam(value = "gid", required = false) Long gid,
        @RequestParam(value = "cid", required = false) Long cid,
        @RequestParam(value = "generic", required = false) Boolean generic,
        @RequestParam(value = "searching", required = false) Boolean searching);


    /**
     * 根据分类id 查询组、组下的规格参数信息
     * @param cid
     * @return
     */
    @GetMapping("group/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid") Long cid);
}
