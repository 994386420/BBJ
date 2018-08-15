package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.Bean.PlBean;
import com.bbk.Bean.ShopDianpuBean;
import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyGridView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by rtj on 2018/3/7.
 */

public class MyShopPLAdapter extends RecyclerView.Adapter {
    private List<PlBean> plBeans;
    private Context context;

    public MyShopPLAdapter(Context context, List<PlBean> plBeans) {
        this.plBeans = plBeans;
        this.context = context;
    }
    public void notifyData(List<PlBean> plBeans) {
        this.plBeans.addAll(plBeans);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder ViewHolder = new ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.my_shop_pl_listview, parent, false));
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
        return plBeans.size();
    }

    private void recyGrid(ViewHolder viewHolder, List<String> imglist, List<String> videoimg) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.mgridview.getLayoutParams();
        //根据图片个数来设置布局
        if (imglist.size() == 1) {
            viewHolder.mgridview.setNumColumns(1);
            layoutParams.width = width / 2;
        } else if (imglist.size() == 2) {
            viewHolder.mgridview.setNumColumns(2);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        } else if (imglist.size() == 4) {
            viewHolder.mgridview.setNumColumns(2);
            layoutParams.width = width * 2 / 3;
        } else {
            viewHolder.mgridview.setNumColumns(3);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }
        viewHolder.mgridview.setLayoutParams(layoutParams);
        GossipPiazzaImageAdapter adapter = new GossipPiazzaImageAdapter(context, imglist, videoimg);
        viewHolder.mgridview.setAdapter(adapter);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mimg)
        ImageView mimg;
        @BindView(R.id.mname)
        TextView mname;
        @BindView(R.id.mratingbar)
        MaterialRatingBar mratingbar;
        @BindView(R.id.mcontent)
        TextView mcontent;
        @BindView(R.id.mgridview)
        MyGridView mgridview;
        @BindView(R.id.mtime)
        TextView mtime;

        public ViewHolder(View mView) {
            super(mView);
            ButterKnife.bind(this, mView);
        }
    }

    private void initTop(final ViewHolder viewHolder, final int position) {
        try {
            final PlBean plBean = plBeans.get(position);
            viewHolder.mname.setText(plBean.getName());
            viewHolder.mratingbar.setRating(Float.valueOf("5"));
            viewHolder.mcontent.setText(plBean.getContent());
            viewHolder.mtime.setText(plBean.getTime()+" "+plBean.getParam());
            CircleImageView1.getImg(context, plBean.getImg(), viewHolder.mimg);
            if (plBean.getPlimgs() == null) {
                viewHolder.mgridview.setVisibility(View.GONE);
            } else {
                JSONArray array = new JSONArray(plBean.getPlimgs());
                if (array.length() != 0) {
                    final List<String> imglist = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        String images = array.optString(i);
                        imglist.add(images);
                    }
                    List<String> videoimg = new ArrayList<String>();
                    recyGrid(viewHolder, imglist, videoimg);
                    viewHolder.mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent Intent = new Intent(context, DesPictureActivity.class);
                            Intent.putStringArrayListExtra("list", (ArrayList<String>) imglist);
                            Intent.putExtra("position", position);
                            context.startActivity(Intent);

                        }
                    });
                } else {
                    viewHolder.mgridview.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
