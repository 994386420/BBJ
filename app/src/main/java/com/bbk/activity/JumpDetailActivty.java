package com.bbk.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.JumpBean;
import com.bbk.Bean.NewHomeCzgBean;
import com.bbk.Bean.ShareBean;
import com.bbk.adapter.DetailImageAdapter;
import com.bbk.adapter.NewCzgGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageGuanggaoLoader;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.NoFastClickUtils;
import com.bbk.util.ShareFenXiangUtil;
import com.bbk.util.ShareJumpUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.AdaptionSizeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class JumpDetailActivty extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.bprice)
    TextView bprice;
    @BindView(R.id.quan)
    TextView quan;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.detail_image)
    ImageView detailImage;
    @BindView(R.id.detail_image_list)
    RecyclerView detailImageList;
    @BindView(R.id.guess_like_list)
    RecyclerView guessLikeList;
    @BindView(R.id.tv_zuan)
    TextView tvZuan;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_lingquan)
    LinearLayout llLingquan;
    @BindView(R.id.tv_mall)
    TextView tvMall;
    @BindView(R.id.image_fenxiang)
    ImageView imageFenxiang;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_qhj)
    TextView tvQhj;
    @BindView(R.id.tv_zuan1)
    TextView tvZuan1;
    @BindView(R.id.rl_detail)
    RelativeLayout rlDetail;
    @BindView(R.id.view)
    View view;
    private String content;
    List<NewHomeCzgBean> czgBeans;//超值购数据
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    private String url, rowkey, domain, quans,jumpdomain;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    public static String Flag = "";
    public static String flag = "",LogFlag = "";
    private UpdataDialog updataDialog;
    private String isFirstClick;
    private String urltaobaoOrjd ;
    private Handler mHandler = new Handler();
    private boolean cancleJump = true;
    private String isczg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_detail_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, null);
        ButterKnife.bind(this);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                if (getIntent().getStringExtra("content") != null) {
                    content = getIntent().getStringExtra("content");
                    initview();
                }
