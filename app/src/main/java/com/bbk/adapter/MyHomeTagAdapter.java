package com.bbk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.ButtonListBean;
import com.bbk.Bean.TagBean;
import com.bbk.activity.R;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.NoFastClickUtils;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.logg.Logg;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2018/3/7.
 */

public class MyHomeTagAdapter extends RecyclerView.Adapter {
    private List<TagBean> tagBeans;
    private Context context;
    private userInterface userInterface;
    private JSONArray array;
    private String colorz,colorb;//10个tag字颜色

    public MyHomeTagAdapter(Context context, List<TagBean> tagBeans, JSONArray tagarray,String colorz,String colorb) {
        this.tagBeans = tagBeans;
        this.context = context;
        this.array = tagarray;
        this.colorz = colorz;
        this.colorb = colorb;
    }

    public MyHomeTagAdapter.userInterface getUserInterface() {
        return userInterface;
    }

    public void setUserInterface(MyHomeTagAdapter.userInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.home_tag_item, parent, false));
        return ViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        initTop(viewHolder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (tagBeans != null && tagBeans.size() > 0) {
            return tagBeans.size();
        } else {
            return 0;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_tag)
        ImageView imgTag;
        @BindView(R.id.tv_tag)
        TextView tvTag;
        @BindView(R.id.ll_tag)
        LinearLayout llTag;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final TagBean tagBean = tagBeans.get(position);
            viewHolder.tvTag.setText(tagBean.getName());
            Glide.with(context)
                    .load(tagBean.getImg())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//不加这句会有绿色背景
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(viewHolder.imgTag);
            viewHolder.llTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NoFastClickUtils.isFastClick()){
                        StringUtil.showToast(context,"对不起，您的点击太快了，请休息一下");
                    }else {
                        try {
//                            Logg.e(array.getJSONObject(position).optString("eventId"));
                            EventIdIntentUtil.EventIdIntent(context, array.getJSONObject(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
//            Logg.json(colorz+"===="+colorb);
            if (colorz != null) {
                viewHolder.tvTag.setTextColor(Color.parseColor(colorz));
            }
            if (colorb != null) {
                viewHolder.llTag.setBackgroundColor(Color.parseColor(colorb));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface userInterface {
        void Intent(String name);
    }
}
