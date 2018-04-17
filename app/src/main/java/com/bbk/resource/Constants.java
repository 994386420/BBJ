package com.bbk.resource;


public interface Constants {
	
	/**主url*/
	//内网，陈远鹏 更换165
	public static final String MAIN_BASE_URL_MOBILE = "http://192.168.20.165/APIService/";
//	public static final String MAIN_BASE_URL_MOBILE = "http://125.64.92.222:7083/APIService/";
	//外网
//	public static final String MAIN_BASE_URL_MOBILE = "http://www.bibijing.com/";
	//新版首页超值购等分类模块
	public static final String GetQueryAppIndexByType = "newService/queryAppIndexByType";
	//更新APP
	String UPDATEVERSIONXMLPATH = MAIN_BASE_URL_MOBILE + "apiService/getAndroidUpdate";
	//获取看比价
	String  getBijiaArr = "newService/getBijiaArr";
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
