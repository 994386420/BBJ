package com.bbk.fragment;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.fastjson.JSON;
import com.andview.refreshview.callback.IFooterCallBack;
import com.bbk.Bean.ButtonListBean;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.NewUserBean;
import com.bbk.Bean.UserBean;
import com.bbk.activity.AboutUsActivity;
import com.bbk.activity.AddressMangerActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.BrokerageActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.CoinGoGoGoActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.ContactActivity;
import com.bbk.activity.FanLiOrderActivity;
import com.bbk.activity.FensiActivity;
import com.bbk.activity.HomeActivity;
import com.bbk.activity.JumpDetailActivty;
import com.bbk.activity.LogisticsQueryActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.OrderListActivity;
import com.bbk.activity.R;
import com.bbk.activity.TuiguangDialogActivity;
import com.bbk.activity.UserAccountActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.UserSuggestionActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.activity.YaoqingFriendsActivity;
import com.bbk.adapter.MyButtonListAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.component.HomeAllComponent2;
import com.bbk.component.HomeAllComponent3;
import com.bbk.component.HomeAllComponent7;
import com.bbk.component.JingbiComponent;
import com.bbk.component.QiandaoComponent;
import com.bbk.component.ShouyiComponent;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.model.DiscountActivity;
import com.bbk.model.JiFenActivity;
import com.bbk.model.MainActivity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.HongbaoDialog;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.AdaptionSizeTextView;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyScrollViewNew;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.bumptech.glide.Glide;
import com.logg.Logg;
import com.pdd.pop.sdk.http.PopAccessTokenClient;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends BaseViewPagerFragment implements OnClickListener, MyButtonListAdapter.userInterface {
    //    @BindView(R.id.tv_shouyi)
//    TextView tvShouyi;
//    @BindView(R.id.tv_hongbao)
//    TextView tvHongbao;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.mjdshopcart)
    LinearLayout mjdshopcart;
    @BindView(R.id.mTaobaoshopcart)
    LinearLayout mTaobaoshopcart;
    @BindView(R.id.ll_fanli_order)
    LinearLayout llFanliOrder;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.ll_jingbi)
    LinearLayout llJingbi;
    @BindView(R.id.ll_fensi)
    LinearLayout llFensi;
    @BindView(R.id.ll_yaoqing)
    LinearLayout llYaoqing;
    @BindView(R.id.ll_all_order)
    LinearLayout llAllOrder;
    @BindView(R.id.ll_daifukuan)
    LinearLayout llDaifukuan;
    @BindView(R.id.ll_daifahuo)
    LinearLayout llDaifahuo;
    @BindView(R.id.ll_daishouhuo)
    LinearLayout llDaishouhuo;
    @BindView(R.id.ll_daipl)
    LinearLayout llDaipl;
    @BindView(R.id.ll_shouhou)
    LinearLayout llShouhou;
    @BindView(R.id.ll_car)
    LinearLayout llCar;
    @BindView(R.id.ll_foot)
    LinearLayout llFoot;
    @BindView(R.id.ll_pl)
    LinearLayout llPl;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.ll_tq)
    LinearLayout llTq;
    @BindView(R.id.ll_xs)
    LinearLayout llXs;
    @BindView(R.id.ll_cjwt)
    LinearLayout llCjwt;
    @BindView(R.id.ll_yjfk)
    LinearLayout llYjfk;
    @BindView(R.id.ll_woyao)
    LinearLayout llWoyao;
    @BindView(R.id.ll_pudao)
    LinearLayout llPudao;
    @BindView(R.id.ll_kefu)
    LinearLayout llKefu;
    @BindView(R.id.ll_adoutbbj)
    LinearLayout llAdoutbbj;
    @BindView(R.id.ll_benyueyugu)
    LinearLayout llBenyueyugu;
    @BindView(R.id.tv_ketimoney)
    TextView tvKetimoney;
    @BindView(R.id.tv_lastmonth)
    TextView tvLastmonth;
    @BindView(R.id.tv_thismonth)
    TextView tvThismonth;
    @BindView(R.id.tv_thismonthyu)
    TextView tvThismonthyu;
    @BindView(R.id.tv_yaoqingma)
    TextView tvYaoqingma;
    @BindView(R.id.tv_so)
    TextView tvSo;
    @BindView(R.id.tv_sl)
    TextView tvSl;
    @BindView(R.id.tv_ss)
    TextView tvSs;
    @BindView(R.id.button_list)
    RecyclerView buttonList;
    @BindView(R.id.scrollView)
    MyScrollViewNew scrollView;
    @BindView(R.id.ll_order_user)
    LinearLayout llOrderUser;
    @BindView(R.id.huodongimg)
    ImageView huodongimg;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_dsh_num)
    AdaptionSizeTextView tvDshNum;
    @BindView(R.id.img_hongbao)
    ImageView imgHongbao;
    @BindView(R.id.hongbao_num)
    TextView hongbaoNum;
    @BindView(R.id.ll_hongbao)
    RelativeLayout llHongbao;
    @BindView(R.id.tv_s4)
    AdaptionSizeTextView tvS4;
    @BindView(R.id.ll_discount)
    LinearLayout llDiscount;
    @BindView(R.id.ll_jifen)
    LinearLayout llJifen;
    @BindView(R.id.tv_cps_order)
    TextView tvCpsOrder;
    private View mView;
    private RelativeLayout newpinglun;
    private TextView sign, mjb, mcollectnum, mfootnum, mnewmsg, mJlzText;
    private View user_head;
    private ImageView mjbimg;
    private LinearLayout mbackground;
    private LinearLayout mjingbi, mcollection, mfoot, mphonechongzhi, mshopcart, morderlist, mlogisticsquery, mycomment,
            mygossip, mfeedback, mcallservices, mabout, msign, mtaobaologin, mFabiaoLayout, mJiebiaoLayout, mAddress;
    private boolean issign = true;
    private DataFlow dataFlow;
    private String token;
    private String signnum = "";
    private ViewGroup anim_mask_layout;
    private ImageView user_img;
    private TextView user_name;
    private boolean isoncreat = false;
    private int num;
    private TextView mchongshi, mtaobaouser, mtaobaotext;
    private RelativeLayout mzhanwei;
    private SmartRefreshLayout xrefresh;
    private boolean isTaoBaoLogin = false;
    private boolean isuserzhezhao = false;
    private int showTimes = 0;
    String clientId = "bf56af3f63144330b7426e4ccb1fa7a1";
    String clientSecret = "eabde3a4bd1fe5e1680c1b6e6eaa6159474200fd";
    private String result_url="http://www.bibijing.com";


    /**
     * 推广合伙人
     *
     * @param savedInstanceState
     */
    @BindView(R.id.ll_tuiguang_user)
    LinearLayout llTuiguang_user;
    @BindView(R.id.tv_tuiguang_tule)
    TextView tvTuiguangTule;
    @BindView(R.id.tuiguang_user)
    RelativeLayout llTuiguang;
    @BindView(R.id.ll_brokerage)
    LinearLayout llBrokerage;
    String isFirstResultUse;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    private UpdataDialog updataDialog;
    private HongbaoDialog hongbaoDialog;
    private String hongbaoMoney, hongbaoMoney1;
    public static String LogFlag = "0";
    private String isGuanzhuweixin;
    public static String ketiMoney;
    private boolean isshowzhezhao = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_user, null);
        ButterKnife.bind(this, mView);
        dataFlow = new DataFlow(getActivity());
        user_head = mView.findViewById(R.id.user_head);
        Glide.with(getActivity()).load(R.drawable.first_01).into(huodongimg);
        initstateView();
        initView();
        initData();
        clickTuiguang();
        llTuiguang.setVisibility(View.GONE);
        return mView;
    }

    /**
     * 签到
     */
    private void userSign() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> params = new HashMap<>();
        params.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().userSign(
                params, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                if (content.equals("-1")) {
                                    sign.setText("已签到");
                                    mjbimg.setVisibility(View.GONE);
                                    issign = false;
                                } else if (content.equals("-2")) {
                                    StringUtil.showToast(getActivity(), "签到异常");
                                } else {
                                    mjb.setText(content);
                                    sign();
                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 字段hzinfo中取type（是否为合作伙伴：0为不是 1 为是）
     */
    private void queryUserInfoMain() {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        TelephonyManager TelephonyMgr = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String token = TelephonyMgr.getDeviceId();
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
        maps.put("userid", userID);
        maps.put("token", token);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryUserInfoMain(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
//                            Log.i("是否合伙人", s);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
//                                    JSONObject object = new JSONObject(jsonObject.optString("content"));
                                UserBean userBean = JSON.parseObject(jsonObject.optString("content"), UserBean.class);
                                String footprint = String.valueOf(userBean.getFootprint());
                                String messages = String.valueOf(userBean.getMessages());
                                String collect = String.valueOf(userBean.getCollect());
                                String sign = userBean.getSign();
                                String jinbi = String.valueOf(userBean.getJinbi());
                                String username = userBean.getUsername();
                                String imgurl = userBean.getImgurl();
                                String str = userBean.getAddjinbi();
                                String exp = userBean.getExp();//鲸力值
                                SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "footprint", userBean.getFootprint() + "");
                                SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "collect", userBean.getCollect() + "");
                                if (userBean.getHzinfo() != null) {
                                    JSONObject json = new JSONObject(userBean.getHzinfo());
                                    if (json.length() > 0) {
                                        String type = json.optString("type");
                                        String totalmoney = json.optString("totalmoney");//佣金收益总金额
                                        String rebate = json.optString("rebate");//邀请好友未领红包个数
//                                        if (totalmoney != null && !totalmoney.equals("")) {
//                                            tvShouyi.setText(totalmoney);
//                                        } else {
//                                            tvShouyi.setText("¥ 0.0");
//                                        }
//                                        if (rebate != null && !rebate.equals("")) {
//                                            tvHongbao.setText(rebate);
//                                        } else {
//                                            tvHongbao.setText("0");
//                                        }
                                        tvLevel.setVisibility(View.GONE);
                                        mJlzText.setVisibility(View.VISIBLE);
                                        llJifen.setVisibility(View.VISIBLE);
                                        if (type.equals("0")) {
                                            llTuiguang.setVisibility(View.GONE);
                                            llTuiguang_user.setVisibility(View.GONE);
                                            view.setVisibility(View.VISIBLE);
                                            tvLevel.setText("普通会员");
                                        } else if (type.equals("1")) {
                                            llTuiguang.setVisibility(View.VISIBLE);
                                            llTuiguang_user.setVisibility(View.GONE);
                                            view.setVisibility(View.VISIBLE);
                                            tvLevel.setText("合作伙伴");
                                        } else if (type.equals("2")) {
                                            llTuiguang.setVisibility(View.VISIBLE);
                                            llTuiguang_user.setVisibility(View.GONE);
                                            view.setVisibility(View.VISIBLE);
                                            tvLevel.setText("超级伙伴");
                                        }
                                    }
                                }
                                signnum = "+" + str;
                                if (jinbi != null && str != null) {
                                    num = Integer.valueOf(jinbi) + Integer.valueOf(str);
                                }
                                mjb.setText(jinbi);
                                mcollectnum.setText(collect);
                                mfootnum.setText(footprint);
                                user_name.setText(username);
                                mJlzText.setText("积分 " + exp);
                                CircleImageView1.getImg(getActivity(), imgurl, user_img);
                                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "imgUrl",
                                        imgurl);
                                SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "nickname",
                                        username);
                                if (!messages.equals("0")) {
                                    mnewmsg.setVisibility(View.VISIBLE);
                                    NewHomeFragment.mnewmsg.setVisibility(View.VISIBLE);
                                } else {
                                    mnewmsg.setVisibility(View.GONE);
                                    NewHomeFragment.mnewmsg.setVisibility(View.GONE);
                                }
                                if (messages != null) {
                                    NewConstants.messages = Integer.parseInt(messages);
                                    mnewmsg.setText(NewConstants.messages + "");
                                    if (HomeActivity.draggableflagview != null) {
                                        if (NewConstants.messages == 0) {
                                            HomeActivity.draggableflagview.setVisibility(View.GONE);
                                        } else {
                                            HomeActivity.draggableflagview.setVisibility(View.VISIBLE);
                                            HomeActivity.draggableflagview.setText(NewConstants.messages + "");
                                        }
                                    }
                                    if (NewHomeFragment.mnewmsg != null) {
                                        NewHomeFragment.mnewmsg.setText(NewConstants.messages + "");
                                    }
                                }
                                if (sign != null) {
                                    initsignnum(sign);
                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        xrefresh.finishRefresh();
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }


    private void queryUserCenter() {
        Map<String, String> maps = new HashMap<String, String>();
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryUserCenter(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
//                                Logg.json(jsonObject);
                                NewUserBean userBean = JSON.parseObject(jsonObject.optString("content"), NewUserBean.class);
                                String messages = String.valueOf(userBean.getMessages());
                                String username = userBean.getUsername();
                                String imgurl = userBean.getImgurl();
                                isGuanzhuweixin = userBean.getGuanzhuweixin();
                                NewConstants.isGuanzhuweixin = isGuanzhuweixin;
                                NewConstants.imgurl = imgurl;
                                SharedPreferencesUtil.putSharedData(getActivity(), "userImg", "img", imgurl);
                                String exp = userBean.getExp();//鲸力值
                                String partner = userBean.getPartner();
                                hongbaoMoney = userBean.getAward();
                                if (hongbaoMoney != null && !hongbaoMoney.equals("")) {
                                    huodongimg.setVisibility(View.VISIBLE);
                                } else {
                                    huodongimg.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(userID)) {
                                    tvCopy.setVisibility(View.VISIBLE);
                                    tvLevel.setVisibility(View.GONE);
                                    mJlzText.setVisibility(View.VISIBLE);
                                    llJifen.setVisibility(View.VISIBLE);
                                    tvYaoqingma.setVisibility(View.VISIBLE);

                                    if (userBean.getSingleMoney() != null && !userBean.getSingleMoney().equals("")) {
                                        llHongbao.setVisibility(View.VISIBLE);
                                        imgHongbao.setVisibility(View.VISIBLE);
                                        hongbaoNum.setText(userBean.getSingleMoneyNumber());
                                        hongbaoMoney1 = userBean.getSingleMoney();
                                    } else {
                                        llHongbao.setVisibility(View.GONE);
                                        imgHongbao.setVisibility(View.GONE);
                                    }

                                    if (partner.equals("0")) {
                                        llTuiguang.setVisibility(View.GONE);
                                        llTuiguang_user.setVisibility(View.GONE);
                                        view.setVisibility(View.VISIBLE);
                                        tvLevel.setText("普通会员");
                                    } else if (partner.equals("1")) {
                                        llTuiguang.setVisibility(View.VISIBLE);
                                        llTuiguang_user.setVisibility(View.GONE);
                                        view.setVisibility(View.VISIBLE);
                                        tvLevel.setText("合作伙伴");
                                    } else if (partner.equals("2")) {
                                        llTuiguang.setVisibility(View.VISIBLE);
                                        llTuiguang_user.setVisibility(View.GONE);
                                        view.setVisibility(View.VISIBLE);
                                        tvLevel.setText("超级伙伴");
                                    }
                                    user_name.setText(username);
                                    mJlzText.setText("积分 " + exp);
                                    CircleImageView1.getImg(getActivity(), imgurl, user_img);
                                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "imgUrl",
                                            imgurl);
                                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "nickname",
                                            username);
                                    if (!messages.equals("0")) {
                                        mnewmsg.setVisibility(View.VISIBLE);
                                        MainActivity.mnewmsg.setVisibility(View.VISIBLE);
                                    } else {
                                        mnewmsg.setVisibility(View.GONE);
                                        MainActivity.mnewmsg.setVisibility(View.GONE);
                                    }
                                    if (messages != null) {
                                        NewConstants.messages = Integer.parseInt(messages);
                                        mnewmsg.setText(NewConstants.messages + "");
                                        if (HomeActivity.draggableflagview != null) {
                                            if (NewConstants.messages == 0) {
                                                HomeActivity.draggableflagview.setVisibility(View.GONE);
                                            } else {
                                                HomeActivity.draggableflagview.setVisibility(View.VISIBLE);
                                                HomeActivity.draggableflagview.setText(NewConstants.messages + "");
                                            }
                                        }
                                        if (MainActivity.mnewmsg != null) {
                                            MainActivity.mnewmsg.setText(NewConstants.messages + "");
                                        }
                                    }

                                    tvKetimoney.setText("可提现金额 ¥" + userBean.getKeti());
                                    ketiMoney = userBean.getKeti();
                                    JSONObject jsonObject1 = new JSONObject(userBean.getEarn());
                                    tvLastmonth.setText("¥" + jsonObject1.optString("lastMonth"));
                                    tvThismonth.setText("¥" + jsonObject1.optString("thisMonth"));
                                    tvThismonthyu.setText("¥" + jsonObject1.optString("thisMonthYu"));
                                    tvYaoqingma.setText("邀请码： " + userBean.getInvicode());

                                    JSONObject jsonObject2 = new JSONObject(userBean.getGoodsInfo());
                                    if (jsonObject2.has("s0")) {
                                        if (jsonObject2.optString("s0").equals("0")) {
                                            tvSo.setVisibility(View.GONE);
                                        } else {
                                            tvSo.setVisibility(View.VISIBLE);
                                            tvSo.setText(jsonObject2.optString("s0"));
                                        }
                                    }
                                    if (jsonObject2.has("s1")) {
                                        if (jsonObject2.optString("s1").equals("0")) {
                                            tvSl.setVisibility(View.GONE);
                                        } else {
                                            tvSl.setVisibility(View.VISIBLE);
                                            tvSl.setText(jsonObject2.optString("s1"));
                                        }
                                    }
                                    if (jsonObject2.has("s3")) {
                                        if (jsonObject2.optString("s3").equals("0")) {
                                            tvSs.setVisibility(View.GONE);
                                        } else {
                                            tvSs.setVisibility(View.VISIBLE);
                                            tvSs.setText(jsonObject2.optString("s3"));
                                        }
                                    }

                                    if (jsonObject2.has("s2")) {
                                        if (jsonObject2.optString("s2").equals("0")) {
                                            tvDshNum.setVisibility(View.GONE);
                                        } else {
                                            tvDshNum.setVisibility(View.VISIBLE);
                                            tvDshNum.setText(jsonObject2.optString("s2"));
                                        }
                                    }
                                    if (jsonObject2.has("s4")) {
                                        if (jsonObject2.optString("s4").equals("0")) {
                                            tvS4.setVisibility(View.GONE);
                                        } else {
                                            tvS4.setVisibility(View.VISIBLE);
                                            tvS4.setText(jsonObject2.optString("s4"));
                                        }
                                    }

                                    if (NewConstants.eventJson != null) {
                                        if (isshowzhezhao) {
                                            final JSONObject jo = new JSONObject(NewConstants.eventJson);
                                            new HomeAlertDialog(getActivity()).builder()
                                                    .setimag(jo.optString("img"))
                                                    .setonclick(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View arg0) {
                                                            EventIdIntentUtil.EventIdIntent(getActivity(), jo);
                                                        }
                                                    }).show();
                                            isshowzhezhao = false;
                                        }
                                    }

                                } else {
                                    user_name.setText("请登录");
                                    user_img.setImageResource(R.mipmap.logo_01);
                                    mjb.setText("0");
                                    llTuiguang_user.setVisibility(View.GONE);
                                    view.setVisibility(View.GONE);
                                    llTuiguang.setVisibility(View.GONE);
                                    tvLevel.setVisibility(View.GONE);
                                    mJlzText.setVisibility(View.GONE);
                                    llJifen.setVisibility(View.GONE);
                                    tvYaoqingma.setVisibility(View.GONE);
                                    tvSo.setVisibility(View.GONE);
                                    tvSs.setVisibility(View.GONE);
                                    tvSl.setVisibility(View.GONE);
                                    tvS4.setVisibility(View.GONE);
                                    tvCopy.setVisibility(View.GONE);
                                    tvDshNum.setVisibility(View.GONE);
                                    xrefresh.finishRefresh();
                                    MainActivity.mnewmsg.setVisibility(View.GONE);
                                    HomeActivity.draggableflagview.setVisibility(View.GONE);
                                    mnewmsg.setVisibility(View.GONE);
                                }

                                if (userBean.getButtonlist() != null) {
                                    List<ButtonListBean> buttonListBean = JSON.parseArray(userBean.getButtonlist(), ButtonListBean.class);
                                    MyButtonListAdapter myButtonListAdapter = new MyButtonListAdapter(getActivity(), buttonListBean);
                                    myButtonListAdapter.setUserInterface(UserFragment.this);
                                    buttonList.setAdapter(myButtonListAdapter);
                                }
                                if (userBean.getShowOrders() != null) {
                                    if (userBean.getShowOrders().equals("1")) {
                                        llOrderUser.setVisibility(View.VISIBLE);
                                    } else {
                                        llOrderUser.setVisibility(View.GONE);
                                    }
                                }
