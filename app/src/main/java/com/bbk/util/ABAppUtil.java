package com.bbk.util;

import android.app.Activity;
import android.content.Context;
import android.view.Window;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ABAppUtil {

    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }
}
