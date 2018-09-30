package com.bbk.flow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bbk.client.BaseApiService;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class DataFlowTaobao {
	private Context context;
	private loadInterface loadInterface;
	public DataFlowTaobao(Context context) {
		this.context = context;
	}

	public DataFlowTaobao.loadInterface getLoadInterface() {
		return loadInterface;
	}

	public void setLoadInterface(DataFlowTaobao.loadInterface loadInterface) {
		this.loadInterface = loadInterface;
	}

	public void requestData(final int requestCode, final String api, Map<String, String> paramsMap, final ResultEvent event, boolean isShowDialog, String tips){
		if(isShowDialog == true){
//			if(TextUtils.isEmpty(tips)){
//				DialogCheckYouhuiUtil.show(context);
//			}else{
				DialogCheckYouhuiUtil.show(context,"小鲸正在努力同步中，请您耐心等等哦！");
//			}
		}
		final Map<String, String> params;
		if(paramsMap == null)
			params = new HashMap<String,String>();
		else
			params = paramsMap;
		final String url = BaseApiService.Base_URL+api;
		new Thread(){
			@Override
			public void run() {
//				String dataStr = HttpUtil.sendPost(url, params.toString());
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
				}else{
				}
				data.event.onResultData(requestCode,api,dataJo,content);
			}catch(Exception e){
//				DialogCheckYouhuiUtil.dismiss(0);
//				StringUtil.showToast(context,"连接超时");
				loadInterface.timeOut();
			}
			DialogCheckYouhuiUtil.dismiss(0);
		}
	};	
	private static class Data{
		public int requestCode;
		public String api;
		public JSONObject dataJo;
		public ResultEvent event;
	}

	public interface loadInterface{
		void timeOut();
		void loadFail();
	}
}