package com.bbk.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;

public class RankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	public final static int ITEM_TYPE_BRANB = 1;
	public final static int ITEM_TYPE_PRODUCT = 2;
	public final static int ITEM_TYPE_FOOTER = -1;
	
	private ArrayList<HashMap<String, Object>> items;
	private Context mContext;
	private AdapterDelegatesManager<ArrayList<HashMap<String, Object>>> delegatesManager;
	private View footerView;
	private String footerKey = "";
	
	public RankAdapter() {
		super();
	}
	
	public RankAdapter(Context context, ArrayList<HashMap<String, Object>> items) {
		this.items = items;
		mContext = context;
		delegatesManager  = new AdapterDelegatesManager<>();
		delegatesManager.addDelegate(new RankBrandAdapterDelegate(mContext, ITEM_TYPE_BRANB));
		delegatesManager.addDelegate(new RankProductAdapterDelegate(mContext, ITEM_TYPE_PRODUCT));
	}
	
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		if(position < items.size()){
			delegatesManager.onBindViewHolder(items, position, viewHolder);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		if(viewType != ITEM_TYPE_FOOTER){
			return delegatesManager.onCreateViewHolder(viewGroup, viewType);
		}else{
			if(footerView == null){
				footerView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.view_rank_footer, viewGroup, false);
				footerView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent(mContext, ResultMainActivity.class);
						intent.putExtra("keyword", footerKey.substring(4));
						mContext.startActivity(intent);
					}
				});
			}
			TextView footTv = (TextView) footerView.findViewById(R.id.footer_tv);
			footTv.setText(footerKey);
			return new FooterViewHolder(footerView);
		}
	}
	public void notifyDatasChanged(){
		if(footerView != null){
			if(items.size() == 0){
				footerView.setVisibility(View.GONE);
			}else{
				footerView.setVisibility(View.VISIBLE);
			}
		}
		this.notifyDataSetChanged();
	}
	private static class FooterViewHolder extends RecyclerView.ViewHolder {
		  public FooterViewHolder(View itemView) {
			  super(itemView);
		  }
	}
	
	public void setFooterKey(String footerKey) {
		footerKey = "查看所有" + footerKey;
		if(footerView != null){
			TextView footTv = (TextView) footerView.findViewById(R.id.footer_tv);
			footTv.setText(footerKey);
		}
		this.footerKey = footerKey;
	}
	/*public boolean isScrolledFooter(int dy){
		if(footerView != null){
			Log.e("curr", "dy:"+dy+",footerY:"+footerView.getY()+",footerTop:"+footerView.getTop());
		}
		return false;
	}*/
	@Override
	public int getItemViewType(int position) {
		if(position < items.size()){
			return delegatesManager.getItemViewType(items, position);
		}
		return ITEM_TYPE_FOOTER;
	}

	@Override
	public int getItemCount() {
		return items.size() + 1;
	}
}
