package com.bbk.client;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface BaseApiService<T> {

    public static final String Base_URL = "http://www.bibijing.com/";
    //新版首页超值购等分类模块
    @POST("newService/queryAppIndexByType")
    Observable<String> queryAppIndexByType(@QueryMap Map<String, String> map);
    //新版首页数据
    @POST("newService/queryAppIndexInfo")
    Observable<String> queryAppIndexInfo(@QueryMap Map<String, String> map);
    //商品比价（搜索）
    @POST("apiService/getPageList")
    Observable<String> getPageList(@QueryMap Map<String, String> map);
    //获取爆料
    @POST("newService/queryBaoliaoMessage")
    Observable<String> queryBaoliaoMessage(@QueryMap Map<String, String> map);
    //获取发现
    @POST("newService/queryArticleByType")
    Observable<String> queryArticleByType(@QueryMap Map<String, String> map);
    //获取热门搜索词
    @POST("apiService/getSearchHotWord")
    Observable<String> getSearchHotWord(@QueryMap Map<String, String> map);
    //搜索超值购
    @POST("apiService/getPageListChaozhigou")
    Observable<String> getPageListChaozhigou(@QueryMap Map<String, String> map);
    @POST("searchAutoService/getAutoApp")
    Observable<String> getAutoApp(@QueryMap Map<String, String> map);
    //消息
    @POST("bid/querySysMessage")
    Observable<String> querySysMessage(@QueryMap Map<String, String> map);
    //读取消息
    @POST("bid/readSysmsg")
    Observable<String> readSysmsg(@QueryMap Map<String, String> map);
    //扑吧首页数据
    @POST("bid/queryIndex")
    Observable<String> queryIndex(@QueryMap Map<String, String> map);
    //扑倒数据
    @POST("bid/queryBidList")
    Observable<String> queryBidList(@QueryMap Map<String, String> map);
    //我要的list列表数据
    @POST("bid/queryBidByStatus")
    Observable<String> queryBidByStatus(@QueryMap Map<String, String> map);
    //扑倒的list列表数据
    @POST("bid/queryJBiaoMsgByStatus")
    Observable<String> queryJBiaoMsgByStatus(@QueryMap Map<String, String> map);
    //消息中心接口
    @POST("newService/querySysTMessage")
    Observable<String> querySysTMessage(@QueryMap Map<String, String> map);
    @POST("newService/queryPLMyRe")
    Observable<String> queryPLMyRe(@QueryMap Map<String, String> map);
    @POST("newService/queryPLOtherRe")
    Observable<String> queryPLOtherRe(@QueryMap Map<String, String> map);
}
