package com.bbk.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.UserBean;
import com.bbk.activity.AboutUsActivity;
import com.bbk.activity.AddressMangerActivity;
import com.bbk.activity.BidListDetailActivity;
import com.bbk.activity.BidMyListDetailActivity;
import com.bbk.activity.BrokerageActivity;
import com.bbk.activity.BrowseActivity;
import com.bbk.activity.CollectionActivity;
import com.bbk.activity.ContactActivity;
import com.bbk.activity.FensiActivity;
import com.bbk.activity.LogisticsQueryActivity;
import com.bbk.activity.MesageCenterActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.MyCoinActivity;
import com.bbk.activity.MyGossipActivity;
import com.bbk.activity.OrderListActivity;
import com.bbk.activity.R;
import com.bbk.activity.ShopCartActivity;
import com.bbk.activity.TuiguangDialogActivity;
import com.bbk.activity.UserAccountActivity;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.activity.UserSuggestionActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.component.HomeAllComponent2;
import com.bbk.component.HomeAllComponent3;
import com.bbk.component.JingbiComponent;
import com.bbk.component.QiandaoComponent;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.NewConstants;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CircleImageView1;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends BaseViewPagerFragment implements OnClickListener {
    @BindView(R.id.tv_shouyi)
    TextView tvShouyi;
    @BindView(R.id.tv_hongbao)
    TextView tvHongbao;
    @BindView(R.id.tv_level)
    TextView tvLevel;
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
    @BindView(R.id.ll_fensi)
    LinearLayout llFensi;
    String isFirstResultUse;

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
    private void  userSign() {
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
                            Log.i("是否合伙人", s);
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
//                                    JSONObject object = new JSONObject(jsonObject.optString("content"));
                                UserBean userBean = JSON.parseObject(jsonObject.optString("content"),UserBean.class);
                                    String footprint = String.valueOf(userBean.getFootprint());
                                    String messages = String.valueOf(userBean.getMessages());
                                    String collect = String.valueOf(userBean.getCollect());
                                    String sign = userBean.getSign();
                                    String jinbi = String.valueOf(userBean.getJinbi());
                                    String username = userBean.getUsername();
                                    String imgurl = userBean.getImgurl();
                                    String str = userBean.getAddjinbi();
                                    String exp = userBean.getExp();//鲸力值
                                    SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "footprint", userBean.getFootprint()+"");
                                    SharedPreferencesUtil.putSharedData(getActivity(), "userInfor", "collect", userBean.getCollect()+"");
                                    if (userBean.getHzinfo() != null) {
                                        JSONObject json = new JSONObject(userBean.getHzinfo());
                                        if (json.length() > 0) {
                                            String type = json.optString("type");
                                            String totalmoney = json.optString("totalmoney");//佣金收益总金额
                                            String rebate = json.optString("rebate");//邀请好友未领红包个数
                                            if (totalmoney != null && !totalmoney.equals("")) {
                                                tvShouyi.setText(totalmoney);
                                            } else {
                                                tvShouyi.setText("¥ 0.0");
                                            }
                                            if (rebate != null && !rebate.equals("")) {
                                                tvHongbao.setText(rebate);
                                            } else {
                                                tvHongbao.setText("0");
                                            }
                                            tvLevel.setVisibility(View.VISIBLE);
                                            if (type.equals("0")) {
                                                llTuiguang.setVisibility(View.GONE);
                                                llTuiguang_user.setVisibility(View.VISIBLE);
                                                tvLevel.setText("普通会员");
                                            } else if (type.equals("1")){
                                                llTuiguang.setVisibility(View.VISIBLE);
                                                llTuiguang_user.setVisibility(View.GONE);
                                                tvLevel.setText("合作伙伴");
                                            }else if (type.equals("2")){
                                                llTuiguang.setVisibility(View.VISIBLE);
                                                llTuiguang_user.setVisibility(View.GONE);
                                                tvLevel.setText("超级伙伴");
                                            }
                                        }
                                    }
                                    signnum = "+" + str;
                                    num = Integer.valueOf(jinbi) + Integer.valueOf(str);
                                    mjb.setText(jinbi);
                                    mcollectnum.setText(collect);
                                    mfootnum.setText(footprint);
                                    mnewmsg.setText(messages);
                                    user_name.setText(username);
                                    mJlzText.setText("鲸力值" + exp);
                                    CircleImageView1.getImg(getActivity(), imgurl, user_img);
                                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "imgUrl",
                                            imgurl);
                                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "nickname",
                                            username);
                                    if (!messages.equals("0")) {
                                        mnewmsg.setVisibility(View.VISIBLE);
                                    } else {
                                        mnewmsg.setVisibility(View.GONE);
                                    }
                                    initsignnum(sign);
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
        mshopcart = mView.findViewById(R.id.mshopcart);
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
        mshopcart.setOnClickListener(this);
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
        if (!TextUtils.isEmpty(userID)) {
            queryUserInfoMain();
        } else {
            user_name.setText("请登录");
            user_img.setImageResource(R.mipmap.logo_01);
            mjb.setText("0");
            llTuiguang_user.setVisibility(View.VISIBLE);
            llTuiguang.setVisibility(View.GONE);
            tvLevel.setVisibility(View.GONE);
            xrefresh.finishRefresh();
        }


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
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    NewConstants.logFlag = "3";
                    intent = new Intent(getActivity(), TuiguangDialogActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mtaobaologin:
                TaoBaoLoginandLogout();
                break;
            case R.id.mjingbi:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyCoinActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mcollection:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mfoot:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), BrowseActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mphonechongzhi:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://www.bibijing.com/mobile/recharge");
                startActivity(intent);
                break;
            case R.id.mshopcart:
                if (TextUtils.isEmpty(userID)) {
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), ShopCartActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.morderlist:
                if (TextUtils.isEmpty(userID)) {
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
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), MyGossipActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.mfeedback:
                if (TextUtils.isEmpty(userID)) {
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
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(getActivity(), UserAccountActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_name:
                if (TextUtils.isEmpty(userID)) {
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
                    intent = new Intent(getActivity(), UserLoginNewActivity.class);
                    startActivity(intent);
                }

                break;
            //我的发镖
            case R.id.ll_my_fabiao:
                if (TextUtils.isEmpty(userID)) {
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
        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(),"isFirstMyUse", "isFirstMyUserUse");
        if (isFirstResultUse.equals("no")) {
            xrefresh.autoRefresh();
        }else {
            initData();
        }
        try {
            if (showTimes == 0) {
                msign.post(new Runnable() {
                    @Override
                    public void run() {
                        //我的引导页只显示一次
                        isFirstResultUse = SharedPreferencesUtil.getSharedData(getActivity(), "isFirstMyUse", "isFirstMyUserUse");
                        if (TextUtils.isEmpty(isFirstResultUse)) {
                            isFirstResultUse = "yes";
                        }
                        if (isFirstResultUse.equals("yes")) {
                            showGuideView(msign, msign);
                        }
                    }
                });
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
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
                showGuideViewHehuoren(targetView1);
            }
        });

        builder.addComponent(new QiandaoComponent()).addComponent(new HomeAllComponent2());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(getActivity());
    }

    public void showGuideViewHehuoren(View targetView) {
        showTimes++;
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(llTuiguang_user)
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
                SharedPreferencesUtil.putSharedData(getActivity(), "isFirstMyUse", "isFirstMyUserUse", "no");
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


    @OnClick({R.id.tv_tuiguang_tule, R.id.ll_brokerage, R.id.ll_fensi})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_tuiguang_tule:
                //推广规则跳转链接
                String url = BaseApiService.Base_URL + "mobile/user/generalize";
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
            case R.id.ll_brokerage:
                intent = new Intent(getActivity(), BrokerageActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_fensi:
                intent = new Intent(getActivity(), FensiActivity.class);
                startActivity(intent);
                break;
        }
    }
}
