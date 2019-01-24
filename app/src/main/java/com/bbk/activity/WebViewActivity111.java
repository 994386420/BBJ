package com.bbk.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.bbk.flow.DataFlow;
import com.bbk.flow.ResultEvent;
import com.bbk.model.DianpuSearchActivity;
import com.bbk.resource.Constants;
import com.bbk.resource.NewConstants;
import com.bbk.util.EventIdIntentUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.ShareUtil;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.MyWebView;
import com.bbk.view.X5WebView;
import com.logg.Logg;
import com.tamic.jswebview.browse.JsWeb.CustomWebChromeClient;
import com.tamic.jswebview.browse.JsWeb.CustomWebViewClient;
import com.tamic.jswebview.view.ProgressBarWebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class WebViewActivity111 extends BaseActivity implements OnClickListener, ResultEvent,IUiListener {

    private X5WebView mPbWebview;
    private String url = "";
    protected ProgressBar bar;
    private DataFlow dataFlow;
    private String weburl;
    private String webtitle;
    private boolean isintent = false;
    private ImageView topbar_goback_btn,fengxiang;
    private TextView mtitle;
    private String title = "";
    private Tencent mTencent;
    private String content = "";
    private String type = "";
    private String shareurl = "";
    private ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_activity);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, WebViewActivity111.this);
        dataFlow = new DataFlow(this);
        getWindow().setBackgroundDrawable(null);
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        // MyApplication.getInstance().addActivity(this);
        content = getIntent().getStringExtra("url").replace("\r\n\r\n","");
        if (content.contains("@@")){
            String[] split = content.split("@@");
            url = split[0];
            title = split[1];
            shareurl = split[2];
            type = split[3];
        }else {
            url = content;
        }

