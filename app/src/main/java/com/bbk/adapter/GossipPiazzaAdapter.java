package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.bbk.Bean.BiaoLiaoBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.chat.model.Conversation;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView;
import com.bbk.view.CircleImageView1;
import com.bbk.view.ExpandableTextView;
import com.bbk.view.MyGridView;
import com.bbk.view.StretchyTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2017/11/23.
 */
public class GossipPiazzaAdapter extends RecyclerView.Adapter implements ResultEvent,View.OnClickListener{
    private Context context;
    private List<BiaoLiaoBean> biaoLiaoBeans;
    private ImageView zanimg11 = null;
    private TextView mzantext11 = null;
    private int positionnum;
    private OnItemClickListener mOnItemClickListener = null;

    public GossipPiazzaAdapter(Context context, List<BiaoLiaoBean> biaoLiaoBeans){
        this.context = context;
        this.biaoLiaoBeans = biaoLiaoBeans;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    public void notifyData(List<BiaoLiaoBean> biaoLiaoBeans){
        if (biaoLiaoBeans != null){
            this.biaoLiaoBeans.addAll(biaoLiaoBeans);
            notifyDataSetChanged();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gossippiazza_child, parent, false);
        MyViewHolder MyViewHolder = new MyViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return MyViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final int position22 = position;
            final MyViewHolder viewHolder = (MyViewHolder) holder;
            //将position保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);
            viewHolder.mcontent.setContent(biaoLiaoBeans.get(position).getContent(),biaoLiaoBeans.get(position).getUrl());
            viewHolder.mname.setText(biaoLiaoBeans.get(position).getUsername());
            viewHolder.mtime.setText(biaoLiaoBeans.get(position).getDtime());
            viewHolder.mreadnum.setText(biaoLiaoBeans.get(position).getReadnum()+"阅读");
            viewHolder.itemlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GossipPiazzaDetailActivity.class);
                    intent.putExtra("blid",biaoLiaoBeans.get(position).getBlid());
                    context.startActivity(intent);
                }
            });
            if (!"0".equals(biaoLiaoBeans.get(position).getZannum())){
                viewHolder.mzantext.setText(biaoLiaoBeans.get(position).getZannum());
            }else {
                viewHolder.mzantext.setText("赞");
            }
            if (!"0".equals(biaoLiaoBeans.get(position).getPlnum())){
                viewHolder.mpingluntext.setText(biaoLiaoBeans.get(position).getPlnum());
            }else {
                viewHolder.mpingluntext.setText("评论");
            }
            final String iszan = biaoLiaoBeans.get(position).getIsZan();
            final String content = biaoLiaoBeans.get(position).getContent();
            if (!"0".equals(iszan)){
                viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan2);
            }else {
                viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan);
            }
            final String blid =biaoLiaoBeans.get(position).getBlid();
            final String title = biaoLiaoBeans.get(position).getTitle();
            final String content1 = biaoLiaoBeans.get(position).getContent();
            viewHolder.mzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
                    if (TextUtils.isEmpty(userID)){
                        Intent intent = new Intent(context, UserLoginNewActivity.class);
                        context.startActivity(intent);
                    }else {
                        if ("0".equals(iszan)){
                            zanimg11 = viewHolder.mzanimg;
                            mzantext11 = viewHolder.mzantext;
                            positionnum = position22;
                            DataFlow dataFlow = new DataFlow(context);
                            Map<String,String> map1 = new HashMap<String, String>();

                            map1.put("userid",userID);
                            map1.put("blid",blid);
                            dataFlow.requestData(1,"newService/insertDianZan",map1,GossipPiazzaAdapter.this,true);

                        }else {

                        }
                    }
                }
            });
            viewHolder.mzhuanfa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
                    String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?"+"blid="+blid;
                    ShareUtil.showShareDialog(v,(Activity) context,"专业的网购比价、导购平台",title,url,"");
                }
            });
            CircleImageView1.getImg(context,biaoLiaoBeans.get(position).getHeadimg(),viewHolder.mimg);
