package com.bbk.shopcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.TuiKuanBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退款详情
 */
public class TuiKuanDeatailActivity extends BaseActivity {


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_wuliu_message)
    TextView tvWuliuMessage;
    @BindView(R.id.tv_wulitime)
    TextView tvWulitime;
    @BindView(R.id.ll_wuliu)
    LinearLayout llWuliu;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_usejinbi)
    TextView tvUsejinbi;
    @BindView(R.id.tv_shifukuan)
    TextView tvShifukuan;
    @BindView(R.id.ll_shifukuan)
    LinearLayout llShifukuan;
    @BindView(R.id.item_img)
    ImageView itemImg;
    @BindView(R.id.item_title)
    TextView itemTitle;
    @BindView(R.id.tv_shop_price)
    TextView tvShopPrice;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.ll_price1)
    LinearLayout llPrice1;
    @BindView(R.id.tv_shop_guige)
    TextView tvShopGuige;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_jianshu)
    TextView tvJianshu;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_bianhao)
    TextView tvBianhao;
    @BindView(R.id.ll_lx_maijia)
    LinearLayout llLxMaijia;
    @BindView(R.id.ll_bohao)
    LinearLayout llBohao;
    @BindView(R.id.topbar_layout)
    RelativeLayout topbarLayout;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.view5)
    View view5;
    @BindView(R.id.view6)
    View view6;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.tv_useyongjin)
    TextView tvUseyongjin;
    @BindView(R.id.ll_yongjin)
    LinearLayout llYongjin;
    @BindView(R.id.tv_usejingbi)
    TextView tvUsejingbi;
    @BindView(R.id.ll_jingbi)
    LinearLayout llJingbi;
    @BindView(R.id.tv_shouli)
    TextView tvShouli;
    @BindView(R.id.item_order)
    LinearLayout itemOrder;
    private String dianpuid, orderid, states;
    TuiKuanBean tuiKuanBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuikuan_detail_layout);
        ButterKnife.bind(this);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topbarLayout);
        initView();
        dianpuid = getIntent().getStringExtra("dianpuid");
        orderid = getIntent().getStringExtra("orderid");
        states = getIntent().getStringExtra("states");
        queryRefundProgress();
    }

    public void initView() {
        titleText.setText("退款详情");
    }


    private void queryRefundProgress() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("orderid", orderid);
        maps.put("dianpuid", dianpuid);
        RetrofitClient.getInstance(this).createBaseApi().queryRefundProgress(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                tuiKuanBean = JSON.parseObject(content, TuiKuanBean.class);
                                tvReason.setText("退款原因： " + tuiKuanBean.getReson());
                                tvMoney.setText("退款金额： " + tuiKuanBean.getTotal());
                                tvJianshu.setText("申请件事： " + tuiKuanBean.getNumber());
                                tvTime.setText("申请时间： " + tuiKuanBean.getSdate1());
                                tvBianhao.setText("退款编号： " + tuiKuanBean.getRefundnum());
                                itemTitle.setText(tuiKuanBean.getTitle());

                                Glide.with(TuiKuanDeatailActivity.this)
                                        .load(tuiKuanBean.getImgurl())
                                        .priority(Priority.HIGH)
                                        .placeholder(R.mipmap.zw_img_300)
                                        .into(itemImg);
                                tvShopGuige.setText(tuiKuanBean.getParam());
                                if (tuiKuanBean.getWxtotal().equals("0.00") || tuiKuanBean.getWxtotal().equals("0.0") || tuiKuanBean.getWxtotal().equals("0")) {
                                    llWeixin.setVisibility(View.GONE);
                                } else {
                                    llWeixin.setVisibility(View.VISIBLE);
                                    tvShouli.setText("微信受理");
                                    tvUsejinbi.setText("¥" + tuiKuanBean.getWxtotal());
                                }
                                tvTotalMoney.setText("¥" + tuiKuanBean.getTotal());
                                tvTime1.setText(tuiKuanBean.getSdate2());
                                tvTime2.setText(tuiKuanBean.getSdate2());
                                tvTime3.setText(tuiKuanBean.getSdate3());
                                if (tuiKuanBean.getUsejinbi().equals("0.00") || tuiKuanBean.getUsejinbi().equals("0.0") || tuiKuanBean.getUsejinbi().equals("0")) {
                                    llJingbi.setVisibility(View.GONE);
                                } else {
                                    llJingbi.setVisibility(View.VISIBLE);
                                    tvShouli.setText("商家受理");
                                    tvUsejingbi.setText("¥" + tuiKuanBean.getUsejinbi());
                                }
                                if (tuiKuanBean.getUseyongjin().equals("0.00") || tuiKuanBean.getUseyongjin().equals("0.0") || tuiKuanBean.getUseyongjin().equals("0")) {
                                    llYongjin.setVisibility(View.GONE);
                                } else {
                                    llYongjin.setVisibility(View.VISIBLE);
                                    tvShouli.setText("商家受理");
                                    tvUseyongjin.setText("¥" + tuiKuanBean.getUseyongjin());
                                }
                                if (states.equals("-1")) {
                                    tvStatus.setText("商家同意退款，退款处理中");
                                    view4.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
                                    view5.setBackgroundColor(getResources().getColor(R.color.__picker_common_primary));
                                    view6.setBackground(getResources().getDrawable(R.drawable.bg_cicyle6));
                                } else {
                                    tvStatus.setText("退款成功");
                                    view4.setBackgroundColor(getResources().getColor(R.color.tuiguang_color5));
                                    view5.setBackgroundColor(getResources().getColor(R.color.tuiguang_color5));
                                    view6.setBackground(getResources().getDrawable(R.drawable.bg_cicyle1));
                                }
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
                        DialogSingleUtil.show(TuiKuanDeatailActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(TuiKuanDeatailActivity.this, e.message);
                    }
                });
    }


    @OnClick({R.id.title_back_btn,R.id.item_order})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.item_order:
                if (tuiKuanBean != null) {
                    if (tuiKuanBean.getProductstate().equals("1")) {
                        intent = new Intent(this, ShopDetailActivty.class);
                        intent.putExtra("id", tuiKuanBean.getId());
                        startActivity(intent);
                        return;
                    }
                }
                StringUtil.showToast(this, "该商品已下架！");
                break;
        }
    }
}