//                                scrollView.scrollTo(0, 0);
                                /**
                                 * 弹窗 写在懒加载里面
                                 */
//                                if (userBean.getEventJson() != null) {
//                                    if (isshowzhezhao) {
//                                        final JSONObject jo = new JSONObject(userBean.getEventJson());
//                                        new HomeAlertDialog(getActivity()).builder()
//                                                .setimag(jo.optString("img"))
//                                                .setonclick(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View arg0) {
//                                                        EventIdIntentUtil.EventIdIntent(getActivity(), jo);
//                                                    }
//                                                }).show();
//                                        isshowzhezhao = false;
//                                    }
//                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        xrefresh.finishRefresh();
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    public void initView() {
        //禁用滑动事件
        buttonList.setNestedScrollingEnabled(false);
        buttonList.setHasFixedSize(true);
        buttonList.setFocusable(false);
        buttonList.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        mAddress = mView.findViewById(R.id.maddress);
        mAddress.setOnClickListener(this);
        mFabiaoLayout = mView.findViewById(R.id.ll_my_fabiao);
        mJiebiaoLayout = mView.findViewById(R.id.ll_my_jiebiao);
        mJlzText = mView.findViewById(R.id.jlz_text);
        anim_mask_layout = createAnimLayout();
        newpinglun = mView.findViewById(R.id.newpinglun);
        xrefresh = mView.findViewById(R.id.xrefresh);
        mzhanwei = mView.findViewById(R.id.mzhanwei);
        user_img = mView.findViewById(R.id.user_img);
//		CircleImageView1.getImg(getActivity(), R.mipmap.logo_01, user_img);
        mbackground = mView.findViewById(R.id.mbackground);
        sign = mView.findViewById(R.id.sign);
        user_name = mView.findViewById(R.id.user_name);
        mjb = mView.findViewById(R.id.mjb);
        mcollectnum = mView.findViewById(R.id.mcollectnum);
        mfootnum = mView.findViewById(R.id.mfootnum);
        mnewmsg = mView.findViewById(R.id.mnewmsg);
        mchongshi = mView.findViewById(R.id.mchongshi);
        mtaobaotext = mView.findViewById(R.id.mtaobaotext);
        mtaobaouser = mView.findViewById(R.id.mtaobaouser);
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        LayoutParams params = mbackground.getLayoutParams();
        params.height = (width * 360) / 828;
        mbackground.setLayoutParams(params);
        mjingbi = mView.findViewById(R.id.mjingbi);
        mcollection = mView.findViewById(R.id.mcollection);
        mfoot = mView.findViewById(R.id.mfoot);
        mphonechongzhi = mView.findViewById(R.id.mphonechongzhi);
//        mshopcart = mView.findViewById(R.id.mshopcart);
        morderlist = mView.findViewById(R.id.morderlist);
        mlogisticsquery = mView.findViewById(R.id.mlogisticsquery);
        mycomment = mView.findViewById(R.id.mycomment);
        mygossip = mView.findViewById(R.id.mygossip);
        mfeedback = mView.findViewById(R.id.mfeedback);
        mtaobaologin = mView.findViewById(R.id.mtaobaologin);
        mcallservices = mView.findViewById(R.id.mcallservices);
        mabout = mView.findViewById(R.id.mabout);
        msign = mView.findViewById(R.id.msign);
        mcallservices = mView.findViewById(R.id.mcallservices);
        mjbimg = mView.findViewById(R.id.mjbimg);
        mFabiaoLayout.setOnClickListener(this);
        mJiebiaoLayout.setOnClickListener(this);
        mjingbi.setOnClickListener(this);
        mfoot.setOnClickListener(this);
        mphonechongzhi.setOnClickListener(this);
//        mshopcart.setOnClickListener(this);
        morderlist.setOnClickListener(this);
        mlogisticsquery.setOnClickListener(this);
        mycomment.setOnClickListener(this);
        mygossip.setOnClickListener(this);
        mfeedback.setOnClickListener(this);
        mcallservices.setOnClickListener(this);
        mabout.setOnClickListener(this);
        msign.setOnClickListener(this);
        user_img.setOnClickListener(this);
        newpinglun.setOnClickListener(this);
        mcollection.setOnClickListener(this);
        user_name.setOnClickListener(this);
        mtaobaologin.setOnClickListener(this);
        xrefresh.setEnableLoadMore(false);
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                initData();
            }
        });
    }

    /**
     * 推广点击事件
     *
     * @return
     */
    private void clickTuiguang() {
        llTuiguang_user.setOnClickListener(this);
    }

    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getContext().getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    private void initstateView() {
        if (Build.VERSION.SDK_INT >= 19) {
            user_head.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight();
        LayoutParams layoutParams = user_head.getLayoutParams();
        layoutParams.height = result;
        user_head.setLayoutParams(layoutParams);
    }

    public void initData() {
        if (AlibcLogin.getInstance().getSession() != null) {
            String nick = AlibcLogin.getInstance().getSession().nick;
            if (nick != null && !"".equals(nick)) {
                mtaobaotext.setText("取消淘宝授权");
                mtaobaouser.setText(nick);
                isTaoBaoLogin = true;
            } else {
                mtaobaotext.setText("淘宝授权登录");
                mtaobaouser.setText("");
                isTaoBaoLogin = false;
            }
        } else {
            mtaobaotext.setText("淘宝授权登录");
            mtaobaouser.setText("");
            isTaoBaoLogin = false;
        }

        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        TelephonyManager TelephonyMgr = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String token = TelephonyMgr.getDeviceId();
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
//        if (!TextUtils.isEmpty(userID)) {
//            queryUserInfoMain();
        queryUserCenter();
//            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
//            if (!TextUtils.isEmpty(userID)) {
//                llBrokerage.postDelayed(new Runnable() {
//                    //                    @Override
//                    public void run() {
//                        //引导页只显示一次
//                        String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstUserFanli", "isFirstUserFanli");
//                        if (TextUtils.isEmpty(isFirstResultUse)) {
//                            isFirstResultUse = "yes";
//                        }
//                        if (isFirstResultUse.equals("yes") && NewConstants.yingdaoFlag.equals("1") && JumpDetailActivty.Flag.equals("home")) {
//                            showGuideViewHehuoren(llBrokerage, llFanliOrder);
//                        }
//                    }
//                }, 2000);
//            }
//        } else {
//            user_name.setText("请登录");
//            user_img.setImageResource(R.mipmap.logo_01);
//            mjb.setText("0");
//            llTuiguang_user.setVisibility(View.GONE);
//            view.setVisibility(View.GONE);
//            llTuiguang.setVisibility(View.GONE);
//            tvLevel.setVisibility(View.GONE);
//            mJlzText.setVisibility(View.GONE);
//            tvYaoqingma.setVisibility(View.GONE);
//            tvSo.setVisibility(View.GONE);
//            tvSs.setVisibility(View.GONE);
//            tvSl.setVisibility(View.GONE);
//            xrefresh.finishRefresh();
//        }


    }

    /**
     * 淘宝授权登录
     */
    private void TaoBaoLoginandLogout() {
        if (isTaoBaoLogin) {
            DialogSingleUtil.show(getActivity(), "取消授权中...");
            AlibcLogin alibcLogin = AlibcLogin.getInstance();

            alibcLogin.logout(getActivity(), new LogoutCallback() {
                @Override
                public void onSuccess() {
                    DialogSingleUtil.dismiss(0);
                    StringUtil.showToast(getActivity(), "退出登录成功");
                    mtaobaotext.setText("淘宝授权登录");
                    mtaobaouser.setText("");
                    SharedPreferencesUtil.cleanShareData(MyApplication.getApplication(), "taobao");
                    isTaoBaoLogin = false;
                }

                @Override
                public void onFailure(int code, String msg) {
                    DialogSingleUtil.dismiss(0);
                    StringUtil.showToast(getActivity(), "退出登录失败 " + code + msg);
                    isTaoBaoLogin = true;
                }
            });
        } else {
            DialogSingleUtil.show(getActivity(), "授权中...");
            final AlibcLogin alibcLogin = AlibcLogin.getInstance();

            alibcLogin.showLogin(getActivity(), new AlibcLoginCallback() {

                @Override
                public void onSuccess() {
                    DialogSingleUtil.dismiss(0);
                    StringUtil.showToast(getActivity(), "登录成功 ");
                    mtaobaotext.setText("取消淘宝授权");
                    mtaobaouser.setText(AlibcLogin.getInstance().getSession().nick.toString());
                    isTaoBaoLogin = true;
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String date = sDateFormat.format(new Date());
                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "taobao", "taobaodata", date);
                    //获取淘宝用户信息
//					Log.e("=====淘宝授权=====", "获取淘宝用户信息: "+AlibcLogin.getInstance().getSession().nick);

                }

                @Override
                public void onFailure(int code, String msg) {
                    DialogSingleUtil.dismiss(0);
                    StringUtil.showToast(getActivity(), "登录失败 ");
                }
            });
            DialogSingleUtil.dismiss(0);
        }
    }

    @Override
    public void onClick(View v) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_tuiguang_user:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    NewConstants.logFlag = "3";
                    intent = new Intent(getActivity(), TuiguangDialogActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mtaobaologin:
//                TaoBaoLoginandLogout();

                break;
            case R.id.mjingbi:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyCoinActivity.class);
                    startActivity(intent);
                }
