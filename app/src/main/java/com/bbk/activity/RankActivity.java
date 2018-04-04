package com.bbk.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.AdapterView.OnItemClickListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bbk.adapter.CategoryAdapter;

import com.bbk.adapter.RankActivityListViewAdapter;
import com.bbk.adapter.RankAdapter;
import com.bbk.adapter.RankSubCategoryAdapter;
import com.bbk.evaluator.BGAlphaEvaluator;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.RankreplceFragment;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.WordWrapView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RankActivity extends BaseFragmentActivity implements OnClickListener, ResultEvent {
	private int screenWidth = 0;
	private DataFlow dataFlow;

	public final static int CATEGORY_LOAD_OVER = 1;
	public final static int PRODUCT_LOAD_OVER = 2;
	public final static int SEARCH_CATEGORY = 3;
	private FrameLayout frameContainer;
	private View resultView;
	private TextView text1,text2,text3,text4;
	private ImageView img1,img2,img3,img4;
	private String addtion = "";

	private ImageButton goBackBtn;
	private TextView titleTv, resultTv;
	private RelativeLayout result_all, cancelBt;
	private LinearLayout mtop_category_layout;
	private HorizontalScrollView topCategoryScroll;
	private TextView unfoldHintTv;
	private LinearLayout topCategoryLayout;
	private int currentTopIndex;
	private LinearLayout unfoldCategoryTopLayout;
	private ImageView unfoldImageView;
	private PopupWindow topCatagoryWindow;
	private WordWrapView wordWrapView;
	private EditText searchEt;
	private ListView mlistview_rank;
	private List<Map<String, Object>> datalist,datalist1;
	
	private CategoryAdapter categoryAdapter;
	private LinearLayoutManager categoryLinearLayoutManager;
	private ArrayList<HashMap<String, Object>> categoryItemList = new ArrayList<HashMap<String, Object>>();
	private int currentPosition = 0;
	private JSONArray categoryJsonArr;


	private TextView subCategoryTitle;
	private GridView subCategoryGridView;
	private ArrayList<String> list;
	private ArrayList<String> listaddtion;
	private ArrayList<HashMap<String, Object>> subItemList = new ArrayList<HashMap<String, Object>>();
	private RankSubCategoryAdapter subAdapter;

	private RecyclerView rankDatasRecyclerView;
	private RankAdapter rankDatasAdapter;
	private LinearLayoutManager productLinearLayoutManager;
	private ArrayList<HashMap<String, Object>> productItemList = new ArrayList<HashMap<String, Object>>();
	private ViewGroup datasTipsLayout;
	private TextView datasCategoryKey;

	private String mType = "1";
	private String mPage = "1";
	private String mAddtion = "";
	

	// 排行榜当前位置
	private int currentTypeIndex = -1;
	private Typeface face;
	private int isfirst = 1;

	/**
	 * 排行榜切换对象
	 */
	private RelativeLayout rankRelativeLayout;
	private LinearLayout rankSwichLayout;
	private FrameLayout rankSwichShow;
	private LinearLayout rankSwitchDiv;
	private int rankSwitchDivHeight;
	private LinearLayout rankMoreLayout;
	private boolean isSwichRankShow;
	private String name1;
	private RankActivityListViewAdapter adapter;

	/**
	 * 排行榜列表最多显示多少行
	 */
	public static int productLines = 2;

	// 保存搜索反馈View,以便再次搜索时先以移除之前view
	private View searchResultView = null;
	private static final int SEARCH_HASRESULT = 0;
	private static final int SEARCH_NORESULT = 1;
	private String currentKeyword = "";
	private int selectIndex;
	private String rankIndex;
	private int position2;
	private FrameLayout replaceID;
	private FragmentManager manager;
	private Fragment fragment;
	private String keyword;
	private boolean isrequest = false;
	private int requestnum = 0;
	private int removenum = 0;
	private Thread thread;
	private boolean isrun = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rank);
		screenWidth = BaseTools.getWindowsWidth(this);
		face = getApp().getFontFace();
		dataFlow = new DataFlow(this);
		if (getIntent().getStringExtra("keyword")!= null) {
			keyword = getIntent().getStringExtra("keyword");
			searchRank(keyword);
			list = new ArrayList<>();
			listaddtion = new ArrayList<>();
		}else{
		list = getIntent().getStringArrayListExtra("listname");
		String position1 = getIntent().getStringExtra("position");
		position2 = Integer.parseInt(position1);
		selectIndex = position2;
		addtion = getIntent().getStringArrayListExtra("listaddtion").get(position2);
		listaddtion = getIntent().getStringArrayListExtra("listaddtion");
		name1 = list.get(position2);
		}
		initView();
		initData();
	}

	/**
	 * 初始化页面数据
	 */
	public void initData() {
		initTopCatagoryPopupWindow();		
		initTopCategory();
		isrequest = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("addtion", addtion);
		params.put("type", rankIndex);
		dataFlow.requestData(1, "newApp/getSortingPage", params, this);
		
	}

	public void initView() {
		datalist = new ArrayList<>();
		datalist1 = new ArrayList<>();
		goBackBtn = $(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		titleTv = $(R.id.topbar_title_tv);
//		initfourrank();
		unfoldHintTv = $(R.id.unfold_hint);
		topCategoryScroll = $(R.id.top_category_scroll);
		topCategoryLayout = $(R.id.top_category);
		unfoldCategoryTopLayout = $(R.id.unfold_category_top);
		unfoldImageView = $(R.id.unfold_iv);
		searchEt = (EditText) findViewById(R.id.topbar_search_et);
		mlistview_rank = $(R.id.mlistview_rank);

		// 初始化排行榜切换对象
		rankRelativeLayout = $(R.id.rank_relative_layout);
		rankSwichLayout = $(R.id.rank_switch_layout);
		rankSwichShow = $(R.id.rank_swich_show);
		rankSwitchDiv = $(R.id.rank_switch_div);
		rankMoreLayout = $(R.id.rank_more_layout);
		replaceID = $(R.id.replaceID);
		searchEt.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH){ 
					keyword = searchEt.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);    
			        imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0) ;  
		            searchRank(keyword);
		            
		            return true;  
		        }  
				return false;
			}
		});
		searchEt.addTextChangedListener(new TextWatcher() {
			
			@Override 
			public void beforeTextChanged(CharSequence s, int start, int count, 
			int after) { 
			// TODO Auto-generated method stub 
			} 
			@Override 
			public void onTextChanged(CharSequence s, int start, int before, int count) { 
			// TODO Auto-generated method stub 
			} 
			@Override 
			public void afterTextChanged(Editable s) { 
			// TODO Auto-generated method stub 
			int lines = searchEt.getLineCount(); 
			// 限制最大输入行数 
			if (lines > 1) { 
			String str = s.toString(); 
			int cursorStart = searchEt.getSelectionStart(); 
			int cursorEnd = searchEt.getSelectionEnd(); 
			if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) { 
			str = str.substring(0, cursorStart-1) + str.substring(cursorStart); 
			} else { 
			str = str.substring(0, s.length()-1); 
			} 
			// setText会触发afterTextChanged的递归 
			searchEt.setText(str);	
			// setSelection用的索引不能使用str.length()否则会越界 
			searchEt.setSelection(searchEt.getText().length()); 
			} 
			}
		});
		rankIndex = getIntent().getStringExtra("rankIndex");
		if (rankIndex.equals("3")) {
			mlistview_rank.setVisibility(View.GONE);
			replaceID.setVisibility(View.VISIBLE);
		}
		int index = Integer.parseInt(rankIndex);
		switchRankTitle(index);
		initRankView();

	}

	private void initfourrank() {
		text1 = $(R.id.text1);
		text2 = $(R.id.text2);
		text3 = $(R.id.text3);
		text4 = $(R.id.text4);
		img1 = $(R.id.img1);
		img2 = $(R.id.img2);
		img3 = $(R.id.img3);
		img4 = $(R.id.img4);
		String strname = SharedPreferencesUtil.getSharedData(this, "userInfor", "strname");
		String strimg = SharedPreferencesUtil.getSharedData(this, "userInfor", "strimg");
		if (strname!= null && strimg!=null) {
			String[] name = strname.split("\\|");
			String[] img = strimg.split("\\|");
			text1.setText(name[0]);
			text2.setText(name[1]);
			text3.setText(name[2]);
			text4.setText(name[3]);
			Glide.with(this)
			.load(img[0])
			.skipMemoryCache(true)
			.diskCacheStrategy(DiskCacheStrategy.RESULT)
			.dontAnimate()
			.into(img1);
			Glide.with(this)
			.load(img[1])
			.skipMemoryCache(true)
			.diskCacheStrategy(DiskCacheStrategy.RESULT)
			.dontAnimate()
			.into(img2);
			Glide.with(this)
			.load(img[2])
			.skipMemoryCache(true)
			.diskCacheStrategy(DiskCacheStrategy.RESULT)
			.dontAnimate()
			.into(img3);
			Glide.with(this)
			.load(img[3])
			.skipMemoryCache(true)
			.diskCacheStrategy(DiskCacheStrategy.RESULT)
			.dontAnimate()
			.into(img4);
		}
		
		
	}

	/**
	 * 提示可左右滑动
	 */
	private void showTips() {
		final ViewGroup tipsLayout = $(R.id.tips_layout);
		ImageView tipsIV = $(R.id.tips_iv);
		Glide.with(this).load(R.mipmap.activity_rank_tips).asGif()
				.override(DensityUtil.dip2px(this, 128), DensityUtil.dip2px(this, 122)).priority(Priority.IMMEDIATE)
				.diskCacheStrategy(DiskCacheStrategy.SOURCE).into(tipsIV);
		tipsLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				view.setVisibility(View.GONE);
			}
		});
		tipsLayout.setVisibility(View.VISIBLE);
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
				@Override
				public void onClick(View view) {
					closeRrank();
					if (keyword== null) {
						switch (index) {
						case 0:
							rankIndex = "5";
							switchRankTitle(Integer.valueOf(rankIndex));
							addtion = listaddtion.get(selectIndex);
							HttpGetData2(addtion);
							break;
						case 1:
							rankIndex = "6";
							switchRankTitle(Integer.valueOf(rankIndex));
							addtion = listaddtion.get(selectIndex);
							HttpGetData2(addtion);
							break;
						case 2:
							rankIndex = "2";
							switchRankTitle(Integer.valueOf(rankIndex));
							addtion = listaddtion.get(selectIndex);
							HttpGetData2(addtion);
							break;
						case 3:
							rankIndex = "3";
							switchRankTitle(Integer.valueOf(rankIndex));
							addtion = listaddtion.get(selectIndex);
							HttpGetData2(addtion);
							break;

						default:
							break;
						}
					}else{
						switch (index) {
						case 0:
							rankIndex = "5";
							switchRankTitle(Integer.valueOf(rankIndex));
							searchRank(keyword);
							
							break;
						case 1:
							rankIndex = "6";
							switchRankTitle(Integer.valueOf(rankIndex));
							searchRank(keyword);
							
							break;
						case 2:
							rankIndex = "2";
							switchRankTitle(Integer.valueOf(rankIndex));
							searchRank(keyword);
							
							break;
						case 3:
							rankIndex = "3";
							switchRankTitle(Integer.valueOf(rankIndex));
							searchRank(keyword);
							break;

						default:
							break;
					}
					}
					
				}
			});
		}
	}

	/**
	 * 切换排行榜标题类型
	 * 
	 * @param index
	 *            排行榜类型位置
	 */
	private void switchRankTitle(int index) {
		int index1 = 0;
		switch (index) {
		case 5:
			mType = "1";
			if (name1!=null) {
				titleTv.setText("最好货-"+name1);
			}else{
				titleTv.setText("最好货-"+keyword);
			}
			index1 = 0;
			searchEt.setHint("查询商品好评榜");
			break;
		case 6:
			mType = "2";
			if (name1!=null) {
				titleTv.setText("超好卖-"+name1);
			}else{
				titleTv.setText("超好卖-"+keyword);
			}
			index1 = 1;
			searchEt.setHint("查询商品销量榜");
			break;
		case 2:
			mType = "3";
			if (name1!=null) {
				titleTv.setText("便宜货-"+name1);
			}else{
				titleTv.setText("便宜货-"+keyword);
			}
			index1 = 2;
			searchEt.setHint("查询商品降价榜");
			break;

		case 3:
			mType = "4";
			if (name1!=null) {
				titleTv.setText("热品牌-"+name1);
			}else{
				titleTv.setText("热品牌-"+keyword);
			}
			index1 = 3;
			searchEt.setHint("查询商品品牌榜");
			break;
		}
		// 切换显示弹出框默认勾选
		if (currentTypeIndex!= -1) {
			ViewGroup group1 = (ViewGroup) rankMoreLayout.getChildAt(currentTypeIndex);
			group1 = (ViewGroup) group1.getChildAt(0);
			View view1 = group1.getChildAt(1);
			view1.setVisibility(View.GONE);
		}
			ViewGroup group = (ViewGroup) rankMoreLayout.getChildAt(index1);
			group = (ViewGroup) group.getChildAt(0);
			View view = group.getChildAt(1);
			view.setVisibility(View.VISIBLE);
			currentTypeIndex = index1;
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
	 * 初始化上边一级分类选项
	 */
	private void initTopCategory() {
		try {
			int length = list.size();
			for (int i = 0; i < length; i++) {
				String name1 = list.get(i);
				addTopCategory(name1, i, selectIndex);
				addWordWrapView(name1, i);
			}
			// 滚动到默认选中
			final View view = topCategoryLayout.getChildAt(selectIndex);
			// view加载完成时回调
			view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressLint("NewApi")
				@Override
				public void onGlobalLayout() {
					topCategoryScroll.scrollTo(view.getLeft(), 0);
					if (Build.VERSION.SDK_INT < 16) {
						view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					} else {
						view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		unfoldCategoryTopLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (topCatagoryWindow != null && topCatagoryWindow.isShowing()) {
					topCatagoryWindow.dismiss();
					foldTopAnimation();
				} else {
					unfoldTopAnimation();
					topCatagoryWindow.showAsDropDown(view, 0, 0);
					// Log.e("=======topCatagoryWindow======",""+
					// topCatagoryWindow.isShowing());
				}
			}
		});
	}

	/**
	 * 添加上边左右滚动一级分类选项按钮
	 * 
	 * @param name
	 *            按钮文字
	 * @param index
	 *            按钮位置
	 */
	private void addTopCategory(String name, final int index, final int selectIndex1) {
		View view = inflateView(R.layout.item_top_category);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(lp);
		final TextView title = (TextView) view.findViewById(R.id.item_title);
		final View bView = view.findViewById(R.id.bottom_view);
		title.setText(name);
		if (index == selectIndex1) {
			title.setTextColor(Color.parseColor("#0098FF"));
			bView.setBackgroundColor(Color.parseColor("#0098FF"));
			currentTopIndex = selectIndex1;
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (index!= selectIndex) {
					selectIndex = index;
					addtion = listaddtion.get(index);
					name1 = list.get(index);
					select2Category(index);
					HttpGetData(addtion);
				}
			}
		});
		topCategoryLayout.addView(view);
	}
	private void select2Category(final int newTopIndex) {
		// 更改上边左右滑动一级的样式
		// 撤销上次选中的样式
		View currentTopView = topCategoryLayout.getChildAt(currentTopIndex);
		TextView cTv = (TextView) currentTopView.findViewById(R.id.item_title);
		cTv.setTextColor(Color.parseColor("#323232"));
		View cBView = currentTopView.findViewById(R.id.bottom_view);
		cBView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		// 更改新的选中样式
		View view = topCategoryLayout.getChildAt(newTopIndex);
		TextView tv = (TextView) view.findViewById(R.id.item_title);
		tv.setTextColor(Color.parseColor("#0098FF"));
		View bView = view.findViewById(R.id.bottom_view);
		bView.setBackgroundColor(Color.parseColor("#0098FF"));
		// 自动滑动到新的view位置
		topCategoryScroll.scrollTo(view.getLeft()-200, 0);


		// 改变展开后的一级分类样式
		// 撤销上次选中的样式
		TextView currentPopulView = (TextView) wordWrapView.getChildAt(currentTopIndex);
		currentPopulView.setTextColor(Color.parseColor("#323232"));
		// 更改新的选中样式
		TextView ptv = (TextView) wordWrapView.getChildAt(newTopIndex);
		ptv.setTextColor(Color.parseColor("#0098FF"));

		// 更新当前选中位置
		currentTopIndex = newTopIndex;

	}
	private void HttpGetData(String addtion){
		isrequest = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("addtion", addtion);
		params.put("type", rankIndex);
		dataFlow.requestData(2, "newApp/getSortingPage", params, this);
	}
	private void HttpGetData2(String addtion){
		isrequest = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("addtion", addtion);
		params.put("type", rankIndex);
		dataFlow.requestData(5, "newApp/getSortingPage", params, this);
	}
	/**
	 * 展开一级分类弹窗效果
	 */
	private void unfoldTopAnimation() {
		topCategoryScroll.setVisibility(View.GONE);
		unfoldHintTv.setVisibility(View.VISIBLE);
		ObjectAnimator animator = ObjectAnimator.ofFloat(unfoldImageView, "rotation", 0f, 180f);
		animator.setDuration(500);
		animator.start();
	}

	/**
	 * 收起一级分类弹窗效果
	 */
	private void foldTopAnimation() {
		topCategoryScroll.setVisibility(View.VISIBLE);
		unfoldHintTv.setVisibility(View.GONE);
		ObjectAnimator animator = ObjectAnimator.ofFloat(unfoldImageView, "rotation", 180f, 0f);
		animator.setDuration(500);
		animator.start();
	}

	/**
	 * 初始化上边展开后所有一级分类弹出界面
	 */
	private void initTopCatagoryPopupWindow() {
		View topView = inflateView(R.layout.popup_window_top_category);
		topCatagoryWindow = new PopupWindow(topView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		topCatagoryWindow.setFocusable(true);
		topCatagoryWindow.setTouchable(true);
		topCatagoryWindow.setOutsideTouchable(true);
		topCatagoryWindow.setBackgroundDrawable(new BitmapDrawable());

		RelativeLayout dismissLayout = (RelativeLayout) topView.findViewById(R.id.dismiss_layout);
		dismissLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topCatagoryWindow.dismiss();
			}
		});
		topCatagoryWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				foldTopAnimation();
			}
		});
		wordWrapView = (WordWrapView) topView.findViewById(R.id.view_wordwrap);
	}

	/**
	 * 添加上边展开后所有一级分类选项按钮
	 * 
	 * @param name
	 *            按钮文字
	 * @param index
	 *            按钮位置
	 */
	private void addWordWrapView(String name, final int index) {
		TextView textView = new TextView(this);
		textView.setTextSize(15);
		textView.setTypeface(face);
		textView.setText(name);
		if (index == 0) {
			textView.setTextColor(Color.parseColor("#0098FF"));
		} else {
			textView.setTextColor(Color.parseColor("#323232"));
		}
		wordWrapView.addView(textView);
		textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (index!= selectIndex) {
					addtion = listaddtion.get(index);
					name1 = list.get(index);
					select2Category(index);
					topCatagoryWindow.dismiss();
					foldTopAnimation();
					HttpGetData(addtion);
				}
			}
		});
	}

	/**
	 * 根据关键词全网搜索
	 *
	 */
	private void searchRank(String keyword) {
		currentKeyword = keyword;
		// 获取三级菜单数据源并更新三级菜单
		searchAndUpdateCategoryData(keyword, currentTypeIndex + "");

	}



	/**
	 * 添加搜索反馈布局
	 * 
	 * @param keyword
	 */

	private void addSearchResultFrame(String keyword, int searchType) {
		// 撤销上次选中的样式
		if (isfirst!=1) {
			
			View currentTopView = topCategoryLayout.getChildAt(currentTopIndex);
			TextView cTv = (TextView) currentTopView.findViewById(R.id.item_title);
			cTv.setTextColor(Color.parseColor("#323232"));
			View cBView = currentTopView.findViewById(R.id.bottom_view);
			cBView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
		isfirst = 2;
		frameContainer = (FrameLayout) $(R.id.frame_container1);
		// 删除之前搜索view
		if (searchResultView != null) {
			frameContainer.removeView(searchResultView);
		}
		resultView = inflateView(R.layout.search_back);
		int widthPx = BaseTools.getPixelsFromDp(this, 41);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, widthPx);
		resultView.setLayoutParams(lp);
		resultView.setBackgroundColor(0xFFF2F2F2);
		result_all=(RelativeLayout) resultView.findViewById(R.id.result_all);
		resultTv = (TextView) resultView.findViewById(R.id.resultTv);
		cancelBt = (RelativeLayout) resultView.findViewById(R.id.cancelIb);
		String info;
		if (keyword.length() > 5) {
			keyword = keyword.substring(0, 5) + "...";
		}
		if (searchType == SEARCH_HASRESULT) {
			info = "匹配 " + keyword + " 的全网排行榜";
		} else {
			info = "抱歉,没有匹配到 " + keyword + " 的全网排行榜";
		}
		SpannableStringBuilder builder = new SpannableStringBuilder(info);
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
		builder.setSpan(blueSpan, info.lastIndexOf("的全网排行") - keyword.length() - 1, info.lastIndexOf("的全网排行"),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		resultTv.setText(builder);

		frameContainer.addView(resultView);
		frameContainer.setVisibility(View.VISIBLE);
		searchResultView = resultView;
		result_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
			}
		});
		// 点击X图片移除布局
		cancelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				frameContainer.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * 根据底部排行榜当前分类，以及搜索关键字从服务器获取数据
	 * 
	 * @param keyword
	 * @param type
	 * @return
	 */
	private void searchAndUpdateCategoryData(String keyword, String type) {
		selectIndex = 0;
		if (topCategoryLayout!= null) {
			topCategoryLayout.removeAllViews();
		}
		if (wordWrapView!= null) {
			wordWrapView.removeAllViews();
		}
		isrequest = false;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("keyword", keyword);
		params.put("type", type);
		dataFlow.requestData(3, "apiService/seachCategory", params, this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果当榜单选项页面展开点击返回键就关闭榜单选项
		if (keyCode == KeyEvent.KEYCODE_BACK && isSwichRankShow) {
			closeRrank();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	private void addmap(JSONArray ja) throws JSONException{
		requestnum = 0;
		removenum = 0;
		Map<String, Object> map=null;
		for (int i = 0; i < ja.length(); i++) {
			JSONObject object = ja.getJSONObject(i);
			String title = object.optString("title");
			String imgUrl = object.optString("imgUrl");
			String hnumber = object.optString("hnumber");
			String oldprice = object.optString("oldprice");
			if (object.has("purl")) {
				String purl = object.optString("purl");
			}else{
				String purl = "0";
			}
			String purl = object.optString("purl");
			String url = object.optString("url");
			String rowkey = object.optString("rowkey");
			String domain = object.optString("alldomain");
			int domaincount = object.optInt("domaincount");
			int quote = object.optInt("quote");
			String price = object.optString("price");
			map = new HashMap<>();
			map.put("title", title);
			map.put("imgUrl",imgUrl );
			map.put("hnumber", hnumber);
			map.put("domaincount",domaincount );
			map.put("quote",quote );
			map.put("price", price);
			map.put("oldprice", oldprice);
			map.put("purl", purl);
			map.put("url", url);
			map.put("rowkey", rowkey);
			map.put("domain", domain);
			datalist.add(map);
			datalist1.add(map);
		}
	}
	private void replacefragment(String content){
		mlistview_rank.setVisibility(View.GONE);
		replaceID.setVisibility(View.VISIBLE);
        //1.获取到一个fragment管理器对象
        manager = getSupportFragmentManager();
        //2.通过一个管理器对象开启一个事务
       FragmentTransaction transaction = manager.beginTransaction();
        //3.把实际要展示的碎片对象和占位置的布局id进行替换
       	Bundle bundle = new Bundle();
       	bundle.putString("content", content);
       	bundle.putString("addtion", addtion);
       	fragment = new RankreplceFragment();
		fragment.setArguments(bundle);
		transaction.replace(R.id.replaceID, fragment);
        //4.提交事务
        transaction.commit();
	}
    private void NowPrice(){
    	thread = new Thread(new Runnable() {
			public void run() {
				while (isrun) {
					if (isrequest == true) {
						try {
							Map<String, String> params = new HashMap<>();
							if (!"0".equals(datalist1.get(requestnum).get("purl"))) {
								String str;
								String content;
								params.put("domain", datalist1.get(requestnum).get("domain").toString());
								params.put("rowkey", datalist1.get(requestnum).get("rowkey").toString());
								params.put("fromwhere", "android"+keyword);
								if (datalist1.get(requestnum).get("purl").toString().contains("||")) {
									String url = datalist1.get(requestnum).get("purl").toString();
									String[] split = url.split("\\|\\|");
									String referrer=split[1];
									content = HttpUtil.getHttp1(params, split[0], RankActivity.this, referrer);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", RankActivity.this);
								}else{
									content = HttpUtil.getHttp1(params, datalist1.get(requestnum).get("purl").toString(), RankActivity.this,null);
									params.put("pcontent", content);
									str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", RankActivity.this);
								}
								JSONObject object = new JSONObject(str);
								if ("3".equals(object.optString("type"))) {
									if ("".equals(object.optString("url"))) {
										content = HttpUtil.getHttp1(params, datalist1.get(requestnum).get("url").toString(), RankActivity.this,null);
									}else{
										content = HttpUtil.getHttp1(params, object.optString("url"), RankActivity.this,null);
									}
									params.put("pcontent", content);
									String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct";
									str = HttpUtil.getHttp(params, url, RankActivity.this);
								}
								Message mes = handler.obtainMessage();
								mes.obj = str;
								mes.arg1 = requestnum;
								mes.what =0;
								handler.sendMessage(mes);
							}
							if (requestnum+1 >= datalist1.size()) {
								isrequest = false;
							}
							requestnum++;
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		thread.start();
    }
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				String str = msg.obj.toString();
				int i = msg.arg1;
				try {
					JSONObject object = new JSONObject(str);
					switch (object.optString("type")) {
					case "0":
						datalist.remove(i-removenum);
						adapter.notifyDataSetChanged();
						removenum++;
						break;
					case "1":
						String price = object.optString("price");
						datalist.get(i-removenum).put("price", price);
						adapter.notifyDataSetChanged();
						break;
					default:
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		try {
		switch (requestCode) {
		case 1:
			if (rankIndex.equals("3")) {
				replacefragment(content);
			}else{
			mlistview_rank.setVisibility(View.VISIBLE);
			replaceID.setVisibility(View.GONE);
			datalist.clear();
			datalist1.clear();
			final JSONArray ja = new JSONArray(content);
			addmap(ja);
			if (datalist!= null) {
				adapter = new RankActivityListViewAdapter(datalist, this,rankIndex);
				mlistview_rank.setAdapter(adapter);
				mlistview_rank.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						try {
							Intent intent;
							if (JumpIntentUtil.isJump(datalist,arg2,"domain")) {
								intent = new Intent(RankActivity.this,IntentActivity.class);
								intent.putExtra("title", datalist.get(arg2).get("title").toString());
								intent.putExtra("domain", datalist.get(arg2).get("domain").toString());
								intent.putExtra("url", datalist.get(arg2).get("url").toString());
								intent.putExtra("groupRowKey", datalist.get(arg2).get("rowkey").toString());
							}else{
								intent = new Intent(RankActivity.this,WebViewActivity.class);
								intent.putExtra("url", datalist.get(arg2).get("url").toString());
								intent.putExtra("groupRowKey", datalist.get(arg2).get("title").toString());
							}
							startActivity(intent);							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
				isrequest = true;
				if (thread == null) {
					NowPrice();
				}
				}
			}
			break;
		case 2:
			if (rankIndex.equals("3")) {
				replacefragment(content);
			}else{
			mlistview_rank.setVisibility(View.VISIBLE);
			replaceID.setVisibility(View.GONE);
			datalist.clear();
			datalist1.clear();
			final JSONArray ja1 = new JSONArray(content);
			addmap(ja1);
			Map<String, Object> map1=null;
			if (datalist!= null) {
				if (adapter!= null) {
					adapter.notifyDataSetChanged();
				}else{
					adapter = new RankActivityListViewAdapter(datalist, this,rankIndex);
					mlistview_rank.setAdapter(adapter);
					mlistview_rank.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							try {
								Intent intent;
								if (JumpIntentUtil.isJump(datalist,arg2,"domain")) {
									intent = new Intent(RankActivity.this,IntentActivity.class);
									intent.putExtra("title", datalist.get(arg2).get("title").toString());
									intent.putExtra("domain", datalist.get(arg2).get("domain").toString());
									intent.putExtra("url", datalist.get(arg2).get("url").toString());
									intent.putExtra("groupRowKey", datalist.get(arg2).get("rowkey").toString());
								}else{
									intent = new Intent(RankActivity.this,WebViewActivity.class);
									intent.putExtra("url", datalist.get(arg2).get("url").toString());
									intent.putExtra("groupRowKey", datalist.get(arg2).get("title").toString());
								}
								startActivity(intent);	
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
				}
				int index = Integer.parseInt(rankIndex);
				switchRankTitle(index);
				isrequest = true;
				if (thread == null) {
					NowPrice();
				}
			}
			}
			break;
		case 3:
			
			list.clear();
			listaddtion.clear();
			int type;
			if (TextUtils.isEmpty(content) || "{}".equals(content) || "[]".equals(content)) {
				type = SEARCH_NORESULT;
				if (rankIndex.equals("3")) {
					replaceID.setVisibility(View.GONE);
				}else{
					datalist.clear();
					datalist1.clear();
					mlistview_rank.setVisibility(View.GONE);
				}
				String keyword = searchEt.getText().toString();
				addSearchResultFrame(keyword, type);
			} else {
				type = SEARCH_HASRESULT;
				mlistview_rank.setVisibility(View.VISIBLE);
				JSONArray array = new JSONArray(content);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String addtion = object.optString("addtion");
					String name = object.optString("name");
					list.add(name);
					listaddtion.add(addtion);
				}
				name1 = list.get(0);
				initRankView();
				initTopCatagoryPopupWindow();		
				initTopCategory();
				addSearchResultFrame(keyword, type);
				isrequest = false;
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("addtion", listaddtion.get(0));
				params.put("type", rankIndex);
				dataFlow.requestData(4, "newApp/getSortingPage", params, this);
			}
			break;
		case 4:
			if (rankIndex.equals("3")) {
				replacefragment(content);
		        View view = topCategoryLayout.getChildAt(0);
				TextView tv = (TextView) view.findViewById(R.id.item_title);
				tv.setTextColor(Color.parseColor("#0098FF"));
				View bView = view.findViewById(R.id.bottom_view);
				bView.setBackgroundColor(Color.parseColor("#0098FF"));
			}else{
			mlistview_rank.setVisibility(View.VISIBLE);
			replaceID.setVisibility(View.GONE);
			datalist.clear();
			datalist1.clear();
			JSONArray ja = new JSONArray(content);
			addmap(ja);
			if (datalist!= null) {
				adapter.notifyDataSetChanged();
				int index = Integer.parseInt(rankIndex);
				switchRankTitle(index);
				View view = topCategoryLayout.getChildAt(0);
				TextView tv = (TextView) view.findViewById(R.id.item_title);
				tv.setTextColor(Color.parseColor("#0098FF"));
				View bView = view.findViewById(R.id.bottom_view);
				bView.setBackgroundColor(Color.parseColor("#0098FF"));
				isrequest = true;
				if (thread == null) {
					NowPrice();
				}
			}
			}
			break;
		case 5:
			if (rankIndex.equals("3")) {
				replacefragment(content);
			}else{
			mlistview_rank.setVisibility(View.VISIBLE);
			replaceID.setVisibility(View.GONE);
			datalist.clear();
			datalist1.clear();
			final JSONArray ja1 = new JSONArray(content);
			addmap(ja1);
			if (datalist!= null) {
					adapter = new RankActivityListViewAdapter(datalist, this,rankIndex);
					mlistview_rank.setAdapter(adapter);
					mlistview_rank.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							try {
								Intent intent;
								if (JumpIntentUtil.isJump(datalist,arg2,"domain")) {
									intent = new Intent(RankActivity.this,IntentActivity.class);
									intent.putExtra("title", datalist.get(arg2).get("title").toString());
									intent.putExtra("domain", datalist.get(arg2).get("domain").toString());
									intent.putExtra("url", datalist.get(arg2).get("url").toString());
									intent.putExtra("groupRowKey", datalist.get(arg2).get("rowkey").toString());
								}else{
									intent = new Intent(RankActivity.this,WebViewActivity.class);
									intent.putExtra("url", datalist.get(arg2).get("url").toString());
									intent.putExtra("groupRowKey", datalist.get(arg2).get("title").toString());
								}
								startActivity(intent);	
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
					isrequest = true;
					if (thread == null) {
						NowPrice();
					}
				}
				int index = Integer.parseInt(rankIndex);
				switchRankTitle(index);
			}
			break;
	default:
			break;
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 为了更好的兼容，重写onTouchEvent()方法
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (topCatagoryWindow != null && topCatagoryWindow.isShowing()) {
			topCatagoryWindow.dismiss();
			
		}
//		if (result_all.isClickable() && cancelBt.isClickable()) {
//			frameContainer.removeView(resultView);
//		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		isrun = false;
		super.onDestroy();
	}
}
