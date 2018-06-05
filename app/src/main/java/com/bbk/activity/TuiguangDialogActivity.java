package com.bbk.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
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
        mclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                finish();
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
                        StringUtil.showToast(TuiguangDialogActivity.this, "网络异常");
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
