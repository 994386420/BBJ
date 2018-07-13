package com.bbk.resource;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by 陈维 on 2018/4/25.
 */

public class NewConstants {
    public static HashMap<String, Object> mChatMap = new HashMap<>();
    public static HashMap<String, Object> mChatNickMameMap = new HashMap<>();
    public static String Flag;
    public static String logFlag;
    public static String copyText;
    public static int clickpositionFenlei, clickpositionDianpu, clickpositionMall,messages;

    public static HashMap<String, Object> getJsonObject(String json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
