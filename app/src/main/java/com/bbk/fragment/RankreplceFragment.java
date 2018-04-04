package com.bbk.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.BrandRankAdapter;
import com.bbk.util.JumpIntentUtil;
import com.bumptech.glide.Glide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RankreplceFragment extends Fragment{
	private View mView;
	private TextView firstname,secondname,thirdname;
	private ListView mlistView;
	private ImageView firstimg1,firstimg2,firstimg3,secondimg1,secondimg2,secondimg3,thirdimg1,thirdimg2,thirdimg3;
	private String rowkey1,url1;
	private String rowkey2,url2;
	private String rowkey3,url3;
	private String rowkey4,url4;
	private String rowkey5,url5;
	private String rowkey6,url6;
	private String rowkey7,url7;
	private String rowkey8,url8;
	private String rowkey9,url9;
	private List<Map<String, String>> liststr1;
	private List<String> list;
	private BrandRankAdapter adapter;
	private LinearLayout third,first,second;
	private List<ImageView> imglist;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle!= null) {
			String content = bundle.getString("content");
			String addtion = bundle.getString("addtion");
			mView = inflater.inflate(R.layout.rank_brand_framelayout, null);
			initView();
			initData(content,addtion);
		}
		return mView;
	}

	private void initData(String content,final String addition) {
		try {
			final JSONArray array = new JSONArray(content);
			JSONObject object = array.getJSONObject(0);
			JSONObject object1 = array.getJSONObject(1);
			JSONObject object2 = array.getJSONObject(2);
			initfirst(object,addition);
			initsecond(object1,addition);
			initthird(object2,addition);
			for (int i = 3; i < array.length(); i++) {
				JSONObject object3 = array.getJSONObject(i);
				String brand1 = object3.optString("brand");
				list.add(brand1);
			}
			if (list!= null) {
				adapter = new BrandRankAdapter(getActivity(), list);
				mlistView.setAdapter(adapter);
				mlistView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
							String brand = list.get(arg2);
							Intent intent = new Intent(getActivity(), ResultMainActivity.class);
							intent.putExtra("brand", brand);
							intent.putExtra("addition", addition);
							getActivity().startActivity(intent);
					
					}
				});
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initfirst(JSONObject object,final String addition) throws JSONException {
		final String brand = object.optString("brand");
		firstname.setText(brand);
		first.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ResultMainActivity.class);
				intent.putExtra("brand", brand);
				intent.putExtra("addition", addition);
				getActivity().startActivity(intent);
			}
		});
		JSONArray array = object.getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			final Map<String, String> map = new HashMap<>();
			JSONObject jsonObject = array.getJSONObject(i);
			map.put("rowkey", jsonObject.optString("rowkey"));
			map.put("url", jsonObject.optString("url"));
			map.put("title", jsonObject.optString("title"));
			map.put("domain", jsonObject.optString("domain"));
			map.put("img", jsonObject.optString("img"));
			liststr1.add(map);
			loadimage(i,liststr1);
		}
		
	}
	private void loadimage(final int i, final List<Map<String, String>> liststr){
		Glide.with(getActivity())
		.load(liststr.get(i).get("img"))
		.into(imglist.get(i));
		imglist.get(i).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent;
				if (JumpIntentUtil.isJump2(liststr,i,"domain")) {
					intent = new Intent(getActivity(),IntentActivity.class);
					intent.putExtra("url", liststr.get(i).get("url").toString());
					intent.putExtra("title", liststr.get(i).get("title").toString());
					intent.putExtra("domain", liststr.get(i).get("domain").toString());
					intent.putExtra("groupRowKey", liststr.get(i).get("rowkey").toString());
				}else{
					intent = new Intent(getActivity(),WebViewActivity.class);
					intent.putExtra("url", liststr.get(i).get("url").toString());
					intent.putExtra("groupRowKey", liststr.get(i).get("rowkey").toString());
				}
				startActivity(intent);
			}
		});
	}
	private void initsecond(JSONObject object,final String addition) throws JSONException {
		final String brand = object.optString("brand");
		secondname.setText(brand);
		second.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ResultMainActivity.class);
				intent.putExtra("brand", brand);
				intent.putExtra("addition", addition);
				getActivity().startActivity(intent);
			}
		});
		JSONArray array = object.getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			final Map<String, String> map = new HashMap<>();
			JSONObject jsonObject = array.getJSONObject(i);
			map.put("rowkey", jsonObject.optString("rowkey"));
			map.put("url", jsonObject.optString("url"));
			map.put("title", jsonObject.optString("title"));
			map.put("domain", jsonObject.optString("domain"));
			map.put("img", jsonObject.optString("img"));
			liststr1.add(map);
			loadimage(i+3,liststr1);
		}
		
		
	}
	private void initthird(JSONObject object,final String addition) throws JSONException {
		final String brand = object.optString("brand");
		thirdname.setText(brand);
		third.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), ResultMainActivity.class);
				intent.putExtra("brand", brand);
				intent.putExtra("addition", addition);
				getActivity().startActivity(intent);
			}
		});
		JSONArray array = object.getJSONArray("list");
		for (int i = 0; i < array.length(); i++) {
			final Map<String, String> map = new HashMap<>();
			JSONObject jsonObject = array.getJSONObject(i);
			map.put("rowkey", jsonObject.optString("rowkey"));
			map.put("url", jsonObject.optString("url"));
			map.put("title", jsonObject.optString("title"));
			map.put("domain", jsonObject.optString("domain"));
			map.put("img", jsonObject.optString("img"));
			liststr1.add(map);
			loadimage(i+6,liststr1);
		}
		
	}

	private void initView() {
		list = new ArrayList<>();
		liststr1 = new ArrayList<>();
		imglist = new ArrayList<>();
		firstname = (TextView) mView.findViewById(R.id.firstname);
		secondname = (TextView) mView.findViewById(R.id.secondname);
		thirdname = (TextView) mView.findViewById(R.id.thirdname);
		mlistView = (ListView) mView.findViewById(R.id.mlistView);
		firstimg1 = (ImageView) mView.findViewById(R.id.firstimg1);
		firstimg2 = (ImageView) mView.findViewById(R.id.firstimg2);
		firstimg3 = (ImageView) mView.findViewById(R.id.firstimg3);
		secondimg1 = (ImageView) mView.findViewById(R.id.secondimg1);
		secondimg2 = (ImageView) mView.findViewById(R.id.secondimg2);
		secondimg3 = (ImageView) mView.findViewById(R.id.secondimg3);
		thirdimg1 = (ImageView) mView.findViewById(R.id.thirdimg1);
		thirdimg2 = (ImageView) mView.findViewById(R.id.thirdimg2);
		thirdimg3 = (ImageView) mView.findViewById(R.id.thirdimg3);
		third = (LinearLayout) mView.findViewById(R.id.third);
		second = (LinearLayout) mView.findViewById(R.id.second);
		first = (LinearLayout) mView.findViewById(R.id.first);
		
		imglist.add(firstimg1);
		imglist.add(firstimg2);
		imglist.add(firstimg3);
		imglist.add(secondimg1);
		imglist.add(secondimg2);
		imglist.add(secondimg3);
		imglist.add(thirdimg1);
		imglist.add(thirdimg2);
		imglist.add(firstimg1);
		imglist.add(thirdimg3);
	}

}
