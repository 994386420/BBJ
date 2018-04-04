package com.bbk.activity;

import android.animation.ObjectAnimator;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.StatService;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.fragment.SearchFragment;
import com.bbk.resource.Constants;
import com.bbk.util.ClipDialogUtil;
import com.bbk.util.DensityUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.ValidatorUtil;

public class SearchMainActivity extends ActivityGroup implements
		OnClickListener, OnKeyListener {

	private FrameLayout mContent;
	private Context context;
	private EditText searchText;

	private ImageButton goBackBtn, searchBtn;
	private SearchHistoryDao dao;

	private static final String SEARCH_MAIN_RECOMMEND = "recommemd";
	private static final String SEARCH_MAIN_PROMPT = "prompt";
	private List<String> dataList;
	private LinearLayout toolbarLayout;

	private String keyword;
	private PopupWindow searchKeywordPopupWindow;
	
	
	

	
	private Typeface typeFace;

	private ListView mlistView;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String dataStr = (String) msg.obj;
				try {
					JSONObject data = new JSONObject(dataStr);
					JSONArray array = data.getJSONArray("content");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String lable = object.getString("label");
						Log.e("=====lable======", lable+"");
						dataList.add(lable);
					}
					if (mlistView.getAdapter()!=null) {
						adapter.notifyDataSetChanged();
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
	};

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main);

		// MyApplication.getInstance().addActivity(this);

		initView();
		initData();
	}

	private void initView() {
		dataList = new ArrayList<String>();
		String str = "1";
		dataList.add(str);
		typeFace = ((MyApplication) getApplication()).getFontFace();
		toolbarLayout = (LinearLayout) findViewById(R.id.toolbar_layout);
		keyword = getIntent().getStringExtra("keyword");
		mContent = (FrameLayout) findViewById(R.id.content);
		mlistView = (ListView)findViewById(R.id.mlistView);
		searchText = (EditText) findViewById(R.id.topbar_search_input);
		mlistView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
				String text = mlistView.getAdapter().getItem(i).toString();
				searchText.setText(text);
				searchText.setSelection(text.length());
				mlistView.setVisibility(View.GONE);
				doSearch();
			}
		});
		/*
		 * RelativeLayout.LayoutParams params =
		 * (android.widget.RelativeLayout.LayoutParams)
		 * searchText.getLayoutParams(); params.width =
		 * BaseTools.getWindowsWidth(this) - DensityUtil.dip2px(this, 120);
		 * searchText.setLayoutParams(params);
		 */

		searchBtn = (ImageButton) findViewById(R.id.topbar_search_btn);
		if (!TextUtils.isEmpty(keyword)) {
			searchText.setText(keyword);
			searchText.setSelection(keyword.length());
		}
		// TextPaint tp = searchBtn .getPaint();
		// tp.setFakeBoldText(true);
		goBackBtn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dataList);
		searchBtn.setOnClickListener(this);
		searchText.addTextChangedListener(watcher);
		searchText.setOnKeyListener(this);
		goBackBtn.setOnClickListener(this);

		showSearchRecommendView();

		mlistView.setAdapter(adapter);
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
			// if (s.length() > 0) {
			// showSearchPromptView();
			// } else {
			// showSearchRecommendView();
			// }
		}
	};

	private void initData() {
		dao = new SearchHistoryDao(this);
		
	}

	

	private void showAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0f,
				180f);
		animator.setDuration(500);
		animator.start();
	}

	private void dismisssAnimation(View view) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation",
				180f, 0f);
		animator.setDuration(500);
		animator.start();
	}

	private View inflateView(int id) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(id, null);
		return view;
	}

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
		String keyword = searchText.getText().toString();
		if (keyword.length() <= 0) {
			Toast toast = Toast.makeText(this, "搜索内容为空", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 50);
			toast.show();
			return;
		}
		if (!dao.exsistHistory(keyword)) {
			dao.addHistory(keyword);
		}
		if (ResultMainActivity.instance != null
				&& !ResultMainActivity.instance.isFinishing()) {
			ResultMainActivity.instance.finish();
		}
		finish();
		Intent intent = new Intent(this, ResultMainActivity.class);
		intent.putExtra("keyword", keyword);
		startActivity(intent);
	}

	public void closeKeybord(EditText mEditText, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			goBack();
			break;
		case R.id.topbar_search_btn:
			StatService.onEvent(SearchMainActivity.this, "search", "搜索产品:搜索页面");
			doSearch();
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			// Runtime runtime = Runtime.getRuntime();
			// try {
			// runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
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

	private void showSearchRecommendView() {
		addView(SEARCH_MAIN_RECOMMEND, SearchRecommendActivity.class);
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
		// startActivity(new Intent(getApplicationContext(),
		// HomeActivity.class));
		// Runtime runtime = Runtime.getRuntime();
		// try {
		// runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void finish() {
		closeKeybord(searchText, SearchMainActivity.this);
		super.finish();
	}

	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	private void getHttpData() {
		final String url = Constants.MAIN_BASE_URL_MOBILE+"searchAutoService/getAutoApp";
		String text = searchText.getText().toString();
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("key", text);
		new Thread(new Runnable() {
					
					@Override
					public void run() {
						String dataStr = HttpUtil.getHttp(params, url, context);
						Message msg = handler.obtainMessage();
						msg.what = 0;
						msg.obj = dataStr;
						handler.sendMessage(msg);
						
					}
				}).start();
		
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		String clipchange = SharedPreferencesUtil.getSharedData(this, "clipchange", "clipchange");
		if (clipchange.equals("1")) {
			ClipDialogUtil.creatDialog(this);
		}
	}
}
