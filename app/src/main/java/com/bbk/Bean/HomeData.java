package com.bbk.Bean;

import org.json.JSONArray;

/**
 * Created by rtj on 2017/12/1.
 */

public class HomeData {
    private JSONArray banner ;
    private JSONArray activity ;
    private JSONArray articles ;
    private JSONArray dianpu ;
    private JSONArray tag ;
    private JSONArray tujian ;
    private JSONArray gongneng ;

    public JSONArray getGongneng() {
        return gongneng;
    }

    public void setGongneng(JSONArray gongneng) {
        this.gongneng = gongneng;
    }

    public JSONArray getBanner() {
        return banner;
    }

    public void setBanner(JSONArray banner) {
        this.banner = banner;
    }

    public JSONArray getActivity() {
        return activity;
    }

    public void setActivity(JSONArray activity) {
        this.activity = activity;
    }

    public JSONArray getArticles() {
        return articles;
    }

    public void setArticles(JSONArray articles) {
        this.articles = articles;
    }

    public JSONArray getDianpu() {
        return dianpu;
    }

    public void setDianpu(JSONArray dianpu) {
        this.dianpu = dianpu;
    }

    public JSONArray getTag() {
        return tag;
    }

    public void setTag(JSONArray tag) {
        this.tag = tag;
    }

    public JSONArray getTujian() {
        return tujian;
    }

    public void setTujian(JSONArray tujian) {
        this.tujian = tujian;
    }
}
