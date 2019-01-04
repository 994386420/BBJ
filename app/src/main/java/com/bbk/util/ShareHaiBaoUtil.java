package com.bbk.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.bbk.activity.R;
import com.bbk.resource.Constants;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;

import java.io.File;
import java.util.List;

/**
 * 拉起微信，朋友圈功能类，支持单张图片，多张图片，文字
 */
public class ShareHaiBaoUtil {
    private ShareHaiBaoManager shareManager;
    Activity context;
    private String content;
    List<String> DetailimgUrlList;
    public ShareHaiBaoUtil(Activity context, View view, String content, List<String> DetailimgUrlList){
        this.context=context;
        this.content=content;
        this.DetailimgUrlList = DetailimgUrlList;
        showpop(view);
    }

    private void showpop(View view) {
        View dialogView = LayoutInflater.from(context).inflate(
                R.layout.share_fenxiang_layout, null);
        final PopupWindow popupWindow = new PopupWindow(dialogView,
                BaseTools.getWindowsWidth(context), LayoutParams.WRAP_CONTENT);
        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogView.findViewById(R.id.share_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isWeixinAvilible(context)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(context);
                            share(1,content);
                        } else {
                            StringUtil.showToast(context, "请安装微信客户端");
                        }
                        popupWindow.dismiss();
                    }
                });
        dialogView.findViewById(R.id.share_weixin1).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isWeixinAvilible(context)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(context);
                            share(0,"");
                        } else {
                            StringUtil.showToast(context, "请安装微信客户端");
                        }
                        popupWindow.dismiss();
                    }
                });

        dialogView.findViewById(R.id.topbar_goback).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
        dialogView.findViewById(R.id.ll_copy).setVisibility(View.GONE);
        dialogView.findViewById(R.id.tv1).setVisibility(View.GONE);
        dialogView.findViewById(R.id.tv2).setVisibility(View.GONE);
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

