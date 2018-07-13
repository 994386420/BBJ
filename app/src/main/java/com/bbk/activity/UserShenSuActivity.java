package com.bbk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.BrokerageBean;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.SystemUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserShenSuActivity extends BaseActivity {

    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.medit)
    EditText medit;
    @BindView(R.id.msend)
    TextView msend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shensu_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout_shensu);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
    }

    private void initView() {
        titleText.setText("我要申诉");
    }

    /**
     *
     */
    @OnClick({R.id.title_back_btn, R.id.msend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.msend:
                if (medit.getText().toString() != null && !medit.getText().toString().equals("")){
                    /**
                     * 手机系统版本号，手机型号， 手机厂商
                     */
                    String string =  SystemUtil.getDeviceBrand()+" "+SystemUtil.getSystemModel()+" android:"+SystemUtil.getSystemVersion();
                    insertCpsOrderCheck(medit.getText().toString());
                }else {
                    StringUtil.showToast(UserShenSuActivity.this, "请输入你要申诉的订单号");
                }
                break;
        }
    }

    private void insertCpsOrderCheck(String ddh) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("ddh", ddh);
        RetrofitClient.getInstance(this).createBaseApi().insertCpsOrderCheck(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(UserShenSuActivity.this, "申诉成功");
                                medit.setText("");
                                finish();
                            } else {
                                StringUtil.showToast(UserShenSuActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(UserShenSuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(UserShenSuActivity.this, e.message);
                    }
                });
    }

}