//                getIndexByType();
            }
        });
        if (getIntent().getStringExtra("content") != null) {
            content = getIntent().getStringExtra("content");
            initview();
        }
        isczg = getIntent().getStringExtra("isczg");
    }

    private void initview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        detailImageList.setHasFixedSize(true);
        detailImageList.setLayoutManager(linearLayoutManager);
        JumpBean jumpBean = JSON.parseObject(content, JumpBean.class);
        url = jumpBean.getUrl();
        rowkey = jumpBean.getRowkey();
        domain = jumpBean.getDomain();
        quans = jumpBean.getQuan();
        if (rowkey != null && jumpBean.getTitle() != null) {
            getIndexByType(rowkey,jumpBean.getTitle());
        }
        if (quans != null && !quans.equals("") && !quans.equals("0")) {
            quan.setText("领券减" + jumpBean.getQuan());
        } else {
            quan.setVisibility(View.GONE);
        }
        if (domain != null) {
            if (domain.equals("jd")) {
                tvQhj.setVisibility(View.GONE);
            } else {
                tvQhj.setVisibility(View.VISIBLE);
            }
        }
        tvTitle.setText(jumpBean.getTitle());
        tvDianpu.setText(jumpBean.getService());
        tvSale.setText(jumpBean.getSale() + "人付款");
        price.setText(jumpBean.getPrice());
        if (jumpBean.getBprice() != null && !jumpBean.getBprice().equals("null")) {
            bprice.setVisibility(View.VISIBLE);
            bprice.setText("¥" + jumpBean.getBprice());
        } else {
            bprice.setVisibility(View.GONE);
        }
        if (domain != null){
            if (domain.equals("taobao")){
                tvMall.setText("淘宝");
            }else if (domain.equals("tmall")){
                tvMall.setText("天猫");
            }else if (domain.equals("jd")){
                tvMall.setText("京东");
            }
        }
        tvZuan.setText(jumpBean.getYongjin());
        tvZuan1.setText(jumpBean.getYongjin());
        bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
        List<Object> DetailimgUrlList = new ArrayList<>();
        /**
         * 加载banner
         */
        List<Object> imgUrlList = new ArrayList<>();
        JSONArray imgs = null;
        try {
            if (jumpBean.getDetailImgs() != null) {
                JSONArray detailImags = new JSONArray(jumpBean.getDetailImgs());
                for (int i = 0; i < detailImags.length(); i++) {
                    String imgUrl = detailImags.getString(i);
                    DetailimgUrlList.add(imgUrl);
                }
                if (DetailimgUrlList.size() > 0) {
                    detailImageList.setAdapter(new DetailImageAdapter(this, DetailimgUrlList));
                    rlDetail.setVisibility(View.VISIBLE);
                    detailImageList.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                } else {
                    rlDetail.setVisibility(View.GONE);
                    detailImageList.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                }
            }
            if (jumpBean.getImgs() != null) {
                imgs = new JSONArray(jumpBean.getImgs());
                for (int i = 0; i < imgs.length(); i++) {
                    String imgUrl = imgs.getString(i);
                    imgUrlList.add(imgUrl);
                }
                banner.setImages(imgUrlList)
                        .setImageLoader(new GlideImageGuanggaoLoader())
                        .setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {

                            }
                        })
                        .setDelayTime(3000)
                        .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setIndicatorGravity(BannerConfig.CENTER)
                        .start();
            }
            refresh.finishRefresh();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 猜你喜欢
     * @param rowkey
     */
    private void getIndexByType(String rowkey,String title) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("type", "1");
        maps.put("page", 1 + "");
        maps.put("rowkey",rowkey);
        maps.put("title", title);
        maps.put("userid",userID);
        RetrofitClient.getInstance(this).createBaseApi().queryAppIndexByType(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                content = jsonObject.optString("content");
                                czgBeans = JSON.parseArray(content, NewHomeCzgBean.class);
                                //禁用滑动事件
                                guessLikeList.setNestedScrollingEnabled(false);
                                guessLikeList.setLayoutManager(new GridLayoutManager(JumpDetailActivty.this, 2));
                                guessLikeList.setAdapter(new NewCzgGridAdapter(JumpDetailActivty.this, czgBeans));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                            DialogSingleUtil.show(JumpDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(JumpDetailActivty.this, e.message);
                    }
                });
    }

    /**
     * 显示菜单；图标动画
     */
    private void showGlobalMenu() {
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            detailImageList.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(detailImageList, "alpha", 0, 1)
                    .setDuration(durationAlpha).start();
        } else {
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImageList, "alpha", 1, 0)
                    .setDuration(durationAlpha).start();
            detailImageList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailImageList.setVisibility(View.GONE);
                }
            }, durationAlpha);
        }

    }

    @OnClick({R.id.back_image, R.id.detail_image, R.id.ll_share, R.id.ll_lingquan})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.detail_image:
                isGlobalMenuShow = !isGlobalMenuShow;
                showGlobalMenu();
                break;
            case R.id.ll_share:
                flag = "share";
                if (TextUtils.isEmpty(userID)) {
//                    Flag = "home";
                    LogFlag = "1";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (NoFastClickUtils.isFastClick()){
                        StringUtil.showToast(this,"对不起，您的点击太快了，请休息一下");
                    }else {
                        llShare.setClickable(false);
                        cancleJump = true;
                        shareCpsInfo();
                    }
                }
                break;
            case R.id.ll_lingquan:
                flag = "lingquan";
                if (TextUtils.isEmpty(userID)) {
//                    Flag = "home";
                    LogFlag = "2";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    if (NoFastClickUtils.isFastClick()){
                        StringUtil.showToast(this,"对不起，您的点击太快了，请休息一下");
                    }else {
//                        llLingquan.setClickable(false);
                        cancleJump = true;
//                        shareCpsInfo();
                        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                        alibcShowParams.setClientType("taobao_scheme");
                        exParams = new HashMap<>();
                        exParams.put("isv_code", "appisvcode");
                        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                        if (domain != null) {
                            if (domain.equals("tmall") || domain.equals("taobao")) {
                                showLoadingDialog(JumpDetailActivty.this);
                                showUrl(url);
                            } else if (domain.equals("jd")) {
                                showLoadingDialog(JumpDetailActivty.this);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (cancleJump) {
                                            updataDialog.dismiss();
                                            KeplerApiManager.getWebViewService().openAppWebViewPage(JumpDetailActivty.this,
                                                    url,
                                                    mKeplerAttachParameter,
                                                    mOpenAppAction);
                                        }
                                    }
                                }, 2000);
                            } else {
                                intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
                                if (url != null) {
                                    intent.putExtra("url", url);
                                }
                                if (rowkey != null) {
                                    intent.putExtra("rowkey", rowkey);
                                }
                                startActivity(intent);
//                    updataDialog.dismiss();
                            }
                        }
                    }
                }
                break;
        }
    }

    /**
     * 打开指定链接
     */
    public void showUrl(String url) {
        final String text = url;
        if (TextUtils.isEmpty(text)) {
            StringUtil.showToast(this, "URL为空");
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cancleJump) {
                    updataDialog.dismiss();
                    AlibcTrade.show(JumpDetailActivty.this, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
                }
            }
        }, 2000);
    }

    /**
     * 分享图片文字及连接
     */
    private void shareCpsInfo() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("rowkey", rowkey);
        RetrofitClient.getInstance(this).createBaseApi().shareCpsInfo(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                content = jsonObject.optString("content");
                                JSONObject jsonObject1 = new JSONObject(content);
//                                Log.i("===",s);
//                                isFirstClick = SharedPreferencesUtil.getSharedData(JumpDetailActivty.this, "isFirstClick", "isFirstClick");
//                                if (TextUtils.isEmpty(isFirstClick)) {
//                                    isFirstClick = "yes";
//                                }
//                                if (isFirstClick.equals("yes")) {
//                                    if (jsonObject1.has("errmsg")) {
//                                        if (jsonObject1.optString("errmsg") != null && !jsonObject1.optString("errmsg").equals("")) {
//                                            showMessageDialog(JumpDetailActivty.this,userID);
////                                        StringUtil.showToast(JumpDetailActivty.this, jsonObject1.optString("errmsg"));
//                                            SharedPreferencesUtil.putSharedData(JumpDetailActivty.this, "isFirstClick", "isFirstClick", "no");
//                                        } else {
//                                            shareOrjump();
//                                        }
//                                    }
//                                }else {
                                  shareOrjump();
//                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        llShare.setClickable(true);
                        llLingquan.setClickable(true);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(JumpDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        llShare.setClickable(true);
                        llLingquan.setClickable(true);
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(JumpDetailActivty.this, e.message);
                    }
                });
    }


    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status, final String url) {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
            Intent intent;
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//                DialogSingleUtil.show(JumpDetailActivty.this);
            } else {
//						mKelperTask = null;
//                DialogSingleUtil.dismiss(0);
            }
            if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
                StringUtil.showToast(JumpDetailActivty.this, "未安装京东");
                intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
                if (url != null) {
                    intent.putExtra("url", url);
                }
                if (rowkey != null) {
                    intent.putExtra("rowkey", rowkey);
                }
                startActivity(intent);
                //未安装京东
            } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
                StringUtil.showToast(JumpDetailActivty.this, "不在白名单");
                //不在白名单
            } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
                StringUtil.showToast(JumpDetailActivty.this, "协议错误");
                //协议错误
            } else if (status == OpenAppAction.OpenAppAction_result_APP) {
                //呼京东成功
            } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
                StringUtil.showToast(JumpDetailActivty.this, "网络异常");
                //网络异常
            }
