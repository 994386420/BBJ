package com.bbk.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.Bean.HomeData;
import com.bbk.Decoration.TwoDecoration;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.SearchMainActivity;
import com.bbk.activity.SortActivity;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.HomeAdapter;
import com.bbk.adapter.HomeMyGridAdapter;
import com.bbk.adapter.HomeTitleGridAdapter;
import com.bbk.dialog.HomeAlertDialog;
import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.BaseTools;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyFootView;
import com.bbk.view.MyXRefresh;
import com.bumptech.glide.Glide;


public class HomeFragment2 extends BaseViewPagerFragment implements OnClickListener, ResultEvent {
    private DataFlow dataFlow;
    private View mView, mzhuangtai;
    public static int iscontent = 0;
    private boolean isshowzhezhao = true;
    private int currentIndex = 0;
    private List<Map<String, String>> titlelist;
    private HomeTitleGridAdapter titleadapter;
    private List<Map<String, String>> taglist;
    private List<Map<String, String>> guesslikelist;
    private List<Map<String, Object>> gridlist, gridlist1;
    private int page = 1;
    private int beforindex = -1;
    private boolean isclear = false;
    private String token;
    private int seepage = 1;
    private PopupWindow window;
    private Thread thread;
    private boolean isrequest = true;
    private int requestnum = 0;
    private int removenum = 0;
    public static boolean risrequest = false;
    public static boolean gisrequest = false;
    private HomeAdapter adapter;
    private XRefreshView  xrefresh2;
    private MyXRefresh mrefresh;
    private RecyclerView mrecyclerview;
    private LinearLayout mbox;
    private LinearLayout monclickimg;
    public ImageView unfold_img;
    private RelativeLayout mzhanwei;
    private TextView mchongshi;
    private HorizontalScrollView mhscrollview;
    private LinearLayout msearch, msort;
    private LayoutInflater inflater1;
    private ViewGroup container1;
    private Bundle savedInstanceState1;
    private GridView mtitleresultgrid;
    private HomeMyGridAdapter gridadapter;
    private JSONArray banner = new JSONArray();
    private JSONArray activity = new JSONArray();
    private JSONArray articles = new JSONArray();
    private JSONArray dianpu = new JSONArray();
    private JSONArray tag = new JSONArray();
    private JSONArray tujian = new JSONArray();
    private JSONArray gongneng = new JSONArray();
    private List<HomeData> arrlist;
    private HomeData homeData;
    private ImageView huodongimg;


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
        inflater1 = inflater;
        container1 = container;
        savedInstanceState1 = savedInstanceState;
        if (null != getActivity().getIntent().getStringExtra("content")) {
            String content = getActivity().getIntent().getStringExtra("content");
            try {
//                iscontent = 1;
                isshowzhezhao = false;
                JSONObject jsonObject = new JSONObject(content);
                EventIdIntentUtil.EventIdIntent(getActivity(), jsonObject);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        if (null == mView) {
            dataFlow = new DataFlow(getContext());
            getActivity().getWindow().setBackgroundDrawable(null);
                mView = inflater.inflate(R.layout.fragment_home2, null);
            View topView = mView.findViewById(R.id.lin);
            // 实现沉浸式状态栏
            ImmersedStatusbarUtils.initAfterSetContentView(getActivity(), topView);
            initView();
            initData(true);
        }
        return mView;

    }

    private void initData(boolean is) {
        HashMap<String, String> paramsMap = new HashMap<>();
        dataFlow.requestData(1, "newService/queryIndexInfo", paramsMap, this, is);
        loadTuijian();
		loadGuessLike();
        initPopupWindow();
    }

    /**
     *
     */
    private void initView() {
        arrlist = new ArrayList<>();
        gridlist = new ArrayList<>();
        gridlist1 = new ArrayList<>();
        titlelist = new ArrayList<>();
        taglist = new ArrayList<>();
        guesslikelist = new ArrayList<>();
        homeData = new HomeData();
        homeData.setActivity(activity);
        homeData.setArticles(articles);
        homeData.setBanner(banner);
        homeData.setDianpu(dianpu);
        homeData.setTag(tag);
        homeData.setTujian(tujian);
        homeData.setGongneng(gongneng);
        arrlist.add(homeData);
        TelephonyManager TelephonyMgr = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        if (TelephonyMgr.getDeviceId() != null) {
            token = TelephonyMgr.getDeviceId();
        } else {
            //android.provider.Settings;
            token = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "userInfor", "token", token);
        msort = (LinearLayout) mView.findViewById(R.id.msort);
        msearch = (LinearLayout) mView.findViewById(R.id.msearch);
        mzhuangtai = mView.findViewById(R.id.mzhuangtai);
        mhscrollview = (HorizontalScrollView) mView.findViewById(R.id.mhscrollview);
        monclickimg = (LinearLayout) mView.findViewById(R.id.monclickimg);
        mbox = (LinearLayout) mView.findViewById(R.id.mbox);
        unfold_img = (ImageView) mView.findViewById(R.id.unfold_img);
        huodongimg = (ImageView) mView.findViewById(R.id.huodongimg);
        mzhanwei = (RelativeLayout) mView.findViewById(R.id.mzhanwei);
        mchongshi = (TextView) mView.findViewById(R.id.mchongshi);
        mrefresh = (MyXRefresh) mView.findViewById(R.id.xrefresh);
        xrefresh2 = (XRefreshView) mView.findViewById(R.id.xrefresh2);
        mtitleresultgrid = (GridView) mView.findViewById(R.id.mtitleresultgrid);
        mrecyclerview = (RecyclerView) mView.findViewById(R.id.mrecycler);
        refreshAndloda();
        initstateView();

        monclickimg.setOnClickListener(this);
        mchongshi.setOnClickListener(this);
        msearch.setOnClickListener(this);
        msort.setOnClickListener(this);

    }

    @SuppressWarnings("deprecation")
    private void refreshAndloda() {
        mrefresh.setXRefreshViewListener(new XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                isclear = true;
                if (currentIndex == 0) {
                    seepage = 1;
                    guesslikelist.clear();
                    initData(false);
                    inittuijianandseelike();
                }
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (currentIndex == 0) {
                    seepage++;
                    loadmoreseelike();
                }
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });
        MyFootView footView = new MyFootView(getActivity());
        mrefresh.setCustomFooterView(footView);
        xrefresh2.setXRefreshViewListener(new XRefreshViewListener() {

            @Override
            public void onRelease(float direction) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onRefresh(boolean isPullDown) {
                isclear = true;
                if (currentIndex == 0) {

                } else {
                    page = 1;
                    initgriddata();
                }
            }

            @Override
            public void onRefresh() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                if (currentIndex == 0) {

                } else {
                    page++;
                    initgriddata();
                }

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {
                // TODO Auto-generated method stub

            }
        });


        MyFootView footView1 = new MyFootView(getActivity());
        xrefresh2.setCustomFooterView(footView1);
    }

