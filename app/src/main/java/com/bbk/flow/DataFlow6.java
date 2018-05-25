package com.bbk.flow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bbk.fragment.NewHomeFragment;
import com.bbk.resource.Constants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class DataFlow6 {
	private Context context;
	public DataFlow6(Context context) {
		this.context = context;
	}
	public void requestData(final int requestCode,final String api,Map<String, String> paramsMap,final ResultEvent event,boolean isShowDialog,String tips){
		if(isShowDialog == true){
			if(TextUtils.isEmpty(tips)){
				DialogSingleUtil.show(context);
			}else{
				DialogSingleUtil.show(context,tips);
			}
		}
		final Map<String, String> params;
		if(paramsMap == null)
			params = new HashMap<String,String>();
		else
			params = paramsMap;
		final String url = Constants.MAIN_BASE_URL_MOBILE+api;
		new Thread(){
			@Override
			public void run() {
				String dataStr = HttpUtil.getHttp(params,url,context);
				Message mess = new Message();
				Data data = new Data();
				try {
					JSONObject dataJo = new JSONObject(dataStr);
					data.dataJo = dataJo;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				data.requestCode = requestCode;
				data.event = event;
				data.api = api;
				mess.obj = data;
				handler.sendMessage(mess);
			};
		}.start();
	}
	public void requestData(final int requestCode,String api,final Map<String, String> paramsMap,final ResultEvent event,String tips){
		this.requestData(requestCode,api, paramsMap, event, true,tips);
	}
	public void requestData(final int requestCode,String api,final Map<String, String> paramsMap,final ResultEvent event){
		this.requestData(requestCode,api, paramsMap, event, true,null);
	}
	public void requestData(final int requestCode,String api,final Map<String, String> paramsMap,final ResultEvent event,boolean isShowDialog){
		this.requestData(requestCode,api, paramsMap, event, isShowDialog,null);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(context ==null || context.isRestricted()){
				return;
			}
			Data data = (Data) msg.obj;
			int requestCode = data.requestCode;
			String api = data.api;
			try{
				JSONObject dataJo = data.dataJo;
				String content = dataJo.optString("content");
				if(dataJo.optInt("status")<=0){
					StringUtil.showToast(context, dataJo.optString("errmsg"));
//					Toast.makeText(context, dataJo.optString("errmsg"), Toast.LENGTH_SHORT).show();
				}else{
//					Log.i("网络请求返回数据：",dataJo+"------------------------");
					data.event.onResultData(requestCode,api,dataJo,content);
				}
			}catch(Exception e){
			//	Toast.makeText(context, "服务器忙不过来了，请稍后再试！", Toast.LENGTH_SHORT).show();
			}
			DialogSingleUtil.dismiss(0);
		}
	};	
	private static class Data{
		public int requestCode;
		public String api;
		public JSONObject dataJo;
		public ResultEvent event;
	}


}