package cn.kuaishang.kssdk.util;

import android.content.Context;
import android.content.Intent;

import cn.kuaishang.kssdk.activity.KSConversationActivity;

/**
 * Created by Admin on 2017/2/5.
 */

public class KSIntentBuilder {

    private Context mContext;
    private Intent mIntent;

    public KSIntentBuilder (Context context){
        this.mContext = context;
        this.mIntent = new Intent(context, KSConversationActivity.class);
    }

    public Intent build() {
        return mIntent;
    }
}