//				}
//			});
        }
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 1:
                getJumpUrl();
                break;
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     * 成为合伙人
     */
    private  void updateCooperationByUserid(final Context context, String userID) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("rowkey",rowkey);
        RetrofitClient.getInstance(context).createBaseApi().updateCooperationByUserid(
                maps, new BaseObserver<String>(context) {
                    @Override
                    public void onNext(String s) {
                        try {
                            Intent intent;
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(context,"恭喜成为合伙人");
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
                                if (jsonObject1.has("url")) {
                                   urltaobaoOrjd = jsonObject1.optString("url");
                                }
                                if(flag.equals("lingquan")) {
                                    DialogSingleUtil.show(JumpDetailActivty.this);
                                    alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                                    alibcShowParams.setClientType("taobao_scheme");
                                    exParams = new HashMap<>();
                                    exParams.put("isv_code", "appisvcode");
                                    exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                                    if (domain != null) {
                                        if (domain.equals("tmall") || domain.equals("taobao")) {
                                            showUrl(urltaobaoOrjd);
                                        } else if (domain.equals("jd")) {
                                            // 通过url呼京东主站
                                            // url 通过url呼京东主站的地址
                                            // mKeplerAttachParameter 存储第三方传入参数
                                            // mOpenAppAction  呼京东主站回调
                                            KeplerApiManager.getWebViewService().openAppWebViewPage(JumpDetailActivty.this,
                                                    urltaobaoOrjd,
                                                    mKeplerAttachParameter,
                                                    mOpenAppAction);
                                            DialogSingleUtil.dismiss(100);
                                        } else {
                                            intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
                                            if (urltaobaoOrjd != null) {
                                                intent.putExtra("url", urltaobaoOrjd);
                                            }
                                            if (rowkey != null) {
                                                intent.putExtra("rowkey", rowkey);
                                            }
                                            startActivity(intent);
                                            DialogSingleUtil.dismiss(50);
                                        }
                                    }
                                }
                            } else {
                                StringUtil.showToast(context, jsonObject.optString("errmsg"));
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
                        StringUtil.showToast(context, e.message);
                    }
                });
    }

