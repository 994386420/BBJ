package com.bbk.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bbk.activity.R;

public class ProductSpecFragment extends Fragment {
	
	private View mView;
	private ListView specListView;
	private String spec;
	private ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
	private SimpleAdapter listViewAdapter;
	private View headerView;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		Bundle args = getArguments();
		spec = args != null ? args.getString("specStr") : "";
		super.onCreate(savedInstanceState);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView = LayoutInflater.from(getActivity()).inflate(R.layout.product_spec_fragment, null);
		headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		initView();
		initData();
		
		super.onActivityCreated(savedInstanceState);
	}
	
	private void initView() {
		specListView = (ListView) mView.findViewById(R.id.id_stickynavlayout_innerscrollview);
	}
	
	private void initData() {
		initListViewData();
		if(TextUtils.isEmpty(spec) || "\"null\"".equals(spec) || "|".equals(spec)) {
			specListView.addHeaderView(headerView);
			specListView.setDivider(null);
//			LayoutParams params = specListView.getLayoutParams();
//			params.height = BaseTools.getWindowsHeight(getActivity());
//			specListView.setLayoutParams(params);
		} else {
			loadListViewData();
		}
	}
	
	private void initListViewData() {
		listViewAdapter = new SimpleAdapter(getActivity(), itemList,
				R.layout.listview_item_spec, 
				new String[] {"item_spec_title", "item_spec_value"},
				new int[] {R.id.item_spec_title, R.id.item_spec_value});
		specListView.setAdapter(listViewAdapter);
	}

	private void loadListViewData() {
		if(spec != null) {
			String[] arr = spec.split("\\|");
			for(String str: arr)  
	        {  
				if(!str.contains(":") && !str.contains("：")) {
					continue;
				} else if(str.contains("：")) {
					String[] itemArr = str.split("：");
					HashMap<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("item_spec_title", itemArr[0]);
					itemMap.put("item_spec_value", itemArr[1]);
					itemList.add(itemMap);
				} else if(str.contains(":")) {
					String[] itemArr = str.split(":");
					if(itemArr.length < 2) {
						continue;
					}
					HashMap<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("item_spec_title", itemArr[0]);
					itemMap.put("item_spec_value", itemArr[1]);
					itemList.add(itemMap);
				}
	        }
		}
		
		listViewAdapter.notifyDataSetChanged();
	}
	
}
