package com.bbk.client;
import java.util.Map;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface BaseApiService<T> {

//    public static final String Base_URL = "http://www.bibijing.com/";//正式接口
//    public static final String Base_URL = "http://125.64.92.222:8097/APIService/";//正式测试接口
//    public static final String Base_URL = "http://192.168.20.165/APIService/";//内网测试接口
    public static final String Base_URL = "http://192.168.20.129/APIService/";//内网测试接口
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
//    @POST("newService/queryIndexSeeByToken")
//    Observable<String> queryIndexSeeByToken(@QueryMap Map<String, String> map);
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

    // 查询佣金提现详情
    @POST("newService/queryYongjinListByUserid")
    Observable<String> queryYongjinListByUserid(@QueryMap Map<String, String> map);
    //分享海报
    @POST("newService/newInvitedFriend")
    Observable<String> newInvitedFriend(@QueryMap Map<String, String> map);
    //提醒签到
    @POST("newService/noticeInvitedUserSign")
    Observable<String> noticeInvitedUserSign(@QueryMap Map<String, String> map);
    //查询申诉列表
    @POST("newService/queryCpsOrderCheck")
    Observable<String>queryCpsOrderCheck(@QueryMap Map<String, String> map);

    /**
     * 购物商城接口
     */
    //购物车相关操作
    @POST("mallService/doShoppingCart")
    Observable<String> doShoppingCart(@QueryMap Map<String, String> map);
    //根据id查购物车内容
    @POST("mallService/queryShoppingCartByUserid")
    Observable<String> queryShoppingCartByUserid(@QueryMap Map<String, String> map);
    //搜索商品结果
    @POST("mallService/queryProductListByKeyword")
    Observable<String> queryProductListByKeyword(@QueryMap Map<String, String> map);
    //三级页面详情
    @POST("mallService/queryProductDetailById")
    Observable<String> queryProductDetailById(@QueryMap Map<String, String> map);
    /**
     * 商城订单
     */
    //查询我的订单
    @POST("mallService/queryMyOrder")
    Observable<String> queryMyOrder(@QueryMap Map<String, String> map);
    //删除或者取消订单
    @POST("mallService/deleteMyOrder")
    Observable<String> deleteMyOrder(@QueryMap Map<String, String> map);
    //查询我的订单详情
    @POST("mallService/queryMyOrderDetail")
    Observable<String> queryMyOrderDetail(@QueryMap Map<String, String> map);
    //查询物流
    @POST("mallService/queryMyLogistics")
    Observable<String> queryMyLogistics(@QueryMap Map<String, String> map);
    //店铺主页接口
    @POST("mallService/queryDianpuMainInfo")
    Observable<String> queryDianpuMainInfo(@QueryMap Map<String, String> map);
    //商城首页
    @POST("mallService/queryIndexMain1123")
    Observable<String> queryIndexMain(@QueryMap Map<String, String> map);
    //
    @POST("mallService/queryMyOrderToPay3")
    Observable<String> queryMyOrderToPay(@QueryMap Map<String, String> map);
    @POST("appPayService/getOrderInfoNew")
    Observable<String> getOrderInfo(@QueryMap Map<String, String> map);
    @POST("appPayService/getOrderInfo")
    Observable<String> getOrderInfoByJinbi(@QueryMap Map<String, String> map);


    //查询全部地址
    @POST("mallService/queryAddr")
    Observable<String> queryAddro(@QueryMap Map<String, String> map);
    //删除地址
    @POST("mallService/removeAddr")
    Observable<String> removeAddr(@QueryMap Map<String, String> map);
    //修改收货地址单个或者多个信息
    @POST("mallService/modifyAddr")
    Observable<String> modifyAddr(@QueryMap Map<String, String> map);
    //用户个人中心
    @POST("newService/queryUserCenter")
    Observable<String> queryUserCenter(@QueryMap Map<String, String> map);
    //退货
    @POST("mallService/refundGoods")
    Observable<String> refundGoods(@QueryMap Map<String, String> map);
    //评论
    @POST("mallService/queryPLByProductid")
    Observable<String> queryPLByProductid(@QueryMap Map<String, String> map);
    @POST("mallService/insertPinlun")
    Observable<String> insertPinlun(@QueryMap Map<String, String> map);

    //确认收货
    @POST("mallService/receiptGoods")
    Observable<String> receiptGoods(@QueryMap Map<String, String> map);
    //退款详情
    @POST("mallService/queryRefundProgress")
    Observable<String> queryRefundProgress(@QueryMap Map<String, String> map);

    //9.9，超高赚  筛选的分类
    @POST("apiService/getPageListChaozhigou99Types")
    Observable<String> getPageListChaozhigou99Types(@QueryMap Map<String, String> map);
    //9.9，超级返  数据接口
    @POST("apiService/getPageListChaozhigou99")
    Observable<String> getPageListChaozhigou99(@QueryMap Map<String, String> map);

    //
    @POST("newService/parseCpsDianpuMainUrl")
    Observable<String> parseCpsDianpuMainUrl(@QueryMap Map<String, String> map);

    //点击领取用户收益页面红包
    @POST("newService/getSurpriseGift")
    Observable<String> getSurpriseGift(@QueryMap Map<String, String> map);
    //一键提醒下单
    @POST("newService/remindFriendBuyGoods")
    Observable<String> remindFriendBuyGoods(@QueryMap Map<String, String> map);
    //获取淘宝购物车
    @POST("newService/synchroShoppingCart")
    Observable<String> synchroShoppingCart(@QueryMap Map<String, String> map);

    //获取电商网站的购物车h5页面url
    @POST("newService/getShoppingCartUrlByDomain")
    Observable<String> getShoppingCartUrlByDomain(@QueryMap Map<String, String> map);
    //查询0元购
    @POST("apiService/queryCpsZeroBuy")
    Observable<String> queryCpsZeroBuy(@QueryMap Map<String, String> map);
    //0元购 转换链接接口
    @POST("newService/getTaolijinUrl0Buy")
    Observable<String> getTaolijinUrl0Buy(@QueryMap Map<String, String> map);
    //普通三级页面链接转换接口
    @POST("newService/getTaolijinUrlNormal")
    Observable<String> getTaolijinUrlNormal(@QueryMap Map<String, String> map);
    //自营查询商品
    @POST("mallService/queryZiyingListByKeyword")
    Observable<String> queryZiyingListByKeyword(@QueryMap Map<String, String> map);
    //自营全部分类
    @POST("mallService/queryZiyingProducttype")
    Observable<String> queryZiyingProducttype(@QueryMap Map<String, String> map);
    // 0元购支付接口
    @POST("appPayService/getZeroBuyOrder")
    Observable<String> getZeroBuyOrder(@QueryMap Map<String, String> map);
    //新0元购接口
    @POST("apiService/queryCpsZeroBuyNew")
    Observable<String> queryCpsZeroBuyNew(@QueryMap Map<String, String> map);
}
