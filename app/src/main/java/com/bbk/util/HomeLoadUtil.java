package com.bbk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserSuggestionActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.component.BidHomeComponent;
import com.bbk.component.CarComponent;
import com.bbk.component.HomeAllComponent;
import com.bbk.component.HomeAllComponent1;
import com.bbk.component.HomeAllComponent4;
import com.bbk.component.HomeAllComponent7;
import com.bbk.component.HomeBijiaComponent;
import com.bbk.component.SimpleComponent;
import com.bbk.component.ZiyingComponent;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.fragment.OnClickHomeListioner;
import com.bbk.fragment.OnClickMallListioner;
import com.bbk.model.MainActivity;
import com.bbk.model.ShopFenleiActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.DianpuHomeActivity;
import com.bbk.shopcar.DianpuTypesActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/18/018.
 */

public class HomeLoadUtil {

    public static OnClickHomeListioner onClickListioner;
    public static OnClickMallListioner onClickMallListioner;
    static Context context;
    static String homeclick,dianpuclick;
    static int currentIndexTop = 0,currentIndex = 0,dianpuCickTop = 0,dianpuCick = 0;
    private static UpdataDialog updataDialog;
    private static AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private static Map<String, String> exParams;//yhhpass参数
    KelperTask mKelperTask;

