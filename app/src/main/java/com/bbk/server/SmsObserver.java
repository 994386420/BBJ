package com.bbk.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bbk.activity.UserRegistActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

@SuppressLint("Instantiatable")
public class SmsObserver extends ContentObserver {  
	  
    private Context mContext;  
    private Handler mHandler;  
  
	@SuppressLint("Instantiatable")
	public SmsObserver(Context context, Handler handler) {  
        super(handler);  
        this.mContext = context;  
        this.mHandler = handler;  
    }  
  
    @SuppressLint("NewApi")
	@Override  
    public void onChange(boolean selfChange, Uri uri) {  
        super.onChange(selfChange, uri);  
        // 接收到短信时，onChange会调用两次。第一次还没有将信息写入数据库中。所以我们不处理  
        if (uri.toString().equals("content://sms/raw")) {  
            return;  
        }  
  
        String code = "";  
  
        Uri inboxUri = Uri.parse("content://sms/inbox");  
  
        // 按短信ID倒序排序，避免修改手机时间数据不正确  
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null,  
                null, "_id desc");  
        if (c != null) {  
            if (c.moveToFirst()) {  
                String address = c.getString(c.getColumnIndex("address"));// 发送人号码  
                String body = c.getString(c.getColumnIndex("body")); // 短信内容  
  
                // 读取指定号码的短信内容  
                if (!address.equals("18960405xxx")) {  
                    return;  
                }  
  
                // 正则表达式。表示连续6位的数字,可以在这边修改成自己所要的格式  
                Pattern pattern = Pattern.compile("(\\d{6})");  
                // 匹配短信内容  
                Matcher matcher = pattern.matcher(body);  
                if (matcher.find()) {  
                    code = matcher.group(0);  
                    mHandler.obtainMessage(UserRegistActivity.MSG_RECEIVED_CODE, code)  
                            .sendToTarget();  
                }  
            }  
        }  
    }  
}  
  
