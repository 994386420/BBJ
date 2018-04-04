package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.R;
import com.bbk.view.SquareImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2017/11/23.
 */
public class GossipPiazzaImageAdapter extends BaseAdapter{
    private List<String> videoimg;
    private List<String> list;
    private Context context;
    public GossipPiazzaImageAdapter(Context context,List<String> list,List<String> videoimg){
        this.list = list;
        this.context = context;
        this.videoimg = videoimg;
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
            convertView = View.inflate(context, R.layout.gossippiazza_child_image, null);
            vh.mimg = (SquareImageView) convertView.findViewById(R.id.mimg);
            vh.mplay = (ImageView)convertView.findViewById(R.id.mplay);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        if ("add".equals(list.get(position))){
            Glide.with(context)
                    .load(R.mipmap.icon_addpic_unfocused)
                    .placeholder(R.mipmap.zw_img_300)
                    .priority(Priority.HIGH)
                    .into(vh.mimg);
        }else {
            if (list.get(position).contains(".mp4")){
                vh.mplay.setVisibility(View.VISIBLE);
                vh.mplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent Intent = new Intent(context, DesPictureActivity.class);
                        Intent.putStringArrayListExtra("list", (ArrayList<String>) list);
                        Intent.putExtra("position", position);
                        context.startActivity(Intent);
                    }
                });
                String path = list.get(position);
                SquareImageView mim = vh.mimg;

                if (videoimg.size()==0){

                }else {
                    Glide.with(context)
                            .load(videoimg.get(0))
                            .placeholder(R.mipmap.zw_img_300)
                            .priority(Priority.HIGH)
                            .into(vh.mimg);
                }
            }else {
                Glide.with(context)
                        .load(list.get(position))
                        .placeholder(R.mipmap.zw_img_300)
                        .priority(Priority.HIGH)
                        .into(vh.mimg);
            }
        }
        return convertView;
    }
    class ViewHolder {
        SquareImageView mimg;
        ImageView mplay;
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if (msg.what == 1){
               Map<String,Object> map = (Map<String, Object>) msg.obj;
               SquareImageView mimg = (SquareImageView)map.get("view");
               Bitmap bitmap = (Bitmap) map.get("img");
               mimg.setImageBitmap(bitmap);
           }
        }
    };
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)

    private Bitmap createVideoThumbnail(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int max = Math.max(width, height);
            if (max > 512) {
                float scale = 512f / max;
                int w = Math.round(scale * width);
                int h = Math.round(scale * height);
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
            }else {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
        }
        return bitmap;
    }
}
