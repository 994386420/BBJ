package com.bbk.shopcar.view;


import com.bbk.Bean.DianpuBean;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DianpuView extends View{

    void onSuccess(DianpuBean dianpuBean);

    void onError(String result);
}
