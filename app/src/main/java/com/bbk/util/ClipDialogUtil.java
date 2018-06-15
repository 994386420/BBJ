package com.bbk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.client.BaseApiService;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

public class ClipDialogUtil {
	public static void creatDialog(final Context context) {
		new AlertDialog(context).builder().setTitle("查询历史价格走式").setMsg("是否进入剪贴板里的网址？")
				.setPositiveButton("确定", new View.OnClickListener() {
					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						String object1 = SharedPreferencesUtil.getSharedData(context, "clipchange", "object");
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(object1);
							String title = jsonObject.optString("title");
							String rowkey = jsonObject.optString("rowkey");
							String url = jsonObject.optString("url");
							String domain = jsonObject.optString("domain");
							String price = jsonObject.optString("price");
							String hasCps = jsonObject.optString("hasCps");//  hasCps=1  跳转三级页面 hasCps=0 跳原来的历史价格页面  如果hasCps=1  新增返回  domain   price
							String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
									"QueryHistory", "QueryHistory");
							JSONObject object = new JSONObject();
							object.put("url", url);
							object.put("rowkey", rowkey);
							object.put("title", title);
							JSONArray array;
							if (!LogisticsQuery.isEmpty()) {
								array = new JSONArray(LogisticsQuery);
							} else {
								array = new JSONArray();
							}
							array.put(object);
							if (array.length() > 10) {
								array.remove(0);
							}
							SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "QueryHistory",
									"QueryHistory", array.toString());
							if (hasCps != null){
								Intent intent = new Intent(context, IntentActivity.class);
								if (hasCps.equals("1")){
									if (url != null) {
										intent.putExtra("url", url);
									}
									if (title != null) {
										intent.putExtra("title", title);
									}
									if (domain != null) {
										intent.putExtra("domain", domain);
									}
									if (rowkey != null){
										intent.putExtra("groupRowKey", rowkey);
									}
									if ( price!= null) {
										intent.putExtra("bprice", price);
									}
								}else if (hasCps.equals("0")){
									String url1 = BaseApiService.Base_URL+"mobile/user/history?rowkey=" + rowkey;
									intent = new Intent(context, WebViewActivity.class);
									intent.putExtra("url", url1);
								}
								context.startActivity(intent);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
	}
}
