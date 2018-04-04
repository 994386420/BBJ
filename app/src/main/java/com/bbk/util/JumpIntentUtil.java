package com.bbk.util;

import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/2/24.
 */

public class JumpIntentUtil {
    public static boolean isJump(List<Map<String, Object>> itemList,int position ,String key){
        if("beibei".equals(itemList.get(position).get(key).toString())
            ||"jd".equals(itemList.get(position).get(key).toString()) ||"taobao".equals(itemList.get(position).get(key).toString())
                ||"tmall".equals(itemList.get(position).get(key).toString()) ||"suning".equals(itemList.get(position).get(key).toString())){
            return true;
        }
        return false;
    }
    public static boolean isJump2(List<Map<String, String>> itemList,int position ,String key){
        if("beibei".equals(itemList.get(position).get(key))
            ||"jd".equals(itemList.get(position).get(key)) ||"taobao".equals(itemList.get(position).get(key))
                ||"tmall".equals(itemList.get(position).get(key)) ||"suning".equals(itemList.get(position).get(key))){
            return true;
        }
        return false;
    }
    public static boolean isJump1(String domain){
        if("beibei".equals(domain)
            ||"jd".equals(domain) ||"taobao".equals(domain)
                ||"tmall".equals(domain) ||"suning".equals(domain)){
            return true;
        }
        return false;
    }
}
