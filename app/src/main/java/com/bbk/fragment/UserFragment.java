package com.bbk.fragment;

import android.content.ClipboardManager;
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

import com.ali.auth.third.login.callback.LogoutCallback;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.fastjson.JSON;
import com.appkefu.lib.interfaces.KFAPIs;
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
import com.bbk.flow.DataFlow;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.DianpuHomeActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
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
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.lang.reflect.Field;
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
    LinearLayout llTuiguang;
    @BindView(R.id.ll_brokerage)
    LinearLayout llBrokerage;
    String isFirstResultUse;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    private UpdataDialog updataDialog;
    private HongbaoDialog hongbaoDialog;
    private String hongbaoMoney;
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
                                        tvLevel.setVisibility(View.VISIBLE);
                                        mJlzText.setVisibility(View.VISIBLE);
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
                                mJlzText.setText("鲸力值" + exp);
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
                                    if (HomeActivity.mNumImageView != null) {
//                                        HomeActivity.mNumImageView.setNum(NewConstants.messages);
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
                                NewConstants.imgurl = imgurl;
                                String exp = userBean.getExp();//鲸力值
                                String partner = userBean.getPartner();
                                hongbaoMoney = userBean.getAward();
                                if (hongbaoMoney != null && !hongbaoMoney.equals("")) {
                                    huodongimg.setVisibility(View.VISIBLE);
                                }else {
                                    huodongimg.setVisibility(View.GONE);
                                }
                                if (!TextUtils.isEmpty(userID)) {
                                    tvCopy.setVisibility(View.VISIBLE);
                                    tvLevel.setVisibility(View.VISIBLE);
                                    mJlzText.setVisibility(View.VISIBLE);
                                    tvYaoqingma.setVisibility(View.VISIBLE);
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
                                    mJlzText.setText("鲸力值" + exp);
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
//                                        if (HomeActivity.mNumImageView != null) {
//                                            HomeActivity.mNumImageView.setNum(NewConstants.messages);
//                                        }
                                        if ( MainActivity.mnewmsg != null) {
                                            MainActivity.mnewmsg.setText(NewConstants.messages + "");
                                        }
                                    }

                                    tvKetimoney.setText("可提现金额 ¥" + userBean.getKeti());
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
                                } else {
                                    user_name.setText("请登录");
                                    user_img.setImageResource(R.mipmap.logo_01);
                                    mjb.setText("0");
                                    llTuiguang_user.setVisibility(View.GONE);
                                    view.setVisibility(View.GONE);
                                    llTuiguang.setVisibility(View.GONE);
                                    tvLevel.setVisibility(View.GONE);
                                    mJlzText.setVisibility(View.GONE);
                                    tvYaoqingma.setVisibility(View.GONE);
                                    tvSo.setVisibility(View.GONE);
                                    tvSs.setVisibility(View.GONE);
                                    tvSl.setVisibility(View.GONE);
                                    tvCopy.setVisibility(View.GONE);
                                    xrefresh.finishRefresh();
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
                    JumpDetailActivty.Flag = "home";
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
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_name:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
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
                    JumpDetailActivty.Flag = "home";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

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

    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();

    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status, final String url) {
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
            Intent intent;
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
                DialogSingleUtil.show(getActivity());
            } else {
//						mKelperTask = null;
                DialogSingleUtil.dismiss(0);
            }
            if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
                StringUtil.showToast(getContext(), "未安装京东");
                intent = new Intent(getActivity(), WebViewActivity.class);
                if (url != null) {
                    intent.putExtra("url", url);
                }
                startActivity(intent);
                //未安装京东
            } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
                StringUtil.showToast(getActivity(), "不在白名单");
                //不在白名单
            } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
                StringUtil.showToast(getActivity(), "协议错误");
                //协议错误
            } else if (status == OpenAppAction.OpenAppAction_result_APP) {
                //呼京东成功
            } else if (status == OpenAppAction.OpenAppAction_result_NetError) {
                StringUtil.showToast(getActivity(), "网络异常");
                //网络异常
            }
