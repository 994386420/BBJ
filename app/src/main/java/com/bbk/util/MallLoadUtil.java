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
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.fragment.OnClickHomeListioner;
import com.bbk.fragment.OnClickMallListioner;
import com.bbk.shopcar.DianpuHomeActivity;
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

/**
 * Created by Administrator on 2018/7/18/018.
 */

public class MallLoadUtil {
    public static OnClickMallListioner onClickMallListioner;
    static Context context;
    static String dianpuclick;
    static int dianpuCickTop = 0,dianpuCick = 0;

    public MallLoadUtil(Context context, OnClickMallListioner onClickListioner){
        this.onClickMallListioner = onClickListioner;
        this.context = context;
    }

    public static void addtitleMallTop(final Context context, final String text, final int i, final LinearLayout mboxTop, final LinearLayout mbox, final HorizontalScrollView horizontalScrollViewTop, final HorizontalScrollView horizontalScrollView) {
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
        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != dianpuCickTop) {
                    DialogSingleUtil.show(context);
                    DianpuHomeActivity.refreshLayout.setEnableRefresh(false);
                    SharedPreferencesUtil.putSharedData(context, "homeclick", "homeclick", "no");
                    updateTitleTopMall(i, mboxTop, text,horizontalScrollViewTop);
                    updateTitleMall(i, mbox, text,horizontalScrollView);
                }
            }

        });
        mboxTop.addView(view);
    }


    public void addtitleMall(final Context context, final String text, final int i,final LinearLayout mboxTop, final LinearLayout mbox, final HorizontalScrollView horizontalScrollViewTop, final HorizontalScrollView horizontalScrollView) {
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
        }
        if (i == 1) {

        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != dianpuCick) {
                    SharedPreferencesUtil.putSharedData(context, "homeclick", "homeclick", "yes");
                    DialogSingleUtil.show(context);
                    DianpuHomeActivity.refreshLayout.setEnableRefresh(false);
                    updateTitleMall(i, mbox, text,horizontalScrollView);
                    updateTitleTopMall(i, mboxTop, text,horizontalScrollViewTop);
                }
            }

        });
        mbox.addView(view);
    }



    public static void updateTitleTopMall(int position, LinearLayout mbox, String text, HorizontalScrollView mhscrollview1) {
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
//        if (position == 0) {
//            title1.setTextColor(Color.parseColor("#FF7D41"));
//            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
//        } else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
//        }
        View view4 = mbox.getChildAt(dianpuCickTop);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (dianpuCickTop == position) {
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        mhscrollview1.scrollTo(view.getLeft() - 200, 0);
        dianpuCickTop = position;
        dianpuclick = SharedPreferencesUtil.getSharedData(context, "dianpuclick", "dianpuclick");
        if (dianpuclick.equals("no")) {
//            if (position == 0) {
//                onClickListioner.onClickOnePosition();
//            } else {
            onClickMallListioner.onMallClick(text);
//            }
        }
    }

    public static void updateTitleMall(int position, LinearLayout mbox, String text,HorizontalScrollView mhscrollview) {
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
//        if (position == 0) {
//            title1.setTextColor(Color.parseColor("#FF7D41"));
//            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
//        } else {
            title1.setTextColor(Color.parseColor("#FF7D41"));
            henggang1.setBackgroundColor(Color.parseColor("#FF7D41"));
//        }
        View view4 = mbox.getChildAt(dianpuCick);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        if (dianpuCick == position) {
            title3.setTextColor(Color.parseColor("#FF7D41"));
            henggang3.setBackgroundColor(Color.parseColor("#FF7D41"));
        } else {
            title3.setTextColor(Color.parseColor("#666666"));
            henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        mhscrollview.scrollTo(view.getLeft() - 200, 0);
        dianpuCick = position;
        dianpuclick = SharedPreferencesUtil.getSharedData(context, "dianpuclick", "dianpuclick");
        if (TextUtils.isEmpty(dianpuclick)) {
            dianpuclick = "yes";
        }
        if (dianpuclick.equals("yes")) {
//            if (position == 0) {
//                onClickListioner.onClickOnePosition();
//            } else {
            onClickMallListioner.onMallClick(text);
//            }
        }
    }
}
