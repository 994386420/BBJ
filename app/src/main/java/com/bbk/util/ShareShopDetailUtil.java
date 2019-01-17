package com.bbk.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.activity.YaoqingFriendsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class ShareShopDetailUtil {
    private ShareHaiBaoManager shareManager;
    Activity context;
    private String content;
    List<String> DetailimgUrlList;
    private String jingkouling, imgurl, price,  title;
    private ImageView imageView;
    private TextView mPrice,mTitle,mJinkouling;
    private Bitmap bitmap;
    Handler handler = new Handler();

    public ShareShopDetailUtil(Activity context, View view, String content, List<String> DetailimgUrlList){
        this.context=context;
        this.DetailimgUrlList = DetailimgUrlList;
        showpop(view);
    }

    public ShareShopDetailUtil(Activity context,  List<String> DetailimgUrlList,View view, String jingkouling, String imgurl, String price, String title,Bitmap bitmap) {
        this.context=context;
        this.DetailimgUrlList = DetailimgUrlList;
        this.jingkouling = jingkouling;
        this.imgurl = imgurl;
        this.price = price;
        this.title = title;
        this.bitmap = bitmap;
        showpop(view);
    }

    private void showpop(View view) {
        View dialogView = LayoutInflater.from(context).inflate(
                R.layout.share_shopdetail_layout, null);
        final PopupWindow popupWindow = new PopupWindow(dialogView,
                BaseTools.getWindowsWidth(context), LayoutParams.MATCH_PARENT);
        mPrice = dialogView.findViewById(R.id.tv_shop_price);
        mTitle = dialogView.findViewById(R.id.tv_shop_title);
        imageView = dialogView.findViewById(R.id.iv_image);
        mJinkouling = dialogView.findViewById(R.id.tv_jinkouling);

        mPrice.setText(price);
        mTitle.setText(title);
        Glide.with(context).load(imgurl).into(imageView);
        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogView.findViewById(R.id.share_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isWeixinAvilible(context)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(context);
                            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(jingkouling);
                            mJinkouling.setVisibility(View.VISIBLE);
                            share(0,content);
                        } else {
                            StringUtil.showToast(context, "请安装微信客户端");
                        }
                        popupWindow.dismiss();
                    }
                });
        dialogView.findViewById(R.id.share_QQ).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isQQAvilible(context)) {
                            DialogSingleUtil.show(context);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                        cm.setText(jingkouling);
                                        mJinkouling.setVisibility(View.VISIBLE);
                                        ShareManager.sharedToQQ(context, bitmap);
                                    }
                                }
                            },0);
                        } else {
                            StringUtil.showToast(context, "请安装QQ客户端");
                        }
                        popupWindow.dismiss();
                    }
                });

        dialogView.findViewById(R.id.share_bbj).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(jingkouling);
                mJinkouling.setVisibility(View.VISIBLE);
            }
        });
        dialogView.findViewById(R.id.ll_close).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
        popupWindow.setAnimationStyle(R.style.AnimationPreview);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = context.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                context.getWindow().setAttributes(lp);
            }
        });

    }
    private void share(int i, String s) {
        shareManager = new ShareHaiBaoManager(context);
        shareManager.setShareImage(i,DetailimgUrlList,s);
    }



}

