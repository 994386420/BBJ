package com.bbk.util;

import java.text.SimpleDateFormat;

import static com.bbk.util.MD5Util.Md5;

/**
 * Created by rtj on 2018/2/28.
 */

public class JiaMiUtil {
    public static String jiami(String phone){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String date = sDateFormat.format(new java.util.Date());
        String v = Md5(phone+date);
        v = ("worini"+v);
        v = Md5(v);
        v = (v+"cao123");
        v = Md5(v);
        v = v.substring(0,16);
        v = "bbjtech"+v;
        v = Md5(v);
        return v;
    }
}
