package com.bbk.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bbk.adapter.Adapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ShareFenXiangUtil;
import com.bbk.util.ShareHaiBaoUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YaoqingFriendsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.tv_share)
    TextView tvShare;
    private List<String> imgUrlList = new ArrayList<>();
    private Adapter adapter;
    private String wenan;
    private ShareHaiBaoUtil shareHaiBaoUtil;
    int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yaoqing_friends_layout);
        View topView = findViewById(R.id.activity_main);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        titleText.setText("邀请好友");
        pager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                page.setScaleY(1f - ((float) (0.2 * Math.abs(position))));
                page.setScaleX(1f - ((float) (0.2 * Math.abs(position))));
            }
        });
        pager.addOnPageChangeListener(this);
        newInvitedFriend();
    }


    /**
     * 分享海报
     */
    private void newInvitedFriend() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().newInvitedFriend(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Log.i("======", s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                JSONObject jsonObject1 = new JSONObject(content);
                                wenan = jsonObject1.optString("wenan").replace("|","\n");
                                JSONArray detailImags = new JSONArray(jsonObject1.optString("imgs"));
                                for (int i = 0; i < detailImags.length(); i++) {
                                    String imgUrl = detailImags.getString(i);
                                    imgUrlList.add(imgUrl);
                                }
//                                imgUrlList.add(0,detailImags.getString(2));
//                                imgUrlList.add(4,detailImags.getString(0));
//                                Log.i("=========",imgUrlList+"----------");
                                if (imgUrlList != null && imgUrlList.size() > 0) {
                                    adapter = new Adapter(getSupportFragmentManager(), imgUrlList, YaoqingFriendsActivity.this);
                                    pager.setAdapter(adapter);
                                    pager.setCurrentItem(0);
                                }
                            } else {
                                StringUtil.showToast(YaoqingFriendsActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(YaoqingFriendsActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(YaoqingFriendsActivity.this, e.message);
                    }
                });
    }

    @OnClick({R.id.title_back_btn, R.id.tv_copy, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.tv_copy:
                ClipboardManager cm = (ClipboardManager)YaoqingFriendsActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(wenan);
                StringUtil.showToast(YaoqingFriendsActivity.this,"复制成功");
                break;
            case R.id.tv_share:
                DialogSingleUtil.show(YaoqingFriendsActivity.this);
                List<String> UrlList = new ArrayList<>();
                UrlList.add(imgUrlList.get(pageIndex).toString());
                if (UrlList != null && UrlList.size() > 0) {
                    //调用转发微信功能类
                    shareHaiBaoUtil = new ShareHaiBaoUtil(YaoqingFriendsActivity.this, tvShare, "", UrlList);
                    DialogSingleUtil.dismiss(100);
                }else {
                    DialogSingleUtil.dismiss(100);
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("当前位置",position+"======");
        pageIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}

















