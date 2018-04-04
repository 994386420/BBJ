package com.bbk.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.R;
import com.bbk.view.SquareImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rtj on 2017/11/23.
 */
public class MyGossipGirdAdapter extends BaseAdapter{
    private List<String> litlelist = new ArrayList<>();
    private List<String> list;
    private Context context;
    public MyGossipGirdAdapter(Context context, List<String> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(context, R.layout.mygossip_grid_image, null);
            vh.mimg = (SquareImageView) convertView.findViewById(R.id.mimg);
            vh.mcancle = (ImageView) convertView.findViewById(R.id.mcancle);
            vh.mplay = (ImageView) convertView.findViewById(R.id.mplay);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                if (list.size() == 8){
                    if (!"add".equals(list.get(7))){
                        list.add("add");
                    }
                }
                notifyDataSetChanged();
            }
        });
        vh.mplay.setVisibility(View.GONE);
        if ("add".equals(list.get(position))){
            vh.mcancle.setVisibility(View.GONE);
            Glide.with(context)
                    .load(R.mipmap.icon_addpic_unfocused)
                    .priority(Priority.HIGH)
                    .into(vh.mimg);
        }else {
            vh.mcancle.setVisibility(View.VISIBLE);
            if (list.get(position).contains(".mp4")){
                vh.mplay.setVisibility(View.VISIBLE);
                vh.mplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        litlelist.clear();
                        litlelist.addAll(list);
                        if ("add".equals(list.get(litlelist.size() - 1))) {
                            litlelist.remove(litlelist.size() - 1);
                        }
                        Intent Intent = new Intent(context, DesPictureActivity.class);
                        Intent.putStringArrayListExtra("list", (ArrayList<String>) litlelist);
                        Intent.putExtra("position", position);
                        context.startActivity(Intent);
                    }
                });
                String path = list.get(position);
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                vh.mimg.setImageBitmap(bitmap);
//                MediaMetadataRetriever media =new MediaMetadataRetriever();
//                media.setDataSource(list.get(position).trim());
//                Bitmap bitmap = media.getFrameAtTime();
//                vh.mimg.setImageBitmap(bitmap);
            }else {
                Glide.with(context)
                        .load(list.get(position))
                        .crossFade()
                        .priority(Priority.HIGH)
                        .into(vh.mimg);
            }

        }
        return convertView;
    }
    class ViewHolder {
        SquareImageView mimg;
        ImageView mcancle,mplay;
    }
}
