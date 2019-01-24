package com.bbk.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class DetailImageAdapter extends RecyclerView.Adapter{
    private List<Object> list;
    private Context context;
    private int width;
    public DetailImageAdapter(Activity context,  List<Object>  list){
        this.list = list;
        this.context = context;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate( R.layout.jump_detail_image_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            initTop(viewHolder,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (list != null &  list.size() > 0) {
            return list.size();
        }else {
            return 0;
        }
    }

    public void notifyData(List<Map<String,String>> List){
        this.list.addAll(List);
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img;
        public ViewHolder(View mView) {
            super(mView);
            item_img = mView.findViewById(R.id.detail_image);
        }
    }
    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            int screenWidth = width;
            ViewGroup.LayoutParams lp = viewHolder.item_img.getLayoutParams();
            lp.width = screenWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            viewHolder.item_img.setLayoutParams(lp);
            viewHolder.item_img.setMaxWidth(screenWidth);
//            viewHolder.item_img.setMaxHeight((int) (screenWidth * 3.5));// 这里其实可以根据需求而定，我这里测试为最大宽度的3.5倍
//            Glide.with(context)
//                    .load(list.get(position).toString())
//                    .priority(Priority.HIGH)
//                    .thumbnail(1f)
//                    .placeholder(R.mipmap.zw_img_300)
//                    .into(viewHolder.item_img);
            final String url = list.get(position).toString();
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.mipmap.zw_img_300)
		            .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                int imageHeight = resource.getHeight();
                                if(imageHeight > 4096) {
                                    imageHeight = 4096;
                                    ViewGroup.LayoutParams para =  viewHolder.item_img.getLayoutParams();
                                    para.width = ViewGroup.LayoutParams.MATCH_PARENT;
                                    para.height = imageHeight;
                                    viewHolder.item_img.setLayoutParams(para);
                                    Glide.with(context)
                                            .load(url)
                                            .dontAnimate()
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(viewHolder.item_img);
                                }
                                else {
                                    Glide.with(context)
                                            .load(url)
                                            .placeholder(R.mipmap.zw_img_300)
							                .dontAnimate()
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                            .into(viewHolder.item_img);
                                }
                            }

                        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}