//        url = content.replaceAll("@@","&");
        if (getIntent().getStringExtra("mid") != null) {
            url = url+"&mid="+getIntent().getStringExtra("mid");
        }else {
            url = url+"&mid=";
        }
        initView();
        initData();
        progressbar = new android.widget.ProgressBar(this, null,
                android.R.attr.progressBarStyleHorizontal);
        // 设置进度条的大小
        progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.FILL_PARENT,
                5, 0, 0));
        // 可以改变颜色
        ClipDrawable d = new ClipDrawable(new ColorDrawable(getResources().getColor(R.color.tuiguang_color5)),
                Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressbar.setProgressDrawable(d);
        progressbar.setBackgroundColor(getResources().getColor(R.color.transparent));

        // progressbar.setProgressDrawable(context.getResources().getDrawable(
        // R.drawable.barbgimg));

        mPbWebview.addView(progressbar);
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                webtitle = title;
                mtitle.setText(title);
                super.onReceivedTitle(view, title);
            }
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        };
        // 设置setWebChromeClient对象
        mPbWebview.setWebChromeClient(wvcc);
    }

    private void initView() {
        mPbWebview = $(R.id.web_view_layout);
        mtitle = $(R.id.title);
        fengxiang = $(R.id.fengxiang);
        fengxiang.setVisibility(View.VISIBLE);
        topbar_goback_btn = $(R.id.topbar_goback_btn);
        topbar_goback_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mPbWebview.canGoBack()) {
                    mPbWebview.goBack();
                } else {
                    finish();
                }
            }
        });
        fengxiang.setOnClickListener(this);
        loadWebPage(url);
    }

    private void loadWebPage(String pageUrl) {
        if (TextUtils.isEmpty(pageUrl)) {
            return;
        }
        mPbWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!isintent) {
                    if (url.contains("bbjtech://")) {
                        String[] stringsIntent = url.replace("bbjtech://?", "").replace("@@", "=").split("=");
                        switch (stringsIntent[1]) {
                            case "12":
                                Intent intent = new Intent(WebViewActivity111.this, SearchMainActivity.class);
                                try {
                                    intent.putExtra("keyword", URLDecoder.decode(stringsIntent[3], "utf-8"));
                                    SharedPreferencesUtil.putSharedData(WebViewActivity111.this, "shaixuan", "shaixuan", "yes");
                                    NewConstants.clickpositionFenlei = 5200;
                                    NewConstants.clickpositionDianpu = 5200;
                                    NewConstants.clickpositionMall = 5200;
                                    startActivity(intent);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "121":
                                intent = new Intent(WebViewActivity111.this, MesageCenterActivity.class);
                                intent.putExtra("type", "0");
                                startActivity(intent);
                                break;
                            case "124":
                                NewConstants.showdialogFlg = "1";
                                Logg.json(stringsIntent[3] + stringsIntent[4]);
                                intent = new Intent(WebViewActivity111.this, IntentActivity.class);
                                intent.putExtra("url", stringsIntent[3] + "=" + stringsIntent[4]);
                                startActivity(intent);
                                break;
                            case "a3":
                                intent = new Intent(WebViewActivity111.this, ShopDetailActivty.class);
                                intent.putExtra("id", stringsIntent[3]);
                                startActivity(intent);
                                break;
                            case "a4":
                                try {
                                    intent = new Intent(WebViewActivity111.this, DianpuSearchActivity.class);
                                    intent.putExtra("keyword", "");
                                    intent.putExtra("dianpuid", "");
                                    intent.putExtra("producttype", URLDecoder.decode(stringsIntent[3], "utf-8"));
                                    intent.putExtra("plevel", "2");
                                    startActivity(intent);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }

                        //跳转到邀请好友页面
                        if (url.contains("yaoqing")){
                            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                            if (TextUtils.isEmpty(userID)) {
                                Intent intent = new Intent(WebViewActivity111.this,UserLoginNewActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(WebViewActivity111.this,YaoqingFriendsActivity.class);
                                startActivity(intent);
                            }
                        }
                            Uri uri = Uri.parse(url);
                        try {
                            JSONObject jsonObject = new JSONObject();
                            // String host = uri.getHost();
                            // String dataString = intent.getDataString();
                            String eventId = uri.getQueryParameter("eventId");
                            jsonObject.put("eventId", eventId);
                            if (uri.getQueryParameter("htmlUrl") != null) {
                                String htmlUrl = uri.getQueryParameter("htmlUrl");
                                jsonObject.put("htmlUrl", htmlUrl);
                            }
                            if (uri.getQueryParameter("groupRowkey") != null) {
                                String groupRowkey = uri.getQueryParameter("groupRowkey");
                                jsonObject.put("groupRowkey", groupRowkey);
                            }
                            if (uri.getQueryParameter("rankType") != null) {
                                String rankType = uri.getQueryParameter("rankType");
                                jsonObject.put("rankType", rankType);
                            }
                            if (uri.getQueryParameter("keyword") != null) {
                                String keyword = uri.getQueryParameter("keyword");
                                jsonObject.put("keyword", keyword);
                            }
                            if (uri.getQueryParameter("url")!=null) {
                                String url2 = uri.getQueryParameter("url");
                                jsonObject.put("url", url2);

                            }
                            EventIdIntentUtil.EventIdIntent(WebViewActivity111.this,jsonObject);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
                if (!url.startsWith("http:") && !url.startsWith("https:")) {
                    return true;
                }
                weburl = url;
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    weburl = url;
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity111.this, description, Toast.LENGTH_SHORT).show();
            }

        });

        mPbWebview.loadUrl(pageUrl);
    }
    private void initData() {
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fengxiang:
                String useid = SharedPreferencesUtil.getSharedData(
                        getApplicationContext(), "userInfor", "userID");
                if (!TextUtils.isEmpty(useid)) {
                    Logg.e(content);
                    ShareUtil.showShareDialog(v, this, "专业的网购比价、导购平台", title, shareurl,type);
                }else{
                    Intent intent14 = new Intent(this, UserLoginNewActivity.class);
                    intent14.putExtra("iswebyanzheng", "yes");
                    isintent = true;
                    startActivityForResult(intent14, 1);
                }



                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AudioManager am = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                am.adjustStreamVolume(AudioManager.STREAM_DTMF, AudioManager.ADJUST_RAISE, 0);
                break;
            default:
                if(keyCode == KeyEvent.KEYCODE_BACK && mPbWebview.canGoBack()){
                    mPbWebview.goBack();
                    return true;
                }else{
                    finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mTencent != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        isintent = false;
        if (!TextUtils.isEmpty(userID)){
//            int i = url.indexOf("userid=");
//            url = url.substring(0,i) + "userid=" + userID;
//            mPbWebview.loadUrl(url);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if(null!=mPbWebview) {
            if (null != this.mPbWebview.getView()) {
                this.mPbWebview.getView().setVisibility(View.GONE);
                long  timeout
                        = ViewConfiguration.getZoomControlsTimeout();

                new Timer().schedule(new TimerTask(){
                    @Override
                    public void run() {
                        try {
                            ((WebView)mPbWebview.getView()).destroy();
                        }catch (Exception e){

                        }
                    }
                }, timeout+1000L);
            }
        }
        super.onDestroy();
    }

    /**
     * QQ回调
     */
    @Override
    public void onCancel() {

    }

    @Override
    public void onComplete(Object arg0) {
        Toast.makeText(this, "分享成功",
                Toast.LENGTH_LONG).show();
        loadData();
    }

    @Override
    public void onError(UiError arg0) {
        Toast.makeText(this, "分享取消",Toast.LENGTH_LONG).show();
    }
    private void loadData() {
//        Map<String, String> paramsMap = new HashMap<String, String>();
//        paramsMap.put("userid", SharedPreferencesUtil.getSharedData(getApplicationContext(), "userInfor", "userID"));
//        dataFlow.requestData(1, "newService/checkIsShare", paramsMap, this, false);

    }
    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}
