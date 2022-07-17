package com.disco.pojo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: SpuDetail
 * @Description: spu详情实体类
 * @date: 2022/7/17
 * @author zhb
 */
@Table(name = "tb_spu_detail")
public class SpuDetail {

    @Id
    private Long spuId; // spu的id
    private Long description; // 商品描述信息
    private Long spugenericSpecId; // 通用规格参数数据
    private Long specialSpec; // 特有规格参数及可选值信息，json格式
    private Long packingList; // 包装清单
    private Long afterService; // 售后服务

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getDescription() {
        return description;
    }

    public void setDescription(Long description) {
        this.description = description;
    }

    public Long getSpugenericSpecId() {
        return spugenericSpecId;
    }

    public void setSpugenericSpecId(Long spugenericSpecId) {
        this.spugenericSpecId = spugenericSpecId;
    }

    public Long getSpecialSpec() {
        return specialSpec;
    }

    public void setSpecialSpec(Long specialSpec) {
        this.specialSpec = specialSpec;
    }

    public Long getPackingList() {
        return packingList;
    }

    public void setPackingList(Long packingList) {
        this.packingList = packingList;
    }

    public Long getAfterService() {
        return afterService;
    }

    public void setAfterService(Long afterService) {
        this.afterService = afterService;
    }
}
