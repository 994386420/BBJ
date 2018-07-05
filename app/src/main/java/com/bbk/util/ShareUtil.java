package com.bbk.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.wxapi.WXEntryActivity;
import com.bbk.client.BaseApiService;
import com.bbk.flow.DataFlow;
import com.bbk.resource.Constants;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.HashMap;
import java.util.Map;

public class ShareUtil {
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
	
	
	public ShareUtil(final Activity activity,String title,String content,String url,String type) {
		this.title = title;
		this.content = content;
		this.url = url;
		this.activity = activity;
		this.type = type;
	}
	public static void showShareDialog(View view, Activity activity) {
		ShareUtil shareUtil = new ShareUtil(activity,null,null,null,"");
		shareUtil.showShareDialog(view);
	}
	public static void showShareDialog(View view, Activity activity,String title,String content,String url,String type) {
		ShareUtil shareUtil = new ShareUtil(activity,title,content,url,type);
		shareUtil.showShareDialog(view);
	}

	public void showShareDialog(View view) {
		View dialogView = LayoutInflater.from(activity).inflate(
				R.layout.share_layout, null);

		final PopupWindow popupWindow = new PopupWindow(dialogView,
				BaseTools.getWindowsWidth(activity), LayoutParams.WRAP_CONTENT);
		//防止虚拟软键盘被弹出菜单遮住
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		dialogView.findViewById(R.id.share_qq).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShareUtil.this.qqShare(qqListener);
						popupWindow.dismiss();
					}
				});

		dialogView.findViewById(R.id.share_weixin).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShareUtil.this.share2weixin(1);
						popupWindow.dismiss();
					}
				});
		dialogView.findViewById(R.id.share_weixin1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShareUtil.this.share2weixin(0);
						popupWindow.dismiss();
					}
				});

		dialogView.findViewById(R.id.share_weibo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ShareUtil.this.sendMultiMessage(true);
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
	 * qq分享
	 */
	public void qqShare(IUiListener iUiListener) {
		mTencent = Tencent.createInstance(Constants.QQ_APP_ID, activity);
		Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		
		if(title == null || content == null || url == null){
			params.putString(QQShare.SHARE_TO_QQ_TITLE, "比比鲸上线了"); // 分享标题
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "比比鲸上线了，赶紧下载去吧"); // 分享摘要
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,"http://a.app.qq.com/o/simple.jsp?pkgname=com.bbk.activity"); // 分享链接
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,"http://www.bibkan.com/bibijing1.png"); // 图片链接
		}else{
			params.putString(QQShare.SHARE_TO_QQ_TITLE, title); // 分享标题
			params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content); // 分享摘要
			params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url); // 分享链接
		}
		mTencent.shareToQQ(activity, params, iUiListener);
		loadData();
	}

	/**
	 * 微信分享
	 * 
	 * @param flag
	 *            1是朋友圈 0是好友
	 */
	public void share2weixin(int flag) {
		wxApi = WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID);
		WXWebpageObject webpageObject = new WXWebpageObject();
		if(title == null || content == null || url == null) {
			webpageObject.webpageUrl = "http://www.bibijing.com/bibijing.apk";
		}else{
			webpageObject.webpageUrl = url;
		}
		WXMediaMessage msg = new WXMediaMessage(webpageObject);
		if(title == null || content == null || url == null){
			webpageObject.webpageUrl = "http://www.bibijing.com/bibijing.apk";
			msg.title = "快去下载比比鲸吧！";
			msg.description = "用数据悄悄告诉你什么值得买";
		}else {
			if(flag == 1){
				msg.title = content;
			}else {
				msg.title = title;
			}
			msg.description = content;
		}
		Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(),
				R.mipmap.icon_logo);
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = flag;
		if (flag == 1){
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}else {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}
		wxApi.sendReq(req);
		loadData();
	}

	/**
	 * type 1、查询历史价格	2、分享鲸港圈3、分享文章 4爆料
	 */
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
			if(title == null || content == null || url == null){
				TextObject textObject = new TextObject();
				textObject.text = "比比鲸上线了，赶紧去下载吧 http://a.app.qq.com/o/simple.jsp?pkgname=com.bbk.activity ";
				weiboMessage.textObject = textObject;
			}else{
				TextObject textObject = new TextObject();
				textObject.text = title+"，"+content+" "+url+" ";
				weiboMessage.textObject = textObject;
			}
			
		}
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		mWeiboShareAPI.sendRequest(activity, request); // 发送请求消息到微博，唤起微博分享界面
		loadData();
	}
}
