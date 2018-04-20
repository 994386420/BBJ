package com.bbk.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.adapter.ResultExpandableListViewAdapter;
import com.bbk.evaluator.BGAlphaEvaluator;
import com.bbk.adapter.ResultMyGridAdapter;
import com.bbk.adapter.ResultMyListAdapter;
import com.bbk.adapter.SecondAdapter4;
import com.bbk.adapter.SecondAdapter5;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.SearchFragment;
import com.bbk.resource.Constants;
import com.bbk.util.AppJsonFileReader;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.FooterView;
import com.bbk.view.MyFootView;
import com.bbk.view.MyGridView;
import com.bbk.view.XCFlowLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * 搜索返回结果界面
 */
public class ResultMainActivity extends BaseActivity
		implements ResultEvent ,OnClickListener{

	private boolean isprice = true;
	private DataFlow dataFlow;
	public static Activity instance;
	private TextView topbar_search_input,filter_price,compositerank,sellrank;
	private ImageButton topbar_goback_btn,topbar_search_btn,to_top_btn,back_third;
	private String keyword;
	private String addition="";
	private String brand="";
	private String bPrice="";
	private String ePrice="";
	private String sortway="0";
	private boolean isfilter = false;
	// 分类
	private JSONArray secondTypeJsonArr = new JSONArray();
	private JSONArray thirdTypeJsonArr = new JSONArray();
	private List<Map<String, Object>> parentlist;
	private List<List<Map<String, Object>>> childlist;
	
	private int currentPageIndex = 1;
	private boolean islistview = true;
	private Map<String, List<Map<String, Object>>> filterMap;;
	private RelativeLayout mComposite,mnumber,mprice,mfilter,henggang,biankuang1,biankuang2;
	private XRefreshView xrefresh;
	private ListView result_list;
	private GridView mgridView_main;
	private List<Map<String, Object>> itemList,itemList1;
	private LinearLayout correctLayout,view_box,third_hei,third_bai,second_hei,second,third,correctLayout1,shopbox;
	private TextView correctTv,ensure,request,correctTv1;
	private RelativeLayout tipsLayout,second_bai,style_box;
	private TextView tipsKeys,tuijianText;
	private int isshow = 1;
	private SecondAdapter4 secondadapter;
	private FooterView mFooterView;
	private boolean canLoadMore;
	private boolean isLoad;
	private ResultMyListAdapter listAdapter;
	private ResultMyGridAdapter gridviewadapter;
//	private XScrollView scrollView_home;
	private String[] brandArr;
	private JSONObject abcBrand;
	private int curposition=-1;
	private List<Map<String, Object>> data;
	private SecondAdapter5 adapter;
	private MyGridView gridView;
	private View view;
	private TextView select1,sort_text,filter,tv1,tv2;
	private ExpandableListView mexpandableListView;
	private String typeStr;
	private ResultExpandableListViewAdapter exadapter;
	private JSONObject info;
	private JSONObject oldinfo;
	private boolean isfirstinfo = true;
	private ImageView mtop,filter_img,mzhezhao;
	private EditText begin_price_et,end_price_et;
	private Toast toast;
	private XCFlowLayout mflowlayout = null;
	 private List<View> tvs = null;
	private View data_head;  
	private boolean istv1=false;
	private boolean istv2=false;
	private String stype = "0";
	private String domains = "";
	private DataFlow3 dataFlow1;
	/**
	 * 排行榜切换对象
	 */
	private RelativeLayout rankRelativeLayout;
	private LinearLayout rankSwichLayout;
	private FrameLayout rankSwichShow;
	private LinearLayout rankSwitchDiv;
	private int rankSwitchDivHeight;
	private boolean isSwichRankShow;
	private String name1;
	private LinearLayout rankMoreLayout,scanle;
	private TextView text1,text2,text3,text4;
	private ImageView img1,img2,img3,img4;
	private String addtion1;
	private ArrayList<String> listname = new ArrayList<>();
	private ArrayList<String> listaddtion = new ArrayList<>();
	private int position8;
	private boolean isrequest = false;
	private int requestnum = 0;
	private int removenum = 0;
	private Thread thread;
	private int pagenum = 0;
	private Thread threadtmall;
	private XRefreshView xrefresh1;
	private LinearLayout mshaixuanbox;
	private boolean isrun = true;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.result_main2);
		data_head = findViewById(R.id.data_head);
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		ImmersedStatusbarUtils.initAfterSetContentView(this, data_head);
		initstateView();
		dataFlow = new DataFlow(this);
		dataFlow1 = new DataFlow3(this);
		initView();
		initData();
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
	private void initData() {
		isrequest = false;
		initRankView();
		Map<String, String> paramsMap = params();
		dataFlow.requestData(1, "apiService/getPageList", paramsMap, this);
	}
	private void initData1() {
		Map<String, String> paramsMap = params();
		dataFlow1.requestData(1, "apiService/getPageList", paramsMap, this);
	}
	private Map<String, String> params(){
		exadapter = new ResultExpandableListViewAdapter(ResultMainActivity.this,parentlist,childlist);
		mexpandableListView.setAdapter(exadapter);
		mexpandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Map<String, Object> map = childlist.get(groupPosition).get(childPosition);
				sort_text.setText(map.get("item_text").toString());
				addition = map.get("item_addtion").toString();
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				third.setVisibility(View.GONE);
				loadfilter();
				return true;
			}
		});
		Map<String, String> paramsMap = new HashMap<String, String>();
