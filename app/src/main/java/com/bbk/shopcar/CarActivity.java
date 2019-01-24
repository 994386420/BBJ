package com.bbk.shopcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.adapter.TaoBaoAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlowTaobao;
import com.bbk.flow.ResultEvent;
import com.bbk.fragment.CarFrament;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.resource.NewConstants;
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
import com.bbk.view.X5CarWebView;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 购物车
 */
public class CarActivity extends BaseActivity implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener, CommonLoadingView.LoadingHandler, CommonLoadingView.LogThird, ResultEvent, DataFlowTaobao.loadInterface {
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
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_text2)
    TextView titleText2;
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
    public static TextView tvWeiTz;
    @BindView(R.id.ll_third_car)
    LinearLayout llThirdCar;
    @BindView(R.id.ll_refresh_car)
    LinearLayout llRefreshCar;
    @BindView(R.id.img_refresh)
    ImageView imgRefresh;
    public static SmartRefreshLayout mPtrframe;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;
    private List<StoreInfo> groups; //组元素的列表
    private Map<String, List<GoodsInfo>> childs; //子元素的列表
    private String ids, nums, guiges;
    private int carCount;
    private JSONObject jsonObject;
    private String content;
    private List<TaoBaoCarBean> taoBaoCarBeans;
    private DataFlowTaobao dataFlow;
    private int curPosition = 0;
    public static boolean cancelCheck = true;// 是否取消查询
    Handler mHandler;
    List<TaobaoCarListBean> taobaoCarListBeans;
    private int sum = 0;//定义一个变量
    private AlibcShowParams alibcShowParams;//页面打开方式，默认，H5，Native
    private Map<String, String> exParams;//yhhpass参数
    KelperTask mKelperTask;
    /**
     * webview加载超时处理
     */
    private Timer mTimer;
    private TimerTask mTimerTask;
    private final int TIMEOUT = 10000;
    private final int TIMEOUT_ERROR = 9527;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcar_home_layout);
        mHandler = new Handler();
        dataFlow = new DataFlowTaobao(this);
        dataFlow.setLoadInterface(this);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View topView = findViewById(R.id.activity_car);
        topView.setBackgroundResource(R.color.white);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        NewConstants.refeshFlag = "0";
        mPtrframe = findViewById(R.id.mPtrframe);
        llBottomCar.setVisibility(View.GONE);
        titleText2.setVisibility(View.GONE);
        titleText.setText("淘宝购物车");
        tvWeiTz = findViewById(R.id.tv_wei_tz);
        showTaoBaoCar();
        initPtrFrame();
        initActionBar();
        titleText2.setText("编辑");
        progress.setLoadingHandler(this);
        progress.setLogThird(this);
        titleBackBtn.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        getShoppingCartShowList();
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
//                    showJdCar();
                    getShoppingCartUrlByDomain("jd");
                } else {
                    sum = 0;
//                    showTaoBaoCar();
                    getShoppingCartUrlByDomain("taobao");
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
        adapter = new ShopcatAdapter(this, groups, childs, mcontext);
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
                titleText.setText("鲸城购物车(" + count + ")");
            }
        }

    }

    private void clearCart() {
        titleText.setText("鲸城购物车(0)");
        llCart.setVisibility(View.VISIBLE);
    }


    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        DialogSingleUtil.show(CarActivity.this);
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
        Intent intent = new Intent(this, NewDianpuActivity.class);
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
        DialogSingleUtil.show(this);
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
        DialogSingleUtil.show(CarActivity.this);
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
        DialogSingleUtil.show(this);
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
        Intent intent = new Intent(this, ShopDetailActivty.class);
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
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        if (num <= 0) {
            StringUtil.showToast(this, "数量超出范围");
            doShoppingCart(id, "5", dialognum, guige, "");
            good.setNum(1);
            adapter.notifyDataSetChanged();
        } else if (num > 99) {
            StringUtil.showToast(this, "数量超出范围");
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
                                Intent intent = new Intent(CarActivity.this, ConfirmOrderActivity.class);
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

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.title_back_btn, R.id.title_text2, R.id.ll_refresh_car, R.id.ll_third_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.title_text2:
                flag = !flag;
//                setActionBarEditor();
                setVisiable();
                break;
            case R.id.ll_refresh_car:
                RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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

    /**
     * 跳转购物车
     *
     * @param domain
     */
    private void getShoppingCartUrlByDomainAndIntent(String domain) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("domain", domain);
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().getShoppingCartUrlByDomain(
                maps, new BaseObserver<String>(CarActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            jsonObject = new JSONObject(s);
                            Logg.json("jdurl" + jsonObject);
                            if (jsonObject.optString("status").equals("1")) {
//                                init(jsonObject.optString("content"));
                                if (curPosition == 2) {
                                    try {
                                        KeplerApiManager.getWebViewService().openJDUrlPage(jsonObject.optString("content"), mKeplerAttachParameter, CarActivity.this, mOpenAppAction, 1500);
                                    } catch (KeplerBufferOverflowException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showUrl(jsonObject.optString("content"));
                                }
                            } else {
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(CarActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, e.message);
                    }
                });
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
            StringUtil.showToast(CarActivity.this, "URL为空");
            return;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogSingleUtil.dismiss(0);
                AlibcTrade.show(CarActivity.this, new AlibcPage(text), alibcShowParams, null, exParams, new DemoTradeCallback());
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
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().queryShoppingCartByUserid(
                maps, new BaseObserver<String>(CarActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                String content = jsonObject.optString("content");
                                JSONObject jsonObject1 = new JSONObject(content);
                                String list = jsonObject1.optString("list");
                                Logg.json(list);
                                mcontext = CarActivity.this;
                                groups = new ArrayList<StoreInfo>();
                                childs = new HashMap<String, List<GoodsInfo>>();
                                List<GoodsInfo1> goods = JSON.parseArray(list, GoodsInfo1.class);
                                if (goods != null && goods.size() > 0) {
                                    listView.setVisibility(View.VISIBLE);
                                    progress.loadSuccess();
                                    llBottomCar.setVisibility(View.VISIBLE);
                                    titleText2.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < goods.size(); i++) {
                                        groups.add(new StoreInfo(goods.get(i).getDianpuid(), goods.get(i).getDianpu(), goods.get(i).getDianpuyouhui()));
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
                                    titleText.setText("鲸城购物车");
                                    if (TextUtils.isEmpty(userID)) {
                                        progress.loadHomeSuccess(CarActivity.this, "登录后才能使用购物车哦", "去登录", true);
                                    } else {
                                        progress.loadSuccess(true);
                                    }
                                }
                            } else {
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
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
                        StringUtil.showToast(CarActivity.this, e.message);
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
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().doShoppingCart(
                maps, new BaseObserver<String>(CarActivity.this) {
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
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
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
                        StringUtil.showToast(CarActivity.this, e.message);
                    }
                });
    }

    /**
     * 请求失败重试
     */
    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        if (curPosition == 0) {
            sum = 0;
            DialogCheckYouhuiUtil.show(CarActivity.this, "小鲸正在努力同步中，请您耐心等等哦！");
            getShoppingCartUrlByDomain("taobao");
        } else if (curPosition == 2) {
            sum = 0;
            DialogCheckYouhuiUtil.show(CarActivity.this, "小鲸正在努力同步中，请您耐心等等哦！");
            getShoppingCartUrlByDomain("jd");
        } else {
            DialogSingleUtil.show(CarActivity.this);
            queryShoppingCartByUserid();
        }
    }


    /**
     * 获取总的数量
     */
    private void getAllNum() {
//        List<String> arr = new ArrayList<>();
        List<String> arrjdzy = new ArrayList<>();
        List<String> arrjdcs = new ArrayList<>();
        for (int i = 0; i < taoBaoCarBeans.size(); i++) {
            taobaoCarListBeans = JSON.parseArray(taoBaoCarBeans.get(i).getList(), TaobaoCarListBean.class);
//            arr.add(String.valueOf(taobaoCarListBeans.size()));
            if (taoBaoCarBeans.get(i).getName() != null&&taoBaoCarBeans.get(i).getName().equals("京东自营")) {
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
        if (curPosition == 2) {
            sum = taoBaoCarBeans.size() + NewConstants.jdcsNum + NewConstants.jdzyNum - 1;
            TaoBaoAdapter taoBaoAdapter = new TaoBaoAdapter(CarActivity.this, taoBaoCarBeans, "jd", sum);
            recyclerView.setAdapter(taoBaoAdapter);
        } else {
            sum = taoBaoCarBeans.size();
            TaoBaoAdapter taoBaoAdapter = new TaoBaoAdapter(CarActivity.this, taoBaoCarBeans, "taobao", sum);
            recyclerView.setAdapter(taoBaoAdapter);
        }
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
            progress.loadHomeSuccess(CarActivity.this, "登录后才能使用购物车哦", "去登录", true);
            DialogSingleUtil.dismiss(0);
        } else {
            if (!AlibcLogin.getInstance().isLogin()) {
                listView.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tishi.setVisibility(View.GONE);
                progress.loadCarSuccess(CarActivity.this, "登陆后可一键同步淘宝购物车内商品", "授权登陆淘宝", true);
                llBottomTbCar.setVisibility(View.GONE);
                DialogSingleUtil.dismiss(0);
            } else {
                llBottomTbCar.setVisibility(View.VISIBLE);
                tishi.setVisibility(View.VISIBLE);
                String homeContent = SharedPreferencesUtil.getSharedData(CarActivity.this, "homeTbCarContent", "homeTbCarContent");
                try {
                    if (homeContent != null && !homeContent.equals("")) {
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
                                DialogSingleUtil.dismiss(0);
                            }
                        } else {
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
            progress.loadHomeSuccess(CarActivity.this, "登录后才能使用购物车哦", "去登录", true);
            DialogSingleUtil.dismiss(0);
        } else {
            KeplerApiManager.getWebViewService().checkLoginState(new ActionCallBck() {
                @Override
                public boolean onDateCall(int key, String info) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Logg.e("已登陆京东");
                            tishi.setVisibility(View.VISIBLE);
                            llBottomTbCar.setVisibility(View.VISIBLE);
                            String homeJdContent = SharedPreferencesUtil.getSharedData(CarActivity.this, "homeJdCarContent", "homeJdCarContent");
                            try {
                                if (homeJdContent != null && !homeJdContent.equals("")) {
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
                                            DialogSingleUtil.dismiss(0);
                                        }
                                    } else {
                                        DialogSingleUtil.dismiss(0);
                                    }
                                } else {
                                    DialogSingleUtil.dismiss(0);
                                    getShoppingCartUrlByDomain("jd");
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
                            tishi.setVisibility(View.GONE);
                            progress.loadCarSuccess(CarActivity.this, "登陆后可一键同步京东购物车内商品", "授权登陆京东", true);
                            llBottomTbCar.setVisibility(View.GONE);
                            DialogSingleUtil.dismiss(0);
                        }
                    });
                    return false;
                }
            });
        }
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

    /**
     * 京东开普勒登录回调
     */
    LoginListener mLoginListener = new LoginListener() {

        @Override
        public void authSuccess(final Object token) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    StringUtil.showToast(CarActivity.this, "登录成功");
                    checkLoginStatus();
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
                        StringUtil.showToast(CarActivity.this, "登录失败");
                    }
                });
            }
        }
    };

    /**
     * 登陆淘宝京东
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
                            StringUtil.showToast(CarActivity.this, "已登录");
                        }
                    });
                    return false;
                }

                @Override
                public boolean onErrCall(int key, String error) {
                    // 直接授权登录京东​
                    KeplerApiManager.getWebViewService().login(
                            CarActivity.this, mLoginListener);
                    return false;
                }
            });
        } else {
            if (!AlibcLogin.getInstance().isLogin()) {
                DialogSingleUtil.show(CarActivity.this, "授权中...");
                final AlibcLogin alibcLogin = AlibcLogin.getInstance();
                alibcLogin.showLogin(CarActivity.this, new AlibcLoginCallback() {
                    @Override
                    public void onSuccess() {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, "登录成功 ");
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String date = sDateFormat.format(new Date());
                        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "taobao", "taobaodata", date);
                        getShoppingCartUrlByDomain("taobao");

                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, "登录失败 ");
                    }
                });
            } else {
                StringUtil.showToast(CarActivity.this, "已经登陆过了");
            }
        }
    }

    /**
     * 获取当前登录用户的淘宝京东购物车链接
     *
     * @param domain
     */
    private void getShoppingCartUrlByDomain(String domain) {
        cancelCheck = true;
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("domain", domain);
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().getShoppingCartUrlByDomain(
                maps, new BaseObserver<String>(CarActivity.this) {
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
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
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
                        DialogCheckYouhuiUtil.show(CarActivity.this, "小鲸正在努力同步中，请您耐心等等哦！");
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogCheckYouhuiUtil.dismiss(0);
                        mPtrframe.finishRefresh();
                        StringUtil.showToast(CarActivity.this, e.message);
                    }
                });
    }

    /**
     * 获取淘宝购物车信息
     *
     * @param content
     */
    private void synchroShoppingCart(String content, String domain) {
        final Map<String, String> maps = new HashMap<String, String>();
        Logg.json(content);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        maps.put("userid", userID);
        maps.put("domain", domain);
        maps.put("client", "android");
        maps.put("content", content);
//        dataFlow.requestData(1, "newService/synchroShoppingCart", maps, this, false);
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().synchroShoppingCart(
                maps, new BaseObserver<String>(CarActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                mPtrframe.finishRefresh();
                                Logg.json(jsonObject);
                                if (cancelCheck) {
                                    //缓存加载出来的购物车数据
                                    if (curPosition == 2) {
                                        SharedPreferencesUtil.putSharedData(CarActivity.this, "homeJdCarContent", "homeJdCarContent", jsonObject.toString());
                                    } else {
                                        SharedPreferencesUtil.putSharedData(CarActivity.this, "homeTbCarContent", "homeTbCarContent", jsonObject.toString());
                                    }
//                                    解析数据
                                    taoBaoCarBeans = JSON.parseArray(jsonObject.optString("content"), TaoBaoCarBean.class);
                                    if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
                                        recyclerView.setVisibility(View.VISIBLE);
                                        progress.setVisibility(View.GONE);
                                        getAllNum();
                                        StringUtil.showToast(CarActivity.this, "已同步最新宝贝数据");
                                        llBottomTbCar.setVisibility(View.VISIBLE);
                                        tishi.setVisibility(View.VISIBLE);
                                    } else {
                                        recyclerView.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                        tishi.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogCheckYouhuiUtil.dismiss(0);
//                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
//                        DialogCheckYouhuiUtil.show(CarActivity.this, "小鲸正在努力同步中，请您耐心等等哦！");
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, e.message);
                    }
                });
    }


    Handler mHandlerWebView = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case TIMEOUT_ERROR :
                    //这里对已经显示出页面且加载超时的情况不做处理
                    if(tbWebview != null &&tbWebview.getProgress() < 100){
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this,"加载超时");
                        mPtrframe.finishRefresh();
                    }
                    break ;
            }
        }
    };

    private void init(final String url) {
        // 开启JavaScript支持
        tbWebview.getSettings().setJavaScriptEnabled(true);
        tbWebview.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        //不加载网络上的图片资源
        tbWebview.getSettings().setBlockNetworkImage(true);
        // 加载链接
        tbWebview.loadUrl(url);
        tbWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Logg.e("onPageStarted url:" + url);
                System.out.println("======> onPageStarted 开始加载");
                mTimer = new Timer();
                if (tbWebview.getProgress() < 100) {
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            Message m = new Message();
                            m.what = TIMEOUT_ERROR;
                            mHandlerWebView.sendMessage(m);
                            if (mTimer != null) {
                                mTimer.cancel();
                                mTimer.purge();
                            }
                        }
                    };
                    mTimer.schedule(tt, TIMEOUT);
                }else {
                    if (mTimer != null) {
                        mTimer.cancel();
                        mTimer.purge();
                    }
                }
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
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer.purge();
                }
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


    /**
     * 获取解析后的购物车数据
     *
     * @param requestCode
     * @param api
     * @param jsonObject
     * @param content
     */
    @Override
    public void onResultData(int requestCode, String api, JSONObject jsonObject, String content) {
//        final JSONObject jsonObject;
        try {
            if (jsonObject.optString("status").equals("1")) {
                mPtrframe.finishRefresh();
                Logg.json("=========>>>>", jsonObject);
                if (cancelCheck) {
                    //缓存加载出来的购物车数据
                    if (curPosition == 2) {
                        SharedPreferencesUtil.putSharedData(CarActivity.this, "homeJdCarContent", "homeJdCarContent", jsonObject.toString());
                    } else {
                        SharedPreferencesUtil.putSharedData(CarActivity.this, "homeTbCarContent", "homeTbCarContent", jsonObject.toString());
                    }
                    //解析数据
                    taoBaoCarBeans = JSON.parseArray(jsonObject.optString("content"), TaoBaoCarBean.class);
                    if (taoBaoCarBeans != null && taoBaoCarBeans.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        getAllNum();
                        DialogCheckYouhuiUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, "已同步最新宝贝数据");
                        llBottomTbCar.setVisibility(View.VISIBLE);
                        tishi.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadSuccess(true);
                        tishi.setVisibility(View.GONE);
                    }
                }
            } else {
                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 超时回调
     */
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
        StringUtil.showToast(CarActivity.this, "连接超时");
    }

    @Override
    public void loadFail() {

    }

    public void finishFresh() {
        mPtrframe.finishRefresh();
    }


    /**
     * 获取淘宝京东购物车的html并上传服务器解析
     */
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



    /**
     *购物车显示方式
     */
    private void getShoppingCartShowList() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(CarActivity.this).createBaseApi().getShoppingCartShowList(
                maps, new BaseObserver<String>(CarActivity.this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.optString("content"));
                                String jd = jsonObject1.optString("jd");
                                String taobao = jsonObject1.optString("taobao");
                                String ziying = jsonObject1.optString("ziying");
                                if (jd.equals("1") && taobao.equals("1") && ziying.equals("1")){
                                    tablayout.setxTabDisplayNum(3);
                                    tablayout.addTab(tablayout.newTab().setText("淘宝购物车"));
                                    tablayout.addTab(tablayout.newTab().setText("京东购物车"));
                                    tablayout.addTab(tablayout.newTab().setText("鲸城购物车"));
                                } else if (jd.equals("0") && taobao.equals("1") && ziying.equals("1")) {
                                    tablayout.setxTabDisplayNum(2);
                                    tablayout.addTab(tablayout.newTab().setText("淘宝购物车"));
                                    tablayout.addTab(tablayout.newTab().setText("鲸城购物车"));
                                } else if (jd.equals("1") && taobao.equals("0") && ziying.equals("1")) {
                                    tablayout.setxTabDisplayNum(2);
                                    tablayout.addTab(tablayout.newTab().setText("京东购物车"));
                                    tablayout.addTab(tablayout.newTab().setText("鲸城购物车"));
                                } else if (jd.equals("1") && taobao.equals("1") && ziying.equals("0")) {
                                    tablayout.setxTabDisplayNum(2);
                                    tablayout.addTab(tablayout.newTab().setText("淘宝购物车"));
                                    tablayout.addTab(tablayout.newTab().setText("京东购物车"));
                                }
                                tablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(XTabLayout.Tab tab) {
                                        Logg.e(tab.getText());
                                        String j = tab.getText().toString();
                                        if (j.equals("淘宝购物车")) {
                                            showTaobao();
                                        } else if (j.equals("京东购物车")) {
                                            showJd();
                                        } else {
                                            showZiying();
                                        }
                                    }

                                    @Override
                                    public void onTabUnselected(XTabLayout.Tab tab) {

                                    }

                                    @Override
                                    public void onTabReselected(XTabLayout.Tab tab) {

                                    }
                                });
                                /**
                                 * 从商城进入购物车跳自营
                                 */
                                if (getIntent().getStringExtra("ziying") != null) {
                                    XTabLayout.Tab tabAt;
                                    switch (getIntent().getStringExtra("ziying")) {
                                        case "yes":
                                            if (jd.equals("1") && taobao.equals("1") && ziying.equals("1")){
                                                tabAt = tablayout.getTabAt(2);
                                                tabAt.select();
                                            } else if (jd.equals("0") && taobao.equals("1") && ziying.equals("1")) {
                                                tabAt = tablayout.getTabAt(1);
                                                tabAt.select();
                                            } else if (jd.equals("1") && taobao.equals("0") && ziying.equals("1")) {
                                                tabAt = tablayout.getTabAt(1);
                                                tabAt.select();
                                            } else if (jd.equals("1") && taobao.equals("1") && ziying.equals("0")) {
                                            }
                                            break;
                                        case "jd":
                                            if (jd.equals("1") && taobao.equals("1") && ziying.equals("1")){
                                                tabAt = tablayout.getTabAt(1);
                                                tabAt.select();
                                            } else if (jd.equals("0") && taobao.equals("1") && ziying.equals("1")) {
                                            } else if (jd.equals("1") && taobao.equals("0") && ziying.equals("1")) {
                                                tabAt = tablayout.getTabAt(0);
                                                tabAt.select();
                                            } else if (jd.equals("1") && taobao.equals("1") && ziying.equals("0")) {
                                                tabAt = tablayout.getTabAt(1);
                                                tabAt.select();
                                            }
                                            break;
                                    }
                                }
                            } else {
                                StringUtil.showToast(CarActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(CarActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(CarActivity.this, e.message);
                    }
                });
    }


    /**
     * TAOBAO
     */
    public void showTaobao(){
        curPosition = 0;
        sum = 0;
        titleText.setText("淘宝购物车");
        llBottomCar.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        titleText2.setVisibility(View.GONE);
        DialogSingleUtil.show(CarActivity.this);
        showTaoBaoCar();
    }

    /**
     * JD
     */
    public void showJd(){
        curPosition = 2;
        sum = 0;
        llBottomCar.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        titleText2.setVisibility(View.GONE);
        titleText.setText("京东购物车");
        DialogSingleUtil.show(CarActivity.this);
        showJdCar();
    }
    /**
     * 自营
     */
    public void showZiying(){
        curPosition = 1;
        tishi.setVisibility(View.GONE);
        llBottomTbCar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        llBottomCar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        titleText.setText("鲸城购物车(" + carCount + ")");
        titleText2.setVisibility(View.VISIBLE);
        DialogSingleUtil.show(CarActivity.this);
        queryShoppingCartByUserid();
    }
}