//                intent = new Intent(getActivity(), ShopOrderActivity.class);
//                startActivity(intent);
                break;
            case R.id.mcollection:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mfoot:
//                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
//                    intent = new Intent(getActivity(), BrowseActivity.class);
//                    startActivity(intent);
//                }
                intent = new Intent(getActivity(), NewDianpuHomeActivity.class);
                startActivity(intent);
                break;
            case R.id.mphonechongzhi:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.bibijing.com/mobile/recharge");
                startActivity(intent);
                break;
//            case R.id.mshopcart:
//                if (TextUtils.isEmpty(userID)) {
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
//                    intent = new Intent(getActivity(), ShopCartActivity.class);
//                    startActivity(intent);
//                }
//                break;
            case R.id.morderlist:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mlogisticsquery:
                intent = new Intent(getActivity(), LogisticsQueryActivity.class);
                startActivity(intent);
                break;
            case R.id.mycomment:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
                break;
            case R.id.newpinglun:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "19";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                }
                break;
            case R.id.mygossip:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyGossipActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mfeedback:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserSuggestionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mcallservices:
                intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
                break;
            case R.id.user_img:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "17";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_name:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "18";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mabout:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.msign:
                if (!TextUtils.isEmpty(userID)) {
                    if (issign) {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("userid", userID);
//                        dataFlow.requestData(3, "newService/userSign", params, this, false);
                        userSign();
                        issign = false;
                    } else {

                    }
                } else {
                    StringUtil.showToast(getActivity(), "请先登录！");
//                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivity(intent);
                }

                break;
            //我的发镖
            case R.id.ll_my_fabiao:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            //我的劫镖
            case R.id.ll_my_jiebiao:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            //我的地址
            case R.id.maddress:
                intent = new Intent(getActivity(), AddressMangerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(R.id.anim_mask_layout);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * @param @param  vg
     * @param @param  view
     * @param @param  location
     * @param @return
     * @return View
     * @throws
     * @Description: 添加视图到动画层
     */
    private View addViewToAnimLayout(final ViewGroup vg) {
//		int x = location[0];
//		int y = location[1];
        ImageView view = new ImageView(getActivity());
        view.setImageResource(R.mipmap.u_jb);
        vg.addView(view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.height = BaseTools.getPixelsFromDp(getActivity(), 20);
        lp.width = BaseTools.getPixelsFromDp(getActivity(), 20);
//		lp.leftMargin = x;
//		lp.topMargin = y;
        float y1 = msign.getY();
        float x1 = msign.getX();
        view.setX(x1 + 50);
        view.setY(y1);
        view.setLayoutParams(lp);
        return view;
    }

    private View addTViewToAnimLayout(final ViewGroup vg) {
//		int x = location[0];
//		int y = location[1];
        TextView view = new TextView(getActivity());
        view.setText(signnum);
        view.setTextColor(Color.parseColor("#f23030"));
        view.setTextSize(BaseTools.getPixelsFromDp(getActivity(), 16));
        vg.addView(view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		lp.leftMargin = x;
//		lp.topMargin = y;
        float y1 = msign.getY();
        float x1 = msign.getX();
        view.setX(x1);
        view.setY(y1);
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 平移动画
     *
     * @return
     */
    private TranslateAnimation initAnimations_One() {
        /** 设置位移动画 向右位移150 */
        final TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 500);
        animation.setDuration(3000);// 设置动画持续时间
        animation.setRepeatCount(2);// 设置重复次数
        animation.setRepeatMode(Animation.REVERSE);// 设置反方向执行
        return animation;
    }


    private void sign() {
        final View view = addViewToAnimLayout(anim_mask_layout);
        int[] end_location = new int[2];
        Animation mTranslateAnimation = new TranslateAnimation
                (TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE,
                        800, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.ABSOLUTE, -1000);// 移动
        mTranslateAnimation.setDuration(3000);
        final View view2 = addTViewToAnimLayout(anim_mask_layout);
        Animation mHiddenAction = AnimationUtils.loadAnimation(getActivity(), R.anim.pingyi_shang);
        view2.setAnimation(mHiddenAction);
        mHiddenAction.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                view2.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
            }
        });
//		 mHiddenAction.start();
//		 AnimationSet mAnimationSet=new AnimationSet(false); //这块要注意，必须设为false,不然组件动画结束后，不会归位。
//		 mAnimationSet.setFillAfter(false);
//		 mAnimationSet.addAnimation(mTranslateAnimation);
//		 view.startAnimation(mAnimationSet);
//		 mAnimationSet.setAnimationListener(new AnimationListener() {
//
//			@Override
//			public void onAnimationStart(Animation arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation arg0) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				anim_mask_layout.getChildAt(0).setVisibility(View.GONE);
////				vg.addView(mjbimg);
//			}
//		});
//		 String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        sign.setText("已签到");
        mjbimg.setVisibility(View.GONE);
        mjb.setText(num + "");


    }

    private void initsignnum(String sign2) {
        if (sign2.equals("1")) {
            sign.setText("已签到");
            mjbimg.setVisibility(View.GONE);
            issign = false;
        } else {
            sign.setText("签到+");
            mjbimg.setVisibility(View.VISIBLE);
            issign = true;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isoncreat) {
            System.gc();
            initData();
        } else {
            isoncreat = true;
        }

    }

    @Override
    protected void loadLazyData() {
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstMyUse", "isFirstMyUserUse");
//        if (isFirstResultUse.equals("no")) {
//            xrefresh.autoRefresh();
//        } else {
        initData();
//        }
//        try {
//            if (showTimes == 0) {
//                msign.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //我的引导页只显示一次
//                        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstMyUse", "isFirstMyUserUse");
//                        if (TextUtils.isEmpty(isFirstResultUse)) {
//                            isFirstResultUse = "yes";
//                        }
//                        if (isFirstResultUse.equals("yes")) {
//                            showGuideView(msign, msign);
//                        }
//                    }
//                });
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
    }

    /**
     * 个人中心引导图层
     *
     * @param targetView
     * @param targetView1
     */
    public void showGuideView(View targetView, final View targetView1) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(msign)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(55)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                SharedPreferencesUtil.putSharedData(getActivity(), "isFirstMyUse", "isFirstMyUserUse", "no");
