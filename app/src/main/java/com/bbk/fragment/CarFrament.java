package com.bbk.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.DemoTradeCallback;
import com.bbk.Bean.TaoBaoCarBean;
import com.bbk.Bean.TaobaoCarListBean;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.adapter.TaoBaoAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlowTaobao;
import com.bbk.flow.ResultEvent;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.ConfirmOrderActivity;
import com.bbk.shopcar.NewDianpuActivity;
import com.bbk.shopcar.SwipeExpandableListView;
import com.bbk.shopcar.Utils.UtilsLog;
import com.bbk.shopcar.adapter.ShopcatAdapter;
import com.bbk.shopcar.entity.GoodsInfo;
import com.bbk.shopcar.entity.GoodsInfo1;
import com.bbk.shopcar.entity.StoreInfo;
import com.bbk.util.DialogCheckYouhuiUtil;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.AdaptionSizeTextView;
import com.bbk.view.CommonLoadingView;
import com.kepler.jd.Listener.ActionCallBck;
import com.kepler.jd.Listener.LoginListener;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 购物车
 */
public class CarFrament extends BaseViewPagerFragment implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener, CommonLoadingView.LoadingHandler, CommonLoadingView.LogThird, ResultEvent, DataFlowTaobao.loadInterface {
    @BindView(R.id.listView)
    SwipeExpandableListView listView;
    @BindView(R.id.all_checkBox)
    CheckBox allCheckBox;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.order_info)
    LinearLayout orderInfo;
    @BindView(R.id.share_goods)
    TextView shareGoods;
    @BindView(R.id.collect_goods)
    TextView collectGoods;
    @BindView(R.id.del_goods)
    TextView delGoods;
    @BindView(R.id.share_info)
    LinearLayout shareInfo;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text3)
    TextView titleText;
    @BindView(R.id.title_text2)
    TextView titleText2;
    @BindView(R.id.mPtrframe)
    SmartRefreshLayout mPtrframe;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.ll_bottom_car)
    LinearLayout llBottomCar;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tb_webview)
    WebView tbWebview;
    @BindView(R.id.ll_bottom_tb_car)
    LinearLayout llBottomTbCar;
    @BindView(R.id.tv_tb_car)
    TextView tvCar;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tishi)
    AdaptionSizeTextView tishi;
    @BindView(R.id.ll_refresh_car)
    LinearLayout llRefreshCar;
    @BindView(R.id.tv_intent_car)
    TextView tvIntentCar;
    //    @BindView(R.id.tv_wei_tz)
    public static TextView tvWeiTz;
    @BindView(R.id.ll_third_car)
    LinearLayout llThirdCar;
    @BindView(R.id.img_tishi)
    ImageView imgTishi;
    @BindView(R.id.img_refresh)
    ImageView imgRefresh;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;
    private List<StoreInfo> groups; //组元素的列表
    private Map<String, List<GoodsInfo>> childs; //子元素的列表
    private String ids, nums, guiges;
    private View mView;
    private int carCount;
    private JSONObject jsonObject;
    private String content;
    private List<TaoBaoCarBean> taoBaoCarBeans;
    private DataFlowTaobao dataFlow;
    private int curPosition = 0;
    public static boolean cancelCheck = true;// 是否取消查询
    Handler mHandler;
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    KelperTask mKelperTask;
    List<TaobaoCarListBean> taobaoCarListBeans;
    private int sum = 0;//定义一个变量

    @SuppressLint("JavascriptInterface")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            mView = inflater.inflate(R.layout.shopcar_home_layout, null);
            mHandler = new Handler();
            dataFlow = new DataFlowTaobao(getContext());
            dataFlow.setLoadInterface(this);
            ButterKnife.bind(this, mView);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            View topView = mView.findViewById(R.id.activity_car);
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            ButterKnife.bind(this, mView);
            initPtrFrame();
            initActionBar();
