package com.bbk.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.AlertDialog;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokerage_layout);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        refreshLayout.setEnableLoadMore(false);
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
                                tvMoney1.setText(brokerageBean.getOne());
                                tvMoney2.setText(brokerageBean.getTwo());
                                tvMoney3.setText(brokerageBean.getThree());
                                tvMoney4.setText(brokerageBean.getFour());
                                tvFukuangNumber.setText(brokerageBean.getPayCount());
                                if (brokerageBean.getYesYongjinSum() != null && !brokerageBean.getYesYongjinSum().equals("")) {
                                    tvYongjin.setText("¥ "+brokerageBean.getYesYongjinSum());
                                } else {
                                    tvYongjin.setText("¥ 0.0");
                                }

                                if (brokerageBean.getJinYongjinSum() != null && !brokerageBean.getJinYongjinSum().equals("")) {
                                    tvYongjinJinri.setText("¥ "+brokerageBean.getJinYongjinSum());
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

    @OnClick({R.id.title_back_btn, R.id.tv_tixian, R.id.tablayout, R.id.ll_one, R.id.ll_two, R.id.ll_three, R.id.ll_four})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.tv_tixian:
                new AlertDialog(this).builder().setTitle("提现攻略")
                        .setMsg("第1步：搜索并关注“比比鲸大数据”公众号")
                        .setMsg2("第2步：发送“佣金提现”")
                        .setMsg3("第3步：绑定账号，验证身份")
                        .setMsg4("第4步：领取红包")
                        .setLeft()
                        .setPositiveButton("OK,去微信提现", new View.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View v) {

                            }
                        }).setPositiveButtonColor("#ffffff").setPositiveBackgroundColor("#ff7d41").show();
                break;
            case R.id.tablayout:
                break;
            case R.id.ll_one:
                intent = new Intent(this, BrokerageDetailActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t1");
                } else {
                    intent.putExtra("type", "j1");
                }
                startActivity(intent);
                break;
            case R.id.ll_two:
                intent = new Intent(this, BrokerageDetailActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t2");
                } else {
                    intent.putExtra("type", "j2");
                }
                startActivity(intent);
                break;
            case R.id.ll_three:
                intent = new Intent(this, BrokerageDetailActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t3");
                } else {
                    intent.putExtra("type", "j3");
                }
                startActivity(intent);
                break;
            case R.id.ll_four:
                intent = new Intent(this, BrokerageDetailActivity.class);
                if (type.equals("1")) {
                    intent.putExtra("type", "t4");
                } else {
                    intent.putExtra("type", "j4");
                }
                startActivity(intent);
                break;
        }
    }
}
