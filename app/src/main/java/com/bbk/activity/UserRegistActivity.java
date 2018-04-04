package com.bbk.activity;


import java.lang.reflect.Field;

import org.json.JSONObject;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.server.SmsObserver;
import com.bbk.util.ImmersedStatusbarUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserRegistActivity extends BaseActivity implements ResultEvent,TextWatcher,OnClickListener{

    public static final int MSG_RECEIVED_CODE = 0;  
    private SmsObserver mObserver;  
    private Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MSG_RECEIVED_CODE) {  
                String code = (String) msg.obj;  
                mcodeedit.setText(code);  
            }  
			return false;
		}
	});
	private DataFlow dataFlow;
	private View data_head;
	private TextView mgetcode,mregist,mprotocol; 
	private EditText mcodeedit,mphoneedit,mpassedit1,mpassedit2;  
	private LinearLayout mqqlogin,mwxlogin;
	private ImageView mclear1,mclear2,mclear3,mclear4;


    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_user_regist);  
        mObserver = new SmsObserver(this, mHandler);  
        Uri uri = Uri.parse("content://sms");  
        // 注册ConentObserver  
        getContentResolver().registerContentObserver(uri, true, mObserver);
        dataFlow = new DataFlow(this);
		data_head = findViewById(R.id.data_head);
		ImmersedStatusbarUtils.initAfterSetContentView(this, data_head);
		ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
		ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
		initstateView();
		initView();
		initData();
    }  
  
    private void initView() {
    	 mcodeedit = (EditText) findViewById(R.id.mcodeedit);  
    	 mphoneedit = (EditText) findViewById(R.id.mphoneedit);  
    	 mpassedit1 = (EditText) findViewById(R.id.mpassedit1);  
    	 mpassedit2 = (EditText) findViewById(R.id.mpassedit2);  
    	 mgetcode = (TextView) findViewById(R.id.mgetcode);  
    	 mregist = (TextView) findViewById(R.id.mregist);  
    	 mprotocol = (TextView) findViewById(R.id.mprotocol);  
    	 mqqlogin = (LinearLayout) findViewById(R.id.mqqlogin);  
    	 mwxlogin = (LinearLayout) findViewById(R.id.mwxlogin);  
    	 mclear1 = (ImageView) findViewById(R.id.mclear1);  
    	 mclear2 = (ImageView) findViewById(R.id.mclear2);  
    	 mclear3 = (ImageView) findViewById(R.id.mclear3);  
    	 mclear4 = (ImageView) findViewById(R.id.mclear4);  
    	 
    	 mcodeedit.addTextChangedListener(this);
    	 mphoneedit.addTextChangedListener(this);
    	 mpassedit1.addTextChangedListener(this);
    	 mpassedit2.addTextChangedListener(this);
    	 mclear1.setOnClickListener(this);
    	 mclear2.setOnClickListener(this);
    	 mclear3.setOnClickListener(this);
    	 mclear4.setOnClickListener(this);
	}

	private void initData() {
		
	}
	private int getStatusBarHeight() {  
        Class<?> c = null;  
  
        Object obj = null;  
  
        Field field = null;  
  
        int x = 0, sbar = 0;  
  
        try {  
  
            c = Class.forName("com.android.internal.R$dimen");  
  
            obj = c.newInstance();  
  
            field = c.getField("status_bar_height");  
  
            x = Integer.parseInt(field.get(obj).toString());  
  
            sbar = getResources().getDimensionPixelSize(x);  
  
        } catch (Exception e1) {  
  
            e1.printStackTrace();  
  
        }  
  
        return sbar;  
    } 
	private void initstateView() {
		if (Build.VERSION.SDK_INT >=19) {
			data_head.setVisibility(View.VISIBLE);
		}
		int result = getStatusBarHeight();
		LayoutParams layoutParams = data_head.getLayoutParams();
		layoutParams.height = result;
		data_head.setLayoutParams(layoutParams);
	}
	protected void onPause() {  
        super.onPause();  
        // 注销ConentObserver  
        getContentResolver().unregisterContentObserver(mObserver);  
    }

	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		String code = mcodeedit.getText().toString();
		String phonenumber = mphoneedit.getText().toString();
		String password1 = mpassedit1.getText().toString();
		String password2 = mpassedit2.getText().toString();
		isClearGone(code,mclear2);
		isClearGone(phonenumber,mclear1);
		isClearGone(password1,mclear3);
		isClearGone(password2,mclear4);
		if (!TextUtils.isEmpty(code) && !TextUtils.isEmpty(phonenumber) 
				&& !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2)) {
			mregist.setBackgroundResource(R.drawable.text_result_red);
		}else{
			mregist.setBackgroundResource(R.drawable.text_result_gray2);
		}
	}
	public void isClearGone(String str, ImageView mclear){
		if (TextUtils.isEmpty(str)) {
			mclear.setVisibility(View.GONE);
		}else{
			mclear.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mback:
			finish();
			break;
		case R.id.mclear1:
			mphoneedit.setText("");
			break;
		case R.id.mclear2:
			mcodeedit.setText("");
			break;
		case R.id.mclear3:
			mpassedit1.setText("");
			break;
		case R.id.mclear4:
			mpassedit2.setText("");
			break;
		default:
			break;
		}
	} 
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		// TODO Auto-generated method stub
		
	}
 
}

