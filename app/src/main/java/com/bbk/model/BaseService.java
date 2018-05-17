package com.bbk.model;

import android.app.Activity;

/**
 * Created by Administrator on 2016/9/21.
 */
public class BaseService {
    private static PayModel payModel;
    public static PayModel getPayModel(Activity context) {
        if (payModel == null) {
            payModel = new PayModel(context);
        }
        return payModel;
    }
}
