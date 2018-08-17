package com.bbk.model.tablayout;

import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

class ViewUtilsLollipop {

    static void setBoundsViewOutlineProvider(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

}
