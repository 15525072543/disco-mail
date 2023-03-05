package com.disco.item.service.impl;

import com.disco.item.mapper.SpecGroupMapper;
import com.disco.item.mapper.SpecParamMapper;
import com.disco.pojo.SpecGroup;
import com.disco.pojo.SpecParam;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: SpecificationServiceImpl
 * @Description: 规格参数服务层实现类
 * @date: 2022/7/12
 * @author zhb
 */
@Service
public class SpecificationServiceImpl {

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Resource
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id 查询规格参数组
     * @param cid 分类id
     * @return 规格参数组
     */
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return specGroupMapper.select(record);
    }

    /**
     * 根据组id 查询规格参数
     * @param gid 规格参数组id
     * @return 规格参数
     */
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);
    }
}
