package com.bbk.util;

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

import com.bbk.activity.BidBillDetailActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.fragment.OnClickHomeListioner;
import com.bbk.fragment.OnClickListioner;
import com.bumptech.glide.Glide;
import com.logg.Logg;
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

/**
 * Created by Administrator on 2018/7/18/018.
 */

public class HomeLoadUtil {

    public static OnClickHomeListioner onClickListioner;
    static Context context;
    static String homeclick;
    static int currentIndexTop = 0,currentIndex = 0;

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
                    .placeholder(R.mipmap.zw_img_160)
                    .thumbnail(0.5f)
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
            mbuyprice.setText("我要价 " + buyprice);
            msellprice.setText("扑倒价 " + sellprice);
            mcount.setText("扑倒中" + count + "人");
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
                    DialogSingleUtil.show(context);
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
                    DialogSingleUtil.show(context);
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
        mhscrollview1.scrollTo(view.getLeft() - 200, 0);
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
        mhscrollview.scrollTo(view.getLeft() - 200, 0);
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
}
