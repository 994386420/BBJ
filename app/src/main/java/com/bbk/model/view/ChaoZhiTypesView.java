package com.bbk.model.view;


import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.shopcar.view.View;

import java.util.List;

/**
 * Created by Administrator on 2018/8/30/030.
 */

public interface ChaoZhiTypesView extends View {

    void onSuccess(String content);

    void onError(String result);

    void onHide();

    void onFailed();
}
