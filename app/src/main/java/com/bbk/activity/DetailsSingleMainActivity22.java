package com.bbk.activity;


import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.baidu.mobstat.StatService;
import com.bbk.adapter.DetailListViewAdapter;
import com.bbk.adapter.ResultListAdapter;
import com.bbk.adapter.SecondAdapter5;
import com.bbk.dao.BrowseHistoryDao;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.AddStarUtil;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.LoadImgUtil;
import com.bbk.util.NumberUtil;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyFootView;
import com.bbk.view.MyGridView;
import com.bbk.view.MyListView;
import com.bbk.view.RollHeaderView2;
import com.bbk.view.SuperScrollView;
import com.bbk.view.SuperScrollView.OnScrollListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.tauth.Tencent;

public class DetailsSingleMainActivity22 extends BaseFragmentActivity
		implements OnClickListener, ResultEvent,OnScrollListener {

	private DataFlow dataFlow;
	private DataFlow3 dataFlow1;
	private String groupRowkey = "";
	private String userID = "";
	private boolean isCollection = false;

	private ImageView loadingIv;
	private ImageButton goBackBtn;
	private ImageButton collectionBtn;
	private SuperScrollView scroll;
	private ImageView detailImg,domainimg,imgtitle;
	private TextView detailTitleTv,mnumber,snumber,from_shop,text_history,text_detail;
	private Integer curPage;
	private int srcollY = 0;
	private int srcollY1 = 0;
	/** 历史价格 */
	private LinearLayout historyPriceLayout;
	private String historyPrice = "", priceStr = "";
	private int maxPriceInt = 0, minPriceInt = 0;
	private List<PointValue> values = new ArrayList<PointValue>();
	private List<AxisValue> axisValuesX = new ArrayList<AxisValue>();
	private List<AxisValue> axisValuesY = new ArrayList<AxisValue>();

	private String sortWay = "1";

	/** 报价商城列表 */
	private LinearLayout rank_box,banner_layout;

	private TextView littleprice,bigprice;

	private CheckBox sczyCb;
	private CheckBox qjdCb;
    
	private String filterWay = "0";
	private RelativeLayout detail_layout,detail,intentbuy;
	
	// 记录对应domain的报价数据
	private HashMap<String, List<String>> domainDataMap = new HashMap<String, List<String>>();
	// 记录对应domain的报价的总页数
	private HashMap<String, Integer> domainNumMap = new HashMap<String, Integer>();
	// 记录对应domain的布局
	private HashMap<String, Object> domainLayoutMap = new HashMap<String, Object>();
	private HashMap<String, Object> LayoutMap = new HashMap<String, Object>();
	// 记录对应domain展开的分页页数
	private HashMap<String, Integer> domainPageMap = new HashMap<String, Integer>();

	private JSONObject resultJsonObj;

	private BrowseHistoryDao dao;

	private Tencent mTencent;
	
	private int num = 0;
	private int height2;
	private String token;
	private int itemHeight=0;
	private View domainview;
	private List<String> list12 = new ArrayList<>();
	private Animation mShowAction;
	private Animation mHiddenAction;
	private View data_head;
	private RollHeaderView2 bannerView;
	private LineChartView historyChart;
	private MyListView mlistView;
	private View listhenggang;
	private View listhenggang1;
	private String title;
	private String url;
	private String domain1;
	
	private ImageView filter_img;
	private ImageView mtop;
	private LinearLayout shopbox;
	private LinearLayout mfilterbox,mfilterbox1;
	private LinearLayout second_hei,mbig;
	private RelativeLayout second_bai;
	private LinearLayout second;
	private RelativeLayout biankuang1;
	private RelativeLayout hinttext,hinttext1;
	private RelativeLayout biankuang2;
	private LinearLayout view_box;
	private TextView sellrank;
	private TextView compositerank;
	private TextView filter_price1;
	private TextView compositerank1;
	private TextView tv1;
	private MyListView mlistview;
	private TextView tv2;
	private TextView request;
	private TextView ensure;
	private TextView filter;
	private RelativeLayout mComposite1;
	private RelativeLayout mnumber1;
	private RelativeLayout mprice1;
	private RelativeLayout mfilter1;
	private JSONObject jsonObject;
	private List<Map<String, Object>> itemList;
	private String domain="";
	private List<Map<String, Object>> data;
	private int curposition=-1;
	private String brand;
	private int page=1;
	private boolean istv1=false;
	private boolean istv2=false;
	private String sortway1="0";
	private boolean isprice = true;
	private XRefreshView xrefresh;
	private ResultListAdapter listadapter;
	private JSONObject abcBrand;
	private String[] brandArr;
	private View view;
	private MyGridView gridView;
	private TextView select1;
	private SecondAdapter5 adapter;
	
	private TextView filter2;
	private RelativeLayout mComposite2;
	private RelativeLayout mnumber2;
	private RelativeLayout mprice2;
	private RelativeLayout mfilter2;
	private TextView compositerank2;
	private TextView filter_price2;
	private TextView sellrank2;
	private ImageView mtop2;
	private ImageView filter_img2;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_single_main22);
		data_head = findViewById(R.id.data_head);
		ImmersedStatusbarUtils.initAfterSetContentView(this, data_head);
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initstateView();
		dataFlow = new DataFlow(this);
		dataFlow1 = new DataFlow3(this);
		mTencent = Tencent.createInstance(Constants.QQ_APP_ID, DetailsSingleMainActivity22.this);
		groupRowkey = getIntent().getStringExtra("groupRowKey");
		Log.e("======groupRowKey=====", groupRowkey+"");
		userID = SharedPreferencesUtil.getSharedData(this, "userInfor", "userID");
		if (groupRowkey!=null) {
			dao = new BrowseHistoryDao(this);
			dao.addBrowseHistory(groupRowkey);
		}
		
		initView();
		// initData();
	}
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    } 
	private void initstateView() {
		if (Build.VERSION.SDK_INT >=19) {
			data_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = data_head.getLayoutParams();
		layoutParams.height = result;
		data_head.setLayoutParams(layoutParams);
	}
	/**
	 * 信鸽推送，点击通知跳转到该页面，在onResume中获取参数，并加载页面
	 */
	@Override
	protected void onResume() {
		super.onResume();
		XGPushClickedResult click = XGPushManager.onActivityStarted(this);
		if (click != null) { // 判断是否来自信鸽的打开方式
			String customContent = click.getCustomContent();
			if (customContent != null && customContent.length() != 0) {
				try {
					JSONObject obj = new JSONObject(customContent);
					if (!obj.isNull("groupRowKey")) {
						String value = obj.getString("groupRowKey");
						groupRowkey = value;
						Log.e("======groupRowKey=====", groupRowkey+"");
						dao = new BrowseHistoryDao(this);
						dao.addBrowseHistory(groupRowkey);
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (!is4Result) {
			domainDataMap.clear();
			domainLayoutMap.clear();
			domainPageMap.clear();

			initData();
		}
		is4Result = false;
	}

	@Override
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}

	public void initView() {
		data = new ArrayList<>();
		itemList = new ArrayList<>();
		goBackBtn = $(R.id.topbar_goback_btn);
		intentbuy = $(R.id.intentbuy);
		listhenggang = $(R.id.listhenggang);
		listhenggang1 = $(R.id.listhenggang1);
		domainimg = $(R.id.domainimg);
		imgtitle = $(R.id.imgtitle);
		from_shop = $(R.id.from_shop);
		snumber = $(R.id.snumber);
		mnumber = $(R.id.mnumber);
		littleprice = $(R.id.littleprice);
		bigprice = $(R.id.bigprice);
		goBackBtn = $(R.id.topbar_goback_btn);
		loadingIv = $(R.id.loading_iv);
		collectionBtn = $(R.id.collection_btn);
		scroll = $(R.id.main_scrollview);
		detailImg = $(R.id.detail_img);
		banner_layout = $(R.id.banner_layout);
		detailTitleTv = $(R.id.detail_title);
		detail_layout = $(R.id.detail_layout);
		detail = $(R.id.detail);
		text_detail = $(R.id.text_detail);
		text_history = $(R.id.text_history);
		filter_img = (ImageView) findViewById(R.id.filter_img1);
		mtop = (ImageView) findViewById(R.id.mtop1);
		shopbox = (LinearLayout) findViewById(R.id.shopbox);
		mfilterbox = (LinearLayout) findViewById(R.id.mfilterbox);
		second_hei = (LinearLayout) findViewById(R.id.second_hei);
		second_bai = (RelativeLayout) findViewById(R.id.second_bai);
		second = (LinearLayout) findViewById(R.id.second);
		biankuang1 = (RelativeLayout) findViewById(R.id.biankuang1);
		hinttext = (RelativeLayout) findViewById(R.id.hinttext);
		biankuang2 = (RelativeLayout) findViewById(R.id.biankuang2);
		view_box = (LinearLayout) findViewById(R.id.view_box);
		sellrank = (TextView) findViewById(R.id.sellrank1);
		compositerank = (TextView) findViewById(R.id.compositerank1);
		filter_price1 = (TextView) findViewById(R.id.filter_price1);
		compositerank1 = (TextView) findViewById(R.id.compositerank1);
		tv1 = (TextView) findViewById(R.id.tv1);
		mlistview = (MyListView)findViewById(R.id.mlistview);
		tv2 = (TextView) findViewById(R.id.tv2);
		request = (TextView) findViewById(R.id.request);
		request.setOnClickListener(this);
		ensure = (TextView) findViewById(R.id.ensure);
		ensure.setOnClickListener(this);
		filter = (TextView) findViewById(R.id.filter1);
		mComposite1 = (RelativeLayout) findViewById(R.id.mComposite1);
		mnumber1 = (RelativeLayout) findViewById(R.id.mnumber1);
		mprice1 = (RelativeLayout) findViewById(R.id.mprice1);
		mfilter1 = (RelativeLayout) findViewById(R.id.mfilter1);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
		
		mfilterbox1 = $(R.id.mfilterbox1);
		mbig = $(R.id.mbig);
		hinttext1 = $(R.id.hinttext1);
		filter2 = (TextView) findViewById(R.id.filter2);
		mComposite2 = (RelativeLayout) findViewById(R.id.mComposite2);
		mnumber2 = (RelativeLayout) findViewById(R.id.mnumber2);
		mprice2 = (RelativeLayout) findViewById(R.id.mprice2);
		mfilter2 = (RelativeLayout) findViewById(R.id.mfilter2);
		compositerank2 = (TextView) findViewById(R.id.compositerank2);
		filter_price2 = (TextView) findViewById(R.id.filter_price2);
		sellrank2 = (TextView) findViewById(R.id.sellrank2);
		mtop2 = (ImageView) findViewById(R.id.mtop2);
		filter_img2 = (ImageView) findViewById(R.id.filter_img2);
		
		xrefresh.setPullRefreshEnable(false);
		xrefresh.setXRefreshViewListener(new XRefreshViewListener() {
			
			@Override
			public void onRelease(float direction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRefresh(boolean isPullDown) {

			}
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadMore(boolean isSilence) {
				page++;
				loadData();
			}
			
			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
				// TODO Auto-generated method stub
				
			}
		});
		xrefresh.stopLoadMore();
		MyFootView footView = new MyFootView(this);
		xrefresh.setCustomFooterView(footView);
		TextPaint tp = text_detail .getPaint();  
		TextPaint tp1 = text_history .getPaint();  
		tp.setFakeBoldText(true);  
		tp1.setFakeBoldText(true);  
		
		historyChart = (LineChartView)findViewById(R.id.history_price_chart);
		mlistView = (MyListView)findViewById(R.id.mlistView);
		bannerView = new RollHeaderView2(this,BaseTools.getPixelsFromDp(this, 2),true);
		banner_layout.addView(bannerView);

		intentbuy.setOnClickListener(this);
		goBackBtn.setOnClickListener(this);
		collectionBtn.setOnClickListener(this);
		
		mComposite1.setOnClickListener(this);
		mnumber1.setOnClickListener(this);
		mprice1.setOnClickListener(this);
		mfilter1.setOnClickListener(this);
		second_hei.setOnClickListener(this);
		second_bai.setOnClickListener(this);
		biankuang1.setOnClickListener(this);
		biankuang2.setOnClickListener(this);
		
		mComposite2.setOnClickListener(this);
		mnumber2.setOnClickListener(this);
		mprice2.setOnClickListener(this);
		mfilter2.setOnClickListener(this);
		scroll.setOnScrollListener(this);




		
		$(R.id.share_btn).setOnClickListener(this);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rowkey", groupRowkey);
		dataFlow.requestData(7, "apiService/getDetailInfoByRowkey", params, this);
		
	}


	
	public void initData() {
		Glide.with(this).load(R.mipmap.page_loading).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate()
				.into(loadingIv);

		sortWay = "1";
		getHttpData(groupRowkey, "", 0, sortWay, 1);
		queryCollection(3);
		if (view_box!=null) {
			view_box.removeAllViews();
		}
		if (shopbox!=null) {
			shopbox.removeAllViews();
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("rowkey", groupRowkey);
		dataFlow.requestData(8, "newApp/querySimilar", paramsMap, this);
		
	}
	public void initData1() {
		Glide.with(this).load(R.mipmap.page_loading).diskCacheStrategy(DiskCacheStrategy.NONE).dontAnimate()
		.into(loadingIv);
		
		sortWay = "1";
		getHttpData(groupRowkey, "", 0, sortWay, 14);
		queryCollection(15);
		if (view_box!=null) {
			view_box.removeAllViews();
		}
		if (shopbox!=null) {
			shopbox.removeAllViews();
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("rowkey", groupRowkey);
		dataFlow.requestData(13, "newApp/querySimilar", paramsMap, this);
		
	}
	private void loadData(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		initstype();
		paramsMap.put("rowkey", groupRowkey);
		paramsMap.put("page", page+"");
		paramsMap.put("sortWay", sortway1);
		paramsMap.put("filterWay", filterWay);
		paramsMap.put("domain", domain);
		paramsMap.put("brand", brand);
		dataFlow1.requestData(9, "newApp/querySimilarList", paramsMap, this);
	}
	public void initData12(){
			Map<String, String> paramsMap = new HashMap<String, String>();
			initstype();
			paramsMap.put("rowkey", groupRowkey);
			paramsMap.put("page", page+"");
			paramsMap.put("sortWay", sortway1);
			paramsMap.put("filterWay", filterWay);
			paramsMap.put("domain", domain);
			paramsMap.put("brand", brand);
			dataFlow.requestData(10, "newApp/querySimilarList", paramsMap, this);
	}
	
	private void initstype(){
		if (!istv1&&!istv2) {
			filterWay ="0";
		}else if(istv1&&!istv2){
			filterWay ="1";
		}else if(!istv1&&istv2){
			filterWay ="2";
		}else{
			filterWay ="3";
		}
	}	
	public void getHttpData(String key, String domain, int page, String sortWay, int what) {
		HashMap<String, String> params = new HashMap<String, String>();
		token = SharedPreferencesUtil.getSharedData(DetailsSingleMainActivity22.this, "userInfor", "token");
		params.put("groupRowkey", key);
		params.put("domain", domain);
		params.put("filterWay", filterWay);
		if (!"".equals(domain) && domain != null) {
			params.put("page", "" + page);
			if (page != 1)
				what = 6;
		}
		params.put("sortWay", sortWay);
		if (null!= userID) {
			params.put("userId", userID);
		}else{
			params.put("token", token);
		}
		

		dataFlow.requestData(what, "apiService/getDetailList", params, this);
	}
	//添加banner
		private void loadBannerItem(final JSONObject ja){
			List<Object> imgUrlList = new ArrayList<>();
			String str = ja.optString("bigImg");
			if (str.contains("zhanwei")) {
				LoadImgUtil.loadImg(DetailsSingleMainActivity22.this, imgtitle, str);
				LoadImgUtil.loadImg(DetailsSingleMainActivity22.this, detailImg, str);
			}else{
				if (str.contains("bibijing")) {
					String[] split = str.split("bibijing");
					for (int i = 0; i < split.length-1; i++) {
						imgUrlList.add(split[i]);
					}
					bannerView.setImgUrlData(imgUrlList);
					LoadImgUtil.loadImg(DetailsSingleMainActivity22.this, imgtitle, split[0]);
					detailImg.setVisibility(View.GONE);
					banner_layout.setVisibility(View.VISIBLE);
					bannerView.setOnHeaderViewClickListener(new RollHeaderView2.HeaderViewClickListener() {
						@Override
						public void HeaderViewClick(int position) {
							return;
						}
					});
				}else{
					imgUrlList.add(str);
					LoadImgUtil.loadImg(DetailsSingleMainActivity22.this, imgtitle, str);
					LoadImgUtil.loadImg(DetailsSingleMainActivity22.this, detailImg, str);
					banner_layout.setVisibility(View.GONE);
					detailImg.setVisibility(View.VISIBLE);
				}
			}			
		}

	public void queryCollection(int what) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("commonType", "3");
		params.put("collectType", "collect_query");
		if (null!= userID) {
			params.put("userName", userID);
		}
		params.put("groupRowkey", groupRowkey);

		dataFlow.requestData(what, "apiService/getUserInfo", params, this, false);
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		Log.e("========", requestCode+"");
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(content) || "{}".equals(content) || "[]".equals(content)) {
			return;
		}
		switch (requestCode) {
		case 1:
			try {
				xrefresh.stopLoadMore();
				resultJsonObj = new JSONObject(content);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			initDetailsData(content);
			

			
			((RelativeLayout) loadingIv.getParent()).setVisibility(View.GONE);
			Glide.clear(loadingIv);
			break;
		case 14:
			try {
				xrefresh.stopLoadMore();
				resultJsonObj = new JSONObject(content);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			initDetailsData(content);
			
			
			((RelativeLayout) loadingIv.getParent()).setVisibility(View.GONE);
			Glide.clear(loadingIv);
			scroll.fullScroll(ScrollView.FOCUS_UP);
			break;
		case 2:
			String domain = "";
			try {
				JSONArray jsonArr = new JSONArray(content);
				domain = jsonArr.optJSONObject(0).optString("domain");
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<String> list = new ArrayList<>();
			list.add(content);
			domainDataMap.put(domain, list);
			
			break;
		case 3:
			if ("0".equals(content)) {
				collectionBtn.setImageResource(R.mipmap.shoucang_02);
				isCollection = true;
			}
			break;
		case 15:
			if ("0".equals(content)) {
				collectionBtn.setImageResource(R.mipmap.shoucang_02);
				isCollection = true;
			}
			break;
		case 4:
			if ("success".equals(content)) {
				collectionBtn.setImageResource(R.mipmap.shoucang_01);
				isCollection = false;
				Toast.makeText(DetailsSingleMainActivity22.this, "亲，您已取消收藏！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(DetailsSingleMainActivity22.this, "取消收藏失败，请稍后重试", Toast.LENGTH_SHORT).show();
			}
			collectionBtn.setEnabled(true);
			break;
		case 5:
			if ("success".equals(content)) {
				collectionBtn.setImageResource(R.mipmap.shoucang_02);
				isCollection = true;
				Toast.makeText(DetailsSingleMainActivity22.this, "亲，您已成功收藏！", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(DetailsSingleMainActivity22.this, "收藏失败，请稍后重试", Toast.LENGTH_SHORT).show();
			}
			collectionBtn.setEnabled(true);
			break;
		case 7:
			JSONObject jsonObj;
			try {
				historyPrice = "";
				priceStr ="";
				jsonObj = new JSONObject(content);
				historyPrice = jsonObj.optString("history");
				initHistoryPriceData(historyChart);
				initListview(jsonObj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 8:
			try {
			xrefresh.stopLoadMore();
			itemList.clear();
			xrefresh.setVisibility(View.VISIBLE);
			jsonObject = new JSONObject(content);
			JSONArray page = jsonObject.getJSONArray("page");
			initListView(page);
			hinttext.setVisibility(View.VISIBLE);
			mfilterbox.setVisibility(View.VISIBLE);
			loadfiltershop(jsonObject);
			loadFilterBrand(jsonObject);
			$(R.id.xrefresh).setVisibility(View.VISIBLE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 13:
			try {
				xrefresh.stopLoadMore();
				itemList.clear();
				xrefresh.setVisibility(View.VISIBLE);
				jsonObject = new JSONObject(content);
				JSONArray page = jsonObject.getJSONArray("page");
				initListView(page);
				hinttext.setVisibility(View.VISIBLE);
				loadfiltershop(jsonObject);
				loadFilterBrand(jsonObject);
				scroll.fullScroll(ScrollView.FOCUS_UP);
				$(R.id.xrefresh).setVisibility(View.VISIBLE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 9:
			xrefresh.stopRefresh();
			xrefresh.stopLoadMore();
			JSONArray page1;
			try {
				page1 = new JSONArray(content);
			
			for (int i = 0; i < page1.length(); i++) {
				JSONObject object = page1.getJSONObject(i);
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("img", object.optString("imgUrl"));
				itemMap.put("title",object.optString("title"));
				String price = object.optString("price");
				itemMap.put("price", price);
				itemMap.put("hnumber", object.optString("comnum"));
				itemMap.put("domainCount", object.optString("domainCount"));
				itemMap.put("groupRowKey", object.optString("groupRowkey"));
				itemMap.put("quote", object.optString("numberCount"));
				itemMap.put("allDomain", object.optString("alldomain"));
				if (!object.optString("url").isEmpty()) {
					itemMap.put("url", object.optString("url"));
					itemMap.put("title", object.optString("title"));
					itemMap.put("domain1", object.optString("domain"));
				}else{
					itemMap.put("url", "1");
					itemMap.put("title", "1");
					itemMap.put("domain1", "1");
				}
				itemMap.put("hassimi", "2");
				itemList.add(itemMap);
			}
			if (page1.length()<12) {
				xrefresh.setLoadComplete(true);
			}
			listadapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 10:
			xrefresh.stopLoadMore();
			itemList.clear();
			JSONArray page2;
			try {
				page2 = new JSONArray(content);
			
			if (page2.length()==0) {
				listadapter.notifyDataSetChanged();
				mlistview.setVisibility(View.GONE);
				xrefresh.setLoadComplete(true);
			}else{
				mlistview.setVisibility(View.VISIBLE);
				for (int i = 0; i < page2.length(); i++) {
					JSONObject object = page2.getJSONObject(i);
					HashMap<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("img", object.optString("imgUrl"));
					itemMap.put("title",object.optString("title"));
					String price = object.optString("price");
					itemMap.put("price", price);
					itemMap.put("hnumber", object.optString("comnum"));
					itemMap.put("domainCount", object.optString("domainCount"));
					itemMap.put("groupRowKey", object.optString("groupRowkey"));
					itemMap.put("quote", object.optString("numberCount"));
					itemMap.put("allDomain", object.optString("alldomain"));
					if (!object.optString("url").isEmpty()) {
						itemMap.put("url", object.optString("url"));
						itemMap.put("title", object.optString("title"));
						itemMap.put("domain1", object.optString("domain"));
					}else{
						itemMap.put("url", "1");
						itemMap.put("title", "1");
						itemMap.put("domain1", "1");
					}
					itemMap.put("hassimi", "2");
					itemList.add(itemMap);
				}
				if (page2.length()<12) {
					xrefresh.setLoadComplete(true);
				}
				listadapter.notifyDataSetChanged();
				
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}
	private void initListview(JSONObject jsonObj) {
		if (!jsonObj.optString("spec").isEmpty()) {
			String spec =jsonObj.optString("spec");
			if (!spec.isEmpty()) {
				detail.setVisibility(View.VISIBLE);
				listhenggang.setVisibility(View.VISIBLE);
				listhenggang1.setVisibility(View.VISIBLE);
				String[] split = spec.split("\\|");
				List<Map<String, String>> lists = new ArrayList<>();
				for (int i = 0; i < split.length; i++) {
					Map<String, String> map = new HashMap<>();
					String str = split[i];
					int end = str.indexOf(":");
					String text1 = str.substring(0, end);
					String text2 = str.substring(end+1,str.length());
					map.put("text1", text1);
					map.put("text2", text2);
					lists.add(map);
				}
				DetailListViewAdapter adapter = new DetailListViewAdapter(this, lists);
				mlistView.setAdapter(adapter);
			}else{
				detail.setVisibility(View.GONE);
				listhenggang.setVisibility(View.GONE);
				listhenggang1.setVisibility(View.GONE);
			}
		}
		
		
		
	}
	/**
	 * 加载历史价格数据
	 */
	public void initHistoryPriceData(LineChartView historyChart) {
		
			if (TextUtils.isEmpty(historyPrice) || "[]".equals(historyPrice)) {
				historyPriceLayout.setVisibility(View.GONE);
				return;
			}

			initPointData();
			getMaxMinPrice();
			// initAxisValuesY();
			initChartData(historyChart);
	
		
	}
	public void initPointData() {
		try {
			JSONArray jsonArray = new JSONArray(historyPrice);

			int length = jsonArray.length();
			int i = 0, k = 0, j = 0;
			if (length >= 7) {
				i = length - 7;
			}
			for (i=0; i < length; i++) {
				JSONObject json = new JSONObject();
				json = jsonArray.optJSONObject(i);
				String key = "";
				String value = "";
				Iterator it = json.keys();
				while (it.hasNext()) {
					key = (String) it.next();
					value = json.optString(key);
				}
				if (TextUtils.isEmpty(value)) {
					continue;
				}
				priceStr = value + "," + priceStr;

				String yStr = value;
				values.add(new PointValue(k++, NumberUtil.parseFloat(yStr, 0)));

				String xStr = key.substring(key.indexOf("-") + 1, key.length());
				axisValuesX.add(new AxisValue(j++).setLabel(xStr));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void getMaxMinPrice() {
		String[] priceArr = priceStr.split(",");
		int length = priceArr.length;
		maxPriceInt = Integer.parseInt(priceArr[0].split("\\.")[0]);
		minPriceInt = Integer.parseInt(priceArr[0].split("\\.")[0]);
		for (int i = 0; i < length; i++) {
			int priceInt = Integer.parseInt(priceArr[i].split("\\.")[0]);
			if (priceInt < minPriceInt) {
				minPriceInt = priceInt;
			}
			if (priceInt > maxPriceInt) {
				maxPriceInt = priceInt;
			}
		}
	}
	private void initChartData(LineChartView historyChart) {
		Line line = new Line(values);
		line.setColor(getResources().getColor(R.color.main_color));
		line.setCubic(true);
		line.setFilled(false);
		line.setHasLabels(false);
		line.setHasLabelsOnlyForSelected(true);
		line.setHasLines(true);
		line.setHasPoints(true);
		line.setPointRadius(2);
		line.setStrokeWidth(1);

		List<Line> lines = new ArrayList<Line>();
		lines.add(line);

		LineChartData data = new LineChartData(lines);

		data.setAxisXBottom(new Axis(axisValuesX).setHasLines(true)
				.setName(" "));
		data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));

		historyChart.setLineChartData(data);
		historyChart.setViewportCalculationEnabled(false);

		setViewport(historyChart);
	}
	private void setViewport(LineChartView historyChart) {
		final Viewport v = historyChart.getMaximumViewport();
		v.bottom = minPriceInt - 100;
		v.top = maxPriceInt + 100;
		v.left = 0;
		v.right = 6;
		// v.set(0, 4, 6, 0);
		System.out.println(v.width() + " | " + v.height());
		historyChart.setMaximumViewport(v);
		historyChart.setCurrentViewport(v);
	}


	public void collection(int cancelWhat, int addWhat) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", "1");
		params.put("userid", userID);
		params.put("rowkey", groupRowkey);

		String url = "";
		int what;
		if (isCollection) {
			url = "cancelCollect";
			StatService.onEvent(DetailsSingleMainActivity22.this, "collection", "取消收藏:三级页面");
			what = cancelWhat;
		} else {
			url = "addCollect";

			params.put("currPrice", resultJsonObj.optString("price"));
			params.put("title", resultJsonObj.optString("title"));
			params.put("url", resultJsonObj.optString("url"));
			params.put("img", resultJsonObj.optString("bigImg"));
			params.put("token", SharedPreferencesUtil.getSharedData(this, "deviceToken", "token"));

			JSONObject countJson = resultJsonObj.optJSONObject("domainsCount");
			Iterator it = countJson.keys();
			int countOffer = 0;
			while (it.hasNext()) {
				String key = it.next().toString();
				countOffer += NumberUtil.parseInt(countJson.optString(key), 0);
			}

			params.put("quote", countOffer + "");
			params.put("domainCount", resultJsonObj.optString("domainCount"));
			params.put("review", "");

			StatService.onEvent(DetailsSingleMainActivity22.this, "collection", "收藏:三级页面");
			what = addWhat;
		}

		dataFlow.requestData(what, "apiService/" + url, params, this);
	}

	public void initDetailsData(String str) {
		if (TextUtils.isEmpty(str) || "{}".equals(str)) {
			return;
		}
		try {
			JSONObject jsonObj = new JSONObject(str);
			title = jsonObj.optString("title");
			if (!jsonObj.optString("url").isEmpty()) {
				url = jsonObj.optString("url");
			}
			detailTitleTv.setText(title);
			loadBannerItem(jsonObj);
			int countOffer = 0;
			int countDomain = 0;
			JSONObject countJson = jsonObj.optJSONObject("domainsCount");
			Iterator it = countJson.keys();
			while (it.hasNext()) {
				String key = it.next().toString();
				int value = NumberUtil.parseInt(countJson.optString(key), 0);
				int num = value - 1;
				int pageNumber = num % 10 == 0 ? num / 10 : num / 10 + 1;
				domainNumMap.put(key, pageNumber);
				countOffer += value;
				countDomain++;
			}
			Log.i("liye","countDomain==="+countDomain+"   countOffer==="+countOffer);
			String price = jsonObj.optString("price");
			String bigprice1;
	        String littleprice1;
	        if (price.contains(".")) {
	        	int end = price.indexOf(".");
	    		bigprice1 = price.substring(0, end);
	    		littleprice1 = price.substring(end, price.length());
			}else{
				bigprice1 = price;
				littleprice1 = ".0";
			}
			bigprice.setText(bigprice1);
			littleprice.setText(littleprice1);
			String comnum = jsonObj.optString("comnum");
			String sale = jsonObj.optString("sale");
			mnumber.setText("全网总评"+comnum);
			snumber.setText("全网销量"+sale);
			String domain = jsonObj.optString("service");
			String hassimi = jsonObj.optString("hassimi");
			from_shop.setText(domain);
			domain1 = jsonObj.optString("domain");
			int drawS = getResources().getIdentifier(domain1,"domain", getPackageName());
 	        domainimg.setBackgroundResource(drawS);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public void isExpanded() {

	}

	
	


	public View inflate(int id) {
		return getLayoutInflater().inflate(id, null, false);
	}



	public void goBack() {
//		Intent intent = new Intent(DetailsMainActivity22.this,HomeActivity.class);
//		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			goBack();
			break;
		case R.id.share_btn:
			ShareUtil.showShareDialog(v, this);
			break;
		case R.id.collection_btn:
			String login_STATE = SharedPreferencesUtil.getSharedData(DetailsSingleMainActivity22.this,
					"userInfor", "login_STATE");
			if (login_STATE.isEmpty()) {
				Intent userLoginIntent = new Intent(getApplicationContext(), UserLoginNewActivity.class);
				startActivityForResult(userLoginIntent, 1);
			} else {
				collectionBtn.setEnabled(false);
				collection(4, 5);
	
			}
			break;
		case R.id.intentbuy:
			Intent intent = new Intent(DetailsSingleMainActivity22.this,
					IntentActivity.class);
			intent.putExtra("url", url);
			intent.putExtra("title", title);
			intent.putExtra("domain", domain1);
			startActivity(intent);
			break;
		case R.id.second_bai:
			break;
		case R.id.second_hei:
			second.setVisibility(View.GONE);
			break;
		case R.id.biankuang1:
			if (istv1) {
				istv1 = false;
				tv1.setTextColor(Color.parseColor("#222222"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview);
			}else{
				istv1 = true;
				tv1.setTextColor(Color.parseColor("#0098ff"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview2);
			}
			break;
		case R.id.biankuang2:
			if (istv2) {
				istv2 = false;
				tv2.setTextColor(Color.parseColor("#222222"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview);
			}else{
				istv2 = true;
				tv2.setTextColor(Color.parseColor("#0098ff"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview2);
			}
			break;
		case R.id.ensure:
				if ("".equals(brand) &&"".equals(domain)&& !istv1 && !istv2) {
					filter_img.setImageResource(R.mipmap.shaixuan_01);
					filter.setTextColor(Color.parseColor("#222222"));
				}else{
					filter_img.setImageResource(R.mipmap.shaixuan_02);
					filter.setTextColor(Color.parseColor("#0098ff"));
				}
				second.setVisibility(View.GONE);
				initData12();
				
			break;
		case R.id.request:
				istv1 = false;
				tv1.setTextColor(Color.parseColor("#222222"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview);
				istv2 = false;
				tv2.setTextColor(Color.parseColor("#222222"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview);
				filter_img.setImageResource(R.mipmap.shaixuan_01);
				filter.setTextColor(Color.parseColor("#222222"));
				data.clear();
				filterWay = "0";
				domain = "";
				brand ="";
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				loadFilterBrand(jsonObject);
				loadfiltershop(jsonObject);
			break;
		case R.id.mComposite1:
			rankcomposite();
			break;
		case R.id.mnumber1:
			ranknumber();
			break;
		case R.id.mprice1:
			rankprice();
			break;
		case R.id.mfilter1:
			srcollY1 = scroll.getScrollY();
			second.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	private void rankcomposite(){
		mtop.setImageResource(R.mipmap.gaodi_01);
		compositerank.setTextColor(Color.parseColor("#0098ff"));
		sellrank.setTextColor(Color.parseColor("#222222"));
		compositerank1.setTextColor(Color.parseColor("#0098ff"));
		filter_price1.setTextColor(Color.parseColor("#222222"));
		page=1;
		sortway1 = "0";
		initData12();
	}
	private void ranknumber(){
		mtop.setImageResource(R.mipmap.gaodi_01);
		sellrank.setTextColor(Color.parseColor("#0098ff"));
		compositerank.setTextColor(Color.parseColor("#222222"));
		compositerank1.setTextColor(Color.parseColor("#222222"));
		filter_price1.setTextColor(Color.parseColor("#222222"));
		page=1;
		sortway1 = "1";
		initData12();
	}
	private void rankprice(){
		sellrank.setTextColor(Color.parseColor("#222222"));
		compositerank.setTextColor(Color.parseColor("#222222"));
		filter_price1.setTextColor(Color.parseColor("#0098ff"));
		compositerank1.setTextColor(Color.parseColor("#222222"));
		page=1;
		if (isprice) {
			sortway1 = "2";
			mtop.setImageResource(R.mipmap.gaodi_02);
			isprice = false;
		}else{
			sortway1 = "3";
			mtop.setImageResource(R.mipmap.gaodi_03);
			isprice = true;
		}
		initData12();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			goBack();
		}
		return super.onKeyDown(keyCode, event);
	}

	boolean is4Result = false;
	private int mbigheight;
	private void heightAnimation(final View target, final int start, final int end, int duration) {
	    ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
	    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	        @Override
	        public void onAnimationUpdate(ValueAnimator animation) {
	            IntEvaluator intEvaluator = new IntEvaluator();
	            //获取当前帧值
	            int currentValue = (int) animation.getAnimatedValue();

	            //Elapsed/interpolated fraction of the animation.
	            float fraction = animation.getAnimatedFraction();

	            target.getLayoutParams().height = intEvaluator.evaluate(fraction, start, end);
	            target.requestLayout();
	        }
	    });
	    valueAnimator.setDuration(duration).start();
	}
	@Override
	protected void onPause() {
		super.onPause();
		XGPushManager.onActivityStoped(this);
	}
	private void initListView(final JSONArray page) throws JSONException {
		for (int i = 0; i < page.length(); i++) {
			JSONObject object = page.getJSONObject(i);
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("img", object.optString("imgUrl"));
			itemMap.put("title",object.optString("title"));
			String price = object.optString("price");
			itemMap.put("price", price);
			itemMap.put("hnumber", object.optString("comnum"));
			itemMap.put("domainCount", object.optString("domainCount"));
			itemMap.put("groupRowKey", object.optString("groupRowkey"));
			itemMap.put("quote", object.optString("numberCount"));
			itemMap.put("allDomain", object.optString("alldomain"));
			if (!object.optString("url").isEmpty()) {
				itemMap.put("url", object.optString("url"));
				itemMap.put("title", object.optString("title"));
				itemMap.put("domain1", object.optString("domain"));
			}else{
				itemMap.put("url", "1");
				itemMap.put("title", "1");
				itemMap.put("domain1", "1");
			}
			itemMap.put("hassimi", "2");
			itemList.add(itemMap);
		}
		if (page.length()<12) {
			xrefresh.setLoadComplete(true);
		}else{
			xrefresh.setAutoLoadMore(true);
		}
		listadapter = new ResultListAdapter(itemList, this);
		mlistview.setAdapter(listadapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				try {
				if (itemList.get(position).get("quote").equals("1")) {
					groupRowkey = page.getJSONObject(position).optString("groupRowkey");
					initData1();
					hinttext1.setVisibility(View.GONE);
		        	mfilterbox1.setVisibility(View.GONE);
		        	$(R.id.xrefresh).setVisibility(View.GONE);
				}else{
					Intent intent = new Intent(DetailsSingleMainActivity22.this, DetailsMainActivity22.class);
					
						intent.putExtra("groupRowKey",page.getJSONObject(position).optString("groupRowkey"));
					
					startActivity(intent);
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	
	private void loadFilterBrand(JSONObject json) {
		try {
//			Iterator<String> keys = json.keys();
//			while (keys.hasNext()) {
//				String key = keys.next();
//				Log.e("========key=======", key+"");
//			}
			final String jsonObject = json.optString("brand");
			abcBrand = json.optJSONObject("abcBrand");
			brandArr = jsonObject.split("\\|");
			final List<Map<String, Object>> data1 = new ArrayList<>();
			for (int i = 0; i < brandArr.length; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("item_text", brandArr[i]);
				map.put("item_selected", "no");
				data1.add(map);
			}
			if (brandArr.length>3) {
				data.add(data1.get(0));
				data.add(data1.get(1));
				data.add(data1.get(2));
			}else{
				data.addAll(data1);
			}
			LayoutInflater inflater = LayoutInflater.from(this);
			view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
			gridView = (MyGridView) view.findViewById(R.id.mgridView);
			select1 = (TextView) view.findViewById(R.id.select);
			adapter = new SecondAdapter5(DetailsSingleMainActivity22.this, data);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (position==8) {
						Intent intent = new Intent(DetailsSingleMainActivity22.this, ReslutBrandActivity.class);
						intent.putExtra("jsonObject", jsonObject);
						intent.putExtra("abcBrand", abcBrand.toString());
						startActivityForResult(intent, 1);
					}else{
						Map<String, Object> item = data.get(position);
						
						if ("yes".equals(item.get("item_selected").toString())) {
							
							item.put("item_selected", "no");
							adapter.notifyDataSetChanged();
							select1.setText("");
							curposition=-1;
						}else{
							if (select1!=null&&select1.getText().toString()!="") {
								Map<String, Object> item1 = data.get(curposition);
								item1.put("item_selected", "no");
								item.put("item_selected", "yes");
								adapter.notifyDataSetChanged();
								select1.setText(item.get("item_text").toString());
								curposition=position;
							}else{
								item.put("item_selected", "yes");
								adapter.notifyDataSetChanged();
								select1.setText(item.get("item_text").toString());
								curposition=position;
							}
						}
						brand = select1.getText().toString();
					}
				}
			});
			TextView title = (TextView) view.findViewById(R.id.mtext);
			title.setText("品牌");
			final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
			RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
			if (brandArr.length<4) {
				imgclick.setVisibility(View.GONE);
			}else{
				imgclick.setVisibility(View.VISIBLE);
			}
			
			imgclick.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (data.size()>3) {
						foldTopAnimation(mimg);
						data.clear();
						data.add(data1.get(0));
						data.add(data1.get(1));
						data.add(data1.get(2));
						adapter.notifyDataSetChanged();
					}else{
						unfoldTopAnimation(mimg);
						if (brandArr.length>9) {
							data.clear();
							for (int i = 0; i < 8; i++) {
								Map<String, Object> map = new HashMap<>();
								map.put("item_text", brandArr[i]);
								map.put("item_selected", "no");
								data.add(map);
							}
							Map<String, Object> map = new HashMap<>();
							map.put("item_text", "全部品牌");
							map.put("item_selected", "no");
							data.add(map);
							adapter.notifyDataSetChanged();
						}else{
							data.clear();
							Map<String, Object> map = new HashMap<>();
							for (int i = 0; i < brandArr.length; i++) {
								map.put("item_text", brandArr[i]);
								map.put("item_selected", "no");
								data.add(map);
							}
							adapter.notifyDataSetChanged();
						}
					}
					
				}
			});
			view_box.addView(view);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}
	
	
	
	
	
	private void loadfiltershop(JSONObject jsonObject){
		String key = jsonObject.optString("domainChall");
		final List<Map<String, Object>> data = new ArrayList<>(); 
		final List<Map<String, Object>> data1 = new ArrayList<>();
		String[] vs = key.split("\\|");
		for (int i = 0; i < vs.length; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("item_text", vs[i]);
			map.put("item_selected", "no");
			data.add(map);
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
		final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
		RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
		TextView title = (TextView) view.findViewById(R.id.mtext);
		title.setText("商城");
		final TextView select = (TextView) view.findViewById(R.id.select);
		MyGridView mgridView = (MyGridView) view.findViewById(R.id.mgridView);
		if (data.size()>3) {
			data1.add(data.get(0));
			data1.add(data.get(1));
			data1.add(data.get(2));
			imgclick.setVisibility(View.VISIBLE);
		}else{
			data1.addAll(data);
			imgclick.setVisibility(View.GONE);
		}
		final SecondAdapter5 adapter = new SecondAdapter5(this, data1);
		mgridView.setAdapter(adapter);
		
		imgclick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (data1.size()<4) {
					data1.clear();
					unfoldTopAnimation(mimg);
					data1.addAll(data);
					adapter.notifyDataSetChanged();
				}else{
					data1.clear();
					foldTopAnimation(mimg);
					data1.add(data.get(0));
					data1.add(data.get(1));
					data1.add(data.get(2));
					adapter.notifyDataSetChanged();
				}
				
				
			}
		});
		mgridView.setOnItemClickListener(new OnItemClickListener() {

			private Toast toast;

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long arg3) {
				Map<String, Object> item = data.get(position);
				
				if ("yes".equals(item.get("item_selected").toString())) {
					
					item.put("item_selected", "no");
					adapter.notifyDataSetChanged();
					String str = null;
					for (int i = 0; i < data.size(); i++) {
						Map<String, Object> list = data.get(i);
						if ("yes".equals(list.get("item_selected").toString())) {
							String str1 = list.get("item_text").toString();
							if (str!=null) {
								str = str+","+str1;
							}else{
								str = ","+str1;
							}
							
						}
					}
					if (str!=null) {
						select.setText(str.subSequence(1, str.length()));
						domain = (String) str.subSequence(1, str.length());
					}else{
						select.setText(null);
						domain = "";
					}
					
					
				}else{
					int num=0;
					for (int i = 0; i < data.size(); i++) {
						Map<String, Object> list = data.get(i);
						if ("yes".equals(list.get("item_selected").toString())) {
							num++;
						}
					}
					if (num<5) {
						select.setVisibility(View.VISIBLE);
						item.put("item_selected", "yes");
						adapter.notifyDataSetChanged();
						if (select.getText().toString().isEmpty()) {
							domain = (String) item.get("item_text").toString();
							select.setText(item.get("item_text").toString());
						}else{
							domain = (String)select.getText()+","+item.get("item_text").toString();
							select.setText(select.getText()+","+item.get("item_text").toString());
						}
					}else{
						if (toast!=null) {
							toast.cancel();
						}
						toast = Toast.makeText(DetailsSingleMainActivity22.this, "最多只能选5个", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		shopbox.addView(view);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data5) {
		super.onActivityResult(requestCode, resultCode, data5);
		switch (requestCode) {
		case 1:
			if (data5.getStringExtra("choseBrandName")!=null) {
				String choseBrandName = data5.getStringExtra("choseBrandName");
				if (curposition!=-1) {
					Map<String, Object> item1 = data.get(curposition);
					item1.put("item_selected", "no");
					curposition=-1;
				}
				adapter.notifyDataSetChanged();
			for (int i = 0; i < data.size(); i++) {
				if (choseBrandName.equals(data.get(i).get("item_text").toString())) {
					Map<String, Object> item = data.get(i);
					item.put("item_selected", "yes");
					curposition=i;
					adapter.notifyDataSetChanged();
					select1.setText(item.get("item_text").toString());
				}
			}
			brand = choseBrandName;
			}
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 展开弹窗效果
	 */
	private void unfoldTopAnimation(View v) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 0f, 180f);
		animator.setDuration(500);
		animator.start();
	}

	/**
	 * 收起弹窗效果
	 */
	private void foldTopAnimation(View v) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 180f, 0f);
		animator.setDuration(500);
		animator.start();
	}
	/** 
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置 
     */  
    @Override    
    public void onWindowFocusChanged(boolean hasFocus) {    
        super.onWindowFocusChanged(hasFocus);    
        if(hasFocus){  
        	mbigheight = mbig.getHeight();
        	Log.e("===mbigheight====", mbigheight+"");
        }  
    }
	@Override
	public void onScroll(int scrollY) {
		Log.e("===scrollY====", scrollY+"");
		if(scrollY > (mbigheight)){  
			mbigheight = mbig.getHeight();
			if (scrollY > (mbigheight)) {
				hinttext1.setVisibility(View.VISIBLE);
				mfilterbox1.setVisibility(View.VISIBLE);
			}
        }else if(scrollY <= mbigheight){  
        	hinttext1.setVisibility(View.GONE);
        	mfilterbox1.setVisibility(View.GONE);
        } 
	}
}
