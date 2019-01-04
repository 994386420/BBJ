package com.bbk.Bean;

/**
 * Created by Administrator on 2018/5/25/025.
 */

public class CheckBean {
    private String hasCps;
    private String price;
    private String rowkey;
    private String domain;
    private String url;
    private String message1;
    private String message2;
    private String findyouhuikey;

    public String getFindyouhuikey() {
        return findyouhuikey;
    }

    public void setFindyouhuikey(String findyouhuikey) {
        this.findyouhuikey = findyouhuikey;
    }

    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getHasCps() {
        return hasCps;
    }

    public void setHasCps(String hasCps) {
        this.hasCps = hasCps;
    }
}