//        initData();
//        initEvents();
//            init();
            tvWeiTz = mView.findViewById(R.id.tv_wei_tz);
            titleText.setText("购物车");
            titleText2.setText("编辑");
            rlTitle.setBackgroundResource(R.color.tuiguang_color10);
            titleText.setTextColor(getActivity().getResources().getColor(R.color.white));
            titleText2.setTextColor(getActivity().getResources().getColor(R.color.white));
            titleText2.setVisibility(View.VISIBLE);
            progress.setLoadingHandler(this);
            progress.setLogThird(this);
            titleBackBtn.setVisibility(View.GONE);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            tablayout.setxTabDisplayNum(3);
            tablayout.addTab(tablayout.newTab().setText("淘宝购物车"));
            tablayout.addTab(tablayout.newTab().setText("京东购物车"));
            tablayout.addTab(tablayout.newTab().setText("鲸城购物车"));
            tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(XTabLayout.Tab tab) {
                    Logg.e(tab.getText());
                    int j = tab.getPosition();
                    final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                    if (j == 0) {
                        curPosition = 0;
                        sum = 0;
                        titleText.setText("淘宝购物车");
                        llBottomCar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        titleText2.setVisibility(View.GONE);
                        DialogSingleUtil.show(getActivity());
                        showTaoBaoCar();
                    } else if (j == 1) {
                        curPosition = 2;
                        sum = 0;
                        llBottomCar.setVisibility(View.GONE);
                        listView.setVisibility(View.GONE);
                        titleText2.setVisibility(View.GONE);
                        titleText.setText("京东购物车");
                        DialogSingleUtil.show(getActivity());
                        showJdCar();
                    } else {
                        curPosition = 1;
                        tishi.setVisibility(View.GONE);
                        imgTishi.setVisibility(View.GONE);
                        llBottomTbCar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        llBottomCar.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        titleText.setText("购物车(" + carCount + ")");
                        titleText2.setVisibility(View.VISIBLE);
                        DialogSingleUtil.show(getActivity());
                        queryShoppingCartByUserid();
//                        progress.loadSuccess(true);

                    }
                }

                @Override
                public void onTabUnselected(XTabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(XTabLayout.Tab tab) {

                }
            });
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    private void initPtrFrame() {
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (curPosition == 1) {
                    queryShoppingCartByUserid();
                    reSet();
                } else if (curPosition == 2) {
                    sum = 0;
                    showJdCar();
                } else {
                    sum = 0;
                    showTaoBaoCar();
//                    getShoppingCartUrlByDomain();
                }
            }
        });
        mPtrframe.setEnableLoadMore(false);
    }


    /**
     * 刷新调用方法重置购物车
     */
    private void reSet() {
        if (groups != null) {
            for (int i = 0; i < groups.size(); i++) {
                StoreInfo group = groups.get(i);
                group.setChoosed(false);
                List<GoodsInfo> child = childs.get(group.getId());
                for (int j = 0; j < child.size(); j++) {
                    child.get(j).setChoosed(false);//这里出现过错误
                }
            }

            calulate();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            allCheckBox.setChecked(false);
        }
    }


    private void initEvents() {
//        actionBarEdit.setOnClickListener(this);
        adapter = new ShopcatAdapter(getActivity(), groups, childs, mcontext);
        adapter.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        adapter.setGroupEditorListener(this);//关键步骤3:监听组列表的编辑状态
        listView.setGroupIndicator(null); //设置属性 GroupIndicator 去掉向下箭头
        listView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i); //关键步骤4:初始化，将ExpandableListView以展开的方式显示
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int firstVisiablePostion = view.getFirstVisiblePosition();
                int top = -1;
                View firstView = view.getChildAt(firstVisibleItem);
                UtilsLog.i("childCount=" + view.getChildCount());//返回的是显示层面上的所包含的子view的个数
                if (firstView != null) {
                    top = firstView.getTop();
                }
                UtilsLog.i("firstVisiableItem=" + firstVisibleItem + ",fistVisiablePosition=" + firstVisiablePostion + ",firstView=" + firstView + ",top=" + top);
                if (firstVisibleItem == 0 && top == 0) {
//                    mPtrFrame.setEnabled(true);
                } else {
//                    mPtrFrame.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Logg.e(NewConstants.refeshFlag);
        if (NewConstants.refeshFlag.equals("1")) {
//            mtotalCount = 0;
//            goPay.setText("去支付(" + mtotalCount + ")");
            if (curPosition == 1) {
                queryShoppingCartByUserid();
                reSet();
            } else if (curPosition == 2) {
                showJdCar();
            } else {
                showTaoBaoCar();
            }
        } else {
            setCartNum();
        }
    }

    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        if (groups != null && groups.size() > 0) {
            for (int i = 0; i < groups.size(); i++) {
                StoreInfo group = groups.get(i);
                group.setChoosed(allCheckBox.isChecked());
                List<GoodsInfo> Childs = childs.get(group.getId());
                for (GoodsInfo childs : Childs) {
                    count++;
                }
            }
        }
