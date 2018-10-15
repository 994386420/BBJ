package com.bbk.resource;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by 陈维 on 2018/4/25.
 */

public class NewConstants {
    public static HashMap<String, Object> mMessageMap = new HashMap<>();//点击
    public static HashMap<String, Object> mJdMessageMap = new HashMap<>();//点击
    public static HashMap<String, Object> mJdzycsMessageMap = new HashMap<>();
    public static HashMap<String, Object> mJdzyMessageMap = new HashMap<>();
    public static HashMap<String, Object> mChatMap = new HashMap<>();
    public static HashMap<String, Object> mChatNickMameMap = new HashMap<>();
    public static String Flag,yingdaoFlag = "2",showdialogFlg = "0",car = "1",refeshFlag = "0",refeshOrderFlag = "0",address,liuyan;
    public static String logFlag;
    public static String copyText,imgurl;
    public static int clickpositionFenlei, clickpositionDianpu, clickpositionMall,messages,postion,option,carnum = 0,jdCarNum = 0;
    public static int jdzyNum = 0,jdcsNum = 0;
//    public final static String PHONE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public final static String PHONE_PATTERN = "^1\\d{10}$";
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
