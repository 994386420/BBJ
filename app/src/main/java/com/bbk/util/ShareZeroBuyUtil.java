package com.bbk.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
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
import com.bbk.client.BaseApiService;
import com.bbk.resource.Constants;
import com.bbk.wxpay.Util;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareZeroBuyUtil{
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
    private ShareManager shareManager;
    List<String> DetailimgUrlList;
    Handler handler = new Handler();


    public ShareZeroBuyUtil(final Activity activity, String title, String content, String url, String type, String imageurl, String wenan, Bitmap bitmap,List<String> DetailimgUrlList) {
        this.title = title;
        this.content = content;
        this.url = url;
        this.activity = activity;
        this.type = type;
        this.imageurl = imageurl;
        this.wenan = wenan;
        this.bitmap = bitmap;
        this.DetailimgUrlList = DetailimgUrlList;
    }
    public static void showShareDialog(View view, Activity activity) {
        ShareUtil shareUtil = new ShareUtil(activity,null,null,null,"");
        shareUtil.showShareDialog(view);
    }
    public static void showShareDialog(View view, Activity activity, String title, String content, String url, String imgurl,String type, ImageView imageView,String wenan,Bitmap bitmap,List<String> DetailimgUrlList) {
        ShareZeroBuyUtil shareUtil = new ShareZeroBuyUtil(activity,title,content,url,imgurl,type,wenan,bitmap,DetailimgUrlList);
        shareUtil.showShareDialog(view,imageView);
    }

    public void showShareDialog(View view, final ImageView imageView) {
        View dialogView = LayoutInflater.from(activity).inflate(
                R.layout.share_zerobuy_layout, null);

        final PopupWindow popupWindow = new PopupWindow(dialogView,
                BaseTools.getWindowsWidth(activity), LayoutParams.WRAP_CONTENT);
        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        dialogView.findViewById(R.id.ll_copy).setVisibility(View.VISIBLE);
        dialogView.findViewById(R.id.share_qq).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtil.isQQAvilible(activity)) {// 判断是否安装qq客户端
                            DialogSingleUtil.show(activity);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (bitmap != null) {
                                        //1为QQ空间 0为QQ好友
                                        sharePicToQQ(activity, 1,bitmap );
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
                            share(activity,1,"");
                            ClipboardManager cm = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            cm.setText(wenan);
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
                        ShareZeroBuyUtil.this.sendMultiMessage(true);
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
    private void share(Context context,int i, String s) {
        shareManager = new ShareManager(context);
        shareManager.setShareImage(i,DetailimgUrlList,s);
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

    /**
     * 分享图片到 QQ 、QQ 空间
     */
    public  void sharePicToQQ(Activity activity, int flag, Bitmap bitmap) {
        ShareManager.bitMap2File(bitmap);
        String picPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            picPath = Environment.getExternalStorageDirectory() + File.separator+"share" + ".jpg";
        }
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
        ShareListener myListener = new ShareListener();
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, picPath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, activity.getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
        mTencent.shareToQQ(activity, params, myListener);
        DialogSingleUtil.dismiss(0);
        SharedPreferencesUtil.putSharedData(activity, "isShare", "isShare", "1");
        loadData();
    }

    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
        }

        @Override
        public void onComplete(Object arg0) {
        }

        @Override
        public void onError(UiError arg0) {
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
}
