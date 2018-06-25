package com.bbk.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.adapter.login.AlibcLogin;
import com.alibaba.baichuan.android.trade.callback.AlibcLoginCallback;
import com.bbk.activity.IntentActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.WebViewActivity;
import com.bbk.adapter.ResultDialogAdapter1;
import com.bbk.adapter.ResultDialogAdapter2;
import com.bbk.flow.DataFlow3;
import com.bbk.resource.Constants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyScrollViewNew;
import com.bbk.view.MyWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewAlertDialog {
    @BindView(R.id.mwebview_scroll)
    MyScrollViewNew mwebviewScroll;
    private Context context;
    private Dialog dialog;
    private Display display;
    private boolean ishistory = false;
    private TextView history;
    private TextView domain_info;
    private MyScrollViewNew mscroll;
    private View henggang1;
    private View henggang2;
    private LinearLayout mhistorybox;
    private MyWebView mwebview;
    private LinearLayout llayout;
    private DataFlow3 dataFlow;
    private String token;
    private String content;
    private ImageView mclose;
    private String type;
    private ListView mlistview1, mlistview2;
    private ResultDialogAdapter1 adapter1;
    private ResultDialogAdapter2 adapter2;
    private List<Map<String, Object>> list1, list2, list;
    private LinearLayout wantdomain;
    private View henggang;
    private Thread thread;
    private boolean isrequest = true;
    private int requestnum = 0;
    private int removenum = 0;
    private String keyword;

    public WebViewAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public WebViewAlertDialog builder(String content, String type, boolean ishis, String keyword) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.activity_web_view_dialog, null);
        this.content = content;
        this.type = type;
        this.keyword = keyword;
        dataFlow = new DataFlow3(context);
        ButterKnife.bind(this, view);
        token = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "token");
        initView(view, ishis);
        initData();
        // ����Dialog���ֺͲ���
        dialog = new Dialog(context, R.style.AlertDialogStyle2);
        dialog.setContentView(view);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                if (mwebview != null) {
                    mwebview.setVisibility(View.GONE);
                    mwebviewScroll.setVisibility(View.GONE);
                }
            }
        });
        if (thread == null) {
            NowPrice();
        }
        return this;
    }

    private void initView(View view, boolean ishis) {
        mlistview1 = view.findViewById(R.id.mlistview1);
        mlistview2 = view.findViewById(R.id.mlistview2);
        llayout = (LinearLayout) view.findViewById(R.id.llayout);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        llayout.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), LayoutParams.WRAP_CONTENT));
        wantdomain = (LinearLayout) view.findViewById(R.id.wantdomain);
        mhistorybox = (LinearLayout) view.findViewById(R.id.mhistorybox);
        mwebview = (MyWebView) view.findViewById(R.id.mwebview);
        ViewGroup.LayoutParams params = mwebview.getLayoutParams();
        params.width = (int) (display.getWidth() * 0.85);
        mwebview.setLayoutParams(params);
        mscroll = (MyScrollViewNew) view.findViewById(R.id.mscroll);
//		historyChart = (LineChartView)findViewById(R.id.history_price_chart);

        henggang = view.findViewById(R.id.henggang);
        henggang1 = view.findViewById(R.id.henggang1);
        henggang2 = view.findViewById(R.id.henggang2);
        history = (TextView) view.findViewById(R.id.history);
        domain_info = (TextView) view.findViewById(R.id.domain_info);

        history.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!ishistory) {
                    history.setTextColor(Color.parseColor("#ff7d41"));
                    domain_info.setTextColor(Color.parseColor("#333333"));
                    henggang1.setBackgroundColor(Color.parseColor("#ffffff"));
                    henggang2.setBackgroundColor(Color.parseColor("#ff7d41"));
                    mwebview.setVisibility(View.VISIBLE);
                    mwebviewScroll.setVisibility(View.VISIBLE);
                    mscroll.setVisibility(View.GONE);
                    ishistory = true;
                }
            }
        });
        domain_info.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ishistory) {
                    history.setTextColor(Color.parseColor("#333333"));
                    domain_info.setTextColor(Color.parseColor("#ff7d41"));
                    henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
                    henggang2.setBackgroundColor(Color.parseColor("#ffffff"));
                    mwebview.setVisibility(View.GONE);
                    mwebviewScroll.setVisibility(View.GONE);
                    mscroll.setVisibility(View.VISIBLE);
                    ishistory = false;
                }
            }
        });
        mclose = (ImageView) view.findViewById(R.id.mclose);
        mclose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        if (type.equals("1")) {
            history.setTextColor(Color.parseColor("#ff7d41"));
            domain_info.setTextColor(Color.parseColor("#333333"));
            henggang1.setBackgroundColor(Color.parseColor("#ffffff"));
            henggang2.setBackgroundColor(Color.parseColor("#ff7d41"));
            mwebview.setVisibility(View.VISIBLE);
            mwebviewScroll.setVisibility(View.VISIBLE);
            mscroll.setVisibility(View.GONE);
            ishistory = true;
        } else {
            history.setTextColor(Color.parseColor("#333333"));
            domain_info.setTextColor(Color.parseColor("#ff7d41"));
            henggang1.setBackgroundColor(Color.parseColor("#ff7d41"));
            henggang2.setBackgroundColor(Color.parseColor("#ffffff"));
            mwebview.setVisibility(View.GONE);
            mwebviewScroll.setVisibility(View.GONE);
            mscroll.setVisibility(View.VISIBLE);
            ishistory = false;
        }
        if (ishis) {
            mhistorybox.setVisibility(View.VISIBLE);
        } else {
            mhistorybox.setVisibility(View.GONE);
        }
