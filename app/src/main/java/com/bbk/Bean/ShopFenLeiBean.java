package com.bbk.Bean;

import java.util.List;

/**
 * Created by Administrator on 2018/11/22/022.
 */

public class ShopFenLeiBean {
    private String name;
    private String list;
    private List<ListBean> listbean;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public List<ListBean> getListbean() {
        return listbean;
    }

    public static class ListBean{
        private String name;
        private String img;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
