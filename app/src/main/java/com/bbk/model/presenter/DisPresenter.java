package com.bbk.model.presenter;


import com.bbk.shopcar.view.View;



public interface DisPresenter {
    void attachView(View view);

    void attachCenterMenu(View view);

    void attachCenterList (View view);
}