    public HomeLoadUtil(Context context){
        this.context = context;
    }
    public HomeLoadUtil(Context context,OnClickHomeListioner onClickListioner){
        this.onClickListioner = onClickListioner;
        this.context = context;
    }
    /***
     * 加载中部图标
     * @param tag
     * @throws Exception
     */
    public static void loadTag(final Context context, List<Map<String, String>> taglist, final JSONArray tag,ImageView mImageView1,ImageView mImageView2,ImageView mImageView3,ImageView mImageView4,ImageView mImageView5
    ,TextView mTextView1 ,TextView mTextView2 ,TextView mTextView3 ,TextView mTextView4 ,TextView mTextView5,LinearLayout mLlLayout1,LinearLayout mLlLayout2,LinearLayout mLlLayout3,LinearLayout mLlLayout4,LinearLayout mLlLayout5) throws Exception {
        taglist.clear();
//        Logg.json(tag.toString());
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
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//不加这句会有绿色背景
                    .thumbnail(0.5f)
                    .placeholder(R.mipmap.zw_img_160)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (NoFastClickUtils.isFastClick()){
                            StringUtil.showToast(context,"对不起，您的点击太快了，请休息一下");
                        }else {
                            EventIdIntentUtil.EventIdIntent(context, tag.getJSONObject(position));
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /***
     * 加载中部图标
     * @param tag
     * @throws Exception
     */
    public static void loaddianpuTag(final Context context, List<Map<String, String>> taglist, final JSONArray tag,ImageView mImageView1,ImageView mImageView2,ImageView mImageView3,ImageView mImageView4,ImageView mImageView5
            ,ImageView mImageView6,ImageView mImageView7,ImageView mImageView8,TextView mTextView1 ,TextView mTextView2 ,TextView mTextView3 ,TextView mTextView4 ,TextView mTextView5,TextView mTextView6,TextView mTextView7,TextView mTextView8,
                                     LinearLayout mLlLayout1,LinearLayout mLlLayout2,LinearLayout mLlLayout3, LinearLayout mLlLayout4,LinearLayout mLlLayout5,LinearLayout mLlLayout6,LinearLayout mLlLayout7,LinearLayout mLlLayout8) throws Exception {
        taglist.clear();
        Logg.json(tag.toString());
        for (int i = 0; i < tag.length(); i++) {
            JSONObject object = tag.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
//            String htmlUrl = object.optString("htmlUrl");
            String eventId = object.optString("eventId");
            String img = object.optString("img");
            String name = object.optString("name");
            String keyword = object.optString("keyword");
//            map.put("htmlUrl", htmlUrl);
            map.put("eventId", eventId);
            map.put("text", name);
            map.put("imageUrl", img);
            map.put("keyword",keyword);
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
        imglist.add(mImageView6);
        imglist.add(mImageView7);
        imglist.add(mImageView8);
        textlist.add(mTextView1);
        textlist.add(mTextView2);
        textlist.add(mTextView3);
        textlist.add(mTextView4);
        textlist.add(mTextView5);
        textlist.add(mTextView6);
        textlist.add(mTextView7);
        textlist.add(mTextView8);
        boxlist.add(mLlLayout1);
        boxlist.add(mLlLayout2);
        boxlist.add(mLlLayout3);
        boxlist.add(mLlLayout4);
        boxlist.add(mLlLayout5);
        boxlist.add(mLlLayout6);
        boxlist.add(mLlLayout7);
        boxlist.add(mLlLayout8);
        for (int i = 0; i < boxlist.size(); i++) {
            final int position = i;
            TextView textView = textlist.get(position);
            ImageView imageView = imglist.get(position);
            final Map<String, String> map = taglist.get(position);
            String text = map.get("text").toString();
            textlist.get(position).setText(text);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(true);
            String imageUrl = map.get("imageUrl").toString();
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//不加这句会有绿色背景
                    .thumbnail(0.5f)
                    .placeholder(R.mipmap.zw_img_160)
                    .into(imageView);
            boxlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (NoFastClickUtils.isFastClick()){
                            StringUtil.showToast(context,"对不起，您的点击太快了，请休息一下");
                        }else {
                            if (position == 7) {
                                Intent intent = new Intent(context, ShopFenleiActivity.class);
                                context.startActivity(intent);
                            }else {
                                if (map.get("eventId").toString().equals("a2")) {
                                    Intent intent = new Intent(context, DianpuTypesActivity.class);
                                    intent.putExtra("tag", tag.toString());
                                    intent.putExtra("keyword", map.get("keyword").toString());
                                    intent.putExtra("position", position + "");
                                    context.startActivity(intent);
                                }else {
                                    EventIdIntentUtil.EventIdIntent(context, tag.getJSONObject(position));
                                }
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    /***
     * 加载轮播
     * @param fabiao
     * @throws JSONException
     */
    public static void loadViewflipper(final Context context, ViewFlipper  mviewflipper, JSONArray fabiao) throws JSONException {
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
            mbuyprice.setText("买方出价 " + buyprice);
            msellprice.setText("接单价 " + sellprice);
            mcount.setText("接单中" + count + "人");
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
            mviewflipper.addView(view);
        }
        Animation ru = AnimationUtils.loadAnimation(context, R.anim.lunbo_ru);
        Animation chu = AnimationUtils.loadAnimation(context, R.anim.lunbo_chu);
        mviewflipper.setInAnimation(ru);
        mviewflipper.setOutAnimation(chu);
        mviewflipper.startFlipping();
    }


    /**
     * 加载Banner
     *
     * @param banner
     */
    public static void loadbanner(final Context context, final Banner mBanner, final JSONArray banner) {
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
//        Logg.json(banner);
        mBanner.setImages(imgUrlList)
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
                .setDelayTime(5000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        mBanner.setOnTouchListener(new View.OnTouchListener() {
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
//                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX + 500 < distanceY) {
                            mBanner.requestDisallowInterceptTouchEvent(false);
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

    public static void loadGuanggaoBanner(final Context context, final Banner mBanner, final JSONObject banner) {
        List<Object> imgUrlList = new ArrayList<>();
        Logg.json(banner);
        try {
//            for (int i = 0; i < banner.length(); i++) {
//                JSONObject jo = banner.getJSONObject(i);
                String imgUrl = banner.getString("img");
                imgUrlList.add(imgUrl);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBanner.setImages(imgUrlList)
                .setImageLoader(new GlideImageGuanggaoLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                 EventIdIntentUtil.EventIdIntent(context, banner);
//                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
//                        Intent intentxh;
//                        if (TextUtils.isEmpty(userID)){
//                            intentxh = new Intent(context, UserLoginNewActivity.class);
//                            context.startActivity(intentxh);
//                        }else {
//                            intentxh = new Intent(context, NewDianpuHomeActivity.class);
//                            context.startActivity(intentxh);
//                        }
                    }
                })
                .setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        mBanner.setOnTouchListener(new View.OnTouchListener() {
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
//                        refreshLayout.setEnabled(false);
                        // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                        if (distanceX + 500 < distanceY) {
                            mBanner.requestDisallowInterceptTouchEvent(false);
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

    public static void addtitleTop(final Context context, final String text, final int i, final LinearLayout mboxTop, final LinearLayout mbox, final HorizontalScrollView horizontalScrollViewTop, final HorizontalScrollView horizontalScrollView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.super_item_title, null);
        //设置view的weight为1，保证导航铺满当前页面
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(context, 0), 0, BaseTools.getPixelsFromDp(context, 0), 0);
        if (i == 0) {
            view.setVisibility(View.VISIBLE);
            title.setTextColor(Color.parseColor("#FF7D41"));
            henggang.setBackgroundColor(Color.parseColor("#FF7D41"));
            title.setText("超值购");
        }
        if (i == 1) {

        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Logg.e(i+"==="+currentIndexTop);
                if (i != currentIndexTop) {
                    DialogHomeUtil.show(context);
                    NewHomeFragment.refreshLayout.setEnableRefresh(false);
                    SharedPreferencesUtil.putSharedData(context, "homeclick", "homeclick", "no");
                    updateTitleTop(i, mboxTop, text,horizontalScrollViewTop);
                    updateTitle(i, mbox, text,horizontalScrollView);
                }
            }

        });
        mboxTop.addView(view);
    }


    public void addtitle(final Context context, final String text, final int i,final LinearLayout mboxTop, final LinearLayout mbox, final HorizontalScrollView horizontalScrollViewTop, final HorizontalScrollView horizontalScrollView) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.super_item_title, null);
        //设置view的weight为1，保证导航铺满当前页面
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(context, 0), 0, BaseTools.getPixelsFromDp(context, 0), 0);
        if (i == 0) {
            view.setVisibility(View.VISIBLE);
            title.setTextColor(Color.parseColor("#FF7D41"));
            henggang.setBackgroundColor(Color.parseColor("#FF7D41"));
            title.setText("超值购");
        }
        if (i == 1) {

        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != currentIndex) {
                    SharedPreferencesUtil.putSharedData(context, "homeclick", "homeclick", "yes");
                    DialogHomeUtil.show(context);
                    NewHomeFragment.refreshLayout.setEnableRefresh(false);
                    updateTitle(i, mbox, text,horizontalScrollView);
                    updateTitleTop(i, mboxTop, text,horizontalScrollViewTop);
                }
            }

        });
        mbox.addView(view);
    }


    public static void updateTitleTop(int position, LinearLayout mbox, String text, HorizontalScrollView mhscrollview1) {
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        if (position == currentIndexTop) {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }
        View view4 = mbox.getChildAt(currentIndexTop);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (position == currentIndexTop) {
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
//        mhscrollview1.scrollTo(view.getLeft() - 200, 0);
        mhscrollview1.smoothScrollTo(view.getLeft() - 250, 0);
        currentIndexTop = position;
        homeclick = SharedPreferencesUtil.getSharedData(context, "homeclick", "homeclick");
        if (homeclick.equals("no")) {
            if (position == 0) {
                onClickListioner.onClickOnePosition();
            } else {
                onClickListioner.onClick(text);
            }
        }
    }

    public static void updateTitle(int position, LinearLayout mbox, String text,HorizontalScrollView mhscrollview) {
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        if (position == currentIndex) {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
        }
        View view4 = mbox.getChildAt(currentIndex);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (position == currentIndex) {
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
//        mhscrollview.scrollTo(view.getLeft() - 200, 0);
        mhscrollview.smoothScrollTo(view.getLeft() - 250, 0);
        currentIndex = position;
        homeclick = SharedPreferencesUtil.getSharedData(context, "homeclick", "homeclick");
        if (TextUtils.isEmpty(homeclick)) {
            homeclick = "yes";
        }
        if (homeclick.equals("yes")) {
            if (position == 0) {
                onClickListioner.onClickOnePosition();
            } else {
                onClickListioner.onClick(text);
            }
        }
    }


    /**
     * 首页引导图层
     *
     * @param targetView
     * @param targetView1
     */

    public static void showGuideView(final Context context, View targetView, final View targetView1) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(70)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showGuideViewBijia(context,targetView1);
            }
        });

        builder.addComponent(new SimpleComponent()).addComponent(new HomeAllComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    public static void showGuideViewBijia(final Context context, final View targetView) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(70)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showCarGuideView(context,HomeActivity.imgCarBtn);
            }
        });

        builder.addComponent(new HomeBijiaComponent()).addComponent(new HomeAllComponent1());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }

    /**
     * 购物车引导
     */
    public static void showCarGuideView(final Context context, final View view) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingLeft(10)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingBottom(70)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override public void onShown() {
            }

            @Override public void onDismiss() {
                SharedPreferencesUtil.putSharedData(context, "isFirstHomeUse", "isFirstHomeUserUse", "no");
            }
        });

        builder.addComponent(new CarComponent()).addComponent(new HomeAllComponent4());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }



    public static void showGuideViewZiying(final Context context, final View targetView3, final View targetView4, View targetView6) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView6)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(70)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                HomeLoadUtil.showGuideView(context, targetView3, targetView4);
            }
        });

        builder.addComponent(new ZiyingComponent()).addComponent(new HomeAllComponent7());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show((Activity) context);
    }



    /**
     * 跳转京东淘宝APP
     *
     * @param url
     */
    public void jumpThirdApp(String url) {
        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        alibcShowParams.setClientType("taobao_scheme");
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        if (url.contains("tmall") || url.contains("taobao")) {
            showUrl(url);
        } else if (url.contains("jd")) {
//            KeplerApiManager.getWebViewService().openAppWebViewPage(context,
//                    url,
//                    mKeplerAttachParameter,
//                    mOpenAppAction);
            try {
                KeplerApiManager.getWebViewService().openJDUrlPage(url, mKeplerAttachParameter,context, mOpenAppAction, 1500);
            } catch (KeplerBufferOverflowException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(context, WebViewActivity.class);
            if (url != null) {
                intent.putExtra("url", url);
            }
           context. startActivity(intent);
        }
    }

    /**
     * 打开指定链接
     */
    public void showUrl(String url) {
        String text = url;
        if (TextUtils.isEmpty(text)) {
            StringUtil.showToast(context, "URL为空");
            return;
        }
        AlibcTrade.show((Activity) context, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
    }

    private static KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status) {
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
            } else {
                mKelperTask = null;
            }
        }
    };
