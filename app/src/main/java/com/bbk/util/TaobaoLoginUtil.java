package com.bbk.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;

/**
 * Created by rtj on 2018/1/25.
 */

public class TaobaoLoginUtil {
    public static void  taobaoLogin(final Context context){
        String nick = AlibcLogin.getInstance().getSession().nick;
        if (nick!= null && !"".equals(nick)) {

        }else {
            AlibcLogin alibcLogin = AlibcLogin.getInstance();

            alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

                @Override
                public void onSuccess() {
                    Toast.makeText(context, "登录成功 ",
                            Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int code, String msg) {
                    Toast.makeText(context, "登录失败 ",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
