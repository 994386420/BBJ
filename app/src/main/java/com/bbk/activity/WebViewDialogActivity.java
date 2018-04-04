package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.adapter.ResultDialogAdapter1;
import com.bbk.adapter.ResultDialogAdapter2;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.util.NumberUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.renderer.PieChartRenderer;
import lecho.lib.hellocharts.view.AbstractChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class WebViewDialogActivity extends BaseActivity{
	
	private MyListView mlistview1,mlistview2;
	private ResultDialogAdapter1 adapter1;
	private ResultDialogAdapter2 adapter2;
	private List<Map<String, Object>> list1,list2;
	private ImageView mclose;
	private LinearLayout wantdomain;
	private View henggang;
	private String title;
	private String url;
	private String token;
	private DataFlow3 dataFlow;
	private boolean ishistory = false;
//	private LineChartView historyChart;
	/** 历史价格 */
//	private String historyPrice = "", priceStr = "";
	private int maxPriceInt = 0, minPriceInt = 0;
	private List<PointValue> values = new ArrayList<PointValue>();
	private List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
	private List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
	private TextView history;
	private TextView domain_info;
	private ScrollView mscroll;
	private String type;
	private String content;
	private View henggang1;
	private View henggang2;
	private LinearLayout mhistorybox;
	private WebView mwebview;
	private LinearLayout llayout;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view_dialog);
		content = getIntent().getStringExtra("content");
		type = getIntent().getStringExtra("type");
		dataFlow = new DataFlow3(this);
		token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
		initView();
		initData();
	}
	private void initData() {
		try {
			JSONObject object = new JSONObject(content);
			JSONArray list = object.optJSONArray("list");
//			historyPrice = object.optString("history");
			String url = object.optString("hisurl");
			mwebview.loadUrl(url);
			// 支持JS
			WebSettings settings = mwebview.getSettings();
			settings.setJavaScriptEnabled(true);
			// 支持屏幕缩放
			settings.setSupportZoom(true);
			settings.setBuiltInZoomControls(true);
			// 不显示webview缩放按钮
			settings.setDisplayZoomControls(false);
			initlist(list);
			if (list2.isEmpty()) {
				wantdomain.setVisibility(View.GONE);
				henggang.setVisibility(View.GONE);
			}
			adapter1 = new ResultDialogAdapter1(list1, this);
			adapter2 = new ResultDialogAdapter2(list2, this);
			mlistview1.setAdapter(adapter1);
			mlistview2.setAdapter(adapter2);

			mlistview1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Map<String, Object> map = list1.get(arg2);
					final String url = map.get("url").toString();
					final String title = map.get("title").toString();
					final String domain1 = map.get("domain").toString();
							Intent intent = new Intent(WebViewDialogActivity.this,WebViewActivity.class);
							intent.putExtra("url", url);
							WebViewActivity.instance.finish();
							intent.putExtra("title", title);
							intent.putExtra("domain", domain1);
							startActivity(intent);
							finish();

				}
			});
			mlistview2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					Map<String, Object> map = list2.get(arg2);
					final String url = map.get("url").toString();
					Intent intent = new Intent(WebViewDialogActivity.this,WebViewActivity.class);
					WebViewActivity.instance.finish();
					intent.putExtra("url", url);
					startActivity(intent);
					finish();
				}
			});
//			initHistoryPriceData(historyChart);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initlist(JSONArray array) {
		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				if (object.optString("price").isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("domain", object.optString("domain"));
					map.put("domainCh", object.optString("domainCh"));
					map.put("url", object.optString("url"));
					list2.add(map);
				}else{
					Map<String, Object> map = new HashMap<>();
					map.put("title", object.optString("title"));
					map.put("price", object.optString("price"));
					map.put("domain", object.optString("domain"));
					map.put("domainCh", object.optString("domainCh"));
					map.put("url", object.optString("url"));
					map.put("groupRowKey", object.optString("rowkey"));
					list1.add(map);
				}
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		mlistview1 = (MyListView) findViewById(R.id.mlistview1);
		mlistview2 = (MyListView) findViewById(R.id.mlistview2);
		llayout = (LinearLayout) findViewById(R.id.llayout);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		llayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
		wantdomain = (LinearLayout) findViewById(R.id.wantdomain);
		mhistorybox = (LinearLayout) findViewById(R.id.mhistorybox);
		mwebview = (WebView) findViewById(R.id.mwebview);
		mscroll = (ScrollView) findViewById(R.id.mscroll);
