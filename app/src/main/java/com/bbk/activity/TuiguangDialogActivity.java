package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.fragment.DataFragment;
import com.bbk.resource.NewConstants;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.TencentLoginUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TuiguangDialogActivity extends BaseActivity {
    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.imageView1)
    RoundedImageView imageView1;
    @BindView(R.id.tv_hehuo_person)
    TextView tvHehuoPerson;
    @BindView(R.id.mclose)
    ImageView mclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuiguang_dialog);
        ButterKnife.bind(this);
        if (NewConstants.logFlag != null) {
            if (NewConstants.logFlag.equals("1") || NewConstants.logFlag.equals("2")) {
                tvHehuoPerson.setVisibility(View.GONE);
            }
        }
        mclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (NewConstants.logFlag != null) {
                    if (NewConstants.logFlag.equals("1")|| NewConstants.logFlag.equals("2")) {
                        intent = new Intent(TuiguangDialogActivity.this, HomeActivity.class);
//								setResult(3, intent);
                        intent.putExtra("type", "4");
                        if (DataFragmentActivity.login_remind != null) {
                            DataFragmentActivity.login_remind.setVisibility(View.GONE);
                        }
                        startActivity(intent);
                        finish();
                    }else {
                        finish();
                    }
                }
            }
        });
        tvHehuoPerson.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCooperationByUserid();
            }
        });
    }

    /**
     * 成为合伙人
     */
    private void updateCooperationByUserid() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().updateCooperationByUserid(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            Intent intent;
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                if (NewConstants.logFlag != null) {
                                    if (NewConstants.logFlag.equals("1")|| NewConstants.logFlag.equals("2")) {
                                        intent = new Intent(TuiguangDialogActivity.this, HomeActivity.class);
                                        intent.putExtra("type", "4");
                                        if (DataFragmentActivity.login_remind != null) {
                                            DataFragmentActivity.login_remind.setVisibility(View.GONE);
                                        }
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        //推广规则跳转链接
                                        String url = BaseApiService.Base_URL + "mobile/user/generalize";
                                        intent = new Intent(TuiguangDialogActivity.this, WebViewActivity.class);
                                        intent.putExtra("url", url);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                                StringUtil.showToast(TuiguangDialogActivity.this,"恭喜成为合伙人");
                            } else {
                                StringUtil.showToast(TuiguangDialogActivity.this, jsonObject.optString("errmsg"));
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
                        StringUtil.showToast(TuiguangDialogActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
