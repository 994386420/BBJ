package com.bbk.Bean;

/**
 * Created by Administrator on 2019/1/22/022.
 */

public class FenSiOrderBean {

    /**
     * username
     * money :
     * notice
     */

    private String username;
    private String money;
    private String notice;
    private String inviteduserid;
    private String msg;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInviteduserid() {
        return inviteduserid;
    }

    public void setInviteduserid(String inviteduserid) {
        this.inviteduserid = inviteduserid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "FenSiOrderBean{" +
                "username='" + username + '\'' +
                ", money='" + money + '\'' +
                ", notice='" + notice + '\'' +
                '}';
    }
}
