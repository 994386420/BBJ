package com.bbk.model.view;


import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DianpuSearchView extends View {

    void onSuccess(List<ShopDianpuBean> shopDianpuBeans,String thirdLevels);

    void onError(String result);

    void onHide();

    void onFailed();

    void noData();

    void noDataFirst();
}
