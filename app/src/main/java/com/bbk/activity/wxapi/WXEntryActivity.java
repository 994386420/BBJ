package com.bbk.activity.wxapi;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bbk.activity.R;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.HttpUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler,ResultEvent{
	private Context context = WXEntryActivity.this;
	private static final String TAG = "WXEntryActivity";
	private static IWXAPI api;
	private SendAuth.Req req;

	protected static final int RETURN_OPENID_ACCESSTOKEN = 0;// 返回openid，accessToken消息码
	protected static final int RETURN_NICKNAME_UID = 1; // 返回昵称，uid消息码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Toast.makeText(context,"46546546",Toast.LENGTH_LONG).show();
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
//		api.registerApp(Constants.WX_APP_ID);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
		finish();
	}

	@Override
	public void onReq(BaseReq req) {
		finish();
	}

		@Override
		public void onResp(BaseResp resp) {
			String result = "";
			if(ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()){
				result = "分享";
				if(resp.errCode == BaseResp.ErrCode.ERR_OK) {
//					loadData();
				}
				
			}else if(ConstantsAPI.COMMAND_SENDAUTH == resp.getType()){
				result = "登录";
			}
	
			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result += "成功";
				if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
					StringUtil.showToast(context, result);
					break;
				}
				String code = ((SendAuth.Resp) resp).code;
				
				SharedPreferences settings = getSharedPreferences("setting", 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.remove("code");
		        editor.commit();
		        editor.putString("code",code);
		        editor.commit();

				StringUtil.showToast(context, result);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result += "取消";
				StringUtil.showToast(this, result);
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result += "被拒绝";
				StringUtil.showToast(this, result);
				break;
			default:
				result = "返回";
				StringUtil.showToast(this, result);
				break;
			}
			finish();
		}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RETURN_OPENID_ACCESSTOKEN:
				Bundle bundle1 = (Bundle) msg.obj;
				String accessToken = bundle1.getString("access_token");
				String openId = bundle1.getString("open_id");

				//				getUID(openId, accessToken);
				break;

			case RETURN_NICKNAME_UID:
				Bundle bundle2 = (Bundle) msg.obj;
				String nickname = bundle2.getString("nickname");
				String uid = bundle2.getString("unionid");
				//				textView.setText("uid:" + uid);
				//				loginBtn.setText("昵称：" + nickname);
				break;

			default:
				break;
			}
		};
	};

	private void handleIntent(Intent paramIntent) {
		api.handleIntent(paramIntent, this);
	}
	
	private void loadData() {
				DataFlow dataFlow = new DataFlow(WXEntryActivity.this);
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("userid", SharedPreferencesUtil.getSharedData(getApplicationContext(), "userInfor", "userID"));
				dataFlow.requestData(1, "newService/checkIsShare", paramsMap, WXEntryActivity.this,false);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		// TODO Auto-generated method stub
		
	}


}
