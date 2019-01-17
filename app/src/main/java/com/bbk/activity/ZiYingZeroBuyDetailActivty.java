package com.bbk.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.ShopDetailBean;
import com.bbk.Bean.TypesChooseBean;
import com.bbk.Bean.TypesChooseLevelOneBean;
import com.bbk.Bean.TypesChooseSizeBean;
import com.bbk.Bean.TypesLevelBean;
import com.bbk.adapter.DetailImageAdapter;
import com.bbk.adapter.TagAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.ConfirmOrderZeroBuyActivity;
import com.bbk.shopcar.ShopOrderActivity;
import com.bbk.shopcar.Utils.ShopDialog;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.GlideImageGuanggaoLoader;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ShareZeroBuyUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.util.UpdataDialog;
import com.bumptech.glide.Glide;
import com.hhl.library.FlowTagLayout;
import com.hhl.library.OnTagSelectListener;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

public class ZiYingZeroBuyDetailActivty extends BaseActivity {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_dianpu)
    TextView tvDianpu;
    @BindView(R.id.tv_sale)
    TextView tvSale;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.bprice)
    TextView bprice;
    @BindView(R.id.quan)
    TextView quan;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.detail_image)
    ImageView detailImage;
    @BindView(R.id.detail_image_list)
    RecyclerView detailImageList;
    @BindView(R.id.guess_like_list)
    RecyclerView guessLikeList;
    @BindView(R.id.tv_zuan)
    TextView tvZuan;
    @BindView(R.id.ll_share)
    LinearLayout llShare;
    @BindView(R.id.ll_lingquan)
    LinearLayout llLingquan;
    @BindView(R.id.tv_mall)
    TextView tvMall;
    @BindView(R.id.image_fenxiang)
    ImageView imageFenxiang;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_qhj)
    TextView tvQhj;
    @BindView(R.id.tv_zuan1)
    TextView tvZuan1;
    @BindView(R.id.rl_detail)
    RelativeLayout rlDetail;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_dikou)
    TextView tvDikou;
    @BindView(R.id.ck_dikou)
    CheckBox ckDikou;
    @BindView(R.id.ll_check)
    LinearLayout llCheck;
    @BindView(R.id.ll_zerobuy)
    LinearLayout llZerobuy;
    @BindView(R.id.ll_shareorlingquan)
    LinearLayout llShareorlingquan;
    @BindView(R.id.rl_guess_like)
    RelativeLayout rlGuessLike;
    @BindView(R.id.sx_view)
    View sxView;
    @BindView(R.id.ll_shuxing)
    LinearLayout llShuxing;
    @BindView(R.id.tv_car)
    TextView tvCar;
    private String content;
    private int durationRotate = 700;// 旋转动画时间
    private int durationAlpha = 500;// 透明度动画时间
    private boolean isGlobalMenuShow = true;
    private String url, rowkey, domain, quans, jumpdomain, zuan, tljNumber, isOldUser, title, bPrice, id, gid;
    public static String Flag = "";
    public static String flag = "", LogFlag = "";
    private UpdataDialog updataDialog;
    Bitmap bitmap = null;
    private Handler handler = new Handler();
    public static TextView tvShare;
    public static TextView tvZeroBuy;
    private ShopDetailBean shopDetailBean;
    private ShopDialog shopDialog;
    private FlowTagLayout gridViewName;
    private FlowTagLayout gridViewSize;
    private TextView tvHasChoose;
    private TextView tvMoney, tvSize, tvColor;
    private String chooseGuigeColor, chooseGuigeSize;
    private boolean isChooseColor = false;//判断是否旋转过颜色
    private int countNum = 1;//购买商品数量
    private TagAdapter<String> mSizeTagAdapter;
    private TagAdapter<String> mColorTagAdapter;
    private int curposition = 0, sizeCurposition = 0, picturePostion0ne = 0, picturePostionTwo = 0;
    private List<TypesChooseBean> typesChooseBeans;
    private List<TypesChooseSizeBean> typesChooseSizeBeans;
    private List<TypesLevelBean> typesLevelBeans;
    private List<TypesChooseLevelOneBean> typesChooseLevelOneBeans;
    private String isOlder,jifen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_detail_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, null);
        ButterKnife.bind(this);
        NewConstants.address = "1";
        id = getIntent().getStringExtra("id");
        gid = getIntent().getStringExtra("gid");
        isOlder = getIntent().getStringExtra("isOlder");
        jifen = getIntent().getStringExtra("jifen");
        if (isOlder.equals("yes")){
            tvCar.setText(jifen+"积分兑换");
        }else {
            tvCar.setText("新人0元购");
        }
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                queryProductDetailById();
            }
        });
        quan.setVisibility(View.GONE);
        tvSale.setVisibility(View.GONE);
        llZerobuy.setVisibility(View.VISIBLE);
        tvQhj.setVisibility(View.GONE);
        tvMall.setText("比比鲸");
        rlGuessLike.setVisibility(View.GONE);
        guessLikeList.setVisibility(View.GONE);
        llCheck.setVisibility(View.GONE);
        sxView.setVisibility(View.GONE);
        llShuxing.setVisibility(View.VISIBLE);
        queryProductDetailById();
    }


    /**
     * 显示菜单；图标动画
     */
    private void showGlobalMenu() {
        if (isGlobalMenuShow) {
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 0, 180)
                    .setDuration(durationRotate).start();
            detailImageList.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(detailImageList, "alpha", 0, 1)
                    .setDuration(durationAlpha).start();
        } else {
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImage, "rotation", 180, 360)
                    .setDuration(durationRotate).start();
            ObjectAnimator.ofFloat(detailImageList, "alpha", 1, 0)
                    .setDuration(durationAlpha).start();
            detailImageList.postDelayed(new Runnable() {
                @Override
                public void run() {
                    detailImageList.setVisibility(View.GONE);
                }
            }, durationAlpha);
        }

    }

    @OnClick({R.id.back_image, R.id.detail_image, R.id.ll_share, R.id.ll_lingquan, R.id.ll_zerobuy})
    public void onViewClicked(View view) {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        switch (view.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.detail_image:
                isGlobalMenuShow = !isGlobalMenuShow;
                showGlobalMenu();
                break;
            case R.id.ll_zerobuy:
                if (TextUtils.isEmpty(userID)) {
                    LogFlag = "3";
                    intent = new Intent(this, UserLoginNewActivity.class);
                    startActivityForResult(intent, 1);
                } else {
//                    showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                    if (shopDetailBean.getGuigetype() != null){
                        if (shopDetailBean.getGuigetype().equals("0")){
                            showZeroBuyDiscountDialog(this);
                        }else {
                            showChooseGuigeDialog(this);
                        }
                    }
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (arg0) {
                case 1:
                    queryProductDetailById1();
                    break;
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     * 获取当前分享图片的bitmap
     *
     * @param shareBean
     * @param url
     * @return
     */
    public Bitmap returnZeroBuyBitMap(final ShopDetailBean shareBean, final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                URL imageurl = null;
                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            /**
                             * 调起分享dialog
                             */
                            DialogSingleUtil.dismiss(0);
                            List<String> DetailimgUrlList = new ArrayList<>();
                            DetailimgUrlList.add(shareBean.getShareimg());
                            ShareZeroBuyUtil.showShareDialog(llShare, ZiYingZeroBuyDetailActivty.this, shareBean.getTitle(), "", "", shareBean.getShareimg(), "", imageFenxiang, "", bitmap, DetailimgUrlList);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        if (ZiYingZeroBuyDetailActivty.this != null && ZiYingZeroBuyDetailActivty.this.isFinishing()) {
            DialogSingleUtil.dismiss(0);
        }
        super.onDestroy();
    }


    /**
     * 抢购弹窗
     *
     * @param context
     */
    public void showZeroBuyDiscountDialog(final Context context) {
        if (updataDialog == null || !updataDialog.isShowing()) {
            //初始化弹窗 布局 点击事件的id
            updataDialog = new UpdataDialog(context, R.layout.hehuo_dialog_layout,
                    new int[]{R.id.tv_update_gengxin});
            updataDialog.show();
            updataDialog.setCanceledOnTouchOutside(true);
            TextView title = updataDialog.findViewById(R.id.title);
            TextView tv_update = updataDialog.findViewById(R.id.tv_update);
            tvShare = updataDialog.findViewById(R.id.tv_update_refuse);
            tvZeroBuy = updataDialog.findViewById(R.id.tv_update_gengxin);
            tvShare.setVisibility(View.VISIBLE);
            tvShare.setText("分享");
            tvZeroBuy.setText("下单");
            ImageView img_close = updataDialog.findViewById(R.id.img_close);
            if (isOlder.equals("yes")){
                if (shopDetailBean.getCanZeroBuyJifen() != null && shopDetailBean.getCanZeroBuyJifen().equals("1")) {
                    title.setText("抢单成功");
                    if (isOlder.equals("yes")){
                        tv_update.setText("分享朋友圈立即兑换");
                    }else {
                        tv_update.setText("分享朋友圈立即0元购");
                    }
                    String isShare = SharedPreferencesUtil.getSharedData(context, "isShare", "isShare");
                    //判断是否分享
                    if (TextUtils.isEmpty(isShare)) {
                        /**
                         * 未分享提示分享
                         */
                        tvShare.setBackgroundResource(R.drawable.bg_czg1);
                        tvShare.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                        tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                        tvZeroBuy.setClickable(false);
                        tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                StringUtil.showToast(context, "分享后才能下单哦");
                            }
                        });
                    } else {
                        /**
                         * 分享后的操作
                         */
                        tvShare.setBackgroundResource(R.drawable.bg_czg1);
                        tvShare.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setBackgroundResource(R.drawable.bg_czg1);
                        tvZeroBuy.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setClickable(true);
                        tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                Intent intent;
                                switch (shopDetailBean.getGuigetype()) {
                                    case "0":
                                        //无规格选
                                        if (isOlder.equals("yes")) {
                                            //直接支付改为跳转订单页面
//                                            getZeroBuyOrderOld("");
                                            shopDialog.dismiss();
                                            setIntent(id,"",shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                            return;
                                        }
                                        getZeroBuyOrder("");
                                        break;
                                    case "1":
                                        if (chooseGuigeColor != null) {
                                            shopDialog.dismiss();
                                            if (isOlder.equals("yes")) {
//                                                getZeroBuyOrderOld(chooseGuigeColor);
                                                setIntent(id,chooseGuigeColor,shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                                return;
                                            }
                                            getZeroBuyOrder(chooseGuigeColor);
                                            return;
                                        }
                                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "请选择商品规格");
                                        break;
                                    case "2":
                                        if (chooseGuigeColor != null && chooseGuigeSize != null) {
                                            shopDialog.dismiss();
                                            if (isOlder.equals("yes")) {
                                                setIntent(id,chooseGuigeColor + " " + chooseGuigeSize,shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                                return;
                                            }
                                            getZeroBuyOrder(chooseGuigeColor + " " + chooseGuigeSize);
                                            return;
                                        }
                                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "请选择商品规格");
                                        break;
                                }
                            }
                        });
                    }
                } else {
                    tvShare.setBackgroundResource(R.drawable.bg_czg1);
                    tvShare.setTextColor(getResources().getColor(R.color.white));
                    tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                    tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                    title.setText(shopDetailBean.getZeroBuyDescJifen());
                    tv_update.setVisibility(View.GONE);
                    tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            StringUtil.showToast(context, shopDetailBean.getZeroBuyDescJifen());
                        }
                    });
                }
            }else {
                if (isOldUser != null && isOldUser.equals("0")) {
                    title.setText("抢单成功");
                    if (isOlder.equals("yes")){
                        tv_update.setText("分享朋友圈立即兑换");
                    }else {
                        tv_update.setText("分享朋友圈立即0元购");
                    }
                    String isShare = SharedPreferencesUtil.getSharedData(context, "isShare", "isShare");
                    //判断是否分享
                    if (TextUtils.isEmpty(isShare)) {
                        tvShare.setBackgroundResource(R.drawable.bg_czg1);
                        tvShare.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                        tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                        tvZeroBuy.setClickable(false);
                        tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                StringUtil.showToast(context, "分享后才能下单哦");
                            }
                        });
                    } else {
                        tvShare.setBackgroundResource(R.drawable.bg_czg1);
                        tvShare.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setBackgroundResource(R.drawable.bg_czg1);
                        tvZeroBuy.setTextColor(getResources().getColor(R.color.white));
                        tvZeroBuy.setClickable(true);
                        tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updataDialog.dismiss();
                                switch (shopDetailBean.getGuigetype()) {
                                    case "0":
                                        //无规格选
                                        if (isOlder.equals("yes")) {
//                                            getZeroBuyOrderOld("");
                                            shopDialog.dismiss();
                                            setIntent(id,"",shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                            return;
                                        }
                                        getZeroBuyOrder("");
                                        break;
                                    case "1":
                                        if (chooseGuigeColor != null) {
                                            shopDialog.dismiss();
                                            if (isOlder.equals("yes")) {
//                                                getZeroBuyOrderOld(chooseGuigeColor);
                                                setIntent(id,chooseGuigeColor,shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                                return;
                                            }
                                            getZeroBuyOrder(chooseGuigeColor);
                                            return;
                                        }
                                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "请选择商品规格");
                                        break;
                                    case "2":
                                        if (chooseGuigeColor != null && chooseGuigeSize != null) {
                                            shopDialog.dismiss();
                                            if (isOlder.equals("yes")) {
//                                                getZeroBuyOrderOld(chooseGuigeColor + " " + chooseGuigeSize);
                                                setIntent(id,chooseGuigeColor + " " + chooseGuigeSize,shopDetailBean.getImgurl(),shopDetailBean.getTitle(),shopDetailBean.getPrice());
                                                return;
                                            }
                                            getZeroBuyOrder(chooseGuigeColor + " " + chooseGuigeSize);
                                            return;
                                        }
                                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "请选择商品规格");
                                        break;
                                }
                            }
                        });
                    }
                } else {
                    tvShare.setBackgroundResource(R.drawable.bg_czg1);
                    tvShare.setTextColor(getResources().getColor(R.color.white));
                    tvZeroBuy.setBackgroundResource(R.drawable.bg_update1);
                    tvZeroBuy.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                    title.setText("新用户才能享受此特权哦\n分享给好友0元购吧");
                    tv_update.setVisibility(View.GONE);
                    tvZeroBuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updataDialog.dismiss();
                            StringUtil.showToast(context, "新用户才能享受此特权");
                        }
                    });
                }
            }
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
                }
            });
            tvShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updataDialog.dismiss();
