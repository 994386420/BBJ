package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.bbk.adapter.ListViewAdapter;
import com.bbk.adapter.SortRightFragmentListviewAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow2;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;

public class SortActivity extends BaseActivity implements OnItemClickListener,OnClickListener{
	private View rank_head;
	private ListView mlistView,mlistViewRight;
	//分类数组
	private String[] str = {"为你推荐","服饰","鞋靴/箱包","美妆/个护 ","母婴","玩具","手机/数码","电脑/办公","家电/厨具","酒水/食品","钟表/珠宝","户外",
			"家装/建材","保健","汽车"};
	private ListViewAdapter adapter;
	private List<Map<String, String>> listright;
	private SortRightFragmentListviewAdapter adapterright;
	private RelativeLayout msearchall;
	private int curselect = 0;
	private DataFlow2 dataFlow;
	//分类数组对应的addition
	private String[] str2 = {"","24","20|21","05|10","17","19","01|07","02|08","03|15","04|09","22|25","16","12|14","23","18"};
	public static int mPosition;
	private ImageButton goBackBtn, searchBtn;
	String addtion = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_rank);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow2(this);
		mPosition = 0;
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initView();
		initData();
	}
	private void initData() {
		Map<String, String> params = new HashMap<String, String>();
		String token = SharedPreferencesUtil.getSharedData(this, "userInfor", "token");
		params.put("addtion", addtion);
		params.put("token", token);
		RetrofitClient.getInstance(this).createBaseApi().queryCatagTree(
				params, new BaseObserver<String>(this) {
					@Override
					public void onNext(String s) {
						try {
							JSONObject jsonObject = new JSONObject(s);
							String content = jsonObject.optString("content");
							if (jsonObject.optString("status").equals("1")) {
								JSONArray array = new JSONArray(content);
								Map<String, String> map = null;
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									String name = object.optString("name");
									String chid = object.optString("chid");
									String tongjilist = object.optString("tongjilist");
									map = new HashMap<>();
									map.put("name", name);
									map.put("content", chid);
									map.put("tongjilist", tongjilist);
									listright.add(map);

								}
								if (adapterright == null) {
									adapterright = new SortRightFragmentListviewAdapter(listright, SortActivity.this);
									mlistViewRight.setAdapter(adapterright);
								}else{
									adapterright.notifyDataSetChanged();
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					protected void hideDialog() {
						DialogSingleUtil.dismiss(0);
					}

					@Override
					protected void showDialog() {
						DialogSingleUtil.show(SortActivity.this);
					}

					@Override
					public void onError(ExceptionHandle.ResponeThrowable e) {
						DialogSingleUtil.dismiss(0);
						StringUtil.showToast(SortActivity.this, e.message);
				}
		});
	}

	private void initView() {
		goBackBtn =  findViewById(R.id.topbar_goback_btn);
		listright = new ArrayList<>();
		mlistView =  findViewById(R.id.mlistview);
		mlistViewRight =  findViewById(R.id.mlistviewright);
		msearchall = findViewById(R.id.msearchall);
		adapter = new ListViewAdapter(str, this);
		mlistView.setAdapter(adapter);
		mlistView.setOnItemClickListener(this);
		msearchall.setOnClickListener(this);
		goBackBtn.setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (curselect!= position) {
			curselect = position;
			mPosition =position;
			adapter.notifyDataSetChanged();
			listright.clear();
			addtion = str2[position];
			initData();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.topbar_goback_btn:
				finish();
				break;
			case R.id.msearchall:
				Intent intent = new Intent(SortActivity.this, SearchMainActivity.class);
				startActivity(intent);
				break;

		}
	}
}
