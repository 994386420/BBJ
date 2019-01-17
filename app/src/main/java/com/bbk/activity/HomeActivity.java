package com.bbk.activity;


import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.CustomFragmentPagerAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.entity.XGMessageEntity;
import com.bbk.fragment.BaseViewPagerFragment;
import com.bbk.fragment.CarFrament;
import com.bbk.fragment.MesageCenteFragment;
import com.bbk.fragment.SortFragment;
import com.bbk.fragment.UserFragment;
import com.bbk.model.MainActivity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.update.UpdateVersionService;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogHomeUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CustomViewPager;
import com.bbk.view.DraggableFlagView;
import com.bbk.view.NumImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.logg.Logg;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

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
import butterknife.OnClick;

import static com.ali.auth.third.core.context.KernelContext.context;


public class HomeActivity extends BaseFragmentActivity implements Response {

    private static final int TAB_SIZE = 5;
    private static CustomViewPager mViewPager;
    @BindView(R.id.rl_home)
    LinearLayout rlHome;
    @BindView(R.id.rl_sort)
    LinearLayout rlSort;
    @BindView(R.id.rl_message)
    LinearLayout rlMessage;
    @BindView(R.id.rl_car)
    LinearLayout rlCar;
    @BindView(R.id.rl_my)
    LinearLayout rlMy;
    @BindView(R.id.tab_layout)
    LinearLayout tabLayout;
    @BindView(R.id.img_home_btn)
    ImageView imgHomeBtn;
    @BindView(R.id.img_sort_btn)
    ImageView imgSortBtn;
    @BindView(R.id.img_message_btn)
    NumImageView imgMessageBtn;
    @BindView(R.id.img_car_btn)
    ImageView imgCarBtn;
    @BindView(R.id.img_user_btn)
    ImageView imgUserBtn;
    @BindView(R.id.mtext)
    TextView mtext;
    @BindView(R.id.tv_sort)
    TextView tvSort;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_car)
    TextView tvCar;
    @BindView(R.id.tv_my)
    TextView tvMy;
    public static DraggableFlagView draggableflagview;
    @BindView(R.id.activity_home_layout)
    RelativeLayout activityHomeLayout;
    private CustomFragmentPagerAdapter mPagerAdapter;
    private ArrayList<BaseViewPagerFragment> fragments = new ArrayList<BaseViewPagerFragment>();
    private LinearLayout tabParentLayout;
    private int[] tabImgBlue = {R.mipmap.bottom_01, R.mipmap.bottom_07,
            R.mipmap.bottom_03, R.mipmap.bottom_08, R.mipmap.bottom_05};
    private int[] tabImgGray = {R.mipmap.bottom_11, R.mipmap.bottom_17,
            R.mipmap.bottom_13, R.mipmap.bottom_18, R.mipmap.bottom_15};
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
    public static ImageView mHomeGudieImage;//第一次安装首页新人引导
    public static int position = 5;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        instance = this;
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.WEIBO_APP_KEY);
        mWeiboShareAPI.registerApp();
        draggableflagview = findViewById(R.id.draggableflagview);
        /**
         * 拖拽监听
         */
        draggableflagview.setOnDraggableFlagViewListener(new DraggableFlagView.OnDraggableFlagViewListener() {
            @Override
            public void onFlagDismiss(DraggableFlagView view, float x, float y) {
                playImageDismissAnim(x, y);
                insertMessageReadOneKey();
            }
        });
        initView();
