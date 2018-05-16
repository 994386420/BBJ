package com.bbk.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.Bean.HomeData;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.DataFragmentActivity;
import com.bbk.activity.DomainMoreActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultDialogActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.fragment.OnClickListioner;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyXRefresh;
import com.bbk.view.RollHeaderView3;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bbk.view.selecttableview.SelectableTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2017/12/1.
 */

public class NewHomeAdapter extends RecyclerView.Adapter implements ResultEvent, View.OnClickListener{
    private Context context;
    private List<Map<String, String>> taglist,list,mList,mAddList;
    private JSONArray tag, gongneng,fabiao,banner;
    private int ITEM_TYPE_TOP = 1; //顶部 banner,tag,util
    private int ITEM_TYPE_TUIJIAN = 2; //为你推荐
    private DataFlow dataFlow;
    private String wztitle = "";
    private OnItemClickListener mOnItemClickListener = null;
    private String type = "1";
    private boolean isclear = false;
    private int page = 1,x = 1;
    JSONArray array;
    private OnClickListioner onClickListioner;
    private NewBjAdapter adapter;
    private NewBlAdapter mBlAdapter;
    private NewCzgAdapter mCzgAdapter;
    private NewFxAdapter mFxAdapter;
    private RecyclerView mlistview;
    TopViewHolder topViewHolder;
    String flag= "1";


    public NewHomeAdapter(Context context, List<Map<String, String>> taglist, List<Map<String, String>> list, JSONArray banner,JSONArray tag,JSONArray fabiao,JSONArray gongneng) {
        this.context = context;
        this.tag =tag;
        this.gongneng = gongneng;
        this.fabiao = fabiao;
        this.dataFlow = new DataFlow(context);
        this.taglist = taglist;
        this.banner = banner;
//        this.list = list;
//        this.topcount = 3 + articles.length() + tujian.length();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnClickListioner(OnClickListioner onClickListioner) {
        this.onClickListioner = onClickListioner;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public void notifyData(List<Map<String, String>> List){
        if (List != null){
            mCzgAdapter.notifyData(List);
        }
    }
    public void notifyBjData(List<Map<String, String>> List){
        if (List != null) {
            adapter.notifyData(List);
        }
    }
    public void notifyBlData(List<Map<String, String>> List){
        if (List != null) {
            mBlAdapter.notifyData(List);
        }
    }
    public void notifyFxData(List<Map<String, String>> List){
        if (List != null) {
            mFxAdapter.notifyData(List);
        }
    }
    public void notifyData1(List<Map<String, String>> List){
        if (mlistview != null && List != null) {
            mCzgAdapter = new NewCzgAdapter(context, List);
            mlistview.setAdapter(mCzgAdapter);
        }
    }
    public void notifyBjData1(List<Map<String, String>> List){
        if (mlistview != null && List != null) {
            adapter = new NewBjAdapter(context, List);
            mlistview.setAdapter(adapter);
        }
    }
    public void notifyBlData1(List<Map<String, String>> List){
        if (mlistview != null && List != null) {
            mBlAdapter = new NewBlAdapter(context, List);
            mlistview.setAdapter(mBlAdapter);
        }
    }
    public void notifyFxData1(List<Map<String, String>> List){
        if (mlistview != null && List != null) {
            mFxAdapter = new NewFxAdapter(context, List);
            mlistview.setAdapter(mFxAdapter);
        }
    }

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_TOP;
        } else if (position >= 1) {
            return ITEM_TYPE_TUIJIAN;
        }else {
            return ITEM_TYPE_TOP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_TOP ){
                TopViewHolder TopViewHolder = new TopViewHolder(
                        LayoutInflater.from(context).inflate(R.layout.fragment_home_top, parent, false));
                return TopViewHolder;
            }else if(viewType == ITEM_TYPE_TUIJIAN){
                    ViewHolder ViewHolder = new ViewHolder(
                            LayoutInflater.from(context).inflate(R.layout.recyclerlayout, parent, false));
                    return ViewHolder;
            }else {
                return null;
            }
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof TopViewHolder) {
                TopViewHolder viewHolder = (TopViewHolder) holder;
                initTop(viewHolder);
            } else if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                initTop(viewHolder, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTop(final TopViewHolder viewHolder)  {
        try {
            loadbanner(banner, viewHolder);
            loadTag(tag, viewHolder);
            loadViewflipper(fabiao,viewHolder);
            viewHolder.compareutil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BidHomeActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.queryhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, QueryHistoryActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.jingtopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.initone();
                }
            });
            if (gongneng.length() >= 2) {
                Glide.with(context).
                        load(gongneng.getJSONObject(0).optString("img")).
                        placeholder(R.mipmap.bjsq).into(viewHolder.compareimg);
                Glide.with(context).load(gongneng.getJSONObject(1).optString("img")).placeholder(R.mipmap.lsyg).into(viewHolder.queryhistoryimg);
            }
            viewHolder.mLlCzgLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "1";
                    setView(viewHolder);
