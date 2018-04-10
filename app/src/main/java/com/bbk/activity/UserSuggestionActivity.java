package com.bbk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;

public class UserSuggestionActivity extends BaseActivity implements OnClickListener,ResultEvent {
	
	private DataFlow dataFlow;
	private TextView msend;
	private EditText medit;
	private ImageView topbar_goback_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_suggestion);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		dataFlow = new DataFlow(this);

//		MyApplication.getInstance().addActivity(this);

		initView();
	}

	/**
	 * 
	 */
	private void initView() {
		msend = (TextView)findViewById(R.id.msend);
		msend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				initData();
				finish();
			}
		});
		medit = (EditText)findViewById(R.id.medit);
		topbar_goback_btn = (ImageView)findViewById(R.id.topbar_goback_btn);
		topbar_goback_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}

	/**
	 * 
	 */
	private void initData() {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		Map<String, String> paramsMap = new HashMap<>();
		paramsMap.put("userid", userID);
		paramsMap.put("content", medit.getText().toString());
		dataFlow.requestData(1, "newService/insertFeedBack", paramsMap, this);
		Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


}