//        initData();
        queryAppIndexInfo();
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
        activityHomeLayout = (RelativeLayout) findViewById(R.id.activity_home_layout);
        mHomeGudieImage = findViewById(R.id.new_gudie_image_home);
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String token = TelephonyMgr.getDeviceId();
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
        mViewPager = $(R.id.main_layout);
        mViewPager.setScanScroll(false);
        tabParentLayout = $(R.id.tab_layout);
        mzhezhao = $(R.id.mzhezhao);
    }


    /**
     * 在Activity中定义一个方法用来设置Handler对象
     */
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 一键读取消息
     */
    private void insertMessageReadOneKey(){
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().insertMessageReadOneKey(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Logg.json(jsonObject);
                            if (jsonObject.optString("status").equals("1")) {
                                /**
                                 * Activity中发送消息给Fragment中的Hanlder进行交互。
                                 */
                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(HomeActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(HomeActivity.this, e.message);
                    }
                });
    }




    /**
     * 首页数据请求
     */
    private void queryAppIndexInfo() {
        initViewPager();
        initViewPagerData();
        clickTab();
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0);
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        maps.put("userid", userID);
        maps.put("versioncode", versionCode + "");
        RetrofitClient.getInstance(this).createBaseApi().queryAppIndexInfo(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.optJSONObject("content");
                                if (jsonObject1.has("zhuanti")) {
                                    JSONObject object = new JSONObject(jsonObject1.optString("zhuanti"));
                                    Logg.json(object);
                                    isshow = true;
                                    //底部按钮图片选中
                                    if (object.has("button1s")) {
                                        tabImgBlue2.add(object.optString("button1s"));
                                    }
                                    if (object.has("button2s")) {
                                        tabImgBlue2.add(object.optString("button2s"));
                                    }
                                    if (object.has("button3s")) {
                                        tabImgBlue2.add(object.optString("button3s"));
                                    }
                                    if (object.has("button4s")) {
                                        tabImgBlue2.add(object.optString("button4s"));
                                    }
                                    if (object.has("button5s")) {
                                        tabImgBlue2.add(object.optString("button5s"));
                                    }
                                    //底部按钮图片
                                    if (object.has("button1")) {
                                        tabImgGray2.add(object.optString("button1"));
                                    }
                                    if (object.has("button2")) {
                                        tabImgGray2.add(object.optString("button2"));
                                    }
                                    if (object.has("button3")) {
                                        tabImgGray2.add(object.optString("button3"));
                                    }
                                    if (object.has("button4")) {
                                        tabImgGray2.add(object.optString("button4"));
                                    }
                                    if (object.has("button5")) {
                                        tabImgGray2.add(object.optString("button5"));
                                    }
                                    if (object.has("colord2")) {
                                        ctcolor = object.optString("colord2");//底部字的颜色（选中）
                                    }
                                    if (object.has("colord1")) {
                                        tcolor = object.optString("colord1");//底部字的颜色
                                    }
                                    if (object.has("colordb")) {
                                        bcolor = object.optString("colordb");//底部背景
                                    }
                                    tabParentLayout.setBackgroundColor(Color.parseColor(bcolor));
                                    //加载底部图片
                                    for (int i = 0; i < 5; i++) {
                                        if (isshow) {
                                            if (tabImgGray2.size() > 0) {
                                                Glide.with(HomeActivity.this)
                                                        .load(tabImgGray2.get(0))
                                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                        .placeholder(tabImgGray[0])
                                                        .into(imgHomeBtn);
                                                Glide.with(HomeActivity.this)
                                                        .load(tabImgGray2.get(1))
                                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                        .placeholder(tabImgGray[1])
                                                        .into(imgSortBtn);
                                                Glide.with(HomeActivity.this)
                                                        .load(tabImgGray2.get(2))
                                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                        .placeholder(tabImgGray[2])
                                                        .into(imgMessageBtn);
                                                Glide.with(HomeActivity.this)
                                                        .load(tabImgGray2.get(3))
                                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                        .placeholder(tabImgGray[3])
                                                        .into(imgCarBtn);
                                                Glide.with(HomeActivity.this)
                                                        .load(tabImgGray2.get(4))
                                                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                                        .placeholder(tabImgGray[4])
                                                        .into(imgUserBtn);
                                            }else {
                                                imgHomeBtn.setImageResource(tabImgGray[0]);
                                                imgSortBtn.setImageResource(tabImgGray[1]);
                                                imgMessageBtn.setImageResource(tabImgGray[2]);
                                                imgCarBtn.setImageResource(tabImgGray[3]);
                                                imgUserBtn.setImageResource(tabImgGray[4]);
                                            }
                                        } else {
                                            imgHomeBtn.setImageResource(tabImgGray[0]);
                                            imgSortBtn.setImageResource(tabImgGray[1]);
                                            imgMessageBtn.setImageResource(tabImgGray[2]);
                                            imgCarBtn.setImageResource(tabImgGray[3]);
                                            imgUserBtn.setImageResource(tabImgGray[4]);
                                        }
                                        mtext.setTextColor(Color.parseColor(tcolor));
                                        tvSort.setTextColor(Color.parseColor(tcolor));
                                        tvMessage.setTextColor(Color.parseColor(tcolor));
                                        tvCar.setTextColor(Color.parseColor(tcolor));
                                        tvMy.setTextColor(Color.parseColor(tcolor));
                                    }
                                    LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
                                    ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
                                    if (isshow) {
                                        if (tabImgBlue2.size() > 0) {
                                            Glide.with(HomeActivity.this).
                                                    load(tabImgBlue2.get(currentIndex))
                                                    .placeholder(tabImgBlue[0]).
                                                    into(currentIV);
                                        }else {
                                            currentIV.setImageResource(tabImgBlue[currentIndex]);
                                        }
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

                    }
                });
    }
