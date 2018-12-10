package com.bbk.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.activity.BaseActivity;
import com.bbk.activity.MyApplication;
import com.bbk.activity.R;
import com.bbk.activity.UserLoginNewActivity;
import com.bbk.model.tablayout.XTabLayout;
import com.bbk.util.HomeLoadUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.ClearableEditText;
import com.bbk.view.CommonLoadingView;
import com.logg.Logg;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺搜索
 */
public class SearchZiyiShopActivity extends BaseActivity implements View.OnKeyListener {
    @BindView(R.id.title_back_btn)
    ImageButton titleBackBtn;
    @BindView(R.id.tablayout)
    XTabLayout tablayout;
    @BindView(R.id.mrecycler)
    RecyclerView mrecycler;
    @BindView(R.id.progress)
    CommonLoadingView progress;
    @BindView(R.id.mPtrframe)
    SmartRefreshLayout mPtrframe;
    @BindView(R.id.type_image)
    LinearLayout typeImage;
    @BindView(R.id.lltype)
    LinearLayout lltype;
    @BindView(R.id.type_grid)
    GridView typeGrid;
    @BindView(R.id.ll_shouqi)
    LinearLayout llShouqi;
    @BindView(R.id.fl_type)
    FrameLayout flType;
    @BindView(R.id.miaosha_status)
    GridView miaoshaStatus;
    @BindView(R.id.img_tishi)
    ImageView imgTishi;
    @BindView(R.id.topbar_search_input)
    ClearableEditText topbarSearchInput;
    @BindView(R.id.img_more_black)
    ImageView imgMoreBlack;
    @BindView(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @BindView(R.id.sl_history)
    ScrollView slHistory;
    @BindView(R.id.ll_delete_history)
    LinearLayout llDeleteHistory;
    private int curposition = 0;
    //布局管理器
    private LayoutInflater mInflater;
    private SharedPreferences mPref;//使用SharedPreferences记录搜索历史
    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";
    //流式布局的子布局
    private List<String> strings;
    private TextView tv;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String history = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
                    if (!TextUtils.isEmpty(history)) {
                        List<String> list = new ArrayList<String>();
                        for (Object o : history.split(",")) {
                            list.add((String) o);
                        }
                        strings = list;
                    }
                    /**
                     * 判断历史记录显示还是隐藏
                     */
                    if (strings.size() > 0) {
                        slHistory.setVisibility(View.VISIBLE);
                        idFlowlayout.setAdapter(new TagAdapter<String>(strings) {
                            @Override
                            public View getView(FlowLayout parent, int position, String s) {
                                tv = (TextView) mInflater.inflate(R.layout.tv,
                                        idFlowlayout, false);
                                tv.setText(s);
                                return tv;
                            }
                        });
                    } else {
                        slHistory.setVisibility(View.GONE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.home_types_layout);
        View topView = findViewById(R.id.lltype);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);

        mPref = getSharedPreferences("input", Activity.MODE_PRIVATE);
        mInflater = LayoutInflater.from(this);
        strings = new ArrayList<>();
        //初始化历史纪录
        handler.sendEmptyMessageDelayed(1, 0);

        ButterKnife.bind(this);
        refreshAndloda();
        slHistory.setVisibility(View.VISIBLE);
        tablayout.setVisibility(View.GONE);
        mrecycler.setVisibility(View.GONE);
        topbarSearchInput.setVisibility(View.VISIBLE);
        topbarSearchInput.setOnKeyListener(this);
        imgMoreBlack.setVisibility(View.VISIBLE);
        //流式布局tag的点击方法
        idFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(SearchZiyiShopActivity.this, DianpuSearchActivity.class);
                intent.putExtra("dianpuid", "");
                intent.putExtra("producttype", "");
                intent.putExtra("plevel", "");
                intent.putExtra("search","1");
                intent.putExtra("keyword", strings.get(position).toString());
                startActivity(intent);
                return true;
            }
        });
    }

    /**
     * 刷新事件
     */
    private void refreshAndloda() {
        mPtrframe.setEnableLoadMore(false);
        mPtrframe.setEnableRefresh(false);
    }


    @OnClick({R.id.title_back_btn,R.id.ll_delete_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.ll_delete_history:
                cleanHistory();
                break;
        }
    }

    @OnClick(R.id.img_more_black)
    public void onViewClicked() {
        Intent intent;
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (TextUtils.isEmpty(userID)) {
            intent = new Intent(this, UserLoginNewActivity.class);
            startActivityForResult(intent, 1);
        } else {
            HomeLoadUtil.showItemPop(this, imgMoreBlack);
        }
    }

    /**
     * 登陆回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        if (userID != null && !userID.equals("")) {
            switch (requestCode) {
                case 1:
                    HomeLoadUtil.showItemPop(this, imgMoreBlack);
                    break;
            }
        }
    }

    /**
     * 搜索监听
     *
     * @param v
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            InputMethodManager imm = (InputMethodManager) v.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            doSearch();
            return true;
        }
        return false;
    }

    /**
     * 搜索事件
     */
    public void doSearch() {
        String aa = topbarSearchInput.getText().toString().trim();
        strings.add(aa);
        save();
        String sarechword = topbarSearchInput.getText().toString();
        if (sarechword == null || sarechword.equals("")) {
            StringUtil.showToast(this, "搜索内容为空");
            return;
        }
        Intent intent = new Intent(this, DianpuSearchActivity.class);
        intent.putExtra("dianpuid", "");
        intent.putExtra("producttype", "");
        intent.putExtra("plevel", "");
        intent.putExtra("search","1");
        intent.putExtra("keyword", sarechword);
        startActivity(intent);
    }

    /**
     * 储存搜索历史
     */
    public void save() {
        String text = topbarSearchInput.getText().toString();
        String oldText = mPref.getString(KEY_SEARCH_HISTORY_KEYWORD, "");
        Log.e("tag", "" + oldText);
        Log.e("Tag", "" + text);
        Log.e("Tag", "" + oldText.contains(text));
        if (!TextUtils.isEmpty(text) && !(oldText.contains(text))) {
//            if (strings.size() > 5) {
//                //最多保存条数
//                return;
//            }
            SharedPreferences.Editor editor = mPref.edit();
            editor.putString(KEY_SEARCH_HISTORY_KEYWORD, text + "," + oldText);
            editor.commit();
            strings.add(0, text);
        }
        //通知handler更新UI
        handler.sendEmptyMessageDelayed(1, 0);
    }

    /**
     * 清除历史纪录
     */
    public void cleanHistory() {
        mPref = getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(KEY_SEARCH_HISTORY_KEYWORD).commit();
        strings.clear();
        //通知handler更新UI
        handler.sendEmptyMessageDelayed(1, 0);
        StringUtil.showToast(this, "清楚搜索历史成功");
    }

}
