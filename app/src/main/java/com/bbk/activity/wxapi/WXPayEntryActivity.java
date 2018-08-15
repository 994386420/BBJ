package com.bbk.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bbk.activity.R;
import com.bbk.resource.Constants;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.util.StringUtil;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_new_home_layout);
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
        finish();
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Intent intent;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                StringUtil.showToast(this,"支付成功");
                intent = new Intent(this, ShopOrderActivity.class);
                intent.putExtra("status", "2");
                startActivity(intent);
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                StringUtil.showToast(this,"取消支付");
                intent = new Intent(this, ShopOrderActivity.class);
                startActivity(intent);
                finish();
                break;
            case BaseResp.ErrCode.ERR_COMM:
                StringUtil.showToast(this,"支付错误");
                finish();
                break;
            default:
                finish();
                break;
        }
    }
}