//                    mIdex("1",2,true);
                    setText(viewHolder.mCzgText,viewHolder.mCzgView);
                    onClickListioner.onCzgClick();
                }
            });
            viewHolder. mLlbjLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "2";
                    setView(viewHolder);
//                    mIdex("2",2,true);
                    setText(viewHolder.mBjText,viewHolder.mBjView);
                    onClickListioner.onBjClick();
                }
            });
            viewHolder.mLlblLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "3";
                    setView(viewHolder);
//                    mIdex("3",2,true);
                    setText(viewHolder.mBlText,viewHolder.mBlView);
                    onClickListioner.onBlClick();
                }
            });
            viewHolder.mLlfxLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type = "4";
                    setView(viewHolder);
//                    mIdex("4",2,true);
                    setText(viewHolder.mFxText,viewHolder.mFxView);
                    onClickListioner.onFxClick();
                }
            });
            viewHolder.mSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchMainActivity.class);
                    context.startActivity(intent);
                }
            });
            viewHolder.mSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SortActivity.class);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //超值购等数据
    private void mIdex(String str,int code,boolean is){
        type = str;
        x=1;
        page=1;
        isclear = true;
        getIndexByType(is,code);
    }
    //首页分类数据
    private void getIndexByType(boolean is,int code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type",type);
        paramsMap.put("page",page+"");
        dataFlow.requestData(code, Constants.GetQueryAppIndexByType, paramsMap, this, is);
    }
    private void setView(TopViewHolder topViewHolder){
        topViewHolder.mCzgText.setTextColor(context.getResources().getColor(R.color.color_line_text));
        topViewHolder.mCzgView.setVisibility(View.GONE);
        topViewHolder.mBjText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mQbText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder. mBjView.setVisibility(View.GONE);
        topViewHolder.mBlText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mDfkText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder.mBlView.setVisibility(View.GONE);
        topViewHolder.mFxText.setTextColor(context.getResources().getColor(R.color.color_line_text));
//            mDfhText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        topViewHolder.mFxView.setVisibility(View.GONE);
    }
    private void setText(TextView text,View view){
        text.setTextColor(context.getResources().getColor(R.color.color_line_top));
        view.setVisibility(View.VISIBLE);
    }
    class TopViewHolder extends RecyclerView.ViewHolder {
        //        private GridView mgridView;
        private ViewFlipper mviewflipper;
        private LinearLayout queryhistory, compareutil, jingtopic, box1, box2, box3, box4, box5;
        private TextView text1, text2, text3, text4, text5;
        private ImageView img1, img2, img3, img4, img5, queryhistoryimg, compareimg;
        private Banner mBanner;
        private LinearLayout mLlCzgLayout,mLlbjLayout,mLlblLayout,mLlfxLayout;
        private View mCzgView,mBjView,mBlView,mFxView;
        private TextView mCzgText,mBjText,mBlText,mFxText;
        private LinearLayout mSort,mSearch;//搜索，分类;

        public TopViewHolder(View mView) {
            super(mView);
//            mgridView = (GridView) mView.findViewById(R.id.mgridView);
            mBanner = mView.findViewById(R.id.banner);
            mSearch = mView.findViewById(R.id.msearch);
            mSort = mView.findViewById(R.id.msort);
            mBanner = (Banner) mView.findViewById(R.id.banner);
            mviewflipper = (ViewFlipper) mView.findViewById(R.id.mviewflipper);
            queryhistory = (LinearLayout) mView.findViewById(R.id.queryhistory);
            compareutil = (LinearLayout) mView.findViewById(R.id.compareutil);
            jingtopic = (LinearLayout) mView.findViewById(R.id.jingtopic);
            box1 = (LinearLayout) mView.findViewById(R.id.box1);
            box2 = (LinearLayout) mView.findViewById(R.id.box2);
            box3 = (LinearLayout) mView.findViewById(R.id.box3);
            box4 = (LinearLayout) mView.findViewById(R.id.box4);
            box5 = (LinearLayout) mView.findViewById(R.id.box5);
            text1 = (TextView) mView.findViewById(R.id.text1);
            text2 = (TextView) mView.findViewById(R.id.text2);
            text3 = (TextView) mView.findViewById(R.id.text3);
            text4 = (TextView) mView.findViewById(R.id.text4);
            text5 = (TextView) mView.findViewById(R.id.text5);
            img1 = (ImageView) mView.findViewById(R.id.img1);
            img2 = (ImageView) mView.findViewById(R.id.img2);
            img3 = (ImageView) mView.findViewById(R.id.img3);
            img4 = (ImageView) mView.findViewById(R.id.img4);
            img5 = (ImageView) mView.findViewById(R.id.img5);
            queryhistoryimg = (ImageView) mView.findViewById(R.id.queryhistoryimg);
            compareimg = (ImageView) mView.findViewById(R.id.compareimg);
                    mLlCzgLayout = mView.findViewById(R.id.ll_czg_layout);
        mLlbjLayout = mView.findViewById(R.id.ll_bj_layout);
        mLlblLayout = mView.findViewById(R.id.ll_bl_layout);
        mLlfxLayout = mView.findViewById(R.id.ll_fx_layout);
        mCzgText = mView.findViewById(R.id.czg_text);
        mBjText = mView.findViewById(R.id.bj_text);
        mBlText = mView.findViewById(R.id.bl_text);
        mFxText = mView.findViewById(R.id.fx_text);
        mCzgView = mView.findViewById(R.id.czg_view);
        mBjView = mView.findViewById(R.id.bj_view);
        mBlView = mView.findViewById(R.id.bl_view);
        mFxView = mView.findViewById(R.id.fx_view);
        }
    }

    private void loadTag(final JSONArray tag, TopViewHolder viewHolder) throws Exception {
        taglist.clear();
        for (int i = 0; i < tag.length(); i++) {
            JSONObject object = tag.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            String htmlUrl = object.optString("htmlUrl");
            String eventId = object.optString("eventId");
            String img = object.optString("img");
            String name = object.optString("name");
            map.put("htmlUrl", htmlUrl);
            map.put("eventId", eventId);
            map.put("text", name);
            map.put("imageUrl", img);
            taglist.add(map);
        }
        List<ImageView> imglist = new ArrayList<>();
        List<TextView> textlist = new ArrayList<>();
        List<LinearLayout> boxlist = new ArrayList<>();
        imglist.add(viewHolder.img1);
        imglist.add(viewHolder.img2);
        imglist.add(viewHolder.img3);
        imglist.add(viewHolder.img4);
        imglist.add(viewHolder.img5);
        textlist.add(viewHolder.text1);
        textlist.add(viewHolder.text2);
        textlist.add(viewHolder.text3);
        textlist.add(viewHolder.text4);
        textlist.add(viewHolder.text5);
        boxlist.add(viewHolder.box1);
        boxlist.add(viewHolder.box2);
        boxlist.add(viewHolder.box3);
        boxlist.add(viewHolder.box4);
        boxlist.add(viewHolder.box5);
        for (int i = 0; i < boxlist.size(); i++) {
            final int position = i;
            TextView textView = textlist.get(position);
            ImageView imageView = imglist.get(position);
            Map<String, String> map = taglist.get(position);
            String text = map.get("text").toString();
            textlist.get(position).setText(text);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            String imageUrl = map.get("imageUrl").toString();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.mipmap.zw_img_160)
                    .thumbnail(0.5f)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 4) {
                        Intent intent = new Intent(context, DataFragmentActivity.class);
                        context.startActivity(intent);
                    } else {
                        try {
                            EventIdIntentUtil.EventIdIntent(context, tag.getJSONObject(position));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void loadViewflipper(JSONArray fabiao,TopViewHolder viewHolder) throws JSONException {
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        for (int i = 0; i < fabiao.length(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.flipper_bidhome, null);
            TextView mtitle = view.findViewById(R.id.mtitle);
            TextView mbuyprice = view.findViewById(R.id.mbuyprice);
            TextView msellprice = view.findViewById(R.id.msellprice);
            TextView mcount = view.findViewById(R.id.mcount);
            ImageView mimg = view.findViewById(R.id.mimg);
            final JSONObject object = fabiao.getJSONObject(i);
            final String id = object.optString("id");
            String title = object.optString("title");
            final String count = object.optString("count");
            String buyprice = object.optString("buyprice");
            String img = object.optString("img");
            String sellprice = object.optString("sellprice");
            final String url = object.optString("url");
            mtitle.setText(title);
            mbuyprice.setText("发镖价 " + buyprice);
            msellprice.setText("接镖价 " + sellprice);
            mcount.setText("应镖 " + count + "人");
            Glide.with(context).load(img).into(mimg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (object.get("userid").equals(userID)) {
                            Intent intent = new Intent(context, BidBillDetailActivity.class);
                            intent.putExtra("fbid", id);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, BidDetailActivity.class);
                            intent.putExtra("id", id);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            viewHolder.mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(context, R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(context, R.anim.lunbo_chu);
        viewHolder.mviewflipper.setInAnimation(ru);
        viewHolder.mviewflipper.setOutAnimation(chu);
        viewHolder.mviewflipper.startFlipping();
    }

    private void loadbanner(final JSONArray banner, final TopViewHolder viewHolder) {
        List<Object> imgUrlList = new ArrayList<>();
        try {
            for (int i = 0; i < banner.length(); i++) {
                JSONObject jo = banner.getJSONObject(i);
                String imgUrl = jo.getString("img");
                imgUrlList.add(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.mBanner.setImages(imgUrlList)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        try {
                            EventIdIntentUtil.EventIdIntent(context, banner.getJSONObject(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        viewHolder.mBanner.setOnTouchListener(new View.OnTouchListener() {
            public float startX;
            public float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        viewHolder.mBanner.requestDisallowInterceptTouchEvent(true);
                        // 记录手指按下的位置
                        startY = event.getY();
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 获取当前手指位置
                        float endY = event.getY();
                        float endX = event.getX();
                        float distanceX = Math.abs(endX - startX);
                        float distanceY = Math.abs(endY - startY);
                        viewHolder.mBanner.requestDisallowInterceptTouchEvent(true);
//                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX+500 < distanceY) {
                            viewHolder.mBanner.requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
//                        refreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });
    }


class ViewHolder extends RecyclerView.ViewHolder {
//        private LinearLayout layout;
    public ViewHolder(View mView) {
        super(mView);
        mlistview= mView.findViewById(R.id.mlistview);
        mlistview.setHasFixedSize(true);
        mlistview.setLayoutManager(new LinearLayoutManager(context));
    }
}
    private void initTop(ViewHolder viewHolder, final int position) {
        try {
//            isclear = true;
            getIndexByType(false,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode) {
            case 1:
                Intent intent = new Intent(context, WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                context.startActivity(intent);
                break;
            case 2:
                try {
                    list = new ArrayList<>();
                    if (isclear) {
                        list.clear();
                    }
                    array = new JSONArray(content);
                    if (type.equals("2")){
                        addList(array);
                    }else if (type.equals("3")){
                        addBList(array);
                    }else if (type.equals("4")){
                        addFxList(array);
                    }else if (type.equals("1")){
                        addCzgList(array);
                    }
                    if (x == 1) {
                        mList = list;
                        handler.sendEmptyMessageDelayed(1, 0);
                    } else if (x == 2) {
                        mAddList = list;
                        handler.sendEmptyMessageDelayed(2, 0);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    //发现数据
    public void addFxList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("content",object.optString("content"));
            map.put("img",object.optString("img"));
            map.put("atime",object.optString("atime"));
            map.put("count",object.optString("count"));
            map.put("zan",object.optString("zan"));
            map.put("title",object.optString("title"));
            map.put("id",object.optString("id"));
            list.add(map);
        }

    }
    //爆料数据
    public void addBList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("extra",object.optString("extra"));
            map.put("img",object.optString("img"));
            map.put("dtime",object.optString("dtime"));
            map.put("readnum",object.optString("readnum"));
            map.put("title",object.optString("title"));
            map.put("plnum",object.optString("plnum"));
            map.put("zannum",object.optString("zannum"));
            map.put("blid",object.optString("blid"));
            map.put("content",object.optString("content"));
            list.add(map);
        }
    }
    //镖局数据
    public void addList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("endtime",object.optString("endtime"));
            map.put("id",object.optString("id"));
            map.put("img",object.optString("img"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("extra",object.optString("extra"));
            map.put("number",object.optString("number"));
            map.put("type",object.optString("type"));
            map.put("userid",object.optString("userid"));//新增字段，用于判断是否是自己的发标，是则跳转发标详情
            list.add(map);
        }

    }
    //超值购数据
    public void addCzgList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("id",object.optString("id"));
            map.put("imgurl",object.optString("imgurl"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("dianpu",object.optString("dianpu"));
            map.put("youhui",object.optString("youhui"));
            map.put("url",object.optString("url"));
            if (object.optString("hislowprice") != null){
                map.put("hislowprice",object.optString("hislowprice"));
            }
            list.add(map);
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    mRefreshableView.stopLoadMore();
//                    mRefreshableView.stopRefresh();
//                    refreshLayout.finishLoadmore();
//                    refreshLayout.finishRefresh();
                    if (mList.size() > 0 && mList.size() < 5) {
//                        mRefreshableView.setLoadComplete(true);
                    }
                    if (mList != null && mList.size() > 0) {
                        if (type.equals("2")){
                            adapter = new NewBjAdapter(context, mList);
                            mlistview.setAdapter(adapter);
                        }else if (type.equals("3")){
                            mBlAdapter = new NewBlAdapter(context, mList);
                            mlistview.setAdapter(mBlAdapter);
                        }else if (type.equals("4")){
                            mFxAdapter = new NewFxAdapter(context, mList);
                            mlistview.setAdapter(mFxAdapter);
                        }else if (type.equals("1")){
                            mCzgAdapter = new NewCzgAdapter(context, mList);
                            mlistview.setAdapter(mCzgAdapter);
                        }
                    }
                    break;
                case 2:
//                    maxNum = mCzgAdapter.getCount();
//                    mRefreshableView.stopLoadMore();
//                    mRefreshableView.stopRefresh();
//                    refreshLayout.finishLoadmore();
//                    refreshLayout.finishRefresh();
                    if (mAddList != null && mAddList.size() > 0) {
                        if (type.equals("2")) {
                            adapter.notifyData(mAddList);
                        } else if (type.equals("3")) {
                            mBlAdapter.notifyData(mAddList);
                        } else if (type.equals("4")) {
                            mFxAdapter.notifyData(mAddList);
                        } else if (type.equals("1")) {
                            mCzgAdapter.notifyData(mAddList);
                        }
                    } else {
//                        refreshLayout.finishLoadmore(true);
//                        mRefreshableView.stopLoadMore(false);
//                        refreshLayout.finishLoadMoreWithNoMoreData();
                        StringUtil.showToast(context, "没有更多了");
                    }
                    break;
            }
        }
    };
}
