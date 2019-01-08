package com.bbk.Bean;


public class DiscountBean {
    private String msg;
    private String mjmoneyprice;
    private String begin;
    private String mjmoney;
    private String id;
    private String end;
    private String state;
    private String mjmoneyCh2;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }
    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getMjmoneyCh2() {
        return mjmoneyCh2;
    }

    public void setMjmoneyCh2(String mjmoneyCh2) {
        this.mjmoneyCh2 = mjmoneyCh2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getMjmoney() {
        return mjmoney;
    }

    public void setMjmoney(String mjmoney) {
        this.mjmoney = mjmoney;
    }

    public String getMjmoneyprice() {
        return mjmoneyprice;
    }

    public void setMjmoneyprice(String mjmoneyprice) {
        this.mjmoneyprice = mjmoneyprice;
    }
}
