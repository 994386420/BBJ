package com.bbk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.FenSiOrderBean;
import com.bbk.Bean.FensiBean;
import com.bbk.Bean.InvitedBean;
import com.bbk.adapter.FenSiAdapter;
import com.bbk.adapter.FenSiInvitedAdapter;
import com.bbk.adapter.FenSiNjiOrderAdapter;
import com.bbk.adapter.FenSiOrderAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.toprightmenu.JifenTopMenu;
import com.zaaach.toprightmenu.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收益报表
 */
public class FensiActivity extends BaseActivity implements CommonLoadingView.LoadingHandler {

    String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.fensi_recyclerview)
    RecyclerView fensiRecyclerview;
    @BindView(R.id.fensi_layout)
    LinearLayout fensiLayout;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    int page = 1, x = 1;
    List<FensiBean> fensiBeans;
    List<InvitedBean> invitedBeans;
    List<FenSiOrderBean> fenSiOrderBeans;
    FenSiAdapter fenSiAdapter;
    FenSiInvitedAdapter fenSiInvitedAdapter;
    FenSiOrderAdapter fenSiOrderAdapter;
    FenSiNjiOrderAdapter fenSiNjiOrderAdapter;
    @BindView(R.id.friends_num)
    TextView friendsNum;
    @BindView(R.id.ll_yaoqing)
    LinearLayout llYaoqing;
    @BindView(R.id.fensi_recyclerview1)
    RecyclerView fensiRecyclerview1;
    @BindView(R.id.tv_tixing)
    TextView tvTixing;
    @BindView(R.id.ll_reamind)
    LinearLayout llReamind;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.tv_tixing_sign)
    TextView tvTixingSign;
    @BindView(R.id.img_fensi)
    ImageView imgFensi;
    private String type = "1", title = "所有记录";
    private int curPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fensi_layout);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        titleText.setText("好友红包");
        tablayout.setxTabDisplayNum(2);
        tablayout.addTab(tablayout.newTab().setText("提醒粉丝下单"));
        tablayout.addTab(tablayout.newTab().setText("提醒粉丝签到"));
        tablayout.getTabAt(0).setCustomView(getTabView("提醒粉丝下单"));
        /**
         * 获取目标tab重新设置点击事件
         */
        XTabLayout.Tab tab = tablayout.getTabAt(0);
        if (tab.getCustomView() != null) {
            View tabView = (View) tab.getCustomView().getParent();
            tabView.setTag(0);
            tabView.setOnClickListener(mTabOnClickListener);
        }
        tablayout.setOnTabSelectedListener(tabSelectedListener);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

        };
        fensiRecyclerview.setLayoutManager(linearLayoutManager);
        fensiRecyclerview.setHasFixedSize(true);
        fensiRecyclerview1.setLayoutManager(linearLayoutManager1);
        fensiRecyclerview1.setHasFixedSize(true);
        progress.setLoadingHandler(this);
        refreshAndloda();
        queryMyFansToBuyState(type);

        if (getIntent().getStringExtra("isQiandao") != null){
            XTabLayout.Tab tabAt;
            tabAt = tablayout.getTabAt(1);
            tabAt.select();
        }
    }


    /**
     * 积分记录选择弹窗
     *
     * @param context
     * @param view
     */
    public void showItemPop(final Context context, View view) {
        JifenTopMenu mJiFenMenu;
        mJiFenMenu = new JifenTopMenu((Activity) context);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("一级粉丝"));
        menuItems.add(new MenuItem("N级粉丝"));
        mJiFenMenu
                .setHeight(334)  //默认高度480
                .setWidth(320)//默认宽度wrap_content
                .showIcon(true) //显示菜单图标，默认为true
                .dimBackground(false) //背景变暗，默认为true
                .needAnimationStyle(false) //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new JifenTopMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        XTabLayout.Tab tab = tablayout.getTabAt(0);
                        switch (position) {
                            case 0:
                                type = "1";
                                page = 1;
                                x = 1;
                                queryMyFansToBuyState(type);
                                break;
                            case 1:
                                type = "0";
                                page = 1;
                                x = 1;
                                queryMyFansToBuyState(type);
                                break;
                        }
                    }
                })
                .showAsDropDown(view, 100, -50);
    }

    /**
     * table选择事件
     */
    XTabLayout.OnTabSelectedListener tabSelectedListener = new XTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(XTabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                curPosition = 0;
                title = "提醒粉丝下单";
                x = 1;
                page = 1;
                changeTabSelect(tab, title);
                queryMyFansToBuyState(type);
            } else if (j == 1) {
                curPosition = 1;
                x = 1;
                page = 1;
                queryUserBrokerage();
            }
        }

        @Override
        public void onTabUnselected(XTabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                changeTabNormal(tab);
            }
        }

        @Override
        public void onTabReselected(XTabLayout.Tab tab) {

        }
    };

    /**
     * 自定义tab样式
     *
     * @param title
     * @return
     */
    public View getTabView(String title) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
        TextView txt_title = view.findViewById(R.id.txt_title);
        txt_title.setText(title);
        return view;
    }

    /**
     * 选择tab显示样式
     *
     * @param tab
     * @param title
     */
    private void changeTabSelect(XTabLayout.Tab tab, String title) {
        View view = tab.getCustomView();
        TextView img_title = view.findViewById(R.id.img_title);
        TextView txt_title = view.findViewById(R.id.txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.color_line_top));
        //改变tab时需动态显示已选择项
        txt_title.setText(title);
        img_title.setVisibility(View.VISIBLE);
    }

    /**
     * 未选择tab显示样式
     *
     * @param tab
     */
    private void changeTabNormal(XTabLayout.Tab tab) {
        View view = tab.getCustomView();
        TextView img_title = view.findViewById(R.id.img_title);
        TextView txt_title = view.findViewById(R.id.txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.shop_color1));
        img_title.setVisibility(View.GONE);
    }

    /**
     * 积分记录点击事件
     */
    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (position == 0 && tablayout.getTabAt(position).isSelected() == true) {
                showItemPop(FensiActivity.this, v);
            }
        }
    };


    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                if (curPosition == 0) {
                    queryMyFansToBuyState(type);
                    return;
                }
                queryUserBrokerage();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                if (curPosition == 0) {
                    queryMyFansToBuyState(type);
                    return;
                }
                queryUserBrokerage();
            }
        });
    }

    /**
     * 查询返利金币列表 （页面显示状态：0为未领1为已领）
     */
    private void queryUserBrokerage() {
        refreshLayout.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().querySignFanLi(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject object = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                fensiRecyclerview.setVisibility(View.VISIBLE);
                                fensiRecyclerview1.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                refreshLayout.setEnableLoadMore(true);
                                Glide.with(FensiActivity.this).load(object.optString("img")).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgFensi);
                                friendsNum.setText("共" + object.optString("count") + "位一级粉丝");
                                invitedBeans = JSON.parseArray(object.optString("arr2"), InvitedBean.class);
                                if (x == 1) {
                                    llReamind.setVisibility(View.VISIBLE);
                                    tvTixingSign.setVisibility(View.VISIBLE);
                                    tvTixing.setVisibility(View.GONE);
                                    fensiBeans = JSON.parseArray(object.optString("arr"), FensiBean.class);
                                    if (fensiBeans.size() > 0 && fensiBeans != null) {
                                        fenSiAdapter = new FenSiAdapter(FensiActivity.this, fensiBeans);
                                        fensiRecyclerview.setAdapter(fenSiAdapter);
                                    }
                                    if (invitedBeans.size() > 0 && invitedBeans != null) {
                                        fenSiInvitedAdapter = new FenSiInvitedAdapter(FensiActivity.this, invitedBeans);
                                        fensiRecyclerview1.setAdapter(fenSiInvitedAdapter);
                                    } else {
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                    if (fensiBeans.size() == 0 && invitedBeans.size() == 0) {
                                        progress.setVisibility(View.VISIBLE);
                                        fensiRecyclerview.setVisibility(View.GONE);
                                        fensiRecyclerview1.setVisibility(View.GONE);
                                        progress.loadSuccess(true);
                                        llReamind.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (invitedBeans.size() > 0 && invitedBeans != null) {
                                        fenSiInvitedAdapter.notifyData(invitedBeans);
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }

                            } else {
                                StringUtil.showToast(FensiActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(FensiActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        fensiRecyclerview.setVisibility(View.GONE);
                        fensiRecyclerview1.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(FensiActivity.this, e.message);
                    }
                });
    }


    /**
     * 一键提醒下单
     */
    private void remindFriendBuyGoods() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().remindFriendBuyGoods(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(FensiActivity.this, "提醒成功");
                                tvTixing.setBackgroundResource(R.drawable.bg_czg6);
                                tvTixing.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                                tvTixing.setText("已经提醒过了");
                                tvTixing.setClickable(false);
                            } else {
                                StringUtil.showToast(FensiActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(FensiActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(FensiActivity.this, e.message);
                    }
                });
    }

    /**
     * 一键提醒签到
     */
    private void remindFriendSig() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        RetrofitClient.getInstance(this).createBaseApi().remindFriendSign(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(FensiActivity.this, "提醒成功");
                                tvTixingSign.setBackgroundResource(R.drawable.bg_czg6);
                                tvTixingSign.setTextColor(getResources().getColor(R.color.tuiguang_color4));
                                tvTixingSign.setText("已经提醒过了");
                                tvTixingSign.setClickable(false);
                            } else {
                                StringUtil.showToast(FensiActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(FensiActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(FensiActivity.this, e.message);
                    }
                });
    }

    /**
     * 查询提醒好友购买详情列表
     *
     * @param type （1为直属下级，其他为第N级粉丝）
     */
    private void queryMyFansToBuyState(final String type) {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("page", page + "");
        maps.put("type", type);
        RetrofitClient.getInstance(this).createBaseApi().queryMyFansToBuyState(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            JSONObject jsonObject1 = new JSONObject(content);
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(content);
                                Glide.with(FensiActivity.this).load(jsonObject1.optString("img")).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgFensi);
                                fensiRecyclerview.setVisibility(View.GONE);
                                fensiRecyclerview1.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                refreshLayout.setEnableLoadMore(true);
                                if (type.equals("1")) {
                                    friendsNum.setText("共" + jsonObject1.optString("count") + "位一级粉丝");
                                } else {
                                    friendsNum.setText("共" + jsonObject1.optString("count") + "位N级粉丝");
                                }
                                fenSiOrderBeans = JSON.parseArray(jsonObject1.optString("arr"), FenSiOrderBean.class);
                                if (x == 1) {
                                    if (fenSiOrderBeans.size() > 0 && fenSiOrderBeans != null) {
                                        if (type.equals("1")) {
                                            llReamind.setVisibility(View.VISIBLE);
                                            tvTixingSign.setVisibility(View.GONE);
                                            tvTixing.setVisibility(View.VISIBLE);
                                            fenSiOrderAdapter = new FenSiOrderAdapter(FensiActivity.this, fenSiOrderBeans);
                                            fensiRecyclerview1.setAdapter(fenSiOrderAdapter);
                                        } else {

                                            llReamind.setVisibility(View.GONE);
                                            fenSiNjiOrderAdapter = new FenSiNjiOrderAdapter(FensiActivity.this, fenSiOrderBeans);
                                            fensiRecyclerview1.setAdapter(fenSiNjiOrderAdapter);
                                        }
                                    } else {
                                        refreshLayout.setEnableLoadMore(false);
                                        progress.setVisibility(View.VISIBLE);
                                        fensiRecyclerview.setVisibility(View.GONE);
                                        fensiRecyclerview1.setVisibility(View.GONE);
                                        progress.loadSuccess(true);
                                        llReamind.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (fenSiOrderBeans.size() > 0 && fenSiOrderBeans != null) {
                                        if (type.equals("1")) {
                                            fenSiOrderAdapter.notifyData(fenSiOrderBeans);
                                        } else {
                                            fenSiNjiOrderAdapter.notifyData(fenSiOrderBeans);
                                        }
                                    } else {
                                        refreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }

                            } else {
                                StringUtil.showToast(FensiActivity.this, jsonObject.optString("errmsg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(FensiActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        fensiRecyclerview.setVisibility(View.GONE);
                        fensiRecyclerview1.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
                        progress.loadError();
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(FensiActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        if (curPosition == 0) {
            queryMyFansToBuyState(type);
            return;
        }
        queryUserBrokerage();
    }

    @OnClick({R.id.title_back_btn, R.id.ll_yaoqing, R.id.tv_tixing, R.id.tv_tixing_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_yaoqing:
                Intent intent = new Intent(FensiActivity.this, YaoqingFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_tixing:
                remindFriendBuyGoods();
                break;
            case R.id.tv_tixing_sign:
                remindFriendSig();
                break;
        }
    }
}
