package com.bbk.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mobstat.ar;
import com.bbk.activity.wxapi.WXEntryActivity;
import com.bbk.adapter.QueryHistoryAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QueryHistoryActivity extends Activity implements OnClickListener, ResultEvent, TextWatcher {

	private ImageButton topbar_goback_btn;
	private EditText medit;
	private RelativeLayout mqueryhistory;
	private MyListView mlistview;
	private LinearLayout mdelete;
	private List<Map<String, String>> list;
	private QueryHistoryAdapter adapter;
	private JSONArray array;
	private DataFlow dataFlow;
	private Toast toast;
	private TextView historytext;
	private LinearLayout myindao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_history);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);
		initView();
		initData();
	}

	private void initView() {
		list = new ArrayList<>();
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		medit = (EditText) findViewById(R.id.medit);
		mqueryhistory = (RelativeLayout) findViewById(R.id.mqueryhistory);
		mlistview = (MyListView) findViewById(R.id.mlistview);
		mdelete = (LinearLayout) findViewById(R.id.mdelete);
		myindao = (LinearLayout) findViewById(R.id.myindao);
		historytext = (TextView) findViewById(R.id.historytext);
		mdelete.setOnClickListener(this);
		mqueryhistory.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
		medit.addTextChangedListener(this);
		mqueryhistory.setEnabled(false);
	}

	private void initData() {
		adapter = new QueryHistoryAdapter(list, this);
		mlistview.setAdapter(adapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
				String rowkey = list.get(arg2).get("rowkey");
				String url1 = BaseApiService.Base_URL + "mobile/user/history?rowkey=" + rowkey+"&userid="+userID;
				Intent intent = new Intent(QueryHistoryActivity.this, WebViewActivity.class);
				intent.putExtra("url", url1);
				startActivity(intent);
			}
		});
		String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "QueryHistory",
				"QueryHistory");
		if (!LogisticsQuery.isEmpty()) {
			try {
				array = new JSONArray(LogisticsQuery);
				for (int i = 0; i < array.length(); i++) {
					Map<String, String> map = new HashMap<>();
					JSONObject object = array.getJSONObject(i);
					String url = object.optString("url");
					String title = object.optString("title");
					String rowkey = object.optString("rowkey");
					map.put("url", url);
					map.put("title", title);
					map.put("rowkey", rowkey);
					list.add(map);
				}
				adapter.notifyDataSetChanged();
				mdelete.setVisibility(View.VISIBLE);
				historytext.setVisibility(View.VISIBLE);
				myindao.setVisibility(View.GONE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mdelete.setVisibility(View.GONE);
			historytext.setVisibility(View.GONE);
			myindao.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mdelete:
			SharedPreferencesUtil.cleanShareData(MyApplication.getApplication(), "QueryHistory");
			list.clear();
			adapter.notifyDataSetChanged();
			mdelete.setVisibility(View.GONE);
			historytext.setVisibility(View.GONE);
			myindao.setVisibility(View.VISIBLE);
			break;
		case R.id.mqueryhistory:
			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("url", medit.getText().toString());
			dataFlow.requestData(1, "newService/checkExsistProduct", paramsMap, this);
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:

			try {
				if (!content.equals("{}") && !content.equals("")) {
					JSONObject jsonObject = new JSONObject(content);

					String title = jsonObject.optString("title");
					String rowkey = jsonObject.optString("rowkey");
					String url = jsonObject.optString("url");
					String LogisticsQuery = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),
							"QueryHistory", "QueryHistory");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
					String now = sdf.format(new Date());
					String time = now.substring(5, 10);
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
					SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "QueryHistory", "QueryHistory",
							array.toString());
					Map<String, String> map = new HashMap<>();
					map.put("url", url);
					map.put("rowkey", rowkey);
					map.put("title", title);
					list.add(map);
					adapter.notifyDataSetChanged();
					mdelete.setVisibility(View.VISIBLE);
					historytext.setVisibility(View.VISIBLE);
					myindao.setVisibility(View.GONE);
					String url1 = BaseApiService.Base_URL + "mobile/user/history?rowkey=" + rowkey;
					Intent intent = new Intent(this, WebViewActivity.class);
					intent.putExtra("url", url1);
					startActivity(intent);
				} else {
					if (toast != null) {
						toast.cancel();
					}
					toast = Toast.makeText(this, "找不到数据！", Toast.LENGTH_SHORT);
					toast.show();
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

	@Override
	public void afterTextChanged(Editable arg0) {

	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String str = medit.getText().toString();
		if (!str.isEmpty()) {
			mqueryhistory.setBackgroundResource(R.drawable.text_result_red);
			mqueryhistory.setEnabled(true);
		} else {
			mqueryhistory.setBackgroundResource(R.drawable.text_result_gray2);
			mqueryhistory.setEnabled(false);
		}
	}
}
