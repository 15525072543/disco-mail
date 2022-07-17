package com.disco.pojo;

/**
 * @ClassName: SpuBo
 * @Description: spu使用的业务实体类
 * @date: 2022/7/17
 * @author zhb
 */
public class SpuBo extends Spu{

    private String bname;

    private String cname;

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