//                showGuideViewHehuoren(targetView1);
                String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                if (!TextUtils.isEmpty(userID)) {
                    llBrokerage.postDelayed(new Runnable() {
                        //                    @Override
                        public void run() {
                            //引导页只显示一次
                            String isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstUserFanli", "isFirstUserFanli");
                            if (TextUtils.isEmpty(isFirstResultUse)) {
                                isFirstResultUse = "yes";
                            }
                            if (isFirstResultUse.equals("yes") && NewConstants.yingdaoFlag.equals("2")) {
                                showGuideViewHehuoren(llBrokerage, llFanliOrder);
                            }
                        }
                    }, 0);
                }
            }
        });

        builder.addComponent(new QiandaoComponent()).addComponent(new HomeAllComponent2());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(getActivity());
    }

    public void showGuideViewHehuoren(View targetView, final View targetView1) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(30)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                showGuideViewHehuoren1(targetView1);
            }
        });

        builder.addComponent(new ShouyiComponent()).addComponent(new HomeAllComponent7());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(getActivity());
    }

    public void showGuideViewHehuoren1(View targetView1) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(targetView1)
//                .setFullingViewId(R.id.ll_view_group)
                .setAlpha(150)
                .setHighTargetCorner(20)
                .setHighTargetPaddingBottom(30)
                .setHighTargetPaddingRight(10)
                .setHighTargetPaddingLeft(10)
                .setExitAnimationId(android.R.anim.fade_out)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                SharedPreferencesUtil.putSharedData(getActivity(), "isFirstUserFanli", "isFirstUserFanli", "no");
            }
        });

        builder.addComponent(new JingbiComponent()).addComponent(new HomeAllComponent3());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 显示我的购物车
     */
    public void showCart() {
        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        AlibcBasePage alibcBasePage = new AlibcMyCartsPage();
        AlibcTrade.show(getActivity(), alibcBasePage, alibcShowParams, null, exParams, new DemoTradeCallback());
    }

    @OnClick({R.id.tv_cps_order,R.id.ll_jifen, R.id.ll_discount, R.id.ll_sign, R.id.ll_jingbi, R.id.ll_fensi, R.id.ll_yaoqing, R.id.ll_all_order, R.id.ll_daifukuan, R.id.ll_daifahuo, R.id.ll_daishouhuo, R.id.ll_daipl, R.id.ll_shouhou, R.id.ll_car, R.id.ll_foot,
            R.id.ll_pl, R.id.ll_address, R.id.ll_tq, R.id.ll_xs, R.id.ll_cjwt, R.id.ll_yjfk, R.id.ll_woyao, R.id.ll_pudao, R.id.ll_kefu, R.id.ll_adoutbbj, R.id.tv_tuiguang_tule, R.id.ll_brokerage, R.id.mjdshopcart,
            R.id.mTaobaoshopcart, R.id.ll_fanli_order, R.id.ll_benyueyugu, R.id.huodongimg, R.id.img_hongbao})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.tv_cps_order:
                intent = new Intent(getActivity(), FanLiOrderActivity.class);
                startActivity(intent);
                break;
            //积分
            case R.id.ll_jifen:
                intent = new Intent(getActivity(), JiFenActivity.class);
                startActivity(intent);
                break;
            //优惠券
            case R.id.ll_discount:
                intent = new Intent(getActivity(), DiscountActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_tuiguang_tule:
                //推广规则跳转链接
//                String url = BaseApiService.Base_URL + "mobile/user/generalize";
//                intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
//                showMessageDialog(getActivity());
                yongjintixian();
                break;
            case R.id.ll_brokerage:
                intent = new Intent(getActivity(), BrokerageActivity.class);
                startActivity(intent);
                break;
            case R.id.mjdshopcart:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
//                    KeplerApiManager.getWebViewService().openAppWebViewPage(getActivity(),
//                            "https://p.m.jd.com/cart/cart.action",
//                            mKeplerAttachParameter,
//                            mOpenAppAction);
                }
                break;
            case R.id.mTaobaoshopcart:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    showCart();
                }
                break;
            case R.id.ll_fanli_order:
