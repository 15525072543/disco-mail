package com.disco.bo;

import com.disco.pojo.Sku;
import com.disco.pojo.Spu;
import com.disco.pojo.SpuDetail;

import java.util.List;

/**
 * @ClassName: SpuBo
 * @Description: spu使用的业务实体类
 * @date: 2022/7/17
 * @author zhb
 */
public class SpuBo extends Spu {

    private String bname;

    private String cname;

    private List<Sku> skus;

    private SpuDetail spuDetail;

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
