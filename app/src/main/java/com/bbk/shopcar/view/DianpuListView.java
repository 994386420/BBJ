package com.bbk.shopcar.view;


import com.bbk.Bean.DianpuBean;
import com.bbk.Bean.ShopDianpuBean;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DianpuListView extends View{

    void onSuccess(List<ShopDianpuBean> shopDianpuBeans);

    void onError(String result);
}