//    /**
//     * 初始化数据并从接口获取底部Tag
//     */
//    public void initData() {
//        initViewPager();
//        initViewPagerData();
//        clickTab();
//        mViewPager.setOffscreenPageLimit(4);
//        mViewPager.setCurrentItem(0);
//        Map<String, String> paramsMap = new HashMap<>();
//        RetrofitClient.getInstance(this).createBaseApi().queryIndexMenu(
//                paramsMap, new BaseObserver<String>(this) {
//                    @Override
//                    public void onNext(String s) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            Logg.json(jsonObject);
//                            String content = jsonObject.optString("content");
//                            if (jsonObject.optString("status").equals("1")) {
//                                JSONObject object = new JSONObject(content);
//                                if ("1".equals(object.optString("isshow"))) {
//                                    isshow = true;
//                                    JSONArray img = object.optJSONArray("img");
//                                    JSONArray imgs = object.optJSONArray("imgs");
//                                    for (int i = 0; i < 5; i++) {
//                                        tabImgBlue2.add(imgs.optString(i));
//                                        tabImgGray2.add(img.optString(i));
//                                    }
//                                    ctcolor = object.optString("ccolors");
//                                    tcolor = object.optString("ccolor");
//                                    bcolor = object.optString("bcolor");
//                                    tabParentLayout.setBackgroundColor(Color.parseColor(bcolor));
//                                    for (int i = 0; i < 5; i++) {
////                                        LinearLayout nextLayout = ((LinearLayout) tabParentLayout.getChildAt(i));
////                                        ImageView nextIV = (ImageView) nextLayout.getChildAt(0);
//                                        if (isshow) {
//                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(0)).placeholder(tabImgGray[0]).into(imgHomeBtn);
//                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(1)).placeholder(tabImgGray[1]).into(imgSortBtn);
//                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(2)).placeholder(tabImgGray[2]).into(imgMessageBtn);
//                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(3)).placeholder(tabImgGray[3]).into(imgCarBtn);
//                                            Glide.with(HomeActivity.this).load(tabImgGray2.get(4)).placeholder(tabImgGray[4]).into(imgUserBtn);
//                                        } else {
//                                            Logg.e("=========================>>>>", i);
//                                            imgHomeBtn.setImageResource(tabImgGray[0]);
//                                            imgSortBtn.setImageResource(tabImgGray[1]);
//                                            imgMessageBtn.setImageResource(tabImgGray[2]);
//                                            imgCarBtn.setImageResource(tabImgGray[3]);
//                                            imgUserBtn.setImageResource(tabImgGray[4]);
//                                        }
////                                        TextView nextTV = (TextView) nextLayout.getChildAt(1);
////                                        nextTV.setTextColor(Color.parseColor(tcolor));
//                                        mtext.setTextColor(Color.parseColor(tcolor));
//                                        tvSort.setTextColor(Color.parseColor(tcolor));
//                                        tvMessage.setTextColor(Color.parseColor(tcolor));
//                                        tvCar.setTextColor(Color.parseColor(tcolor));
//                                        tvMy.setTextColor(Color.parseColor(tcolor));
//                                    }
//                                    LinearLayout currentLayout = ((LinearLayout) tabParentLayout.getChildAt(currentIndex));
//                                    ImageView currentIV = (ImageView) currentLayout.getChildAt(0);
//                                    if (isshow) {
//                                        Log.e("==================", "" + currentIndex);
//                                        Glide.with(HomeActivity.this).
//                                                load(tabImgBlue2.get(currentIndex))
//                                                .placeholder(tabImgBlue[0]).
//                                                into(currentIV);
//                                    } else {
//                                        currentIV.setImageResource(tabImgBlue[currentIndex]);
//                                    }
//                                    TextView currentTV = (TextView) currentLayout.getChildAt(1);
//                                    currentTV.setTextColor(Color.parseColor(ctcolor));
//                                } else {
//                                    isshow = false;
//                                    switchTab(currentIndex);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    protected void hideDialog() {
//                    }
//
//                    @Override
//                    protected void showDialog() {
//                    }
//
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        StringUtil.showToast(HomeActivity.this, e.message);
//                    }
//                });
//    }

    /**
     * 初始化viewpager
     */
    public void initViewPager() {
        fragments.clear();
        mPagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
//                switchTab(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 初始化viewpager
     */
    public void initViewPagerData() {
        MainActivity homeFragment = new MainActivity();
        MesageCenteFragment mesageCenteFragment = new MesageCenteFragment();
        SortFragment sortFragment = new SortFragment();
        UserFragment userFragment = new UserFragment();
        CarFrament carFrament = new CarFrament();
        fragments.add(homeFragment);//首页
        fragments.add(sortFragment);
        fragments.add(mesageCenteFragment);//消息
        fragments.add(carFrament);//发现
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
                        load(tabImgGray2.get(currentIndex)).diskCacheStrategy(DiskCacheStrategy.RESULT).
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
            Logg.e(HomeActivity.position);
            if (HomeActivity.position == 0) {
//                        homeImgBtn.setBackgroundResource(R.mipmap.bottom_01);
                StringUtil.setScalse(imgHomeBtn);//设置缩放动画
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
                params.height = StringUtil.dip2px(HomeActivity.this, 42);
                params.width = StringUtil.dip2px(HomeActivity.this, 42);
                imgHomeBtn.setLayoutParams(params);
            } else {
//                        homeImgBtn.setBackgroundResource(R.mipmap.bottom_11);
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
            }
//            tabLayout.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (index == 0) {
////                        homeImgBtn.setBackgroundResource(R.mipmap.bottom_01);
//                        StringUtil.setScalse(imgHomeBtn);//设置缩放动画
//                        imgHomeBtn.setVisibility(View.VISIBLE);
//                        mtext.setVisibility(View.GONE);
//                        ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
//                        params.height = StringUtil.dip2px(HomeActivity.this, 42);
//                        params.width = StringUtil.dip2px(HomeActivity.this, 42);
//                        imgHomeBtn.setLayoutParams(params);
//                    } else {
////                        homeImgBtn.setBackgroundResource(R.mipmap.bottom_11);
//                        imgHomeBtn.setVisibility(View.VISIBLE);
//                        mtext.setVisibility(View.VISIBLE);
//                        ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
//                        params.height = StringUtil.dip2px(HomeActivity.this, 25);
//                        params.width = StringUtil.dip2px(HomeActivity.this, 25);
//                        imgHomeBtn.setLayoutParams(params);
//                        switch (index) {
//                            case 1:
//                                StringUtil.setScalse(imgSortBtn);//设置缩放动画
//                                break;
//                            case 2:
//                                StringUtil.setScalse(imgMessageBtn);//设置缩放动画
//                                break;
//                            case 3:
//                                StringUtil.setScalse(imgCarBtn);//设置缩放动画
//                                break;
//                            case 4:
//                                StringUtil.setScalse(imgUserBtn);//设置缩放动画
//                                break;
//                        }
//                    }
//                    if (index == 2) {
//                        Flag = "home";
//                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
//                        if (TextUtils.isEmpty(userID)) {
//                            Intent intent4 = new Intent(getApplicationContext(), UserLoginNewActivity.class);
//                            startActivityForResult(intent4, 1);
//                        }
//                    }
//                    mViewPager.setCurrentItem(index);
//                }
//            });

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
        String type = SharedPreferencesUtil.getSharedData(getApplicationContext(), "homeactivty", "type");
        if (!TextUtils.isEmpty(type)) {
            SharedPreferencesUtil.cleanShareData(getApplicationContext(), "homeactivty");
            mViewPager.setCurrentItem(Integer.valueOf(type));
            if (HomeActivity.position == 0) {
                StringUtil.setScalse(imgHomeBtn);//设置缩放动画
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
                params.height = StringUtil.dip2px(HomeActivity.this, 42);
                params.width = StringUtil.dip2px(HomeActivity.this, 42);
                imgHomeBtn.setLayoutParams(params);
            } else {
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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


    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.rl_home, R.id.rl_sort, R.id.rl_message, R.id.rl_car, R.id.rl_my})
    public void onViewClicked(View view) {
        ViewGroup.LayoutParams params = imgHomeBtn.getLayoutParams();
        switch (view.getId()) {
            case R.id.rl_home:
                StringUtil.setScalse(imgHomeBtn);//设置缩放动画
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.GONE);
                params.height = StringUtil.dip2px(HomeActivity.this, 42);
                params.width = StringUtil.dip2px(HomeActivity.this, 42);
                imgHomeBtn.setLayoutParams(params);
                mViewPager.setCurrentItem(0);
                colorChange(imgHomeBtn, mtext, 0);
                colorChange2(imgSortBtn, tvSort, 1);
                colorChange2(imgMessageBtn, tvMessage, 2);
                colorChange2(imgCarBtn, tvCar, 3);
                colorChange2(imgUserBtn, tvMy, 4);
                break;
            case R.id.rl_sort:
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
                StringUtil.setScalse(imgSortBtn);//设置缩放动画
                mViewPager.setCurrentItem(1);
                colorChange2(imgHomeBtn, mtext, 0);
                colorChange(imgSortBtn, tvSort, 1);
                colorChange2(imgMessageBtn, tvMessage, 2);
                colorChange2(imgCarBtn, tvCar, 3);
                colorChange2(imgUserBtn, tvMy, 4);
                break;
            case R.id.rl_message:
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
                StringUtil.setScalse(imgMessageBtn);
                mViewPager.setCurrentItem(2);
                colorChange2(imgHomeBtn, mtext, 0);
                colorChange2(imgSortBtn, tvSort, 1);
                colorChange(imgMessageBtn, tvMessage, 2);
                colorChange2(imgCarBtn, tvCar, 3);
                colorChange2(imgUserBtn, tvMy, 4);
                break;
            case R.id.rl_car:
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
                StringUtil.setScalse(imgCarBtn);
                mViewPager.setCurrentItem(3);
                colorChange2(imgHomeBtn, mtext, 0);
                colorChange2(imgSortBtn, tvSort, 1);
                colorChange2(imgMessageBtn, tvMessage, 2);
                colorChange(imgCarBtn, tvCar, 3);
                colorChange2(imgUserBtn, tvMy, 4);
                break;
            case R.id.rl_my:
                imgHomeBtn.setVisibility(View.VISIBLE);
                mtext.setVisibility(View.VISIBLE);
                params.height = StringUtil.dip2px(HomeActivity.this, 25);
                params.width = StringUtil.dip2px(HomeActivity.this, 25);
                imgHomeBtn.setLayoutParams(params);
                StringUtil.setScalse(imgUserBtn);
                mViewPager.setCurrentItem(4);
                colorChange2(imgHomeBtn, mtext, 0);
                colorChange2(imgSortBtn, tvSort, 1);
                colorChange2(imgMessageBtn, tvMessage, 2);
                colorChange2(imgCarBtn, tvCar, 3);
                colorChange(imgUserBtn, tvMy, 4);
                break;
        }
    }

    /**
     * 选中的颜色变化
     * @param imageView
     * @param textView
     * @param i
     */
    private void colorChange(ImageView imageView, TextView textView, int i) {
        if (isshow) {
            if (tabImgBlue2.size() > 0) {
                Glide.with(this).
                        load(tabImgBlue2.get(i)).
                        into(imageView);
            }else {
                imageView.setImageResource(tabImgBlue[i]);
            }
        } else {
            imageView.setImageResource(tabImgBlue[i]);
        }
        textView.setTextColor(Color.parseColor(ctcolor));
    }

    /**
     * 未选中的颜色变化
     * @param imageView
     * @param textView
     * @param i
     */
    private void colorChange2(ImageView imageView, TextView textView, int i) {

        textView.setTextColor(Color.parseColor(ctcolor));
        if (isshow) {
            if (tabImgGray2.size() > 0) {
                Glide.with(this).load(tabImgGray2.get(i)).into(imageView);
            }else {
                imageView.setImageResource(tabImgGray[i]);
            }
        } else {
            imageView.setImageResource(tabImgGray[i]);
        }
        textView.setTextColor(Color.parseColor(tcolor));
    }

    /**
     * 播放气泡消失动画
     *
     * @param x 气泡的x坐标
     * @param y 气泡的y坐标
     */
    private void playImageDismissAnim(float x, float y) {
        ImageView img_dismiss = new ImageView(HomeActivity.this);
        img_dismiss.setImageResource(R.drawable.tip_anim);
        img_dismiss.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        img_dismiss.setX(x);
        img_dismiss.setY(y);
        activityHomeLayout.addView(img_dismiss);
        //播放气泡消失动画
        ((AnimationDrawable) img_dismiss.getDrawable()).start();
    }

}