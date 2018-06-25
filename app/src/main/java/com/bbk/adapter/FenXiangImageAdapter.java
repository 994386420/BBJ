package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.Bean.FenXiangItemBean;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.R;
import com.bbk.view.SquareImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rtj on 2017/11/23.
 */
public class FenXiangImageAdapter extends BaseAdapter {
    private List<FenXiangItemBean> fenXiangItemBeans;
    private Context context;

    public FenXiangImageAdapter(Context context, List<FenXiangItemBean> fenXiangItemBeans) {
        this.context = context;
        this.fenXiangItemBeans = fenXiangItemBeans;
    }

    @Override
    public int getCount() {
        return fenXiangItemBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return fenXiangItemBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.fenxiang_item_image, null);
            vh = new ViewHolder(convertView);
            vh.mimg = (SquareImageView) convertView.findViewById(R.id.mimg);
            vh.mplay = (ImageView) convertView.findViewById(R.id.mplay);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Glide.clear(vh.mimg);
        Glide.with(context)
                .load(fenXiangItemBeans.get(position).getImg())
                .placeholder(R.mipmap.zw_img_300)
                .priority(Priority.HIGH)
                .into(vh.mimg);
        if (fenXiangItemBeans.get(position).getPrice() != null) {
            vh.price.setText(fenXiangItemBeans.get(position).getPrice());
        }
        if (fenXiangItemBeans.get(position).getZuan() != null) {
            vh.zuan.setText(fenXiangItemBeans.get(position).getZuan().replace("预估", ""));
        }
        vh.flItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, IntentActivity.class);
                if (fenXiangItemBeans.get(position).getRequestUrl() != null) {
                    intent.putExtra("url", fenXiangItemBeans.get(position).getRequestUrl());
                }
                if (fenXiangItemBeans.get(position).getTitle() != null) {
                    intent.putExtra("title", fenXiangItemBeans.get(position).getTitle());
                }
                if (fenXiangItemBeans.get(position).getDomain() != null) {
                    intent.putExtra("domain", fenXiangItemBeans.get(position).getDomain());
                }
                if (fenXiangItemBeans.get(position).getRowkey() != null) {
                    intent.putExtra("groupRowKey", fenXiangItemBeans.get(position).getRowkey());
                }
                intent.putExtra("isczg", "1");
                if (fenXiangItemBeans.get(position).getBprice() != null) {
                    intent.putExtra("bprice", fenXiangItemBeans.get(position).getBprice());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.zuan)
        TextView zuan;
        SquareImageView mimg;
        ImageView mplay;
        @BindView(R.id.fl_item_layout)
        FrameLayout flItemLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
