package com.bbk.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.bbk.activity.AboutUsActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.WelcomeActivity;
import com.bbk.activity.ZhongzhuanActivity;
import com.bbk.fragment.HomeFragment2;

import org.json.JSONObject;

/**
 * Created by rtj on 2017/12/8.
 */

public class SchemeIntentUtil {
    public static void intent(Uri uri,Context context){

//        Uri uri = Uri.parse("bbjtech://?eventId=12&keyword=连衣裙");
        if (uri != null) {
            try {
                JSONObject jsonObject = new JSONObject();
//            String host = uri.getHost();
//            String dataString = intent.getDataString();
                String eventId = uri.getQueryParameter("eventId");
                jsonObject.put("eventId", eventId);
                if (uri.getQueryParameter("htmlUrl")!=null) {
                    String htmlUrl = uri.getQueryParameter("htmlUrl");
                    jsonObject.put("htmlUrl", htmlUrl);
                }
                if (uri.getQueryParameter("groupRowkey")!=null) {
                    String groupRowkey = uri.getQueryParameter("groupRowkey");
                    jsonObject.put("groupRowkey", groupRowkey);
                }
                if (uri.getQueryParameter("rankType")!=null) {
                    String rankType = uri.getQueryParameter("rankType");
                    jsonObject.put("rankType", rankType);
                }
                if (uri.getQueryParameter("keyword")!=null) {
                    String keyword = uri.getQueryParameter("keyword");
                    jsonObject.put("keyword", keyword);
                }
                if (uri.getQueryParameter("url")!=null) {
                    String url = uri.getQueryParameter("url");
                    jsonObject.put("url", url);

                }
                HomeFragment2.iscontent = 0;
                if (HomeActivity.instance!= null){
                    HomeActivity.instance.finish();
                }
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.putExtra("content", jsonObject.toString());
                context.startActivity(intent1);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

//            String path = uri.getPath();
//            String path1 = uri.getEncodedPath();
//            String queryString = uri.getQuery();
        }
    }
}
