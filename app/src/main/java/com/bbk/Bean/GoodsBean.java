package com.bbk.Bean;

/**
 * Created by Administrator on 2018/8/02/002.
 */

public class GoodsBean {
    private String wuliu;
    private String dianpu;
    private String totalnumber;
    private String list;
    private String subprice;
    private String youhui;
    private String liuyan;//留言
    private String leastjin;//当前店铺至少需要金币个数

    public String getLeastjin() {
        return leastjin;
    }

    public void setLeastjin(String leastjin) {
        this.leastjin = leastjin;
    }

    public String getLiuyan() {
        return liuyan;
    }

    public void setLiuyan(String liuyan) {
        this.liuyan = liuyan;
    }

    public String getYouhui() {
        return youhui;
    }

    public void setYouhui(String youhui) {
        this.youhui = youhui;
    }

    public String getDianpu() {
        return dianpu;
    }

    public void setDianpu(String dianpu) {
        this.dianpu = dianpu;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getWuliu() {
        return wuliu;
    }

    public void setWuliu(String wuliu) {
        this.wuliu = wuliu;
    }

    public String getSubprice() {
        return subprice;
    }

    public void setSubprice(String subprice) {
        this.subprice = subprice;
    }

    public String getTotalnumber() {
        return totalnumber;
    }

    public void setTotalnumber(String totalnumber) {
        this.totalnumber = totalnumber;
    }
}