//		paramsMap.put("stype", String.valueOf(SearchFragment.stypeWay));
		paramsMap.put("keyword", keyword);
		paramsMap.put("productType", addition);
		paramsMap.put("brand", brand);
		paramsMap.put("bprice", bPrice);
		paramsMap.put("eprice", ePrice);
		String filter = getFilterString();
		paramsMap.put("filter", filter);
		paramsMap.put("sortWay", sortway);
		paramsMap.put("domains", domains);
		initstype();
		paramsMap.put("stype", stype);
		paramsMap.put("client", "Android");
		paramsMap.put("page", currentPageIndex + "");
		return paramsMap;
	}
	public void loadfilter(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("keyword", keyword);
		paramsMap.put("addtion", addition);
		dataFlow1.requestData(4, "apiService/requestFilter", paramsMap, this);
	}
	private void initView() {
		keyword = getIntent().getStringExtra("keyword");
		addition = getIntent().getStringExtra("addition");
		brand = getIntent().getStringExtra("brand");
		itemList = new ArrayList<>();
		itemList1 = new ArrayList<>();
		data = new ArrayList<>();
		parentlist = new ArrayList<>();
		childlist = new ArrayList<>();
		tvs = new LinkedList<View>();
		
		mflowlayout = (XCFlowLayout) findViewById(R.id.mflowlayout);
		third_hei = (LinearLayout) findViewById(R.id.third_hei);
		mshaixuanbox = (LinearLayout) findViewById(R.id.mshaixuanbox);
		tuijianText = (TextView) findViewById(R.id.tuijianText);
		sort_text = (TextView) findViewById(R.id.sort_text);
		end_price_et = (EditText) findViewById(R.id.end_price_et);
		third_bai = (LinearLayout) findViewById(R.id.third_bai);
		style_box = (RelativeLayout) findViewById(R.id.style_box);
		henggang = (RelativeLayout) findViewById(R.id.henggang);
		// 初始化排行榜切换对象
		rankRelativeLayout = $(R.id.rank_relative_layout);
		rankSwichLayout = $(R.id.rank_switch_layout);
		rankSwichShow = $(R.id.rank_swich_show);
		rankSwitchDiv = $(R.id.rank_switch_div);
		rankMoreLayout = $(R.id.rank_more_layout);
		scanle = $(R.id.scanle);
		
		back_third =(ImageButton) findViewById(R.id.back_third);
		mexpandableListView = (ExpandableListView) findViewById(R.id.mexpandableListView);
		begin_price_et = (EditText) findViewById(R.id.begin_price_et);
		third = (LinearLayout) findViewById(R.id.third);
		filter_img = (ImageView) findViewById(R.id.filter_img);
		mtop = (ImageView) findViewById(R.id.mtop);
		mzhezhao = (ImageView) findViewById(R.id.mzhezhao);
		shopbox = (LinearLayout) findViewById(R.id.shopbox);
		second_hei = (LinearLayout) findViewById(R.id.second_hei);
		second_bai = (RelativeLayout) findViewById(R.id.second_bai);
		second = (LinearLayout) findViewById(R.id.second);
		biankuang1 = (RelativeLayout) findViewById(R.id.biankuang1);
		biankuang2 = (RelativeLayout) findViewById(R.id.biankuang2);
		view_box = (LinearLayout) findViewById(R.id.view_box);
		sellrank = (TextView) findViewById(R.id.sellrank);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		request = (TextView) findViewById(R.id.request);
		ensure = (TextView) findViewById(R.id.ensure);
		filter = (TextView) findViewById(R.id.filter);
		
		filter_price = (TextView) findViewById(R.id.filter_price);
		compositerank = (TextView) findViewById(R.id.compositerank);
//		scrollView_home = (XScrollView) findViewById(R.id.scrollView_home);
		mgridView_main = (GridView) findViewById(R.id.mgridView_main);
		result_list = (ListView) findViewById(R.id.result_list);
		to_top_btn = (ImageButton) findViewById(R.id.to_top_btn);
		correctLayout = $(R.id.correct_layout);
		correctLayout1 = $(R.id.correct_layout1);
		correctTv = $(R.id.correct_tv);
		correctTv1 = $(R.id.correct_tv1);
		tipsLayout = (RelativeLayout) findViewById(R.id.tips_layout);
		tipsKeys = $(R.id.tips_keys);
		
		topbar_search_input = (TextView) findViewById(R.id.topbar_search_input);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_search_btn = (ImageButton) findViewById(R.id.topbar_search_btn);
		mComposite = (RelativeLayout) findViewById(R.id.mComposite);
		mnumber = (RelativeLayout) findViewById(R.id.mnumber);
		mprice = (RelativeLayout) findViewById(R.id.mprice);
		mfilter = (RelativeLayout) findViewById(R.id.mfilter);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
		xrefresh1 = (XRefreshView) findViewById(R.id.xrefresh1);
//		xrefresh.setAutoLoadMore(true);
//		xrefresh1.setAutoLoadMore(true);
		
		
		compositerank.setTextColor(Color.parseColor("#0098ff"));
		sellrank.setTextColor(Color.parseColor("#222222"));
		filter_price.setTextColor(Color.parseColor("#222222"));
		onrefresh(xrefresh);
		onrefresh(xrefresh1);
		
//		xrefresh.setMoveFootWhenDisablePullLoadMore(true);
		biankuang1.setOnClickListener(this);
		biankuang2.setOnClickListener(this);
		topbar_search_input.setOnClickListener(this);
		mComposite.setOnClickListener(this);
		mnumber.setOnClickListener(this);
		mprice.setOnClickListener(this);
		mfilter.setOnClickListener(this);
		topbar_search_btn.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
		to_top_btn.setOnClickListener(this);
		second_bai.setOnClickListener(this);
		second_hei.setOnClickListener(this);
		third_bai.setOnClickListener(this);
		third_hei.setOnClickListener(this);
		style_box.setOnClickListener(this);
		back_third.setOnClickListener(this);
		ensure.setOnClickListener(this);
		request.setOnClickListener(this);
		mzhezhao.setOnClickListener(this);
//		scrollView_home.setOnScrollListener(new OnScrollListener() {
//			
//			@Override
//			public void onScrollStateChanged(ScrollView view, int scrollState, boolean arriveBottom) {
//				switch (scrollState) {
//				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//					// 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
//					Glide.with(ResultMainActivity.this).pauseRequests();
//					isrequest = false;
//					break;
//				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//					
//					break;
//				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//					// 当ListView处于静止状态时，继续加载图片
//					Glide.with(ResultMainActivity.this).resumeRequests();
//					isrequest = true;
//					break;
//				}
//			}
//			
//			@Override
//			public void onScroll(int l, int t, int oldl, int oldt) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		//		initfourrank();
		
		
	}

	private void onrefresh(XRefreshView xrefresh2) {
		xrefresh2.setXRefreshViewListener(new XRefreshViewListener() {
			
			@Override
			public void onRelease(float direction) {

			}
			
			@Override
			public void onRefresh(boolean isPullDown) {
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				currentPageIndex=1;
				initData1();
			}
			
			@Override
			public void onRefresh() {

			}
			
			@Override
			public void onLoadMore(boolean isSilence) {		
				if (canLoadMore) {
					currentPageIndex++;
					loadData();
				}
			}
			
			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
		
			}
		});
		
		MyFootView footView = new MyFootView(this);
		xrefresh2.setCustomFooterView(footView);
	}
	private void loadData() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("stype", String.valueOf(SearchFragment.stypeWay));
		paramsMap.put("keyword", keyword);
		paramsMap.put("productType", addition);
		paramsMap.put("brand", brand);
		paramsMap.put("bprice", bPrice);
		paramsMap.put("eprice", ePrice);
		paramsMap.put("domains", domains);
		initstype();
		paramsMap.put("stype", stype);
		String filter = getFilterString();
		paramsMap.put("filter", filter);
		paramsMap.put("sortWay", sortway);
		paramsMap.put("client", "Android");
		paramsMap.put("page", currentPageIndex + "");

		dataFlow1.requestData(2, "apiService/getPageList", paramsMap, this);
	}
	private void initstype(){
		if (!istv1&&!istv2) {
			stype ="0";
		}else if(istv1&&!istv2){
			stype ="1";
		}else if(!istv1&&istv2){
			stype ="2";
		}else{
			stype ="3";
		}
	}

	/**
	 * 获取多选过滤条件
	 * 
	 * @return
	 */
	private String getFilterString() {
		JSONObject filterJO = new JSONObject();
		if (filterMap != null) {
			Set<String> keys = filterMap.keySet();
			for (String key : keys) {
				if (!key.equals("商城")) {
					List<Map<String, Object>> data = filterMap.get(key);
					StringBuffer sb = new StringBuffer();
					for (Map<String, Object> map : data) {
						if (map.get("item_selected").equals("yes")) {
							sb.append(map.get("item_text") + "、");
						}
					}
					if (sb.length() > 0) {
						isfilter = true;
						sb.deleteCharAt(sb.length() - 1);
						try {
							filterJO.put(key, sb.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}else{
						isfilter = false;
					}
				}
				
			}
		}else{
			isfilter = false;
		}
		return filterJO.toString();
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.topbar_search_input:
			intent = new Intent(this, SearchMainActivity.class);
			if (keyword.startsWith("@@")) {
				intent.putExtra("keyword", "");
			}else{
				intent.putExtra("keyword", keyword);
			}
			startActivity(intent);
			break;
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mzhezhao:
			mzhezhao.setVisibility(View.GONE);
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
			bPrice = begin_price_et.getText().toString();
			ePrice = end_price_et.getText().toString();
			if (bPrice.isEmpty()  || ePrice.isEmpty()) {
				if (bPrice.isEmpty() ) {
					bPrice = "0";
				}
				if (ePrice.isEmpty()) {
					ePrice = "10000";
				}
			}
			if (Integer.valueOf(bPrice)>Integer.valueOf(ePrice)) {
				 if (toast!= null) {
					toast.cancel();
				}
				 toast = Toast.makeText(ResultMainActivity.this, "最低价不能高于最高价", Toast.LENGTH_SHORT);
				 toast.show();
			}else{
				if (!isfilter && "".equals(brand) &&"".equals(sort_text.getText().toString())&& !istv1 && !istv2) {
					filter_img.setImageResource(R.mipmap.shaixuan_01);
					filter.setTextColor(Color.parseColor("#222222"));
				}else{
					filter_img.setImageResource(R.mipmap.shaixuan_02);
					filter.setTextColor(Color.parseColor("#0098ff"));
				}
				second.setVisibility(View.GONE);
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				initData();
			}
			break;
		case R.id.request:
			try {
				data.clear();
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				istv1 = false;
				tv1.setTextColor(Color.parseColor("#222222"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview);
				istv2 = false;
				tv2.setTextColor(Color.parseColor("#222222"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview);
				loadFilterBrand(oldinfo);
				loadfilterMap();
				loadFilterCheckView(info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.back_third:
			third.setVisibility(View.GONE);
			break;
		case R.id.style_box:
			third.setVisibility(View.VISIBLE);
			break;
		case R.id.third_hei:
			third.setVisibility(View.GONE);
			break;
		case R.id.second_bai:
			break;
		case R.id.second_hei:
			second.setVisibility(View.GONE);
			break;
		case R.id.to_top_btn:
			result_list.smoothScrollToPosition(0);
			mgridView_main.smoothScrollToPosition(0);
//			scrollView_home.fullScroll(ScrollView.FOCUS_UP);
			break;
		case R.id.topbar_search_btn:
			if (islistview) {
				islistview = false;
				topbar_search_btn.setImageResource(R.mipmap.lietu);
				xrefresh.setVisibility(View.VISIBLE);
				xrefresh1.setVisibility(View.GONE);
			}else{
				islistview = true;
				topbar_search_btn.setImageResource(R.mipmap.liebiao);
				xrefresh.setVisibility(View.GONE);
				xrefresh1.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.mfilter:
			second.setVisibility(View.VISIBLE);			
			break;
		case R.id.mprice:
			filter_price.setTextColor(Color.parseColor("#0098ff"));
			sellrank.setTextColor(Color.parseColor("#222222"));
			compositerank.setTextColor(Color.parseColor("#222222"));
			currentPageIndex=1;
			if (isprice) {
				sortway = "1";
				mtop.setImageResource(R.mipmap.gaodi_02);
				isprice = false;
			}else{
				sortway = "2";
				mtop.setImageResource(R.mipmap.gaodi_03);
				isprice = true;
			}
			initData();
			break;
		case R.id.mnumber:
			mtop.setImageResource(R.mipmap.gaodi_01);
			sellrank.setTextColor(Color.parseColor("#0098ff"));
			compositerank.setTextColor(Color.parseColor("#222222"));
			filter_price.setTextColor(Color.parseColor("#222222"));
			currentPageIndex=1;
			sortway = "3";
			initData();
			break;
		case R.id.mComposite:
			mtop.setImageResource(R.mipmap.gaodi_01);
			compositerank.setTextColor(Color.parseColor("#0098ff"));
			sellrank.setTextColor(Color.parseColor("#222222"));
			filter_price.setTextColor(Color.parseColor("#222222"));
			currentPageIndex=1;
			sortway = "0";
			initData();
			break;

			
		default:
			break;
		}
	}

	private void loadfilterMap() {
		Set<String> ks = filterMap.keySet();
		for (String key : ks) {
			List<Map<String, Object>> list = filterMap.get(key);
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				map.put("item_selected", "no");
			}
		}
	}

	/**
	 * 初始化排行榜快速切换样式 并添加监听
	 */
	private void initRankView() {
		rankRelativeLayout.setBackgroundColor(Color.parseColor("#00000000"));
		if (rankSwitchDivHeight == 0) {
			int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
			rankSwitchDiv.measure(inth, inth);
			rankSwitchDivHeight = rankSwitchDiv.getMeasuredHeight();
		}
		rankSwichLayout.setTranslationY(rankSwitchDivHeight);
		rankSwichShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isSwichRankShow) {
					closeRrank();
				} else {
					openRank();
				}
			}
		});
		rankRelativeLayout.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (isSwichRankShow && arg1.getAction() == MotionEvent.ACTION_DOWN) {
					closeRrank();
					return true;
				}
				return false;
			}
		});

		int count = rankMoreLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = rankMoreLayout.getChildAt(i);
			final int index = i;
			view.setOnClickListener(new OnClickListener() {
				private String rankIndex;

				@Override
				public void onClick(View view) {
					closeRrank();
					switch (index) {
					case 0:
						rankIndex = "5";
						break;
					case 1:
						rankIndex = "6";
						break;
					case 2:
						rankIndex = "2";
						break;
					case 3:
						rankIndex = "3";
						break;

					default:
						rankIndex = "5";
						break;
					}
					try {
						intentRank(rankIndex);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					
				}
			});
		}
	}

	private void intentRank(String rankIndex) throws JSONException{
		initaddtion1List();
		Intent intent = new Intent(ResultMainActivity.this, RankActivity.class);
		intent.putExtra("position", String.valueOf(position8));
		intent.putExtra("rankIndex", rankIndex);
		intent.putStringArrayListExtra("listname",listname);
		intent.putStringArrayListExtra("listaddtion",listaddtion);
		startActivity(intent);
	}
	private void initaddtion1List() throws JSONException{
		// 获取系统全部分类
		String jsonStr = AppJsonFileReader.getJson(getBaseContext(), "productType.json");
		JSONArray jsonArr = new JSONArray(jsonStr);
		int length = jsonArr.length();
		for (int i = 0; i < length; i++) {
			JSONObject jsonObj = jsonArr.optJSONObject(i);
			if (jsonObj.optString("addtion").length() == 6
					&& jsonObj.optString("addtion").substring(0, 4).equals(addtion1.substring(0, 4))) {
				String str = jsonObj.optString("addtion");
				String name = jsonObj.optString("productTypeCh");
				listaddtion.add(str);
				listname.add(name);
			}
		}
		for (int i = 0; i < listaddtion.size(); i++) {
			if (addtion1.equals(listaddtion.get(i))) {
				position8 = i;
				break;
			}
		}
	}

	/**
	 * 打开排行榜快速切换
	 */
	private void openRank() {
		PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat("translationY", rankSwitchDivHeight, 0);
		ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(rankSwichLayout, pvhTranslationY);

		ObjectAnimator anim = ObjectAnimator.ofObject(rankRelativeLayout, "BackgroundColor", new BGAlphaEvaluator(), 0,
				155);

		AnimatorSet animatorSet = new AnimatorSet();
		Collection<Animator> items = new ArrayList<>();
		items.add(animator1);
		items.add(anim);
		animatorSet.playTogether(items);
		animatorSet.setDuration(230);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator arg0) {
				isSwichRankShow = true;
				rankSwichShow.setVisibility(View.GONE);
			}
		});
		animatorSet.start();
	}

	/**
	 * 关闭排行榜快速切换
	 */
	private void closeRrank() {
		PropertyValuesHolder pvhTranslationY = PropertyValuesHolder.ofFloat("translationY", 0, rankSwitchDivHeight);
		ObjectAnimator animator1 = ObjectAnimator.ofPropertyValuesHolder(rankSwichLayout, pvhTranslationY);

		ObjectAnimator anim = ObjectAnimator.ofObject(rankRelativeLayout, "BackgroundColor", new BGAlphaEvaluator(),
				155, 0);

		AnimatorSet animatorSet = new AnimatorSet();
		Collection<Animator> items = new ArrayList<>();
		items.add(animator1);
		items.add(anim);
		animatorSet.playTogether(items);
		animatorSet.setDuration(230);
		animatorSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator arg0) {
				isSwichRankShow = false;
				rankSwichShow.setVisibility(View.VISIBLE);
			}
		});
		animatorSet.start();
	}
	/**
	 * 加载ListView列表
	 * 
	 * 
	 */
	private void initListViewData(JSONObject json) {
		try {		
				String tmp = json.optString("page");
				if (!tmp.isEmpty()) {
					JSONArray arr = new JSONArray(tmp);
					addmap(arr, itemList,0,arr.length());	
				}
			listAdapter = new ResultMyListAdapter(itemList, this);
			result_list.setAdapter(listAdapter);
			result_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					Intent intent;
					if (JumpIntentUtil.isJump(itemList,position,"domain1")) {
						intent = new Intent(ResultMainActivity.this,IntentActivity.class);
						if ("0".equals(itemList.get(position).get("androidurl"))) {
							intent.putExtra("url", itemList.get(position).get("url").toString());
						}else{
							intent.putExtra("url", itemList.get(position).get("androidurl").toString());
						}
						intent.putExtra("title", itemList.get(position).get("title").toString());
						intent.putExtra("domain", itemList.get(position).get("domain1").toString());
						intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
					}else{
						intent = new Intent(ResultMainActivity.this,WebViewActivity.class);
						if ("0".equals(itemList.get(position).get("androidurl"))) {
							intent.putExtra("url", itemList.get(position).get("url").toString());
						}else{
							intent.putExtra("url", itemList.get(position).get("androidurl").toString());
						}
						intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
					}
					startActivity(intent);

				}
			});
			gridviewadapter = new ResultMyGridAdapter(itemList, this);
			mgridView_main.setAdapter(gridviewadapter);
			mgridView_main.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					Intent intent;
					if (JumpIntentUtil.isJump(itemList,position,"domain1")) {
						intent = new Intent(ResultMainActivity.this,IntentActivity.class);
						if ("0".equals(itemList.get(position).get("androidurl"))) {
							intent.putExtra("url", itemList.get(position).get("url").toString());
						}else{
							intent.putExtra("url", itemList.get(position).get("androidurl").toString());
						}
						intent.putExtra("title", itemList.get(position).get("title").toString());
						intent.putExtra("domain", itemList.get(position).get("domain1").toString());
						intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
					}else{
						intent = new Intent(ResultMainActivity.this,WebViewActivity.class);
						if ("0".equals(itemList.get(position).get("androidurl"))) {
							intent.putExtra("url", itemList.get(position).get("url").toString());
						}else{
							intent.putExtra("url", itemList.get(position).get("androidurl").toString());
						}
						intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
					}
					startActivity(intent);
				}
			});

			isLoad = false;						
		} catch (JSONException e) {
//			Log.e("=======ResultMainActivity=======", e+"");
			e.printStackTrace();
		}
		
		
	}
	private void addmap(JSONArray arr,List<Map<String, Object>> list,int j,int k){
		for (int i = j; i < k; i++) {
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("img", arr.optJSONObject(i).optString("imgUrl"));
			itemMap.put("title",
			arr.optJSONObject(i).optString("title").replace("<span>", "").replace("</span>", ""));
			String price = arr.optJSONObject(i).optString("price");
			itemMap.put("price", price);
			itemMap.put("hnumber", arr.optJSONObject(i).optString("comnum"));
			itemMap.put("domainCount", arr.optJSONObject(i).optString("domainCount"));
			itemMap.put("groupRowKey", arr.optJSONObject(i).optString("groupRowkey"));
			itemMap.put("quote", arr.optJSONObject(i).optString("numberCount"));
			itemMap.put("allDomain", arr.optJSONObject(i).optString("alldomain"));
			itemMap.put("hassimi", arr.optJSONObject(i).optString("hassimi"));
			itemMap.put("tarr", arr.optJSONObject(i).optString("tarr").toString());
			itemMap.put("domain1",  arr.optJSONObject(i).optString("domain"));
			itemMap.put("url",  arr.optJSONObject(i).optString("url"));
			if (arr.optJSONObject(i).has("saleinfo")){
				itemMap.put("saleinfo",  arr.optJSONObject(i).optString("saleinfo"));
			}else {
				itemMap.put("saleinfo",  "-1");
			}
			if (arr.optJSONObject(i).has("yjson")){
				itemMap.put("yjson",  arr.optJSONObject(i).optString("yjson"));
			}else {
				itemMap.put("yjson",  "0");
			}
			if (arr.optJSONObject(i).has("androidurl")) {
				itemMap.put("androidurl",  arr.optJSONObject(i).optString("androidurl"));
			}else{
				itemMap.put("androidurl",  "0");
			}
			itemMap.put("keyword",  keyword);
			itemMap.put("isxianshi", "1");
			if (arr.optJSONObject(i).has("purl")) {
				itemMap.put("purl",  arr.optJSONObject(i).optString("purl"));
			}else{
				itemMap.put("purl",  "0");
			}
			itemList.add(itemMap);
			itemList1.add(itemMap);
		}	
	}
	private void loadFilterCheckView(JSONObject result) throws JSONException {
		if (filterMap == null) {
			filterMap = new HashMap<>();
			JSONObject jsonObject = result.optJSONObject("filter");
			if (jsonObject != null) {
				Iterator<String> keys = jsonObject.keys();
				while (keys.hasNext()) {
					String key = keys.next();
					String value = jsonObject.getString(key);
					String[] vs = value.split("\\|");
					final List<Map<String, Object>> data = new ArrayList<>();
					for (int i = 0; i < vs.length; i++) {
						Map<String, Object> map = new HashMap<>();
						map.put("item_text", vs[i]);
						map.put("item_selected", "no");
						data.add(map);
					}
					filterMap.put(key, data);
				}
			}
		}
		if (filterMap!=null) {
			Set<String> ks = filterMap.keySet();
			for (String key : ks) {
				if (key.equals("商城")) {
					View shopview = loadfiltershop(key);
					shopbox.addView(shopview);
				}else{
					View view2 = loadfilterchild(key);
					view_box.addView(view2);
				}
		}	
		}
		
	}
	private View loadfiltershop(String key){
		final List<Map<String, Object>> data = filterMap.get(key); 
		final List<Map<String, Object>> data1 = new ArrayList<>();
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
		final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
		RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
		TextView title = (TextView) view.findViewById(R.id.mtext);
		title.setText(key);
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
						domains = (String) str.subSequence(1, str.length());
					}else{
						select.setText(null);
						domains = "";
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
							domains = (String) item.get("item_text").toString();
							select.setText(item.get("item_text").toString());
						}else{
							domains = (String)select.getText()+","+item.get("item_text").toString();
							select.setText(select.getText()+","+item.get("item_text").toString());
						}
					}else{
						if (toast!=null) {
							toast.cancel();
						}
						toast = Toast.makeText(ResultMainActivity.this, "最多只能选5个", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		return view;
	}
	private View loadfilterchild(String key){
		final List<Map<String, Object>> data = filterMap.get(key); 
		final List<Map<String, Object>> data1 = new ArrayList<>();
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
		final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
		RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
		TextView title = (TextView) view.findViewById(R.id.mtext);
		title.setText(key);
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
					}else{
						select.setText(null);
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
							select.setText(item.get("item_text").toString());
						}else{
							select.setText(select.getText()+","+item.get("item_text").toString());
						}
					}else{
						if (toast!=null) {
							toast.cancel();
						}
						toast = Toast.makeText(ResultMainActivity.this, "最多只能选5个", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		return view;
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
			adapter = new SecondAdapter5(ResultMainActivity.this, data);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (position==8) {
						Intent intent = new Intent(ResultMainActivity.this, ReslutBrandActivity.class);
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
	 * 加载分类界面
	 */
	private void loadThirdSort(JSONObject json) {
		typeStr = json.optString("tree");
		if (!typeStr.isEmpty()) {
			// 获取系统全部分类
			String jsonStr = AppJsonFileReader.getJson(getBaseContext(), "productType.json");
			// 转换当前搜索结果分类
			String[] typeArr = typeStr.split("\\|");
			try {
				JSONArray jsonArr = new JSONArray(jsonStr);
				int length = jsonArr.length();

				for (int i = 0; i < typeArr.length; i++) {
					typeArr[i] = typeArr[i].split(":")[0];
					for (int j = 0; j < length; j++) {
						JSONObject jsonObj = jsonArr.optJSONObject(j);
						if (jsonObj.optString("addtion").length() == 6
								&& jsonObj.optString("productType").equals(typeArr[i])) {
							thirdTypeJsonArr.put(jsonObj);
							break;
						}
					}
				}

				int len = thirdTypeJsonArr.length();

				for (int i = 0; i < len; i++) {
					JSONObject typeJsonObj = thirdTypeJsonArr.optJSONObject(i);
					for (int j = 0; j < length; j++) {
						JSONObject jsonObj = jsonArr.optJSONObject(j);
						String addtion = jsonObj.optString("addtion");
						if (addtion.length() == 4 && addtion.equals(typeJsonObj.optString("addtion").substring(0, 4))) {
							int mainLength = secondTypeJsonArr.length();
							boolean isAdded = false;
							for (int k = 0; k < mainLength; k++) {
								JSONObject mainTypeJsonObj = secondTypeJsonArr.optJSONObject(k);
								if (mainTypeJsonObj.optString("addtion").equals(addtion)) {
									isAdded = true;
									break;
								}
							}

							if (!isAdded) {
								secondTypeJsonArr.put(jsonObj);
							}
						}
					}
				}
				for (int i = 0; i < secondTypeJsonArr.length(); i++) {
					JSONObject jsonObj = secondTypeJsonArr.optJSONObject(i);
					HashMap<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("item_text", jsonObj.optString("productTypeCh"));
					itemMap.put("item_addtion", jsonObj.optString("addtion"));
					parentlist.add(itemMap);
				}	
				
				
				for (int i = 0; i < parentlist.size(); i++) {
					String text = parentlist.get(i).get("item_addtion").toString();
					List<Map<String, Object>> list = new ArrayList<>();
					for (int j = 0; j < thirdTypeJsonArr.length(); j++) {
						JSONObject object = thirdTypeJsonArr.optJSONObject(j);
						if (object.optString("addtion").length() == 6
						&& object.optString("addtion").substring(0, 4).equals(text)) {
						HashMap<String, Object> itemMap = new HashMap<String, Object>();
						itemMap.put("item_text", object.optString("productTypeCh"));
						itemMap.put("item_addtion", object.optString("addtion"));
						list.add(itemMap);
						}
					}
					childlist.add(list);
				}
				
				exadapter.notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	private void initChildViews(String[] str) {  
		 MarginLayoutParams lp = new MarginLayoutParams(
	                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	        lp.leftMargin = 15;
	        lp.rightMargin = 15;
	        lp.topMargin = 15;
	        lp.bottomMargin = 15;
	        for(int i = 0; i < str.length; i ++){
	            TextView view = new TextView(this);
	            final String st = str[i];
	            view.setText(str[i]);
	            view.setTextColor(Color.parseColor("#222222"));
	            view.setGravity(Gravity.CENTER_VERTICAL);
	            view.setPadding(30, 10, 30, 10);
	            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_view_border));
	            view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if (view_box!=null) {
							view_box.removeAllViews();
						}
						if (shopbox!=null) {
							shopbox.removeAllViews();
						}
						currentPageIndex=1;
						keyword = st;
						initData();
					}
				});
	            mflowlayout.addView(view,lp);
	        }
    }  
    private void ViewGone(){
    	tipsLayout.setVisibility(View.GONE);
		correctLayout.setVisibility(View.GONE);
		correctLayout1.setVisibility(View.GONE);
		tuijianText.setVisibility(View.GONE);
		mflowlayout.setVisibility(View.GONE);
		henggang.setVisibility(View.GONE);
    }
    
    private void NowPrice(){
    	thread = new Thread(new Runnable() {
			public void run() {
				while (isrun) {
					if (isrequest == true) {
						try {
							Map<String, String> params = new HashMap<>();
							if (!"0".equals(itemList1.get(requestnum).get("purl"))) {
								String str;
								String content;
								params.put("domain", itemList1.get(requestnum).get("domain1").toString());
								params.put("rowkey", itemList1.get(requestnum).get("groupRowKey").toString());
								params.put("fromwhere", "android"+keyword);
								if (itemList1.get(requestnum).get("purl").toString().contains("||")) {
									String url = itemList1.get(requestnum).get("purl").toString();
									String[] split = url.split("\\|\\|");
									String referrer=split[1];
									content = HttpUtil.getHttp1(params, split[0], ResultMainActivity.this, referrer);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", ResultMainActivity.this);
								}else{
									content = HttpUtil.getHttp1(params, itemList1.get(requestnum).get("purl").toString(), ResultMainActivity.this,null);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", ResultMainActivity.this);
								}
								JSONObject object = new JSONObject(str);
								if ("3".equals(object.optString("type"))) {
									if ("".equals(object.optString("url"))) {
										content = HttpUtil.getHttp1(params, itemList1.get(requestnum).get("url").toString(), ResultMainActivity.this,null);
									}else{
										content = HttpUtil.getHttp1(params, object.optString("url"), ResultMainActivity.this,null);
									}
									params.put("pcontent", content);
									String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct";
									str = HttpUtil.getHttp(params, url, ResultMainActivity.this);
								}
								Message mes = handler.obtainMessage();
								mes.obj = str;
								mes.arg1 = requestnum;
								mes.what =0;
								handler.sendMessage(mes);
							}
							if (requestnum+1 >= itemList1.size()) {
								isrequest = false;
							}
							requestnum++;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							continue;
						}
					}
				}
			}
		});
		thread.start();
    }
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (isrequest == true){
			if (msg.what == 0) {
				String str = msg.obj.toString();
				int i = msg.arg1;
				try {
					JSONObject object = new JSONObject(str);
					switch (object.optString("type")) {
					case "0":
						itemList.remove(i-removenum);
						listAdapter.notifyDataSetChanged();
						gridviewadapter.notifyDataSetChanged();
						removenum++;
						if (currentPageIndex == 1 && i%12 == 0 && removenum >6) {
							if (canLoadMore) {
								currentPageIndex++;
								loadData();
							}
						}
						break;
					case "1":
						String price = object.optString("price");
						String saleinfo = object.optString("saleinfo");
						if (itemList.size()!= 0){
							itemList.get(i-removenum).put("price", price);
							itemList.get(i-removenum).put("saleinfo", saleinfo);
							listAdapter.notifyDataSetChanged();
							gridviewadapter.notifyDataSetChanged();
						}
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what == 1){
				listAdapter.notifyDataSetChanged();
				gridviewadapter.notifyDataSetChanged();
			}
		}
		}
	};


	private void inittmallmore(final String tmallSearchUrl) {
		threadtmall = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<>();
					String content = HttpUtil.getHttp1(params, tmallSearchUrl, ResultMainActivity.this, null);
					String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/parseTmall";
					params.put("content", content);
					String str = HttpUtil.getHttp(params, url, ResultMainActivity.this);
					JSONArray arr = new JSONArray(str);
					for (int i = 0; i < arr.length(); i++) {
						HashMap<String, Object> itemMap = new HashMap<String, Object>();
						itemMap.put("img", arr.optJSONObject(i).optString("imgUrl"));
						itemMap.put("title",
						arr.optJSONObject(i).optString("title").replace("<span>", "").replace("</span>", ""));
						String price = arr.optJSONObject(i).optString("price");
						itemMap.put("price", price);
						itemMap.put("hnumber", arr.optJSONObject(i).optString("comnum"));
						itemMap.put("url",  arr.optJSONObject(i).optString("url"));
						itemMap.put("domain1",  "tmall");
						itemMap.put("isxianshi", "0");
						itemList.add(itemMap);
					}	
					Message mes = handler.obtainMessage();
					mes.what =1;
					handler.sendMessage(mes);
				} catch (Exception e) {
//					Log.e("======ResultMainActivity=========", e+"");
					e.printStackTrace();
				}
			}
		});
		threadtmall.start();
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
			switch (requestCode) {
			case 1:
				requestnum = 0;
				removenum = 0;
				xrefresh.setAutoLoadMore(true);
				xrefresh1.setAutoLoadMore(true);
				data.clear();	
				itemList.clear();
				itemList1.clear();
				if (keyword.startsWith("@@")) {
					topbar_search_input.setText("");
				}else{
					topbar_search_input.setText(keyword);
				}
				xrefresh.stopRefresh();
				xrefresh1.stopRefresh();
				JSONObject jo11 = new JSONObject(content);
				String tmallSearchUrl = jo11.optString("tmallSearchUrl");
				if (!jo11.optString("sortAddtion").isEmpty()) {
					rankRelativeLayout.setVisibility(View.VISIBLE);
					addtion1 = jo11.optString("sortAddtion");
					initRankView();
				}
								
				String  isBland= jo11.optString("isBland");				
				
				if (currentPageIndex == 1) {
					ViewGone();
					switch (isBland) {
					case "1":
						
						break;
					case "2":
						
						String tj = jo11.optString("tuijian");
						if (!TextUtils.isEmpty(tj)) {
							String[] tjs = tj.split(",");
							initChildViews(tjs);
							mflowlayout.setVisibility(View.VISIBLE);
							henggang.setVisibility(View.VISIBLE);
							correctLayout1.setVisibility(View.VISIBLE);
						} else {
							tipsLayout.setVisibility(View.VISIBLE);
							tipsKeys.setText("没有找到相关商品");
						}
						break;
					case "3":
						
						keyword = jo11.optString("blandkey");
						correctTv.setText("没有找到相关的商品， 推荐“" + keyword + "”的搜索结果,试试");
						tuijianText.setText(keyword);
						tuijianText.setVisibility(View.VISIBLE);
						tuijianText.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								if (view_box!=null) {
									view_box.removeAllViews();
								}
								if (shopbox!=null) {
									shopbox.removeAllViews();
								}
								currentPageIndex=1;
								initData();
							}
						});
						correctLayout.setVisibility(View.VISIBLE);
						
						break;
					}
				}
				info = jo11.getJSONObject("info");
				if (isfirstinfo){
					oldinfo = jo11.getJSONObject("info");
					isfirstinfo = false;
				}
				String tmp1 = info.optString("page");
				if (!tmp1.isEmpty()) {
					initListViewData(info);
					isrequest = true;
//					result_list.smoothScrollToPosition(0);
//					mgridView_main.smoothScrollToPosition(0);
//					scrollView_home.fullScroll(ScrollView.FOCUS_UP);
					if (keyword.startsWith("@@")) {
						topbar_search_input.setText("");
						mshaixuanbox.setVisibility(View.GONE);
					}else{
						mshaixuanbox.setVisibility(View.VISIBLE);
						loadFilterBrand(info);
						loadFilterCheckView(info);
						loadThirdSort(info);
					}
					String isFirstResultUse = SharedPreferencesUtil.getSharedData(this,"isFirstUse", "isFirstResultUse");
					if (TextUtils.isEmpty(isFirstResultUse)) {
						isFirstResultUse = "yes";
					}
					if (isFirstResultUse.equals("yes")) {
						SharedPreferencesUtil.putSharedData(this, "isFirstUse","isFirstResultUse", "no");
						mzhezhao.setVisibility(View.VISIBLE);
					} 
					if (thread == null) {
						NowPrice();
					}
					int totalCount = info.optInt("totalCount");
					if (totalCount%12 == 0) {
						pagenum = totalCount/12;
					}else{
						pagenum = (int)(totalCount/12)+1;
					}
					if (pagenum <=currentPageIndex) {
						canLoadMore = false;
						xrefresh.setLoadComplete(true);
						xrefresh1.setLoadComplete(true);
						inittmallmore(tmallSearchUrl);
					} else {
						canLoadMore = true;
						xrefresh.setLoadComplete(false);
						xrefresh1.setLoadComplete(false);
					}
				}else{
					tipsLayout.setVisibility(View.VISIBLE);
					tipsKeys.setText("当前筛选条件下无搜索结果");
				}
				
				break;
			case 2:
				xrefresh.stopLoadMore();
				xrefresh1.stopLoadMore();
				JSONObject jo1 = new JSONObject(content);
				String tmallSearchUrl1 = jo1.optString("tmallSearchUrl");
				if (currentPageIndex == 1) {
					ViewGone();
					switch (jo1.optString("isBland")) {
					case "1":
						
						break;
					case "2":
					
						String tj = jo1.optString("tuijian");
						if (!TextUtils.isEmpty(tj)) {
							String[] tjs = tj.split(",");
							initChildViews(tjs);
							mflowlayout.setVisibility(View.VISIBLE);
							henggang.setVisibility(View.VISIBLE);
							correctLayout1.setVisibility(View.VISIBLE);
						} else {
							tipsLayout.setVisibility(View.VISIBLE);
						}
						break;
					case "3":
						
						keyword = jo1.optString("blandkey");
						correctTv.setText("没有找到相关的商品， 推荐“" + keyword + "”的搜索结果,试试");
						tuijianText.setText(keyword);
						tuijianText.setVisibility(View.VISIBLE);
						tuijianText.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								if (view_box!=null) {
									view_box.removeAllViews();
								}
								if (shopbox!=null) {
									shopbox.removeAllViews();
								}
								currentPageIndex=1;
								initData();
								correctLayout.setVisibility(View.GONE);
							}
						});
						correctLayout.setVisibility(View.VISIBLE);
						break;
					}
				}
				JSONObject info1 = jo1.getJSONObject("info");
				String tmp = info1.optString("page");
				int totalCount = info1.optInt("totalCount");
				if (totalCount%12 == 0) {
					pagenum = totalCount/12;
				}else{
					pagenum = (int)(totalCount/12)+1;
				}
				if (pagenum <= currentPageIndex) {
					canLoadMore = false;
					xrefresh.setLoadComplete(true);
					xrefresh1.setLoadComplete(true);
					inittmallmore(tmallSearchUrl1);
				} else {
					canLoadMore = true;
				}
				JSONArray arr = new JSONArray(tmp);
				addmap(arr, itemList,0,arr.length());	
				listAdapter.notifyDataSetChanged();
				gridviewadapter.notifyDataSetChanged();		
				isrequest = true;
//				scrollView_home.smoothScrollTo(0, scrollY+40);
	
				break;
			case 4:
				data.clear();
				JSONObject object = new JSONObject(content);
				info = object;
				loadFilterBrand(info);
				loadFilterCheckView(info);
				break;
			default:
				break;
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	@Override
	public void onBackPressed() {
		if (third.getVisibility()==View.VISIBLE) {
			third.setVisibility(View.GONE);
			return;
		}else if(third.getVisibility()==View.GONE && second.getVisibility()==View.VISIBLE){
			second.setVisibility(View.GONE);
			return;
		}else{
			isrun = false;
			finish();
			handler.removeCallbacks(thread);
			handler.removeCallbacks(threadtmall);
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		isrun = false;
		handler.removeCallbacks(thread);
		handler.removeCallbacks(threadtmall);
		super.onDestroy();
	}
}
