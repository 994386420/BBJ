package com.bbk.Bean;

/**
 * Created by Administrator on 2018/8/01/001.
 */

public class OrderItembean {
    private String id;
    private String title;
    private String price;
    private String number;
    private String imgurl;
    private String param;
    private String substate;//-1退款中 -3退款完成
    private String plstate;
    private String goodsid;
    private String productstate;//1表示正常，不为1表示下架

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getPlstate() {
        return plstate;
    }

    public void setPlstate(String plstate) {
        this.plstate = plstate;
    }

    public String getSubstate() {
        return substate;
    }

    public void setSubstate(String substate) {
        this.substate = substate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getProductstate() {
        return productstate;
    }

    public void setProductstate(String productstate) {
        this.productstate = productstate;
    }
}
