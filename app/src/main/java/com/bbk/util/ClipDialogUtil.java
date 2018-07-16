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
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.client.BaseApiService;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class ClipDialogUtil {
	static String title;
	static String url = "";
	static String rowkey = "";
	static String domain = "";
	static String price = "";
	static String hasCps = "";
	public static void creatDialog(final Context context) {
		try {
			final String object1 = SharedPreferencesUtil.getSharedData(context, "clipchange", "object");
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
			if (jsonObject[0].has("hasCps")) {
				hasCps = jsonObject[0].optString("hasCps");//  hasCps=1  跳转三级页面 hasCps=0 跳原来的历史价格页面  如果hasCps=1  新增返回  domain   price
			}
		}catch (Exception e){
			e.printStackTrace();
		}


		if (hasCps != null) {
			if (hasCps.equals("1")) {
			new AlertDialog(context).builder().setTitle("进入商品详情").setMsg("您要进入剪切板中的商品详情？")
					.setPositiveButton("确定", new View.OnClickListener() {
						@SuppressLint("NewApi")
						@Override
						public void onClick(View v) {
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
							context.startActivity(intent);
						}
					}).setNegativeButton("取消", new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			}).show();
		  }else if (hasCps.equals("0")){
				new AlertDialog(context).builder().setTitle("查询历史价格走势").setMsg("您要查询剪切板中的网址?")
						.setPositiveButton("确定", new View.OnClickListener() {
							@SuppressLint("NewApi")
							@Override
							public void onClick(View v) {
								try {
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
								} catch (JSONException e) {
									e.printStackTrace();
								}
								String url1 = BaseApiService.Base_URL + "mobile/user/history?rowkey=" + rowkey;
								Intent intent = new Intent(context, WebViewActivity.class);
								intent.putExtra("url", url1);
								if (domain != null && !domain.equals("")) {
									intent.putExtra("domain", domain);
								}
								context.startActivity(intent);
							}
						}).setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
			}else if (hasCps.equals("2")){
				new AlertDialog(context).builder().setTitle("进入商品列表").setMsg("您要进入剪切板中的商品列表?")
						.setPositiveButton("确定", new View.OnClickListener() {
							@SuppressLint("NewApi")
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context, SearchMainActivity.class);
								if (title != null && !title.equals("")) {
									intent.putExtra("keyword", title);
								}
								context.startActivity(intent);
							}
						}).setNegativeButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				}).show();
			}
		}
	}
}
