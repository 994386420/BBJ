package com.bbk.Bean;

/**
 * Created by Administrator on 2018/5/21/021.
 */

public class NewHomeCzgBean {
    private String id;
    private String title;
    private String price;//券后价
    private String dianpu;
    private String youhui;
    private String hislowprice;
    private String url;
    private String quan;//满减
    private String zuan;//赚
    private String bprice;//原价
    private String imgurl;
    private String domain;
    private String rowkey;
    private String requestUrl;
    private String sale;

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDianpu() {
        return dianpu;
    }

    public void setDianpu(String dianpu) {
        this.dianpu = dianpu;
    }

    public String getYouhui() {
        return youhui;
    }

    public void setYouhui(String youhui) {
        this.youhui = youhui;
    }

    public String getHislowprice() {
        return hislowprice;
    }

    public void setHislowprice(String hislowprice) {
        this.hislowprice = hislowprice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getZuan() {
        return zuan;
    }

    public void setZuan(String zuan) {
        this.zuan = zuan;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
