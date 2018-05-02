package com.bbk.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.MyApplication;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import static com.tencent.qalsdk.service.QalService.tag;

/**
 * Created by rtj on 2018/3/21.
 */

public class TencentLoginUtil {

    public static void Login(final Context context){
        String identifier = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "identifier");
        String userSig = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userSig");
        TIMManager.getInstance().login(identifier, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.d(tag, "login failed. code: " + code + " errmsg: " + desc);
            }

            @Override
            public void onSuccess() {
                Log.d(tag, "login succ");
            }
        });
    }
    public static void Loginout(final Context context) {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
            StringUtil.showToast(context,"退出成功");
            }
        });
    }
}
