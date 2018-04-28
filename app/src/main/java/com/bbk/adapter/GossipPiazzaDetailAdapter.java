package com.bbk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bbk.activity.DesPictureActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyGridView;
import com.bbk.view.StretchyTextView;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by rtj on 2017/11/23.
 */
public class GossipPiazzaDetailAdapter extends RecyclerView.Adapter implements View.OnClickListener,ResultEvent{
    private Context context;
    private List<Map<String,String>> list;
    private OnItemClickListener mOnItemClickListener = null;
    private Map<String, String> map;
    private EditText msgEdittext;
    private ImageView zanimg11 = null;
    private TextView mzantext11 = null;

    public GossipPiazzaDetailAdapter(Context context, Map<String, String> map, List<Map<String, String>> list, EditText msgEdittext){
        this.context = context;
        this.list = list;
        this.map = map;
        this.msgEdittext = msgEdittext;
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
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 ){
            return 1;
        }else {
            return 2;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1){
            View view = LayoutInflater.from(context).inflate(R.layout.gossippiazza_detail_top, parent, false);
            TopViewHolder topViewHolder = new TopViewHolder(view);
            return topViewHolder;
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.gossippiazza_detail_end, parent, false);
            EndViewHolder endViewHolder = new EndViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return endViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof TopViewHolder){
                TopViewHolder viewHolder = (TopViewHolder) holder;
                initTop(viewHolder);
            }else if (holder instanceof EndViewHolder){
                EndViewHolder viewHolder = (EndViewHolder) holder;
                viewHolder.itemView.setTag(position-1);
                initEnd(viewHolder,position-1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initTop(final TopViewHolder viewHolder) throws JSONException {
        final String content =  map.get("content");
        String imgs =  map.get("imgs");
        String username =  map.get("username");
        String zan =  map.get("zan");
        final String iszan =  map.get("iszan");
        String headimg =  map.get("headimg");
        String dtime =  map.get("dtime");
        String plnum =  map.get("plnum");
        String zannum =  map.get("zannum");
        final String blid =  map.get("blid");
        String readnum =  map.get("readnum");
        final String url =  map.get("url");
        if (content!= null){
            SpannableString msp = new SpannableString(content+"  购买链接");
            msp.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url",url);
                    context.startActivity(intent);
                }
            },content.length()+2, content.length()+6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.mcontent.setText(msp, TextView.BufferType.SPANNABLE);
            viewHolder.mcontent.setMovementMethod(LinkMovementMethod.getInstance());//激活链接
        }
        CircleImageView1.getImg(context,headimg,viewHolder.mimg);
        viewHolder.mname.setText(username);
        viewHolder.mtime.setText(dtime);
        viewHolder.mpingluntext.setText(plnum);
        viewHolder.mpingluntexttitle.setText("评论（"+plnum+"）");
        viewHolder.mzantext.setText(zannum);
        viewHolder.mreadnum.setText(readnum+"阅读");
        if (!"0".equals(map.get("zannum"))){
            viewHolder.mzantext.setText(map.get("zannum"));
        }else {
            viewHolder.mzantext.setText("赞");
        }
        if (!"0".equals(map.get("plnum"))){
            viewHolder.mpingluntext.setText(map.get("plnum"));
        }else {
            viewHolder.mpingluntext.setText("评论");
        }
        if (!"0".equals(iszan)){
            viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan2);
        }else {
            viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan);
        }
        viewHolder.mpinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pupopSoftKey();
            }
        });
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
                        DataFlow dataFlow = new DataFlow(context);
                        Map<String,String> map1 = new HashMap<String, String>();
                        Log.i("blid","===="+blid);
                        map1.put("userid",userID);
                        map1.put("blid",blid);
                        dataFlow.requestData(1,"newService/insertDianZan",map1,GossipPiazzaDetailAdapter.this,true);

                    }else {

                    }
                }
            }
        });
        if (imgs!= null){
            JSONArray array = new JSONArray(imgs);
            if (array.length()!= 0) {
                final List<String> imglist = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String images = array.optString(i);
                    imglist.add(images);
                }
                List<String> videoimg = new ArrayList<String>();
                videoimg.add(map.get("video"));
                recyGrid(viewHolder, imglist, videoimg);
                viewHolder.mrecy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent Intent = new Intent(context, DesPictureActivity.class);
                        Intent.putStringArrayListExtra("list", (ArrayList<String>) imglist);
                        Intent.putExtra("position", position);
                        context.startActivity(Intent);

                    }
                });
            }
            viewHolder.mzhuanfa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
                    String url = Constants.MAIN_BASE_URL_MOBILE +"mobile/blbar?"+"blid="+blid;
                    ShareUtil.showShareDialog(v,(Activity) context,"专业的网购比价、导购平台",content,url,"");
                }
            });
        }

    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        if (zanimg11!= null){
            if ("1".equals(dataJo.optString("status"))){
                zanimg11.setImageResource(R.mipmap.bl_zan2);
                map.put("iszan","1");
                String zannum = map.get("zannum");
                Integer i = Integer.valueOf(zannum);
                map.put("zannum",(i+1)+"");
//                mzantext11.setText((i+1)+"");
                notifyDataSetChanged();
            }else {

            }
        }
    }
    private void initEnd(EndViewHolder viewHolder, int position) {
        if (list.size()!= 0){
            Map<String, String> map = list.get(position);
            String content = map.get("content");
            String uid = map.get("uid");
            String time = map.get("time");
            String iszan = map.get("iszan");
            String zan = map.get("zan");
            String plid = map.get("plid");
            String name = map.get("name");
            String img = map.get("img");
            String lou = map.get("lou");
            String sublist = map.get("sublist");
            String count = map.get("count");
            viewHolder.mcontent.setText(content);
            viewHolder.mname.setText(name);
            viewHolder.mtime.setText(time);
            viewHolder.mzantext.setText(zan);
            CircleImageView1.getImg(context,img,viewHolder.mimg);
            if (!"0".equals(iszan)){
                viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan2);
            }else {
                viewHolder.mzanimg.setImageResource(R.mipmap.bl_zan);
            }
            int c = Integer.valueOf(count);
            if (c>0){
                viewHolder.mcount.setVisibility(View.VISIBLE);
                viewHolder.mcount.setText("有"+count+"条回复");

            }else {
                viewHolder.mcount.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    private void recyGrid(TopViewHolder viewHolder, List<String> imglist, List<String> videoimg){
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

    private void pupopSoftKey(){
        msgEdittext.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 开启或者关闭软键盘
        imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_FORCED);// 弹出软键盘时，焦点定位在输入框中
    }

    class TopViewHolder extends RecyclerView.ViewHolder{
        TextView mcontent,mname,mtime,mreadnum,mzantext,mpingluntext,mzhuanfatext,mpingluntexttitle;
        MyGridView mrecy;
        ImageView mzanimg,mimg;
        RelativeLayout mpinglun,mzan,mzhuanfa;
        public TopViewHolder(View view) {
            super(view);
            mrecy = (MyGridView) view.findViewById(R.id.mrecy);
            mcontent = (TextView) view.findViewById(R.id.mcontent);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            mname = (TextView) view.findViewById(R.id.mname);
            mtime = (TextView) view.findViewById(R.id.mtime);
            mreadnum = (TextView) view.findViewById(R.id.mreadnum);
            mrecy = (MyGridView) view.findViewById(R.id.mrecy);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            mzanimg = (ImageView) view.findViewById(R.id.mzanimg);
            mname = (TextView) view.findViewById(R.id.mname);
            mtime = (TextView) view.findViewById(R.id.mtime);
            mreadnum = (TextView) view.findViewById(R.id.mreadnum);
            mzantext = (TextView) view.findViewById(R.id.mzantext);
            mpingluntext = (TextView) view.findViewById(R.id.mpingluntext);
            mpingluntexttitle = (TextView) view.findViewById(R.id.mpingluntexttitle);
            mzhuanfatext = (TextView) view.findViewById(R.id.mzhuanfatext);
            mpinglun = (RelativeLayout) view.findViewById(R.id.mpinglun);
            mzan = (RelativeLayout) view.findViewById(R.id.mzan);
            mzhuanfa = (RelativeLayout) view.findViewById(R.id.mzhuanfa);
        }
    }

    class EndViewHolder extends RecyclerView.ViewHolder{
        ImageView mimg,mzanimg;
        TextView mname,mtime,mcount,mcontent,mzantext,mpingluntext;
        public EndViewHolder(View view) {
            super(view);
            mcontent = (TextView) view.findViewById(R.id.mcontent);
            mimg = (ImageView) view.findViewById(R.id.mimg);
            mzanimg = (ImageView) view.findViewById(R.id.mzanimg);
            mname = (TextView) view.findViewById(R.id.mname);
            mtime = (TextView) view.findViewById(R.id.mtime);
            mcount = (TextView) view.findViewById(R.id.mcount);
            mzantext = (TextView) view.findViewById(R.id.mzantext);
            mpingluntext = view.findViewById(R.id.mpingluntext);
        }
    }

}
