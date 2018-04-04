package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.util.JumpIntentUtil;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2017/12/5.
 */

public class HomeTuijianRecyAdapter extends RecyclerView.Adapter{
    private List<Map<String, String>> list;
    private Context context;

    public HomeTuijianRecyAdapter(Context context, List<Map<String, String>> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.home_tuijian_hro_domain, null);
        MyViewHoler viewHoler = new MyViewHoler(view);
        return viewHoler;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHoler viewHoler = (MyViewHoler) holder;
        Map<String, String> map = list.get(position);
        final String imgUrl = map.get("imgUrl");
        String price = map.get("price");
        final String title = map.get("title");
        final String rowkey = map.get("rowkey");
        final String url = map.get("url");
        final String domain = map.get("domain");
        viewHoler.mtitle.setText(title);
        viewHoler.mprice.setText("￥" + price);
        viewHoler.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent;
                if (JumpIntentUtil.isJump1(domain)) {
                    intent = new Intent(context,IntentActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("domain", domain);
                    intent.putExtra("url", url);
                    intent.putExtra("groupRowKey", rowkey);
                }else{
                    intent = new Intent(context,WebViewActivity.class);
                    intent.putExtra("url", url);
                    intent.putExtra("groupRowKey", rowkey);
                }
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(imgUrl).placeholder(R.mipmap.zw_img_300).into(viewHoler.mimg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHoler extends RecyclerView.ViewHolder{
        private ImageView mimg;
        private TextView mtitle,mprice;
        public MyViewHoler(View view) {
            super(view);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            mtitle = (TextView) view.findViewById(R.id.title);
            mprice = (TextView) view.findViewById(R.id.price);
        }
    }
}
