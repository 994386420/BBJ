package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/13/013.
 */

public class UserBean {
    private int footprint;
    private int messages;
    private int collect;
    private int jinbi;
    private String sign;
    private String continuous_day;
    private String username;
    private String imgurl;
    private String addjinbi;
    private String exp;
    private String hzinfo;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getFootprint() {
        return footprint;
    }

    public void setAddjinbi(String addjinbi) {
        this.addjinbi = addjinbi;
    }

    public int getJinbi() {
        return jinbi;
    }

    public void setContinuous_day(String continuous_day) {
        this.continuous_day = continuous_day;
    }

    public int getMessages() {
        return messages;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getAddjinbi() {
        return addjinbi;
    }

    public void setFootprint(int footprint) {
        this.footprint = footprint;
    }

    public String getContinuous_day() {
        return continuous_day;
    }

    public void setHzinfo(String hzinfo) {
        this.hzinfo = hzinfo;
    }

    public String getExp() {
        return exp;
    }

    public void setJinbi(int jinbi) {
        this.jinbi = jinbi;
    }

    public String getHzinfo() {
        return hzinfo;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
