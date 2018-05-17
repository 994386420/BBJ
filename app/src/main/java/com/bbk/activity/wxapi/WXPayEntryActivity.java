package com.bbk.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bbk.activity.R;
import com.bbk.resource.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by Administrator on 2016/6/1.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    public static final String action = "jason.broadcast.action";

    //    public static String ACTION_NAME = "code";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home_layout);
        Log.i("微信回调", "wx==========0========");
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d("微信回调结果", "==============回调结果================" + resp.errCode);
        Intent intent;
        switch (resp.errCode) {
            case 0:
//                    intent = new Intent(action);
//                    intent.putExtra("data", "1");
//                    sendBroadcast(intent);
//                    finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                intent = new Intent(action);
//                intent.putExtra("data", "0");
//                sendBroadcast(intent);
//                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                intent = new Intent(action);
//                intent.putExtra("data", "0");
//                sendBroadcast(intent);
//                finish();
                break;
            default:
//                intent = new Intent(action);
//                intent.putExtra("data", "0");
//                sendBroadcast(intent);
//                finish();
                break;
        }
    }
}
