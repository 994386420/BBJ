package com.bbk.model.view;


import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.Bean.ZeroBuyBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface ZeroBuyView extends View {

    void onSuccess(List<ZeroBuyBean> zeroBuyBeans,String bannerimg,String bannerurl);

    void onError(String result);

    void onHide();

    void onFailed();

    void noData();

    void noDataFirst();
}
