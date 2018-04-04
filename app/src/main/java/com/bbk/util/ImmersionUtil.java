package com.bbk.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;

/**
 * Created by rtj on 2018/3/5.
 */

public class ImmersionUtil {
    public static void initstateView(Activity context, View data_head) {
        if (Build.VERSION.SDK_INT >= 19) {
            data_head.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight(context);
        ViewGroup.LayoutParams layoutParams = data_head.getLayoutParams();
        layoutParams.height = result;
        data_head.setLayoutParams(layoutParams);
        ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(context.getWindow(),true);
    }
    private static int getStatusBarHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }
}
