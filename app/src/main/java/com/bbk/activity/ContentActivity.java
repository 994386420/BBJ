package com.bbk.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.util.BaseTools;
import com.bbk.util.DensityUtil;
import com.bumptech.glide.Glide;

public class ContentActivity extends BaseActivity implements OnClickListener {
	
	private TextView experienceTitleTextView, experienceDateTextView, contentTitleTextView;
	private ImageButton goBackButton;
	private String content = "";
	private WebView contentWeb;
	private LinearLayout contentLayout;
	private Typeface face;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_main);
//		MyApplication.getInstance().addActivity(this);
		
		face = getApp().getFontFace();
		
		initView();
		
		initData();	
	}

	private void initView(){
		experienceTitleTextView = (TextView) findViewById(R.id.experience_title);
		experienceDateTextView = (TextView) findViewById(R.id.experience_date);
		contentWeb = (WebView) findViewById(R.id.content_web);
		contentTitleTextView = (TextView) findViewById(R.id.bar_content_title);
		
		goBackButton = (ImageButton) findViewById(R.id.content_bar_goback);
		goBackButton.setOnClickListener(this);
		
		contentLayout = (LinearLayout) findViewById(R.id.content_layout);
	}
	
	private void initData(){
		
		contentTitleTextView.setText("经验攻略");
		
		String title = getIntent().getStringExtra("title");
		String date = getIntent().getStringExtra("date");
		content = getIntent().getStringExtra("content");
		
		experienceTitleTextView.setText(title);
		experienceDateTextView.setText(date);
		
		initContent(content);
//		contentWeb.getSettings().setJavaScriptEnabled(false);
//		contentWeb.getSettings().setSupportZoom(false);
//		contentWeb.getSettings().setBuiltInZoomControls(false);
//		contentWeb.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		
//		contentWeb.loadDataWithBaseURL("", content, "text/html", "utf-8", "");
	}
	
	public static SpannableString setTextBold( String content, int startIndex, int endIndex ){
        if( TextUtils.isEmpty(content) || startIndex < 0 || endIndex > content.length() || startIndex >= endIndex ){
            return null;
        }
        SpannableString spannableString = new SpannableString( content );
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
	
	public void initContent(String contentStr) {
		if(TextUtils.isEmpty(contentStr)) {
			return;
		}
		
		String[] contentArr = contentStr.split("\\|\\|\\|");
		
		for(String str: contentArr) {
			if(str.indexOf("<bbk_title>") > 0) {
				int index = str.indexOf("<bbk_title>");
				addTitle(contentLayout, str.replace("<bbk_title>", ""));
				continue;
			}
			if(str.indexOf("<bbk_text>") > 0) {
				int index = str.indexOf("<bbk_text>");
				addParagraph(contentLayout, str.replace("<bbk_text>", ""));
				continue;
			}
			if(str.indexOf("<bbk_img>") > 0) {
				int index = str.indexOf("<bbk_img>");
				addImg(contentLayout, str.replace("<bbk_img>", ""));
				continue;
			}
		}
	}
	
	public void addParagraph(ViewGroup parent, String str) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.bottomMargin = DensityUtil.dip2px(this, 12);
		params.topMargin = DensityUtil.dip2px(this, 12);
		TextView localTextView = new TextView(this);
		localTextView.setTextColor(Color.parseColor("#808080"));
		localTextView.setLineSpacing(DensityUtil.dip2px(this, 8), 1f);
		localTextView.setTextSize(14);
//		localTextView.setText("\u3000\u3000" + str);
		localTextView.setText(str);
		localTextView.setTypeface(face);
		parent.addView(localTextView, params);
	}
	
	public void addTitle(ViewGroup parent, String str) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TextView localTextView = new TextView(this);
		params.bottomMargin = DensityUtil.dip2px(this, 8);
		params.topMargin = DensityUtil.dip2px(this, 12);
		localTextView.setTextColor(Color.parseColor("#464646"));
		localTextView.setTextSize(14);
		localTextView.setText(str);
		localTextView.setTypeface(face);
		parent.addView(localTextView, params);
	}
	
	public void addImg(ViewGroup parent, String str) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		int w = BaseTools.getWindowsWidth(this) - DensityUtil.dip2px(this, 16);
		int h = w * 5 / 9;
		
		params.weight = w;
		params.height = h;
		params.bottomMargin = DensityUtil.dip2px(this, 24);
		params.topMargin = DensityUtil.dip2px(this, 24);
		
		ImageView localImageView = new ImageView(this);
		Glide.with(this).load(str).into(localImageView);
		parent.addView(localImageView, params);
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		setContentView(R.layout.view_null);
		System.gc();
		super.onDestroy();
	}
	
}