//                    intent = new Intent(getActivity(), FanLiOrderActivity.class);
//                    startActivity(intent);
                intent = new Intent(getActivity(), BrokerageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sign:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "13";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyCoinActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_jingbi:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "14";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CoinGoGoGoActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                }
                break;
            case R.id.ll_fensi:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "15";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), FensiActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_yaoqing:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "16";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), YaoqingFriendsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_all_order:
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "12";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), ShopOrderActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_daifukuan:
                LogFlag = "7";
                mShopOrder(userID, "1");
                break;
            case R.id.ll_daifahuo:
                LogFlag = "8";
                mShopOrder(userID, "2");
                break;
            case R.id.ll_daishouhuo:
                LogFlag = "9";
                mShopOrder(userID, "3");
                break;
            case R.id.ll_daipl:
                LogFlag = "10";
                mShopOrder(userID, "4");
                break;
            case R.id.ll_shouhou:
                LogFlag = "20";
                mShopOrder(userID, "5");
//                if (TextUtils.isEmpty(userID)) {
////                    JumpDetailActivty.Flag = "home";
//                    LogFlag = "11";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
////                    HomeLoadUtil.startChat(getActivity());
//                    MainActivity.consultService(getActivity(), "", "我的",null);
//                }
                break;
            case R.id.ll_car:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CarActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_foot:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BrowseActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_pl:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
                break;
            case R.id.ll_address:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), AddressMangerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_tq:
                break;
            case R.id.ll_xs:
                break;
            case R.id.ll_cjwt:
                break;
            case R.id.ll_yjfk:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserSuggestionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_woyao:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_pudao:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_kefu:
                break;
            case R.id.ll_adoutbbj:
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_benyueyugu:
                intent = new Intent(getActivity(), BrokerageActivity.class);
                startActivity(intent);
                break;
            case R.id.huodongimg:
                showHongbaoDialog(getActivity());
                break;
            case R.id.img_hongbao:
                showHongbaoDialog1(getActivity());
                break;
        }
    }

    private void mShopOrder(String userid, String value) {
        Intent intent;
        if (TextUtils.isEmpty(userid)) {
//            JumpDetailActivty.Flag = "home";
            intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(getActivity(), ShopOrderActivity.class);
            intent.putExtra("status", value);
            startActivity(intent);
        }
    }

    /**
     * 提现弹窗
     * @param context
     * @param content
     */
    public void showMessageDialog(final Context context, String content, String status,String errmsg) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.tixian_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            TextView mTitle = updataDialog.findViewById(R.id.tv_title);
            TextView tv_tixian = updataDialog.findViewById(R.id.tv_tixian);
            tv_tixian.setText("发送“bd+注册手机号”即可提现");
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            LinearLayout llWeiguanzhu = updataDialog.findViewById(R.id.ll_weiguanzhu);//未关注微信布局
            TextView mTianxianMoney = updataDialog.findViewById(R.id.tv_tianxian_money);//已关注有金额布局
            TextView mTianxianMessage = updataDialog.findViewById(R.id.tv_tianxian_message);
            TextView mTianxianFailed = updataDialog.findViewById(R.id.tv_failed_message);

            TextView mOneMoney = updataDialog.findViewById(R.id.tv_one_money);

            /**
             * 提现金额小于一元提示
             */
            double money = Double.parseDouble(ketiMoney);

            if (money<1){
                mOneMoney.setVisibility(View.VISIBLE);
            }

            img_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            /**
             * 已经关注微信
             */
            if (isGuanzhuweixin!= null && isGuanzhuweixin.equals("1")){
                llWeiguanzhu.setVisibility(View.GONE);
                /**
                 * content为提现的金额 content为“”提现失败
                 */
                if (status.equals("1")) {
                    if (content != null && !content.equals("")) {
                        mTianxianMoney.setVisibility(View.VISIBLE);
                        mTianxianMessage.setVisibility(View.VISIBLE);
                        mTianxianMoney.setText(content + "元");
                        mTianxianMessage.setText("已发放至您绑定的微信用户");
                        mTitle.setText("提现成功");
                        tv_update_gengxin.setText("立即查看");
                        tv_update_gengxin.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID, false);
                                if (api.isWXAppInstalled()) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setComponent(cmp);
                                    startActivity(intent);
                                } else {
                                    StringUtil.showToast(getActivity(), "微信未安装");
                                }
                                updataDialog.dismiss();
                            }
                        });
                    }
                    /**
                     * 提现失败
                     */
                    else {
                        mTianxianFailed.setVisibility(View.VISIBLE);
                        mTitle.setText("提现失败");
                        tv_update_gengxin.setText("联系客服");
                        tv_update_gengxin.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                MainActivity.consultService(context);
                            }
                        });
                    }
                }   /**
                 * 余额不足
                 */
                else {
                    String[] strings = errmsg.split("\\|");
                    mTitle.setText(strings[0]);
                    mTianxianFailed.setVisibility(View.VISIBLE);
                    mTianxianFailed.setText(strings[1]);
                    tv_update_gengxin.setText("查看赚钱攻略");
                    tv_update_gengxin.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            String url = "http://www.51biaohuo.com/mobile/html/introduce.jsp?site=1";
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                    });
                }
            }
            /**
             * 未关注微信
             */
            else {
                llWeiguanzhu.setVisibility(View.VISIBLE);
                mOneMoney.setVisibility(View.GONE);
                mTitle.setText("提现攻略");
                tv_update_gengxin.setText("立即去绑定");
                tv_update_gengxin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(),Constants.APP_ID, false);
                        if (api.isWXAppInstalled()) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);
                        }else{
                            StringUtil.showToast(getActivity(), "微信未安装");
                        }
                        updataDialog.dismiss();
                    }
                });
            }

        }
    }

    /**
     * 红包弹窗
     * @param context
     */
    public void showHongbaoDialog(final Context context) {
        if (hongbaoDialog == null || !hongbaoDialog.isShowing()) {
            hongbaoDialog = new HongbaoDialog(context, R.layout.hongbao_dialog_layout,
                    new int[]{R.id.mclose});
            hongbaoDialog.show();
            hongbaoDialog.setCanceledOnTouchOutside(true);
            AdaptionSizeTextView textView = hongbaoDialog.findViewById(R.id.tv_hongbao_money);
            if (hongbaoMoney != null) {
                textView.setText(hongbaoMoney);
            }
            LinearLayout llclose = hongbaoDialog.findViewById(R.id.mclose);
            llclose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hongbaoDialog.dismiss();
                }
            });
        }
    }

    public void showHongbaoDialog1(final Context context) {
        if (hongbaoDialog == null || !hongbaoDialog.isShowing()) {
            hongbaoDialog = new HongbaoDialog(context, R.layout.hongbao_dialog_layout1,
                    new int[]{R.id.mclose});
            hongbaoDialog.show();
            hongbaoDialog.setCanceledOnTouchOutside(true);
            AdaptionSizeTextView textView = hongbaoDialog.findViewById(R.id.tv_hongbao_money);
            AdaptionSizeTextView tvLingqu = hongbaoDialog.findViewById(R.id.tv_lingqu);
            if (hongbaoMoney1 != null) {
                textView.setText(hongbaoMoney1);
            }
            LinearLayout llclose = hongbaoDialog.findViewById(R.id.mclose);
            llclose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    hongbaoDialog.dismiss();
                }
            });
            tvLingqu.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSurpriseGift();
                }
            });
        }
    }

    /**
     * 领取红包
     */
    private void getSurpriseGift() {
        Map<String, String> maps = new HashMap<String, String>();
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().getSurpriseGift(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                StringUtil.showToast(getActivity(), "红包领取成功");
                                hongbaoDialog.dismiss();
                                queryUserCenter();
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    //打开指定页面， 获取授权值，（返回的页面是，你填写的回调地址）

    public String CodeUrl(){
        String url="http://jinbao.pinduoduo.com/open.html";
        //client_id
        url+="?client_id="+clientId;
        //授权类型为CODE
        url+="&response_type=code";
        //授权回调地址
        url+="&redirect_uri="+result_url;
        return url;
    }
    /**
     * 获取访问令牌（access_token）
     * 正式环境：http://open-api.pinduoduo.com/oauth/token
     *  参考    http://open.pinduoduo.com/#/document
     */
    public String  Codeaccess_token(String code){
        String url="http://open-api.pinduoduo.com/oauth/token";
        JSONObject json=new JSONObject();
        try {
            json.put("client_id",clientId);
            json.put("client_secret",clientSecret);
            json.put("grant_type","authorization_code");
            json.put("code",code);
            json.put("redirect_uri",result_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json1 = loadJSON(url, json.toString());
        Logg.json(json1.toString());
        return  json1.toString();
    }

    public String loadJSON (String url,String param) {
        StringBuilder json = new StringBuilder();
        PrintWriter out = null;
        try {
            // Post请求的url，与get不同的是不需要带参数
            URL oracle = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) oracle.openConnection();
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置接收数据的格式
            connection.connect();

            out = new PrintWriter(connection.getOutputStream());
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));

            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            return "-1";
        }
        return json.toString();
    }

    @Override
    public void Intent(String name) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (name) {
            case "购物车":
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "2";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CarActivity.class);
                    startActivity(intent);
                }
                break;
            case "足迹":
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "6";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BrowseActivity.class);
                    startActivity(intent);
                }
                break;
            case "我的评论":
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "5";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MesageCenterActivity.class);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                }
                break;
            case "收货地址":
                NewConstants.address = "1";
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "4";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), AddressMangerActivity.class);
                    startActivity(intent);
                }
                break;
            case "会员特权":
                //推广规则跳转链接
