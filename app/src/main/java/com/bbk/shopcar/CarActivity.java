package com.bbk.shopcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.ShopDetailActivty;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.resource.NewConstants;
import com.bbk.shopcar.Utils.UtilsLog;
import com.bbk.shopcar.adapter.ShopcatAdapter;
import com.bbk.shopcar.entity.GoodsInfo;
import com.bbk.shopcar.entity.GoodsInfo1;
import com.bbk.shopcar.entity.StoreInfo;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物车
 */
public class CarActivity extends BaseActivity implements View.OnClickListener, ShopcatAdapter.CheckInterface, ShopcatAdapter.ModifyCountInterface, ShopcatAdapter.GroupEditorListener, CommonLoadingView.LoadingHandler {
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
    @BindView(R.id.mPtrframe)
    SmartRefreshLayout mPtrframe;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.ll_bottom_car)
    LinearLayout llBottomCar;
    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;
    //false就是编辑，ture就是完成
    private boolean flag = false;
    private ShopcatAdapter adapter;
    private List<StoreInfo> groups; //组元素的列表
    private Map<String, List<GoodsInfo>> childs; //子元素的列表
    private String ids,nums,guiges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopcar_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View topView = findViewById(R.id.activity_car);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initPtrFrame();
        initActionBar();
//        initData();
        NewConstants.refeshFlag = "0";
        queryShoppingCartByUserid();
//        initEvents();
        titleText.setText("购物车");
        titleText2.setText("编辑");
        titleText2.setVisibility(View.VISIBLE);
        progress.setLoadingHandler(this);
    }

    private void initPtrFrame() {
//        final StoreHouseHeader header=new StoreHouseHeader(this);
//        header.setPadding(dp2px(20), dp2px(20), 0, 0);
//        header.initWithString("xiaoma is good");
        mPtrframe.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                queryShoppingCartByUserid();
                if (groups != null) {
                    for (int i = 0; i < groups.size(); i++) {
                        StoreInfo group = groups.get(i);
                        group.setChoosed(false);
                        List<GoodsInfo> child = childs.get(group.getId());
                        for (int j = 0; j < child.size(); j++) {
                            child.get(j).setChoosed(false);//这里出现过错误
                        }
                    }
                }
                calulate();
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                allCheckBox.setChecked(false);
            }
        });
        mPtrframe.setEnableLoadMore(false);
    }


    private void initEvents() {
//        actionBarEdit.setOnClickListener(this);
        adapter = new ShopcatAdapter(CarActivity.this, groups, childs, mcontext);
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
    protected void onResume() {
        super.onResume();
        Logg.e(NewConstants.refeshFlag);
        if (NewConstants.refeshFlag.equals("1")){
            mtotalCount = 0;
            goPay.setText("去支付(" + mtotalCount + ")");
            queryShoppingCartByUserid();
        }else {
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
            clearCart();
        } else {
            titleText.setText("购物车(" + count + ")");
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
        mcontext = this;
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
        Logg.json(list.toString().replace("[", "").replace("]", "").replace(" ",""));
        doShoppingCart(list.toString().replace("[", "").replace("]", "").replace(" ",""), "-1", "", listguige.toString().replace("[", "").replace("]", ""),"all");
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
     * @param dianpuid
     */
    @Override
    public void IntentGroup(String dianpuid) {
        NewConstants.refeshFlag = "0";
        Intent intent = new Intent(this, NewDianpuActivity.class);
        intent.putExtra("dianpuid",dianpuid);
        startActivity(intent);
    }

    /**
     * 增加购物车数量，最高99
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
        DialogSingleUtil.show(CarActivity.this);
        doShoppingCart(id, "3", 1+ "", guige,"add");
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 减少购物车数量，最低1
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
        doShoppingCart(id, "4", 1 + "", guige,"dec");
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 删除购物车商品
     * @param groupPosition
     * @param childPosition 思路:当子元素=0，那么组元素也要删除
     */
    @Override
    public void childDelete(int groupPosition, int childPosition, String id, int num, String guige) {
        DialogSingleUtil.show(CarActivity.this);
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> child = childs.get(group.getId());
        child.remove(childPosition);
        if (child.size() == 0) {
            groups.remove(groupPosition);
        }
        Logg.e(id + "=====" + num + "====" + guige);
        doShoppingCart(id, "-1", num + "", guige,"child");
        adapter.notifyDataSetChanged();
        calulate();


    }

    /**
     * 跳转到商品详情页
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
     * @param groupPosition
     * @param childPosition
     * @param showCountView
     * @param isChecked
     * @param id
     * @param num
     * @param guige
     */
    @Override
    public void Refresh(int groupPosition, int childPosition, View showCountView, boolean isChecked, String id, int num, String guige,String dialognum) {
//        Logg.e("------------------------>>>>>");
        GoodsInfo good = (GoodsInfo) adapter.getChild(groupPosition, childPosition);
        if (num <= 0) {
            StringUtil.showToast(this, "数量超出范围");
            doShoppingCart(id, "5", dialognum ,guige,"");
            good.setNum(1);
            adapter.notifyDataSetChanged();
        } else if (num > 99) {
            StringUtil.showToast(this, "数量超出范围");
            doShoppingCart(id, "5", dialognum, guige,"");
            good.setNum(99);
            adapter.notifyDataSetChanged();
        } else {
            Logg.e(NewConstants.car);
            good.setNum(num);
            doShoppingCart(id, "5", dialognum, guige,"");
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
                        .setMsg("总计:" + mtotalCount + "种商品，" +   doubleToString(mtotalPrice) + "元")
                        .setPositiveButton("支付", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NewConstants.refeshFlag = "0";
                                Intent intent = new Intent(CarActivity.this,ConfirmOrderActivity.class);
                                intent.putExtra("ids",ids);
                                intent.putExtra("nums",nums);
                                intent.putExtra("guiges",guiges);
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
        Logg.json(list.toString().replace("[", "").replace("]", "").replace(",","|").replace(" ",""));
        Logg.json(listNum.toString().replace("[", "").replace("]", "").replace(",","|").replace(" ",""));
        Logg.json(listguiges.toString().replace("[", "").replace("]", "").replace(",","|"));
        ids = list.toString().replace("[", "").replace("]", "").replace(",","|").replace(" ","");
        nums = listNum.toString().replace("[", "").replace("]", "").replace(",","|").replace(" ","");
        guiges = listguiges.toString().replace("[", "").replace("]", "").replace(",","|");
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
     * @param num
     * @return
     */
    public static String doubleToString(double num){
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
    protected void onDestroy() {
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

    @OnClick({R.id.title_back_btn, R.id.title_text2})
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
        }
    }


    /**
     * 根据id查购物车内容
     */
    private void queryShoppingCartByUserid() {
        Map<String, String> maps = new HashMap<String, String>();
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
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
                                } else {
                                    progress.setVisibility(View.VISIBLE);
                                    llBottomCar.setVisibility(View.GONE);
                                    titleText2.setVisibility(View.GONE);
                                    progress.loadSuccess(true);
                                }
                                initEvents();
                                setCartNum();
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
                        DialogSingleUtil.show(CarActivity.this);
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
                                if (flag.equals("all")){
                                    queryShoppingCartByUserid();
                                }else {

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

    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        queryShoppingCartByUserid();
    }

}