    private void inittuijianandseelike() {
        new Thread() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", token);
                String hometuijian = HttpUtil.getHttp(params,
                        Constants.MAIN_BASE_URL_MOBILE + "newService/queryIndexTuijianByToken", null);
                if (hometuijian != null && !"".equals(hometuijian)) {
                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "homedata", "hometuijian",
                            hometuijian);
                }
                Map<String, String> param = new HashMap<String, String>();
                param.put("token", token);
                param.put("page", "1");
                String seelike = HttpUtil.getHttp(param,
                        Constants.MAIN_BASE_URL_MOBILE + "newService/queryIndexSeeByToken", null);
                if (seelike != null && !"".equals(seelike)) {
                    SharedPreferencesUtil.putSharedData(MyApplication.getApplication(), "homedata", "seelike", seelike);
                }
            }

            ;
        }.start();
    }

    private void loadmoreseelike() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("token", token);
        paramsMap.put("page", seepage + "");
        dataFlow.requestData(3, "newService/queryIndexSeeByToken", paramsMap, this);
    }

    /**
     * 切换标题选中
     */
    private void updateTitle(int position) {
        titlelist.get(currentIndex).put("isselect", "0");
        titlelist.get(position).put("isselect", "1");
        titleadapter.notifyDataSetChanged();
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
        mhscrollview.scrollTo(view.getLeft() - 200, 0);
        if (position == 0) {
            beforindex = position;
            mrefresh.setVisibility(View.VISIBLE);
            xrefresh2.setVisibility(View.GONE);
        } else {
            mrefresh.setVisibility(View.GONE);
            xrefresh2.setVisibility(View.VISIBLE);
            if (beforindex != position) {
                isclear = true;
                page = 1;
                currentIndex = position;
                initgriddata();
            }
        }
        currentIndex = position;
    }

    private void initgriddata() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("keyword", titlelist.get(currentIndex).get("keyword"));
        paramsMap.put("page", page + "");
        dataFlow.requestData(2, "newService/queryIndexSearchList", paramsMap, this);
    }

    private void initPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View popupView = inflater.inflate(R.layout.home_popupwindow, null);
        GridView mtitlegridview = (GridView) popupView.findViewById(R.id.mtitlegridview);
        titleadapter = new HomeTitleGridAdapter(getActivity(), titlelist);
        mtitlegridview.setAdapter(titleadapter);
        mtitlegridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position != currentIndex) {
                    updateTitle(position);
                    window.dismiss();
                    unfold_img.setImageResource(R.mipmap.enter_down);
                }

            }

        });
        window = new PopupWindow(popupView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        //  设置可以获取焦点
        window.setFocusable(true);
        // 设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        // 以下拉的方式显示，并且可以设置显示的位置
        // window.showAsDropDown(monclickimg, 0, 20);
        window.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                unfold_img.setImageResource(R.mipmap.enter_down);
            }
        });

    }

    // 测量状态栏高度
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

            sbar = getContext().getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    // 根据API给状态栏加高度
    private void initstateView() {
        if (Build.VERSION.SDK_INT >= 19) {
            mzhuangtai.setVisibility(View.VISIBLE);
        }
        int result = getStatusBarHeight();
        LayoutParams layoutParams = mzhuangtai.getLayoutParams();
        layoutParams.height = result;
        mzhuangtai.setLayoutParams(layoutParams);
    }

    // 一级菜单一
    private void addtitle(final String text, final int i, String keyword) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.super_item_title, null);
        final TextView title = (TextView) view.findViewById(R.id.item_title);
        final View henggang = view.findViewById(R.id.bottom_view);
        title.setText(text);
        title.setTextColor(Color.parseColor("#666666"));
        henggang.setBackgroundColor(Color.parseColor("#ffffff"));
        view.setPadding(BaseTools.getPixelsFromDp(getContext(), 0), 0, BaseTools.getPixelsFromDp(getContext(), 0), 0);
        if (i == 0) {
            title.setTextColor(Color.parseColor("#ff7d41"));
            henggang.setBackgroundColor(Color.parseColor("#ff7d41"));
        }
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (i != currentIndex) {
                    updateTitle(i);
                }
            }

        });
        mbox.addView(view);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.monclickimg:
                if (window != null && window.isShowing()) {
                    window.dismiss();
                    unfold_img.setImageResource(R.mipmap.enter_down);
                } else {
                    window.showAsDropDown(v);
                    unfold_img.setImageResource(R.mipmap.enter_up);
                }
                break;
            case R.id.msort:
                intent = new Intent(getActivity(), SortActivity.class);
                startActivity(intent);
                break;
            case R.id.msearch:
                intent = new Intent(getActivity(), SearchMainActivity.class);
                startActivity(intent);
                break;
            case R.id.mchongshi:
                isclear = true;
                if (currentIndex == 0) {
                    seepage = 1;
                    guesslikelist.clear();
                    initData(false);
                    inittuijianandseelike();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void loadLazyData() {

    }


    private void loadtitlekeywords(JSONArray searchwords) {
        Map<String, String> map = new HashMap<>();
        map.put("name", "首页");
        map.put("keyword", "首页");
        map.put("isselect", "1");
        titlelist.add(map);
        addtitle("首页", 0, "首页");
        for (int i = 0; i < searchwords.length(); i++) {
            JSONObject object = searchwords.optJSONObject(i);
            String name = object.optString("name");
            String keyword = object.optString("keyword");
            Map<String, String> map1 = new HashMap<>();
            map1.put("name", name);
            map1.put("keyword", keyword);
            map1.put("isselect", "0");
            titlelist.add(map1);
            addtitle(name, i + 1, keyword);
        }
//		vadapter.notifyDataSetChanged();
        titleadapter.notifyDataSetChanged();

    }


    private void loadGuessLike() {
        String seelike = SharedPreferencesUtil.getSharedData(getActivity(), "homedata", "seelike");
        if (seelike != null && !"".equals(seelike)) {
            initseelike(seelike);
        } else {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("token", token);
            paramsMap.put("page", "1");
            dataFlow.requestData(5, "newService/queryIndexTuijianByToken", paramsMap, this, false);
        }

    }

    private void loadTuijian() {
        String hometuijian = SharedPreferencesUtil.getSharedData(getActivity(), "homedata", "hometuijian");
        if (hometuijian != null && !"".equals(hometuijian)) {
            try {
                JSONObject jo = new JSONObject(hometuijian);
                tujian = jo.optJSONArray("content");
                homeData.setTujian(tujian);
                arrlist.clear();
                arrlist.add(homeData);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("token", token);
            dataFlow.requestData(4, "newService/queryIndexTuijianByToken", paramsMap, this, false);
        }

    }

    private void initseelike(String seelike) {
        try {
            JSONObject jo = new JSONObject(seelike);
            JSONArray content = jo.optJSONArray("content");
            for (int i = 0; i < content.length(); i++) {
                JSONObject object = content.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                map.put("title", object.optString("title"));
                map.put("price", object.optString("price"));
                map.put("img", object.optString("img"));
                map.put("url", object.optString("url"));
                map.put("rowkey", object.optString("rowkey"));
                map.put("domain", object.optString("domain"));
                map.put("youhui", object.optString("youhui"));
                map.put("st", object.optString("st"));
                map.put("et", object.optString("et"));
                map.put("sale", object.optString("sale"));
                if (!object.optString("down").isEmpty()) {
                    map.put("down", object.optString("down"));
                    map.put("bprice", object.optString("bprice"));
                }
                guesslikelist.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void loadgridview(JSONArray arr) {
        if (arr.length()<10){
            xrefresh2.setPullLoadEnable(false);
        }else {
            xrefresh2.setPullLoadEnable(true);
        }
        for (int i = 0; i < arr.length(); i++) {
            HashMap<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("img", arr.optJSONObject(i).optString("imgUrl"));
            itemMap.put("title", arr.optJSONObject(i).optString("title").replace("<span>", "").replace("</span>", ""));
            String price = arr.optJSONObject(i).optString("price");
            itemMap.put("price", price);
            itemMap.put("hnumber", arr.optJSONObject(i).optString("comnum"));
            itemMap.put("domainCount", arr.optJSONObject(i).optString("domainCount"));
            itemMap.put("groupRowKey", arr.optJSONObject(i).optString("groupRowkey"));
            itemMap.put("quote", arr.optJSONObject(i).optString("numberCount"));
            itemMap.put("allDomain", arr.optJSONObject(i).optString("alldomain"));
            itemMap.put("hassimi", arr.optJSONObject(i).optString("hassimi"));
            itemMap.put("tarr", arr.optJSONObject(i).optString("tarr").toString());
            itemMap.put("domain1", arr.optJSONObject(i).optString("domain"));
            itemMap.put("url", arr.optJSONObject(i).optString("url"));
            itemMap.put("isxianshi", "1");
            itemMap.put("keyword", titlelist.get(currentIndex).get("keyword"));
            if (arr.optJSONObject(i).has("purl")) {
                itemMap.put("purl", arr.optJSONObject(i).optString("purl"));
            } else {
                itemMap.put("purl", "0");
            }
            gridlist.add(itemMap);
            gridlist1.add(itemMap);
        }
        if (gridadapter != null) {
            gridadapter.notifyDataSetChanged();
        } else {
            gridadapter = new HomeMyGridAdapter(gridlist, getActivity());
            mtitleresultgrid.setAdapter(gridadapter);
            mtitleresultgrid.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Intent intent;
                    if (JumpIntentUtil.isJump(gridlist,position,"domain1")) {
                        intent = new Intent(getActivity(), IntentActivity.class);
                        intent.putExtra("url", gridlist.get(position).get("url").toString());
                        intent.putExtra("title", gridlist.get(position).get("title").toString());
                        intent.putExtra("domain", gridlist.get(position).get("domain1").toString());
                        intent.putExtra("groupRowKey", gridlist.get(position).get("groupRowKey").toString());
                    } else {
                        intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra("url", gridlist.get(position).get("url").toString());
                        intent.putExtra("groupRowKey", gridlist.get(position).get("groupRowKey").toString());
                    }
                    startActivity(intent);
                }
            });
        }
    }

    private void NowPrice() {
        thread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    if (isrequest == true) {
                        try {
                            Map<String, String> params = new HashMap<>();
                            if (!"0".equals(gridlist1.get(requestnum).get("purl"))) {
                                String str;
                                String content;
                                params.put("domain", gridlist1.get(requestnum).get("domain1").toString());
                                params.put("rowkey", gridlist1.get(requestnum).get("groupRowKey").toString());
                                params.put("fromwhere", "android" + titlelist.get(currentIndex).get("keyword"));
                                if (gridlist1.get(requestnum).get("purl").toString().contains("||")) {
                                    String url = gridlist1.get(requestnum).get("purl").toString();
                                    String[] split = url.split("\\|\\|");
                                    String referrer = split[1];
                                    content = HttpUtil.getHttp1(params, split[0], getActivity(), referrer);
                                    params.put("pcontent", content);
                                    str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", getActivity());
                                } else {
                                    content = HttpUtil.getHttp1(params, gridlist1.get(requestnum).get("purl").toString(), getActivity(), null);
                                    params.put("pcontent", content);
                                    str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", getActivity());
                                }
                                JSONObject object = new JSONObject(str);
                                if ("3".equals(object.optString("type"))) {
                                    if ("".equals(object.optString("url"))) {
                                        content = HttpUtil.getHttp1(params, gridlist1.get(requestnum).get("url").toString(), getActivity(), null);
                                    } else {
                                        content = HttpUtil.getHttp1(params, object.optString("url"), getActivity(), null);
                                    }

                                    params.put("pcontent", content);
                                    String url = Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct";
                                    str = HttpUtil.getHttp(params, url, getActivity());
                                }
                                Message mes = handler.obtainMessage();
                                mes.obj = str;
                                mes.arg1 = requestnum;
                                mes.what = 0;
                                handler.sendMessage(mes);
                            }
                            if (requestnum + 1 >= gridlist1.size()) {
                                isrequest = false;
                            }
                            requestnum++;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String str = msg.obj.toString();
                int i = msg.arg1;
                try {
                    JSONObject object = new JSONObject(str);
                    switch (object.optString("type")) {
                        case "0":
                            gridlist.remove(i - removenum);
                            gridadapter.notifyDataSetChanged();
                            removenum++;
                            break;
                        case "1":
                            String price = object.optString("price");
                            gridlist.get(i - removenum).put("price", price);
                            gridadapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        mrefresh.stopLoadMore();
        mrefresh.stopRefresh();
        xrefresh2.stopLoadMore();
        xrefresh2.stopRefresh();
        switch (requestCode) {
            case 1:
                try {
                    if (content.equals("重试")) {
                        mzhanwei.setVisibility(View.VISIBLE);
                        mrefresh.setVisibility(View.GONE);
                        risrequest = true;
                        gisrequest = true;
                    } else {
                        mrefresh.setVisibility(View.VISIBLE);
                        mzhanwei.setVisibility(View.GONE);
                        mrefresh.setPullLoadEnable(true);
                        if (isclear) {
                            titlelist.clear();
                            taglist.clear();
                            if (mbox != null) {
                                mbox.removeAllViews();
                            }
                        }
                        JSONObject object = new JSONObject(content);
                        if (object.has("fubiao")){
                            huodongimg.setVisibility(View.VISIBLE);
                            final JSONObject jo = object.getJSONObject("fubiao");
                            Glide.with(getActivity()).load(jo.optString("img")).into(huodongimg);
                            huodongimg.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EventIdIntentUtil.EventIdIntent(getActivity(),jo);
                                }
                            });
                        }else {
                            huodongimg.setVisibility(View.GONE);
                        }
                        banner = object.optJSONArray("banner");
                        JSONArray searchwords = object.optJSONArray("searchwords");
                        activity = object.optJSONArray("activity");
                        dianpu = object.optJSONArray("dianpu");
                        articles = object.optJSONArray("articles");
                        tag = object.optJSONArray("tag");
                        if (object.has("gongneng")){
                            gongneng = object.optJSONArray("gongneng");
                        }
                        loadtitlekeywords(searchwords);
                        homeData.setActivity(activity);
                        homeData.setArticles(articles);
                        homeData.setBanner(banner);
                        homeData.setDianpu(dianpu);
                        homeData.setGongneng(gongneng);
                        homeData.setTag(tag);
                        arrlist.clear();
                        arrlist.add(homeData);
                        if (adapter!= null){
                            adapter.notifyDataSetChanged();
                        }else {
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    return position < 3+articles.length()+tujian.length() ? 2 : 1;
                                }
                            });
                            mrecyclerview.setLayoutManager(gridLayoutManager);
                            mrecyclerview.addItemDecoration(new TwoDecoration(10,"#f3f3f3",3+articles.length()+tujian.length()));
                            mrecyclerview.setHasFixedSize(true);
                            adapter = new HomeAdapter(getActivity(), taglist, guesslikelist, arrlist,mrefresh);
                            mrecyclerview.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (position>=0){
                                        Intent intent;
                                        //判断是否跳转中转页面
                                        if (JumpIntentUtil.isJump2(guesslikelist,position,"domain")) {
                                            intent = new Intent(getActivity(),IntentActivity.class);
                                            intent.putExtra("title", guesslikelist.get(position).get("title"));
                                            intent.putExtra("domain", guesslikelist.get(position).get("domain"));
                                            intent.putExtra("url", guesslikelist.get(position).get("url"));
                                            intent.putExtra("groupRowKey", guesslikelist.get(position).get("rowkey"));
                                        }else{
                                            intent = new Intent(getActivity(),WebViewActivity.class);
                                            intent.putExtra("url", guesslikelist.get(position).get("url"));
                                            intent.putExtra("groupRowKey", guesslikelist.get(position).get("rowkey"));
                                        }
                                        startActivity(intent);
                                    }
                                }
                            });
//                            mrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                                @Override
//                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                                        Glide.with(getActivity()).resumeRequests();
//                                    }else {
//                                        Glide.with(getActivity()).pauseRequests();
//                                    }
//                                }
//                            });
                        }
                        isclear = false;
//						if (!object.optString("showhis").isEmpty()) {
//							if ("1".equals(object.optString("showhis"))) {
//								mbijiahenggnag.setVisibility(View.VISIBLE);
//								mbijiautil.setVisibility(View.VISIBLE);
//								SharedPreferencesUtil.putSharedData(getActivity(), "isshowhis", "showhis", "1");
//							}else{
//								mbijiahenggnag.setVisibility(View.GONE);
//								mbijiautil.setVisibility(View.GONE);
//								SharedPreferencesUtil.putSharedData(getActivity(), "isshowhis", "showhis", "0");
//							}
//						}
                        if (object.has("guanggao")) {
                            if (isshowzhezhao) {
                                final JSONObject jo = object.optJSONObject("guanggao");
                                new HomeAlertDialog(getActivity()).builder()
                                        .setimag(jo.optString("img"))
                                        .setonclick(new OnClickListener() {

                                            @Override
                                            public void onClick(View arg0) {
                                                EventIdIntentUtil.EventIdIntent(getActivity(), jo);
                                            }
                                        }).show();
                                isshowzhezhao = false;
                            }

                        }
                        mrecyclerview.scrollBy(0,10);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    if (isclear) {
                        gridlist.clear();
                        gridlist1.clear();
                        requestnum = 0;
                        removenum = 0;
                        mtitleresultgrid.smoothScrollToPosition(0);
                    }
                    JSONArray jsonObject = new JSONArray(content);
                    loadgridview(jsonObject);
                    isrequest = true;
                    isclear = false;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 3:
                JSONArray content1;
                try {
                    content1 = new JSONArray(content);
                    for (int i = 0; i < content1.length(); i++) {
                        JSONObject object = content1.getJSONObject(i);
                        Map<String, String> map = new HashMap<>();
                        map.put("title", object.optString("title"));
                        map.put("price", object.optString("price"));
                        map.put("img", object.optString("img"));
                        map.put("url", object.optString("url"));
                        map.put("domain", object.optString("domain"));
                        map.put("rowkey", object.optString("rowkey"));
                        map.put("youhui", object.optString("youhui"));
                        map.put("st", object.optString("st"));
                        map.put("et", object.optString("et"));
                        map.put("sale", object.optString("sale"));
                        if (!object.optString("down").isEmpty()) {
                            map.put("down", object.optString("down"));
                            map.put("bprice", object.optString("bprice"));
                        }
                        guesslikelist.add(map);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case 4:
                if (!content.isEmpty()) {
                    try {
                        tujian = new JSONArray(content);
                        homeData.setTujian(tujian);
                        arrlist.clear();
                        arrlist.add(homeData);
                        SharedPreferencesUtil.putSharedData(
                                MyApplication.getApplication(), "homedata",
                                "hometuijian", dataJo.toString());
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case 5:
                if (!content.isEmpty()) {
                    initseelike(dataJo.toString());
                    SharedPreferencesUtil.putSharedData(
                            MyApplication.getApplication(), "homedata",
                            "seelike", dataJo.toString());
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

}
