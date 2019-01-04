package com.bbk.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.wxapi.WXEntryActivity;
import com.bbk.client.BaseApiService;
import com.bbk.flow.DataFlow;
import com.bbk.resource.Constants;
import com.bbk.wxpay.Util;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.logg.Logg;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShareJumpUtil {
    /** qq分享接口 */
    private Tencent mTencent;
    /** qq回调事件 **/
    private IUiListener qqListener;
    /** 微博分享接口 */
    private IWeiboShareAPI mWeiboShareAPI = null;
    /** 微信分享接口 */
    private IWXAPI wxApi;
    private Activity activity;

    private String title;
    private String content;
    private String url;
    private String type = "";
    private String imageurl,wenan;
    private Bitmap bitmap;
    Handler handler = new Handler();


    public ShareJumpUtil(final Activity activity, String title, String content, String url, String type,String imageurl,String wenan,Bitmap bitmap) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.activity = activity;
        this.type = type;
        this.imageurl = imageurl;
        this.wenan = wenan;
        this.bitmap = bitmap;
    }
    public static void showShareDialog(View view, Activity activity) {
        ShareUtil shareUtil = new ShareUtil(activity,null,null,null,"");
        shareUtil.showShareDialog(view);
    }
    public static void showShareDialog(View view, Activity activity, String title, String content, String url, String imgurl,String type, ImageView imageView,String wenan,Bitmap bitmap) {
        ShareJumpUtil shareUtil = new ShareJumpUtil(activity,title,content,url,imgurl,type,wenan,bitmap);
        shareUtil.showShareDialog(view,imageView);
    }

    public void showShareDialog(View view, final ImageView imageView) {
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.share_layout, null);

        final PopupWindow popupWindow = new PopupWindow(dialogView,
                BaseTools.getWindowsWidth(activity), LayoutParams.WRAP_CONTENT);
        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialogView.findViewById(R.id.ll_copy).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.share_qq).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ShareJumpUtil.this.qqShare(qqListener);
//                        Logg.e(imageurl);
//                        StringUtil.showToast(activity,imageurl);

                        if (StringUtil.isWeixinAvilible(activity)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(activity);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        ShareManager.sharedToQQ(activity, bitmap);
                                    }
                                }
                            },0);
                        } else {
                            StringUtil.showToast(activity, "请安装QQ客户端");
                        }
                        popupWindow.dismiss();
                    }
                });

        dialogView.findViewById(R.id.share_weixin).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isWeixinAvilible(activity)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(activity);
                            share2weixin(1,imageView);
                            ClipboardManager cm = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(wenan);
                        } else {
                            StringUtil.showToast(activity, "请安装微信客户端");
                        }
                        popupWindow.dismiss();
                    }
                });
        dialogView.findViewById(R.id.share_weixin1).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isWeixinAvilible(activity)) {// 判断是否安装微信客户端
                            DialogSingleUtil.show(activity);
                            share2weixin(0,imageView);
                        } else {
                            StringUtil.showToast(activity, "请安装微信客户端");
                        }
                        popupWindow.dismiss();
                    }
                });

        dialogView.findViewById(R.id.share_weibo).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShareJumpUtil.this.sendMultiMessage(true);
                        popupWindow.dismiss();
                    }
                });

        dialogView.findViewById(R.id.cannel_btn).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
        dialogView.findViewById(R.id.mcopyurl).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(url);
                        popupWindow.dismiss();
                        StringUtil.showToast(activity,"复制成功");
                    }
                });

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);

        popupWindow.setAnimationStyle(R.style.AnimationPreview);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }
    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            // 在这里进行 http request.网络请求相关操作
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * qq分享
     */
    public void qqShare(IUiListener iUiListener) {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title); // 分享标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content); // 分享摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url); // 分享链接
        //分享的图片URL
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                imageurl);
        mTencent.shareToQQ(activity, params, iUiListener);
        loadData();
    }

    /**
     * 微信分享
     *
     * @param flag
     *            1是朋友圈 0是好友
     */
    public void share2weixin(int flag,ImageView imageView) {
        wxApi = WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID);
        if (imageView !=null) {
            if (imageView.getDrawable()!=null) {
                Bitmap thumb = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
                WXImageObject wxImageObject = new WXImageObject(thumb);
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = wxImageObject;
                Bitmap bitmap = Bitmap.createScaledBitmap(thumb, 120, 120, true);
//        thumb.recycle();
                msg.thumbData = Util.bmpToByteArray(bitmap, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("img");
                req.message = msg;
                req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                wxApi.sendReq(req);
                DialogSingleUtil.dismiss(0);
                loadData();
            }
        }
    }
    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                String url1 = BaseApiService.Base_URL+"newService/checkIsShare";
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("userid", userid);
                paramsMap.put("type", type);
                String s = HttpUtil.getHttp(paramsMap, url1);
            }
        }).start();
    }
    /**
     * 构建一个唯一标志
     *
     * @param type 分享的类型分字符串
     * @return 返回唯一字符串
     */
    private static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
    /**
     * 微博分享
     *
     *
     */
    public void sendMultiMessage(boolean hasText) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity,
                Constants.WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// 初始化微博的分享消息
        if (hasText) {
                TextObject textObject = new TextObject();
                textObject.text = title+"，"+content+" "+url+" ";
                weiboMessage.textObject = textObject;
        }
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(activity, request); // 发送请求消息到微博，唤起微博分享界面
        loadData();
    }
}
