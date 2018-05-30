package com.bbk.Bean;

/**
 * Created by Administrator on 2018/5/30/030.
 */

public class SendMsgBean {
    private String content;
    private String title;
    private String wzid;
    private String dt;
    private String nickname;
    private String imgurl;
    private String htmlid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlid() {
        return htmlid;
    }

    public void setHtmlid(String htmlid) {
        this.htmlid = htmlid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
