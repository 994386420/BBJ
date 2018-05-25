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
}
