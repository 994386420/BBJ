package com.bbk.activity;

import java.lang.reflect.Field;
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
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.R;
import com.bbk.activity.ResultMainActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.CouponListAdapter;
import com.bbk.adapter.HomeTitleGridAdapter;
import com.bbk.adapter.ListViewAdapter;
import com.bbk.dao.SearchHistoryDao;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DensityUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ValidatorUtil;
import com.bbk.view.MyFootView;
import com.bumptech.glide.Glide;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class CouponActivity extends BaseActivity implements OnClickListener, ResultEvent {

    private DataFlow dataFlow;

    private List<Map<String, String>> list;
    private View search_head;
    private LinearLayout mbox;
    private CouponListAdapter adapter;
    private ListView mlistview;
    private int page = 1;

    private XRefreshView xrefresh;
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
//        search_head = findViewById(R.id.);
//        ImmersedStatusbarUtils.FlymeSetStatusBarLightMode(getWindow(),true);
//        ImmersedStatusbarUtils.MIUISetStatusBarLightMode(this,true);
//        initstateView();
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
        mlistview.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        // 当ListView处于滑动状态时，停止加载图片，保证操作界面流畅
                        Glide.with(CouponActivity.this).pauseRequests();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 当ListView处于静止状态时，继续加载图片
                        Glide.with(CouponActivity.this).resumeRequests();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }
        });
        xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
        refreshAndloda();
    }

    private void refreshAndloda() {
        xrefresh.setXRefreshViewListener(new XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                page = 1;
                initData();

            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (isloadmore) {
                    page++;
                    loadData();
                }
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(this);
        xrefresh.setCustomFooterView(footView);
    }

    private void initData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        dataFlow.requestData(1, "newService/queryYouhuilist", params, this);
        initPopupWindow();
    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        dataFlow.requestData(2, "newService/queryYouhuilist", params, this);
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
        if (arr.length() < 20) {
            isloadmore = false;
            xrefresh.setLoadComplete(true);
        } else {
            isloadmore = true;
            xrefresh.setAutoLoadMore(true);
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
        titleadapter.notifyDataSetChanged();
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
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        xrefresh.stopLoadMore();
        xrefresh.stopRefresh();
        switch (requestCode) {
            case 1:
                try {
                    if (mbox != null) {
                        mbox.removeAllViews();
                    }
                    list.clear();
                    JSONObject object = new JSONObject(content);
                    JSONArray list = object.getJSONArray("list");
                    JSONArray typelist = object.getJSONArray("typelist");
                    loadbox(typelist);
                    loaglist(list);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 2:
                JSONObject object;
                try {
                    object = new JSONObject(content);
                    JSONArray list = object.getJSONArray("list");
                    loaglist(list);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }

    }

    private int getStatusBarHeight() {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

//    private void initstateView() {
//        if (Build.VERSION.SDK_INT >= 19) {
//            search_head.setVisibility(View.VISIBLE);
//        }
//        int result = getStatusBarHeight();
//        LayoutParams layoutParams = search_head.getLayoutParams();
//        layoutParams.height = result;
//        search_head.setLayoutParams(layoutParams);
//    }
    @Override
    public void onResume() {
        super.onResume();

    }
}
