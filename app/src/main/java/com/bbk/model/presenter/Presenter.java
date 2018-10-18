package com.bbk.model.presenter;


import com.bbk.shopcar.view.View;


/**
 * Created by win764-1 on 2016/12/12.
 */

public interface Presenter {
    void attachView(View view);
    void attachTypesView(View view);
    void attachZeroBuyView(View view);
}
