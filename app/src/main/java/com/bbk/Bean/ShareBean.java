package com.bbk.Bean;

/**
 * Created by Administrator on 2018/6/08/008.
 */

public class ShareBean {
    private String imgUrl;
    private String shareUrl;
    private String title;
    private String wenan;//分享文案

    public String getWenan() {
        return wenan;
    }

    public void setWenan(String wenan) {
        this.wenan = wenan;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
