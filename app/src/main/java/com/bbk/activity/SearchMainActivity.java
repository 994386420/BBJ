package com.bbk.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.baidu.mobstat.StatService;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.SearchResultBean;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.adapter.NewBjAdapter;
import com.bbk.adapter.NewBlAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.NewFxAdapter;
import com.bbk.adapter.NewHomeAdapter;
import com.bbk.adapter.ResultExpandableListViewAdapter;
import com.bbk.adapter.ResultMyGridAdapter;
import com.bbk.adapter.ResultMyListAdapter;
import com.bbk.adapter.SecondAdapter4;
import com.bbk.adapter.SecondAdapter5;
import com.bbk.adapter.SsNewCzgAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.evaluator.BGAlphaEvaluator;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.SearchFragment;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.AppJsonFileReader;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.DensityUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.ValidatorUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.FooterView;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.bbk.view.MyGridView;
import com.bbk.view.XCFlowLayout;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class SearchMainActivity extends ActivityGroup implements
		OnClickListener, OnKeyListener,ResultEvent,CommonLoadingView.LoadingHandler{
	private FrameLayout mContent;
	private EditText searchText;
	private ImageButton goBackBtn, searchBtn;
	private SearchHistoryDao dao;
	private static final String SEARCH_MAIN_RECOMMEND = "recommemd";
	private static final String SEARCH_MAIN_PROMPT = "prompt";
	private List<String> dataList;
	private String keyword="";
	private Typeface typeFace;
	private ListView mlistView;
	private ArrayAdapter<String> adapter1;
	private LinearLayout bijia_layout,czg_layout;
	private View bijia_view,czg_view;
	private RelativeLayout mLayout;//隐藏的标题栏
	private RelativeLayout msuccessLayout;//搜索成功界面
	private boolean isprice = true;
	public static Activity instance;
	private TextView topbar_search_input,filter_price,compositerank,sellrank;
	private TextView paixu_czg_text,discount_czg_text,filter_price_czg,filter_czg;//超值购导航栏
	private ImageView mtop_czg;
	private RelativeLayout mComposite_czg,discount_czg,mpriceczg,mfilter_czg;
	private ImageButton topbar_goback_btn,topbar_search_btn,to_top_btn,back_third;
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
	private Map<String, List<Map<String, Object>>> filterMap;;
	private RelativeLayout mComposite,mnumber,mprice,mfilter,henggang,biankuang1,biankuang2;
	private RecyclerView result_list;
	private RecyclerView mgridView_main;
	private List<Map<String, Object>> itemList,itemList1;
	private LinearLayout correctLayout,view_box,third_hei,third_bai,second_hei,second,third,correctLayout1,shopbox;
	private TextView correctTv,ensure,request,correctTv1;
	private RelativeLayout tipsLayout,second_bai,style_box;
	private TextView tipsKeys,tuijianText;
	private boolean canLoadMore;
	private ResultMyListAdapter listAdapter;
	private ResultMyGridAdapter gridviewadapter;
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
	private XCFlowLayout mflowlayout = null;
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
	private LinearLayout rankMoreLayout,scanle;
	private String addtion1;
	private ArrayList<String> listname = new ArrayList<>();
	private ArrayList<String> listaddtion = new ArrayList<>();
	private int position8;
	private boolean isrequest = false;
	private int requestnum = 0;
	private int removenum = 0;
	private Thread thread;
	private int pagenum = 0,x = 1;
	private Thread threadtmall;
	private LinearLayout mshaixuanbox,mshaixuanCzg;
	private boolean isrun = true;
	private String Flag = "1",type = "1";
	private boolean isloadShaixuan = true;
	private CommonLoadingView zLoadingView;//加载框
	private String content,contentCzg;//服务器获取数据

	/**
	 * 搜索超值购
	 * @param savedInstanceState
	 */
	private RecyclerView mCzgListview;
	private NewCzgAdapter newCzgAdapter;
	private List<NewHomeCzgBean> newHomeCzgBeans;

	/**
	 * 商品比价搜索结果数据解析
	 * @param savedInstanceState
	 */
	private List<SearchResultBean> searchResultBeans;
	private SmartRefreshLayout xrefresh;
	private SmartRefreshLayout xrefresh2;
	private SmartRefreshLayout xrefresh1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);
		View topView = findViewById(R.id.toolbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow1 = new DataFlow3(this);
		initView();
		registerBoradcastReceiver();
		Timer timer = new Timer();
		final InputMethodManager inputManager =
				(InputMethodManager)searchText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (getIntent().getStringExtra("keyword") != null){
			//对于刚跳到一个新的界面就要弹出软键盘的情况上述代码可能由于界面为加载完全而无法弹出软键盘。此时应该适当的延迟弹出软键盘如998毫秒（保证界面的数据加载完成）
			timer.schedule(new TimerTask()
						   {
							   public void run()
							   {inputManager.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
							   }
						   },
					998);
			keyword = getIntent().getStringExtra("keyword");
			initData();
		}else {
			loadhotKeyword(type);
			dao = new SearchHistoryDao(this);
			timer.schedule(new TimerTask()
						   {
							   public void run()
							   {
								   inputManager.showSoftInput(searchText, 0);
							   }
						   },
					998);
		}
		if (getIntent().getStringExtra("addition") != null){
			addition = getIntent().getStringExtra("addition");
		}
		if (getIntent().getStringExtra("brand") != null){
			brand = getIntent().getStringExtra("brand");
		}
	}
	private void initView() {
		zLoadingView = findViewById(R.id.progress);
		zLoadingView.setLoadingHandler(this);
		final InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		dataList = new ArrayList<String>();
		String str = "1";
		dataList.add(str);
		typeFace = ((MyApplication) getApplication()).getFontFace();
		mContent = (FrameLayout) findViewById(R.id.content);
		mlistView = (ListView)findViewById(R.id.mlistView);
		searchText = (EditText) findViewById(R.id.topbar_search_input);
		mlistView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
				imm.hideSoftInputFromWindow(mlistView.getWindowToken(), 0);
				String text = mlistView.getAdapter().getItem(i).toString();
				searchText.setText(text);
				searchText.setSelection(text.length());
				mlistView.setVisibility(View.GONE);
				doSearch();
			}
		});
		searchBtn = (ImageButton) findViewById(R.id.topbar_search_btn);
		if (!TextUtils.isEmpty(keyword)) {
			searchText.setText(keyword);
			searchText.setSelection(keyword.length());
		}
		goBackBtn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		searchBtn.setOnClickListener(this);
		searchText.addTextChangedListener(watcher);
		searchText.setOnKeyListener(this);
		goBackBtn.setOnClickListener(this);
		mlistView.setAdapter(adapter1);
		mLayout = findViewById(R.id.tabor_layout);
		mLayout.setVisibility(View.GONE);
		msuccessLayout = findViewById(R.id.search);
		itemList = new ArrayList<>();
		itemList1 = new ArrayList<>();
		data = new ArrayList<>();
		parentlist = new ArrayList<>();
		childlist = new ArrayList<>();
		mflowlayout = (XCFlowLayout) findViewById(R.id.mflowlayout);
		third_hei = (LinearLayout) findViewById(R.id.third_hei);
		mshaixuanbox = (LinearLayout) findViewById(R.id.mshaixuanbox);
		mshaixuanCzg = findViewById(R.id.czg_layout);
		tuijianText = (TextView) findViewById(R.id.tuijianText);
		sort_text = (TextView) findViewById(R.id.sort_text);
		end_price_et = (EditText) findViewById(R.id.end_price_et);
		third_bai = (LinearLayout) findViewById(R.id.third_bai);
		style_box = (RelativeLayout) findViewById(R.id.style_box);
		henggang = (RelativeLayout) findViewById(R.id.henggang);
		// 初始化排行榜切换对象
		rankRelativeLayout = findViewById(R.id.rank_relative_layout);
		rankSwichLayout =findViewById(R.id.rank_switch_layout);
		rankSwichShow = findViewById(R.id.rank_swich_show);
		rankSwitchDiv = findViewById(R.id.rank_switch_div);
		rankMoreLayout = findViewById(R.id.rank_more_layout);
		scanle = findViewById(R.id.scanle);

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
		mgridView_main = (RecyclerView) findViewById(R.id.mgridView_main);
		mgridView_main.setLayoutManager(new GridLayoutManager(this,2));
		mgridView_main.setHasFixedSize(true);
		result_list = (RecyclerView) findViewById(R.id.result_list);
		result_list.setHasFixedSize(true);
		result_list.setLayoutManager(new LinearLayoutManager(this));
		to_top_btn = (ImageButton) findViewById(R.id.to_top_btn);
		correctLayout = findViewById(R.id.correct_layout);
		correctLayout1 = findViewById(R.id.correct_layout1);
		correctTv = findViewById(R.id.correct_tv);
		correctTv1 = findViewById(R.id.correct_tv1);
		tipsLayout = (RelativeLayout) findViewById(R.id.tips_layout);
		tipsKeys = findViewById(R.id.tips_keys);
		topbar_search_input = (TextView) findViewById(R.id.topbar_search_input);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_search_btn = (ImageButton) findViewById(R.id.topbar_search_btn);
		mComposite = (RelativeLayout) findViewById(R.id.mComposite);
		mnumber = (RelativeLayout) findViewById(R.id.mnumber);
		mprice = (RelativeLayout) findViewById(R.id.mprice);
		mfilter = (RelativeLayout) findViewById(R.id.mfilter);
		xrefresh =  findViewById(R.id.xrefresh);
		xrefresh1 =  findViewById(R.id.xrefresh1);
		xrefresh2 = (SmartRefreshLayout) findViewById(R.id.xrefresh2);
		xrefresh2.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				x = 1;
				currentPageIndex=1;
				initDataCzg();
			}
		});
		xrefresh2.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(RefreshLayout refreshlayout) {
				x=2;
				currentPageIndex ++;
				initDataCzg();
			}
		});

		compositerank.setTextColor(Color.parseColor("#f23030"));
		sellrank.setTextColor(Color.parseColor("#222222"));
		filter_price.setTextColor(Color.parseColor("#222222"));
		onrefresh(xrefresh);
		onrefresh(xrefresh1);
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

		//商品比价超值购搜索控件
		//超值购view
		mCzgListview = findViewById(R.id.result_list1);
		mCzgListview.setHasFixedSize(true);
		mCzgListview.setLayoutManager(new LinearLayoutManager(this));
		bijia_layout = findViewById(R.id.ll_bj_layout);
		bijia_layout.setOnClickListener(this);
		bijia_view = findViewById(R.id.bj_view);
		czg_layout = findViewById(R.id.ll_czg_layout);
		czg_layout.setOnClickListener(this);
		czg_view = findViewById(R.id.czg_view);
		paixu_czg_text = findViewById(R.id.paixu_czg_text);
		discount_czg_text = findViewById(R.id.discount_czg_text);
		filter_price_czg = findViewById(R.id.filter_price_czg);
		filter_czg = findViewById(R.id.filter_czg);
		mtop_czg = findViewById(R.id.mtop_czg);
		mComposite_czg = findViewById(R.id.mComposite_czg);
		discount_czg = findViewById(R.id.discount_czg);
		mpriceczg = findViewById(R.id.mpriceczg);
		mfilter_czg = findViewById(R.id.mfilter_czg);
		mComposite_czg.setOnClickListener(this);
		discount_czg.setOnClickListener(this);
		mpriceczg.setOnClickListener(this);
		mfilter_czg.setOnClickListener(this);
		paixu_czg_text.setTextColor(Color.parseColor("#f23030"));
	}

	TextWatcher watcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			// TODO Auto-generated method stub
			if (s.length()>0) {
				mlistView.setVisibility(View.VISIBLE);
				dataList.clear();
				getHttpData();
				if (mlistView.getAdapter()!=null) {
					adapter1.notifyDataSetChanged();
				}
			}else {
				mlistView.setVisibility(View.GONE);
			}
			if (s.length() >= 3
					|| ValidatorUtil.isContainsChinese(s.toString())) {
				showSearchPromptView();
				SearchPromptActivity promptActivity = (SearchPromptActivity) getLocalActivityManager()
						.getActivity(SEARCH_MAIN_PROMPT);
				promptActivity.getHttpData(s.toString(), 1);
			} else {
				showSearchRecommendView();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	};

	public void addView(String id, Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		mContent.removeAllViews();
		mContent.addView(getLocalActivityManager().startActivity(id, intent)
				.getDecorView());
	}

	public void addView(String id, Class<?> clazz, String params) {
		Intent intent = new Intent(this, clazz);
		intent.putExtra("keyword", params);
		mContent.removeAllViews();
		mContent.addView(getLocalActivityManager().startActivity(id, intent)
				.getDecorView());
	}
	public void doSearch() {
		mlistView.setVisibility(View.GONE);
		keyword = searchText.getText().toString();
		if (keyword.length() <= 0) {
			StringUtil.showToast(this, "搜索内容为空");
			return;
		}
		if (!dao.exsistHistory(keyword)) {
			dao.addHistory(keyword);
		}
		if (Flag.equals("1")){
			isloadShaixuan = true;
			x = 1;
			currentPageIndex = 1;
			initData();
		}else {
			x = 1;
			currentPageIndex=1;
			initDataCzg();
		}
	}

	public void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_ENTER
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
			}
			doSearch();
			return true;
		}
		return false;
	}


	private void showSearchPromptView() {
		addView(SEARCH_MAIN_PROMPT, SearchPromptActivity.class);
	}

	private void goBack() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		finish();
	}

	@Override
	public void finish() {
		closeKeybord(searchText, SearchMainActivity.this);
		super.finish();
	}


	@Override
	protected void onResume() {
		super.onResume();
		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
		if (clipchange.equals("1")) {
			ClipDialogUtil.creatDialog(this);
		}
	}

	private void onrefresh(SmartRefreshLayout xrefresh2) {
		xrefresh2.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				currentPageIndex=1;
				x = 1;
				initData();
			}
		});
		xrefresh2.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(RefreshLayout refreshlayout) {
				if (canLoadMore) {
					currentPageIndex++;
					x = 2;
					initData();
				}else {
					xrefresh.finishLoadMore();
					xrefresh1.finishLoadMore();
					StringUtil.showToast(SearchMainActivity.this,"没有更多了");
				}
			}
		});
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

		dataFlow1.requestData(2, "apiService/getPageList", paramsMap, this,true);
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

	private void setText(View view){
		view.setVisibility(View.VISIBLE);
	}

	private void setView(){
		czg_view.setVisibility(View.GONE);
		bijia_view.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		Intent intent;
//		String search = searchText.getText().toString();
		switch (v.getId()) {
			case R.id.ll_bj_layout:
				isloadShaixuan = false;
					Flag = "1";
                    type = "1";
				    loadhotKeyword(type);
				    setView();
				    setText(bijia_view);
				if (keyword != null && !keyword.equals("")) {
					currentPageIndex = 1;
					x=1;
					initData();
				}
				break;
			case R.id.ll_czg_layout:
					Flag = "2";
				    type = "2";
				    x = 1;
				    loadhotKeyword(type);
				    setView();
				    setText(czg_view);
				if (keyword!= null && !keyword.equals("")) {
					currentPageIndex = 1;
					x = 1;
					initDataCzg();
				}
				break;
			case R.id.topbar_goback_btn:
				goBack();
				break;
			case R.id.topbar_search_btn:
				StatService.onEvent(SearchMainActivity.this, "search", "搜索产品:搜索页面");
				doSearch();
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
					tv1.setTextColor(Color.parseColor("#f23030"));
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
					tv2.setTextColor(Color.parseColor("#f23030"));
					biankuang2.setBackgroundResource(R.drawable.shaixuan_textview2);
				}
				break;
			case R.id.ensure:
				try {
					isloadShaixuan = true;
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
						StringUtil.showToast(SearchMainActivity.this, "最低价不能高于最高价");
					}else{
						if (!isfilter && "".equals(brand) &&"".equals(sort_text.getText().toString())&& !istv1 && !istv2) {
							filter_img.setImageResource(R.mipmap.shaixuan_01);
							filter.setTextColor(Color.parseColor("#222222"));
						}else{
							filter_img.setImageResource(R.mipmap.shaixuan_02);
							filter.setTextColor(Color.parseColor("#f23030"));
						}
						second.setVisibility(View.GONE);
						if (view_box!=null) {
							view_box.removeAllViews();
						}
						if (shopbox!=null) {
							shopbox.removeAllViews();
						}
						x = 1;
						currentPageIndex = 1;
						initData();
					}
				}catch (Exception e){
					e.printStackTrace();
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
				mCzgListview.smoothScrollToPosition(0);
				mgridView_main.smoothScrollToPosition(0);
//			scrollView_home.fullScroll(ScrollView.FOCUS_UP);
				break;
			case R.id.mfilter:
				second.setVisibility(View.VISIBLE);
				break;
			case R.id.mprice:
				isloadShaixuan = false;
				filter_price.setTextColor(Color.parseColor("#f23030"));
				sellrank.setTextColor(Color.parseColor("#222222"));
				compositerank.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				x = 1;
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
				isloadShaixuan = false;
				mtop_czg.setImageResource(R.mipmap.gaodi_01);
				sellrank.setTextColor(Color.parseColor("#f23030"));
				compositerank.setTextColor(Color.parseColor("#222222"));
				filter_price.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				x = 1;
				sortway = "3";
				initData();
				break;
			case R.id.mComposite:
				isloadShaixuan = false;
				mtop_czg.setImageResource(R.mipmap.gaodi_01);
				compositerank.setTextColor(Color.parseColor("#f23030"));
				sellrank.setTextColor(Color.parseColor("#222222"));
				filter_price.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				x = 1;
				sortway = "0";
				initData();
				break;
			case R.id.mpriceczg:
				filter_price_czg.setTextColor(Color.parseColor("#f23030"));
				paixu_czg_text.setTextColor(Color.parseColor("#222222"));
				discount_czg_text.setTextColor(Color.parseColor("#222222"));
				filter_czg.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				x = 1;
				if (isprice) {
					sortway = "1";
					mtop_czg.setImageResource(R.mipmap.gaodi_02);
					isprice = false;
				}else{
					sortway = "2";
					mtop_czg.setImageResource(R.mipmap.gaodi_03);
					isprice = true;
				}
				initDataCzg();
				break;
			case R.id.mfilter_czg:
				mtop_czg.setImageResource(R.mipmap.gaodi_01);
				filter_price_czg.setTextColor(Color.parseColor("#222222"));
				paixu_czg_text.setTextColor(Color.parseColor("#222222"));
				discount_czg_text.setTextColor(Color.parseColor("#222222"));
				filter_czg.setTextColor(Color.parseColor("#f23030"));
				currentPageIndex=1;
				sortway = "4";
				x = 1;
				initDataCzg();
				break;
			case R.id.mComposite_czg:
				mtop.setImageResource(R.mipmap.gaodi_01);
				filter_price_czg.setTextColor(Color.parseColor("#222222"));
				paixu_czg_text.setTextColor(Color.parseColor("#f23030"));
				discount_czg_text.setTextColor(Color.parseColor("#222222"));
				filter_czg.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				sortway = "0";
				x = 1;
				initDataCzg();
				break;
			case R.id.discount_czg:
				mtop.setImageResource(R.mipmap.gaodi_01);
				filter_price_czg.setTextColor(Color.parseColor("#222222"));
				paixu_czg_text.setTextColor(Color.parseColor("#222222"));
				discount_czg_text.setTextColor(Color.parseColor("#f23030"));
				filter_czg.setTextColor(Color.parseColor("#222222"));
				currentPageIndex=1;
				sortway = "3";
				x = 1;
				initDataCzg();
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
		rankRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
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
		Intent intent = new Intent(SearchMainActivity.this, RankActivity.class);
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
						toast = Toast.makeText(SearchMainActivity.this, "最多只能选5个", Toast.LENGTH_SHORT);
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
						toast = Toast.makeText(SearchMainActivity.this, "最多只能选5个", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		return view;
	}
	private void loadFilterBrand(JSONObject json) {
		try {
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
			adapter = new SecondAdapter5(SearchMainActivity.this, data);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (position==8) {
						Intent intent = new Intent(SearchMainActivity.this, ReslutBrandActivity.class);
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
		ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
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
					x= 1;
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
							if (searchResultBeans.get(requestnum).getPurl() != null) {
								String str;
								String content;
								params.put("domain", searchResultBeans.get(requestnum).getDomain());
								params.put("rowkey", searchResultBeans.get(requestnum).getGroupRowkey());
								params.put("fromwhere", "android"+keyword);
								if (searchResultBeans.get(requestnum).getPurl().contains("||")) {
									String url = searchResultBeans.get(requestnum).getPurl();
									String[] split = url.split("\\|\\|");
									String referrer=split[1];
									content = HttpUtil.getHttp1(params, split[0], SearchMainActivity.this, referrer);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", SearchMainActivity.this);
								}else{
									content = HttpUtil.getHttp1(params, searchResultBeans.get(requestnum).getPurl(), SearchMainActivity.this,null);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", SearchMainActivity.this);
								}
								JSONObject object = new JSONObject(str);
								if ("3".equals(object.optString("type"))) {
									if ("".equals(object.optString("url"))) {
										content = HttpUtil.getHttp1(params, searchResultBeans.get(requestnum).getUrl(), SearchMainActivity.this,null);
									}else{
										content = HttpUtil.getHttp1(params, object.optString("url"), SearchMainActivity.this,null);
									}
									params.put("pcontent", content);
									String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct";
									str = HttpUtil.getHttp(params, url, SearchMainActivity.this);
								}
								Message mes = handler.obtainMessage();
								mes.obj = str;
								mes.arg1 = requestnum;
								mes.what =0;
								handler.sendMessage(mes);
							}
							if (requestnum+1 >= searchResultBeans.size()) {
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
								searchResultBeans.remove(i-removenum);
								listAdapter.notifyDataSetChanged();
								gridviewadapter.notifyDataSetChanged();
								removenum++;
								if (currentPageIndex == 1 && i%12 == 0 && removenum >6) {
									if (canLoadMore) {
										currentPageIndex++;
										x = 2;
										initData();
									}
								}
								break;
							case "1":
								String price = object.optString("price");
								String saleinfo = object.optString("saleinfo");
								if (searchResultBeans.size()!= 0){
									searchResultBeans.get(i-removenum).setPrice(price);
									searchResultBeans.get(i-removenum).setSaleinfo(saleinfo);
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
					listAdapter.notifyData(searchResultBeans);
					gridviewadapter.notifyData(searchResultBeans);
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
					String content = HttpUtil.getHttp1(params, tmallSearchUrl, SearchMainActivity.this, null);
					String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/parseTmall";
					params.put("content", content);
					String str = HttpUtil.getHttp(params, url, SearchMainActivity.this);
					searchResultBeans = JSON.parseArray(str,SearchResultBean.class);
					handlerMessage.sendEmptyMessageDelayed(3,0);
				} catch (Exception e) {
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
				break;
				case 2:
					break;
				case 4:
					data.clear();
					JSONObject object = new JSONObject(content);
					info = object;
					loadFilterBrand(info);
					loadFilterCheckView(info);
					break;
				case 5:
					break;
				default:
					break;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void showSearchRecommendView() {
		addView(SEARCH_MAIN_RECOMMEND, SearchRecommendActivity.class);
	}
	//超值购搜索历史显示
	private void showSearchRecommendCzgView() {
		addView(SEARCH_MAIN_RECOMMEND, SearchRecommendCzgActivity.class);
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
		setContentView(R.layout.view_null);
		System.gc();
		isrun = false;
		handler.removeCallbacks(thread);
		handler.removeCallbacks(threadtmall);
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	/**
	 * 搜索关键词加载
	 * @param type
	 */
	private void loadhotKeyword(final String type) {
		String hotKeyword = SharedPreferencesUtil.getSharedData(getApplicationContext(), "hotKeyword", "hotKeyword");
		if (hotKeyword == null || "".equals(hotKeyword)) {
			hotKeyword = "[{\"name\":\"iphone se\",\"productType\":\"\"},{\"name\":\"衬衫\",\"productType\":\"240201\"},{\"name\":\"电炖锅\",\"productType\":\"030313\"},{\"name\":\"情侣睡衣\",\"productType\":\"240313\"},{\"name\":\"空调\",\"productType\":\"030102\"},{\"name\":\"风扇\",\"productType\":\"030201\"},{\"name\":\"T恤\",\"productType\":\"160209\"},{\"name\":\"奶粉\",\"productType\":\"1701\"}]";
			SharedPreferencesUtil.putSharedData(getApplicationContext(),
					"hotKeyword", "hotKeyword", hotKeyword);
		} else {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("type",type);
			RetrofitClient.getInstance(this).createBaseApi().getSearchHotWord(
					maps, new BaseObserver<String>(this) {
						@Override
						public void onNext(String s) {
							try {
								JSONObject jsonObject = new JSONObject(s);
								if (jsonObject.optString("status").equals("1")) {
									String content = jsonObject.optString("content");
									if (content!= null && !"".equals(content)) {
										try {
											if (type.equals("1")){
												SharedPreferencesUtil.putSharedData(
														getApplicationContext(), "hotKeyword",
														"hotKeyword", content);
												showSearchRecommendView();
											}else {
												SharedPreferencesUtil.putSharedData(
														getApplicationContext(), "hotCzgKeyword",
														"hotCzgKeyword", content);
												showSearchRecommendCzgView();
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						@Override
						protected void hideDialog() {
							DialogSingleUtil.dismiss(0);
							zLoadingView.loadSuccess();
						}

						@Override
						protected void showDialog() {
							DialogSingleUtil.show(SearchMainActivity.this);
						}

						@Override
						public void onError(ExceptionHandle.ResponeThrowable e) {
							DialogSingleUtil.dismiss(0);
							zLoadingView.setVisibility(View.VISIBLE);
							zLoadingView.loadError();
							StringUtil.showToast(SearchMainActivity.this, "网络异常");
						}
			});
		}
	}



    /***
     * 广播接收keyword
     */
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        IntentFilter myIntentFilterCzg = new IntentFilter();
        myIntentFilterCzg.addAction(SearchRecommendCzgActivity.ACTION_NAME);
        myIntentFilter.addAction(SearchRecommendActivity.ACTION_NAME);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
        registerReceiver(mBroadcastReceiver, myIntentFilterCzg);
    }
    // 广播接收
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            keyword = intent.getStringExtra("keyword");
            if (Flag.equals("1")){
                initData();
            }else {
                initDataCzg();
            }
        }
    };


	/**
	 * 搜索数据
	 */
	Handler handlerMessage = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1:
					try {
						requestnum = 0;
						removenum = 0;
						data.clear();
						topbar_search_input.setText(keyword);
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
											x=1;
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
						String gridtype = info.optString("gridtype");
						if (!tmp1.isEmpty()) {
//							initListViewData(info);//放数据
							if ( x == 1){
								searchResultBeans = JSON.parseArray(tmp1,SearchResultBean.class);
								listAdapter = new ResultMyListAdapter(searchResultBeans, SearchMainActivity.this);
								result_list.setAdapter(listAdapter);
								gridviewadapter = new ResultMyGridAdapter(searchResultBeans, SearchMainActivity.this);
								mgridView_main.setAdapter(gridviewadapter);
								if (isloadShaixuan == true) {
									mshaixuanbox.setVisibility(View.VISIBLE);
									loadFilterBrand(info);
									loadFilterCheckView(info);
									loadThirdSort(info);
								}
							}else {
								searchResultBeans = JSON.parseArray(tmp1,SearchResultBean.class);
								listAdapter.notifyData(searchResultBeans);
								gridviewadapter.notifyData(searchResultBeans);
							}
							isrequest = true;
							int totalCount = info.optInt("totalCount");
							if (totalCount%12 == 0) {
								pagenum = totalCount/12;
							}else{
								pagenum = (int)(totalCount/12)+1;
							}
							if (pagenum <=currentPageIndex) {
								canLoadMore = false;
								inittmallmore(tmallSearchUrl);//当数据小于12条时加载天猫数据
							} else {
								canLoadMore = true;
							}
							if (thread == null) {
								NowPrice();
							}
						}else{
							tipsLayout.setVisibility(View.VISIBLE);
							tipsKeys.setText("当前筛选条件下无搜索结果");
						}
						if (gridtype.equals("1")){
							//显示块状
							xrefresh.setVisibility(View.GONE);
							xrefresh1.setVisibility(View.VISIBLE);
							xrefresh2.setVisibility(View.GONE);
						}else {
							//显示列表
							xrefresh.setVisibility(View.VISIBLE);
							xrefresh1.setVisibility(View.GONE);
							xrefresh2.setVisibility(View.GONE);
						}
						mshaixuanCzg.setVisibility(View.GONE);
						mshaixuanbox.setVisibility(View.VISIBLE);
						msuccessLayout.setVisibility(View.VISIBLE);
						mlistView.setVisibility(View.GONE);
					}catch (Exception e){
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						JSONObject jo = new JSONObject(contentCzg);
						String isBlandCzg = jo.optString("isBland");
						//  isBland为1 表示有数据 isBland为-1表示无数据
						if (isBlandCzg.equals("1")){
							NewConstants.Flag = "2";
							JSONObject info = jo.getJSONObject("info");
							String tmpCzg = info.optString("page");
							xrefresh2.setVisibility(View.VISIBLE);
							mshaixuanCzg.setVisibility(View.VISIBLE);
							mshaixuanbox.setVisibility(View.GONE);
							xrefresh.setVisibility(View.GONE);
							xrefresh1.setVisibility(View.GONE);
							tipsLayout.setVisibility(View.GONE);
							msuccessLayout.setVisibility(View.VISIBLE);
							if (x == 1) {
								newHomeCzgBeans = JSON.parseArray(tmpCzg,NewHomeCzgBean.class);
								newCzgAdapter = new NewCzgAdapter(SearchMainActivity.this,newHomeCzgBeans);
								mCzgListview.setAdapter(newCzgAdapter);
							} else if (x == 2) {
								if (tmpCzg != null && !tmpCzg.toString().equals("[]")){
									newHomeCzgBeans = JSON.parseArray(tmpCzg,NewHomeCzgBean.class);
									newCzgAdapter.notifyData(newHomeCzgBeans);
								}else {
									StringUtil.showToast(SearchMainActivity.this,"没有更多了");
								}
							}
						}else if(isBlandCzg.equals("-1") && x ==2 && NewConstants.Flag.equals("2")){
							StringUtil.showToast(SearchMainActivity.this,"没有更多了");
						}else {
							NewConstants.Flag = "1";
							xrefresh2.finishLoadMore();
							xrefresh2.finishRefresh();
							xrefresh2.setVisibility(View.GONE);
							xrefresh.setVisibility(View.GONE);
							xrefresh1.setVisibility(View.GONE);
							msuccessLayout.setVisibility(View.VISIBLE);
							mshaixuanCzg.setVisibility(View.VISIBLE);
							mshaixuanbox.setVisibility(View.GONE);
							tipsLayout.setVisibility(View.VISIBLE);
							tipsKeys.setText("当前筛选条件下无搜索结果");
						}
					}catch (Exception e){
						e.printStackTrace();
					}
					break;
				case 3:
					listAdapter.notifyData(searchResultBeans);
					gridviewadapter.notifyData(searchResultBeans);
					break;
			}
		}
	};
	@Override
	public void doRequestData() {
		zLoadingView.setVisibility(View.GONE);
		if (getIntent().getStringExtra("keyword") != null){
			keyword = getIntent().getStringExtra("keyword");
			if (Flag.equals("1")){
				isloadShaixuan = true;
				x = 1;
				currentPageIndex = 1;
				initData();
			}else {
				x = 1;
				currentPageIndex = 1;
				initDataCzg();
			}
		}else {
			loadhotKeyword(type);
			dao = new SearchHistoryDao(this);
		}
	}

	/**
	 *商品比价数据请求
	 */
	private void initData() {
		isrequest = false;
		initRankView();
		Map<String, String> paramsMap = params();
		RetrofitClient.getInstance(this).createBaseApi().getPageList(
				paramsMap, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								content = jsonObject.optString("content");
								handlerMessage.sendEmptyMessageDelayed(1, 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						DialogSingleUtil.dismiss(0);
						result_list.setVisibility(View.VISIBLE);
						mgridView_main.setVisibility(View.VISIBLE);
						zLoadingView.loadSuccess();
						xrefresh.finishLoadMore();
						xrefresh1.finishLoadMore();
						xrefresh.finishRefresh();
						xrefresh1.finishRefresh();
					}

					@Override
					protected void showDialog() {
						DialogSingleUtil.show(SearchMainActivity.this);
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogSingleUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						result_list.setVisibility(View.GONE);
						mgridView_main.setVisibility(View.GONE);
						xrefresh.finishLoadMore();
						xrefresh1.finishLoadMore();
						xrefresh.finishRefresh();
						xrefresh1.finishRefresh();
						StringUtil.showToast(SearchMainActivity.this, "网络异常");
					}
				});
	}

	/**
	 *超值购数据请求
	 */
	private void initDataCzg() {
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("keyword", keyword);
		paramsMap.put("sortWay", sortway);
		paramsMap.put("page",currentPageIndex+"");
		paramsMap.put("client", "android");
		RetrofitClient.getInstance(this).createBaseApi().getPageListChaozhigou(
				paramsMap, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							mCzgListview.setVisibility(View.VISIBLE);
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								contentCzg = jsonObject.optString("content");
								handlerMessage.sendEmptyMessageDelayed(2, 0);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						DialogSingleUtil.dismiss(0);
						zLoadingView.loadSuccess();
						xrefresh2.finishLoadMore();
						xrefresh2.finishRefresh();
					}

					@Override
					protected void showDialog() {
						DialogSingleUtil.show(SearchMainActivity.this);
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogSingleUtil.dismiss(0);
						zLoadingView.setVisibility(View.VISIBLE);
						zLoadingView.loadError();
						mCzgListview.setVisibility(View.GONE);
						xrefresh2.finishLoadMore();
						xrefresh2.finishRefresh();
						StringUtil.showToast(SearchMainActivity.this, "网络异常");
					}
				});
	}

	//获取搜索词
	private void getHttpData() {
		String text = searchText.getText().toString();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("key", text);
		RetrofitClient.getInstance(this).createBaseApi().getAutoApp(
				params, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							if (jsonObject.optString("status").equals("1")) {
								try {
									JSONObject data = new JSONObject(s);
									if (data.optString("status").equals("1")){
										JSONArray array = data.getJSONArray("content");
										for (int i = 0; i < array.length(); i++) {
											JSONObject object = array.getJSONObject(i);
											String lable = object.getString("label");
											dataList.add(lable);
										}
										if (mlistView.getAdapter() != null){
											adapter1.notifyDataSetChanged();
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
					}
					@Override
					protected void showDialog() {
					}
					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						StringUtil.showToast(SearchMainActivity.this, "网络异常");
					}
				});
	}

	/**
	 * 搜索数据
	 * @return
	 */
	private Map<String, String> params(){
		try {
			exadapter = new ResultExpandableListViewAdapter(SearchMainActivity.this,parentlist,childlist);
			mexpandableListView.setAdapter(exadapter);
			mexpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
		}catch (Exception e){
			e.printStackTrace();
		}
		String filter = getFilterString();
		Map<String, String> paramsMap = new HashMap<String, String>();
//		paramsMap.put("stype", String.valueOf(SearchFragment.stypeWay));
		if (keyword != null){
			paramsMap.put("keyword", keyword);
		}
		if (addition != null){
			paramsMap.put("productType", addition);
		}
		if (brand != null){
			paramsMap.put("brand", brand);
		}
		if (bPrice != null) {
			paramsMap.put("bprice", bPrice);
		}
		if (ePrice != null) {
			paramsMap.put("eprice", ePrice);
		}
		if (filter != null) {
			paramsMap.put("filter", filter);
		}
		if (sortway != null) {
			paramsMap.put("sortWay", sortway);
		}
		if (domains != null) {
			paramsMap.put("domains", domains);
		}
		initstype();
		if (stype != null) {
			paramsMap.put("stype", stype);
		}
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
}
