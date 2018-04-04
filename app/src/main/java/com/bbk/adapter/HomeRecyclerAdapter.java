package com.bbk.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bbk.activity.R;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.view.RollHeaderView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class HomeRecyclerAdapter extends Adapter<ViewHolder>{
	Context context;
    LayoutInflater inflater;


    //建立枚举 多个item 类型
    public enum ITEM_TYPE {
        BANNER,//广告条
        TAG,//标签
        HISTORY,//查历史价
        TUIJIAN,//为你推荐
        ACTIVITY, //商城活动
        DIANPU,//店铺
        ATICLE,//文章
        GUESS//猜你喜欢
    }
    public HomeRecyclerAdapter(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getItemViewType(int position) {
    	if (position == 0) {
			return ITEM_TYPE.BANNER.ordinal();
    	}else if(position == 1){
    		return ITEM_TYPE.TAG.ordinal();
		}else if(position == 2){
			return ITEM_TYPE.HISTORY.ordinal();
		}else if(position == 3){
			return ITEM_TYPE.TUIJIAN.ordinal();
		}else if(position == 4){
			return ITEM_TYPE.ACTIVITY.ordinal();
		}else if(position == 5){
			return ITEM_TYPE.DIANPU.ordinal();
		}else if(position == 6){
			return ITEM_TYPE.ATICLE.ordinal();
		}else if(position == 7){
			return ITEM_TYPE.GUESS.ordinal();
    	}
    	return 0;
    }
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		 if (viewHolder instanceof BannerViewHolder){
	            //广告条
			 RollHeaderView bannerView = new RollHeaderView(context, 0.20f, true);
			 ((BannerViewHolder) viewHolder).mbox.addView(bannerView);
			 loadBannerItem(bannerView,null);
		 }
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		RecyclerView.ViewHolder holder = null;
		if (viewType == ITEM_TYPE.BANNER.ordinal()) {
			holder = new BannerViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//广告条布局
    	}else if(viewType == ITEM_TYPE.TAG.ordinal()){
    		holder = new TagViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//标签
		}else if(viewType == ITEM_TYPE.HISTORY.ordinal()){
			holder = new HistoryViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//查历史价
		}else if(viewType == ITEM_TYPE.TUIJIAN.ordinal()){
			holder = new TuijianViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//为你推荐
		}else if(viewType == ITEM_TYPE.ACTIVITY.ordinal()){
			holder = new DomainViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//商城活动
		}else if(viewType == ITEM_TYPE.DIANPU.ordinal()){
			holder = new DianpuViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//店铺
		}else if(viewType == ITEM_TYPE.ATICLE.ordinal()){
			holder = new AticleViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//文章
		}else if(viewType == ITEM_TYPE.GUESS.ordinal()){
			holder = new GuessViewHolder(inflater.inflate(R.layout.home_recyclerview_child, parent, false));//猜你喜欢
    	}
		return holder;
	}
    /**
     * 广告条、
     */
    class BannerViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public BannerViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 标签
     */
    class TagViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public TagViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 查历史价
     */
    class HistoryViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 为你推荐
     */
    class TuijianViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public TuijianViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 商城活动
     */
    class DomainViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public DomainViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 店铺
     */
    class DianpuViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public DianpuViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 文章
     */
    class AticleViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public AticleViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
    /**
     * 猜你喜欢
     */
    class GuessViewHolder extends RecyclerView.ViewHolder{
    	LinearLayout mbox;
        public GuessViewHolder(View itemView) {
            super(itemView);
            mbox = (LinearLayout) itemView.findViewById(R.id.mbox);
        }
    }
	// 添加banner
	private void loadBannerItem(RollHeaderView bannerView, final JSONArray ja) {
		List<Object> imgUrlList = new ArrayList<>();
		try {
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jo = ja.getJSONObject(i);
				String imgUrl = jo.getString("img");
				imgUrlList.add(imgUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		bannerView.setOnHeaderViewClickListener(new RollHeaderView.HeaderViewClickListener() {
			@Override
			public void HeaderViewClick(int position) {
				JSONObject object;
				try {
					object = ja.getJSONObject(position);
					if (null != object.optString("eventId")) {
						EventIdIntentUtil.EventIdIntent(context, object);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		bannerView.setImgUrlData(imgUrlList);
	}

}
