package com.bbk.model.presenter;


import com.bbk.shopcar.view.View;



public interface Presenter {
    void attachView(View view);
    void attachTypesView(View view);
    void attachZeroBuyView(View view);
    void attachDianpuSearchView(View view);
    void attachZiyingFelileiView(View view);
}
