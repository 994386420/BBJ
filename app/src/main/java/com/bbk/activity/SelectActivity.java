package com.bbk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class SelectActivity extends BaseActivity implements OnClickListener{
	private TextView select_regist,select_guanlian;
	private ImageButton select_goback_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select);
		initView();
	}

	private void initView() {
		select_regist = (TextView) findViewById(R.id.select_regist);
		select_guanlian = (TextView) findViewById(R.id.select_guanlian);
		select_goback_btn = (ImageButton) findViewById(R.id.select_goback_btn);
		select_regist.setOnClickListener(this);
		select_guanlian.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.select_regist:
			intent = new Intent(this,RegisterBangDingActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.select_guanlian:
			intent = new Intent(this,AcountGuanLianActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.select_goback_btn:
			finish();
		default:
			break;
		}
	}
}
