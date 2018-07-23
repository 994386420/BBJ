package com.bbk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.ShareBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.client.BaseApiService;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.logg.Logg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClipDialogUtil {
	static String title;
	static String url = "";
	static String rowkey = "";
	static String domain = "";
	static String price = "";
	static String hasCps = "";
	private static UpdataDialog updataDialog;
	private static Context context;
	private static Handler mHandler = new Handler();
	private static AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
	private static Map<String, String> exParams;//yhhpass参数
	private String urltaobaoOrjd ;

//https://item.taobao.com/item.htm?id=552692093578
	public ClipDialogUtil(Context context){
		this.context = context;
	}
	public static void creatDialog(final Context context) {
		try {
//			Logg.e("运行到这里了");
			NewConstants.showdialogFlg = "0";
			final String object1 = SharedPreferencesUtil.getSharedData(context, "clipchange", "object");
			Logg.json(object1);
			final JSONObject[] jsonObject = new JSONObject[1];
			jsonObject[0] = new JSONObject(object1);
//							Log.i("==========",jsonObject[0]+"====");
			if (jsonObject[0].has("title")) {
				title = jsonObject[0].optString("title");
			}
			if (jsonObject[0].has("rowkey")) {
				rowkey = jsonObject[0].optString("rowkey");
			}
			if (jsonObject[0].has("url")) {
				url = jsonObject[0].optString("url");
			}
			if (jsonObject[0].has("domain")) {
				domain = jsonObject[0].optString("domain");
			}
			if (jsonObject[0].has("price")) {
				price = jsonObject[0].optString("price");
			}
		DialogCheckYouhuiUtil.show(context);
		if (jsonObject[0].has("hasCps")) {
			hasCps = jsonObject[0].optString("hasCps");
			if (hasCps != null) {
				if (hasCps.equals("1")) {
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent(context, IntentActivity.class);
							if (url != null && !url.equals("")) {
								intent.putExtra("url", url);
							}
							if (title != null && !title.equals("")) {
								intent.putExtra("title", title);
							}
							if (domain != null && !domain.equals("")) {
								intent.putExtra("domain", domain);
							}
							if (rowkey != null && !rowkey.equals("")) {
								intent.putExtra("groupRowKey", rowkey);
							}
							if (price != null && !price.equals("")) {
								intent.putExtra("bprice", price);
							}
							DialogCheckYouhuiUtil.dismiss(1500);
							context.startActivity(intent);
						}
					}, 1500);
				}
			}
		}else{
			DialogCheckYouhuiUtil.dismiss(2000);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					showMessageDialog(context,url);;//耗时操作
				}
			}, 2000);
		}
		}catch (Exception e){
			e.printStackTrace();
		}
//		  }else if (hasCps.equals("0")){
//				new AlertDialog(context).builder().setTitle("查询历史价格走势").setMsg("您要查询剪切板中的网址?")
//						.setPositiveButton("确定", new View.OnClickListener() {
//							@SuppressLint("NewApi")
//							@Override
//							public void onClick(View v) {
//								try {
//								String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
//										"QueryHistory", "QueryHistory");
//								JSONObject object = new JSONObject();
//								object.put("url", url);
//								object.put("rowkey", rowkey);
//								object.put("title", title);
//								JSONArray array;
//								if (!LogisticsQuery.isEmpty()) {
//									array = new JSONArray(LogisticsQuery);
//								} else {
//									array = new JSONArray();
//								}
//								array.put(object);
//								if (array.length() > 10) {
//									array.remove(0);
//								}
//								SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "QueryHistory",
//										"QueryHistory", array.toString());
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//								String url1 = BaseApiService.Base_URL + "mobile/user/history?rowkey=" + rowkey;
//								Intent intent = new Intent(context, WebViewActivity.class);
//								intent.putExtra("url", url1);
//								if (domain != null && !domain.equals("")) {
//									intent.putExtra("domain", domain);
//								}
//								context.startActivity(intent);
//							}
//						}).setNegativeButton("取消", new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//					}
//				}).show();
//			}else if (hasCps.equals("2")){
//				new AlertDialog(context).builder().setTitle("进入商品列表").setMsg("您要进入剪切板中的商品列表?")
//						.setPositiveButton("确定", new View.OnClickListener() {
//							@SuppressLint("NewApi")
//							@Override
//							public void onClick(View v) {
//								Intent intent = new Intent(context, SearchMainActivity.class);
//								if (title != null && !title.equals("")) {
//									intent.putExtra("keyword", title);
//								}
//								context.startActivity(intent);
//							}
//						}).setNegativeButton("取消", new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//					}
//				}).show();
//			}
//		}
	}

	/**
	 *
	 * @param context
	 */
	public static void showMessageDialog(final Context context, final String url) {
		if(updataDialog == null || !updataDialog.isShowing()) {
			//初始化弹窗 布局 点击事件的id
			updataDialog = new UpdataDialog(context, R.layout.check_nomessage_dialog_layout,
					new int[]{R.id.tv_update_gengxin});
			updataDialog.show();
			updataDialog.setCanceledOnTouchOutside(true);
			TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
			tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
					jumpThirdApp(url);
				}
			});
			LinearLayout ll_close = updataDialog.findViewById(R.id.ll_close);
			ll_close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					updataDialog.dismiss();
				}
			});
		}
	}

	public static void jumpThirdApp(String url){
//			DialogSingleUtil.show(context);
			alibcShowParams = new AlibcShowParams(OpenType.Native, false);
			alibcShowParams.setClientType("taobao_scheme");
			exParams = new HashMap<>();
			exParams.put("isv_code", "appisvcode");
			exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
//			if (domain != null) {
				if (url.contains("tmall") || url.contains("taobao")) {
					showUrl(url);
				} else if (url.contains("jd")) {
					KeplerApiManager.getWebViewService().openAppWebViewPage(context,
							url,
							mKeplerAttachParameter,
							mOpenAppAction);
//					DialogSingleUtil.dismiss(100);
				} else {
					Intent intent = new Intent(context, WebViewActivity.class);
					if (url != null) {
						intent.putExtra("url", url);
					}
//					if (rowkey != null) {
//						intent.putExtra("rowkey", rowkey);
//					}
					context.startActivity(intent);
//					DialogSingleUtil.dismiss(50);
				}
//			}
	}

	/**
	 * 打开指定链接
	 */
	public static void showUrl(String url) {
		String text = url;
		if (TextUtils.isEmpty(text)) {
			StringUtil.showToast(context, "URL为空");
			return;
		}
		AlibcTrade.show((Activity) context, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
//		DialogSingleUtil.dismiss(100);
	}

	private static KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

	static OpenAppAction mOpenAppAction = new OpenAppAction() {
		@Override
		public void onStatus(final int status, final String url) {
			Intent intent;
			if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//				DialogSingleUtil.show(context);
			} else {
//				DialogSingleUtil.dismiss(0);
			}
			if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
				StringUtil.showToast(context, "未安装京东");
				intent = new Intent(context, WebViewActivity.class);
				if (url != null) {
					intent.putExtra("url", url);
				}
//				if (rowkey != null) {
//					intent.putExtra("rowkey", rowkey);
//				}
				context.startActivity(intent);
				//未安装京东
			} else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
				StringUtil.showToast(context, "不在白名单");
				//不在白名单
			} else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
				StringUtil.showToast(context, "协议错误");
				//协议错误
			} else if (status == OpenAppAction.OpenAppAction_result_APP) {
				//呼京东成功
			} else if (status == OpenAppAction.OpenAppAction_result_NetError) {
				StringUtil.showToast(context, "网络异常");
				//网络异常
			}
		}
	};
}
