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
