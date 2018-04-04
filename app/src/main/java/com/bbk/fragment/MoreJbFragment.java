package com.bbk.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MoreJbFragment extends Fragment implements ResultEvent{
	private View mView;
	private ViewFlipper mviewflipper;
	private TextView mshare,mjbnum,mfriendnum;
	private DataFlow dataFlow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			, Bundle savedInstanceState) {
		
		mView = LayoutInflater.from(getActivity())
				.inflate(R.layout.fragment_more_jb, null);
		dataFlow = new DataFlow(getActivity());
		initView();
		initData();
		return mView;
	}

	private void initView() {

		
		mviewflipper = (ViewFlipper)mView.findViewById(R.id.mviewflipper);
		mshare = (TextView)mView.findViewById(R.id.mshare);
		mjbnum = (TextView)mView.findViewById(R.id.mjbnum);
		mfriendnum = (TextView)mView.findViewById(R.id.mfriendnum);
		mshare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
				String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/user/InvitRegist?invitCode="+userID;
				ShareUtil.showShareDialog(v, getActivity(), "专业的网购比价、导购平台","比比鲸，帮您比价、省钱、省事", url,"");

			}
		});
		

	}

	private void initData() {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		HashMap<String, String> paramsMap = new HashMap<>();
		paramsMap.put("userid", userID);
		dataFlow.requestData(1, "newService/queryInviteByuserid", paramsMap, this, false);
	}
	private void initViewflipper() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.jbwithdraw_viewflipper2,null);
		CircleImageView userimg1 = (CircleImageView)view.findViewById(R.id.userimg1);
		TextView name1 = (TextView)view.findViewById(R.id.name1);
		TextView mnumber1 = (TextView)view.findViewById(R.id.mnumber1);
		TextView rmb1 = (TextView)view.findViewById(R.id.rmb1);
		CircleImageView userimg2 = (CircleImageView)view.findViewById(R.id.userimg2);
		TextView name2 = (TextView)view.findViewById(R.id.name2);
		TextView mnumber2 = (TextView)view.findViewById(R.id.mnumber2);
		TextView rmb2 = (TextView)view.findViewById(R.id.rmb2);
		mviewflipper.addView(view);
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode) {
		case 1:
			try {
				JSONObject jsonObject = new JSONObject(content);
				String count = jsonObject.optString("count");
				String jinbiCount = jsonObject.optString("jinbiCount");
				mjbnum.setText(jinbiCount);
				mfriendnum.setText(count);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			for (int i = 0; i < 4; i++) {
//				initViewflipper();
//			}
//			Animation ru = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_ru);  
//			Animation chu = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_chu);  
//			mviewflipper.setInAnimation(ru);
//			mviewflipper.setOutAnimation(chu);
//			mviewflipper.startFlipping();
			break;

		default:
			break;
		}
	}


}
