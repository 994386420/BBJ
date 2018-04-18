package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.view.MyFootView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接镖_01_列表
 */
public class BidAcceptanceActivity extends BaseActivity implements ResultEvent{
    private ListView mlistview;
    private XRefreshView mrefresh;
    private LinearLayout mbox;
    private ImageView topbar_goback_btn;
    private String type = "";
    private String describe = "";
    private DataFlow6 dataFlow;
    private int page = 1;
    private List<Map<String,String>> list;
    private boolean isclear = false;
    private BidAcceptanceAdapter adapter;
    private List<Map<String, String>> titlelist;
    private int currentIndex = 1;
    private EditText search_edit;
    private Toast toast;
    private LinearLayout mNoMessageLayout;//无数据显示页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_acceptance);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        initView();
        if (getIntent().getStringExtra("type")!= null){
            type = getIntent().getStringExtra("type");
        }
        initData(type,1);
//        if (getIntent().getStringExtra("describe")!= null){
//            describe = getIntent().getStringExtra("describe");
//            initData(2);
//        }

    }
    public void initView(){
        list = new ArrayList<>();
        titlelist = new ArrayList<>();
        topbar_goback_btn= (ImageView) findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mbox = (LinearLayout)findViewById(R.id.mbox);
        mrefresh = (XRefreshView)findViewById(R.id.mrefresh);
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
        mNoMessageLayout = findViewById(R.id.no_message_layout);
    }
    public void initData(String type,int requestCode){
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("describe",describe);
        paramsMap.put("type",type);
        paramsMap.put("page",page+"");
        dataFlow.requestData(requestCode, "bid/queryBidList", paramsMap, this,true);
    }
    private void refreshAndloda() {
        mrefresh.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                    isclear = true;
                    page = 1;
                    initData(type,1);
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                    page++;
                    isclear = false;
                    initData(type,1);

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(this);
        mrefresh.setCustomFooterView(footView);
    }
    private void dosearch(){
        if (search_edit.getText().toString().isEmpty()) {
            if (toast!= null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT);
            toast.show();
        }else{
			/* 隐藏软键盘 */
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        0);
            }
            type = "";
            describe = search_edit.getText().toString();
            initData(type,2);
        }
    }
    private void updateTitle(int position) {
        titlelist.get(currentIndex).put("isselect", "0");
        titlelist.get(position).put("isselect", "1");
        View view = mbox.getChildAt(position);
        TextView title1 = (TextView) view.findViewById(R.id.item_title);
        View henggang1 = view.findViewById(R.id.bottom_view);
        title1.setTextColor(Color.parseColor("#ff7d41"));
        henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
        View view4 = mbox.getChildAt(currentIndex);
        TextView title3 = (TextView) view4.findViewById(R.id.item_title);
        View henggang3 = view4.findViewById(R.id.bottom_view);
        title3.setTextColor(Color.parseColor("#666666"));
        henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
        // mhscrollview.scrollTo(view.getLeft() - 200, 0);
        currentIndex = position;
        describe = "";
//        type = titlelist.get(position).get("keyword");
//        isclear = true;
//        initData(1);
        if (position == 1) {
            isclear = true;
            type = "";
            page = 1;
            initData(type,1);
        } else {
            isclear = true;
            page = 1;
            type = titlelist.get(position).get("keyword");
            initData(type,1);
        }
    }
    // 一级菜单一
    private void addtitle(final String text, final int i) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.super_item_title, null);
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
            title.setTextColor(Color.parseColor("#ff7d41"));
            henggang.setBackgroundColor(Color.parseColor("#ff7d41"));
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
    public void addList(JSONArray array) throws JSONException {
        if (array.length()<10) {
            mrefresh.setPullLoadEnable(false);
        }else{
            mrefresh.setPullLoadEnable(true);
        }
        for (int i = 0; i < array.length() ; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("endtime",object.optString("endtime"));
            map.put("id",object.optString("id"));
            map.put("img",object.optString("img"));
            map.put("title",object.optString("title"));
            map.put("price",object.optString("price"));
            map.put("bidprice",object.optString("bidprice"));
            map.put("number",object.optString("number"));
            map.put("type",object.optString("type"));
            map.put("url",object.optString("url"));
            Log.i("商品状态++++++",object.optString("status"));
            map.put("status",object.optString("status"));
            list.add(map);
        }
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        mrefresh.stopLoadMore();
        mrefresh.stopRefresh();
        try {
            switch (requestCode){
                case 1:
                    JSONObject object = new JSONObject(content);
                    if (isclear){
                        list.clear();
                    }
                    if (titlelist.size() == 0){
                        JSONArray typelist = object.getJSONArray("typelist");
                        loadtitlekeywords(typelist);
                    }
                    JSONArray array = object.getJSONArray("moren");
                    addList(array);
                    if (list != null && list.size() > 0){
                        adapter = new BidAcceptanceAdapter(this,list);
                        mlistview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        mlistview.setVisibility(View.VISIBLE);
                        mNoMessageLayout.setVisibility(View.GONE);
                    }else {
                        mlistview.setVisibility(View.GONE);
                        mNoMessageLayout.setVisibility(View.VISIBLE);
                    }
                    mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(BidAcceptanceActivity.this, BidDetailActivity.class);
                            intent.putExtra("id",list.get(position).get("id"));
                            intent.putExtra("status",list.get(position).get("status"));
                            startActivity(intent);
                        }
                    });
                    isclear = false;
                    break;
                case 2:
                    list.clear();
                    mbox.getChildAt(0).setVisibility(View.VISIBLE);
                    titlelist.get(currentIndex).put("isselect", "0");
                    titlelist.get(0).put("isselect", "1");
                    View view = mbox.getChildAt(0);
                    TextView title1 = (TextView) view.findViewById(R.id.item_title);
                    View henggang1 = view.findViewById(R.id.bottom_view);
                    title1.setTextColor(Color.parseColor("#ff7d41"));
                    henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));

                    View view4 = mbox.getChildAt(currentIndex);
                    TextView title3 = (TextView) view4.findViewById(R.id.item_title);
                    View henggang3 = view4.findViewById(R.id.bottom_view);
                    title3.setTextColor(Color.parseColor("#666666"));
                    henggang3.setBackgroundColor(Color.parseColor("#ffffff"));
                    // mhscrollview.scrollTo(view.getLeft() - 200, 0);
                    currentIndex = 0;
                    JSONObject object1 = new JSONObject(content);
                    JSONArray array1 = object1.getJSONArray("moren");
                    addList(array1);
                    if (list != null && list.size() > 0){
                        adapter = new BidAcceptanceAdapter(this,list);
                        mlistview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        mlistview.setVisibility(View.VISIBLE);
                        mNoMessageLayout.setVisibility(View.GONE);
                    }else {
                        mlistview.setVisibility(View.GONE);
                        mNoMessageLayout.setVisibility(View.VISIBLE);
                    }
                    mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(BidAcceptanceActivity.this, BidDetailActivity.class);
                            intent.putExtra("id",list.get(position).get("id"));
                            startActivity(intent);
                        }
                    });
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
