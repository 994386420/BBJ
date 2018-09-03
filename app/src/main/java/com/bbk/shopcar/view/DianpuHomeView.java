package com.bbk.shopcar.view;


import com.bbk.Bean.DianPuHomeBean;
import com.bbk.Bean.DianpuBean;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DianpuHomeView extends View{

    void onSuccess(DianPuHomeBean dianPuHomeBean);

    void onError(String result);
}