//                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
                String url = BaseApiService.Base_URL + "mobile/user/generalize";
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
//                }
                break;
            case "新手必看":
//                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://bibijing.com/mobile/html/introduce.jsp");
                startActivity(intent);
//                }
                break;
            case "常见问题":
                //常见问题跳转链接
//                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
                String url1 = BaseApiService.Base_URL + "mobile/user/question";
//                String url1 = "https://mms.pinduoduo.com/open.html?response_type=code&client_id=bf56af3f63144330b7426e4ccb1fa7a1&redirect_uri=http://www.bibijing.com&&state=1212&view=h5";
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", url1);
                startActivity(intent);
//                }
                break;
            case "意见反馈":
//                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
//                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
//                    startActivityForResult(intent, 1);
//                } else {
                intent = new Intent(getActivity(), UserSuggestionActivity.class);
                startActivity(intent);
//                }
                break;
            case "我要的":
                if (TextUtils.isEmpty(userID)) {
//                    JumpDetailActivty.Flag = "home";
                    LogFlag = "3";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case "扑倒的":
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "1";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case "联系客服":
//                HomeLoadUtil.startChat(getActivity());
                MainActivity.consultService(getActivity());
                break;
            case "关于比比鲸":
                intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
                break;
            case "我发的飙":
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "3";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case "我接的单":
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "1";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.tv_copy)
    public void onViewClicked() {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(tvYaoqingma.getText().toString());
        StringUtil.showToast(getActivity(), "复制成功");
    }

    /**
     * 登陆回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (requestCode) {
            case 1:
                if (userID != null && !userID.equals("")) {
                    switch (LogFlag) {
                        case "1":
                            intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                            startActivity(intent);
                            break;
                        case "2":
                            intent = new Intent(getActivity(), CarActivity.class);
                            startActivity(intent);
                            break;
                        case "3":
                            intent = new Intent(getActivity(), BidListDetailActivity.class);
                            startActivity(intent);
                            break;
                        case "4":
                            intent = new Intent(getActivity(), AddressMangerActivity.class);
                            startActivity(intent);
                            break;
                        case "5":
                            intent = new Intent(getActivity(), MesageCenterActivity.class);
                            intent.putExtra("type", "1");
                            startActivity(intent);
                            break;
                        case "6":
                            intent = new Intent(getActivity(), BrowseActivity.class);
                            startActivity(intent);
                            break;
                        case "7":
                            mShopOrder(userID, "1");
                            break;
                        case "8":
                            mShopOrder(userID, "2");
                            break;
                        case "9":
                            mShopOrder(userID, "3");
                            break;
                        case "10":
                            mShopOrder(userID, "4");
                            break;
                        case "11":
                            MainActivity.consultService(getActivity());
                            break;
                        case "12":
                            intent = new Intent(getActivity(), ShopOrderActivity.class);
                            startActivity(intent);
                            break;
                        case "13":
                            intent = new Intent(getActivity(), MyCoinActivity.class);
                            startActivity(intent);
                            break;
                        case "14":
                            intent = new Intent(getActivity(), CoinGoGoGoActivity.class);
                            intent.putExtra("type", "0");
                            startActivity(intent);
                            break;
                        case "15":
                            intent = new Intent(getActivity(), FensiActivity.class);
                            startActivity(intent);
                            break;
                        case "16":
                            intent = new Intent(getActivity(), YaoqingFriendsActivity.class);
                            startActivity(intent);
                            break;
                        case "17":
//                            intent = new Intent(getActivity(), UserAccountActivity.class);
//                            startActivity(intent);
                            break;
                        case "18":
//                            intent = new Intent(getActivity(), UserAccountActivity.class);
//                            startActivity(intent);
                            break;
                        case "19":
                            intent = new Intent(getActivity(), MesageCenterActivity.class);
                            intent.putExtra("type", "0");
                            startActivity(intent);
                            break;
                        case "20":
                            mShopOrder(userID, "5");
                            break;
                    }
                }
                break;
        }
    }


    /**
     * 获取提现金额
     */
    public void yongjintixian(){
        String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
        Map<String,String> params = new HashMap<>();
        params.put("mid",mid);
        RetrofitClient.getInstance(getActivity()).createBaseApi()
                .yongjintixian(params, new BaseObserver<String>(getActivity()) {
            @Override
            public void onNext(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    Logg.json(jsonObject);
                    if (jsonObject.optString("status").equals("1")) {
                        showMessageDialog(getActivity()
                                ,jsonObject.optString("content")
                                ,jsonObject.optString("status"),jsonObject.optString("errmsg"));
                    }else if (jsonObject.optString("status").equals("2")){
                        showMessageDialog(getActivity()
                                ,jsonObject.optString("content")
                                ,jsonObject.optString("status"),jsonObject.optString("errmsg"));
                    }else {
                        StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                DialogSingleUtil.show(getActivity());
            }

            @Override
            public void onError(ExceptionHandle.ResponeThrowable e) {
                DialogSingleUtil.dismiss(0);
                StringUtil.showToast(getActivity(), e.message);
            }
        });
    }



}
