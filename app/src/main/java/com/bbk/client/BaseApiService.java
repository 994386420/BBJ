package com.bbk.client;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface BaseApiService<T> {

//    public static final String Base_URL = "http://www.bibijing.com/";//正式接口
//    public static final String Base_URL = "http://125.64.92.222:8097/APIService/";//正式测试接口
    public static final String Base_URL = "http://192.168.20.188/APIService/";//内网测试接口
    //新版首页超值购等分类模块
    @POST("newService/queryAppIndexByType")
    Observable<String> queryAppIndexByType(@QueryMap Map<String, String> map);
    //签到
    @POST("newService/userSign")
    Observable<String> userSign(@QueryMap Map<String, String> map);
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
    /**
     *  消息中心接口
     */
    @POST("newService/querySysTMessage")
    Observable<String> querySysTMessage(@QueryMap Map<String, String> map);
    @POST("newService/queryPLMyRe")
    Observable<String> queryPLMyRe(@QueryMap Map<String, String> map);
    @POST("newService/queryPLOtherRe")
    Observable<String> queryPLOtherRe(@QueryMap Map<String, String> map);
    @POST("newService/insertMessageRead")
    Observable<String> insertMessageRead(@QueryMap Map<String, String> map);
    @POST("newService/insertWenzhangGuanzhu")
    Observable<String> insertWenzhangGuanzhu(@QueryMap Map<String, String> map);
    @POST("newService/insertPL")
    Observable<String> insertPL(@QueryMap Map<String, String> map);
    /**
     * 收藏接口
     */
    @POST("newService/queryArticlesFootAndCollect")
    Observable<String> queryArticlesFootAndCollect(@QueryMap Map<String, String> map);
    //去扑到详情
    @POST("bid/queryBidDetail")
    Observable<String> queryBidDetail(@QueryMap Map<String, String> map);
    //商品分类
    @POST("newApp/queryCatagTree")
    Observable<String> queryCatagTree(@QueryMap Map<String, String> map);
    //优惠券
    @POST("newService/queryYouhuilist")
    Observable<String> queryYouhuilist(@QueryMap Map<String, String> map);
    //获取广告图
    @POST("apiService/queryAppGuanggao")
    Observable<String> queryAppGuanggao(@QueryMap Map<String, String> map);
    //获取menu图
    @POST("newService/queryIndexMenu")
    Observable<String> queryIndexMenu(@QueryMap Map<String, String> map);
    //获取分类数据
    @POST("newService/queryIndexTuijianByToken")
    Observable<String> queryIndexTuijianByToken(@QueryMap Map<String, String> map);
    @POST("newService/queryIndexSeeByToken")
    Observable<String> queryIndexSeeByToken(@QueryMap Map<String, String> map);
    @POST("newService/checkExsistCps")//checkExsistProduct 替换为 checkExsistCps
    Observable<String> checkExsistProduct(@QueryMap Map<String, String> map);
    //获取比价信息
    @POST("newService/getBijiaArr")
    Observable<String> getBijiaArr(@QueryMap Map<String, String> map);
    //成为合作伙伴 参数userid
    @POST("newService/updateCooperationByUserid")
    Observable<String> updateCooperationByUserid(@QueryMap Map<String, String> map);
    //字段hzinfo中取type（是否为合作伙伴：0为不是 1 为是）
    @POST("newService/queryUserInfoMain")
    Observable<String> queryUserInfoMain(@QueryMap Map<String, String> map);
    //查询用户收益页面
    @POST("newService/queryUserBrokerage")
    Observable<String> queryUserBrokerage(@QueryMap Map<String, String> map);
    //查询返利金币列表 （页面显示状态：0为未领1为已领）
    @POST("newService/querySignFanLi")
    Observable<String> querySignFanLi(@QueryMap Map<String, String> map);
    //领取返利金币
    @POST("newService/getMoneySignFanLi")
    Observable<String> getMoneySignFanLi(@QueryMap Map<String, String> map);
    //跳转页面
    @POST("newApp/getJumpUrl")
    Observable<String> getJumpUrl(@QueryMap Map<String, String> map);
    //收益报表详情
    @POST("newService/queryBrokerageDetail")
    Observable<String> queryBrokerageDetail(@QueryMap Map<String, String> map);
    //跳转详情分享
    @POST("newService/shareCpsInfo")
    Observable<String> shareCpsInfo(@QueryMap Map<String, String> map);

    //看比价
    @POST("newService/queryCompareByUrl")
    Observable<String> queryCompareByUrl(@QueryMap Map<String, String> map);

    //跳转webview   ，返回url
    @POST("newService/parseCpsDomainMainUrl")
    Observable<String> parseCpsDomainMainUrl(@QueryMap Map<String, String> map);

    //查询分享圈列表
    @POST("newService/queryCpsShareList")
    Observable<String> queryCpsShareList(@QueryMap Map<String, String> map);
    //  分享一条分享圈的内容
    @POST("newService/shareCpsInfos")
    Observable<String> shareCpsInfos(@QueryMap Map<String, String> map);
    //获取返利订单列表
    @POST("newService/queryCpsOrderList")
    Observable<String> queryCpsOrderList(@QueryMap Map<String, String> map);
   // 查询cps详情
    @POST(" newService/queryCpsOrderDetail")
    Observable<String> queryCpsOrderDetail(@QueryMap Map<String, String> map);
    // 插入反馈
    @POST("newService/insertCpsOrderCheck")
    Observable<String> insertCpsOrderCheck(@QueryMap Map<String, String> map);

}
