package com.bbk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.DataFragmentActivity;
import com.bbk.activity.DomainMoreActivity;
import com.bbk.activity.GossipPiazzaDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.adapter.NewBjAdapter;
import com.bbk.adapter.NewBlAdapter;
import com.bbk.adapter.NewCzgAdapter;
import com.bbk.adapter.NewFxAdapter;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DensityUtils;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyNewScrollView;
import com.bbk.view.MyScrollListView;
import com.bbk.view.RefreshableView;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewHomeFragment extends BaseViewPagerFragment implements OnClickListener, ResultEvent,MyNewScrollView.OnScrollListener,RefreshableView.RefreshListener {
    private DataFlow6 dataFlow;
    private View mView, mzhuangtai;
    /**
     * Banner
     */
    private Banner mBanner;//首页banner
    private LinearLayout mSort,mSearch;//搜索，分类;
    private JSONArray banner = new JSONArray();
    /**
     * 中间布局
     */
    private LinearLayout mLlLayout1,mLlLayout2,mLlLayout3,mLlLayout4,mLlLayout5;
    private ImageView mImageView1,mImageView2,mImageView3,mImageView4,mImageView5;
    private TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5;
    private JSONArray tag = new JSONArray();
    private List<Map<String, String>> taglist = new ArrayList<>();
    private LinearLayout mCompareutil,mQueryhistory;//比价神器，查历史价
    private ImageView mQueryhistoryimg, mCompareimg;
    private JSONArray gongneng = new JSONArray();
    private JSONArray fabiao = new JSONArray();
    private MyNewScrollView myScrollView;
    /**
     * 顶部固定的TabViewLayout
     */
    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    private LinearLayout mTabViewLayout;

    /**
     * 要悬浮在顶部的View的子View
     */
    private LinearLayout mTopView;
    private LinearLayout layout;

    /**
     * 镖局,超值购...
     * @param savedInstanceState
     */
    private LinearLayout mLlCzgLayout,mLlbjLayout,mLlblLayout,mLlfxLayout;
    private View mCzgView,mBjView,mBlView,mFxView;
    private TextView mCzgText,mBjText,mBlText,mFxText;
    private NewBjAdapter adapter;
    private NewBlAdapter mBlAdapter;
    private NewCzgAdapter mCzgAdapter;
    private NewFxAdapter mFxAdapter;
    private MyScrollListView mlistview,mCzgListview;
    private int page = 1,x = 1;
    private String type = "1";
    private List<Map<String,String>> list,addlist,mList;
    private List<Map<String,String>> czglist,czgaddlist,mCzgList;
    private String wztitle = "";
    private ViewFlipper mviewflipper;//发标动态轮播
    private List<Map<String, String>> titlelist;
    private List<Map<String, String>> datalist;
    private RefreshableView mRefreshableView;
    private View view;
    private ImageView huodongimg;//活动按钮
    private boolean isshowzhezhao = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (null != getActivity().getIntent().getStringExtra("content")) {
            String content = getActivity().getIntent().getStringExtra("content");
            try {
                isshowzhezhao = false;
                JSONObject jsonObject = new JSONObject(content);
                EventIdIntentUtil.EventIdIntent(getActivity(), jsonObject);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (null == mView) {
            dataFlow = new DataFlow6(getContext());
            getActivity().getWindow().setBackgroundDrawable(null);
            mView = inflater.inflate(R.layout.activity_new_home_layout, null);
            View topView = mView.findViewById(R.id.lin);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView(mView);
            mViewLoad();
        }
        return mView;

    }
    //控件实例化
    private void initView(View v){
        huodongimg =mView.findViewById(R.id.huodongimg);
        view = v.findViewById(R.id.view);
        mRefreshableView =  v.findViewById(R.id.refresh_root);
        mRefreshableView.setRefreshListener(this);
        mviewflipper = mView.findViewById(R.id.mviewflipper);
        mBanner = v.findViewById(R.id.banner);
        mSearch = v.findViewById(R.id.msearch);
        mSort = v.findViewById(R.id.msort);
        mLlLayout1 =  mView.findViewById(R.id.box1);
        mLlLayout2 = mView.findViewById(R.id.box2);
        mLlLayout3 =  mView.findViewById(R.id.box3);
        mLlLayout4 =  mView.findViewById(R.id.box4);
        mLlLayout5 =  mView.findViewById(R.id.box5);
        mTextView1 =  mView.findViewById(R.id.text1);
        mTextView2 = mView.findViewById(R.id.text2);
        mTextView3 = mView.findViewById(R.id.text3);
        mTextView4 =  mView.findViewById(R.id.text4);
        mTextView5 =  mView.findViewById(R.id.text5);
        mImageView1 =  mView.findViewById(R.id.img1);
        mImageView2 = mView.findViewById(R.id.img2);
        mImageView3 = mView.findViewById(R.id.img3);
        mImageView4 =  mView.findViewById(R.id.img4);
        mImageView5 = mView.findViewById(R.id.img5);
        mCompareutil = mView.findViewById(R.id.compareutil);
        mQueryhistory = mView.findViewById(R.id.queryhistory);
        mCompareimg = mView.findViewById(R.id.compareimg);
        mQueryhistoryimg = mView.findViewById(R.id.queryhistoryimg);
        myScrollView = mView.findViewById(R.id.myScrollView);
        mTabViewLayout = mView.findViewById(R.id.ll_tabView);
        mTopTabViewLayout = mView.findViewById(R.id.ll_tabTopView);
        mTopView = mView. findViewById(R.id.tv_topView);
        //滑动监听
        myScrollView.setOnScrollListener(this);
        layout = mView.findViewById(R.id.layout);
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
        mLlCzgLayout.setOnClickListener(this);
        mLlbjLayout.setOnClickListener(this);
        mLlblLayout.setOnClickListener(this);
        mLlfxLayout.setOnClickListener(this);
//        mrefresh = mView.findViewById(R.id.mrefresh);
        mlistview = mView.findViewById(R.id.mlistview);
        mCzgListview = mView.findViewById(R.id.mczglistview);
        mSearch.setOnClickListener(this);
        mSort.setOnClickListener(this);
    }

    //首页数据请求
    private void initData(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "newService/queryAppIndexInfo", paramsMap, this, is);
    }
    //首页分类数据
    private void getIndexByType(boolean is,int code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type",type);
        paramsMap.put("page",page+"");
        dataFlow.requestData(code, Constants.GetQueryAppIndexByType, paramsMap, this, is);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.ll_czg_layout:
                setView();
                mIdex("1",2);
                mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
                mCzgView.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_bj_layout:
                setView();
                mIdex("2",2);
                mBjText.setTextColor(getResources().getColor(R.color.color_line_top));
                mBjView.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_bl_layout:
                setView();
                mIdex("3",2);
                mBlText.setTextColor(getResources().getColor(R.color.color_line_top));
                mBlView.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_fx_layout:
                setView();
                mIdex("4",2);
                mFxText.setTextColor(getResources().getColor(R.color.color_line_top));
                mFxView.setVisibility(View.VISIBLE);
                break;
            case R.id.msort:
                intent = new Intent(getActivity(), SortActivity.class);
                startActivity(intent);
                break;
            case R.id.msearch:
                intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setView(){
            mCzgText.setTextColor(getResources().getColor(R.color.color_line_text));
            mCzgView.setVisibility(View.GONE);
            mBjText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mQbText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
            mBjView.setVisibility(View.GONE);
            mBlText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mDfkText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
            mBlView.setVisibility(View.GONE);
            mFxText.setTextColor(getResources().getColor(R.color.color_line_text));
//            mDfhText.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
            mFxView.setVisibility(View.GONE);
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
                try {
                    JSONObject object = new JSONObject(content);
                    if (object.has("fubiao")){
                        huodongimg.setVisibility(View.VISIBLE);
                        final JSONObject jo = object.getJSONObject("fubiao");
                        Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                        huodongimg.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EventIdIntentUtil.EventIdIntent(getActivity(),jo);
                            }
                        });
                    }else {
                        huodongimg.setVisibility(View.GONE);
                    }
                    banner = object.optJSONArray("banner");
                    tag = object.optJSONArray("tag");
                    if (object.has("gongneng")){
                        gongneng = object.optJSONArray("gongneng");
                    }
                    fabiao = object.optJSONArray("fabiao");
                    if (object.has("guanggao")) {
                        if (isshowzhezhao) {
                            final JSONObject jo = object.optJSONObject("guanggao");
                            new HomeAlertDialog(getActivity()).builder()
                                    .setimag(jo.optString("img"))
                                    .setonclick(new OnClickListener() {
                                        @Override
                                        public void onClick(View arg0) {
                                            EventIdIntentUtil.EventIdIntent(getActivity(), jo);
                                        }
                                    }).show();
                            isshowzhezhao = false;
                        }

                    }
                    loadbanner(banner);
                    loadTag(tag);
                    if (fabiao != null ){
                        loadFlipper(fabiao);
                    }
                    //功能模块图片动态获取
                    if (gongneng.length() >= 2) {
                        Glide.with(getActivity()).
                                load(gongneng.getJSONObject(0).optString("img")).
                                placeholder(R.mipmap.bjsq).into(mCompareimg);
                        Glide.with(getActivity()).load(gongneng.getJSONObject(1).optString("img")).placeholder(R.mipmap.lsyg).into(mQueryhistoryimg);
                    }else {
                        mCompareimg.setBackgroundResource(R.mipmap.bjsq);
                        mQueryhistoryimg.setBackgroundResource(R.mipmap.lsyg);
                    }
                    //功能模块点击事件
                    mCompareutil.setOnClickListener(onClickListener);
                    mQueryhistory.setOnClickListener(onClickListener);
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                break;

            case 2:
                try {
                list = new ArrayList<>();
                list.clear();
                    JSONArray array = new JSONArray(content);
                    if (type.equals("2")){
                        addList(array);
                    }else if (type.equals("3")){
                        addBList(array);
                    }else if (type.equals("4")){
                        addFxList(array);
                    }else if (type.equals("1")){
                        addCzgList(array);
                    }
                    loadBjData();
                    initListener();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 3:
            break;
            case 4:
                Intent intent = new Intent(getActivity(), WebViewWZActivity.class);
                intent.putExtra("url", content);
                intent.putExtra("title", wztitle);
                startActivity(intent);
                break;
        }
    }

    //放数据
    private void loadBjData(){
        if (x==1){
            mList = list;
            if (mList != null && mList.size() > 0) {
                if (type.equals("2")){
                    adapter = new NewBjAdapter(getActivity(), mList);
                    mlistview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else if(type.equals("3")){
                    mBlAdapter = new NewBlAdapter(getActivity(), mList);
                    mlistview.setAdapter(mBlAdapter);
                    mBlAdapter.notifyDataSetChanged();
                }else if (type.equals("4")){
                    mFxAdapter = new NewFxAdapter(getActivity(), mList);
                    mlistview.setAdapter(mFxAdapter);
                    mFxAdapter.notifyDataSetChanged();
                }else if (type.equals("1")){
                    mCzgAdapter = new NewCzgAdapter(getActivity(), mList);
                    mlistview.setAdapter(mCzgAdapter);
                    mCzgAdapter.notifyDataSetChanged();
                }
                mlistview.setVisibility(View.VISIBLE);
                mlistview.setOnItemClickListener(onItemClickListener);
                mlistview.LoadingComplete();   //告诉listview已经加载完毕,重置提示文字
                myScrollView.loadingComponent();//告示scrollview已经加载完毕，重置并发控制符的值
            }else {
                mlistview.setVisibility(View.GONE);
            }
        }else if (x==2){
            addlist = list;
            if (addlist != null && addlist.size() > 0){
                if (type.equals("2")){
                    adapter.notifyData(addlist);
                }else if(type.equals("3")){
                    mBlAdapter.notifyData(addlist);
                }else if (type.equals("4")){
                    mFxAdapter.notifyData(addlist);
                }else if(type.equals("1")){
                    mCzgAdapter.notifyData(addlist);
                }
                mlistview.LoadingComplete();
                myScrollView.loadingComponent();//告示scrollview已经加载完毕，重置并发控制符的值
            }else {
                mlistview.LoadingCompleted();
            }
        }
    }

    @Override
    public void lazyLoad() {

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
            map.put("extra",object.optString("extra"));
            map.put("plnum",object.optString("plnum"));
            map.put("zannum",object.optString("zannum"));
            map.put("blid",object.optString("blid"));
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
            if (!object.optString("hislowprice").isEmpty()){
                map.put("hislowprice",object.optString("hislowprice"));
            }
            list.add(map);
        }
    }
    //首页发标
    private void loadFlipper(final JSONArray fabiao) throws JSONException {
        for (int i = 0; i < fabiao.length(); i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.flipper_bidhome, null);
            TextView mtitle = view.findViewById(R.id.mtitle);
            TextView mbuyprice =  view.findViewById(R.id.mbuyprice);
            TextView msellprice =  view.findViewById(R.id.msellprice);
            TextView mcount =  view.findViewById(R.id.mcount);
            ImageView mimg =  view.findViewById(R.id.mimg);
            JSONObject object = fabiao.getJSONObject(i);
            final String id = object.optString("id");
            String title = object.optString("title");
            String count = object.optString("count");
            String buyprice = object.optString("buyprice");
            String img = object.optString("img");
            String sellprice = object.optString("sellprice");
            final String url = object.optString("url");
            mtitle.setText(title);
            mbuyprice.setText("发镖价 "+buyprice);
            msellprice.setText("接镖价 "+sellprice);
            mcount.setText("应镖 "+count+"人");
            Glide.with(getActivity()).load(img).into(mimg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });
            mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(getActivity(), R.anim.lunbo_chu);
        mviewflipper.setInAnimation(ru);
        mviewflipper.setOutAnimation(chu);
        mviewflipper.startFlipping();
    }
    //首页Banner
    private void loadbanner(final JSONArray banner) {
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
        mBanner.setImages(imgUrlList)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        try {
                            EventIdIntentUtil.EventIdIntent(getActivity(), banner.getJSONObject(position));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
       mBanner.setOnTouchListener(onTouchListener);
    }

    private void loadTag(final JSONArray tag) throws Exception {
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
        imglist.add(mImageView1);
        imglist.add(mImageView2);
        imglist.add(mImageView3);
        imglist.add(mImageView4);
        imglist.add(mImageView5);
        textlist.add(mTextView1);
        textlist.add(mTextView2);
        textlist.add(mTextView3);
        textlist.add(mTextView4);
        textlist.add(mTextView5);
        boxlist.add(mLlLayout1);
        boxlist.add(mLlLayout2);
        boxlist.add(mLlLayout3);
        boxlist.add(mLlLayout4);
        boxlist.add(mLlLayout5);
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
            Glide.with(getActivity())
                    .load(imageUrl)
                    .placeholder(R.mipmap.zw_img_160)
                    .thumbnail(0.5f)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 4) {
//                        Intent intent = new Intent(getActivity(), DomainMoreActivity.class);
                        Intent intent = new Intent(getActivity(), DataFragmentActivity.class);
                        startActivity(intent);
                    } else {
                        try {
                            EventIdIntentUtil.EventIdIntent(getActivity(), tag.getJSONObject(position));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void initListener() {
        myScrollView.setOnZdyScrollViewListener(new MyNewScrollView.OnZdyScrollViewListener() {
            @Override
            public void ZdyScrollViewListener() {
                mlistview.onLoading();
                setListView();
            }
        });
    }
    private void setListView() {
            page++;
            x=2;
            getIndexByType(false,2);
    };


    OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                if (type.equals("4")){
                    insertWenzhangGuanzhu(i);
                }else if (type.equals("3")){
                    Intent intent = new Intent(getActivity(), GossipPiazzaDetailActivity.class);
                    intent.putExtra("blid",mList.get(i).get("blid"));
                    startActivity(intent);
                }else if (type.equals("2")){
                    Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                    intent.putExtra("id",mList.get(i).get("id"));
                    startActivity(intent);
                }else if(type.equals("1")){
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("url",  mList.get(i).get("url"));
                    intent.putExtra("title",  mList.get(i).get("title"));
                    startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void insertWenzhangGuanzhu(int position) {
        try {
            wztitle  = mList.get(position).get("title");
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            String token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
            if (!TextUtils.isEmpty(userID)) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", userID);
                params.put("wzid",  mList.get(position).get("id"));
                params.put("token", token);
                params.put("type", "2");
                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
            } else {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("userid", "-1");
                params.put("token", token);
                params.put("wzid",  mList.get(position).get("id"));
                params.put("type", "2");
                dataFlow.requestData(4, "newService/insertWenzhangGuanzhu", params, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mRefreshableView.finishRefresh();
                    break;
            }
        }
    };
    //首页数据下拉刷新
    @Override
    public void onRefresh(RefreshableView view) {
        initData(true);
        setView();
        mViewLoad();
        mIdex("1",2);
        mCzgText.setTextColor(getResources().getColor(R.color.color_line_top));
        mCzgView.setVisibility(View.VISIBLE);
//        initListenerczg();
        initListener();
        handler.sendEmptyMessageDelayed(1, 2000);
    }
    //首页视图数据加载
    private void mViewLoad(){
        initData(true);
        getIndexByType(true,2);
//        initListenerczg();
        initListener();
    }
    //超值购等数据
    private void mIdex(String str,int code){
        type = str;
        x=1;
        page=1;
        getIndexByType(true,code);
    }

    @Override
    public void onScroll(int scrollY) {
        int mHeight = layout.getBottom();
        //判断滑动距离scrollY是否大于0，因为大于0的时候就是可以滑动了，此时mTabViewLayout.getTop()才能取到值。
        if (scrollY > 0 && scrollY >= mHeight) {
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
                view.setVisibility(View.GONE);
            }

        }
    }

    //功能模块点击事件
    OnClickListener onClickListener = new OnClickListener() {
        Intent intent;
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.queryhistory:
                    intent = new Intent(getActivity(), QueryHistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.compareutil:
                    intent = new Intent(getActivity(), BidHomeActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public float startX;
        public float startY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBanner.requestDisallowInterceptTouchEvent(true);
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
                    mBanner.requestDisallowInterceptTouchEvent(true);
                    // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                    if (distanceX+500 < distanceY) {
                        mBanner.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return false;
        }
    };
}