//				}
//			});
        }
    };

    @OnClick({R.id.ll_sign, R.id.ll_jingbi, R.id.ll_fensi, R.id.ll_yaoqing, R.id.ll_all_order, R.id.ll_daifukuan, R.id.ll_daifahuo, R.id.ll_daishouhuo, R.id.ll_daipl, R.id.ll_shouhou, R.id.ll_car, R.id.ll_foot,
            R.id.ll_pl, R.id.ll_address, R.id.ll_tq, R.id.ll_xs, R.id.ll_cjwt, R.id.ll_yjfk, R.id.ll_woyao, R.id.ll_pudao, R.id.ll_kefu, R.id.ll_adoutbbj, R.id.tv_tuiguang_tule, R.id.ll_brokerage, R.id.mjdshopcart,
            R.id.mTaobaoshopcart, R.id.ll_fanli_order, R.id.ll_benyueyugu,R.id.huodongimg})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.tv_tuiguang_tule:
                //推广规则跳转链接
//                String url = BaseApiService.Base_URL + "mobile/user/generalize";
//                intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
                showMessageDialog(getActivity());
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
                    KeplerApiManager.getWebViewService().openAppWebViewPage(getActivity(),
                            "https://p.m.jd.com/cart/cart.action",
                            mKeplerAttachParameter,
                            mOpenAppAction);
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
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyCoinActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_jingbi:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
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
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), FensiActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_yaoqing:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), YaoqingFriendsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_all_order:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), ShopOrderActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_daifukuan:
                mShopOrder(userID, "1");
                break;
            case R.id.ll_daifahuo:
                mShopOrder(userID, "2");
                break;
            case R.id.ll_daishouhuo:
                mShopOrder(userID, "3");
                break;
            case R.id.ll_daipl:
                mShopOrder(userID, "4");
                break;
            case R.id.ll_shouhou:
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    startChat();
                }
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
        }
    }

    private void mShopOrder(String userid, String value) {
        Intent intent;
        if (TextUtils.isEmpty(userid)) {
            JumpDetailActivty.Flag = "home";
            intent = new Intent(getActivity(), UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            intent = new Intent(getActivity(), ShopOrderActivity.class);
            intent.putExtra("status", value);
            startActivity(intent);
        }
    }

    public void showMessageDialog(final Context context) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.tixian_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = updataDialog.findViewById(R.id.tv_update_gengxin);
            AdaptionSizeTextView tv_tixian = updataDialog.findViewById(R.id.tv_tixian);
            tv_tixian.setText("发送“佣金提现”领取现金");
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
        }
    }
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
    @Override
    public void Intent(String name) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        switch (name) {
            case "购物车":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CarActivity.class);
                    startActivity(intent);
                }
                break;
            case "足迹":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BrowseActivity.class);
                    startActivity(intent);
                }
                break;
            case "我的评论":
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
            case "收货地址":
                NewConstants.address = "1";
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), AddressMangerActivity.class);
                    startActivity(intent);
                }
                break;
            case "会员特权":
                //推广规则跳转链接
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    String url = BaseApiService.Base_URL + "mobile/user/generalize";
                    intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                break;
            case "新手必看":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", "http://bibijing.com/mobile/html/introduce.jsp");
                    startActivity(intent);
                }
                break;
            case "常见问题":
                //常见问题跳转链接
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    String url1 = BaseApiService.Base_URL + "mobile/user/question";
                    intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra("url", url1);
                    startActivity(intent);
                }
                break;
            case "意见反馈":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserSuggestionActivity.class);
                    startActivity(intent);
                }
                break;
            case "我要的":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case "扑倒的":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BidMyListDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case "联系客服":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    startChat();
                }
                break;
            case "关于比比鲸":
                if (TextUtils.isEmpty(userID)) {
                    JumpDetailActivty.Flag = "home";
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), AboutUsActivity.class);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    private void startChat() {
        //
        KFAPIs.startChat(getActivity(),
                "bbjkfxz", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
                "比比鲸客服", // 2. 会话界面标题，可自定义
                null, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
                // 如果不想发送此信息，可以将此信息设置为""或者null
                true, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
                // 显示:true, 不显示:false
                5, // 5. 默认显示消息数量
                //修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
                "http://www.bibijing.com/images/zhanwei/logo.png",//6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
                NewConstants.imgurl, //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
                false, // 8. 默认机器人应答
                false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
                null);

    }

    @OnClick(R.id.tv_copy)
    public void onViewClicked() {
        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(tvYaoqingma.getText().toString());
        StringUtil.showToast(getActivity(), "复制成功");
    }
}
