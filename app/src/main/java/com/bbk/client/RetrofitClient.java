package com.bbk.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.bbk.activity.MyApplication;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SystemUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 20;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = BaseApiService.Base_URL;
    private static Context mContext;
    private static RetrofitClient sNewInstance;
    private static ErrorTransformer transformer = new ErrorTransformer();

    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(baseUrl);
    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    private static Class<? extends Class> aClass;

    //默认baseUrl的单例
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                mContext);
    }
    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    //自己提供url
    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }

        return new RetrofitClient(context, url);
    }

    //不仅有url还有headers
    public static RetrofitClient getInstance(Context context, String url, Map<String, String> headers) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient(context, url, headers);
    }

    private RetrofitClient() {

    }

    private RetrofitClient(Context context) {

        this(context, baseUrl, null);
    }

    private RetrofitClient(Context context, String url) {

        this(context, url, null);
    }

    private RetrofitClient(Context context, String url, Map<String, String> headers) {
        //url为空，则默认使用baseUrl
        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        //缓存地址
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(mContext.getCacheDir(), "app_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }
        //okhttp创建
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new CaheInterceptor(context))
                .addNetworkInterceptor(new CaheInterceptor(context))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        //retrofit创建
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();

    }
    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 统一参数传递
     * @param parameters
     * @return
     */
    private Map<String, String> getMap(Map<String, String> parameters){
        String userId = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        String string =  SystemUtil.getDeviceBrand()+" "+SystemUtil.getSystemModel()+" android:"+SystemUtil.getSystemVersion();
        if (userId != null) {
        parameters.put("useridbbj", userId);
        }else {
        parameters.put("useridbbj", "");
        }
        parameters.put("areabbj","");
        parameters.put("clientbbj","android");
        parameters.put("phonesysbbj",string);
        if (isWifi(MyApplication.getContext())) {
            parameters.put("networkbbj", "wifi");
        }else {
            parameters.put("networkbbj", "流量");
        }
        return parameters;
    }

    public  void queryAppIndexByType(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryAppIndexByType(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void userSign(Map<String, String> parameters, Observer<?> observer) {
        apiService.userSign(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryAppIndexInfo(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryAppIndexInfo(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getPageList(Map<String, String> parameters, Observer<?> observer) {
        apiService.getPageList(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryBaoliaoMessage(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBaoliaoMessage(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryArticleByType(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryArticleByType(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getSearchHotWord(Map<String, String> parameters, Observer<?> observer) {
        apiService.getSearchHotWord(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getPageListChaozhigou(Map<String, String> parameters, Observer<?> observer) {
        apiService.getPageListChaozhigou(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getAutoApp(Map<String, String> parameters, Observer<?> observer) {
        apiService.getAutoApp(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void querySysMessage(Map<String, String> parameters, Observer<?> observer) {
        apiService.querySysMessage(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void readSysmsg(Map<String, String> parameters, Observer<?> observer) {
        apiService.readSysmsg(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryIndex(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryIndex(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryBidList(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBidList(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryBidByStatus(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBidByStatus(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryJBiaoMsgByStatuss(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryJBiaoMsgByStatus(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void querySysTMessage(Map<String, String> parameters, Observer<?> observer) {
        apiService.querySysTMessage(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryPLMyRe(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryPLMyRe(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryPLOtherRe(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryPLOtherRe(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void insertMessageRead(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertMessageRead(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void insertWenzhangGuanzhu(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertWenzhangGuanzhu(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void insertPL(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertPL(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryArticlesFootAndCollect(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryArticlesFootAndCollect(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryBidDetail(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBidDetail(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryCatagTree(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCatagTree(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryYouhuilist(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryYouhuilist(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryAppGuanggao(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryAppGuanggao(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryIndexMenu(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryIndexMenu(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
//    public  void queryIndexSeeByToken(Map<String, String> parameters, Observer<?> observer) {
//        apiService.queryIndexSeeByToken(getMap(parameters))
//                .compose(schedulersTransformer)
////                .compose(transformer)
//                .subscribe(observer);
//    }
    public  void queryIndexTuijianByToken(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryIndexTuijianByToken(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void checkExsistProduct(Map<String, String> parameters, Observer<?> observer) {
        apiService.checkExsistProduct(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getBijiaArr(Map<String, String> parameters, Observer<?> observer) {
        apiService.getBijiaArr(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void updateCooperationByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.updateCooperationByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryUserInfoMain(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryUserInfoMain(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryUserBrokerage(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryUserBrokerage(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void querySignFanLi(Map<String, String> parameters, Observer<?> observer) {
        apiService.querySignFanLi(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getMoneySignFanLi(Map<String, String> parameters, Observer<?> observer) {
        apiService.getMoneySignFanLi(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getJumpUrl(Map<String, String> parameters, Observer<?> observer) {
        apiService.getJumpUrl(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryBrokerageDetail(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBrokerageDetail(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void shareCpsInfo(Map<String, String> parameters, Observer<?> observer) {
        apiService.shareCpsInfo(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryCompareByUrl(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCompareByUrl(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void parseCpsDomainMainUrl(Map<String, String> parameters, Observer<?> observer) {
        apiService.parseCpsDomainMainUrl(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryCpsShareList(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsShareList(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void shareCpsInfos(Map<String, String> parameters, Observer<?> observer) {
        apiService.shareCpsInfos(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryCpsOrderList(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsOrderList(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryCpsOrderDetail(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsOrderDetail(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void insertCpsOrderCheck(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertCpsOrderCheck(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryYongjinListByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryYongjinListByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void newInvitedFriend(Map<String, String> parameters, Observer<?> observer) {
        apiService.newInvitedFriend(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void noticeInvitedUserSign(Map<String, String> parameters, Observer<?> observer) {
        apiService.noticeInvitedUserSign(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCpsOrderCheck(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsOrderCheck(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void doShoppingCart(Map<String, String> parameters, Observer<?> observer) {
        apiService.doShoppingCart(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryProductDetailById(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryProductDetailById(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryShoppingCartByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryShoppingCartByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryProductListByKeyword(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryProductListByKeyword(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryMyOrder(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryMyOrder(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void deleteMyOrder(Map<String, String> parameters, Observer<?> observer) {
        apiService.deleteMyOrder(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryMyOrderDetail(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryMyOrderDetail(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryMyLogistics(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryMyLogistics(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryDianpuMainInfo(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryDianpuMainInfo(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryIndexMain(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryIndexMain(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryMyOrderToPay(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryMyOrderToPay(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getOrderInfo(Map<String, String> parameters, Observer<?> observer) {
        apiService.getOrderInfo(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getOrderInfoByJinbi(Map<String, String> parameters, Observer<?> observer) {
        apiService.getOrderInfoByJinbi(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryAddro(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryAddro(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void removeAddr(Map<String, String> parameters, Observer<?> observer) {
        apiService.removeAddr(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void modifyAddr(Map<String, String> parameters, Observer<?> observer) {
        apiService.modifyAddr(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryUserCenter(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryUserCenter(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void refundGoods(Map<String, String> parameters, Observer<?> observer) {
        apiService.refundGoods(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryPLByProductid(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryPLByProductid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void insertPinlun(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertPinlun(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void receiptGoods(Map<String, String> parameters, Observer<?> observer) {
        apiService.receiptGoods(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void queryRefundProgress(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryRefundProgress(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void getPageListChaozhigou99Types(Map<String, String> parameters, Observer<?> observer) {
        apiService.getPageListChaozhigou99Types(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void getPageListChaozhigou99(Map<String, String> parameters, Observer<?> observer) {
        apiService.getPageListChaozhigou99(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void parseCpsDianpuMainUrl(Map<String, String> parameters, Observer<?> observer) {
        apiService.parseCpsDianpuMainUrl(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void getSurpriseGift(Map<String, String> parameters, Observer<?> observer) {
        apiService.getSurpriseGift(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void remindFriendBuyGoods(Map<String, String> parameters, Observer<?> observer) {
        apiService.remindFriendBuyGoods(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void synchroShoppingCart(Map<String, String> parameters, Observer<?> observer) {
        apiService.synchroShoppingCart(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getShoppingCartUrlByDomain(Map<String, String> parameters, Observer<?> observer) {
        apiService.getShoppingCartUrlByDomain(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCpsZeroBuy(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsZeroBuy(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getTaolijinUrl0Buy(Map<String, String> parameters, Observer<?> observer) {
        apiService.getTaolijinUrl0Buy(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void getTaolijinUrlNormal(Map<String, String> parameters, Observer<?> observer) {
        apiService.getTaolijinUrlNormal(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryZiyingListByKeyword(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryZiyingListByKeyword(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryZiyingProducttype(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryZiyingProducttype(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void getZeroBuyOrder(Map<String, String> parameters, Observer<?> observer) {
        apiService.getZeroBuyOrder(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCpsZeroBuyNew(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCpsZeroBuyNew(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryZiyingZeroBuyForOld(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryZiyingZeroBuyForOld(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    public  void getZeroBuyOrderOld(Map<String, String> parameters, Observer<?> observer) {
        apiService.getZeroBuyOrderOld(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCouponListByGoodsId(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCouponListByGoodsId(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void insertCouponsByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertCouponsByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCouponsListByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCouponsListByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCouponsCenterMenu(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCouponsCenterMenu(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryCouponsCenterList(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryCouponsCenterList(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void insertMessageReadOneKey(Map<String, String> parameters, Observer<?> observer) {
        apiService.insertMessageReadOneKey(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryIntegralCenterByUserid(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryIntegralCenterByUserid(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryBrokerageDetailInfo(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryBrokerageDetailInfo(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }

    public  void queryAddrSingle(Map<String, String> parameters, Observer<?> observer) {
        apiService.queryAddrSingle(getMap(parameters))
                .compose(schedulersTransformer)
//                .compose(transformer)
                .subscribe(observer);
    }
    //处理线程调度的变换
    ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return ((Observable) upstream).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };
    //处理错误的变换
    private static class ErrorTransformer<T> implements ObservableTransformer{

        @Override
        public ObservableSource apply(Observable upstream) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Observable<T>) upstream.map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
        }
    }


    public static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }

    public static class HandleFuc<T> implements Function<BaseResponse<T>, T> {
        @Override
        public T apply(BaseResponse<T> response) throws Exception {
            //response中code码不会0 出现错误
            if (!response.isOk())
                throw new RuntimeException(response.getStatus() + "" + response.getErrmsg() != null ? response.getErrmsg() : "");
            return response.getContent();
        }
    }
    /**
     * 创建默认url的api类
     * @return ApiManager
     */
    public RetrofitClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }
}
