package com.bbk.resource;


import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

public interface Constants {
	
	/**主url*/
	//内网，陈远鹏 更换165
//	public static final String MAIN_BASE_URL_MOBILE = "http://192.168.20.188/APIService/";
//	public static final String MAIN_BASE_URL_MOBILE = "http://125.64.92.222:7083/APIService/";
	//外网
	public static final String MAIN_BASE_URL_MOBILE = "http://www.bibijing.com/";
	//新版首页超值购等分类模块
	public static final String GetQueryAppIndexByType = "newService/queryAppIndexByType";
	//更新APP
	String UPDATEVERSIONXMLPATH = MAIN_BASE_URL_MOBILE + "apiService/getAndroidUpdate";
	//appid
	String APP_ID = "wx897849778777b911";
	// AppSecret
	String AppSecret = "f03aec0d445f3e0acbd6c2eb4369a847";
	//获取看比价
	String  getBijiaArr = "newService/getBijiaArr";
	//获取二级页面去发镖信息
	String  getFabiaoMsgByRowkey = "bid/queryFabiaoMsgByRowkey";
	//搜索超值购
	String  getPageListChaozhigou = "apiService/getPageListChaozhigou";//参数sortWay   0默认，1价格升序，2价格降序，3折扣率，4淘宝关注度排序
	//通过手机号注册
	String registUserByPhoneNumber = "apiService/registUserByPhoneNumber";
	//找回密码
	String findPwdByPhone = "apiService/findPwdByPhone";
	/**
	 *增加地址
	 * userid（用户id）
	 * name（收货人名称）
	 * phone（手机号码）
	 * area（所在地区）
	 * street（具体所在的街道）
	 * tag（地址标签）
	 * original（是否为默认收货地址：1为默认，不为默认0或者传空）
	 */
	String addAddress = "mallService/addAddr";
	/**
	 *查询地址
	 * 参数userid（用户id）
	 * addrid（收货地址id存在查单个，不存在查询全部）
	 *
	 */
	String queryAddr = "auction/queryAddr";
	/**
	 *	修改收货地址单个或者多个信息
	 * addrid（地址信息id）
	 * name（收货人名字）
	 * phone（手机号码）
	 * area（所在地区）
	 * street（具体所在的街道）
	 * tag（地址标签）
	 * original（是否为默认收货地址：1为默认，不为默认0或者传空）
	 */
	String modifyAddr = "mallService/modifyAddr";
	/**
	 * 删除收货地址
	 * addrid（收货地址id存在查单个，不存在查询全部）
	 */
	String removeAddr = "auction/removeAddr";

	/**上传图片*/
	public static final String USER_IMG_URL_HEADER = "http://www.bibkan.com/upload/Image/";

	/**微博登录常量*/
	public static final String WEIBO_APP_KEY = "3785658514"; // 应用的APP_KEY
	public static final String REDIRECT_URL = "http://www.bibijing.com"; // 应用的回调页
	public static final String SCOPE = // 应用申请的高级权限
			"email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
	
	/**QQ登录常量*/
	public static final String QQ_APP_ID = "1104896963";
	
	/**微信常量*/
	public static final String WX_APP_ID = "wx897849778777b911";
	public static final String WX_APP_SECRET = "f03aec0d445f3e0acbd6c2eb4369a847";
	// 获取access_token
	public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//获取用户个人信息
	public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
}
