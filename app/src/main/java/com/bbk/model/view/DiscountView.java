package com.bbk.model.view;


import com.bbk.Bean.DiscountBean;
import com.bbk.Bean.DiscountPersonBean;
import com.bbk.Bean.MiaoShaBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.PinTuanBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DiscountView extends View {

    void onSuccess(List<DiscountPersonBean> discountPersonBeans,String yishiyong,String weishiyong,String yiguoqi);

    void onError(String result);

    void onHide();

    void onFailed();

    void noData();

    void noDataFirst();
}
