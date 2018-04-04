package com.bbk.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andview.refreshview.XRefreshView;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.R;
import com.bbk.adapter.CoinWithdrawListAdapter;
import com.bbk.dialog.AlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView;
import com.bbk.view.MyFootView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class EverydayJbFragment extends Fragment implements ResultEvent{
	private View mView;
	private ViewFlipper mviewflipper;
	private TextView mjbcoin,mwithdrawnum,mwithdraw;
	private ListView mlistview;
	private List<Map<String, String>> list;
	private DataFlow dataFlow;
	private String userID;
	private CoinWithdrawListAdapter adapter;
	private XRefreshView xrefresh;
	private boolean isoncreat = false;
	private int page = 1;
	private boolean isclear = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container
			, Bundle savedInstanceState) {
		
		mView = LayoutInflater.from(getActivity())
				.inflate(R.layout.fragment_everyday_jb, null);
		dataFlow = new DataFlow(getActivity());
		initView();
		initData();
		return mView;
	}

	private void initView() {
		list = new ArrayList<Map<String,String>>();

		xrefresh = (XRefreshView)mView.findViewById(R.id.xrefresh);
		mviewflipper = (ViewFlipper)mView.findViewById(R.id.mviewflipper);
		mjbcoin = (TextView)mView.findViewById(R.id.mjbcoin);
		mwithdrawnum = (TextView)mView.findViewById(R.id.mwithdrawnum);
		mwithdraw = (TextView)mView.findViewById(R.id.mwithdraw);
		mlistview = (ListView)mView.findViewById(R.id.mlistview);
		mwithdraw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (Integer.valueOf(mjbcoin.getText().toString())>= 1000) {
					initDialog2();
				}else{
					initDialog1();
				}
				
			}
		});
		xrefresh.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {


			@Override
			public void onRelease(float direction) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onRefresh(boolean isPullDown) {
				isclear = true;
				page = 1;
				initData();

			}

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadMore(boolean isSilence) {
				page++;
				initData();
			}

			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
				// TODO Auto-generated method stub

			}
		});
		MyFootView footView = new MyFootView(getActivity());
		xrefresh.setCustomFooterView(footView);
	}

	private void initDialog1(){
		new AlertDialog(getActivity()).builder().setTitle("提示")
		.setMsg("亲,累计满1元才能提现哦~")
		.setMsg2("明后天继续来签到就有机会提现")
		.setLeft()
		.setNegativeButton("我知道了", new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
			}
		}).setNegativeButtonColor("#333333")
		.setPositiveButton("领更多鲸币", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		}).setPositiveButtonColor("#ff7d41")
		.show();
	}
	private void initDialog2(){
		new AlertDialog(getActivity()).builder().setTitle("提现攻略")
		.setMsg("第1步：搜索并关注“比比鲸大数据”公众号")
		.setMsg2("第2步：发送“提现”")
		.setMsg3("第3步：绑定账号，验证身份")
		.setMsg4("第4步：领取红包")
		.setLeft()
		.setPositiveButton("OK,去微信提现", new View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				
			}
		}).setPositiveButtonColor("#ffffff").setPositiveBackgroundColor("#ff7d41").show();
	}
	private void initData() {
		userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		if (!TextUtils.isEmpty(userID)) {
			HashMap<String, String> paramsMap = new HashMap<>();
			paramsMap.put("userid", userID);
			paramsMap.put("page", page+"");
			dataFlow.requestData(1, "newService/queryJingbiListByUserid", paramsMap, this, false);
		}
		
	}
	private void initViewflipper() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.jbwithdraw_viewflipper,null);
		CircleImageView userimg = (CircleImageView)view.findViewById(R.id.userimg);
		TextView name = (TextView)view.findViewById(R.id.name);
		TextView rmb = (TextView)view.findViewById(R.id.rmb);
		mviewflipper.addView(view);
	}
	private void initlist(JSONArray list2) throws JSONException {
		if (isclear){
			list.clear();
			isclear = false;
		}
		if (list2.length()<10){
			xrefresh.setPullLoadEnable(false);
		}else {
			xrefresh.setPullLoadEnable(true);
		}
		for (int i = 0; i < list2.length(); i++) {
			JSONObject object = list2.getJSONObject(i);
			Map<String, String> map = new HashMap<>();
			map.put("jinbi", object.optString("jinbi"));
			map.put("message", object.optString("message"));
			map.put("time", object.optString("time"));
			map.put("type", object.optString("type"));
			list.add(map);
		}
		if (adapter!=null) {
			adapter.notifyDataSetChanged();
		}else{
			adapter = new CoinWithdrawListAdapter(list, getActivity());
			mlistview.setAdapter(adapter);
		}
	}
	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		xrefresh.stopLoadMore();
		xrefresh.stopRefresh();
		switch (requestCode) {
		case 1:
			try {
				JSONObject object = new JSONObject(content);
				if (isclear){
					int jinbi = object.optInt("jinbi");
					String money = object.optString("money");
					mwithdrawnum.setText("当前可提现额度"+money+"元");
					mjbcoin.setText(jinbi+"");
				}
				JSONArray list = object.getJSONArray("list");
				initlist(list);
				mviewflipper.setFocusable(true);
				mviewflipper.setFocusableInTouchMode(true);  
				mviewflipper.requestFocus();  
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		if (isoncreat) {
			initData();
		}else{
			isoncreat = true;
		}
	}

}
