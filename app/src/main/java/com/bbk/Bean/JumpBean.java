package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class JumpBean {
    private String imgs;// 轮播大图  ["aaaa","bbb"]
    private String title;
    private String shareurl;//分享链接 （淘宝为陶口令，京东为转换后的url）
    private String rowkey;
    private String price;
    private String yongjin;//佣金6.5
    private String jumpThirdPage;// 1表示跳3级页面  0表示直接跳转
    private String detailImgs;// 商品描述大图 ["aaaa","bbb"]
    private String domain;
    private String domainCh;
    private String type;
    private String url;
    private String urlweb;
    private String sale;//销量
    private String bprice;//原价
    private String quan;
    private String desc;
    private String service;
    private String imgurl;
    private String tlj;//该字段存在并且值为1显示鲸币抵扣
    private String tljMsg;//显示鲸币抵扣的内容
    private String tljprice;//鲸币抵扣之后的价格
    private String tljNumber;

    public String getTljNumber() {
        return tljNumber;
    }

    public void setTljNumber(String tljNumber) {
        this.tljNumber = tljNumber;
    }

    public String getTlj() {
        return tlj;
    }

    public void setTlj(String tlj) {
        this.tlj = tlj;
    }

    public String getTljMsg() {
        return tljMsg;
    }

    public void setTljMsg(String tljMsg) {
        this.tljMsg = tljMsg;
    }

    public String getTljprice() {
        return tljprice;
    }

    public void setTljprice(String tljprice) {
        this.tljprice = tljprice;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDetailImgs() {
        return detailImgs;
    }

    public void setDetailImgs(String detailImgs) {
        this.detailImgs = detailImgs;
    }

    public String getDomainCh() {
        return domainCh;
    }

    public void setDomainCh(String domainCh) {
        this.domainCh = domainCh;
    }

    public String getJumpThirdPage() {
        return jumpThirdPage;
    }

    public void setJumpThirdPage(String jumpThirdPage) {
        this.jumpThirdPage = jumpThirdPage;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getUrlweb() {
        return urlweb;
    }

    public void setUrlweb(String urlweb) {
        this.urlweb = urlweb;
    }

    public String getYongjin() {
        return yongjin;
    }

    public void setYongjin(String yongjin) {
        this.yongjin = yongjin;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
