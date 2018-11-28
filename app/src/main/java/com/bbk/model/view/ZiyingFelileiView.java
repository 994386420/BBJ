package com.bbk.model.view;


import com.bbk.Bean.ShopDianpuBean;
import com.bbk.Bean.ShopFenLeiBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface ZiyingFelileiView extends View {

    void onSuccess(List<ShopFenLeiBean> shopFenLeiBeans);

    void onError(String result);

    void onHide();

    void onFailed();

    void noData();

    void noDataFirst();
}
