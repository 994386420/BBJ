package com.bbk.model;


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
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.bbk.Bean.JiFenBean;
import com.bbk.Bean.JiFenJlistBean;
import com.bbk.Bean.JiFenListBean;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.JiFenDetailAdapter;
import com.bbk.adapter.JiFenJlistDetailAdapter;
import com.bbk.client.BaseApiService;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zaaach.toprightmenu.JifenTopMenu;
import com.zaaach.toprightmenu.MenuItem;;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 积分明细
 */
public class JiFenActivity extends BaseActivity implements CommonLoadingView.LoadingHandler{


    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_duihuan)
    TextView tvDuihuan;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.recyc_jifen)
    RecyclerView recycJifen;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_jifen_guoqi)
    TextView tvJifenGuoqi;
    private String type = "0",title = "所有记录",typeSelect = "";
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifen_layout);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        progress.setLoadingHandler(this);
        refreshLayout.setEnableLoadMore(false);
        titleText.setText("我的积分");
        imgMoreBlack.setVisibility(View.VISIBLE);
        recycJifen.setHasFixedSize(true);
        recycJifen.setNestedScrollingEnabled(false);
        recycJifen.setLayoutManager(new LinearLayoutManager(this));
        tablayout.setxTabDisplayNum(2);
        tablayout.addTab(tablayout.newTab().setText("所有记录"));
        tablayout.addTab(tablayout.newTab().setText("兑换记录"));
        tablayout.getTabAt(0).setCustomView(getTabView("所有记录"));
        /**
         * 获取目标tab重新设置点击事件
         */
        XTabLayout.Tab tab = tablayout.getTabAt(0);
        if (tab.getCustomView()!=null){
            View tabView=  (View)tab.getCustomView().getParent();
            tabView.setTag(0);
            tabView.setOnClickListener(mTabOnClickListener);
        }
        tablayout.setOnTabSelectedListener(tabSelectedListener);
        //获取积分列表
        queryIntegralCenterByUserid(type);
        refreshAndloda();
    }

    /**
     * 自定义tab样式
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
     * @param tab
     * @param title
     */
    private void changeTabSelect(XTabLayout.Tab tab,String title) {
        View view = tab.getCustomView();
        TextView img_title = view.findViewById(R.id.img_title);
        TextView txt_title =  view.findViewById(R.id.txt_title);
        txt_title.setTextColor(getResources().getColor(R.color.color_line_top));
        //改变tab时需动态显示已选择项
        txt_title.setText(title);
        img_title.setVisibility(View.VISIBLE);
    }

    /**
     * 未选择tab显示样式
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
     * 下拉刷新
     */
    private void refreshAndloda() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                if (position == 0) {
                    queryIntegralCenterByUserid(type);
                    return;
                }
                queryIntegralCenterByUserid(typeSelect);
            }
        });
    }

    /**
     * 积分记录点击事件
     */
    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position= (int) v.getTag();
            if (position==0 &&tablayout.getTabAt(position).isSelected()==true){
               showItemPop(JiFenActivity.this,v);
            }
        }
    };

    /**
     * 积分记录选择弹窗
     * @param context
     * @param view
     */
    public void showItemPop(final Context context, View view){
        JifenTopMenu mJiFenMenu;
        mJiFenMenu = new JifenTopMenu((Activity) context);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("所有记录"));
        menuItems.add(new MenuItem("获得记录"));
        menuItems.add(new MenuItem("使用记录"));
        mJiFenMenu
                .setHeight(450)  //默认高度480
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
                        switch (position){
                            case 0:
                                type = "0";
                                title = "所有记录";
                                changeTabSelect(tab,title);
                                queryIntegralCenterByUserid(type);
                                break;
                            case 1:
                                type = "1";
                                title = "获得记录";
                                changeTabSelect(tab,title);
                                queryIntegralCenterByUserid(type);
                                break;
                            case 2:
                                type = "2";
                                title = "使用记录";
                                changeTabSelect(tab,title);
                                queryIntegralCenterByUserid(type);
                                break;
                        }
                    }
                })
                .showAsDropDown(view, 100, 0);
    }

    /**
     * table选择事件
     */
    XTabLayout.OnTabSelectedListener tabSelectedListener = new XTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(XTabLayout.Tab tab) {
            int j = tab.getPosition();
            if (j == 0) {
                position = 0;
                typeSelect = type;
                changeTabSelect(tab,title);
            } else if (j == 1) {
                position = 1;
                typeSelect = "3";
            }
            queryIntegralCenterByUserid(typeSelect);
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
     *  查询个人积分中心
     * @param type 0查询积分所有	1查询积分获得记录	2查询积分使用记录	3积分兑换记录
     */
    private void queryIntegralCenterByUserid(final String type) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("type", type);
        RetrofitClient.getInstance(this).createBaseApi().queryIntegralCenterByUserid(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            Logg.json(jsonObject);
                            if (jsonObject.optString("status").equals("1")) {
                                JiFenBean jiFenBean = JSON.parseObject(jsonObject.optString("content"), JiFenBean.class);
                                /**
                                 * later不存在时隐藏该项
                                 */
                                if (jiFenBean.getLater() != null && !jiFenBean.getLater().equals("")) {
                                    tvJifenGuoqi.setVisibility(View.VISIBLE);
                                    tvJifenGuoqi.setText(jiFenBean.getLater());
                                }else {
                                    tvJifenGuoqi.setVisibility(View.GONE);
                                }
                                tvJifen.setText(jiFenBean.getTotal());
                                List<JiFenListBean> jiFenListBeans = JSON.parseArray(jiFenBean.getOlist(),JiFenListBean.class);
                                List<JiFenJlistBean> jiFenJlistBeans = JSON.parseArray(jiFenBean.getJlist(),JiFenJlistBean.class);
                                /**
                                 * type为3时显示兑换列表 否则显示积分列表
                                 */
                                if (type.equals("3")){
                                    if (jiFenJlistBeans.size() > 0 && jiFenJlistBeans != null) {
                                        recycJifen.setVisibility(View.VISIBLE);
                                        progress.setVisibility(View.GONE);
                                        progress.loadSuccess();
                                        recycJifen.setAdapter(new JiFenJlistDetailAdapter(JiFenActivity.this, jiFenJlistBeans));
                                    }else {
                                        recycJifen.setVisibility(View.GONE);
                                        progress.setVisibility(View.VISIBLE);
                                        progress.loadSuccess(true);
                                    }
                                    return;
                                }
                                if (jiFenListBeans.size() > 0 && jiFenListBeans != null) {
                                    recycJifen.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    progress.loadSuccess();
                                    recycJifen.setAdapter(new JiFenDetailAdapter(JiFenActivity.this,jiFenListBeans));
                                }else {
                                    recycJifen.setVisibility(View.GONE);
                                    progress.setVisibility(View.VISIBLE);
                                    progress.loadSuccess(true);
                                }
                            } else {
                                StringUtil.showToast(JiFenActivity.this, jsonObject.optString("errmsg"));
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
                        DialogSingleUtil.show(JiFenActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        progress.loadError();
                        progress.setVisibility(View.VISIBLE);
                        recycJifen.setVisibility(View.GONE);
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        StringUtil.showToast(JiFenActivity.this, e.message);
                    }
                });
    }

    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.title_back_btn, R.id.img_more_black, R.id.tv_duihuan, R.id.tv_rule})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.img_more_black:
                HomeLoadUtil.showItemPop(this, view);
                break;
            case R.id.tv_duihuan:
                intent = new Intent(this, ZiYingZeroBuyShopActivity.class);
                intent.putExtra("isOlder","yes");
                startActivity(intent);
                break;
            case R.id.tv_rule:
                String url = BaseApiService.Base_URL + "mobile/html/jifen_rule.jsp";
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }
    }

    /**
     * 异常重试
     */
    @Override
    public void doRequestData() {
        progress.setVisibility(View.GONE);
        if (position == 0) {
            queryIntegralCenterByUserid(type);
            return;
        }
        queryIntegralCenterByUserid(typeSelect);
    }
}