//            Glide.with(context).load(map.get("headimg")).into(new SimpleTarget<GlideDrawable>() {
//                @Override
//                public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                    viewHolder.mimg.setImageDrawable(glideDrawable);
//                }
//            });
            String imgs = biaoLiaoBeans.get(position).getImgs();
            JSONArray array = new JSONArray(imgs);
            if (array.length()!= 0){
                final List<String> imglist = new ArrayList<>();
                for (int i = 0; i <array.length() ; i++) {
                    String images = array.optString(i);
                    imglist.add(images);
                }
                List<String> videoimg = new ArrayList<String>();
                videoimg.add(biaoLiaoBeans.get(position).getVideo());
                recyGrid(viewHolder,imglist,videoimg);
                viewHolder.mrecy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent Intent = new Intent(context, DesPictureActivity.class);
                        Intent.putStringArrayListExtra("list", (ArrayList<String>) imglist);
                        Intent.putExtra("position",position);
                        context.startActivity(Intent);

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return biaoLiaoBeans.size();
    }

    private void recyGrid(MyViewHolder viewHolder, List<String> imglist, List<String> videoimg){
        WindowManager wm = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.mrecy.getLayoutParams();
        //根据图片个数来设置布局
        if (imglist.size() == 1){
            viewHolder.mrecy.setNumColumns(1);
            layoutParams.width = width/2;
        }else if(imglist.size() == 2){
            viewHolder.mrecy.setNumColumns(2);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }else if (imglist.size() == 4){
            viewHolder.mrecy.setNumColumns(2);
            layoutParams.width = width*2/3;
        }else {
            viewHolder.mrecy.setNumColumns(3);
            layoutParams.width = GridLayoutManager.LayoutParams.MATCH_PARENT;
        }
        viewHolder.mrecy.setLayoutParams(layoutParams);
        GossipPiazzaImageAdapter adapter = new GossipPiazzaImageAdapter(context,imglist,videoimg);
        viewHolder.mrecy.setAdapter(adapter);
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        if (zanimg11!= null){
            if ("1".equals(dataJo.optString("status"))){
                zanimg11.setImageResource(R.mipmap.bl_zan2);
//                list.get(positionnum).put("isZan","1");
                String zannum = biaoLiaoBeans.get(positionnum).getZannum();
                Integer i = Integer.valueOf(zannum);
                biaoLiaoBeans.get(positionnum).setIsZan("1");
                biaoLiaoBeans.get(positionnum).setZannum((i+1)+"");
//                list.get(positionnum).put("zannum",(i+1)+"");
//                mzantext11.setText((i+1)+"");
                notifyDataSetChanged();
            }else {

            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        MyGridView mrecy;
        StretchyTextView mcontent;
//        CircleImageView mimg;
        ImageView mzanimg,mimg;
        TextView mname,mtime,mreadnum,mzantext,mpingluntext,mzhuanfatext;
        RelativeLayout mpinglun,mzan,mzhuanfa;
        LinearLayout itemlayout;
        public MyViewHolder(View view) {
            super(view);
            mrecy = (MyGridView) view.findViewById(R.id.mrecy);
            mcontent = (StretchyTextView) view.findViewById(R.id.mcontent);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            mzanimg = (ImageView) view.findViewById(R.id.mzanimg);
            mname = (TextView) view.findViewById(R.id.mname);
            mtime = (TextView) view.findViewById(R.id.mtime);
            mreadnum = (TextView) view.findViewById(R.id.mreadnum);
            mzantext = (TextView) view.findViewById(R.id.mzantext);
            mpingluntext = (TextView) view.findViewById(R.id.mpingluntext);
            mzhuanfatext = (TextView) view.findViewById(R.id.mzhuanfatext);
            mpinglun = (RelativeLayout) view.findViewById(R.id.mpinglun);
            mzan = (RelativeLayout) view.findViewById(R.id.mzan);
            mzhuanfa = (RelativeLayout) view.findViewById(R.id.mzhuanfa);
            itemlayout = view.findViewById(R.id.result_item);
        }
    }

}
