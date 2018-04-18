package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.RollHeaderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class DataFragmentActivity extends BaseActivity implements OnClickListener{
//	private View mView;
	private ScrollView introduceScroll;
	private RelativeLayout list1,list2;
	public static RelativeLayout login_remind;
	private LinearLayout banner_layout;
	private Context context;
	private View data_head;
	private TextView mlogin;
	private RollHeaderView bannerView;
	private ImageView image1,image2,image3,image4,image5;
	private SharedPreferences sp;
	private ImageButton goBackBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_data);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
//		data_head = findViewById(R.id.data_head);
//		initstateView();
		initView();
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
//	private void initstateView() {
//		if (Build.VERSION.SDK_INT >=19) {
//			data_head.setVisibility(View.VISIBLE);
//		}
//		int result = getStatusBarHeight();
//		LayoutParams layoutParams = data_head.getLayoutParams();
//		layoutParams.height = result;
//		data_head.setLayoutParams(layoutParams);
//	}
	public void initView() {
		goBackBtn =  findViewById(R.id.topbar_goback_btn);
		login_remind = findViewById(R.id.login_remind);
//		sp = getActivity().getSharedPreferences("userInfor", Context.MODE_PRIVATE);
//		String login_STATE = sp.getString("login_STATE", null);
		String login_STATE = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "login_STATE");
		Log.e("====login_STATE=====", ""+login_STATE);
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		if (login_STATE.equals("1")) {
			login_remind.setVisibility(View.GONE);
		}else{
			login_remind.setVisibility(View.VISIBLE);
		}
		list1 = findViewById(R.id.list1);
		list2 = findViewById(R.id.list2);
		mlogin =findViewById(R.id.mlogin);
		banner_layout = findViewById(R.id.banner_layout);
		initimageview();
		bannerView = new RollHeaderView(this,0.20f,true);
		banner_layout.addView(bannerView);
		loadBannerItem();
		list1.setOnClickListener(this);
		list2.setOnClickListener(this);
		mlogin.setOnClickListener(this);
		goBackBtn.setOnClickListener(this);
	}
	//设置图片高度
	private void initimageview() {
		image1 = findViewById(R.id.image1);
		image2 = findViewById(R.id.image2);
		image3 = findViewById(R.id.image3);
		image4 = findViewById(R.id.image4);
		image5 = findViewById(R.id.image5);
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
	    int width = wm.getDefaultDisplay().getWidth();
	    List<ImageView> list = new ArrayList<>();
	    list.add(image1);
	    list.add(image2);
	    list.add(image3);
	    list.add(image4);
	    list.add(image5);
	    for (int i = 0; i < list.size(); i++) {
	    	LayoutParams params = list.get(i).getLayoutParams();
		    params.height = (width)*268/640;
		    list.get(i).setLayoutParams(params);
		}
	    
	}

	//添加banner
		private void loadBannerItem(){
			List<Object> imgUrlList = new ArrayList<>();
			imgUrlList.add(R.mipmap.banner_01);
			imgUrlList.add(R.mipmap.banner_02);
			bannerView.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
	            @Override
	            public void HeaderViewClick(int position) {
	            	String password = SharedPreferencesUtil.getSharedData(DataFragmentActivity.this, "userInfor", "mid");
	        		String userID = SharedPreferencesUtil.getSharedData(DataFragmentActivity.this,"userInfor", "userID");
//	        		String pass = MD5Util.Md5(password);
					String pass = password;
	            	Intent intent;
	            	String login_STATE = SharedPreferencesUtil.getSharedData(DataFragmentActivity.this,
	    					"userInfor", "login_STATE");
	            	if (position == 0) {
	        			if (login_STATE.isEmpty()) {
	        				intent = new Intent(DataFragmentActivity.this, UserLoginNewActivity.class);
	        				startActivity(intent);
	        			}else{
	        				intent = new Intent(DataFragmentActivity.this, DataWebViewActivity.class);
	        				intent.putExtra("url", "http://bibijing.com/market?"+"&mid="+pass+"&client=android");//bid=+userID+
	        				startActivity(intent);
	        			}
					}else{
						if (login_STATE.isEmpty()) {
							intent = new Intent(DataFragmentActivity.this, UserLoginNewActivity.class);
							startActivity(intent);
						}else{
							intent = new Intent(DataFragmentActivity.this, DataWebViewActivity.class);
							intent.putExtra("url", "http://bibijing.com/data/main?"+"&mid="+pass+"&client=android");//+userID bid=
							startActivity(intent);
						}
					}
	            	
	            	
	            }
	        });
			bannerView.setImgUrlData(imgUrlList);
		}

	@Override
	public void onClick(View v) {
//		String password = sp.getString("password", null);
//		String userID = sp.getString("userID", null);
//		String login_STATE = sp.getString("login_STATE", null);
		String pass = SharedPreferencesUtil.getSharedData(getApplicationContext(), "userInfor", "password");
		String userID = SharedPreferencesUtil.getSharedData(getApplicationContext(),"userInfor", "userID");
		String login_STATE = SharedPreferencesUtil.getSharedData(getApplicationContext(),
				"userInfor", "login_STATE");
		Intent intent;
		switch (v.getId()) {
		//点击行业报告
		case R.id.list1:
			
			if (login_STATE.isEmpty()) {
				intent = new Intent(DataFragmentActivity.this, UserLoginNewActivity.class);
				startActivity(intent);
				Toast.makeText(DataFragmentActivity.this, "查看大数据分析请先登录", Toast.LENGTH_LONG).show();
			}else{
				intent = new Intent(DataFragmentActivity.this, DataWebViewActivity.class);
				intent.putExtra("url", "http://bibijing.com/market?bid="+userID+"&pass="+pass+"&client=android");
				startActivity(intent);
				
			}
			
			break;
			//点击数据图表
		case R.id.list2:
			if (login_STATE.isEmpty()) {
				intent = new Intent(DataFragmentActivity.this, UserLoginNewActivity.class);
				startActivity(intent);
				Toast.makeText(DataFragmentActivity.this, "查看大数据分析请先登录", Toast.LENGTH_LONG).show();
			}else{
				intent = new Intent(DataFragmentActivity.this, DataWebViewActivity.class);
				intent.putExtra("url", "http://bibijing.com/data/main?bid="+userID+"&pass="+pass+"&client=android");
				startActivity(intent);
			}
			break;
		case R.id.mlogin:
			intent = new Intent(DataFragmentActivity.this, UserLoginNewActivity.class);
			startActivity(intent);
			break;
			case R.id.topbar_goback_btn:
				finish();
				break;
		default:
			break;
		}
	}
		
	
}
