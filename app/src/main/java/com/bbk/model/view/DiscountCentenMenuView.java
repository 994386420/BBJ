package com.bbk.model.view;


import com.bbk.Bean.DiscountPersonBean;
import com.bbk.shopcar.view.View;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface DiscountCentenMenuView extends View {

    void onSuccess(JSONArray jsonArray);

    void onError(String result);

    void onHide();

    void onFailed();
}
