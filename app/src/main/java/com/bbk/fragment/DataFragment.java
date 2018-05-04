package com.bbk.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.bbk.activity.DataWebViewActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.util.MD5Util;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.RollHeaderView;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class DataFragment extends BaseViewPagerFragment implements OnClickListener{
	private View mView;
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
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(null == mView) {
			mView = inflater.inflate(R.layout.fragment_data, null);
			data_head = mView.findViewById(R.id.data_head);
			initstateView();
			initView();
		}
		return mView;
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
  
            sbar = getContext().getResources().getDimensionPixelSize(x);  
  
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
	public void initView() {
		login_remind = (RelativeLayout) mView.findViewById(R.id.login_remind);
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
		list1 = (RelativeLayout) mView.findViewById(R.id.list1);
		list2 = (RelativeLayout) mView.findViewById(R.id.list2);
		mlogin = (TextView) mView.findViewById(R.id.mlogin);
		banner_layout = (LinearLayout) mView.findViewById(R.id.banner_layout);
		initimageview();
		bannerView = new RollHeaderView(getActivity(),0.20f,true);
		banner_layout.addView(bannerView);
		loadBannerItem();
		list1.setOnClickListener(this);
		list2.setOnClickListener(this);
		mlogin.setOnClickListener(this);
	}
	//设置图片高度
	private void initimageview() {
		image1 = (ImageView) mView.findViewById(R.id.image1);
		image2 = (ImageView) mView.findViewById(R.id.image2);
		image3 = (ImageView) mView.findViewById(R.id.image3);
		image4 = (ImageView) mView.findViewById(R.id.image4);
		image5 = (ImageView) mView.findViewById(R.id.image5);
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
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

	@Override
	protected void loadLazyData() {

	}

	//添加banner
		private void loadBannerItem(){
			List<Object> imgUrlList = new ArrayList<>();
			imgUrlList.add(R.mipmap.banner_01);
			imgUrlList.add(R.mipmap.banner_02);
			bannerView.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
	            @Override
	            public void HeaderViewClick(int position) {
	            	String password = SharedPreferencesUtil.getSharedData(getActivity(), "userInfor", "mid");
	        		String userID = SharedPreferencesUtil.getSharedData(getActivity(),"userInfor", "userID");
//	        		String pass = MD5Util.Md5(password);
					String pass = password;
	            	Intent intent;
	            	String login_STATE = SharedPreferencesUtil.getSharedData(getContext(),
	    					"userInfor", "login_STATE");
	            	if (position == 0) {
	        			if (login_STATE.isEmpty()) {
	        				intent = new Intent(getActivity(), UserLoginNewActivity.class);
	        				startActivity(intent);
	        			}else{
	        				intent = new Intent(getActivity(), DataWebViewActivity.class);
	        				intent.putExtra("url", "http://bibijing.com/market?"+"&mid="+pass+"&client=android");//bid=+userID+
	        				startActivity(intent);
	        			}
					}else{
						if (login_STATE.isEmpty()) {
							intent = new Intent(getActivity(), UserLoginNewActivity.class);
							startActivity(intent);
						}else{
							intent = new Intent(getActivity(), DataWebViewActivity.class);
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
		String pass = SharedPreferencesUtil.getSharedData(getContext().getApplicationContext(), "userInfor", "password");
		String userID = SharedPreferencesUtil.getSharedData(getContext().getApplicationContext(),"userInfor", "userID");
		String login_STATE = SharedPreferencesUtil.getSharedData(getContext().getApplicationContext(),
				"userInfor", "login_STATE");
		Intent intent;
		switch (v.getId()) {
		//点击行业报告
		case R.id.list1:
			
			if (login_STATE.isEmpty()) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivity(intent);
				Toast.makeText(getActivity(), "查看大数据分析请先登录", Toast.LENGTH_LONG).show();
			}else{
				intent = new Intent(getActivity(), DataWebViewActivity.class);
				intent.putExtra("url", "http://bibijing.com/market?bid="+userID+"&pass="+pass+"&client=android");
				startActivity(intent);
				
			}
			
			break;
			//点击数据图表
		case R.id.list2:
			if (login_STATE.isEmpty()) {
				intent = new Intent(getActivity(), UserLoginNewActivity.class);
				startActivity(intent);
				Toast.makeText(getActivity(), "查看大数据分析请先登录", Toast.LENGTH_LONG).show();
			}else{
				intent = new Intent(getActivity(), DataWebViewActivity.class);
				intent.putExtra("url", "http://bibijing.com/data/main?bid="+userID+"&pass="+pass+"&client=android");
				startActivity(intent);
			}
			break;
		case R.id.mlogin:
			intent = new Intent(getActivity(), UserLoginNewActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
		
	
}
