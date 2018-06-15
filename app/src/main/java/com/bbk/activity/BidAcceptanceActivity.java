package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.bbk.Bean.PuDaoBean;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.CommonLoadingView;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 正在接镖
 */
public class BidAcceptanceActivity extends BaseActivity implements CommonLoadingView.LoadingHandler{
    private ListView mlistview;
    private SmartRefreshLayout mrefresh;
    private LinearLayout mbox;
    private ImageView topbar_goback_btn;
    private String type = "";
    private String describe = "";
    private int page = 1, x = 1;
    private boolean isclear = false;
    private BidAcceptanceAdapter adapter;
    private List<Map<String, String>> titlelist;
    private int currentIndex = 1;
    private EditText search_edit;
    private CommonLoadingView zLoadingView;//加载框
    private List<PuDaoBean> puDaoBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_acceptance);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        initView();
        if (getIntent().getStringExtra("type")!= null){
            type = getIntent().getStringExtra("type");
        }
        initData(type,1);
    }
    public void initView(){
        zLoadingView =findViewById(R.id.progress);
        zLoadingView.setLoadingHandler(this);
        titlelist = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbox = (LinearLayout)findViewById(R.id.mbox);
        mrefresh = (SmartRefreshLayout) findViewById(R.id.mrefresh);
        search_edit = (EditText) findViewById(R.id.search_edit);
        mlistview = (ListView)findViewById(R.id.mlistview);
        refreshAndloda();
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    dosearch();
                    return true;
                }
                return false;
            }
        });
    }
    public void initData(String type, final int search){
        mrefresh.setNoMoreData(false);
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("describe",describe);
        maps.put("type",type);
        maps.put("page",page+"");
        RetrofitClient.getInstance(this).createBaseApi().queryBidList(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.optString("status").equals("1")){
                                String content = jsonObject.optString("content");
                                JSONObject object = new JSONObject(content);
                                String moren = object.optString("moren");
                                DialogSingleUtil.dismiss(0);
                                if (search == 2){
                                    puDaoBeans = JSON.parseArray(moren,PuDaoBean.class);
                                    handler.sendEmptyMessageDelayed(1,0);
                                    handler.sendEmptyMessageDelayed(2,0);
                                }else {
                                    if (titlelist.size() == 0){
                                        JSONArray typelist = object.getJSONArray("typelist");
                                        loadtitlekeywords(typelist);
                                    }
                                    puDaoBeans = JSON.parseArray(moren,PuDaoBean.class);
                                    if (x==1){
                                        handler.sendEmptyMessageDelayed(2,0);
                                    }else {
                                        if (puDaoBeans != null && puDaoBeans.size() > 0) {
                                            adapter.notifyData(puDaoBeans);
                                            zLoadingView.loadSuccess();
                                            mlistview.setVisibility(View.VISIBLE);
                                        } else {
                                            zLoadingView.loadSuccess();
                                            mlistview.setVisibility(View.VISIBLE);
//                                            StringUtil.showToast(BidAcceptanceActivity.this, "没有更多了");
                                            mrefresh.finishLoadMoreWithNoMoreData();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    protected void hideDialog() {
                        mrefresh.finishLoadMore();
                        mrefresh.finishRefresh();
                    }
                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(BidAcceptanceActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadError();
                        mlistview.setVisibility(View.GONE);
                        mrefresh.finishLoadMore();
                        mrefresh.finishRefresh();
                        if (BidAcceptanceActivity.class != null){
                            StringUtil.showToast(BidAcceptanceActivity.this, e.message);
                        }
                    }
                });
    }
    private void refreshAndloda() {
        mrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                page = 1;
                x = 1;
                initData(type,1);
            }
        });
        mrefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                x = 2;
                initData(type,1);
            }
        });
    }
    private void dosearch(){
        if (search_edit.getText().toString().isEmpty()) {
            StringUtil.showToast(this, "搜索内容不能为空");
        }else{
			/* 隐藏软键盘 */
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        0);
            }
            type = "";
            describe = search_edit.getText().toString();
            x = 1;
            page = 1;
            initData(type,2);
        }
    }
    private void updateTitle(int position) {
        titlelist.get(currentIndex).put("isselect", "0");
        titlelist.get(position).put("isselect", "1");
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        title1.setTextColor(Color.parseColor("#b40000"));
        henggang1.setBackgroundColor(Color.parseColor("#b40000"));
        View view4 = mbox.getChildAt(currentIndex);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        title3.setTextColor(Color.parseColor("#666666"));
        henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        // mhscrollview.scrollTo(view.getLeft() - 200, 0);
        currentIndex = position;
        describe = "";
        if (position == 1) {
            isclear = true;
            type = "";
            page = 1;
            x = 1;
            initData(type,1);
        } else {
            isclear = true;
            page = 1;
            x = 1;
            type = titlelist.get(position).get("keyword");
            initData(type,1);
        }
    }
    // 一级菜单一
    private void addtitle(final String text, final int i) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.super_item_title, null);
        //设置view的weight为1，保证导航铺满当前页面
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(this, 0), 0, BaseTools.getPixelsFromDp(this, 0), 0);
        if (i == 0) {
            view.setVisibility(View.GONE);
        }
        //判断如果传进来的type值与当前页面值一样则显示当然页面选项
        if (type.equals(text)) {
            title.setTextColor(Color.parseColor("#b40000"));
            henggang.setBackgroundColor(Color.parseColor("#b40000"));
        }
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != currentIndex) {
                    mbox.getChildAt(0).setVisibility(View.GONE);
                    updateTitle(i);
                }
            }

        });
        mbox.addView(view);
    }

    private void loadtitlekeywords(JSONArray searchwords) throws JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("keyword", "我的搜索");
        map.put("isselect", "0");
        titlelist.add(map);
        addtitle("我的搜索", 0);
        // searchwords=searchwords+"|";
        for (int i = 0; i < searchwords.length(); i++) {
            Map<String, String> map2 = new HashMap<>();
            String  keyword = searchwords.getJSONObject(i).optString("name");
            map2.put("keyword", keyword);
            map2.put("isselect", "0");
            titlelist.add(map2);
            addtitle(keyword, i + 1);
            if (!"".equals(type)){
                if (type.equals(keyword)){
                    currentIndex = i+1;
                }
            }
        }
    }

    @Override
    public void doRequestData() {
        zLoadingView.setVisibility(View.GONE);
        initData(type,1);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    mbox.getChildAt(0).setVisibility(View.VISIBLE);
                    titlelist.get(currentIndex).put("isselect", "0");
                    titlelist.get(0).put("isselect", "1");
                    View view = mbox.getChildAt(0);
                    TextView title1 = (TextView) view.findViewById(R.id.item_title);
                    View henggang1 = view.findViewById(R.id.bottom_view);
                    title1.setTextColor(Color.parseColor("#b40000"));
                    henggang1.setBackgroundColor(Color.parseColor("#b40000"));
                    View view4 = mbox.getChildAt(currentIndex);
                    TextView title3 = (TextView) view4.findViewById(R.id.item_title);
                    View henggang3 = view4.findViewById(R.id.bottom_view);
                    title3.setTextColor(Color.parseColor("#666666"));
                    henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
                    currentIndex = 0;
                    break;
                case 2:
                    if (puDaoBeans != null && puDaoBeans.size() > 0) {
                        mrefresh.setEnableLoadMore(true);
                        adapter = new BidAcceptanceAdapter(BidAcceptanceActivity.this, puDaoBeans);
                        mlistview.setAdapter(adapter);
                        zLoadingView.loadSuccess();
                        mlistview.setVisibility(View.VISIBLE);
                    }else {
                        zLoadingView.setVisibility(View.VISIBLE);
                        zLoadingView.loadSuccess(true);
                        mlistview.setVisibility(View.GONE);
                        mrefresh.setEnableLoadMore(false);
                    }
                    break;
            }
        }
    };
}