//		String showis = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "isshowhis", "showhis");
//		if (!TextUtils.isEmpty(showis) && "1".equals(showis)) {
//			mhistorybox.setVisibility(View.VISIBLE);
//		}else{
//			mhistorybox.setVisibility(View.GONE);
//		}

    }

    private void initData() {
        try {
            JSONObject object = new JSONObject(content);
            JSONArray list = object.optJSONArray("list");
//			historyPrice = object.optString("history");
            if (object.has("hisurl")) {
                String url = object.optString("hisurl");
                mwebview.loadUrl(url);
                // 支持JS
                WebSettings settings = mwebview.getSettings();
                settings.setJavaScriptEnabled(true);
                // 支持屏幕缩放
                settings.setSupportZoom(true);
                settings.setBuiltInZoomControls(true);
                // 不显示webview缩放按钮
                settings.setDisplayZoomControls(false);
                webintent(mwebview);
            } else {
                mhistorybox.setVisibility(View.GONE);
            }

            initlist(list);
            if (list2.isEmpty()) {
                wantdomain.setVisibility(View.GONE);
                henggang.setVisibility(View.GONE);
            }
            adapter1 = new ResultDialogAdapter1(list1, context);
            adapter2 = new ResultDialogAdapter2(list2, context);
            mlistview1.setAdapter(adapter1);
            mlistview2.setAdapter(adapter2);
            setHeight1();
            setHeight2();
            mlistview1.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Map<String, Object> map = list1.get(arg2);
                    final String url = map.get("url").toString();
                    final String title = map.get("title").toString();
                    final String domain1 = map.get("domain").toString();
                    final String groupRowKey = map.get("groupRowKey").toString();
                    Intent intent;
                    if (JumpIntentUtil.isJump1(domain1)) {
                        intent = new Intent(context, IntentActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("domain", domain1);
                        intent.putExtra("url", url);
                        intent.putExtra("groupRowKey", groupRowKey);
                        intent.putExtra("bprice", map.get("price").toString());
                    } else {
                        intent = new Intent(context, WebViewActivity.class);
//                        WebViewActivity.instance.finish();
                        intent.putExtra("url", url);
                        intent.putExtra("rowkey", groupRowKey);
                    }
                    context.startActivity(intent);
                    dialog.dismiss();

                }
            });
            mlistview2.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    dialog.dismiss();
                    Map<String, Object> map = list2.get(arg2);
                    final String url = map.get("url").toString();
                    Intent intent = new Intent(context, WebViewActivity.class);
//                    WebViewActivity.instance.finish();
                    intent.putExtra("url", url);
                    context.startActivity(intent);
//					finish();
                }
            });
