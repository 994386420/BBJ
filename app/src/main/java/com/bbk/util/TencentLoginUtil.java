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
    //			 identifier为用户名，userSig 为用户登录凭证
//    String identifier = "bbj486";
//    String userSig = "eJxNTttugjAYfhdut2xtaRWXeMXBkTg3GMq8agq00hmxKZUNzN59QDDxv-u-89VK1p9PdXGkT" +
//            "ClZWC8WxACAmYMRsh5Hkv8qqTllwnA98IQQ1EsmdrRRZqitB-cNNvLERzFcYGJjACdcFrwyUsgxKsu*sTO7BeX5*VIZalrF74Jqeei-N3-rhpHXzl" +
//            "O*mp8SUaeLrXTKgMXde5ce17ufknzsVp4WQdYgX1VReHi9JCHxZJQZd6P3zj5*tmG7CVo--nIbyEQedUWs84ekBMuprOG6lueqL0Sgn45sMJz19w-m-lfk";
    //    String identifier = "bbj488";
//			String userSig = "eJxNTt1ugjAUfpfesmyltEqX7MJszhAnkSkz8aaB0mLdLE2pDmb27gOCyc7d" +
//					"*f6vYPu2ua*LT5YZowrwCHwMIZyEGCFwN5CiMcoKlkknbM8TQlAnGdnBxjLHAtu7b7BTJzGIfYoJpTQY" +
//					"cVUI7ZRUQ1SeH3EY3oI4r87aMdca8S*oVmX3rebpc5S8WO486dYk1*2CpzwgmwpXp0NV0sX*y-PszjbIvO4*sPyOyvXD" +
//					"YR9vl3Hr*00yO8*PqZum9OeipI6mq7pIJu*hjKlDy9nTWHYRtlaV7goR7KajAPYHfv8A*4dX3Q__";


    public static void Login(final Context context){
        String identifier = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "identifier");
        String userSig = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userSig");
        TIMManager.getInstance().login(identifier, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.d(tag, "login failed. code: " + code + " errmsg: " + desc);
//                Toast.makeText(context,"登录失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Log.d(tag, "login succ");
//                Toast.makeText(context,"登录成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void Login1(final Context context){
        String identifier = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "identifier");
        String userSig = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userSig");
        TIMManager.getInstance().login(identifier, userSig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                Log.d(tag, "login failed. code: " + code + " errmsg: " + desc);
//                Toast.makeText(context,"登录失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess() {
                Log.d(tag, "login succ");

//                Toast.makeText(context,"登录成功！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
