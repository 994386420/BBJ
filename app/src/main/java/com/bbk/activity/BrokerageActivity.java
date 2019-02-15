package com.bbk.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.fragment.ShouyiActivity;
import com.bbk.model.MainActivity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bbk.fragment.UserFragment.ketiMoney;

/**
 * 收益报表
 */
public class BrokerageActivity extends BaseActivity {

    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_tixian)
    TextView tvTixian;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.henggang213)
    View henggang213;
    @BindView(R.id.tv_money1)
    TextView tvMoney1;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.tv_money2)
    TextView tvMoney2;
    @BindView(R.id.tv_money3)
    TextView tvMoney3;
    @BindView(R.id.tv_money4)
    TextView tvMoney4;
    @BindView(R.id.tv_fukuang_number)
    TextView tvFukuangNumber;
    @BindView(R.id.tv_yongjin)
    TextView tvYongjin;
    @BindView(R.id.brokerage_layout)
    LinearLayout brokerageLayout;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    String type = "1";
    @BindView(R.id.ll_one)
    LinearLayout llOne;
    @BindView(R.id.ll_two)
    LinearLayout llTwo;
    @BindView(R.id.ll_three)
    LinearLayout llThree;
    @BindView(R.id.ll_four)
    LinearLayout llFour;
    @BindView(R.id.tv_fukuang_number_jinri)
    TextView tvFukuangNumberJinri;
    @BindView(R.id.tv_yongjin_jinri)
    TextView tvYongjinJinri;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title_text1)
    TextView titleText1;
    @BindView(R.id.tv_tixian_detail)
    TextView tvTixianDetail;
    @BindView(R.id.shensu_jilu)
    TextView shensuJilu;
    @BindView(R.id.ll_mingxi)
    LinearLayout llMingxi;
    @BindView(R.id.ll_shensu)
    LinearLayout llShensu;
    @BindView(R.id.tv_tixian_money)
    TextView tvTixianMoney;
    private UpdataDialog updataDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokerage_layout);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshLayout.setEnableLoadMore(false);
        titleText1.setVisibility(View.VISIBLE);
        titleText1.setText("我要申诉");
        titleText.setText("收益报表");
        tablayout.addTab(tablayout.newTab().setText("淘宝收益"));
        tablayout.addTab(tablayout.newTab().setText("京东收益"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.addOnTabSelectedListener(tabSelectedListener);
        StringUtil.setIndicator(tablayout, 50, 50);
        queryUserBrokerage();
        refreshAndloda();
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryUserBrokerage();
            }
        });
    }

    /**
     * table点击事件
     */
    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                type = "1";
            } else if (j == 1) {
                type = "2";
            }
            queryUserBrokerage();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    /**
     * 查询用户收益
     */
    private void queryUserBrokerage() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);//type（类型 1为淘宝佣金	2为京东佣金）
        RetrofitClient.getInstance(this).createBaseApi().queryUserBrokerage(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                BrokerageBean brokerageBean = JSON.parseObject(jsonObject.optString("content"), BrokerageBean.class);
                                if (brokerageBean.getTotalmoney() != null && !brokerageBean.getTotalmoney().equals("")) {
                                    tvMoney.setText(brokerageBean.getTotalmoney());
                                } else {
                                    tvMoney.setText("¥ 0.0");
                                }
                                tvTixianMoney.setText("累计提现："+brokerageBean.getTotaltixian()+"元");
                                tvMoney1.setText(brokerageBean.getOne());
                                tvMoney2.setText(brokerageBean.getTwo());
                                tvMoney3.setText(brokerageBean.getThree());
                                tvMoney4.setText(brokerageBean.getFour());
                                tvFukuangNumber.setText(brokerageBean.getPayCount());
                                if (brokerageBean.getYesYongjinSum() != null && !brokerageBean.getYesYongjinSum().equals("")) {
                                    tvYongjin.setText("¥ " + brokerageBean.getYesYongjinSum());
                                } else {
                                    tvYongjin.setText("¥ 0.0");
                                }

                                if (brokerageBean.getJinYongjinSum() != null && !brokerageBean.getJinYongjinSum().equals("")) {
                                    tvYongjinJinri.setText("¥ " + brokerageBean.getJinYongjinSum());
                                } else {
                                    tvYongjinJinri.setText("¥ 0.0");
                                }
                                if (brokerageBean.getJinpayCount() != null && !brokerageBean.getJinpayCount().equals("")) {
                                    tvFukuangNumberJinri.setText(brokerageBean.getJinpayCount());
                                } else {
                                    tvFukuangNumberJinri.setText("0");
                                }
                            } else {
                                StringUtil.showToast(BrokerageActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(BrokerageActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(BrokerageActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ll_shensu, R.id.title_back_btn, R.id.tv_tixian, R.id.tablayout, R.id.ll_one, R.id.ll_two, R.id.ll_three, R.id.ll_four, R.id.title_text1, R.id.tv_tixian_detail, R.id.shensu_jilu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_shensu:
                intent = new Intent(this, UserShenSuActivity.class);
                startActivity(intent);
                break;
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.tv_tixian:
//                new AlertDialog(this).builder().setTitle("提现攻略")
//                        .setMsg("第1步：搜索并关注“比比鲸大数据”公众号")
//                        .setMsg2("第2步：发送“佣金提现”")
//                        .setMsg3("第3步：绑定账号，验证身份")
//                        .setMsg4("第4步：领取红包")
//                        .setLeft()
//                        .setPositiveButton("OK,去微信提现", new View.OnClickListener() {
//                            @SuppressLint("NewApi")
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).setPositiveButtonColor("#ffffff").setPositiveBackgroundColor("#ff7d41").show();
//                showMessageDialog(BrokerageActivity.this);
                yongjintixian();
                break;
            case R.id.tablayout:
                break;
            case R.id.ll_one:
                intent = new Intent(this, ShouyiActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t1");
                } else {
                    intent.putExtra("type", "j1");
                }
                startActivity(intent);
                break;
            case R.id.ll_two:
                intent = new Intent(this, ShouyiActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t2");
                } else {
                    intent.putExtra("type", "j2");
                }
                startActivity(intent);
                break;
            case R.id.ll_three:
                intent = new Intent(this, ShouyiActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t3");
                } else {
                    intent.putExtra("type", "j3");
                }
                startActivity(intent);
                break;
            case R.id.ll_four:
                intent = new Intent(this, ShouyiActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t4");
                } else {
                    intent.putExtra("type", "j4");
                }
                startActivity(intent);
                break;
            case R.id.title_text1:
                intent = new Intent(this, UserShenSuActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_tixian_detail:
                intent = new Intent(this, TiXianDetailActivity.class);
                startActivity(intent);
                break;
            case R.id.shensu_jilu:
                intent = new Intent(this, ShesuRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 提现弹窗
     *
     * @param context
     * @param content
     * @param status
     */
    public void showTiXianMessageDialog(final Context context, String content, String status, String errmsg) {
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
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            TextView mOneMoney = updataDialog.findViewById(R.id.tv_one_money);

            /**
             * 提现金额小于一元提示
             */
            double money = Double.parseDouble(ketiMoney);

            if (money < 1) {
                mOneMoney.setVisibility(View.VISIBLE);
            }

            /**
             * 已经关注微信
             */
            if (NewConstants.isGuanzhuweixin != null && NewConstants.isGuanzhuweixin.equals("1")) {
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
                        tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                IWXAPI api = WXAPIFactory.createWXAPI(BrokerageActivity.this, Constants.APP_ID, false);
                                if (api.isWXAppInstalled()) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setComponent(cmp);
                                    startActivity(intent);
                                } else {
                                    StringUtil.showToast(BrokerageActivity.this, "微信未安装");
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
                        mTianxianFailed.setText("请立即联系客服申诉处理");
                        mTitle.setText("提现失败");
                        tv_update_gengxin.setText("联系客服");
                        tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                MainActivity.consultService(context);
                            }
                        });
                    }
                }
                /**
                 * 余额不足
                 */
                else {
                    String[] strings = errmsg.split("\\|");
                    mTitle.setText(strings[0]);
                    mTianxianFailed.setVisibility(View.VISIBLE);
                    mTianxianFailed.setText(strings[1]);
                    tv_update_gengxin.setText("查看赚钱攻略");
                    tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            String url = "http://www.51biaohuo.com/mobile/html/introduce.jsp?site=1";
                            Intent intent = new Intent(BrokerageActivity.this, WebViewActivity.class);
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
                mTitle.setText("提现攻略");
                mOneMoney.setVisibility(View.GONE);
                tv_update_gengxin.setText("立即去绑定");
                tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        IWXAPI api = WXAPIFactory.createWXAPI(BrokerageActivity.this, Constants.APP_ID, false);
                        if (api.isWXAppInstalled()) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);
                        } else {
                            StringUtil.showToast(BrokerageActivity.this, "微信未安装");
                        }
                        updataDialog.dismiss();
                    }
                });
            }

        }
    }

    @OnClick(R.id.ll_mingxi)
    public void onViewClicked() {
        Intent intent = new Intent(this, FanLiOrderActivity.class);
        startActivity(intent);
    }


    /**
     * 获取提现金额
     */
    public void yongjintixian() {
        String mid = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "mid");
        Map<String, String> params = new HashMap<>();
        params.put("mid", mid);
        RetrofitClient.getInstance(BrokerageActivity.this).createBaseApi()
                .yongjintixian(params, new BaseObserver<String>(BrokerageActivity.this) {
                    @Override
                    public void onNext(String s) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                showTiXianMessageDialog(BrokerageActivity.this
                                        , jsonObject.optString("content")
                                        , jsonObject.optString("status")
                                        , jsonObject.optString("errmsg"));
                            } else if (jsonObject.optString("status").equals("2")) {
                                showTiXianMessageDialog(BrokerageActivity.this
                                        , jsonObject.optString("content")
                                        , jsonObject.optString("status")
                                        , jsonObject.optString("errmsg"));
                            } else {
                                StringUtil.showToast(BrokerageActivity.this
                                        , jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(BrokerageActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(BrokerageActivity.this, e.message);
                    }
                });
    }
}
