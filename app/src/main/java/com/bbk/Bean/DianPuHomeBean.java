package com.bbk.Bean;

/**
 * Created by Administrator on 2018/8/02/002.
 */

public class DianPuHomeBean {
    private String hotlist;
    private String tag;
    private String types;
    private String brand;
    private String banner;
    private String guanggao;

    public String getGuanggao() {
        return guanggao;
    }

    public void setGuanggao(String guanggao) {
        this.guanggao = guanggao;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getHotlist() {
        return hotlist;
    }

    public void setHotlist(String hotlist) {
        this.hotlist = hotlist;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
