package com.bbk.Bean;

/**
 * Created by Administrator on 2018/8/06/006.
 */

public class NewUserBean {
    private String goodsInfo;
    private String exp;
    private String username;
    private String keti;
    private String partner;
    private String earn;
    private String invicode;
    private String messages;
    private String imgurl;
    private String buttonlist;
    private String showOrders;//1表示显示订单，没有或者0表示不显示
    private String award;

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getButtonlist() {
        return buttonlist;
    }

    public void setButtonlist(String buttonlist) {
        this.buttonlist = buttonlist;
    }

    public String getShowOrders() {
        return showOrders;
    }

    public void setShowOrders(String showOrders) {
        this.showOrders = showOrders;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getExp() {
        return exp;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public String getUsername() {
        return username;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getEarn() {
        return earn;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGoodsInfo() {
        return goodsInfo;
    }

    public void setEarn(String earn) {
        this.earn = earn;
    }

    public String getInvicode() {
        return invicode;
    }

    public void setGoodsInfo(String goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public String getKeti() {
        return keti;
    }

    public void setInvicode(String invicode) {
        this.invicode = invicode;
    }

    public String getMessages() {
        return messages;
    }

    public void setKeti(String keti) {
        this.keti = keti;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
