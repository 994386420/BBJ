package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public class BrowseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	public final static int ITEM_TYPE_BROWSE = 1;
	
	private ArrayList<HashMap<String, Object>> items;
	private Context mContext;
	private AdapterDelegatesManager<ArrayList<HashMap<String, Object>>> delegatesManager;
	
	public BrowseListAdapter() {
		super();
	}
	
	public BrowseListAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		this.items = items;
		mContext = context;
		delegatesManager  = new AdapterDelegatesManager<>();
		delegatesManager.addDelegate(new BrowseListAdapterDelegate(mContext, ITEM_TYPE_BROWSE));
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder arg0, int arg1) {
		delegatesManager.onBindViewHolder(items, arg1, arg0);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return delegatesManager.onCreateViewHolder(arg0, arg1);
	}
	
	@Override
	public int getItemViewType(int position) {
		return delegatesManager.getItemViewType(items, position);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}
}
