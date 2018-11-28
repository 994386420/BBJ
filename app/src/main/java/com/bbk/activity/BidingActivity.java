package com.bbk.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.RushBuyCountDownTimerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 接镖_03_填写
 */
public class BidingActivity extends BaseActivity implements ResultEvent {
    private DataFlow6 dataFlow;
    private String fbid;
    private ImageView item_img;
    private TextView item_title,mprice,mcount,mcommit,magrenment;
    private EditText mbidprice,mediturl,mdesc;
    private RushBuyCountDownTimerView mtime;
    private ImageView magrement;
    private boolean isagrement = true;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biding);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        fbid = getIntent().getStringExtra("fbid");
        initView();
        initData();
    }
    public void initView(){
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbidprice = (EditText)findViewById(R.id.mbidprice);
        SpannableString ss = new SpannableString("请输入价格");//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(30,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        magrement = (ImageView) findViewById(R.id.magrement);
//        magrement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isagrement){
//                    isagrement = false;
//                    magrement.setImageResource(R.mipmap.bj_09_01);
//                    mcommit.setBackgroundColor(Color.parseColor("#999999"));
//                    mcommit.setClickable(false);
//                }else {
//                    isagrement = true;
//                    magrement.setImageResource(R.mipmap.bj_09_02);
//                    mcommit.setBackgroundColor(Color.parseColor("#b40000"));
//                    mcommit.setClickable(true);
//                }
//            }
//        });
        mbidprice.setHint(new SpannedString(ss));
        item_title = (TextView)findViewById(R.id.item_title);
        mprice = (TextView)findViewById(R.id.mprice);
        mcount = (TextView)findViewById(R.id.mcount);
        mtime = (RushBuyCountDownTimerView) findViewById(R.id.mtime);
        mcommit = (TextView)findViewById(R.id.mcommit);
        magrenment = (TextView)findViewById(R.id.magrenment);
        mediturl = (EditText)findViewById(R.id.mediturl);
        mdesc = (EditText)findViewById(R.id.mdesc);
        item_img = (ImageView) findViewById(R.id.item_img);
        mbidprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String price = mbidprice.getText().toString();
                if (TextUtils.isEmpty(price)){
                    SpannableString ss = new SpannableString("请输入价格");//定义hint的值
                    AbsoluteSizeSpan ass = new AbsoluteSizeSpan(30,true);//设置字体大小 true表示单位是sp
                    ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mbidprice.setHint(new SpannedString(ss));
                }else {
                    mbidprice.setHint("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mcommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertJiebiao();
            }
        });
    }
    public void initData(){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        dataFlow.requestData(1, "bid/queryJieBiaoMsg", paramsMap, this,true);
    }
    public void insertJiebiao(){
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
        String openID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "openID");
//        Log.i("发镖信息openid",openID+"==========");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fbid",fbid);
        paramsMap.put("userid", userID);
        paramsMap.put("price",mbidprice.getText().toString());
        paramsMap.put("desc",mdesc.getText().toString());
        paramsMap.put("url",mediturl.getText().toString());
        paramsMap.put("openid",openID);
        dataFlow.requestData(2, "bid/insertJiebiao", paramsMap, this,true);
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
            switch (requestCode){
                case 1:
                    try {
                    JSONObject object = new JSONObject(content);
                    String endtime = object.optString("endtime");
                    String title = object.optString("title");
                    String price = object.optString("price");
                    String extra = object.optString("extra");
                    String img = object.optString("img");
                    String userid = object.optString("userid");
                    String number = object.optString("number");
                    mtime.addsum(endtime,"#999999");
                    mtime.start();
                    item_title.setText(title);
                    mcount.setText("x"+number);
                    mprice.setText("￥"+price);
                    Glide.with(this)
                            .load(img)
                            .priority(Priority.HIGH)
                            .placeholder(R.mipmap.zw_img_300)
                            .into(item_img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    try {
                    if(dataJo.optInt("status")>0){
                        Intent intent = new Intent(BidingActivity.this,BidMyListDetailActivity.class);
                        startActivity(intent);
                    }
                   } catch (Exception e) {
                   e.printStackTrace();
                   }
                    break;
                default:
                    break;
            }
    }
}