//        Logg.e("购物车数量"+count);
        //购物车已经清空
        if (count == 0) {
            if (curPosition == 0) {
                titleText.setText("淘宝购物车");
            } else if (curPosition == 2) {
                titleText.setText("京东购物车");
            } else {
                clearCart();
            }
        } else {
            carCount = count;
            if (curPosition == 0) {
                titleText.setText("淘宝购物车");
            } else if (curPosition == 2) {
                titleText.setText("京东购物车");
            } else {
                titleText.setText("购物车(" + count + ")");
            }
        }

    }

    private void clearCart() {
        titleText.setText("购物车(0)");
//        actionBarEdit.setVisibility(View.GONE);
        llCart.setVisibility(View.VISIBLE);
//        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
     * 其Key是组元素的Id
     */
    private void initData() {
        mcontext = getActivity();
        groups = new ArrayList<StoreInfo>();
        childs = new HashMap<String, List<GoodsInfo>>();
        for (int i = 0; i < 5; i++) {
            groups.add(new StoreInfo(i + "", "小马的第" + (i + 1) + "号当铺"));
            List<GoodsInfo> goods = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                int[] img = {R.mipmap.new_app_icon, R.mipmap.new_app_icon, R.mipmap.new_app_icon, R.mipmap.new_app_icon, R.mipmap.new_app_icon, R.mipmap.new_app_icon};
                //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
//                goods.add(new GoodsInfo(i + "-" + j, "商品", groups.get(i).getName() + "的第" + (j + 1) + "个商品", 255.00 + new Random().nextInt(1500), 1555 + new Random().nextInt(3000), "第一排", "出头天者", img[j], new Random().nextInt(100)));
            }
            childs.put(groups.get(i).getId(), goods);
        }
    }

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        DialogSingleUtil.show(getActivity());
        List<String> list = new ArrayList<>();
        List<String> listguige = new ArrayList<>();
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>(); //待删除的组元素
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteChilds = new ArrayList<GoodsInfo>();//待删除的子元素
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                if (child.get(j).isChoosed()) {
                    toBeDeleteChilds.add(child.get(j));
                    list.add(child.get(j).getId());
                    listguige.add(child.get(j).getGuige());
//                    Logg.e(child.get(j).getId());
                }
            }
            child.removeAll(toBeDeleteChilds);
        }
        Logg.json(list.toString().replace("[", "").replace("]", "").replace(" ", ""));
        doShoppingCart(list.toString().replace("[", "").replace("]", "").replace(" ", ""), "-1", "", listguige.toString().replace("[", "").replace("]", ""), "all");
        groups.removeAll(toBeDeleteGroups);
        //重新设置购物车
        setCartNum();
        calulate();
        adapter.notifyDataSetChanged();

    }


    private void findView(View view) {
//        shoppingcatNum = (TextView) view.findViewById(R.id.shoppingcat_num);
//        actionBarEdit = (Button) view.findViewById(R.id.actionBar_edit);
//        //不知道为什么，ButterKnife不知道BindView
//        empty_shopcart = (LinearLayout) findViewById(R.id.layout_empty_shopcart);
    }


    private void initActionBar() {
//        //隐藏标题栏
//        if (getSupportActionBar() != null) {
//            //去掉阴影
//            getSupportActionBar().setElevation(0);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowCustomEnabled(true);
//            View view = getLayoutInflater().inflate(R.layout.acitonbar, null);
//            findView(view);
//            getSupportActionBar().setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            ActionBar.LayoutParams lp = (ActionBar.LayoutParams) view.getLayoutParams();
//            lp.gravity = Gravity.HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
//            getSupportActionBar().setCustomView(view, lp);
//        }


    }


    /**
     * @param groupPosition 组元素的位置
     * @param isChecked     组元素的选中与否
     *                      思路:组元素被选中了，那么下辖全部的子元素也被选中
     */
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            child.get(i).setChoosed(isChecked);
        }
        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @return 判断组元素是否全选
     */
    private boolean isCheckAll() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param isChecked     子元素的选中与否
     */
    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true; //判断该组下面的所有子元素是否处于同一状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        for (int i = 0; i < child.size(); i++) {
            //不选全中
            if (child.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }

        if (allChildSameState) {
            group.setChoosed(isChecked);//如果子元素状态相同，那么对应的组元素也设置成这一种的同一状态
        } else {
            group.setChoosed(false);//否则一律视为未选中
        }

        if (isCheckAll()) {
            allCheckBox.setChecked(true);//全选
        } else {
            allCheckBox.setChecked(false);//反选
        }

        adapter.notifyDataSetChanged();
        calulate();

    }

    /**
     * 跳转到店铺页
     *
     * @param dianpuid
     */
    @Override
    public void IntentGroup(String dianpuid) {
        NewConstants.refeshFlag = "0";
        Intent intent = new Intent(getActivity(), NewDianpuActivity.class);
        intent.putExtra("dianpuid", dianpuid);
        startActivity(intent);
    }

    /**
     * 增加购物车数量，最高99
     *
     * @param groupPosition 组元素的位置
     * @param childPosition 子元素的位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     * @param id
     * @param num
     * @param guige
     */
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getNum();
        if (count == 99) {
            StringUtil.showToast(mcontext, "不能再增加了哦");
            return;
        }
        count++;
        good.setNum(count);
        ((TextView) showCountView).setText(String.valueOf(count));
        DialogSingleUtil.show(getActivity());
        doShoppingCart(id, "3", 1 + "", guige, "add");
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 减少购物车数量，最低1
     *
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getNum();
        if (count == 1) {
            StringUtil.showToast(mcontext, "不能再减少了哦");
            return;
        }
        count--;
        good.setNum(count);
        ((TextView) showCountView).setText("" + count);
        DialogSingleUtil.show(getActivity());
        doShoppingCart(id, "4", 1 + "", guige, "dec");
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 删除购物车商品
     *
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition, String id, int num, String guige) {
        DialogSingleUtil.show(getActivity());
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        child.remove(childPosition);
        if (child.size() == 0) {
            groups.remove(groupPosition);
        }
        Logg.e(id + "=====" + num + "====" + guige);
        doShoppingCart(id, "-1", num + "", guige, "child");
        adapter.notifyDataSetChanged();
        calulate();


    }

    /**
     * 跳转到商品详情页
     *
     * @param id
     */
    @Override
    public void Intent(String id) {
        NewConstants.refeshFlag = "0";
        Intent intent = new Intent(getActivity(), ShopDetailActivty.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    /**
     * 直接填数量时价格刷新
     *
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     * @param id
     * @param num
     * @param guige
     */
    @Override
    public void Refresh(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige, String dialognum) {
//        Logg.e("------------------------>>>>>");
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        if (num <= 0) {
            StringUtil.showToast(getActivity(), "数量超出范围");
            doShoppingCart(id, "5", dialognum, guige, "");
            good.setNum(1);
            adapter.notifyDataSetChanged();
        } else if (num > 99) {
            StringUtil.showToast(getActivity(), "数量超出范围");
            doShoppingCart(id, "5", dialognum, guige, "");
            good.setNum(99);
            adapter.notifyDataSetChanged();
        } else {
            Logg.e(NewConstants.car);
            good.setNum(num);
            doShoppingCart(id, "5", dialognum, guige, "");
        }
        calulate();
    }

    public void doUpdate(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        int count = good.getNum();
        UtilsLog.i("进行更新数据，数量" + count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();


    }

    @Override
    public void groupEditor(int groupPosition) {

    }

    @OnClick({R.id.all_checkBox, R.id.go_pay, R.id.share_goods, R.id.collect_goods, R.id.del_goods})
    public void onClick(View view) {
        AlertDialog dialog;
        switch (view.getId()) {
            case R.id.all_checkBox:
                doCheckAll();
                break;
            case R.id.go_pay:
                if (mtotalCount == 0) {
                    StringUtil.showToast(mcontext, "请选择要支付的商品");
                    return;
                }
//                dialog = new AlertDialog.Builder(mcontext).create();
//                dialog.setMessage("总计:" + mtotalCount + "种商品，" +   doubleToString(mtotalPrice) + "元");
//                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "支付", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        NewConstants.refeshFlag = "0";
//                        Intent intent = new Intent(CarActivity.this,ConfirmOrderActivity.class);
//                        intent.putExtra("ids",ids);
//                        intent.putExtra("nums",nums);
//                        intent.putExtra("guiges",guiges);
//                        startActivity(intent);
//                        return;
//                    }
//                });
//                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                dialog.show();
                new com.bbk.dialog.AlertDialog(mcontext).builder().setTitle("提示")
                        .setMsg("总计:" + mtotalCount + "种商品，" + doubleToString(mtotalPrice) + "元")
                        .setPositiveButton("支付", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewConstants.refeshFlag = "0";
                                Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
                                intent.putExtra("ids", ids);
                                intent.putExtra("nums", nums);
                                intent.putExtra("guiges", guiges);
                                startActivity(intent);
                                return;
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                }).show();
                break;
            case R.id.share_goods:
                if (mtotalCount == 0) {
                    StringUtil.showToast(mcontext, "请选择要分享的商品");
                    return;
                }
                StringUtil.showToast(mcontext, "分享成功");
                break;
            case R.id.collect_goods:
                if (mtotalCount == 0) {
                    StringUtil.showToast(mcontext, "请选择要收藏的商品");
                    return;
                }
                StringUtil.showToast(mcontext, "收藏成功");
                break;
            case R.id.del_goods:
                if (mtotalCount == 0) {
                    StringUtil.showToast(mcontext, "请选择要删除的商品");
                    return;
                }
//                dialog = new AlertDialog.Builder(mcontext).create();
//                dialog.setMessage("确认要删除该商品吗?");
//                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doDelete();
//                    }
//                });
//                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                dialog.show();
                new com.bbk.dialog.AlertDialog(mcontext).builder().setTitle("提示")
                        .setMsg("确认要删除该商品吗?")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                doDelete();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        return;
                    }
                }).show();
                break;
//            case R.id.actionBar_edit:
//                flag = !flag;
//                setActionBarEditor();
//                setVisiable();
//                break;
        }
    }

    /**
     * ActionBar标题上点编辑的时候，只显示每一个店铺的商品修改界面
     * ActionBar标题上点完成的时候，只显示每一个店铺的商品信息界面
     */
    private void setActionBarEditor() {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isActionBarEditor()) {
                group.setActionBarEditor(false);
            } else {
                group.setActionBarEditor(true);
            }
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            group.setChoosed(allCheckBox.isChecked());
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                child.get(j).setChoosed(allCheckBox.isChecked());//这里出现过错误
            }
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        List<String> list = new ArrayList<>();
        List<String> listNum = new ArrayList<>();
        List<String> listguiges = new ArrayList<>();
        mtotalPrice = 0.00;
        mtotalCount = 0;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> child = childs.get(group.getId());
            for (int j = 0; j < child.size(); j++) {
                GoodsInfo good = child.get(j);
                if (good.isChoosed()) {
                    list.add(good.getId());
                    listNum.add(String.valueOf(good.getNum()));
                    listguiges.add(good.getGuige());
                    mtotalCount++;
                    mtotalPrice += good.getPrice() * good.getNum();
                }
            }
        }
        Logg.json(list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", ""));
        Logg.json(listNum.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", ""));
        Logg.json(listguiges.toString().replace("[", "").replace("]", "").replace(",", "|"));
        ids = list.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
        nums = listNum.toString().replace("[", "").replace("]", "").replace(",", "|").replace(" ", "");
        guiges = listguiges.toString().replace("[", "").replace("]", "").replace(",", "|");
        totalPrice.setText("￥" + doubleToString(mtotalPrice) + "");
        goPay.setText("去支付(" + mtotalCount + ")");
        if (mtotalCount == 0) {
            setCartNum();
        } else {
//            titleText.setText("购物车(" + mtotalCount + ")");
        }


    }

    /**
     * double转String,保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    private void setVisiable() {
        if (flag) {
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            titleText2.setText("完成");
        } else {
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            titleText2.setText("编辑");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter = null;
        if (childs != null) {
            childs.clear();
        }
        if (groups != null) {
            groups.clear();
        }
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }

    @OnClick({R.id.title_back_btn, R.id.title_text2, R.id.ll_refresh_car, R.id.ll_third_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
//                finish();
                break;
            case R.id.title_text2:
                flag = !flag;
//                setActionBarEditor();
                setVisiable();
                break;
            case R.id.ll_refresh_car:
//                RotateAnimation ra = new RotateAnimation(0, 360);
                //还可指明绕着某一点旋转,RotateAnimation(0,360,x坐标,y坐标)
//                RotateAnimation ra = new RotateAnimation(0,360,200,200);
//                使用相对坐标
                RotateAnimation ra = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                ra.setDuration(1000);
                imgRefresh.startAnimation(ra);
                if (curPosition == 2) {
                    getShoppingCartUrlByDomain("jd");
                } else {
                    getShoppingCartUrlByDomain("taobao");
                }
                break;
            case R.id.ll_third_car:
                if (curPosition == 2) {
                    getShoppingCartUrlByDomainAndIntent("jd");
                } else {
                    getShoppingCartUrlByDomainAndIntent("taobao");
                }
                break;
        }
    }

    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status) {
            if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
            } else {
                mKelperTask = null;
            }
        }
    };

    /**
     * 打开指定链接
     */
    public void showUrl(String url) {
        Handler handler = new Handler();
        alibcShowParams = new AlibcShowParams(OpenType.Native, false);
        alibcShowParams.setClientType("taobao_scheme");
        exParams = new HashMap<>();
        exParams.put("isv_code", "appisvcode");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        final String text = url;
        if (TextUtils.isEmpty(text)) {
            StringUtil.showToast(getActivity(), "URL为空");
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogSingleUtil.dismiss(0);
                AlibcTrade.show(getActivity(), new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
            }
        }, 0);
    }

    /**
     * 根据id查购物车内容
     */
    private void queryShoppingCartByUserid() {
        Map<String, String> maps = new HashMap<String, String>();
        final String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        RetrofitClient.getInstance(getActivity()).createBaseApi().queryShoppingCartByUserid(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                JSONObject jsonObject1 = new JSONObject(content);
                                String list = jsonObject1.optString("list");
                                Logg.json(list);
                                mcontext = getActivity();
                                groups = new ArrayList<StoreInfo>();
                                childs = new HashMap<String, List<GoodsInfo>>();
                                List<GoodsInfo1> goods = JSON.parseArray(list, GoodsInfo1.class);
                                if (goods != null && goods.size() > 0) {
                                    listView.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                    llBottomCar.setVisibility(View.VISIBLE);
                                    titleText2.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < goods.size(); i++) {
                                        groups.add(new StoreInfo(goods.get(i).getDianpuid(), goods.get(i).getDianpu()));
                                        for (int j = 0; j <= i; j++) {
//                                        Logg.json(goods.get(i).getList());
                                            List<GoodsInfo> goods1 = JSON.parseArray(goods.get(i).getList(), GoodsInfo.class);
                                            childs.put(groups.get(i).getId(), goods1);
                                        }
                                    }
                                    initEvents();
                                    setCartNum();
                                } else {
                                    listView.setVisibility(View.GONE);
                                    progress.setVisibility(View.VISIBLE);
                                    llBottomCar.setVisibility(View.GONE);
                                    titleText2.setVisibility(View.GONE);
                                    titleText.setText("购物车");
                                    if (TextUtils.isEmpty(userID)) {
                                        progress.loadHomeSuccess(getActivity(), "登录后才能使用购物车哦", "去登录", true);
                                    } else {
                                        progress.loadSuccess(true);
                                    }
                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        mPtrframe.finishRefresh();
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        llBottomCar.setVisibility(View.GONE);
                        titleText2.setVisibility(View.GONE);
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 购物车操作
     * type
     * 3表示增加数量
     * 4表示减少数量
     * -1表示删除物品
     * 1表示添加物品
     */
    private void doShoppingCart(String productid, String type, String number, String guige, final String flag) {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("productid", productid);
        maps.put("type", type);
        maps.put("number", number);
        maps.put("guige", guige);
        RetrofitClient.getInstance(getActivity()).createBaseApi().doShoppingCart(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                if (flag.equals("all")) {
                                    queryShoppingCartByUserid();
                                } else {

                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        if (curPosition == 0) {
//            showTaoBaoCar();
            sum = 0;
            DialogCheckYouhuiUtil.show(getActivity(), "小鲸正在努力同步中，请您耐心等等哦！");
            getShoppingCartUrlByDomain("taobao");
        } else if (curPosition == 2) {
//            showJdCar();
            sum = 0;
            DialogCheckYouhuiUtil.show(getActivity(), "小鲸正在努力同步中，请您耐心等等哦！");
            getShoppingCartUrlByDomain("jd");
        } else {
            DialogSingleUtil.show(getActivity());
            queryShoppingCartByUserid();
        }
    }

    @Override
    protected void loadLazyData() {
        NewConstants.refeshFlag = "0";
        llBottomCar.setVisibility(View.GONE);
        titleText2.setVisibility(View.GONE);
//        llBottomTbCar.setVisibility(View.VISIBLE);
        titleText.setText("淘宝购物车");
        showTaoBaoCar();
    }

    /**
     * 购物车显示方法
     */
    private void showTaoBaoCar() {
        mPtrframe.finishRefresh();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (TextUtils.isEmpty(userID)) {
            progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            llBottomTbCar.setVisibility(View.GONE);
            tishi.setVisibility(View.GONE);
            imgTishi.setVisibility(View.GONE);
            progress.loadHomeSuccess(getActivity(), "登录后才能使用购物车哦", "去登录", true);
            DialogSingleUtil.dismiss(0);
        } else {
            if (!AlibcLogin.getInstance().isLogin()) {
                listView.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                progress.loadCarSuccess(getActivity(), "登陆后可一键同步淘宝购物车内商品", "授权登陆淘宝", true);
                llBottomTbCar.setVisibility(View.GONE);
                tishi.setVisibility(View.GONE);
                imgTishi.setVisibility(View.GONE);
                DialogSingleUtil.dismiss(0);
            } else {
                llBottomTbCar.setVisibility(View.VISIBLE);
                tishi.setVisibility(View.VISIBLE);
//                imgTishi.setVisibility(View.VISIBLE);
                String homeContent = SharedPreferencesUtil.getSharedData(getActivity(), "homeTbCarContent", "homeTbCarContent");
                try {
                    JSONObject object = new JSONObject(homeContent);
                    if (object.length() > 0) {
                        taoBaoCarBeans = JSON.parseArray(object.optString("content"), TaoBaoCarBean.class);
                        if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            getAllNum();
                            DialogSingleUtil.dismiss(0);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            progress.setVisibility(View.VISIBLE);
                            progress.loadSuccess(true);
                            tishi.setVisibility(View.GONE);
                            imgTishi.setVisibility(View.GONE);
                            DialogSingleUtil.dismiss(0);
                        }
                    } else {
                        synchroShoppingCart(content, "taobao");
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 京东购物车显示方法
     */
    private void showJdCar() {
        mPtrframe.finishRefresh();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (TextUtils.isEmpty(userID)) {
            progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            llBottomTbCar.setVisibility(View.GONE);
            tishi.setVisibility(View.GONE);
            imgTishi.setVisibility(View.GONE);
            progress.loadHomeSuccess(getActivity(), "登录后才能使用购物车哦", "去登录", true);
            DialogSingleUtil.dismiss(0);
        } else {
            KeplerApiManager.getWebViewService().checkLoginState(new ActionCallBck() {
                @Override
                public boolean onDateCall(int key, String info) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logg.e("已登陆京东");
                            llBottomTbCar.setVisibility(View.VISIBLE);
                            tishi.setVisibility(View.VISIBLE);
//                            imgTishi.setVisibility(View.VISIBLE);
                            String homeJdContent = SharedPreferencesUtil.getSharedData(getActivity(), "homeJdCarContent", "homeJdCarContent");
                            try {
                                JSONObject object = new JSONObject(homeJdContent);
                                if (object.length() > 0) {
                                    taoBaoCarBeans = JSON.parseArray(object.optString("content"), TaoBaoCarBean.class);
                                    if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        progress.setVisibility(View.GONE);
                                        getAllNum();
                                        DialogSingleUtil.dismiss(0);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        tishi.setVisibility(View.GONE);
                                        imgTishi.setVisibility(View.GONE);
                                        DialogSingleUtil.dismiss(0);
                                    }
                                } else {
                                    synchroShoppingCart(content, "jd");
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    return false;
                }

                @Override
                public boolean onErrCall(int key, String error) {
                    Logg.e("未登陆京东");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setVisibility(View.GONE);
                            progress.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            progress.loadCarSuccess(getActivity(), "登陆后可一键同步京东购物车内商品", "授权登陆京东", true);
                            llBottomTbCar.setVisibility(View.GONE);
                            tishi.setVisibility(View.GONE);
                            imgTishi.setVisibility(View.GONE);
                            DialogSingleUtil.dismiss(0);
                        }
                    });
                    return false;
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 调用验证登录态接口判断是否登录状态
     */
    private void checkLoginStatus() {
        KeplerApiManager.getWebViewService().checkLoginState(new ActionCallBck() {
            @Override
            public boolean onDateCall(int key, String info) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        login_lin.setVisibility(View.VISIBLE);
                    }
                });
                return false;
            }

            @Override
            public boolean onErrCall(int key, String error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        login_lin.setVisibility(View.INVISIBLE);
                    }
                });
                return false;
            }
        });

    }

    LoginListener mLoginListener = new LoginListener() {

        @Override
        public void authSuccess(final Object token) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    StringUtil.showToast(getActivity(), "登录成功");
                    checkLoginStatus();
                    llBottomTbCar.setVisibility(View.VISIBLE);
                    getShoppingCartUrlByDomain("jd");
                }
            });

        }

        @Override
        public void authFailed(final int errorCode) {
            switch (errorCode) {
                case KeplerApiManager.KeplerApiManagerLoginErr_Init:// 初始化失败
                    break;
                case KeplerApiManager.KeplerApiManagerLoginErr_InitIng:// 初始化没有完成
                    break;
                case KeplerApiManager.KeplerApiManagerLoginErr_openH5authPageURLSettingNull:// 跳转url
                    break;
                case KeplerApiManager.KeplerApiManagerLoginErr_getTokenErr:// 获取失败(oath授权之后，获取cookie过程出错)
                    break;
                case KeplerApiManager.KeplerApiManagerLoginErr_User_Cancel:// 用户取消
                    break;
                case KeplerApiManager.KeplerApiManagerLoginErr_AuthErr_ActivityOpen:// 打开授权页面失败
                    break;
                default:
                    break;
            }
            if (!(errorCode == KeplerApiManager.KeplerApiManagerLoginErr_LoginNoConfirm ||
                    errorCode == KeplerApiManager.KeplerApiManagerLoginErr_TokenNoUse)) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(getActivity(), errorCode + ":authFailed", Toast.LENGTH_SHORT).show();
                        StringUtil.showToast(getActivity(), "登录失败");
                    }
                });
            }
        }
    };

    /**
     * 登陆淘宝
     */
    @Override
    public void doLog() {
        if (curPosition == 2) {
            KeplerApiManager.getWebViewService().checkLoginState(new ActionCallBck() {
                @Override
                public boolean onDateCall(int key, String info) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            StringUtil.showToast(getActivity(), "已登录");
                        }
                    });
                    return false;
                }

                @Override
                public boolean onErrCall(int key, String error) {
                    // 直接授权登录京东​
                    KeplerApiManager.getWebViewService().login(
                            getActivity(), mLoginListener);
                    return false;
                }
            });
        } else {
            if (!AlibcLogin.getInstance().isLogin()) {
                DialogSingleUtil.show(getActivity(), "授权中...");
                final AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(getActivity(), new AlibcLoginCallback() {
                    @Override
                    public void onSuccess() {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), "登录成功 ");
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date = sDateFormat.format(new Date());
                        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "taobao", "taobaodata", date);
                        getShoppingCartUrlByDomain("taobao");

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), "登录失败 ");
                    }
                });
            } else {
                StringUtil.showToast(getActivity(), "已经登陆过了");
            }
        }
    }


    private void getShoppingCartUrlByDomain(String domain) {
        cancelCheck = true;
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("domain", domain);
        RetrofitClient.getInstance(getActivity()).createBaseApi().getShoppingCartUrlByDomain(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            jsonObject = new JSONObject(s);
                            Logg.json("jdurl" + jsonObject);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject.optString("content"));
//                                new Thread(networkTask).start();
                                init(jsonObject.optString("content"));
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
//                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogCheckYouhuiUtil.show(getActivity(), "小鲸正在努力同步中，请您耐心等等哦！");
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 跳转购物车
     *
     * @param domain
     */
    private void getShoppingCartUrlByDomainAndIntent(String domain) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("domain", domain);
        RetrofitClient.getInstance(getActivity()).createBaseApi().getShoppingCartUrlByDomain(
                maps, new BaseObserver<String>(getActivity()) {
                    @Override
                    public void onNext(String s) {
                        try {
                            jsonObject = new JSONObject(s);
                            Logg.json("jdurl" + jsonObject);
                            if (jsonObject.optString("status").equals("1")) {
//                                init(jsonObject.optString("content"));
                                if (curPosition == 2) {
                                    try {
                                        KeplerApiManager.getWebViewService().openJDUrlPage(jsonObject.optString("content"), mKeplerAttachParameter, getActivity(), mOpenAppAction, 1500);
                                    } catch (KeplerBufferOverflowException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showUrl(jsonObject.optString("content"));
                                }
                            } else {
                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(getActivity());
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), e.message);
                    }
                });
    }

    /**
     * 获取淘宝购物车信息
     *
     * @param content
     */
    private void synchroShoppingCart(String content, String domain) {
        Map<String, String> maps = new HashMap<String, String>();
        Logg.json(content);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("domain", domain);
        maps.put("client", "android");
        maps.put("content", content);
        dataFlow.requestData(1, "newService/synchroShoppingCart", maps, this, false);
//        RetrofitClient.getInstance(getActivity()).createBaseApi().synchroShoppingCart(
//                maps, new BaseObserver<String>(getActivity()) {
//                    @Override
//                    public void onNext(String s) {
//                        try {
//                            final JSONObject jsonObject = new JSONObject(s);
//                            if (jsonObject.optString("status").equals("1")) {
////                                Logg.json("=========>>>>",jsonObject);
//                                taoBaoCarBeans = JSON.parseArray(jsonObject.optString("content"), TaoBaoCarBean.class);
//                                if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
//                                    recyclerView.setVisibility(View.VISIBLE);
//                                    progress.setVisibility(View.GONE);
//                                    TaoBaoAdapter taoBaoAdapter = new TaoBaoAdapter(getActivity(), taoBaoCarBeans);
//                                    recyclerView.setAdapter(taoBaoAdapter);
//                                } else {
//                                    recyclerView.setVisibility(View.GONE);
//                                    progress.setVisibility(View.VISIBLE);
//                                    progress.loadSuccess(true);
//                                }
//                            } else {
//                                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    protected void hideDialog() {
//                        DialogSingleUtil.dismiss(0);
//                    }
//
//                    @Override
//                    protected void showDialog() {
//                        DialogSingleUtil.show(getActivity());
//                    }
//
//                    @Override
//                    public void onError(ExceptionHandle.ResponeThrowable e) {
//                        DialogSingleUtil.dismiss(0);
//                        StringUtil.showToast(getActivity(), e.message);
//                    }
//                });
    }

//    /**
//     * 网络操作相关的子线程
//     */
//    Runnable networkTask = new Runnable() {
//        @Override
//        public void run() {
//            // 在这里进行 http request.网络请求相关操作
//            try {
////                content = HtmlRequest.getHtml(jsonObject.optString("content"));
////                Logg.json(content);
//                handler.sendEmptyMessageDelayed(0, 2000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    try {
////                        synchroShoppingCart(content);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    break;
//            }
//        }
//    };


    @SuppressLint("SetJavaScriptEnabled")
    private void init(String url) {
        // 开启JavaScript支持
        tbWebview.getSettings().setJavaScriptEnabled(true);

        tbWebview.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");

        // 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放
        tbWebview.getSettings().setSupportZoom(true);

        // 设置WebView是否使用其内置的变焦机制，该机制集合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
        tbWebview.getSettings().setBuiltInZoomControls(true);

        // 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
        tbWebview.getSettings().setDomStorageEnabled(true);

        // 触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        tbWebview.requestFocus();

        // 设置此属性,可任意比例缩放,设置webview推荐使用的窗口
        tbWebview.getSettings().setUseWideViewPort(true);

        // 设置webview加载的页面的模式,缩放至屏幕的大小
        tbWebview.getSettings().setLoadWithOverviewMode(true);

        // 加载链接
        tbWebview.loadUrl(url);

        tbWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // 在开始加载网页时会回调
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 拦截 url 跳转,在里边添加点击链接跳转或者操作
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 在结束加载网页时会回调

                // 获取页面内容
                view.loadUrl("javascript:window.java_obj.showSource("
                        + "document.getElementsByTagName('html')[0].innerHTML);");

                // 获取解析<meta name="share-description" content="获取到的值">
                view.loadUrl("javascript:window.java_obj.showDescription("
                        + "document.querySelector('meta[name=\"share-description\"]').getAttribute('content')"
                        + ");");
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                // 在每一次请求资源时，都会通过这个函数来回调
                return super.shouldInterceptRequest(view, request);
            }

        });
    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject jsonObject, String content) {
//        final JSONObject jsonObject;
        try {
            if (jsonObject.optString("status").equals("1")) {
                mPtrframe.finishRefresh();
                Logg.json("=========>>>>", jsonObject);
                if (cancelCheck) {
                    if (curPosition == 2) {
                        SharedPreferencesUtil.putSharedData(getActivity(), "homeJdCarContent", "homeJdCarContent", jsonObject.toString());
                    } else {
                        SharedPreferencesUtil.putSharedData(getActivity(), "homeTbCarContent", "homeTbCarContent", jsonObject.toString());
                    }
                    taoBaoCarBeans = JSON.parseArray(jsonObject.optString("content"), TaoBaoCarBean.class);
                    if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        tishi.setVisibility(View.VISIBLE);
//                        imgTishi.setVisibility(View.VISIBLE);
                        getAllNum();
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(getActivity(), "已同步最新宝贝数据");
                        llBottomTbCar.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadSuccess(true);
                        tishi.setVisibility(View.GONE);
                        imgTishi.setVisibility(View.GONE);
                    }
                }
            } else {
                StringUtil.showToast(getActivity(), jsonObject.optString("errmsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新购物车
     */
//    @OnClick(R.id.tv_tb_car)
//    public void onViewClicked() {
//        sum = 0;
//        if (curPosition == 2) {
//            getShoppingCartUrlByDomain("jd");
//        } else {
//            getShoppingCartUrlByDomain("taobao");
//        }
//    }

    /**
     * 获取总的数量
     */
    private void getAllNum() {
        List<String> arr = new ArrayList<>();
        List<String> arrjdzy = new ArrayList<>();
        List<String> arrjdcs = new ArrayList<>();
        for (int i = 0; i < taoBaoCarBeans.size(); i++) {
            taobaoCarListBeans = JSON.parseArray(taoBaoCarBeans.get(i).getList(), TaobaoCarListBean.class);
            arr.add(String.valueOf(taobaoCarListBeans.size()));
            if (taoBaoCarBeans.get(i).getName().equals("京东自营")) {
                try {
                    JSONArray jsonArray = new JSONArray(taoBaoCarBeans.get(i).getList());
                    List<TaobaoCarListBean> jdzyCarListBeans = JSON.parseArray(taoBaoCarBeans.get(i).getList(), TaobaoCarListBean.class);
                    Logg.json(jdzyCarListBeans.size() + "====" + jsonArray);
                    for (int j = 0; j < jdzyCarListBeans.size(); j++) {
                        Logg.json("isma" + jdzyCarListBeans.get(j).getIsJDMarket());
                        if (jdzyCarListBeans.get(j).getIsJDMarket().equals("1")) {
                            arrjdcs.add(jdzyCarListBeans.get(j).getIsJDMarket());
                        } else if (jdzyCarListBeans.get(j).getIsJDMarket().equals("0")) {
                            arrjdzy.add(jdzyCarListBeans.get(j).getIsJDMarket());
                        }
                    }
                    NewConstants.jdcsNum = arrjdcs.size();
                    NewConstants.jdzyNum = arrjdzy.size();
//                    Logg.json(arrjdcs.size()+"==============="+arrjdzy.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < arr.size(); i++) {
//            sum = sum+Integer.parseInt(arr.get(i));//通过for循环，去除数组中的元素，累加到sum中
        }
        if (curPosition == 2) {
            sum = taoBaoCarBeans.size() + NewConstants.jdcsNum + NewConstants.jdzyNum - 1;
            TaoBaoAdapter taoBaoAdapter = new TaoBaoAdapter(getActivity(), taoBaoCarBeans, "jd", sum);
            recyclerView.setAdapter(taoBaoAdapter);
        } else {
            sum = taoBaoCarBeans.size();
            TaoBaoAdapter taoBaoAdapter = new TaoBaoAdapter(getActivity(), taoBaoCarBeans, "taobao", sum);
            recyclerView.setAdapter(taoBaoAdapter);
        }
    }

    @Override
    public void timeOut() {
        DialogCheckYouhuiUtil.dismiss(0);
        progress.setVisibility(View.VISIBLE);
        progress.loadError();
        recyclerView.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        mPtrframe.finishRefresh();
        llBottomTbCar.setVisibility(View.GONE);
        llBottomCar.setVisibility(View.GONE);
        StringUtil.showToast(getActivity(), "连接超时");
    }

    @Override
    public void loadFail() {

    }


    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            System.out.println("====>html=" + html);
            content = html;
            if (curPosition == 2) {
                synchroShoppingCart(content, "jd");
            } else {
                synchroShoppingCart(content, "taobao");
            }
        }

        @JavascriptInterface
        public void showDescription(String str) {
            System.out.println("====>html=" + str);
        }
    }

}
