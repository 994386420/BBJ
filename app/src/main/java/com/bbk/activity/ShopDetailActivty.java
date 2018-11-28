package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.CanshuBean;
import com.bbk.Bean.PlBean;
import com.bbk.Bean.ShopDetailBean;
import com.bbk.Bean.ShopTuijianBean;
import com.bbk.Bean.TypesChooseBean;
import com.bbk.Bean.TypesChooseLevelOneBean;
import com.bbk.Bean.TypesChooseSizeBean;
import com.bbk.Bean.TypesLevelBean;
import com.bbk.adapter.DetailImageAdapter;
import com.bbk.adapter.ShopCanshuAdapter;
import com.bbk.adapter.SizeChooseAdapter;
import com.bbk.adapter.TuijianGridAdapter;
import com.bbk.adapter.TypeChooseAdapter;
import com.bbk.adapter.TypeChooseLevelOneAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.MainActivity;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.CarActivity;
import com.bbk.shopcar.ConfirmOrderActivity;
import com.bbk.shopcar.MyPlActivity;
import com.bbk.shopcar.NewDianpuActivity;
import com.bbk.shopcar.NewDianpuHomeActivity;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.shopcar.view.IdeaScrollView;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageGuanggaoLoader;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;
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
    @BindView(R.id.tv_youhui)
    AdaptionSizeTextView tvYouhui;
    @BindView(R.id.ll_youhui)
    LinearLayout llYouhui;
    @BindView(R.id.img_more)
    ImageView imgMore;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.tv_des)
    TextView tvDes;
    private String id;
    private DetailImageAdapter detailImageAdapter;
    private ShopDetailBean shopDetailBean;
    private ShopDialog shopDialog;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private float currentPercentage = 0;
    Handler handler = new Handler();
    private RadioGroup.OnCheckedChangeListener radioGroupListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked() ? getRadioCheckedAlphaColor(currentPercentage) : getRadioAlphaColor(currentPercentage));
                if (radioButton.isChecked() && isNeedScrollTo) {
                    ideaScrollView.setPosition(i);
                    Logg.e(i);
                    if (i == 3) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ideaScrollView.fullScroll(IdeaScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
            }
        }
    };
    private boolean isNeedScrollTo = true;
    private String mKefuDescription;
    private String LogFlag;
    private TypeChooseAdapter typeGridAdapter;
    private TypeChooseLevelOneAdapter typeChooseLevelOneAdapter;
    private SizeChooseAdapter sizeChooseAdapter;
    private int curposition = 0, sizeCurposition = 0;
    private List<TypesChooseBean> typesChooseBeans;
    private List<TypesChooseSizeBean> typesChooseSizeBeans;
    private List<TypesLevelBean> typesLevelBeans;
    private List<TypesChooseLevelOneBean> typesChooseLevelOneBeans;
    private GridView gridViewName;
    private GridView gridViewSize;
    private TextView tvHasChoose;
    private TextView tvMoney,tvSize,tvColor;
    private String chooseGuigeColor, chooseGuigeSize;
    private boolean isChooseColor = false;//判断是否旋转过颜色
    private int countNum = 1;//购买商品数量

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
        llMore.setVisibility(View.VISIBLE);
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
        Logg.e(detailImageList.computeVerticalScrollExtent());
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
                    llMore.setVisibility(View.VISIBLE);
                    imgMoreBlack.setVisibility(View.GONE);
                } else if (percentage > 0.0 && percentage < 0.5) {
                    llCar.setVisibility(View.VISIBLE);
                    imgCarBlack.setVisibility(View.GONE);
                    llMore.setVisibility(View.VISIBLE);
                    imgMoreBlack.setVisibility(View.GONE);
                } else {
                    llCar.setVisibility(View.GONE);
                    imgCarBlack.setVisibility(View.VISIBLE);
                    llMore.setVisibility(View.GONE);
                    imgMoreBlack.setVisibility(View.VISIBLE);
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
                                Logg.json(shopDetailBean.getGuigetype() + "=====>>>" + shopDetailBean.getGuigepro() + "=====>>>" + shopDetailBean.getGuige()
                                        + "=====>>>" + shopDetailBean.getYouhui());
                                if (shopDetailBean.getYouhui() != null && !shopDetailBean.getYouhui().equals("")) {
                                    llYouhui.setVisibility(View.VISIBLE);
                                    tvYouhui.setText(shopDetailBean.getYouhui());
                                } else {
                                    llYouhui.setVisibility(View.GONE);
                                }
                                if (shopDetailBean.getDescrible() != null && !shopDetailBean.getDescrible().equals("")) {
                                    tvDes.setText(shopDetailBean.getDescrible());
                                }else {
                                    tvDes.setVisibility(View.GONE);
                                }
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
                                            detailImageAdapter = new DetailImageAdapter(ShopDetailActivty.this, DetailimgUrlList);
                                            detailImageList.setAdapter(detailImageAdapter);
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

    @OnClick({R.id.title_back_btn, R.id.ll_add_car, R.id.ll_buy_now, R.id.ll_fuwu, R.id.ll_shuxing, R.id.ll_canshu, R.id.tv_dianpu, R.id.back_image, R.id.tv_all_mall,
            R.id.ll_dianpu, R.id.ll_car, R.id.ll_pl, R.id.ll_kefu, R.id.ll_more, R.id.img_more_black, R.id.img_car_black})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_add_car:
                if (shopDetailBean != null) {
                    if (TextUtils.isEmpty(userID)) {
                        LogFlag = "1";
                        intent = new Intent(this, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
//                        doShoppingCart(shopDetailBean.getId(), "1", "1", " ");
                        showChooseGuigeDialog(this);
                    }
                }
                break;
            case R.id.ll_buy_now:
                if (shopDetailBean != null) {
                    if (TextUtils.isEmpty(userID)) {
                        LogFlag = "2";
                        intent = new Intent(this, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
//                        intent = new Intent(ShopDetailActivty.this, ConfirmOrderActivity.class);
//                        intent.putExtra("ids", shopDetailBean.getId());
//                        intent.putExtra("nums", "1");
//                        intent.putExtra("guiges", " ");
//                        startActivity(intent);
                        showChooseGuigeDialog(this);
                    }
                }
                break;
            case R.id.ll_fuwu:
                if (shopDetailBean != null) {
                    showBaozhangeDialog(this);
                }
                break;
            case R.id.ll_shuxing:
                if (shopDetailBean != null) {
                    if (TextUtils.isEmpty(userID)) {
                        LogFlag = "9";
                        intent = new Intent(this, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        showChooseGuigeDialog(this);
                    }
                }
                break;
            case R.id.ll_canshu:
                if (shopDetailBean != null) {
                    showMessageDialog(this);
                }
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
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "3";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(this, CarActivity.class);
                    intent.putExtra("ziying","yes");
                    startActivity(intent);
                }
                break;
            case R.id.ll_pl:
                if (shopDetailBean != null) {
                    if (TextUtils.isEmpty(userID)) {
                        LogFlag = "4";
                        intent = new Intent(this, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        intent = new Intent(this, MyPlActivity.class);
                        intent.putExtra("id", id);
                        if (shopDetailBean.getImgurl() != null) {
                            intent.putExtra("img", shopDetailBean.getImgurl());
                        }
                        startActivity(intent);
                    }
                }
                break;
            case R.id.ll_kefu:
                if (shopDetailBean != null) {
                    if (TextUtils.isEmpty(userID)) {
                        LogFlag = "5";
                        intent = new Intent(this, UserLoginNewActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
//                        HomeLoadUtil.startChat(this);
                        MainActivity.consultService(this, "", "商品详情",null);
                    }
                }
                break;
            case R.id.ll_more:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "6";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    HomeLoadUtil.showItemPop(this, llMore);
                }
                break;
            case R.id.img_more_black:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "7";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    HomeLoadUtil.showItemPop(this, imgMoreBlack);
                }
                break;
            case R.id.img_car_black:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "8";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
                    intent = new Intent(this, CarActivity.class);
                    intent.putExtra("ziying","yes");
                    startActivity(intent);
                }
                break;
        }
    }


    /**
     * 登陆回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    switch (LogFlag) {
                        case "1":
                            showChooseGuigeDialog(this);
                            break;
                        case "2":
                            showChooseGuigeDialog(this);
                            break;
                        case "3":
                            intent = new Intent(this, CarActivity.class);
                            intent.putExtra("ziying","yes");
                            startActivity(intent);
                            break;
                        case "4":
                            if (shopDetailBean != null) {
                                intent = new Intent(this, MyPlActivity.class);
                                intent.putExtra("id", id);
                                if (shopDetailBean.getImgurl() != null) {
                                    intent.putExtra("img", shopDetailBean.getImgurl());
                                }
                                startActivity(intent);
                            }
                            break;
                        case "5":
                            if (shopDetailBean != null) {
//                                HomeLoadUtil.startChat(this);
                                MainActivity.consultService(this, "", "我的",null);
                            }
                            break;
                        case "6":
                            HomeLoadUtil.showItemPop(this, llMore);
                            break;
                        case "7":
                            HomeLoadUtil.showItemPop(this, imgMoreBlack);
                            break;
                        case "8":
                            intent = new Intent(this, CarActivity.class);
                            intent.putExtra("ziying","yes");
                            startActivity(intent);
                            break;
                        case "9":
                            showChooseGuigeDialog(this);
                            break;
                    }
                    break;
            }
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
            Logg.json(shopDetailBean.getProperty());
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

    /**
     * 选择规格
     *
     * @param context
     */
    public void showChooseGuigeDialog(final Context context) {
        if (shopDialog == null || !shopDialog.isShowing()) {
            isChooseColor = false;
            chooseGuigeColor = null;
            chooseGuigeSize = null;
            countNum = 1;
            shopDialog = new ShopDialog(context, R.layout.shop_dialog,
                    new int[]{R.id.ll_close});
            shopDialog.show();
            shopDialog.setCanceledOnTouchOutside(true);
            LinearLayout ll_close = shopDialog.findViewById(R.id.ll_close);
            gridViewName = shopDialog.findViewById(R.id.type_grid_name);
            gridViewSize = shopDialog.findViewById(R.id.type_grid_size);
            tvHasChoose = shopDialog.findViewById(R.id.tv_have_choose);
            tvMoney = shopDialog.findViewById(R.id.tv_money);
            tvColor = shopDialog.findViewById(R.id.tv_color);
            tvSize = shopDialog.findViewById(R.id.tv_size);
            LinearLayout lltype = shopDialog.findViewById(R.id.ll_type);
            ImageView ivSub = shopDialog.findViewById(R.id.iv_sub);
            final EditText etGoodNum = shopDialog.findViewById(R.id.et_good_num);
            ImageView ivAdd = shopDialog.findViewById(R.id.iv_add);
            LinearLayout llAddCar = shopDialog.findViewById(R.id.ll_add_car);
            LinearLayout llBuyNow = shopDialog.findViewById(R.id.ll_buy_now);
            tvMoney.setText("¥ " + shopDetailBean.getPrice());
            ImageView imageView = shopDialog.findViewById(R.id.iv_image);
            Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
            gridViewSize.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gridViewName.setSelector(new ColorDrawable(Color.TRANSPARENT));
            typesLevelBeans =  JSON.parseArray(shopDetailBean.getGuigepro(), TypesLevelBean.class);

            /**
             * 0无规格选	1一种规格  2两种规格
             */
            switch (shopDetailBean.getGuigetype()) {
                case "0":
                    //无规格选
                    lltype.setVisibility(View.GONE);
                    break;
                case "1":
                    //一种规格
                    //两种规格
                    tvColor.setText(typesLevelBeans.get(0).getName());
                    tvSize.setVisibility(View.GONE);
                    gridViewSize.setVisibility(View.GONE);
                    gridViewName.setVisibility(View.VISIBLE);
                    typesChooseLevelOneBeans = JSON.parseArray(shopDetailBean.getGuige(), TypesChooseLevelOneBean.class);
                    typeChooseLevelOneAdapter = new TypeChooseLevelOneAdapter(context, typesChooseLevelOneBeans);
                    gridViewName.setAdapter(typeChooseLevelOneAdapter);
                    gridViewName.setOnItemClickListener(onItemClickLevelOneListener);
                    llAddCar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (chooseGuigeColor != null) {
                                doShoppingCart(shopDetailBean.getId(), "1", etGoodNum.getText().toString(), chooseGuigeColor);
                                shopDialog.dismiss();
                            } else {
                                StringUtil.showToast(ShopDetailActivty.this, "请选择商品规格");
                            }
                        }
                    });
                    break;
                case "2":
                    //两种规格
                    tvColor.setText(typesLevelBeans.get(0).getName());
                    tvSize.setText(typesLevelBeans.get(1).getName());
                    gridViewSize.setVisibility(View.VISIBLE);
                    gridViewName.setVisibility(View.VISIBLE);
                    typesChooseBeans = JSON.parseArray(shopDetailBean.getGuige(), TypesChooseBean.class);
                    typeGridAdapter = new TypeChooseAdapter(context, typesChooseBeans);
                    gridViewName.setAdapter(typeGridAdapter);
                    gridViewName.setOnItemClickListener(onItemClickListener);
                    gridViewSize.setOnItemClickListener(onItemSizeClickListener);
                    List<String> list = new ArrayList<>();
                    if (typesChooseBeans != null && typesChooseBeans.size() > 0) {
                        for (int i = 0; i < typesChooseBeans.size(); i++) {
                            typesChooseSizeBeans = JSON.parseArray(typesChooseBeans.get(i).getList(), TypesChooseSizeBean.class);
                            for (int j = 0; j < typesChooseSizeBeans.size(); j++) {
                                Logg.json(typesChooseSizeBeans.get(j).getSize());
                                list.add(typesChooseSizeBeans.get(j).getSize());
                            }
                        }
                    }
                    sizeChooseAdapter = new SizeChooseAdapter(context, StringUtil.removeDuplicate(list), StringUtil.removeDuplicate(list));
                    gridViewSize.setAdapter(sizeChooseAdapter);
                    break;
            }


            /**
             * 加入购物车
             */
            llAddCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (shopDetailBean.getGuigetype()) {
                        case "0":
                            //无规格选
                            doShoppingCart(shopDetailBean.getId(), "1", etGoodNum.getText().toString(),  "");
                            shopDialog.dismiss();
                            break;
                        case "1":
                            if (chooseGuigeColor != null) {
                                doShoppingCart(shopDetailBean.getId(), "1", etGoodNum.getText().toString(), chooseGuigeColor);
                                shopDialog.dismiss();
                                return;
                            }
                            StringUtil.showToast(ShopDetailActivty.this, "请选择商品规格");
                            break;
                        case "2":
                            if (chooseGuigeColor != null && chooseGuigeSize != null) {
                                doShoppingCart(shopDetailBean.getId(), "1", etGoodNum.getText().toString(), chooseGuigeColor + " " + chooseGuigeSize);
                                shopDialog.dismiss();
                                return;
                            }
                            StringUtil.showToast(ShopDetailActivty.this, "请选择商品规格");
                            break;
                    }
                }
            });


            /**
             * 立即购买
             */
            llBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    switch (shopDetailBean.getGuigetype()) {
                        case "0":
                            //无规格选
                            intent = new Intent(ShopDetailActivty.this, ConfirmOrderActivity.class);
                            intent.putExtra("ids", shopDetailBean.getId());
                            intent.putExtra("nums", etGoodNum.getText().toString());
                            intent.putExtra("guiges", "");
                            startActivity(intent);
                            shopDialog.dismiss();
                            break;
                        case "1":
                            if (chooseGuigeColor != null) {
                                intent = new Intent(ShopDetailActivty.this, ConfirmOrderActivity.class);
                                intent.putExtra("ids", shopDetailBean.getId());
                                intent.putExtra("nums", etGoodNum.getText().toString());
                                intent.putExtra("guiges", chooseGuigeColor);
                                startActivity(intent);
                                shopDialog.dismiss();
                                return;
                            }
                            StringUtil.showToast(ShopDetailActivty.this, "请选择商品规格");
                            break;
                        case "2":
                            if (chooseGuigeColor != null && chooseGuigeSize != null) {
                                intent = new Intent(ShopDetailActivty.this, ConfirmOrderActivity.class);
                                intent.putExtra("ids", shopDetailBean.getId());
                                intent.putExtra("nums", etGoodNum.getText().toString());
                                intent.putExtra("guiges", chooseGuigeColor + " " + chooseGuigeSize);
                                startActivity(intent);
                                shopDialog.dismiss();
                                return;
                            }
                            StringUtil.showToast(ShopDetailActivty.this, "请选择商品规格");
                            break;
                    }
                }
            });
            /**
             * 增加数量
             */
            ivAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countNum < 99) {
                        countNum = countNum + 1;
                        etGoodNum.setText(countNum + "");
                    } else {
                        StringUtil.showToast(ShopDetailActivty.this, "不能再加啦!");
                    }
                }
            });
            /**
             * 减少数量
             */
            ivSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countNum > 1) {
                        countNum = countNum - 1;
                        etGoodNum.setText(countNum + "");
                    } else {
                        StringUtil.showToast(ShopDetailActivty.this, "不能再减啦!");
                    }
                }
            });
            /**
             * 数量监听
             */
            etGoodNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                        if (!s.toString().substring(1, 2).equals(".")) {
                            etGoodNum.setText(s.subSequence(0, 1));
                            etGoodNum.setSelection(1);
                            return;
                        }
                    }
                    if (s.length() > 0) {
                        if (Integer.parseInt(s.toString().trim()) > 99) {
                            countNum = 99;
                            etGoodNum.setText("99");
                            StringUtil.showToast(ShopDetailActivty.this, "不能再加啦!");
                        } else if (Integer.parseInt(s.toString().trim()) < 1) {
                            countNum = 1;
                            etGoodNum.setText("1");
                            StringUtil.showToast(ShopDetailActivty.this, "不能再减啦!");
                        } else {
                            countNum = Integer.parseInt(s.toString().trim());
                        }
                    } else {

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            /**
             * 关闭弹窗
             */
            ll_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
        }
    }

    /**
     * 一级点击监听
     */
    AdapterView.OnItemClickListener onItemClickLevelOneListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            isChooseColor = true;
            curposition = position;
            typeChooseLevelOneAdapter.setSeclection(position);
            typeChooseLevelOneAdapter.notifyDataSetChanged();
            if (!typesChooseLevelOneBeans.get(position).getPrice().equals("")) {
                tvMoney.setText("¥ " + typesChooseLevelOneBeans.get(position).getPrice());
            }else {
                tvMoney.setText("¥ " + shopDetailBean.getPrice());
            }
            StringBuffer sb = new StringBuffer();
            chooseGuigeColor = typesChooseLevelOneBeans.get(position).getName();
            tvHasChoose.setText(sb.append("已选择:").append(typesChooseLevelOneBeans.get(position).getName()).toString());
        }
    };

    /**
     * 二级点击监听
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            isChooseColor = true;
            chooseGuigeColor = null;
            chooseGuigeSize = null;
            curposition = position;
            typeGridAdapter.setSeclection(position);
            typeGridAdapter.notifyDataSetChanged();
            tvMoney.setText("¥ " + shopDetailBean.getPrice());
            StringBuffer sb = new StringBuffer();
            List<String> list = new ArrayList<>();
            List<String> listSize = new ArrayList<>();
            List<String> listPrice = new ArrayList<>();
            for (int i = 0; i < typesChooseBeans.size(); i++) {
                Logg.json(typesChooseBeans.get(position).getList());
                typesChooseSizeBeans = JSON.parseArray(typesChooseBeans.get(position).getList(), TypesChooseSizeBean.class);
                for (int j = 0; j < typesChooseSizeBeans.size(); j++) {
                    listSize.add(typesChooseSizeBeans.get(j).getSize());
                    list.add(typesChooseSizeBeans.get(j).getSize());
                    listPrice.add(typesChooseSizeBeans.get(j).getPrice());
                }
            }
            tvHasChoose.setText(sb.append("已选择:").append(typesChooseBeans.get(position).getName()).toString());
            sizeChooseAdapter = new SizeChooseAdapter(ShopDetailActivty.this, StringUtil.removeDuplicate(listSize), StringUtil.removeDuplicate(listSize));
            gridViewSize.setAdapter(sizeChooseAdapter);
        }
    };

    /**
     * 二级点击监听
     */
    AdapterView.OnItemClickListener onItemSizeClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isChooseColor) {
                sizeCurposition = position;
                sizeChooseAdapter.setSeclection(position);
                sizeChooseAdapter.notifyDataSetChanged();
                StringBuffer sb = new StringBuffer();
                StringBuffer sbMoney = new StringBuffer();
                List<String> listSize = new ArrayList<>();
                List<String> listPrice = new ArrayList<>();
                for (int i = 0; i < typesChooseSizeBeans.size(); i++) {
                    Logg.json(typesChooseSizeBeans.get(position).getPrice());
//                    typesChooseSizeBeans = JSON.parseArray(typesChooseBeans.get(position).getList(), TypesChooseSizeBean.class);
//                    for (int j = 0; j < typesChooseSizeBeans.size(); j++) {
                        listSize.add(typesChooseSizeBeans.get(i).getSize());
                        listPrice.add(typesChooseSizeBeans.get(i).getPrice());
//                    }
                }

                if (!listPrice.get(position).equals("")) {
                    tvMoney.setText(sbMoney.append("¥ ").append(listPrice.get(position).toString()));
                }else {
                    tvMoney.setText(sbMoney.append("¥ ").append(shopDetailBean.getPrice()));
                }
                tvHasChoose.setText(sb.append("已选择:").append(typesChooseBeans.get(curposition).getName()).append(" ").append(listSize.get(position).toString()).toString());
                chooseGuigeColor = typesChooseBeans.get(curposition).getName();
                chooseGuigeSize = listSize.get(position).toString();
            } else {
                StringUtil.showToast(ShopDetailActivty.this, "请选择商品颜色");
            }
        }
    };
//        //"<img border=\\\"0\" src=\"" + shopDetailBean.getImgurl() + "\" />   <p>商品名称：" + shopDetailBean.getTitle() + "</p> <p>\\"+shopDetailBean.getPrice()+"</p> "
//        mKefuDescription = "<img border='0' src='" + shopDetailBean.getImgurl() + "' /> <p>商品名称：" + shopDetailBean.getTitle() + " </p>   <p>商品价格：" + shopDetailBean.getPrice() + "</p> ";
}
