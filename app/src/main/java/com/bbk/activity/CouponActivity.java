package com.bbk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.NewFxBean;
import com.bbk.adapter.CouponListAdapter;
import com.bbk.adapter.FindListAdapter;
import com.bbk.adapter.HomeTitleGridAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.StringUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class CouponActivity extends BaseActivity implements OnClickListener{

    private DataFlow dataFlow;
    private List<Map<String, String>> list;
    private LinearLayout mbox;
    private CouponListAdapter adapter;
    private ListView mlistview;
    private int page = 1,x= 1;
    private SmartRefreshLayout xrefresh;
    private boolean isloadmore = false;
    private List<Map<String, String>> titlelist;
    private HomeTitleGridAdapter titleadapter;
    private PopupWindow window;
    private LinearLayout monclickimg;
    private ImageView unfold_img;
    private ImageButton goBackBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_coupon);
        dataFlow = new DataFlow(this);
        View topView = findViewById(R.id.search_head);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
        initData();
    }

    public void initView() {
        list = new ArrayList<>();
        titlelist = new ArrayList<>();
        goBackBtn =  findViewById(R.id.topbar_goback_btn);
        goBackBtn.setOnClickListener(this);
        monclickimg = (LinearLayout) findViewById(R.id.monclickimg);
        monclickimg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (window != null && window.isShowing()) {
                    window.dismiss();
                    unfold_img.setImageResource(R.mipmap.enter_down);
                } else {
                    window.showAsDropDown(v);
                    unfold_img.setImageResource(R.mipmap.enter_up);
                }
            }
        });
        unfold_img = (ImageView) findViewById(R.id.unfold_img);
        mbox = (LinearLayout) findViewById(R.id.mbox);
        mlistview = (ListView) findViewById(R.id.mlistview);
//        mlistview.setOnScrollListener(new OnScrollListener() {
//
//            @Override
//            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        // 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
//                        Glide.with(CouponActivity.this).pauseRequests();
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        // 当ListView处于静止状态时，继续加载图片
//                        Glide.with(CouponActivity.this).resumeRequests();
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
//                // TODO Auto-generated method stub
//
//            }
//        });
        xrefresh = (SmartRefreshLayout) findViewById(R.id.xrefresh);
        refreshAndloda();
    }

    private void refreshAndloda() {
        xrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                initData();
            }
        });
        xrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if (isloadmore) {
                    page++;
                    x = 2;
                    initData();
                }
            }
        });
    }

    private void initData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        RetrofitClient.getInstance(this).createBaseApi().queryYouhuilist(
                params, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                JSONObject object = new JSONObject(content);
                                if (x == 1) {
                                    if (mbox != null) {
                                        mbox.removeAllViews();
                                    }
                                    list.clear();
                                    JSONArray list = object.getJSONArray("list");
                                    JSONArray typelist = object.getJSONArray("typelist");
                                    loadbox(typelist);
                                    loaglist(list);
                                }else {
                                    JSONArray list = object.getJSONArray("list");
                                    loaglist(list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        initPopupWindow();
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                    }

                    @Override
                    protected void showDialog() {
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        xrefresh.finishLoadMore();
                        xrefresh.finishRefresh();
                        StringUtil.showToast(CouponActivity.this, "网络异常");
                    }
                });
    }


    private void initPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.home_popupwindow, null);
        GridView mtitlegridview = (GridView) popupView.findViewById(R.id.mtitlegridview);
        titleadapter = new HomeTitleGridAdapter(this, titlelist);
        mtitlegridview.setAdapter(titleadapter);
        mtitlegridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent = new Intent(CouponActivity.this, WebViewActivity.class);
                intent.putExtra("url", titlelist.get(position).get("url"));
                startActivity(intent);
                window.dismiss();
                unfold_img.setImageResource(R.mipmap.enter_down);
            }

        });
        window = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // TODO: 2016/5/17 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        // TODO: 2016/5/17 设置可以获取焦点
        window.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        window.update();
//		// TODO: 2016/5/17 以下拉的方式显示，并且可以设置显示的位置
//		window.showAsDropDown(monclickimg, 0, 20);
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                unfold_img.setImageResource(R.mipmap.enter_down);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_goback_btn:
                finish();
                break;
            default:
                break;
        }
    }

    private void loaglist(JSONArray arr) throws JSONException {
        if (arr.length() < 10) {
            isloadmore = false;
            xrefresh.setEnableLoadMore(false);
        } else {
            isloadmore = true;
            xrefresh.setEnableLoadMore(true);
        }
        for (int i = 0; i < arr.length(); i++) {
            JSONObject object = arr.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("title", object.optString("title"));
            map.put("price", object.optString("price"));
            map.put("img", object.optString("img"));
            map.put("url", object.optString("url"));
            map.put("rowkey", object.optString("rowkey"));
            map.put("youhui", object.optString("youhui"));
            map.put("st", object.optString("st"));
            map.put("et", object.optString("et"));
            map.put("sale", object.optString("sale"));
            if (!object.optString("down").isEmpty()) {
                map.put("down", object.optString("down"));
                map.put("bprice", object.optString("bprice"));
            }
            list.add(map);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new CouponListAdapter(list, this);
            mlistview.setAdapter(adapter);
            mlistview.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent intent = new Intent(CouponActivity.this, WebViewActivity.class);
                    intent.putExtra("url", list.get(arg2).get("url"));
                    intent.putExtra("rowkey", list.get(arg2).get("rowkey"));
                    startActivity(intent);
                }
            });
        }
    }

    private void loadbox(JSONArray typelist) throws JSONException {
        titlelist.clear();
        for (int i = 0; i < typelist.length(); i++) {
            JSONObject object = typelist.getJSONObject(i);
            addbox(object);
            Map<String, String> map = new HashMap<>();
            map.put("url", object.optString("url"));
            map.put("name", object.optString("domain"));
            map.put("isselect", "0");
            titlelist.add(map);
        }
//        titleadapter.notifyDataSetChanged();
    }

    private void addbox(final JSONObject object) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.coupon_box_text, null);
        TextView tv = (TextView) view.findViewById(R.id.mtext);
        tv.setText(object.optString("domain"));
        tv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(CouponActivity.this, WebViewActivity.class);
                intent.putExtra("url", object.optString("url"));
                startActivity(intent);
            }
        });
        mbox.addView(view);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
}
