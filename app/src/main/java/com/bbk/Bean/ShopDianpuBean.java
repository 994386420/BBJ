package com.bbk.Bean;

/**
 * Created by Administrator on 2018/7/31/031.
 */

public class ShopDianpuBean {
    private String id;
    private String title;
    private String price;
    private String kucun;
    private String dianpu;
    private String imgurl;
    private String sale;
    private String dianpulogo;
    private String dianpuid;
    private String bprice;
    private String leastjin;//至少使用多少个金币


    public String getLeastjin() {
        return leastjin;
    }

    public void setLeastjin(String leastjin) {
        this.leastjin = leastjin;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getDianpulogo() {
        return dianpulogo;
    }

    public void setDianpulogo(String dianpulogo) {
        this.dianpulogo = dianpulogo;
    }

    public String getDianpuid() {
        return dianpuid;
    }

    public void setDianpuid(String dianpuid) {
        this.dianpuid = dianpuid;
    }

    public String getDianpu() {
        return dianpu;
    }

    public void setDianpu(String dianpu) {
        this.dianpu = dianpu;
    }

    public String getKucun() {
        return kucun;
    }

    public void setKucun(String kucun) {
        this.kucun = kucun;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