//    OpenAppAction mOpenAppAction = new OpenAppAction() {
//        @Override
//        public void onStatus(final int status, final String url) {
//            Intent intent;
//            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//            } else {
//            }
//            if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
//                StringUtil.showToast(context, "未安装京东");
//                intent = new Intent(context, WebViewActivity.class);
//                if (url != null) {
//                    intent.putExtra("url", url);
//                }
//                context.startActivity(intent);
//                //未安装京东
//            } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
//                StringUtil.showToast(context, "不在白名单");
//                //不在白名单
//            } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
//                StringUtil.showToast(context, "协议错误");
//                //协议错误
//            } else if (status == OpenAppAction.OpenAppAction_result_APP) {
//                //呼京东成功
//            } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
//                StringUtil.showToast(context, "网络异常");
//                //网络异常
//            }
//        }
//    };


    /**
     * 快捷菜单弹窗
     */
    public static void showItemPop(final Context context,View view){
        TopRightMenu mTopRightMenu;
        mTopRightMenu = new TopRightMenu((Activity) context);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.bibijing_02,"消息", NewConstants.messages + ""));
        menuItems.add(new MenuItem(R.mipmap.bibijing_03,"首页"));
        menuItems.add(new MenuItem(R.mipmap.bibijing_04,"在线客服"));
        mTopRightMenu
                .setHeight(620)     //默认高度480
                .setWidth(400)//默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)           //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .addMenuItem(new MenuItem(R.mipmap.bibijing_05,"意见反馈"))
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        Intent intent;
                        switch (position){
                            case 0:
                                intent = new Intent(context, MesageCenterActivity.class);
                                intent.putExtra("type", "0");
                                context.startActivity(intent);
                                break;
                            case 1:
                                HomeActivity.position = 0;
                                SharedPreferencesUtil.putSharedData(context, "homeactivty", "type","0");
                                intent = new Intent(context, HomeActivity.class);
                                context.startActivity(intent);
                                break;
                            case 2:
                                MainActivity.consultService(context);
                                break;
                            case 3:
                                intent = new Intent(context, UserSuggestionActivity.class);
                                context.startActivity(intent);
                                break;
                        }
                    }
                })
                .showAsDropDown(view, -315, 0);
    }

}
