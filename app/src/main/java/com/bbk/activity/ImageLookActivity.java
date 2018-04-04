package com.bbk.activity;

import com.bumptech.glide.Glide;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageLookActivity extends BaseActivity {
	private ImageView mimg;
	private RelativeLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_look);
		String img = getIntent().getStringExtra("img");
		mimg = (ImageView) findViewById(R.id.mimg);
		layout = (RelativeLayout) findViewById(R.id.layout);
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		Glide.with(this).load(img).into(mimg);
	}
}
