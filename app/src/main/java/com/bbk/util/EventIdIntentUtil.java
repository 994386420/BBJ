package com.bbk.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.activity.BidHomeActivity;
import com.bbk.activity.BrokerageActivity;
import com.bbk.activity.BrokerageDetailActivity;
import com.bbk.activity.CoinGoGoGoActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.CouponActivity;
import com.bbk.activity.DataFragmentActivity;
import com.bbk.activity.DetailsMainActivity22;
import com.bbk.activity.DianpuActivity;
import com.bbk.activity.FanLiOrderActivity;
import com.bbk.activity.FenXiangActivty;
import com.bbk.activity.FensiActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.NewRankActivty;
import com.bbk.activity.QueryHistoryActivity;
import com.bbk.activity.R;
import com.bbk.activity.RankCategoryActivity;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.activity.TuiguangDialogActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.UserShenSuActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.WebViewActivity111;
import com.bbk.activity.WebViewActivity_copy;
import com.bbk.activity.WebViewRechargeActivity;
import com.bbk.activity.WebViewWZActivity;
import com.bbk.activity.WebViewXGActivity;
import com.bbk.activity.WelcomeActivity;
import com.bbk.activity.YaoqingFriendsActivity;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.model.ChaoZhiGouTypesActivity;
import com.bbk.model.DianpuSearchActivity;
import com.bbk.model.JiFenActivity;
import com.bbk.model.ShopFenleiActivity;
import com.bbk.model.ZiYingZeroBuyShopActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.DianpuHomeActivity;
import com.bbk.shopcar.DianpuTypesActivity;
import com.bbk.shopcar.NewDianpuActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.view.AdaptionSizeTextView;
import com.bumptech.glide.Glide;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static com.bbk.util.ClipDialogUtil.url;

public class EventIdIntentUtil {
    private static AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private static Map<String, String> exParams;//yhhpass参数
    static String url;
    private static UpdataDialog updataDialog;
    private static String isFirstClick;
    private static Context contexte;
    private static String jumpdomain;
    private static boolean cancleJump = true;
    static KelperTask mKelperTask;
    private static Handler mHandler = new Handler();

    public static void main(String[] args) {

    }

