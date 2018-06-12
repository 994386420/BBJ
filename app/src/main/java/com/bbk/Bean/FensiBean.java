package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class FensiBean {
    private String id;
    private String status;
    private String money;
    private String invitedname;
    private String sdates;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvitedname() {
        return invitedname;
    }

    public void setInvitedname(String invitedname) {
        this.invitedname = invitedname;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSdates() {
        return sdates;
    }

    public void setSdates(String sdates) {
        this.sdates = sdates;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
