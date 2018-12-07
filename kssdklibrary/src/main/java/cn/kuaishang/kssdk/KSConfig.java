package cn.kuaishang.kssdk;

import android.content.Context;

import cn.kuaishang.core.KSManager;
import cn.kuaishang.kssdk.controller.ControllerImpl;
import cn.kuaishang.kssdk.controller.KSController;
import cn.kuaishang.listener.KsInitListener;

/**
 * Created by Admin on 2016/12/8.
 */

public class KSConfig {

    private static KSController sController;

    public static KSController getController(Context context) {
        if (sController == null) {
            synchronized (KSConfig.class) {
                if (sController == null) {
                    sController = new ControllerImpl(context.getApplicationContext());
                }
            }
        }
        return sController;
    }

    public static void init(Context context, String appKey, KsInitListener listener){
        KSManager.getInstance(context).initSdk(appKey,listener);
    }

    public static void closeDialog(Context context){
        KSManager.getInstance(context).closeDialog();
    }
}
