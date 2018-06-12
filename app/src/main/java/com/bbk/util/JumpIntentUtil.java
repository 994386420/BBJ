package com.bbk.util;

import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.SearchBean;
import com.bbk.Bean.SearchResultBean;

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

    public static boolean isJump4(List<SearchResultBean> itemList, int position){
        if("beibei".equals(itemList.get(position).getDomain())
                ||"jd".equals(itemList.get(position).getDomain()) ||"taobao".equals(itemList.get(position).getDomain())
                ||"tmall".equals(itemList.get(position).getDomain()) ||"suning".equals(itemList.get(position).getDomain())){
            return true;
        }
        return false;
    }
    public static boolean isJump5 (List<NewHomeCzgBean> itemList, int position){
        if("beibei".equals(itemList.get(position).getDomain())
                ||"jd".equals(itemList.get(position).getDomain()) ||"taobao".equals(itemList.get(position).getDomain())
                ||"tmall".equals(itemList.get(position).getDomain()) ||"suning".equals(itemList.get(position).getDomain())){
            return true;
        }
        return false;
    }
}