    /**
     * //按eventId跳转:    1超值购    2超爆款    3潮潮潮    4美味生鲜    5html活动页面(htmlUrl)        6三级页面(groupRowkey)
     * 7超市    8全球购    9服饰    10充值    11榜单(type) 12搜索
     * 101回复评论,103签到(鲸币界面) 104优惠券,105爆料,106发现,107数据频道 109跳京东返利web  110淘宝返利web 111大转盘 112查历史价格 113鲸港圈  114收益报表  115我的订单 116粉丝
     * 117淘宝本月结算 118淘宝本月付款 119京东本月结算 120京东本月付款 121系统消息(非聊天) 122消息聊天  123首页banner拉起京东淘宝店铺 124经过jump跳cps三级详情 127发飙  128(9块9)  129秒杀  130好货拼团  131超高赚 135购物车
     *
     * @param context
     * @param jo
     */
    public static void EventIdIntent(Context context, JSONObject jo) {
        contexte = context;
        String eventId = jo.optString("eventId");
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (eventId) {
            //店铺
            case "a1":
                intent = new Intent(context, NewDianpuActivity.class);
                if (jo.has("keyword")) {
                    intent.putExtra("dianpuid", jo.optString("keyword"));
                }
                context.startActivity(intent);
                break;
                //店铺分类
            case "a2":
                intent = new Intent(context, DianpuTypesActivity.class);
                intent.putExtra("tag", jo.toString());
                intent.putExtra("keyword", jo.optString("keyword"));
                intent.putExtra("position", 0 + "");
                context.startActivity(intent);
                break;
                //自营商品详情
            case "a3":
                intent = new Intent(context, ShopDetailActivty.class);
                if (jo.has("keyword")) {
                    intent.putExtra("id", jo.optString("keyword"));
                }
                context.startActivity(intent);
                break;
                //店铺搜索结果界面
            case "a4":
                intent = new Intent(context, DianpuSearchActivity.class);
                intent.putExtra("keyword", "");
                intent.putExtra("dianpuid", "");
                intent.putExtra("producttype", jo.optString("keyword"));
                intent.putExtra("plevel", "2");
                context.startActivity(intent);
                break;
            //自营分类
            case "a5":
                intent = new Intent(context, ShopFenleiActivity.class);
                context.startActivity(intent);
                break;
                //自营首页
            case "xihujie":
                Intent intentxh;
                intentxh = new Intent(context, NewDianpuHomeActivity.class);
                context.startActivity(intentxh);
                break;
                //  5html活动页面(htmlUrl)
            case "4":
                String htmlUrlweizhu = jo.optString("htmlUrl");
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
//                    intent.putExtra("url", htmlUrlweizhu);
                    context.startActivity(intent);
                } else {
                    String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", htmlUrlweizhu + "&mid=" + mid);
                    context.startActivity(intent);
                }
                break;
            case "5":
                String htmlUrl = jo.optString("htmlUrl");
                Intent intent4;
                Logg.json(htmlUrl);
                if (htmlUrl.contains("@@")) {
                    if (htmlUrl.contains("user")) {
                        if (TextUtils.isEmpty(userID)) {
                            intent4 = new Intent(context, UserLoginNewActivity.class);
                            intent4.putExtra("url", htmlUrl);
                            context.startActivity(intent4);
                        } else {
                            String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                            intent4 = new Intent(context, WebViewActivity111.class);
                            intent4.putExtra("url", htmlUrl);
                            intent4.putExtra("mid", mid);
                            context.startActivity(intent4);
                        }
                    } else {
                        String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                        if (TextUtils.isEmpty(mid)) {
                            mid = "";
                        }
                        intent4 = new Intent(context, WebViewActivity111.class);
                        intent4.putExtra("url", htmlUrl);
                        intent4.putExtra("mid", mid);
                        context.startActivity(intent4);
                    }
                } else {
                    intent4 = new Intent(context, WebViewActivity.class);
                    intent4.putExtra("url", htmlUrl);
                    context.startActivity(intent4);
                }
                break;
                //三级页面(groupRowkey)
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
                try {
                    Intent intent11 = new Intent(context, SearchMainActivity.class);
                    intent11.putExtra("keyword", URLDecoder.decode(keyword , "utf-8"));
                    context.startActivity(intent11);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case "13":
                String htmlUrl13 = jo.optString("htmlUrl");
                Intent intent13 = new Intent(context, WebViewActivity.class);
                intent13.putExtra("url", htmlUrl13);
                context.startActivity(intent13);
                break;
            case "14":
                Intent intent14;
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    intent14.putExtra("wzurl", jo.optString("url"));
                    context.startActivity(intent14);
                } else {
                    intent14 = new Intent(context, WebViewWZActivity.class);
                    intent14.putExtra("url", jo.optString("url") + "&userid=" + userID);
                    context.startActivity(intent14);
                }

                break;
            case "102":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    Intent intent102 = new Intent(context, CollectionActivity.class);
                    intent102.putExtra("type", "1");
                    context.startActivity(intent102);
                }
                break;
            case "103":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    Intent intent103 = new Intent(context, MyCoinActivity.class);
                    context.startActivity(intent103);
                }
                break;
            case "104":
                Intent intent104 = new Intent(context, CouponActivity.class);
                context.startActivity(intent104);
                break;
            case "105":
                HomeActivity.position = 1;
                SharedPreferencesUtil.putSharedData(context, "homeactivty", "type", "2");
                intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                break;
            case "106":
                intent = new Intent(context, NewRankActivty.class);
                context.startActivity(intent);
                break;
            case "107":
                Intent intent107 = new Intent(context, DataFragmentActivity.class);
                context.startActivity(intent107);
                break;
            case "108":
                intent = new Intent(context, UserLoginNewActivity.class);
                context.startActivity(intent);

                break;
            /**
             * eventID 109
             *newService/ parseCpsDomainMainUrl   参数 userid domain="jd"
             *eventID 110
             *newService/ parseCpsDomainMainUrl   参数 userid domain="taobao"
             *跳转webview   ，返回url
             */
            case "109":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    parseCpsDomainMainUrl(context, userID, "jd");
                }
                break;
            case "110":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    parseCpsDomainMainUrl(context, userID, "taobao");
                }
                break;
            case "111":
                String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                if (!TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, WebViewActivity.class);
                    String url = BaseApiService.Base_URL + "mobile/user/lottery?mid=";
                    intent.putExtra("url", url + mid);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                }
                break;
            case "112":
                intent = new Intent(context, QueryHistoryActivity.class);
                context.startActivity(intent);
                break;
            case "113":
