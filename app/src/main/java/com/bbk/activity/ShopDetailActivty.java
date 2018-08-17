package com.bbk.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.interfaces.KFCallBack;
import com.bbk.Bean.CanshuBean;
import com.bbk.Bean.PlBean;
import com.bbk.Bean.ShopDetailBean;
import com.bbk.Bean.ShopTuijianBean;
import com.bbk.adapter.DetailImageAdapter;
import com.bbk.adapter.ShopCanshuAdapter;
import com.bbk.adapter.TuijianGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.ConfirmOrderActivity;
import com.bbk.shopcar.DianpuHomeActivity;
import com.bbk.shopcar.MyPlActivity;
import com.bbk.shopcar.NewDianpuActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.shopcar.view.IdeaScrollView;
import com.bbk.util.DensityUtils;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageGuanggaoLoader;
import com.bbk.util.GlideImageLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CircleImageView1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/06/006.
 */

public class ShopDetailActivty extends BaseActivity {
    @BindView(R.id.one)
    LinearLayout one;
    @BindView(R.id.two)
    LinearLayout two;
    @BindView(R.id.three)
    LinearLayout three;
    @BindView(R.id.four)
    LinearLayout four;
    @BindView(R.id.ideaScrollView)
    IdeaScrollView ideaScrollView;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.layer)
    View layer;
    @BindView(R.id.headerParent)
    LinearLayout headerParent;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.detail_image_list)
    RecyclerView detailImageList;
    @BindView(R.id.shop_price)
    TextView shopPrice;
    @BindView(R.id.shop_bprice)
    TextView shopBprice;
    @BindView(R.id.shop_title)
    TextView shopTitle;
    @BindView(R.id.shop_kuaidi)
    TextView shopKuaidi;
    @BindView(R.id.shop_sale)
    TextView shopSale;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.guess_like_list)
    RecyclerView guessLikeList;
    @BindView(R.id.ll_add_car)
    LinearLayout llAddCar;
    @BindView(R.id.ll_buy_now)
    LinearLayout llBuyNow;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_tuijian)
    TextView tvTuijian;
    @BindView(R.id.ll_fuwu)
    LinearLayout llFuwu;
    @BindView(R.id.ll_shuxing)
    LinearLayout llShuxing;
    @BindView(R.id.ll_canshu)
    LinearLayout llCanshu;
    @BindView(R.id.dianpu)
    TextView dianpu;
    @BindView(R.id.mimg)
    ImageView mimg;
    @BindView(R.id.mname)
    TextView mname;
    @BindView(R.id.mcontent)
    TextView mcontent;
    @BindView(R.id.dianpu_img)
    ImageView dianpuImg;
    @BindView(R.id.tv_all_mall)
    TextView tvAllMall;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.ll_dianpu)
    LinearLayout llDianpu;
    @BindView(R.id.img_car)
    ImageView imgCar;
    @BindView(R.id.ll_car)
    LinearLayout llCar;
    @BindView(R.id.img_car_black)
    ImageView imgCarBlack;
    @BindView(R.id.layout)
    FrameLayout layout;
    @BindView(R.id.ll_pl)
    LinearLayout llPl;
    @BindView(R.id.ll_pj)
    LinearLayout llPj;
    @BindView(R.id.ll_kefu)
    LinearLayout llKefu;
    private String id;
    private ShopDetailBean shopDetailBean;
    private ShopDialog shopDialog;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private float currentPercentage = 0;
    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked() ? getRadioCheckedAlphaColor(currentPercentage) : getRadioAlphaColor(currentPercentage));
                if (radioButton.isChecked() && isNeedScrollTo) {
                    ideaScrollView.setPosition(i);
                }
            }
        }
    };
    private boolean isNeedScrollTo = true;
    private String mKefuDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, null);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
            queryProductDetailById();
        }
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                queryProductDetailById();
            }
        });
        backImage.setVisibility(View.VISIBLE);
        llCar.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        detailImageList.setHasFixedSize(true);
        detailImageList.setLayoutManager(linearLayoutManager);


        Rect rectangle = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
        ideaScrollView.setViewPager(banner, getMeasureHeight(headerParent) - rectangle.top);
        icon.setImageAlpha(0);
        icon.setVisibility(View.GONE);
        radioGroup.setAlpha(0);
        radioGroup.check(radioGroup.getChildAt(0).getId());

        View one = findViewById(R.id.one);
        View two = findViewById(R.id.two);
        View four = findViewById(R.id.four);
        View three = findViewById(R.id.three);
        ArrayList<Integer> araryDistance = new ArrayList<>();

        araryDistance.add(0);
        araryDistance.add(getMeasureHeight(one) - getMeasureHeight(headerParent));
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) - getMeasureHeight(headerParent));
        araryDistance.add(getMeasureHeight(one) + getMeasureHeight(two) + getMeasureHeight(three) + getMeasureHeight(one) - getMeasureHeight(headerParent));

        ideaScrollView.setArrayDistance(araryDistance);

        ideaScrollView.setOnScrollChangedColorListener(new IdeaScrollView.OnScrollChangedColorListener() {
            @Override
            public void onChanged(float percentage) {
                icon.setVisibility(View.VISIBLE);
                int color = getAlphaColor(percentage > 0.9f ? 1.0f : percentage);
                float alpha = (255 * percentage);
                header.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
//                radioGroup.setBackgroundDrawable(new ColorDrawable(color));
                icon.setImageAlpha((int) ((percentage > 0.9f ? 1.0f : percentage) * 255));
                radioGroup.setAlpha((percentage > 0.9f ? 1.0f : percentage) * 255);
                radioGroup.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                setRadioButtonTextColor(percentage);
                if (percentage == 0.0) {
                    backImage.setVisibility(View.VISIBLE);
                    titleBackBtn.setVisibility(View.GONE);
                } else if (percentage > 0.0 && percentage < 0.5) {
                    backImage.setVisibility(View.VISIBLE);
                    titleBackBtn.setVisibility(View.GONE);
                } else {
                    backImage.setVisibility(View.GONE);
                    titleBackBtn.setVisibility(View.VISIBLE);
                }
                if (percentage == 0.0) {
                    llCar.setVisibility(View.VISIBLE);
                    imgCarBlack.setVisibility(View.GONE);
                } else if (percentage > 0.0 && percentage < 0.5) {
                    llCar.setVisibility(View.VISIBLE);
                    imgCarBlack.setVisibility(View.GONE);
                } else {
                    llCar.setVisibility(View.GONE);
                    imgCarBlack.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChangedFirstColor(float percentage) {

            }

            @Override
            public void onChangedSecondColor(float percentage) {

            }
        });

        ideaScrollView.setOnSelectedIndicateChangedListener(new IdeaScrollView.OnSelectedIndicateChangedListener() {
            @Override
            public void onSelectedChanged(int position) {
                isNeedScrollTo = false;
                radioGroup.check(radioGroup.getChildAt(position).getId());
                isNeedScrollTo = true;
            }
        });

        radioGroup.setOnCheckedChangeListener(radioGroupListener);
    }


    /**
     * 商品详情
     */
    private void queryProductDetailById() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", id);
        RetrofitClient.getInstance(this).createBaseApi().queryProductDetailById(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                shopDetailBean = JSON.parseObject(jsonObject.optString("content"), ShopDetailBean.class);
                                shopAddress.setText(shopDetailBean.getFahuodi());
                                if (shopDetailBean != null) {
                                    Glide.with(ShopDetailActivty.this)
                                            .load(shopDetailBean.getImgurl())
                                            .priority(Priority.HIGH)
                                            .placeholder(R.mipmap.zw_img_300)
                                            .into(icon);
                                    Glide.with(ShopDetailActivty.this)
                                            .load(shopDetailBean.getDianpulogo())
                                            .priority(Priority.HIGH)
                                            .placeholder(R.mipmap.zw_img_300)
                                            .into(dianpuImg);
                                    List<Object> imgUrlList = new ArrayList<>();
                                    if (shopDetailBean.getImgs() != null) {
                                        JSONArray imgs = new JSONArray(shopDetailBean.getImgs());
                                        for (int i = 0; i < imgs.length(); i++) {
                                            String imgUrl = imgs.getString(i);
                                            imgUrlList.add(imgUrl);
                                        }
                                        banner.setImages(imgUrlList)
                                                .setImageLoader(new GlideImageGuanggaoLoader())
                                                .setOnBannerListener(new OnBannerListener() {
                                                    @Override
                                                    public void OnBannerClick(int position) {

                                                    }
                                                })
                                                .setDelayTime(3000)
                                                .setBannerStyle(BannerConfig.NUM_INDICATOR)
                                                .setIndicatorGravity(BannerConfig.RIGHT)
                                                .start();
                                    }
                                    if (shopDetailBean.getDetailImgs() != null) {
                                        List<Object> DetailimgUrlList = new ArrayList<>();
                                        JSONArray detailImags = new JSONArray(shopDetailBean.getDetailImgs());
                                        for (int i = 0; i < detailImags.length(); i++) {
                                            String imgUrl = detailImags.getString(i);
                                            DetailimgUrlList.add(imgUrl);
                                        }
                                        if (DetailimgUrlList.size() > 0) {
                                            detailImageList.setAdapter(new DetailImageAdapter(ShopDetailActivty.this, DetailimgUrlList));
                                        }
                                    }
                                    if (shopDetailBean.getSale() != null) {
                                        shopSale.setText("月销" + shopDetailBean.getSale() + "笔");
                                    }
                                    if (shopDetailBean.getTitle() != null) {
                                        shopTitle.setText(shopDetailBean.getTitle());
                                    }
                                    if (shopDetailBean.getPrice() != null) {
                                        shopPrice.setText(shopDetailBean.getPrice());
                                    }
                                    if (shopDetailBean.getBprice() != null) {
                                        shopBprice.setText(shopDetailBean.getBprice());
                                    }
                                    if (shopDetailBean.getDianpu() != null) {
                                        dianpu.setText(shopDetailBean.getDianpu());
                                    }
                                    shopBprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                                    if (shopDetailBean.getPl() != null) {
                                        llPj.setVisibility(View.VISIBLE);
                                        PlBean plBean = JSON.parseObject(shopDetailBean.getPl(), PlBean.class);
                                        CircleImageView1.getImg(ShopDetailActivty.this, plBean.getImg(), mimg);
                                        mname.setText(plBean.getName());
                                        mcontent.setText(plBean.getContent());
                                    } else {
                                        llPj.setVisibility(View.GONE);
                                    }
                                    if (shopDetailBean.getTuijianlist() != null) {
                                        List<ShopTuijianBean> shopTuijianBean = JSON.parseArray(shopDetailBean.getTuijianlist(), ShopTuijianBean.class);
                                        //禁用滑动事件
                                        guessLikeList.setNestedScrollingEnabled(false);
                                        guessLikeList.setLayoutManager(new GridLayoutManager(ShopDetailActivty.this, 2));
                                        guessLikeList.setAdapter(new TuijianGridAdapter(ShopDetailActivty.this, shopTuijianBean));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        refresh.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ShopDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(ShopDetailActivty.this, e.message);
                    }
                });
    }

    public void setRadioButtonTextColor(float percentage) {
        if (Math.abs(percentage - currentPercentage) >= 0.1f) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked() ? getRadioCheckedAlphaColor(percentage) : getRadioAlphaColor(percentage));
            }
            this.currentPercentage = percentage;
        }
    }

    public int getMeasureHeight(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }

    public int getAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x09, 0xc1, 0xf4);
    }

    public int getLayerAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x09, 0xc1, 0xf4);
    }

    public int getRadioCheckedAlphaColor(float f) {
        return Color.argb((int) (f * 255), 253, 125, 20);
    }

    public int getRadioAlphaColor(float f) {
        return Color.argb((int) (f * 255), 0x44, 0x44, 0x44);
//        return Color.argb((int) (f*255),0xFF,0xFF,0xFF);
    }

    @Override
    protected void onDestroy() {
        if (ShopDetailActivty.this != null && ShopDetailActivty.this.isFinishing()) {
            DialogSingleUtil.dismiss(0);
        }
        super.onDestroy();
    }

    @OnClick({R.id.title_back_btn, R.id.ll_add_car, R.id.ll_buy_now, R.id.ll_fuwu, R.id.ll_shuxing, R.id.ll_canshu, R.id.tv_dianpu, R.id.back_image, R.id.tv_all_mall, R.id.ll_dianpu, R.id.ll_car, R.id.ll_pl,R.id.ll_kefu})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_add_car:
                doShoppingCart(shopDetailBean.getId(), "1", "1", "111");
                break;
            case R.id.ll_buy_now:
                intent = new Intent(ShopDetailActivty.this, ConfirmOrderActivity.class);
                intent.putExtra("ids", shopDetailBean.getId());
                intent.putExtra("nums", "1");
                intent.putExtra("guiges", "11111");
                startActivity(intent);
                break;
            case R.id.ll_fuwu:
                showBaozhangeDialog(this);
                break;
            case R.id.ll_shuxing:
                break;
            case R.id.ll_canshu:
                showMessageDialog(this);
                break;
            case R.id.tv_all_mall:
                intent = new Intent(this, NewDianpuHomeActivity.class);
                if (shopDetailBean != null) {
                    if (shopDetailBean.getDianpuid() != null) {
                        intent.putExtra("dianpuid", shopDetailBean.getDianpuid());
                    }
                }
                startActivity(intent);
                break;
            case R.id.tv_dianpu:
                intent = new Intent(this, NewDianpuActivity.class);
                if (shopDetailBean != null) {
                    if (shopDetailBean.getDianpuid() != null) {
                        intent.putExtra("dianpuid", shopDetailBean.getDianpuid());
                    }
                }
                startActivity(intent);
                break;
            case R.id.back_image:
                finish();
                break;
            case R.id.ll_dianpu:
                intent = new Intent(this, NewDianpuActivity.class);
                if (shopDetailBean != null) {
                    if (shopDetailBean.getDianpuid() != null) {
                        intent.putExtra("dianpuid", shopDetailBean.getDianpuid());
                    }
                }
                startActivity(intent);
                break;
            case R.id.ll_car:
                intent = new Intent(this, CarActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_pl:
                intent = new Intent(this, MyPlActivity.class);
                intent.putExtra("id", id);
                if (shopDetailBean.getImgurl() != null) {
                    intent.putExtra("img", shopDetailBean.getImgurl());
                }
                startActivity(intent);
                break;
            case R.id.ll_kefu:
                startECChat();
                break;
        }
    }


    /**
     * 购物车操作
     * type
     * 3表示增加数量
     * 4表示减少数量
     * -1表示删除物品
     * 1表示添加物品
     */
    private void doShoppingCart(String productid, String type, String number, String guige) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("productid", productid);
        maps.put("type", type);
        maps.put("number", number);
        maps.put("guige", guige);
        RetrofitClient.getInstance(ShopDetailActivty.this).createBaseApi().doShoppingCart(
                maps, new BaseObserver<String>(ShopDetailActivty.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                NewConstants.refeshFlag = "1";
//                                addCart(llAddCar);
                                StringUtil.showToast(ShopDetailActivty.this, "添加成功");
                            } else {
                                StringUtil.showToast(ShopDetailActivty.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(ShopDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ShopDetailActivty.this, e.message);
                    }
                });
    }


    /**
     * 商品规格参数
     *
     * @param context
     */
    public void showMessageDialog(final Context context) {
        if (shopDialog == null || !shopDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            shopDialog = new ShopDialog(context, R.layout.shop_dialog_layout,
                    new int[]{R.id.tv_ok});
            shopDialog.show();
            shopDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = shopDialog.findViewById(R.id.tv_ok);
            TextView tv_title = shopDialog.findViewById(R.id.tv_title);
            tv_title.setText("商品参数");
            RecyclerView recyclerView = shopDialog.findViewById(R.id.recyclerview_shop_dialog);
            LinearLayout linearLayout = shopDialog.findViewById(R.id.ll_baozhang);
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (shopDetailBean != null) {
                if (shopDetailBean.getProperty() != null) {
                    List<CanshuBean> canshuBean = JSON.parseArray(shopDetailBean.getProperty(), CanshuBean.class);
                    recyclerView.setAdapter(new ShopCanshuAdapter(context, canshuBean));
                }
            }
            ImageView img_close = shopDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
        }
    }

    /**
     * 商品基础保障
     *
     * @param context
     */
    public void showBaozhangeDialog(final Context context) {
        if (shopDialog == null || !shopDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            shopDialog = new ShopDialog(context, R.layout.shop_dialog_layout,
                    new int[]{R.id.tv_ok});
            shopDialog.show();
            shopDialog.setCanceledOnTouchOutside(true);
            TextView tv_update_gengxin = shopDialog.findViewById(R.id.tv_ok);
            TextView tv_title = shopDialog.findViewById(R.id.tv_title);
            tv_title.setText("基础保障");
            RecyclerView recyclerView = shopDialog.findViewById(R.id.recyclerview_shop_dialog);
            LinearLayout linearLayout = shopDialog.findViewById(R.id.ll_baozhang);
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ImageView img_close = shopDialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
            tv_update_gengxin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.img_car_black)
    public void onViewClicked() {
        Intent intent = new Intent(this, CarActivity.class);
        startActivity(intent);
    }


    private void addCart(LinearLayout iv) {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(this);
//        goods.setImageDrawable(iv.getDrawable());
        goods.setImageDrawable(getResources().getDrawable(R.mipmap.loading_01));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtils.dp2px(getApplicationContext(), 20), DensityUtils.dp2px(getApplicationContext(), 20));
        layout.addView(goods, params);
//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        layout.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
//        mBottomCarImage.getLocationInWindow(endLoc);
//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2 - DensityUtils.dp2px(getApplicationContext(), 50);

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
//        float toX = endLoc[0] - parentLocation[0] + mBottomCarImage.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1] - DensityUtils.dp2px(getApplicationContext(), 50);

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
//        path.quadTo(startX, startY-DensityUtils.dp2px(getApplicationContext(),100), toX+DensityUtils.dp2px(getApplicationContext(),25), toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);
//        float cx = mBottomCarImage.getX();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(700);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();
//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                layout.removeView(goods);
                DialogSingleUtil.dismiss(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    // 电商专用咨询页面
    private void startECChat() {
        //"<img border=\\\"0\" src=\"" + shopDetailBean.getImgurl() + "\" />   <p>商品名称：" + shopDetailBean.getTitle() + "</p> <p>\\"+shopDetailBean.getPrice()+"</p> "
        mKefuDescription = "<img border='0' src='" + shopDetailBean.getImgurl() + "' /> <p>商品名称：" +  shopDetailBean.getTitle() + " </p>   <p>商品价格："+shopDetailBean.getPrice()+"</p> ";
        KFAPIs.startECChat(this,
                "bbjkfxz",//1. 客服工作组名称(请务必保证大小写一致)，请在管理后台分配
                "比比鲸客服",//2. 会话界面标题，可自定义
                mKefuDescription,//3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
                //   如果不想发送此信息，可以将此信息设置为""或者null
                true,//4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
                //	请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
                //	显示:true, 不显示:false
                5,//5. 默认显示消息数量
                "http://www.bibijing.com/images/zhanwei/logo.png",//6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
                NewConstants.imgurl, //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
                false,                    //8. 默认机器人应答
                true,                    //9. 是否显示商品详情，显示：true；不显示：false
                shopDetailBean.getImgurl(),//10.商品详情图片
                shopDetailBean.getTitle(),                    //11.商品详情简介
                shopDetailBean.getPrice(),                                            //12.商品详情价格
                "http://www.bibijing.com/images/zhanwei/logo.png",                            //13.商品网址链接
                "goodsCallbackId",                                //14.点击商品详情布局回调参数
                false,                                            //15.退出对话的时候是否强制评价，强制：true，不评价：false
                new KFCallBack() {        //15. 会话页面右上角回调函数

                    /**
                     * 16.是否使用对话界面右上角默认动作. 使用默认动作返回：true, 否则返回false
                     */
                    @Override
                    public Boolean useTopRightBtnDefaultAction() {

                        return true;
                    }

                    /**
                     * 17.点击对话界面右上角按钮动作，依赖于 上面一个函数的返回结果
                     */
                    @Override
                    public void OnChatActivityTopRightButtonClicked() {
                        // TODO Auto-generated method stub
                        Log.d("KFMainActivity", "右上角回调接口调用");

                    }

                    /**
                     * 18.点击商品详情图片回调函数
                     */
                    @Override
                    public void OnECGoodsImageViewClicked(String imageViewURL) {
                        // TODO Auto-generated method stub

                        Log.d("KFMainActivity", "OnECGoodsImageViewClicked" + imageViewURL);

                    }

                    /**
                     * 19.点击商品详情简介回调函数
                     */
                    @Override
                    public void OnECGoodsTitleDetailClicked(String titleDetailString) {
                        // TODO Auto-generated method stub
                        Log.d("KFMainActivity", "OnECGoodsIntroductionClicked" + titleDetailString);

                    }

                    /**
                     * 20.点击商品详情价格回调函数
                     */
                    @Override
                    public void OnECGoodsPriceClicked(String priceString) {
                        // TODO Auto-generated method stub
                        Log.d("KFMainActivity", "OnECGoodsPriceClicked" + priceString);

                    }

                    /**
                     * 21.点击商品详情布局回调函数
                     */
                    @Override
                    public void OnEcGoodsInfoClicked(String callbackId) {
                        // TODO Auto-generated method stub
                        Log.d("KFMainActivity", "OnEcGoodsInfoClicked" + callbackId);

                    }

                    /**
                     * 用户点击会话页面下方“常见问题”按钮时，是否使用自定义action，如果返回true,
                     * 则默认action将不起作用，会调用下方OnFaqButtonClicked函数
                     */
                    public Boolean userSelfFaqAction() {
                        return false;
                    }

                    /**
                     * 用户点击“常见问题”按钮时，自定义action回调函数接口
                     */
                    @Override
                    public void OnFaqButtonClicked() {
                        Log.d("KFMainActivity", "OnFaqButtonClicked");
                    }

                });

    }
}
