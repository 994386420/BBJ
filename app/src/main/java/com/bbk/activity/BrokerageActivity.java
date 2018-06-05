package com.bbk.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brokerage_layout);
        ButterKnife.bind(this);
        titleText.setText("收益报表");
        tablayout.addTab(tablayout.newTab().setText("淘宝收益"));
        tablayout.addTab(tablayout.newTab().setText("京东收益"));
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        StringUtil.setIndicator(tablayout,70,70);
    }

    /**
     * 成为合伙人
     */
    private void updateCooperationByUserid() {
        Map<String, String> maps = new HashMap<String, String>();
        RetrofitClient.getInstance(this).createBaseApi().updateCooperationByUserid(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                finish();
                            } else {
                                StringUtil.showToast(BrokerageActivity.this, jsonObject.optString("errmsg"));
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
                        StringUtil.showToast(BrokerageActivity.this, "网络异常");
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.title_back_btn, R.id.tv_tixian, R.id.tablayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                break;
            case R.id.tv_tixian:
                break;
            case R.id.tablayout:
                break;
        }
    }
}