//				HomeActivity.position = 1;
//				SharedPreferencesUtil.putSharedData(context, "homeactivty", "type", "1");
                intent = new Intent(context, FenXiangActivty.class);
                context.startActivity(intent);
                break;
            case "114":
                intent = new Intent(context, BrokerageActivity.class);
                context.startActivity(intent);
                break;
            case "115":
                intent = new Intent(context, FanLiOrderActivity.class);
                context.startActivity(intent);
                break;
            case "116":
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, FensiActivity.class);
                    context.startActivity(intent);
                }
                break;
            case "117":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "t1");
                context.startActivity(intent);
                break;
            case "118":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "t3");
                context.startActivity(intent);
                break;
            case "119":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "j1");
                context.startActivity(intent);
                break;
            case "120":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "j3");
                context.startActivity(intent);
                break;
            case "121":
                SharedPreferencesUtil.putSharedData(context, "homeactivty", "type", "2");
                intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                break;
            case "122":
                SharedPreferencesUtil.putSharedData(context, "Bidhomeactivty", "type", "3");
                intent = new Intent(context, BidHomeActivity.class);
                intent.putExtra("currentitem", "2");
                context.startActivity(intent);
                break;
            case "123":
                alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                alibcShowParams.setClientType("taobao_scheme");
                exParams = new HashMap<>();
                exParams.put("isv_code", "appisvcode");
                exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                if (jo.optString("htmlUrl").contains("jd")) {
                    try {
                        KeplerApiManager.getWebViewService().openJDUrlPage(url, mKeplerAttachParameter, context, mOpenAppAction, 1500);
                    } catch (KeplerBufferOverflowException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showUrl(context, jo.optString("htmlUrl"));
                }
                break;
                //jump页面
            case "124":
                NewConstants.showdialogFlg = "1";
                intent = new Intent(context, IntentActivity.class);
                intent.putExtra("url", jo.optString("htmlUrl"));
                context.startActivity(intent);
                break;
                //申诉
            case "125":
                intent = new Intent(context, UserShenSuActivity.class);
                intent.putExtra("status", "1");
                context.startActivity(intent);
                break;

            case "126":
                intent = new Intent(context, CoinGoGoGoActivity.class);
                intent.putExtra("type", "0");
                context.startActivity(intent);
                break;
                //发飙
            case "127":
                intent = new Intent(context, BidHomeActivity.class);
                context.startActivity(intent);
                break;
            //9块9
            case "128":
                intent = new Intent(context, ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "1");
                context.startActivity(intent);
                break;
                //秒杀
            case "129":
                intent = new Intent(context, ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "4");
                context.startActivity(intent);
                break;
            //好货拼团
            case "130":
                intent = new Intent(context, ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "3");
                context.startActivity(intent);
                break;
            //超高赚
            case "131":
                intent = new Intent(context, ChaoZhiGouTypesActivity.class);
                intent.putExtra("type", "2");
                context.startActivity(intent);
                break;
            case "132":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    parseCpsDianpuMainUrl(context, userID, "taobao", jo.optString("keyword"), jo.optString("htmlUrl"));
                }
                break;
            case "133":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    parseCpsDianpuMainUrl(context, userID, "jd", jo.optString("keyword"), jo.optString("htmlUrl"));
                }
                break;
                //邀请好友
            case "134":
                if (TextUtils.isEmpty(userID)) {
                    intent14 = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent14);
                } else {
                    intent = new Intent(context, YaoqingFriendsActivity.class);
                    context.startActivity(intent);
                }
                break;
            //购物车
            case "135":
                intent = new Intent(context, CarActivity.class);
                context.startActivity(intent);
                break;
            //136 首页底部分类
            case "136":
                HomeActivity.position = 1;
				SharedPreferencesUtil.putSharedData(context, "homeactivty", "type", "1");
                intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                break;
            //137新用户0元购
            case "137":
                intent = new Intent(context, ZiYingZeroBuyShopActivity.class);
                intent.putExtra("isOlder","no");
                context.startActivity(intent);
                break;
            //138老用户0元购
            case "138":
                intent = new Intent(context, ZiYingZeroBuyShopActivity.class);
                intent.putExtra("isOlder","yes");
                context.startActivity(intent);
                break;

            //140自营订单页面
            case "140":
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, ShopOrderActivity.class);
                    context.startActivity(intent);
                }
                break;
            //141自营订单页面-待付款
            case "141":
                mShopOrder(context,userID, "1");
                break;
            //142自营订单页面-待发货
            case "142":
                mShopOrder(context,userID, "2");
                break;
            //143自营订单页面-待收货
            case "143":
                mShopOrder(context,userID, "3");
                break;
            //144购物车 -京东
            case "144":
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, CarActivity.class);
                    intent.putExtra("ziying","jd");
                    context.startActivity(intent);
                }
                break;
            //145购物车-自营
            case "145":
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, CarActivity.class);
                    intent.putExtra("ziying","yes");
                    context.startActivity(intent);
                }
                break;
            case "666":
                String url = jo.optString("url");
                Intent intent666 = new Intent(context, WebViewActivity_copy.class);
                intent666.putExtra("url", url);
                context.startActivity(intent666);
                break;

            default:
                break;
        }
        if (WelcomeActivity.instance != null) {
            WelcomeActivity.instance.finish();
        }
    }

    /**
     * 订单跳转
     * @param context
     * @param userid
     * @param value
     */
    private static void mShopOrder(Context context, String userid, String value) {
        Intent intent;
        if (TextUtils.isEmpty(userid)) {
            intent = new Intent(context, UserLoginNewActivity.class);
            context.startActivity(intent);
        } else {
            intent = new Intent(context, ShopOrderActivity.class);
            intent.putExtra("status", value);
            context.startActivity(intent);
        }
    }


    public static void parseCpsDianpuMainUrl(final Context context, final String userid, final String domain, final String url, String zurl) {
        Map<String, String> maps = new HashMap<String, String>();
        Logg.e(userid + "===" + domain + "===" + url + "===" + zurl);
        maps.put("userid", userid);
        maps.put("domain", domain);
        maps.put("url", url);
        maps.put("zurl", zurl);
        RetrofitClient.getInstance(context).createBaseApi().parseCpsDianpuMainUrl(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        Logg.json("===", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                final JSONObject jsonObject1 = new JSONObject(content);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        jumpUrl(domain, jsonObject1, context);
                                    }
                                },1500);
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                    }

                    @Override
                    protected void showDialog() {
                        showLoadingDialog(context, url);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        updataDialog.dismiss();
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

    public static void parseCpsDomainMainUrl(final Context context, final String userid, final String domain) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userid);
        maps.put("domain", domain);
        RetrofitClient.getInstance(context).createBaseApi().parseCpsDomainMainUrl(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                final JSONObject jsonObject1 = new JSONObject(content);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        jumpUrl(domain, jsonObject1, context);
                                    }
                                },1500);
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(context);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

    /**
     * 打开指定链接
     */
    public static void showUrl(Context context, String url) {
        String text = url;
        if (TextUtils.isEmpty(text)) {
            StringUtil.showToast(context, "URL为空");
            return;
        }
        AlibcTrade.show((Activity) context, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
        DialogSingleUtil.dismiss(100);
    }


    private static KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    static OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status) {
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
            } else {
                mKelperTask = null;
                DialogSingleUtil.dismiss(0);
            }
        }
    };

    /**
     * 成为合伙人
     */
    private static void updateCooperationByUserid(final Context context, String userID, final String url, final String domain) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(context).createBaseApi().updateCooperationByUserid(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            Intent intent;
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(context, "恭喜成为合伙人");
                                alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                                alibcShowParams.setClientType("taobao_scheme");
                                exParams = new HashMap<>();
                                exParams.put("isv_code", "appisvcode");
                                exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                                if (domain != null) {
                                    if (domain.equals("taobao")) {
                                        showUrl(context, url);
                                    } else if (domain.equals("jd")) {
                                        try {
                                            KeplerApiManager.getWebViewService().openJDUrlPage(url, mKeplerAttachParameter, context, mOpenAppAction, 1500);
                                        } catch (KeplerBufferOverflowException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

    /**
     * 不是合伙人弹窗
     *
     * @param context
     * @param useid
     */
    public static void showMessageDialog(final Context context, final String useid, final String url, final String domain) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.hehuo_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_refuse = updataDialog.findViewById(R.id.tv_update_refuse);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    updateCooperationByUserid(context, useid, url, domain);
                }
            });
        }
    }

    /**
     * 跳转京东淘宝APP
     *
     * @param domain
     * @param jsonObject1
     * @param context
     */
    public static void jumpUrl(String domain, JSONObject jsonObject1, Context context) {
        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        alibcShowParams.setClientType("taobao_scheme");
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        updataDialog.dismiss();
        if (domain != null) {
            if (domain.equals("taobao")) {
                showUrl(context, jsonObject1.optString("url"));
            } else if (domain.equals("jd")) {
                DialogSingleUtil.dismiss(100);
                try {
                    KeplerApiManager.getWebViewService().openJDUrlPage(url, mKeplerAttachParameter, context, mOpenAppAction, 1500);
                } catch (KeplerBufferOverflowException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void EventIdIntent(Context context, String eventId, String htmlUrl) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (eventId) {
            case "4":
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                } else {
                    String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra("url", htmlUrl+"&mid="+mid);
                    context.startActivity(intent);
                }
                break;
            case "5":
                Intent intent4;
                if (htmlUrl.contains("@@")) {
                    if (htmlUrl.contains("user")) {
                        if (TextUtils.isEmpty(userID)) {
                            intent4 = new Intent(context, UserLoginNewActivity.class);
                            intent4.putExtra("url", htmlUrl);
                            context.startActivity(intent4);
                        } else {
                            String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                            intent4 = new Intent(context, WebViewActivity111.class);
                            intent4.putExtra("url", htmlUrl);
                            intent4.putExtra("mid", mid);
                            context.startActivity(intent4);
                        }
                    } else {
                        String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                        if (TextUtils.isEmpty(mid)) {
                            mid = "";
                        }
                        intent4 = new Intent(context, WebViewActivity111.class);
                        intent4.putExtra("url", htmlUrl);
                        intent4.putExtra("mid", mid);
                        context.startActivity(intent4);
                    }
                } else {
                    intent4 = new Intent(context, WebViewActivity.class);
                    intent4.putExtra("url", htmlUrl);
                    context.startActivity(intent4);
                }
                break;
            case "102":
                Intent intent102 = new Intent(context, CollectionActivity.class);
                intent102.putExtra("type", "1");
                context.startActivity(intent102);
                break;
            case "103":
                Intent intent103 = new Intent(context, MyCoinActivity.class);
                context.startActivity(intent103);
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
                Intent intent107 = new Intent(context, DataFragmentActivity.class);
                context.startActivity(intent107);
                break;
            case "108":
                intent = new Intent(context, UserLoginNewActivity.class);
                context.startActivity(intent);
                break;
            case "111":
                String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
                if (!TextUtils.isEmpty(userID)) {
                    intent = new Intent(context, WebViewActivity.class);
                    String url = BaseApiService.Base_URL + "mobile/user/lottery?mid=";
                    intent.putExtra("url", url + mid);
                    context.startActivity(intent);
                } else {
                    intent = new Intent(context, UserLoginNewActivity.class);
                    context.startActivity(intent);
                }
                break;
            case "112":
                intent = new Intent(context, QueryHistoryActivity.class);
                context.startActivity(intent);
                break;
            case "113":
                HomeActivity.initone();
                break;
            case "114":
                intent = new Intent(context, BrokerageActivity.class);
                context.startActivity(intent);
                break;
            case "115":
                intent = new Intent(context, FanLiOrderActivity.class);
                context.startActivity(intent);
                break;
            case "116":
                intent = new Intent(context, FensiActivity.class);
                intent.putExtra("isQiandao","1");
                context.startActivity(intent);
                break;
            case "117":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "t1");
                context.startActivity(intent);
                break;
            case "118":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "t3");
                context.startActivity(intent);
                break;
            case "119":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "j1");
                context.startActivity(intent);
                break;
            case "120":
                intent = new Intent(context, BrokerageDetailActivity.class);
                intent.putExtra("type", "j3");
                context.startActivity(intent);
                break;
            case "121":
                SharedPreferencesUtil.putSharedData(context, "homeactivty", "type", "2");
                intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                break;
            case "122":
                SharedPreferencesUtil.putSharedData(context, "Bidhomeactivty", "type", "3");
                intent = new Intent(context, BidHomeActivity.class);
                intent.putExtra("currentitem", "2");
                context.startActivity(intent);
                break;
            case "125":
                intent = new Intent(context, UserShenSuActivity.class);
                intent.putExtra("status", "1");
                context.startActivity(intent);
                break;
            default:
                break;
        }
        if (WelcomeActivity.instance != null) {
            WelcomeActivity.instance.finish();
        }
    }

    public static void EventIdIntentDianpu(Context context, String eventId, String dianpuid) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (eventId) {
            case "a1":
                intent = new Intent(context, NewDianpuActivity.class);
                if (dianpuid != null) {
                    intent.putExtra("dianpuid", dianpuid);
                }
                context.startActivity(intent);
                break;
            case "a3":
                intent = new Intent(context, ShopDetailActivty.class);
                if (dianpuid != null) {
                    intent.putExtra("id", dianpuid);
                }
                context.startActivity(intent);
                break;
        }
    }


    public static void showLoadingDialog(final Context context, String url) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.disanfang_dialog,
                    new int[]{R.id.ll_close});
            updataDialog.show();
            LinearLayout img_close = updataDialog.findViewById(R.id.ll_close);
            ImageView imgLoading = updataDialog.findViewById(R.id.img_loading);
            ImageView imageView = updataDialog.findViewById(R.id.img_app);
            AdaptionSizeTextView adaptionSizeTextViewQuan = updataDialog.findViewById(R.id.quan);
            AdaptionSizeTextView adaptionSizeTextViewQuan1 = updataDialog.findViewById(R.id.quan1);
            if (url.contains("jd")) {
                jumpdomain = "jumpjd";
            } else if (url.contains("tmall")) {
                jumpdomain = "jumptmall";
            } else if (url.contains("taobao")) {
                jumpdomain = "jumptaobao";
            }
            int drawS = context.getResources().getIdentifier(jumpdomain, "mipmap", context.getPackageName());
            imageView.setImageResource(drawS);
            Glide.with(context).load(R.drawable.tuiguang_d05).into(imgLoading);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    cancleJump = false;
                }
            });
        }
    }
}
