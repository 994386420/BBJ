package com.bbk.shopcar;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.WuliuBean;
import com.bbk.Bean.WuliuItemBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.adapter.WuliuAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 物流
 */
public class WuLiuActivity extends BaseActivity {
    String expressage;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_dh)
    TextView tvDh;
    @BindView(R.id.tv_gs)
    TextView tvGs;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.listview_wuliu)
    RecyclerView listviewWuliu;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_kefu)
    TextView tvKefu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wuliu_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initVeiw();
        if (getIntent().getStringExtra("expressnum") != null) {
            expressage = getIntent().getStringExtra("expressnum");
            queryMyLogistics(expressage);
        }
    }

    private void initVeiw() {
        titleText.setText("订单追踪");
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (expressage != null) {
                    queryMyLogistics(expressage);
                }
            }
        });
    }

    /**
     * 查询物流
     */
    private void queryMyLogistics(String expressnum) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("expressnum", expressnum);
        RetrofitClient.getInstance(this).createBaseApi().queryMyLogistics(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                WuliuBean wuliuBean = JSON.parseObject(content,WuliuBean.class);
                                tvDh.setText("运单号："+ wuliuBean.getExpressNum());
                                tvGs.setText("承运公司："+ wuliuBean.getCompany());
                                tvPhone.setText("承运电话："+ wuliuBean.getPhone());
                                if (wuliuBean.getList() != null) {
                                    List<WuliuItemBean> wuliuItemBeans = JSON.parseArray(wuliuBean.getList(), WuliuItemBean.class);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(WuLiuActivity.this, OrientationHelper.VERTICAL, false);
                                    WuliuAdapter mAdapter = new WuliuAdapter(WuLiuActivity.this, wuliuItemBeans);
                                    listviewWuliu.setLayoutManager(layoutManager);
                                    listviewWuliu.setAdapter(mAdapter);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(WuLiuActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(WuLiuActivity.this, e.message);
                    }
                });
    }

    @OnClick({R.id.title_back_btn, R.id.tv_kefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.tv_kefu:
                break;
        }
    }
}
