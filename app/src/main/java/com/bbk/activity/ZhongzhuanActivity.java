package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by rtj on 2017/12/8.
 */

public class ZhongzhuanActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhongzhuan);
        if (null != getIntent().getStringExtra("content")) {
            String content = getIntent().getStringExtra("content");
            try {
                JSONObject jsonObject = new JSONObject(content);
//                EventIdIntentUtil.EventIdIntent(this, jsonObject);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        View text = findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent Intent = new Intent(this,HomeActivity.class);
        startActivity(Intent);
        finish();
    }
}
