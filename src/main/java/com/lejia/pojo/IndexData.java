package com.lejia.pojo;

import javax.persistence.Table;

@Table(name = "tb_consumer")
public class IndexData  extends BasePojo implements java.io.Serializable{
    private Integer id;

    private String type;

    private Integer order;

    private Integer indexCatId;

    private String data;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getIndexCatId() {
        return indexCatId;
    }

    public void setIndexCatId(Integer indexCatId) {
        this.indexCatId = indexCatId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data == null ? null : data.trim();
    }
}