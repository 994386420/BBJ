package com.bbk.Bean;

/**
 * Created by Administrator on 2018/5/29/029.
 */

public class PuDaoListBean {
    private String endtime;
    private String id;
    private String img;
    private String title;
    private String price;
    private String extra;
    private String number;
    private String bidnum;
    private String url;
    private String bidprice;
    private String bidstatus;//判断功能按钮
    private String bidid;



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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getBidprice() {
        return bidprice;
    }

    public void setBidprice(String bidprice) {
        this.bidprice = bidprice;
    }

    public String getBidnum() {
        return bidnum;
    }

    public void setBidnum(String bidnum) {
        this.bidnum = bidnum;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getBidid() {
        return bidid;
    }

    public void setBidid(String bidid) {
        this.bidid = bidid;
    }

    public String getBidstatus() {
        return bidstatus;
    }

    public void setBidstatus(String bidstatus) {
        this.bidstatus = bidstatus;
    }
}
