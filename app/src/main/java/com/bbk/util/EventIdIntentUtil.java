package com.bbk.util;

import org.json.JSONObject;
import org.w3c.dom.Text;

import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.CouponActivity;
import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.RankCategoryActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewActivity111;
import com.bbk.activity.WebViewActivity_copy;
import com.bbk.activity.WebViewRechargeActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.activity.WebViewXGActivity;
import com.bbk.activity.WelcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class EventIdIntentUtil {

	public static void main(String[] args) {

	}
	public static void EventIdIntent(Context context,JSONObject jo){
		String eventId = jo.optString("eventId");
		switch (eventId) {
		case "5":
			String htmlUrl = jo.optString("htmlUrl");
			Intent intent4;
			if (htmlUrl.contains("@@")){
				if (htmlUrl.contains("user")){
					String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");

					if (TextUtils.isEmpty(userID)){
						intent4= new Intent(context, UserLoginNewActivity.class);
						intent4.putExtra("url", htmlUrl);
						context.startActivity(intent4);
					}else {
						String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
						intent4= new Intent(context, WebViewActivity111.class);
						intent4.putExtra("url", htmlUrl);
						intent4.putExtra("mid",mid);
						context.startActivity(intent4);
					}
				}else {
					String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
					if (TextUtils.isEmpty(mid)){
						mid = "";
					}
					intent4= new Intent(context, WebViewActivity111.class);
					intent4.putExtra("url", htmlUrl);
					intent4.putExtra("mid",mid);
					context.startActivity(intent4);
				}
			}else {
				intent4= new Intent(context, WebViewActivity.class);
				intent4.putExtra("url", htmlUrl);
				context.startActivity(intent4);
			}


			break;
		case "6":
			String groupRowkey = jo.optString("groupRowkey");
			Intent intent5 = new Intent(context, DetailsMainActivity22.class);
			intent5.putExtra("groupRowKey", groupRowkey);
			context.startActivity(intent5);
			break;
		case "10":
			String htmlUrl1 = jo.optString("htmlUrl");
			Intent intent9 = new Intent(context, WebViewRechargeActivity.class);
			intent9.putExtra("htmlUrl", htmlUrl1);
			context.startActivity(intent9);
			break;
		case "11":
			String rankType = jo.optString("rankType");
			Intent intent10 = new Intent(context, RankCategoryActivity.class);
			intent10.putExtra("type", rankType);
			context.startActivity(intent10);
			break;
		case "12":
			String keyword = jo.optString("keyword");
			Intent intent11 = new Intent(context, SearchMainActivity.class);
			intent11.putExtra("keyword", keyword);
			context.startActivity(intent11);
			break;
		case "13":
			String htmlUrl13 = jo.optString("htmlUrl");
			Intent intent13 = new Intent(context, WebViewActivity.class);
			intent13.putExtra("url", htmlUrl13);
			context.startActivity(intent13);
			break;
		case "14":
			String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
			Intent intent14;
			if (TextUtils.isEmpty(userID)){
				intent14 = new Intent(context, UserLoginNewActivity.class);
				intent14.putExtra("wzurl", jo.optString("url"));
				context.startActivity(intent14);
			}else {
				intent14 = new Intent(context, WebViewWZActivity.class);
				intent14.putExtra("url", jo.optString("url")+"&userid="+userID);
				context.startActivity(intent14);
			}

			break;
		case "102":
			Intent intent102 = new Intent(context, CollectionActivity.class);
			intent102.putExtra("type", "1");
			context.startActivity(intent102);
			break;
		case "103":
			HomeActivity.initfour();
			break;
		case "104":
			Intent intent104 = new Intent(context, CouponActivity.class);
			context.startActivity(intent104);
			break;
		case "105":
			HomeActivity.inittwo();
			break;
		case "106":
			HomeActivity.initone();
			break;
		case "107":
			HomeActivity.initThree();
			break;
			case "108":
//				SharedPreferencesUtil.putSharedData(context, "Bidhomeactivty", "type", "2");
//				Intent intent = new Intent(context, BidHomeActivity.class);
//				context.startActivity(intent);

				break;
		case "666":
			String url = jo.optString("url");
			Intent intent666 = new Intent(context, WebViewActivity_copy.class);
			intent666.putExtra("url",url);
			context.startActivity(intent666);
			break;

		default:
			break;
		}
		if (WelcomeActivity.instance!= null){
			WelcomeActivity.instance.finish();
		}
	}
}