//			initHistoryPriceData(historyChart);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setHeight1() {
        int height = 0;
        int count = adapter1.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter1.getView(i, null, mlistview1);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        LayoutParams params = (LayoutParams) this.mlistview1.getLayoutParams();
        params.width = LayoutParams.FILL_PARENT;
        params.height = height;
        mlistview1.setLayoutParams(params);
    }

    public void setHeight2() {
        int height = 0;
        int count = adapter2.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter2.getView(i, null, mlistview2);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        LayoutParams params = (LayoutParams) this.mlistview2.getLayoutParams();
        params.width = LayoutParams.FILL_PARENT;
        params.height = height;
        mlistview2.setLayoutParams(params);
    }

    private void webintent(MyWebView mwebview) {
        mwebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("bbjtech://")) {
                    Uri uri = Uri.parse(url);

                    try {
                        JSONObject jsonObject = new JSONObject();
                        // String host = uri.getHost();
                        // String dataString = intent.getDataString();
                        String eventId = uri.getQueryParameter("eventId");
                        jsonObject.put("eventId", eventId);
                        if (uri.getQueryParameter("url") != null) {
                            String htmlUrl = uri.getQueryParameter("url");
                            String url1 = htmlUrl.replace("@@", "&");
                            jsonObject.put("url", url1);
                        }
                        taobaoLogin(context, jsonObject);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }
        });
    }

    public void taobaoLogin(final Context context, final JSONObject jsonObject) {
        if (AlibcLogin.getInstance().getSession() != null) {

            String nick = AlibcLogin.getInstance().getSession().nick;
            if (nick != null && !"".equals(nick)) {
                EventIdIntentUtil.EventIdIntent(context, jsonObject);
                dialog.dismiss();
            } else {
                AlibcLogin alibcLogin = AlibcLogin.getInstance();

                alibcLogin.showLogin((Activity) context, new AlibcLoginCallback() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "登录成功 ",
                                Toast.LENGTH_LONG).show();
                        EventIdIntentUtil.EventIdIntent(context, jsonObject);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(context, "登录失败 ",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            EventIdIntentUtil.EventIdIntent(context, jsonObject);
            dialog.dismiss();
        }

    }

    public WebViewAlertDialog setlist2click(final OnItemClickListener listener) {
        mlistview2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Map<String, Object> map = list2.get(position);
                final String url = map.get("url").toString();
                dialog.dismiss();
            }
        });
        return this;
    }

    private void initlist(JSONArray array) {
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (object.optString("price").isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("domain", object.optString("domain"));
                    map.put("domainCh", object.optString("domainCh"));
                    map.put("url", object.optString("url"));
                    list2.add(map);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", object.optString("title"));
                    map.put("price", object.optString("price"));
                    map.put("domain", object.optString("domain"));
                    map.put("domainCh", object.optString("domainCh"));
                    map.put("url", object.optString("url"));
                    if (object.has("purl")) {
                        map.put("purl", object.optString("purl"));
                    } else {
                        map.put("purl", "0");
                    }
                    map.put("groupRowKey", object.optString("rowkey"));
                    list1.add(map);
                    list.add(map);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        dialog.show();
    }

    private void NowPrice() {
        thread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    if (isrequest == true) {
                        try {
                            Map<String, String> params = new HashMap<>();
                            if (!"0".equals(list.get(requestnum).get("purl"))) {
                                String str;
                                String content;
                                params.put("domain", list.get(requestnum).get("domain").toString());
                                params.put("rowkey", list.get(requestnum).get("groupRowKey").toString());
                                params.put("fromwhere", "android" + keyword);
                                if (list.get(requestnum).get("purl").toString().contains("||")) {
                                    String url = list.get(requestnum).get("purl").toString();
                                    String[] split = url.split("\\|\\|");
                                    String referrer = split[1];
                                    content = HttpUtil.getHttp1(params, split[0], context, referrer);
                                    params.put("pcontent", content);
                                    str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", context);
                                } else {
                                    content = HttpUtil.getHttp1(params, list.get(requestnum).get("purl").toString(), context, null);
                                    params.put("pcontent", content);
                                    str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct", context);
                                }
                                JSONObject object = new JSONObject(str);
                                if ("3".equals(object.optString("type"))) {
                                    if ("".equals(object.optString("url"))) {
                                        content = HttpUtil.getHttp1(params, list.get(requestnum).get("url").toString(), context, null);
                                    } else {
                                        content = HttpUtil.getHttp1(params, object.optString("url"), context, null);
                                    }
                                    params.put("pcontent", content);
                                    String url = Constants.MAIN_BASE_URL_MOBILE + "checkService/checkProduct";
                                    str = HttpUtil.getHttp(params, url, context);
                                }
                                Message mes = handler.obtainMessage();
                                mes.obj = str;
                                mes.arg1 = requestnum;
                                mes.what = 0;
                                handler.sendMessage(mes);
                            }
                            if (requestnum + 1 >= list.size()) {
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String str = msg.obj.toString();
                int i = msg.arg1;
                try {
                    JSONObject object = new JSONObject(str);
                    switch (object.optString("type")) {
                        case "0":
                            list1.remove(i - removenum);
                            adapter1.notifyDataSetChanged();
                            removenum++;
                            break;
                        case "1":
                            String price = object.optString("price");
                            list1.get(i - removenum).put("price", price);
                            adapter1.notifyDataSetChanged();
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

}
