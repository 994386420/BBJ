package com.bbk.client;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface BaseApiService<T> {

//    public static final String Base_URL = "http://www.bibijing.com/";//正式接口
//    public static final String Base_URL = "http://125.64.92.222:8097/APIService/";//正式测试接口
    public static final String Base_URL = "http://192.168.20.165/APIService/";//内网测试接口
//    public static final String Base_URL = "http://192.168.20.129/APIService/";//内网测试接口
    //新版首页超值购等分类模块
    @FormUrlEncoded
    @POST("newService/queryAppIndexByType")
    Observable<String> queryAppIndexByType(@FieldMap Map<String, String> map);

    //签到
    @FormUrlEncoded
    @POST("newService/userSign")
    Observable<String> userSign(@FieldMap Map<String, String> map);

    //新版首页数据
    @POST("newService/queryAppIndexInfo")
    @FormUrlEncoded
    Observable<String> queryAppIndexInfo(@FieldMap Map<String, String> map);

    //商品比价（搜索）
    @POST("apiService/getPageList")
    @FormUrlEncoded
    Observable<String> getPageList(@FieldMap Map<String, String> map);
      abstract
    //获取爆料
    @POST("newService/queryBaoliaoMessage")
    @FormUrlEncoded
    Observable<String> queryBaoliaoMessage(@FieldMap Map<String, String> map);

    //获取发现
    @POST("newService/queryArticleByType")
    @FormUrlEncoded
    Observable<String> queryArticleByType(@FieldMap Map<String, String> map);

    //获取热门搜索词
    @POST("apiService/getSearchHotWord")
    @FormUrlEncoded
    Observable<String> getSearchHotWord(@FieldMap Map<String, String> map);

    //搜索超值购
    @POST("apiService/getPageListChaozhigou")
    @FormUrlEncoded
    Observable<String> getPageListChaozhigou(@FieldMap Map<String, String> map);

    @POST("searchAutoService/getAutoApp")
    @FormUrlEncoded
    Observable<String> getAutoApp(@FieldMap Map<String, String> map);

    //消息
    @POST("bid/querySysMessage")
    @FormUrlEncoded
    Observable<String> querySysMessage(@FieldMap Map<String, String> map);

    //读取消息
    @POST("bid/readSysmsg")
    @FormUrlEncoded
    Observable<String> readSysmsg(@FieldMap Map<String, String> map);

    //扑吧首页数据
    @POST("bid/queryIndex")
    @FormUrlEncoded
    Observable<String> queryIndex(@FieldMap Map<String, String> map);

    //扑倒数据
    @POST("bid/queryBidList")
    @FormUrlEncoded
    Observable<String> queryBidList(@FieldMap Map<String, String> map);

    //我要的list列表数据
    @POST("bid/queryBidByStatus")
    @FormUrlEncoded
    Observable<String> queryBidByStatus(@FieldMap Map<String, String> map);

    //扑倒的list列表数据
    @POST("bid/queryJBiaoMsgByStatus")
    @FormUrlEncoded
    Observable<String> queryJBiaoMsgByStatus(@FieldMap Map<String, String> map);

    /**
     * 消息中心接口
     */
    @POST("newService/querySysTMessage")
    @FormUrlEncoded
    Observable<String> querySysTMessage(@FieldMap Map<String, String> map);

    @POST("newService/queryPLMyRe")
    @FormUrlEncoded
    Observable<String> queryPLMyRe(@FieldMap Map<String, String> map);

    @POST("newService/queryPLOtherRe")
    @FormUrlEncoded
    Observable<String> queryPLOtherRe(@FieldMap Map<String, String> map);

    @POST("newService/insertMessageRead")
    @FormUrlEncoded
    Observable<String> insertMessageRead(@FieldMap Map<String, String> map);

    @POST("newService/insertWenzhangGuanzhu")
    @FormUrlEncoded
    Observable<String> insertWenzhangGuanzhu(@FieldMap Map<String, String> map);

    @POST("newService/insertPL")
    @FormUrlEncoded
    Observable<String> insertPL(@FieldMap Map<String, String> map);

    /**
     * 收藏接口
     */
    @POST("newService/queryArticlesFootAndCollect")
    @FormUrlEncoded
    Observable<String> queryArticlesFootAndCollect(@FieldMap Map<String, String> map);

    //去扑到详情
    @POST("bid/queryBidDetail")
    @FormUrlEncoded
    Observable<String> queryBidDetail(@FieldMap Map<String, String> map);

    //商品分类
    @POST("newApp/queryCatagTree")
    @FormUrlEncoded
    Observable<String> queryCatagTree(@FieldMap Map<String, String> map);

    //优惠券
    @POST("newService/queryYouhuilist")
    @FormUrlEncoded
    Observable<String> queryYouhuilist(@FieldMap Map<String, String> map);

    //获取广告图
    @POST("apiService/queryAppGuanggao")
    @FormUrlEncoded
    Observable<String> queryAppGuanggao(@FieldMap Map<String, String> map);

    //获取menu图
    @POST("newService/queryIndexMenu")
    @FormUrlEncoded
    Observable<String> queryIndexMenu(@FieldMap Map<String, String> map);

    //获取分类数据
    @POST("newService/queryIndexTuijianByToken")
    @FormUrlEncoded
    Observable<String> queryIndexTuijianByToken(@FieldMap Map<String, String> map);

    //    @POST("newService/queryIndexSeeByToken")
//    Observable<String> queryIndexSeeByToken(@FieldMap Map<String, String> map);
    @POST("newService/checkExsistCps")//checkExsistProduct 替换为 checkExsistCps
    @FormUrlEncoded
    Observable<String> checkExsistProduct(@FieldMap Map<String, String> map);

    //获取比价信息
    @POST("newService/getBijiaArr")
    @FormUrlEncoded
    Observable<String> getBijiaArr(@FieldMap Map<String, String> map);

    //成为合作伙伴 参数userid
    @POST("newService/updateCooperationByUserid")
    @FormUrlEncoded
    Observable<String> updateCooperationByUserid(@FieldMap Map<String, String> map);

    //字段hzinfo中取type（是否为合作伙伴：0为不是 1 为是）
    @POST("newService/queryUserInfoMain")
    @FormUrlEncoded
    Observable<String> queryUserInfoMain(@FieldMap Map<String, String> map);

    //查询用户收益页面
    @POST("newService/queryUserBrokerage")
    @FormUrlEncoded
    Observable<String> queryUserBrokerage(@FieldMap Map<String, String> map);

    //查询返利金币列表 （页面显示状态：0为未领1为已领）
    @POST("newService/querySignFanLi")
    @FormUrlEncoded
    Observable<String> querySignFanLi(@FieldMap Map<String, String> map);

    //领取返利金币
    @POST("newService/getMoneySignFanLi")
    @FormUrlEncoded
    Observable<String> getMoneySignFanLi(@FieldMap Map<String, String> map);

    //跳转页面
    @POST("newApp/getJumpUrl")
    @FormUrlEncoded
    Observable<String> getJumpUrl(@FieldMap Map<String, String> map);

    //收益报表详情newService/queryBrokerageDetail
    @POST("newService/queryBrokerageDetailNew")
    @FormUrlEncoded
    Observable<String> queryBrokerageDetail(@FieldMap Map<String, String> map);

    //跳转详情分享
    @POST("newService/shareCpsInfo")
    @FormUrlEncoded
    Observable<String> shareCpsInfo(@FieldMap Map<String, String> map);

    //看比价
    @POST("newService/queryCompareByUrl")
    @FormUrlEncoded
    Observable<String> queryCompareByUrl(@FieldMap Map<String, String> map);

    //跳转webview   ，返回url
    @POST("newService/parseCpsDomainMainUrl")
    @FormUrlEncoded
    Observable<String> parseCpsDomainMainUrl(@FieldMap Map<String, String> map);

    //查询分享圈列表
    @POST("newService/queryCpsShareList")
    @FormUrlEncoded
    Observable<String> queryCpsShareList(@FieldMap Map<String, String> map);

    //  分享一条分享圈的内容
    @POST("newService/shareCpsInfos")
    @FormUrlEncoded
    Observable<String> shareCpsInfos(@FieldMap Map<String, String> map);

    //获取返利订单列表
    @POST("newService/queryCpsOrderList")
    @FormUrlEncoded
    Observable<String> queryCpsOrderList(@FieldMap Map<String, String> map);

    // 查询cps详情
    @POST(" newService/queryCpsOrderDetail")
    @FormUrlEncoded
    Observable<String> queryCpsOrderDetail(@FieldMap Map<String, String> map);

    // 插入反馈
    @POST("newService/insertCpsOrderCheck")
    @FormUrlEncoded
    Observable<String> insertCpsOrderCheck(@FieldMap Map<String, String> map);

    // 查询佣金提现详情
    @POST("newService/queryYongjinListByUserid")
    @FormUrlEncoded
    Observable<String> queryYongjinListByUserid(@FieldMap Map<String, String> map);

    //分享海报
    @POST("newService/newInvitedFriend")
    @FormUrlEncoded
    Observable<String> newInvitedFriend(@FieldMap Map<String, String> map);

    //提醒签到
    @POST("newService/noticeInvitedUserSign")
    @FormUrlEncoded
    Observable<String> noticeInvitedUserSign(@FieldMap Map<String, String> map);

    //查询申诉列表
    @POST("newService/queryCpsOrderCheck")
    @FormUrlEncoded
    Observable<String> queryCpsOrderCheck(@FieldMap Map<String, String> map);

    /**
     * 购物商城接口
     */
    //购物车相关操作
    @POST("mallService/doShoppingCart")
    @FormUrlEncoded
    Observable<String> doShoppingCart(@FieldMap Map<String, String> map);

    //根据id查购物车内容
    @POST("mallService/queryShoppingCartByUserid")
    @FormUrlEncoded
    Observable<String> queryShoppingCartByUserid(@FieldMap Map<String, String> map);

    //搜索商品结果
    @POST("mallService/queryProductListByKeyword")
    @FormUrlEncoded
    Observable<String> queryProductListByKeyword(@FieldMap Map<String, String> map);

    //三级页面详情
    @POST("mallService/queryProductDetailById")
    @FormUrlEncoded
    Observable<String> queryProductDetailById(@FieldMap Map<String, String> map);

    /**
     * 商城订单
     */
    //查询我的订单
    @POST("mallService/queryMyOrder")
    @FormUrlEncoded
    Observable<String> queryMyOrder(@FieldMap Map<String, String> map);

    //删除或者取消订单
    @POST("mallService/deleteMyOrder")
    @FormUrlEncoded
    Observable<String> deleteMyOrder(@FieldMap Map<String, String> map);

    //查询我的订单详情
    @POST("mallService/queryMyOrderDetail")
    @FormUrlEncoded
    Observable<String> queryMyOrderDetail(@FieldMap Map<String, String> map);

    //查询物流
    @POST("mallService/queryMyLogistics")
    @FormUrlEncoded
    Observable<String> queryMyLogistics(@FieldMap Map<String, String> map);

    //店铺主页接口
    @POST("mallService/queryDianpuMainInfo")
    @FormUrlEncoded
    Observable<String> queryDianpuMainInfo(@FieldMap Map<String, String> map);

    //商城首页
    @POST("mallService/queryIndexMain1123")
    @FormUrlEncoded
    Observable<String> queryIndexMain(@FieldMap Map<String, String> map);

    //
    @POST("mallService/queryMyOrderToPay3")
    @FormUrlEncoded
    Observable<String> queryMyOrderToPay(@FieldMap Map<String, String> map);

    @POST("appPayService/getOrderInfoNew")
    @FormUrlEncoded
    Observable<String> getOrderInfo(@FieldMap Map<String, String> map);

    @POST("appPayService/getOrderInfo")
    @FormUrlEncoded
    Observable<String> getOrderInfoByJinbi(@FieldMap Map<String, String> map);


    //查询全部地址
    @POST("mallService/queryAddr")
    @FormUrlEncoded
    Observable<String> queryAddro(@FieldMap Map<String, String> map);

    //删除地址
    @POST("mallService/removeAddr")
    @FormUrlEncoded
    Observable<String> removeAddr(@FieldMap Map<String, String> map);

    //修改收货地址单个或者多个信息
    @POST("mallService/modifyAddr")
    @FormUrlEncoded
    Observable<String> modifyAddr(@FieldMap Map<String, String> map);

    //用户个人中心
    @POST("newService/queryUserCenter")
    @FormUrlEncoded
    Observable<String> queryUserCenter(@FieldMap Map<String, String> map);

    //退货
    @POST("mallService/refundGoods")
    @FormUrlEncoded
    Observable<String> refundGoods(@FieldMap Map<String, String> map);

    //评论
    @POST("mallService/queryPLByProductid")
    @FormUrlEncoded
    Observable<String> queryPLByProductid(@FieldMap Map<String, String> map);

    @POST("mallService/insertPinlun")
    @FormUrlEncoded
    Observable<String> insertPinlun(@FieldMap Map<String, String> map);

    //确认收货
    @POST("mallService/receiptGoods")
    @FormUrlEncoded
    Observable<String> receiptGoods(@FieldMap Map<String, String> map);

    //退款详情
    @POST("mallService/queryRefundProgress")
    @FormUrlEncoded
    Observable<String> queryRefundProgress(@FieldMap Map<String, String> map);

    //9.9，超高赚  筛选的分类
    @POST("apiService/getPageListChaozhigou99Types")
    @FormUrlEncoded
    Observable<String> getPageListChaozhigou99Types(@FieldMap Map<String, String> map);

    //9.9，超级返  数据接口
    @POST("apiService/getPageListChaozhigou99")
    @FormUrlEncoded
    Observable<String> getPageListChaozhigou99(@FieldMap Map<String, String> map);

    //
    @POST("newService/parseCpsDianpuMainUrl")
    @FormUrlEncoded
    Observable<String> parseCpsDianpuMainUrl(@FieldMap Map<String, String> map);

    //点击领取用户收益页面红包
    @POST("newService/getSurpriseGift")
    @FormUrlEncoded
    Observable<String> getSurpriseGift(@FieldMap Map<String, String> map);

    //一键提醒下单
    @FormUrlEncoded
    @POST("newService/remindFriendBuyGoods")
    Observable<String> remindFriendBuyGoods(@FieldMap Map<String, String> map);

    //获取淘宝购物车
    @FormUrlEncoded
    @POST("newService/synchroShoppingCartNew")
    Observable<String> synchroShoppingCart(@FieldMap Map<String, String> map);

    //获取电商网站的购物车h5页面url
    @POST("newService/getShoppingCartUrlByDomain")
    @FormUrlEncoded
    Observable<String> getShoppingCartUrlByDomain(@FieldMap Map<String, String> map);

    //查询0元购
    @POST("apiService/queryCpsZeroBuy")
    @FormUrlEncoded
    Observable<String> queryCpsZeroBuy(@FieldMap Map<String, String> map);

    //0元购 转换链接接口
    @POST("newService/getTaolijinUrl0Buy")
    @FormUrlEncoded
    Observable<String> getTaolijinUrl0Buy(@FieldMap Map<String, String> map);

    //普通三级页面链接转换接口
    @POST("newService/getTaolijinUrlNormal")
    @FormUrlEncoded
    Observable<String> getTaolijinUrlNormal(@FieldMap Map<String, String> map);

    //自营查询商品
    @POST("mallService/queryZiyingListByKeyword")
    @FormUrlEncoded
    Observable<String> queryZiyingListByKeyword(@FieldMap Map<String, String> map);

    //自营全部分类
    @POST("mallService/queryZiyingProducttype")
    @FormUrlEncoded
    Observable<String> queryZiyingProducttype(@FieldMap Map<String, String> map);

    // 0元购支付接口
    @POST("appPayService/getZeroBuyOrder")
    @FormUrlEncoded
    Observable<String> getZeroBuyOrder(@FieldMap Map<String, String> map);

    //新0元购接口
    @POST("apiService/queryCpsZeroBuyNew")
    @FormUrlEncoded
    Observable<String> queryCpsZeroBuyNew(@FieldMap Map<String, String> map);

    //老用户0元购apiService/queryZiyingZeroBuyForOld
    @POST("apiService/queryZiyingZeroBuyForOldJifen")
    @FormUrlEncoded
    Observable<String> queryZiyingZeroBuyForOld(@FieldMap Map<String, String> map);

    //老用户0元购支付
    @POST("appPayService/getZeroBuyOrderOldJifen")
    @FormUrlEncoded
    Observable<String> getZeroBuyOrderOld(@FieldMap Map<String, String> map);

    //查询三级页面领劵列表
    @POST("mallService/queryCouponListByGoodsId")
    @FormUrlEncoded
    Observable<String> queryCouponListByGoodsId(@FieldMap Map<String, String> map);

    //领取优惠券
    @POST("mallService/insertCouponsByUserid")
    @FormUrlEncoded
    Observable<String> insertCouponsByUserid(@FieldMap Map<String, String> map);

    //查询个人的优惠券情况
    @POST("mallService/queryCouponsListByUserid")
    @FormUrlEncoded
    Observable<String> queryCouponsListByUserid(@FieldMap Map<String, String> map);

    //查询领劵中心导航
    @POST("mallService/queryCouponsCenterMenu")
    @FormUrlEncoded
    Observable<String> queryCouponsCenterMenu(@FieldMap Map<String, String> map);

    //查询领劵中心列表
    @POST("mallService/queryCouponsCenterList")
    @FormUrlEncoded
    Observable<String> queryCouponsCenterList(@FieldMap Map<String, String> map);

    //一键读取消息
    @POST("newService/insertMessageReadOneKey")
    @FormUrlEncoded
    Observable<String> insertMessageReadOneKey(@FieldMap Map<String, String> map);

    //查询个人积分中心
    @POST("mallService/queryIntegralCenterByUserid")
    @FormUrlEncoded
    Observable<String> queryIntegralCenterByUserid(@FieldMap Map<String, String> map);

    //佣金明细页面主要信息
    @POST("newService/queryBrokerageDetailInfo")
    @FormUrlEncoded
    Observable<String> queryBrokerageDetailInfo(@FieldMap Map<String, String> map);

    // 查询个人的默认地址
    @POST("mallService/queryAddrSingle")
    @FormUrlEncoded
    Observable<String> queryAddrSingle(@FieldMap Map<String, String> map);

    //newService/yongjintixian
    @POST("newService/yongjintixian")
    @FormUrlEncoded
    Observable<String> yongjintixian (@FieldMap Map<String, String> map);

    //购物车显示方式
    @POST("newService/getShoppingCartShowList")
    @FormUrlEncoded
    Observable<String> getShoppingCartShowList (@FieldMap Map<String, String> map);

    //newService/getZuanAndQuanByRowkey
    @POST("newService/getZuanAndQuanByRowkey")
    @FormUrlEncoded
    Observable<String> getZuanAndQuanByRowkey (@FieldMap Map<String, String> map);

    // 查询提醒好友购买详情列表
    @POST("newService/queryMyFansToBuyState")
    @FormUrlEncoded
    Observable<String> queryMyFansToBuyState (@FieldMap Map<String, String> map);

    //提醒单个好友购买
    @POST("newService/noticeInvitedUserToBuy")
    @FormUrlEncoded
    Observable<String> noticeInvitedUserToBuy (@FieldMap Map<String, String> map);

    //一键提醒好友签到    参数userid
    @POST("newService/remindFriendSign")
    @FormUrlEncoded
    Observable<String> remindFriendSign (@FieldMap Map<String, String> map);
}
