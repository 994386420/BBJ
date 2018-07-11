package com.bbk.activity;

import android.os.Bundle;
import android.util.Log;

import com.bbk.util.EventIdIntentUtil;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/11/011.
 */

public class EventIdIntentActivity extends BaseActivity{
    String customContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customContent =  getIntent().getStringExtra("customContent");
        XGPushClickedResult message = XGPushManager.onActivityStarted(this);
        if(message != null) {
             if(isTaskRoot()) {
                 return;
         }
        // 如果有面板存在则关闭当前的面板
        finish();
      }
        try {
            JSONObject obj = new JSONObject(customContent);
			if (!obj.isNull("eventId")) {
				String eventId = obj.getString("eventId");
                Log.i("eventId======",eventId+"========");
				EventIdIntentUtil.EventIdIntent(this, obj);
				finish();
			}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