//    /**
//     * 不是合伙人弹窗
//     * @param context
//     * @param useid
//     */
//    public void showMessageDialog(final Context context, final String useid) {
//        if(updataDialog == null || !updataDialog.isShowing()) {
//            //初始化弹窗 布局 点击事件的id
//            updataDialog = new UpdataDialog(context, R.layout.hehuo_dialog_layout,
//                    new int[]{R.id.tv_update_gengxin});
//            updataDialog.show();
//            updataDialog.setCanceledOnTouchOutside(true);
//            TextView tv_update_refuse = updataDialog.findViewById(R.id.tv_update_refuse);
//            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
//            if (flag.equals("lingquan")){
//                tv_update_refuse.setText("任性购");
//            }
//            ImageView img_close = updataDialog.findViewById(R.id.img_close);
//            img_close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updataDialog.dismiss();
//                }
//            });
//            tv_update_refuse.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (flag.equals("share")) {
//                        updataDialog.dismiss();
//                    } else if (flag.equals("lingquan")) {
//                        updataDialog.dismiss();
//                        Intent intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
//                        DialogSingleUtil.show(JumpDetailActivty.this);
//                        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
//                        alibcShowParams.setClientType("taobao_scheme");
//                        exParams = new HashMap<>();
//                        exParams.put("isv_code", "appisvcode");
//                        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
//                        if (domain != null) {
//                            if (domain.equals("tmall") || domain.equals("taobao")) {
//                                showUrl(urltaobaoOrjd);
//                            } else if (domain.equals("jd")) {
//                                // 通过url呼京东主站
//                                // url 通过url呼京东主站的地址
//                                // mKeplerAttachParameter 存储第三方传入参数
//                                // mOpenAppAction  呼京东主站回调
//                                KeplerApiManager.getWebViewService().openAppWebViewPage(JumpDetailActivty.this,
//                                        urltaobaoOrjd,
//                                        mKeplerAttachParameter,
//                                        mOpenAppAction);
//                                DialogSingleUtil.dismiss(100);
//                            } else {
//                                intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
//                                if (url != null) {
//                                    intent.putExtra("url", url);
//                                }
//                                if (rowkey != null) {
//                                    intent.putExtra("rowkey", rowkey);
//                                }
//                                startActivity(intent);
//                                DialogSingleUtil.dismiss(50);
//                            }
//                        }
//                    }
//                }
//            });
//            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    updataDialog.dismiss();
//                    updateCooperationByUserid(context,useid);
//                }
//            });
//        }
//    }

        public void showLoadingDialog(final Context context) {
        if(updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.disanfang_dialog,
                    new int[]{R.id.ll_close});
            updataDialog.show();
            LinearLayout img_close = updataDialog.findViewById(R.id.ll_close);
            ImageView imgLoading = updataDialog.findViewById(R.id.img_loading);
            ImageView imageView = updataDialog.findViewById(R.id.img_app);
            AdaptionSizeTextView adaptionSizeTextView = updataDialog.findViewById(R.id.quan);
            if (domain.equals("jd")){
                jumpdomain = "jumpjd";
            }else if (domain.equals("tmall")){
                jumpdomain = "jumptmall";
            }else if (domain.equals("taobao")){
                jumpdomain = "jumptaobao";
            }
            if (quans != null && !quans.equals("") && !quans.equals("0")) {
                adaptionSizeTextView.setVisibility(View.VISIBLE);
            } else {
                adaptionSizeTextView.setVisibility(View.INVISIBLE);
            }
            int drawS = getResources().getIdentifier(jumpdomain,"mipmap", getPackageName());
            imageView.setImageResource(drawS);
            Glide.with(context).load(R.drawable.tuiguang_d05).into(imgLoading);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                    cancleJump = false;
                }
            });
        }
    }

    private void shareOrjump(){
        if (flag.equals("lingquan")) {
            DialogSingleUtil.show(JumpDetailActivty.this);
            alibcShowParams = new AlibcShowParams(OpenType.Native, false);
            alibcShowParams.setClientType("taobao_scheme");
            exParams = new HashMap<>();
            exParams.put("isv_code", "appisvcode");
            exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
            if (domain != null) {
                if (domain.equals("tmall") || domain.equals("taobao")) {
                    showLoadingDialog(JumpDetailActivty.this);
                    showUrl(url);
                } else if (domain.equals("jd")) {
                    showLoadingDialog(JumpDetailActivty.this);
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (cancleJump) {
                                KeplerApiManager.getWebViewService().openAppWebViewPage(JumpDetailActivty.this,
                                        url,
                                        mKeplerAttachParameter,
                                        mOpenAppAction);
                                updataDialog.dismiss();
                            }
                        }
                    }, 2000);
                } else {
                    Intent intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
                    if (url != null) {
                        intent.putExtra("url", url);
                    }
                    if (rowkey != null) {
                        intent.putExtra("rowkey", rowkey);
                    }
                    startActivity(intent);
//                    updataDialog.dismiss();
                }
            }

        } else if (flag.equals("share")) {
            ShareBean shareBean = JSON.parseObject(content, ShareBean.class);
            if (shareBean.getImgUrl() != null) {
                Glide.with(JumpDetailActivty.this).load(shareBean.getImgUrl()).priority(Priority.HIGH).into(imageFenxiang);
            }
            String wenan = "";
            if (shareBean.getWenan() != null) {
                wenan = shareBean.getWenan().replace("|", "\n");
            }
            ClipboardManager cm = (ClipboardManager)JumpDetailActivty.this.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(wenan);
//                                            StringUtil.showToast(JumpDetailActivty.this,"标题已复制，分享可直接粘贴");
            ShareJumpUtil.showShareDialog(llShare, JumpDetailActivty.this, shareBean.getTitle(), "", shareBean.getShareUrl(), shareBean.getImgUrl(), "", imageFenxiang, wenan);
        }
    }

    @Override
    protected void onDestroy() {
        if (JumpDetailActivty.this != null && JumpDetailActivty.this.isFinishing()) {
            DialogSingleUtil.dismiss(0);
        }
        super.onDestroy();
    }

    private void getJumpUrl() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userid", userID);
        if (tvTitle != null) {
            params.put("title", tvTitle.getText().toString());
        }
        if (url != null) {
            params.put("url", url);
        }
        if (domain != null) {
            params.put("domain", domain);
        }
        params.put("client", "andorid");
        if (isczg != null) {
            params.put("isczg", isczg);// 如果是从超值购请求的   新增参数  isczg=1,否则可以不传
        }
        if (bprice != null) {
            params.put("bprice", bprice.getText().toString());
        }
        if (rowkey != null){
            params.put("rowkey",rowkey);
        }
        params.put("client", "andorid");
        RetrofitClient.getInstance(this).createBaseApi().getJumpUrl(
                params, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                JumpBean jumpBean = JSON.parseObject(content,JumpBean.class);
                                url = jumpBean.getUrl();
                                switch (LogFlag){
                                    case "1":
                                        llShare.setClickable(false);
                                        cancleJump = true;
                                        shareCpsInfo();
                                        break;
                                    case "2":
                                        cancleJump = true;
                                        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                                        alibcShowParams.setClientType("taobao_scheme");
                                        exParams = new HashMap<>();
                                        exParams.put("isv_code", "appisvcode");
                                        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                                        if (domain != null) {
                                            if (domain.equals("tmall") || domain.equals("taobao")) {
                                                showLoadingDialog(JumpDetailActivty.this);
                                                showUrl(url);
                                            } else if (domain.equals("jd")) {
                                                showLoadingDialog(JumpDetailActivty.this);
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (cancleJump) {
                                                            updataDialog.dismiss();
                                                            KeplerApiManager.getWebViewService().openAppWebViewPage(JumpDetailActivty.this,
                                                                    url,
                                                                    mKeplerAttachParameter,
                                                                    mOpenAppAction);
                                                        }
                                                    }
                                                }, 2000);
                                            } else {
                                                Intent intent = new Intent(JumpDetailActivty.this, WebViewActivity.class);
                                                if (url != null) {
                                                    intent.putExtra("url", url);
                                                }
                                                if (rowkey != null) {
                                                    intent.putExtra("rowkey", rowkey);
                                                }
                                                startActivity(intent);
                                            }
                                        }
                                        break;
                                }
                            } else {
                                StringUtil.showToast(JumpDetailActivty.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(JumpDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
//						DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(JumpDetailActivty.this, e.message);
                    }
                });
    }
}
