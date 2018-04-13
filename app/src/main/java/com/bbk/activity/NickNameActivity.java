package com.bbk.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.resource.Constants;
import com.bbk.util.DateUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NickNameActivity extends BaseActivity implements OnClickListener{
	private ImageButton topbar_goback_btn;
	private EditText medittext;
	private RelativeLayout mclear;
	private TextView msave;
	private Toast toast;
	private String userID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nick_name);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		initView();
	}

	private void initView() {
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		medittext = (EditText) findViewById(R.id.medittext);
		mclear = (RelativeLayout) findViewById(R.id.mclear);
		msave = (TextView) findViewById(R.id.msave);
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
		topbar_goback_btn.setOnClickListener(this);
		mclear.setOnClickListener(this);
		msave.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.mclear:
			medittext.setText("");
			break;
		case R.id.msave:
			if (!medittext.getText().toString().isEmpty()) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						Map<String, String> params = new HashMap<>();
						params.put("type", "u_nickname");
						params.put("value", medittext.getText().toString());
						params.put("userid", userID);
						String url = Constants.MAIN_BASE_URL_MOBILE+"newService/updateUserInfo";
						String result = HttpUtil.getHttp(params,url,NickNameActivity.this);
						Message msg = Message.obtain();
						msg.what = 3;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				}).start();
			}else{
				if(toast!= null){
					toast.cancel();
				}
				toast = Toast.makeText(NickNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT);
				toast.show();
			}
			break;

		default:
			break;
		}
	}
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isFinishing()) {
				return;
			}
			switch (msg.what) {
			case 3:
				String result = msg.obj.toString();
				try {
					JSONObject object = new JSONObject(result);

					if (object.optString("status").equals("1")) {
						Intent intent = new Intent();
						intent.putExtra("nickname", medittext.getText().toString());
						SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "nickname",
								medittext.getText().toString());
						setResult(4, intent);
						finish();
						Toast.makeText(getApplicationContext(), "昵称修改成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "昵称修改失败", Toast.LENGTH_SHORT).show();
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
}
