package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class BrokerageBean {
    private String totalmoney;  // 账户余额
    private String one;  // 结算
    private String three;  // 付款
    private String two;  // 上个月结算预估
    private String four;  // 上个月付款预估
    private String payCount;  //昨日购买数量
    private String yesYongjinSum;  //昨日获得佣金
    private String jinpayCount;  //今日购买数量
    private String jinYongjinSum;  //今日获得佣金
    private String totaltixian;


    public String getJinpayCount() {
        return jinpayCount;
    }

    public void setJinpayCount(String jinpayCount) {
        this.jinpayCount = jinpayCount;
    }

    public String getJinYongjinSum() {
        return jinYongjinSum;
    }

    public void setJinYongjinSum(String jinYongjinSum) {
        this.jinYongjinSum = jinYongjinSum;
    }

    public String getFour() {
        return four;
    }

    public void setFour(String four) {
        this.four = four;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getPayCount() {
        return payCount;
    }

    public void setPayCount(String payCount) {
        this.payCount = payCount;
    }

    public String getThree() {
        return three;
    }

    public void setThree(String three) {
        this.three = three;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getYesYongjinSum() {
        return yesYongjinSum;
    }

    public void setYesYongjinSum(String yesYongjinSum) {
        this.yesYongjinSum = yesYongjinSum;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getTotaltixian() {
        return totaltixian;
    }

    public void setTotaltixian(String totaltixian) {
        this.totaltixian = totaltixian;
    }
}
