package com.bbk.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.bbk.activity.BaseActivity;
import com.bbk.activity.BidAcceptanceActivity;
import com.bbk.activity.BidDetailActivity;
import com.bbk.activity.R;
import com.bbk.adapter.BidAcceptanceAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.BaseTools;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ImmersionUtil;
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
public class BidAcceptanceFragment extends BaseViewPagerFragment implements ResultEvent{
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
    private View mView;
    private View data_head;
    HorizontalScrollView mHorizontalScrollView;
    private LinearLayout mNoMessageLayout;//无数据显示页面

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (null == mView) {
            dataFlow = new DataFlow6(getActivity());
            getActivity().getWindow().setBackgroundDrawable(null);
            mView = inflater.inflate(R.layout.activity_bid_acceptance, null);
            data_head = mView.findViewById(R.id.data_head);
            ImmersionUtil.initstateView(getActivity(),data_head);
            initView();
            initData(1);
        }
        return mView;
    }

    public void initView(){
        list = new ArrayList<>();
        titlelist = new ArrayList<>();
        topbar_goback_btn = mView.findViewById(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mbox = mView.findViewById(R.id.mbox);
        mrefresh = mView.findViewById(R.id.mrefresh);
        search_edit =  mView.findViewById(R.id.search_edit);
        mlistview = mView.findViewById(R.id.mlistview);
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
        mNoMessageLayout = mView.findViewById(R.id.no_message_layout);
        mHorizontalScrollView = mView.findViewById(R.id.mhscrollview);
    }
    public void initData(int requestCode){
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
                    initData(1);
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                    page++;
                    isclear = false;
                    initData(1);

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        mrefresh.setCustomFooterView(footView);
    }
    private void dosearch(){
        if (search_edit.getText().toString().isEmpty()) {
            if (toast!= null) {
                toast.cancel();
            }
            toast = Toast.makeText(getActivity(), "搜索内容不能为空", Toast.LENGTH_SHORT);
            toast.show();
        }else{
			/* 隐藏软键盘 */
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        0);
            }
            type = "";
            describe = search_edit.getText().toString();
            initData(2);
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
        type = titlelist.get(position).get("keyword");
        isclear = true;
        initData(1);
    }
    // 一级菜单一
    private void addtitle(final String text, final int i) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.super_item_title, null);
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(getActivity(), 0), 0, BaseTools.getPixelsFromDp(getActivity(), 0), 0);
        if (i == 0) {
            view.setVisibility(View.GONE);
        }
        if (i == 1) {
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
                    Log.i("TAG",array.toString()+"---------");
                    if (array.toString().equals("[]")){
                        mNoMessageLayout.setVisibility(View.VISIBLE);
                        mlistview.setVisibility(View.GONE);
                        mHorizontalScrollView.setVisibility(View.VISIBLE);
                    }else {
                        addList(array);
                        adapter = new BidAcceptanceAdapter(getActivity(),list);
                        mlistview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        mlistview.setVisibility(View.VISIBLE);
                        mNoMessageLayout.setVisibility(View.GONE);
                        mHorizontalScrollView.setVisibility(View.VISIBLE);
                        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), BidDetailActivity.class);
                                intent.putExtra("id",list.get(position).get("id"));
                                intent.putExtra("status",list.get(position).get("status"));
                                startActivity(intent);
                            }
                        });
                    }
                    adapter.notifyDataSetChanged();
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
                    adapter.notifyDataSetChanged();
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lazyLoad() {

    }
}
