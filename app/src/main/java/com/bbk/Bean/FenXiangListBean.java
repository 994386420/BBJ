package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/21/021.
 */

public class FenXiangListBean {
    private String title;
    private String time;
    private String items;
    private String rowkeys;
    private String nickname;
    private String headurl;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getRowkeys() {
        return rowkeys;
    }

    public void setRowkeys(String rowkeys) {
        this.rowkeys = rowkeys;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
