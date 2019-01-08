package com.bbk.model.view;


import com.bbk.Bean.DiscountMenuCenterBean;
import com.bbk.Bean.DiscountPersonBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DiscountCentenListView extends View {

    void onSuccess(List<DiscountMenuCenterBean> discountPersonBeans);

    void onError(String result);

    void onHide();

    void onFailed();
}