//		historyChart = (LineChartView)findViewById(R.id.history_price_chart);

		henggang = findViewById(R.id.henggang);
		henggang1 = findViewById(R.id.henggang1);
		henggang2 = findViewById(R.id.henggang2);
		history = (TextView)findViewById(R.id.history);
		domain_info = (TextView)findViewById(R.id.domain_info);
		
		history.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!ishistory) {
					history.setTextColor(Color.parseColor("#ff7d41"));
					domain_info.setTextColor(Color.parseColor("#333333"));
					henggang1.setBackgroundColor(Color.parseColor("#ffffff"));
					henggang2.setBackgroundColor(Color.parseColor("#ff7d41"));
					mwebview.setVisibility(View.VISIBLE);
					mscroll.setVisibility(View.GONE);
					ishistory = true;
				}
			}
		});
		domain_info.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (ishistory) {
					history.setTextColor(Color.parseColor("#333333"));
					domain_info.setTextColor(Color.parseColor("#ff7d41"));
					henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
					henggang2.setBackgroundColor(Color.parseColor("#ffffff"));
					mwebview.setVisibility(View.GONE);
					mscroll.setVisibility(View.VISIBLE);
					ishistory = false;
				}
			}
		});
		mclose = (ImageView) findViewById(R.id.mclose);
		mclose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		if (type.equals("1")) {
			history.setTextColor(Color.parseColor("#ff7d41"));
			domain_info.setTextColor(Color.parseColor("#333333"));
			henggang1.setBackgroundColor(Color.parseColor("#ffffff"));
			henggang2.setBackgroundColor(Color.parseColor("#ff7d41"));
			mwebview.setVisibility(View.VISIBLE);
			mscroll.setVisibility(View.GONE);
			ishistory = true;
		}else {
			history.setTextColor(Color.parseColor("#333333"));
			domain_info.setTextColor(Color.parseColor("#ff7d41"));
			henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
			henggang2.setBackgroundColor(Color.parseColor("#ffffff"));
			mwebview.setVisibility(View.GONE);
			mscroll.setVisibility(View.VISIBLE);
			ishistory = false;
		}
		String showis = SharedPreferencesUtil.getSharedData(this, "isshowhis", "showhis");
		if (!TextUtils.isEmpty(showis) && "1".equals(showis)) {
			mhistorybox.setVisibility(View.VISIBLE);
		}else{
			mhistorybox.setVisibility(View.GONE);
		}
		
	}
	@Override
	public void finish() {
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		view.removeAllViews();
		super.finish();
	}
	/**
//	 * 加载历史价格数据
//	 */
//	public void initHistoryPriceData(LineChartView historyChart) {
//		
//			if (TextUtils.isEmpty(historyPrice) || "[]".equals(historyPrice)) {
//				return;
//			}
//
//			initPointData();
//			getMaxMinPrice();
//			// initAxisValuesY();
//			initChartData(historyChart);
//	
//		
//	}
//	public void initPointData() {
//		try {
//			JSONArray jsonArray = new JSONArray(historyPrice);
//
//			int length = jsonArray.length();
//			int i = 0, k = 0, j = 0,b = 0;
//			if (length >= 7) {
//				b = length - 7;
//			}
//			for (i=b; i < length; i++) {
//				JSONObject json = new JSONObject();
//				json = jsonArray.optJSONObject(i);
//				String key = "";
//				String value = "";
//				Iterator it = json.keys();
//				while (it.hasNext()) {
//					key = (String) it.next();
//					value = json.optString(key);
//				}
//				if (TextUtils.isEmpty(value)) {
//					continue;
//				}
//				priceStr = value + "," + priceStr;
//
//				String yStr = value;
//				values.add(new PointValue(k++, NumberUtil.parseFloat(yStr, 0)));
//
//				String xStr = key.substring(key.indexOf("-") + 1, key.length());
//				axisValuesX.add(new AxisValue(j++).setLabel(xStr));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//	private void getMaxMinPrice() {
//		try {
//			String[] priceArr = priceStr.split(",");
//			int length = priceArr.length;
//			maxPriceInt = (int) Math.ceil(Double.parseDouble(priceArr[0]));
//			minPriceInt = (int) Math.floor(Double.parseDouble(priceArr[0]));
////			maxPriceInt = Integer.parseInt(priceArr[0].split("\\.")[0]);
////			minPriceInt = Integer.parseInt(priceArr[0].split("\\.")[0]);
//			for (int i = 0; i < length; i++) {
//				int priceInt1 = (int) Math.floor(Double.parseDouble(priceArr[i]));
//				if (priceInt1 < minPriceInt) {
//					minPriceInt = priceInt1;
//				}
//				int priceInt2 = (int) Math.ceil(Double.parseDouble(priceArr[i]));
//				if (priceInt2 > maxPriceInt) {
//					maxPriceInt = priceInt2;
//				}
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//	private void initChartData(LineChartView historyChart) {
//		Line line = new Line(values);
//		line.setColor(getResources().getColor(R.color.main_color));
//		line.setCubic(false);
//		line.setFilled(false);
//		line.setHasLabelsOnlyForSelected(true);
//		line.setHasLabels(true);
//		
//		
//		line.setHasLines(true);
//		line.setHasPoints(true);
//		line.setPointRadius(2);
//		line.setStrokeWidth(1);
//		List<Line> lines = new ArrayList<Line>();
//		lines.add(line);
//
//		LineChartData data = new LineChartData(lines);
//		data.setAxisXBottom(new Axis(axisValuesX).setValues(axisValuesX).setHasLines(true)
//				.setName(" ").setMaxLabelChars(8));
//		data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
//		data.setValueLabelBackgroundEnabled(false);
//		data.setValueLabelsTextColor(Color.parseColor("#ff7d41"));
//
//		historyChart.setLineChartData(data);
//		historyChart.setViewportCalculationEnabled(false);
//
//
//		setViewport(historyChart);
//	}
//	private void setViewport(LineChartView historyChart) {
//		try {
//			JSONArray jsonArray = new JSONArray(historyPrice);
//		final Viewport v = historyChart.getMaximumViewport();
//
//		v.bottom = minPriceInt - minPriceInt/5;
//		v.top = maxPriceInt + maxPriceInt/5;
//		v.left = 0;
//		if (jsonArray.length()<7 && jsonArray.length()>1) {
//			v.right = jsonArray.length()-1;
//		}else{
//			v.right = 6;
//		}
//		// v.set(0, 4, 6, 0);
//		System.out.println(v.width() + " | " + v.height());
//		historyChart.setMaximumViewport(v);
//		historyChart.setCurrentViewport(v);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