//                    shareCpsZeroBuyInfo();
                    returnZeroBuyBitMap(shopDetailBean, shopDetailBean.getShareimg());
                }
            });
        }
    }

    private void setIntent(String id, String s, String imgurl, String title, String price) {
        Intent intent;
        intent = new Intent(ZiYingZeroBuyDetailActivty.this, ConfirmOrderZeroBuyActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("guige",s);
        intent.putExtra("imgurl",imgurl);
        intent.putExtra("title",title);
        intent.putExtra("price",price);
        if (shopDetailBean.getDianpu() != null) {
            intent.putExtra("dianpu", shopDetailBean.getDianpu());
        }
        intent.putExtra("jifen",jifen);
        if (shopDetailBean.getKuaidi() != null) {
            intent.putExtra("yunfei", shopDetailBean.getKuaidi());
        }
        startActivity(intent);
    }

    /**
     * 商品详情
     */
    private void queryProductDetailById() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", gid);
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryProductDetailById(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                shopDetailBean = JSON.parseObject(jsonObject.optString("content"), ShopDetailBean.class);
                                isOldUser = shopDetailBean.getIsOldUser();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ZiYingZeroBuyDetailActivty.this,
                                        LinearLayoutManager.VERTICAL, false) {
                                    @Override
                                    public boolean canScrollVertically() {
                                        return false;
                                    }

                                };
                                detailImageList.setHasFixedSize(true);
                                detailImageList.setLayoutManager(linearLayoutManager);
                                title = shopDetailBean.getTitle();
                                bPrice = shopDetailBean.getBprice();
                                tvTitle.setText(title);
                                tvDianpu.setText(shopDetailBean.getDianpu());
                                tvSale.setText(shopDetailBean.getSale() + "人付款");
                                if (shopDetailBean.getBprice() != null && !shopDetailBean.getBprice().equals("null")) {
                                    bprice.setVisibility(View.VISIBLE);
                                    bprice.setText("¥" + shopDetailBean.getBprice());
                                } else {
                                    bprice.setVisibility(View.GONE);
                                }
                                bprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
                                List<Object> DetailimgUrlList = new ArrayList<>();
                                /**
                                 * 加载banner
                                 */
                                List<Object> imgUrlList = new ArrayList<>();
                                JSONArray imgs = null;
                                try {
                                    if (shopDetailBean.getDetailImgs() != null) {
                                        JSONArray detailImags = new JSONArray(shopDetailBean.getDetailImgs());
                                        for (int i = 0; i < detailImags.length(); i++) {
                                            String imgUrl = detailImags.getString(i);
                                            DetailimgUrlList.add(imgUrl);
                                        }
                                        if (DetailimgUrlList.size() > 0) {
                                            detailImageList.setAdapter(new DetailImageAdapter(ZiYingZeroBuyDetailActivty.this, DetailimgUrlList));
                                            rlDetail.setVisibility(View.VISIBLE);
                                            detailImageList.setVisibility(View.VISIBLE);
                                            view.setVisibility(View.VISIBLE);
                                        } else {
                                            rlDetail.setVisibility(View.GONE);
                                            detailImageList.setVisibility(View.GONE);
                                            view.setVisibility(View.GONE);
                                        }
                                    }
                                    if (shopDetailBean.getImgs() != null) {
                                        imgs = new JSONArray(shopDetailBean.getImgs());
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
                                                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                                                .setIndicatorGravity(BannerConfig.CENTER)
                                                .start();
                                    }

                                    if (shopDetailBean.getGuigetype() != null){
                                        if (shopDetailBean.getGuigetype().equals("0")){
                                            llShuxing.setVisibility(View.GONE);
                                        }else {
                                            llShuxing.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    refresh.finishRefresh();
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        refresh.finishRefresh();
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

    private void queryProductDetailById1() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("id", gid);
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().queryProductDetailById(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                shopDetailBean = JSON.parseObject(jsonObject.optString("content"), ShopDetailBean.class);
                                isOldUser = shopDetailBean.getIsOldUser();
                                switch (LogFlag) {
                                    case "3":
                                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                                        if (!TextUtils.isEmpty(userID)) {
                                            if (shopDetailBean.getGuigetype() != null){
                                                if (shopDetailBean.getGuigetype().equals("0")){
                                                    showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                                                }else {
                                                    showChooseGuigeDialog(ZiYingZeroBuyDetailActivty.this);
                                                }
                                            }
                                        }
                                        break;
                                    case "1":
                                        if (shopDetailBean.getGuigetype() != null){
                                            if (shopDetailBean.getGuigetype().equals("0")){
                                                showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                                            }else {
                                                showChooseGuigeDialog(ZiYingZeroBuyDetailActivty.this);
                                            }
                                        }
                                        break;
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
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

    /**
     * 0元购支付接口
     */
    private void getZeroBuyOrder(String guige) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("id", id);
        maps.put("guige", guige);
        RetrofitClient.getInstance(this).createBaseApi().getZeroBuyOrder(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("2")) {
                                Logg.json(jsonObject);
                                Intent intent = new Intent(ZiYingZeroBuyDetailActivty.this, ShopOrderActivity.class);
                                intent.putExtra("status", "2");
                                startActivity(intent);
                                StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "购买成功");
                            } else {
                                DialogSingleUtil.dismiss(0);
                                StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, jsonObject.optString("errmsg"));
                                if (jsonObject.optString("errmsg").contains("收货地址")){
                                    Intent intent = new Intent(ZiYingZeroBuyDetailActivty.this, AddressMangerActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

    /**
     * 老用户0元购支付
     */
    private void getZeroBuyOrderOld(String guige) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("id", id);
        maps.put("guige", guige);
        RetrofitClient.getInstance(this).createBaseApi().getZeroBuyOrderOld(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("2")) {
                                Logg.json(jsonObject);
                                Intent intent = new Intent(ZiYingZeroBuyDetailActivty.this, ShopOrderActivity.class);
                                intent.putExtra("status", "2");
                                startActivity(intent);
                                StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "购买成功");
                            } else {
                                DialogSingleUtil.dismiss(0);
                                StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, jsonObject.optString("errmsg"));
                                if (jsonObject.optString("errmsg").contains("收货地址")){
                                    Intent intent = new Intent(ZiYingZeroBuyDetailActivty.this, AddressMangerActivity.class);
                                    startActivity(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(ZiYingZeroBuyDetailActivty.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, e.message);
                    }
                });
    }

    @OnClick(R.id.ll_shuxing)
    public void onViewClicked() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Intent intent;
        if (shopDetailBean != null) {
            if (TextUtils.isEmpty(userID)) {
                LogFlag = "1";
                intent = new Intent(this, UserLoginNewActivity.class);
                startActivityForResult(intent, 1);
            } else {
                if (shopDetailBean.getGuigetype() != null){
                    if (shopDetailBean.getGuigetype().equals("0")){
                        showZeroBuyDiscountDialog(this);
                    }else {
                        showChooseGuigeDialog(this);
                    }
                }
            }
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
            LinearLayout llZeroBuy = shopDialog.findViewById(R.id.ll_zerobuy);
            llZeroBuy.setVisibility(View.VISIBLE);
            LinearLayout llCar = shopDialog.findViewById(R.id.ll_car);
            llCar.setVisibility(View.GONE);
            gridViewName = shopDialog.findViewById(R.id.size_flow_layout);
            gridViewSize = shopDialog.findViewById(R.id.color_flow_layout);
            tvHasChoose = shopDialog.findViewById(R.id.tv_have_choose);
            tvMoney = shopDialog.findViewById(R.id.tv_money);
            tvColor = shopDialog.findViewById(R.id.tv_color);
            tvSize = shopDialog.findViewById(R.id.tv_size);
            TextView tvCar = shopDialog.findViewById(R.id.tv_car);
            if (isOlder.equals("yes")){
                tvCar.setText(jifen+"积分兑换");
            }else {
                tvCar.setText("新人0元购");
            }
            LinearLayout lltype = shopDialog.findViewById(R.id.ll_type);
            LinearLayout llCarNum = shopDialog.findViewById(R.id.ll_car_num);
            llCarNum.setVisibility(View.GONE);
            tvMoney.setText("¥ " + shopDetailBean.getPrice());
            final ImageView imageView = shopDialog.findViewById(R.id.iv_image);
            Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
            typesLevelBeans = JSON.parseArray(shopDetailBean.getGuigepro(), TypesLevelBean.class);

            /**
             * 0无规格选	1一种规格  2两种规格
             */
            switch (shopDetailBean.getGuigetype()) {
                case "0":
                    //无规格选
                    lltype.setVisibility(View.GONE);
                    final List<String> imgList = new ArrayList<>();
                    imgList.add(shopDetailBean.getImgurl());
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent Intent = new Intent(context, DesPictureActivity.class);
                            Intent.putStringArrayListExtra("list", (ArrayList<String>) imgList);
                            Intent.putExtra("position", "0");
                            context.startActivity(Intent);
                        }
                    });
                    break;
                case "1":
                    //一种规格
                    tvColor.setText(typesLevelBeans.get(0).getName());
                    tvSize.setVisibility(View.GONE);
                    gridViewSize.setVisibility(View.GONE);
                    gridViewName.setVisibility(View.VISIBLE);
                    typesChooseLevelOneBeans = JSON.parseArray(shopDetailBean.getGuige(), TypesChooseLevelOneBean.class);
                    mSizeTagAdapter = new TagAdapter<>(this);
                    gridViewName.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                    gridViewName.setAdapter(mSizeTagAdapter);
                    final List<String> imgListOne = new ArrayList<>();
                    imgListOne.add(shopDetailBean.getImgurl());
                    for (int i = 0; i < typesChooseLevelOneBeans.size(); i++) {
                        if (typesChooseLevelOneBeans.get(i).getImg() != null && !typesChooseLevelOneBeans.get(i).getImg().equals("")) {
                            imgListOne.add(typesChooseLevelOneBeans.get(i).getImg());
                        }
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent Intent = new Intent(context, DesPictureActivity.class);
                            Intent.putStringArrayListExtra("list", (ArrayList<String>) imgListOne);
                            Intent.putExtra("position", picturePostion0ne);
                            context.startActivity(Intent);
                        }
                    });
                    gridViewName.setOnTagSelectListener(new OnTagSelectListener() {
                        @Override
                        public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                            if (selectedList != null && selectedList.size() > 0) {
                                StringBuilder sb = new StringBuilder();
                                StringBuffer sbb = new StringBuffer();
                                for (int i : selectedList) {
                                    sb.append(parent.getAdapter().getItem(i));
                                    curposition = i;
                                }
                                isChooseColor = true;
                                if (typesChooseLevelOneBeans.get(curposition).getPrice() != null && !typesChooseLevelOneBeans.get(curposition).getPrice().equals("")) {
                                    tvMoney.setText("¥ " + typesChooseLevelOneBeans.get(curposition).getPrice());
                                } else {
                                    tvMoney.setText("¥ " + shopDetailBean.getPrice());
                                }
                                chooseGuigeColor = sb.toString();
                                tvHasChoose.setText(sbb.append("已选择:").append(sb.toString()).toString());
                                if (typesChooseLevelOneBeans.get(curposition).getImg() != null && !typesChooseLevelOneBeans.get(curposition).getImg().equals("")) {
                                    Glide.with(context).load(typesChooseLevelOneBeans.get(curposition).getImg()).into(imageView);
                                    return;
                                }
                                Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
                            } else {
                                tvHasChoose.setText("请选择商品规格");
                                chooseGuigeColor = null;
                                Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
                            }
                        }
                    });
                    List<String> dataSource = new ArrayList<>();
                    for (int i = 0; i < typesChooseLevelOneBeans.size(); i++) {
                        dataSource.add(typesChooseLevelOneBeans.get(i).getName());
                    }
                    initSizeData(dataSource);
                    break;
                case "2":
                    //两种规格
                    if (typesLevelBeans.get(0).getLevel().equals("1")) {
                        tvColor.setText(typesLevelBeans.get(0).getName());
                    }
                    if (typesLevelBeans.get(1).getLevel().equals("2")) {
                        tvSize.setText(typesLevelBeans.get(1).getName());
                    }
                    if (typesLevelBeans.get(0).getLevel().equals("2")) {
                        tvSize.setText(typesLevelBeans.get(0).getName());
                    }
                    if (typesLevelBeans.get(1).getLevel().equals("1")) {
                        tvColor.setText(typesLevelBeans.get(1).getName());
                    }
                    gridViewSize.setVisibility(View.VISIBLE);
                    gridViewName.setVisibility(View.VISIBLE);
                    typesChooseBeans = JSON.parseArray(shopDetailBean.getGuige(), TypesChooseBean.class);
                    mSizeTagAdapter = new TagAdapter<>(this);
                    gridViewName.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                    gridViewName.setAdapter(mSizeTagAdapter);
                    final List<String> imgListTwo = new ArrayList<>();
                    imgListTwo.add(shopDetailBean.getImgurl());
                    for (int i = 0; i < typesChooseBeans.size(); i++) {
                        if (typesChooseBeans.get(i).getImg() != null && !typesChooseBeans.get(i).getImg().equals("")) {
                            imgListTwo.add(typesChooseBeans.get(i).getImg());
                        }
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent Intent = new Intent(context, DesPictureActivity.class);
                            Intent.putStringArrayListExtra("list", (ArrayList<String>) imgListTwo);
                            Intent.putExtra("position", picturePostionTwo);
                            context.startActivity(Intent);
                        }
                    });
                    gridViewName.setOnTagSelectListener(new OnTagSelectListener() {
                        @Override
                        public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                            StringBuilder sb = new StringBuilder();
                            StringBuffer sbb = new StringBuffer();
                            if (selectedList != null && selectedList.size() > 0) {
                                for (int i : selectedList) {
                                    sb.append(parent.getAdapter().getItem(i));
                                    curposition = i;
                                }
                                isChooseColor = true;
                                chooseGuigeColor = null;
                                chooseGuigeSize = null;
                                tvMoney.setText("¥ " + shopDetailBean.getPrice());
                                tvHasChoose.setText(sbb.append("已选择:").append(sb.toString()));
                                List<String> list = new ArrayList<>();
                                List<String> listSize = new ArrayList<>();
                                List<String> listPrice = new ArrayList<>();
                                for (int i = 0; i < typesChooseBeans.size(); i++) {
                                    typesChooseSizeBeans = JSON.parseArray(typesChooseBeans.get(curposition).getList(), TypesChooseSizeBean.class);
                                    for (int j = 0; j < typesChooseSizeBeans.size(); j++) {
                                        listSize.add(typesChooseSizeBeans.get(j).getSize());
                                        list.add(typesChooseSizeBeans.get(j).getSize());
                                        listPrice.add(typesChooseSizeBeans.get(j).getPrice());
                                    }
                                }
                                chooseGuigeColor = sb.toString();
                                mColorTagAdapter = new TagAdapter<>(ZiYingZeroBuyDetailActivty.this);
                                gridViewSize.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                                gridViewSize.setAdapter(mColorTagAdapter);
                                initColorData(StringUtil.removeDuplicate(listSize));
                                if (typesChooseBeans.get(curposition).getImg() != null && !typesChooseBeans.get(curposition).getImg().equals("")) {
                                    Glide.with(context).load(typesChooseBeans.get(curposition).getImg()).into(imageView);
                                    return;
                                }
                                Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
                            } else {
                                tvHasChoose.setText("请选择商品规格");
                                chooseGuigeColor = null;
                                Glide.with(context).load(shopDetailBean.getImgurl()).into(imageView);
                            }
                        }
                    });
                    List<String> dataSourceColor = new ArrayList<>();
                    for (int i = 0; i < typesChooseBeans.size(); i++) {
                        dataSourceColor.add(typesChooseBeans.get(i).getName());
                    }
                    initSizeData(dataSourceColor);
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
                    mColorTagAdapter = new TagAdapter<>(this);
                    gridViewSize.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                    gridViewSize.setAdapter(mColorTagAdapter);
                    gridViewSize.setOnTagSelectListener(new OnTagSelectListener() {
                        @Override
                        public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                            StringBuilder sb = new StringBuilder();
                            StringBuffer sbSize = new StringBuffer();
                            if (selectedList != null && selectedList.size() > 0) {
                                for (int i : selectedList) {
                                    sb.append(parent.getAdapter().getItem(i));
                                    sizeCurposition = i;
                                }
                                if (isChooseColor && chooseGuigeColor != null) {
                                    StringBuffer sbMoney = new StringBuffer();
                                    List<String> listSize = new ArrayList<>();
                                    List<String> listPrice = new ArrayList<>();
                                    for (int i = 0; i < typesChooseSizeBeans.size(); i++) {
                                        Logg.json(typesChooseSizeBeans.get(sizeCurposition).getPrice());
                                        listSize.add(typesChooseSizeBeans.get(i).getSize());
                                        listPrice.add(typesChooseSizeBeans.get(i).getPrice());
                                    }
                                    if (!listPrice.get(sizeCurposition).equals("")) {
                                        tvMoney.setText(sbMoney.append("¥ ").append(listPrice.get(sizeCurposition).toString()));
                                    } else {
                                        tvMoney.setText(sbMoney.append("¥ ").append(shopDetailBean.getPrice()));
                                    }
                                    tvHasChoose.setText(sbSize.append("已选择:").append(typesChooseBeans.get(curposition).getName()).append(" ").append(listSize.get(sizeCurposition).toString()).toString());
                                    chooseGuigeColor = typesChooseBeans.get(curposition).getName();
                                    chooseGuigeSize = listSize.get(sizeCurposition).toString();
                                } else {
                                    StringUtil.showToast(ZiYingZeroBuyDetailActivty.this, "请选择商品颜色");
                                }
                            } else {
                                chooseGuigeSize = null;
                                if (chooseGuigeColor == null) {
                                    tvHasChoose.setText("请选择商品规格");
                                    return;
                                }
                                tvHasChoose.setText(sbSize.append("已选择:").append(chooseGuigeColor));
                            }
                        }
                    });
                    initColorData(StringUtil.removeDuplicate(list));
                    break;
            }

            /**
             * 关闭弹窗
             */
            ll_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                }
            });
            llZeroBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopDialog.dismiss();
                    showZeroBuyDiscountDialog(ZiYingZeroBuyDetailActivty.this);
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    private void initSizeData(List<String> list) {
        mSizeTagAdapter.onlyAddAll(list);
    }

    /**
     * 初始化数据
     */
    private void initColorData(List<String> list) {
        mColorTagAdapter.onlyAddAll(list);
    }


}
