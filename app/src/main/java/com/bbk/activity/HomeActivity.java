package com.bbk.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.CustomFragmentPagerAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.entity.XGMessageEntity;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.fragment.FenXiangFragment;
import com.bbk.fragment.HomeMessageFragment;
import com.bbk.fragment.NewHomeFragment;
import com.bbk.fragment.NewRankFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.resource.Constants;
import com.bbk.update.UpdateVersionService;
import com.bbk.util.BaseTools;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CustomViewPager;
import com.bbk.view.NumImageView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends BaseFragmentActivity implements Response {

    private static final int TAB_SIZE = 5;
    private static CustomViewPager mViewPager;
    //    @BindView(R.id.home_image)
//    ImageView homeImage;
    @BindView(R.id.home_img_btn)
    ImageView homeImgBtn;
    @BindView(R.id.mtext)
    TextView mtext;
    @BindView(R.id.search_img_btn)
    ImageView searchImgBtn;
    @BindView(R.id.rank_img_btn)
    NumImageView rankImgBtn;
    @BindView(R.id.data_img_btn)
    ImageView dataImgBtn;
    @BindView(R.id.user_img_btn)
    ImageView userImgBtn;
    private CustomFragmentPagerAdapter mPagerAdapter;
    private ArrayList<BaseViewPagerFragment> fragments = new ArrayList<BaseViewPagerFragment>();
    private LinearLayout tabParentLayout;
    private int[] tabImgBlue = {R.mipmap.bottom_01, R.mipmap.bottom_02,
            R.mipmap.bottom_03, R.mipmap.bottom_04, R.mipmap.bottom_05};
    private int[] tabImgGray = {R.mipmap.bottom_11, R.mipmap.bottom_12,
            R.mipmap.bottom_13, R.mipmap.bottom_14, R.mipmap.bottom_15};
    private List<String> tabImgBlue2 = new ArrayList<>();
    private List<String> tabImgGray2 = new ArrayList<>();
    private boolean isshow = false;
    private int currentIndex = 0;
    private IWeiboShareAPI mWeiboShareAPI = null;
    public static ImageView mzhezhao;
    public static Activity instance = null;
    private String bcolor = "#ffffff";
    private String tcolor = "#444444";
    public static boolean isAlertUpdate = false;
    private String ctcolor = "#ff7d41";
    private UpdateVersionService updateVersionService;
    private final String mPageName = "HomeActivity";
    public static String Flag = "";
    public static NumImageView mNumImageView;
    public static ImageView mHomeGudieImage;//第一次安装首页新人引导


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        instance = this;
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        ViewGroup.LayoutParams params = homeImgBtn.getLayoutParams();
        params.height = StringUtil.dip2px(this, 42);
        params.width = StringUtil.dip2px(this, 42);
        homeImgBtn.setLayoutParams(params);
        initView();
        initData();
    }

    @Override
    protected void onInit() {
        // 来自信鸽消息判断
        XGMessageEntity xgMessage = null;
        String xgString = getIntent().getStringExtra("xgMessage");
        if (xgString != null) {
            xgMessage = new Gson().fromJson(xgString, XGMessageEntity.class);
            Intent intent = null;
            switch (xgMessage.getStartType()) {
                case "update":
//				UpdateChecker updateChecker = new UpdateChecker(this);
//				updateChecker.checkForUpdates();
                    isAlertUpdate = false;
                    new Thread(updateRun).start();
                    break;
                case "openWeb":
                    intent = new Intent(this, PushWebViewActivity.class);
                    intent.putExtra("url", xgMessage.getUrl());
                    startActivity(intent);
                    break;
                default:
                    Class<?> cls = xgMessage.getActivityClass();
                    if (cls != null) {
                        intent = new Intent(this, cls);
                        startActivity(intent);
                    }
                    break;
            }
        }
        if (xgMessage == null || !"update".equals(xgMessage.getStartType())) {
//			UpdateChecker updateChecker = new UpdateChecker(this);
//			updateChecker.checkBGForUpdates();
            isAlertUpdate = false;
            new Thread(updateRun).start();
        }
    }


    // 自动更新
    Runnable updateRun = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateVersionService = new UpdateVersionService(HomeActivity.this);// 创建更新业务对象
            updateVersionService.checkForUpdates();// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
            Looper.loop();
        }
    };

    public void initView() {
        mHomeGudieImage = findViewById(R.id.new_gudie_image_home);
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String token = TelephonyMgr.getDeviceId();
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
        mViewPager = $(R.id.main_layout);
        mViewPager.setScanScroll(false);
        tabParentLayout = $(R.id.tab_layout);
        mzhezhao = $(R.id.mzhezhao);
        mNumImageView = findViewById(R.id.rank_img_btn);
    }

    public void initData() {
        initViewPager();
        initViewPagerData();
        clickTab();
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
        Map<String, String> paramsMap = new HashMap<>();
        RetrofitClient.getInstance(this).createBaseApi().queryIndexMenu(
                paramsMap, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject object = new JSONObject(content);
                                if ("1".equals(object.optString("isshow"))) {
                                    isshow = true;
                                    JSONArray img = object.optJSONArray("img");
                                    JSONArray imgs = object.optJSONArray("imgs");
                                    for (int i = 0; i < 5; i++) {
                                        tabImgBlue2.add(imgs.optString(i));
                                        tabImgGray2.add(img.optString(i));
                                    }
                                    ctcolor = object.optString("ccolors");
                                    tcolor = object.optString("ccolor");
                                    bcolor = object.optString("bcolor");
                                    tabParentLayout.setBackgroundColor(Color.parseColor(bcolor));
                                    for (int i = 0; i < 5; i++) {
                                        LinearLayout nextLayout = ((LinearLayout) tabParentLayout.getChildAt(i));
                                        ImageView nextIV = (ImageView) nextLayout.getChildAt(0);
                                        if (isshow) {
                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(i)).placeholder(tabImgGray[i]).into(nextIV);
                                        } else {
                                            nextIV.setImageResource(tabImgGray[i]);
                                        }
                                        TextView nextTV = (TextView) nextLayout.getChildAt(1);
                                        nextTV.setTextColor(Color.parseColor(tcolor));
                                    }
                                    LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
                                    ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
                                    if (isshow) {
                                        Log.e("==================", "" + tabImgGray2.get(currentIndex));
                                        Glide.with(HomeActivity.this).
                                                load(tabImgBlue2.get(currentIndex))
                                                .placeholder(tabImgBlue[0]).
                                                into(currentIV);
                                    } else {
                                        currentIV.setImageResource(tabImgBlue[currentIndex]);
                                    }
                                    TextView currentTV = (TextView) currentLayout.getChildAt(1);
                                    currentTV.setTextColor(Color.parseColor(ctcolor));
                                } else {
                                    isshow = false;
                                    switchTab(currentIndex);
                                }
                            }
                        } catch (JSONException e) {
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
                        StringUtil.showToast(HomeActivity.this, e.message);
                    }
                });
    }

    public void initViewPager() {
        fragments.clear();
        mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void initViewPagerData() {
        NewHomeFragment homeFragment = new NewHomeFragment();
        NewRankFragment rankFragment = new NewRankFragment();
        HomeMessageFragment bidMessageFragment = new HomeMessageFragment();
//		GossipPiazzaFragment gossipPiazzaFragment = new GossipPiazzaFragment();
        FenXiangFragment fenXiangFragment = new FenXiangFragment();
        UserFragment userFragment = new UserFragment();
        fragments.add(homeFragment);//首页
//		fragments.add(gossipPiazzaFragment);//爆料
        fragments.add(fenXiangFragment);
        fragments.add(bidMessageFragment);//消息
        fragments.add(rankFragment);//发现
        fragments.add(userFragment);//我的
        mPagerAdapter.notifyDataSetChanged();

    }

    public void switchTab(int index) {
        if (index != currentIndex) {
            Resources resource = (Resources) getBaseContext().getResources();
            LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
            ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
            if (isshow) {
                Glide.with(this).
                        load(tabImgGray2.get(currentIndex)).
                        into(currentIV);
            } else {
                currentIV.setImageResource(tabImgGray[currentIndex]);
            }
            TextView currentTV = (TextView) currentLayout.getChildAt(1);
            currentTV.setTextColor(Color.parseColor(tcolor));

            LinearLayout nextLayout = ((LinearLayout) tabParentLayout.getChildAt(index));
            ImageView nextIV = (ImageView) nextLayout.getChildAt(0);
            int select = BaseTools.getPixelsFromDp(this, 35);
            int not = BaseTools.getPixelsFromDp(this, 20);
            // 如果不是在首页则显示图片加文字，在首页就只显示图片
            // if (index!=0) {
            // mtext.setVisibility(View.VISIBLE);
            // android.widget.LinearLayout.LayoutParams params =
            // (android.widget.LinearLayout.LayoutParams)
            // mimg.getLayoutParams();
            // params.height = not;
            // params.topMargin = BaseTools.getPixelsFromDp(this, 5);
            // mimg.setLayoutParams(params);

            // }else{
            //// mtext.setVisibility(View.GONE);
            // android.widget.LinearLayout.LayoutParams params =
            // (android.widget.LinearLayout.LayoutParams)
            // mimg.getLayoutParams();
            // params.height = select;
            // params.topMargin = BaseTools.getPixelsFromDp(this, 0);
            // mimg.setLayoutParams(params);
            // }
            if (isshow) {
                Glide.with(this).load(tabImgBlue2.get(index)).into(nextIV);
            } else {
                nextIV.setImageResource(tabImgBlue[index]);
            }
            TextView nextTV = (TextView) nextLayout.getChildAt(1);
            nextTV.setTextColor(Color.parseColor(ctcolor));

            currentIndex = index;
        }
    }

    public void clickTab() {
        for (int i = 0; i < TAB_SIZE; i++) {
            final int index = i;
            LinearLayout tabLayout = (LinearLayout) tabParentLayout.getChildAt(i);
            tabLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (index == 0) {
                        StringUtil.setScalse(homeImgBtn);//设置缩放动画
                        homeImgBtn.setVisibility(View.VISIBLE);
                        mtext.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = homeImgBtn.getLayoutParams();
                        params.height = StringUtil.dip2px(HomeActivity.this, 42);
                        params.width = StringUtil.dip2px(HomeActivity.this, 42);
                        homeImgBtn.setLayoutParams(params);
                    } else {
                        homeImgBtn.setVisibility(View.VISIBLE);
                        mtext.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams params = homeImgBtn.getLayoutParams();
                        params.height = StringUtil.dip2px(HomeActivity.this, 25);
                        params.width = StringUtil.dip2px(HomeActivity.this, 25);
                        homeImgBtn.setLayoutParams(params);
                        switch (index){
                            case 1:
                                StringUtil.setScalse(searchImgBtn);//设置缩放动画
                                break;
                            case 2:
                                StringUtil.setScalse(rankImgBtn);//设置缩放动画
                                break;
                            case 3:
                                StringUtil.setScalse(dataImgBtn);//设置缩放动画
                                break;
                            case 4:
                                StringUtil.setScalse(userImgBtn);//设置缩放动画
                                break;
                        }
                    }
                    if (index == 2) {
                        Flag = "home";
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        if (TextUtils.isEmpty(userID)) {
                            Intent intent4 = new Intent(getApplicationContext(), UserLoginNewActivity.class);
                            startActivityForResult(intent4, 1);
                        }
                    }
                    mViewPager.setCurrentItem(index);
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 1:
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mViewPager.setCurrentItem(0);
                break;
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);

    }

    @Override
    public void onResponse(BaseResponse arg0) {
        switch (arg0.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
//			loadData();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this, "分享失败", Toast.LENGTH_LONG).show();
                break;
        }

    }


    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            StringUtil.showToast(this, "再按一次返回键退出比比鲸");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        String type = SharedPreferencesUtil.getSharedData(getApplicationContext(), "homeactivty", "type");
        if (!TextUtils.isEmpty(type)) {
            SharedPreferencesUtil.cleanShareData(getApplicationContext(), "homeactivty");
            mViewPager.setCurrentItem(Integer.valueOf(type));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(mPageName);
    }

    public static void initone() {
        mViewPager.setCurrentItem(1);
    }

    public static void inittwo() {
        mViewPager.setCurrentItem(2);
    }

    public static void initThree() {
        mViewPager.setCurrentItem(3);
    }

    public static void initfour() {
        mViewPager.setCurrentItem(4);
    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    /*
    * 保存MyTouchListener接口的列表
    */
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}

