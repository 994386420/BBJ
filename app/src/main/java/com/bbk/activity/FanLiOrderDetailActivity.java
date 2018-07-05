package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bbk.adapter.OrderDetailStatusAdapter;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 淘宝京东返利订单
 */
public class FanLiOrderDetailActivity extends BaseActivity {


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.topbar_layout)
    ConstraintLayout topbarLayout;
    @BindView(R.id.status_horizontallistView)
    GridView statusHorizontallistView;
    @BindView(R.id.tv_yongjin)
    TextView tvYongjin;
    @BindView(R.id.tv_yongjin_money)
    TextView tvYongjinMoney;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_maijia)
    TextView tvOrderMaijia;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    @BindView(R.id.order_detail_img)
    ImageView orderDetailImg;
    @BindView(R.id.order_title)
    TextView orderTitle;
    @BindView(R.id.ll_detail)
    LinearLayout llDetail;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_maijia)
    LinearLayout llMaijia;
    private String ddh = "", spid = "", domain = "", timeArr,imgurl = "",yj= "",yjmoney = "",money="",dianpu="",state= "",title = "",url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_layout);
        ButterKnife.bind(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        if (getIntent().getStringExtra("url") != null) {
            url = getIntent().getStringExtra("url");
        }
        if (getIntent().getStringExtra("ddh") != null) {
            ddh = getIntent().getStringExtra("ddh");
            tvOrderId.setText(ddh);
        }
        if (getIntent().getStringExtra("spid") != null) {
            spid = getIntent().getStringExtra("spid");
        }
        if (getIntent().getStringExtra("domain") != null) {
            domain = getIntent().getStringExtra("domain");
        }
        if (getIntent().getStringExtra("imgurl") != null) {
            imgurl = getIntent().getStringExtra("imgurl");
            Glide.with(FanLiOrderDetailActivity.this)
                    .load(imgurl)
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.zw_img_300)
                    .into(orderDetailImg);
        }
        if (getIntent().getStringExtra("yj") != null) {
            yj = getIntent().getStringExtra("yj");
            tvYongjin.setText(yj);
        }
        if (getIntent().getStringExtra("yjmoney") != null) {
            yjmoney = getIntent().getStringExtra("yjmoney");
            tvYongjinMoney.setText("¥"+yjmoney);
        }
        if (getIntent().getStringExtra("money") != null) {
            money = getIntent().getStringExtra("money");
            tvOrderPrice.setText(money + "元");
        }
        if (getIntent().getStringExtra("dianpu") != null) {
            dianpu = getIntent().getStringExtra("dianpu");
             tvOrderMaijia.setText(dianpu);
        } else {
        llMaijia.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("state") != null && getIntent().getStringExtra("timeArr") != null) {
            state = getIntent().getStringExtra("state");
            timeArr = getIntent().getStringExtra("timeArr");
            List<Object> timeList = new ArrayList<>();
            JSONArray timearr = null;
            try {
                timearr = new JSONArray(timeArr);
                for (int i = 0; i < timearr.length(); i++) {
                    String time = timearr.getString(i);
                    timeList.add(time);
                }
                statusHorizontallistView.setAdapter(new OrderDetailStatusAdapter(FanLiOrderDetailActivity.this,timeList,state));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (getIntent().getStringExtra("title") != null) {
            title = getIntent().getStringExtra("title");
            orderTitle.setText(title);
        }
        initView();
//        queryCpsOrderDetail();
//        refreshAndloda();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
    }

    private void initView() {
        titleText.setText("订单详情");
    }

    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
//                queryCpsOrderDetail();
            }
        });
    }

//    /**
//     * 查询CPS详情
//     */
//    private void queryCpsOrderDetail() {
//        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("userid", "662");
//        maps.put("ddh", ddh);
//        maps.put("spid", spid);
//        maps.put("domain", domain);
//        RetrofitClient.getInstance(this).createBaseApi().queryCpsOrderDetail(
//                maps, new BaseObserver<String>(this) {
//                    @Override
//                    public void onNext(String s) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(s);
//                            if (jsonObject.optString("status").equals("1")) {
//                                String content = jsonObject.optString("content");
//                                orderListDetailBean = JSON.parseObject(content, OrderListDetailBean.class);
//                                tvYongjin.setText(orderListDetailBean.getYj());
//                                tvYongjinMoney.setText("¥"+orderListDetailBean.getYjmoney());
//                                tvOrderId.setText(orderListDetailBean.getDdh());
//                                tvOrderPrice.setText(orderListDetailBean.getMoney() + "元");
//                                orderTitle.setText(orderListDetailBean.getTitle());
//                                if (orderListDetailBean.getDianpu() != null && !orderListDetailBean.getDianpu().equals("")) {
//                                    tvOrderMaijia.setText(orderListDetailBean.getDianpu());
//                                }else {
//                                    llMaijia.setVisibility(View.GONE);
//                                }
//                                Glide.with(FanLiOrderDetailActivity.this)
//                                        .load(orderListDetailBean.getImgurl())
//                                        .priority(Priority.HIGH)
//                                        .placeholder(R.mipmap.zw_img_300)
//                                        .into(orderDetailImg);
//                                statusHorizontallistView.setAdapter(new OrderDetailStatusAdapter(FanLiOrderDetailActivity.this,orderListDetailBean.getTimeArr(),orderListDetailBean.getState()));
//                            } else {
//                                StringUtil.showToast(FanLiOrderDetailActivity.this, jsonObject.optString("errmsg"));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    protected void hideDialog() {
//                        DialogSingleUtil.dismiss(0);
//                        refreshLayout.finishRefresh();
//                        refreshLayout.finishLoadMore();
//                    }
//
//                    @Override
//                    protected void showDialog() {
//                        DialogSingleUtil.show(FanLiOrderDetailActivity.this);
//                    }
//
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        DialogSingleUtil.dismiss(0);
//                        refreshLayout.finishRefresh();
//                        refreshLayout.finishLoadMore();
//                        StringUtil.showToast(FanLiOrderDetailActivity.this, e.message);
//                    }
//                });
//    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.title_back_btn, R.id.ll_detail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_detail:
                Intent intent = new Intent(this,IntentActivity.class);
                intent.putExtra("bprice",money);
                intent.putExtra("domain",domain);
                intent.putExtra("title",title);
                intent.putExtra("url",url);
                startActivity(intent);
                break;
        }
    }
}
