package com.bbk.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.util.LoadImgUtil;

public class HelpContentActivity extends BaseActivity implements OnClickListener {
	
	private ImageButton goBackBtn;
	
	private String title = "";
	private String text = "";
	private int img = 0;
	
	private TextView topBarTitleTv;
	private TextView helpTextTv;
	private ImageView helpImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_content);
		
		title = getIntent().getStringExtra("title");
		text = getIntent().getStringExtra("text");
		img = getIntent().getIntExtra("img", 0);
		
		initView();
		initData();
	}
	
	@Override
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}
	
	public void initView() {
		goBackBtn = $(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		
		topBarTitleTv = $(R.id.topbar_title);
		
		helpTextTv = $(R.id.help_text);
		helpImg = $(R.id.help_img);
	}
	
	public void initData() {
		
		topBarTitleTv.setText(title);
		
		helpTextTv.setText(Html.fromHtml(text));
		if(0 != img) {
			LoadImgUtil.loadImg(this, helpImg, img);
		}
		
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
	
}
