package com.bbk.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.GossipPiazzaDetailAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.SoftHideKeyBoardUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rtj on 2018/2/26.
 */

public class GossipPiazzaDetailActivity extends BaseActivity implements ResultEvent,View.OnClickListener{
    private RecyclerView mrecyclerview;
    private GossipPiazzaDetailAdapter adapter;
    private List<Map<String,String>> list;
    private DataFlow6 dataFlow;
    private String blid;
    Map<String,String> map;
    private TextView mtitle,mhuifusend;
    private EditText msgEdittext;
    private Toast toast;
    private ImageView topbar_goback_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip_detail);
        SoftHideKeyBoardUtil.assistActivity(this,getStatusBarHeight(this));
        View topView = findViewById(R.id.topbar_layout);
        // 实现沉浸式状态栏
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        dataFlow = new DataFlow6(this);
        blid =  getIntent().getStringExtra("blid");
//        blid = "3548";
        initView();
        initData();
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void initView() {
        map = new HashMap<>();
        list = new ArrayList<>();
        topbar_goback_btn = (ImageView) findViewById(R.id.topbar_goback_btn);
        mtitle = (TextView) findViewById(R.id.title);
        mhuifusend = (TextView) findViewById(R.id.mhuifusend);
        msgEdittext = (EditText) findViewById(R.id.msgEdittext);
        mrecyclerview = (RecyclerView) findViewById(R.id.mrecyclerview);
        mrecyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new GossipPiazzaDetailAdapter(this,map,list,msgEdittext);
        adapter.setOnItemClickListener(new GossipPiazzaDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(GossipPiazzaDetailActivity.this,GossipCommentDetailActivity.class);
                intent.putStringArrayListExtra("list",(ArrayList) list);
                intent.putExtra("position",position+"");
                intent.putExtra("blid",map.get("blid"));
                startActivity(intent);
            }
        });
        mrecyclerview.setAdapter(adapter);
        mhuifusend.setOnClickListener(this);
        topbar_goback_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mhuifusend:
                insertPL();
                break;
            case R.id.topbar_goback_btn:
                finish();
                break;
            default:
                break;
        }
    }
    private void initData() {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userID);
        paramsMap.put("client", "android");
        paramsMap.put("blid", blid);
        dataFlow.requestData(1, "newService/queryBaoliaoDetailForApp", paramsMap, this, true);
    }
    public void addMap(JSONObject object){

        map.put("content", object.optString("content"));
        map.put("imgs", object.optString("imgs"));
        map.put("username", object.optString("username"));
        map.put("zan", object.optString("zan"));
        map.put("iszan", object.optString("iszan"));
        map.put("headimg", object.optString("headimg"));
        map.put("dtime", object.optString("dtime"));
        map.put("plnum", object.optString("plnum"));
        map.put("zannum", object.optString("zannum"));
        map.put("blid", object.optString("blid"));
        map.put("readnum", object.optString("readnum"));
        map.put("url", object.optString("url"));
        if (object.has("video")){
            map.put("video", object.optString("video"));
        }else {
            map.put("video", object.optString("0"));
        }

    }
    public void addList(JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("content",object.optString("content"));
            map.put("uid",object.optString("uid"));
            map.put("time",object.optString("time"));
            map.put("iszan",object.optString("iszan"));
            map.put("count",object.optString("count"));
            map.put("zan",object.optString("zan"));
            map.put("plid",object.optString("plid"));
            map.put("name",object.optString("name"));
            map.put("img",object.optString("img"));
            map.put("lou",object.optString("lou"));
            if (object.has("sublist")){
                map.put("sublist",object.optString("sublist"));
            }else {
                map.put("sublist","-1");
            }
            list.add(map);
        }
    }
    public void insertPL(){
        if (msgEdittext.getText().toString().isEmpty()) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT);
            toast.show();
        } else {
			/* 隐藏软键盘 */
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager
                        .hideSoftInputFromWindow(GossipPiazzaDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            //        （reid对谁回复，被回复人的id， plid 评论id  ，wenzhangid 文章id）
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("userid", userID);
//            paramsMap.put("plid", plid);
//            paramsMap.put("reid", reid);
            paramsMap.put("wenzhangid", map.get("blid"));
            paramsMap.put("content", msgEdittext.getText().toString());
            dataFlow.requestData(2, "newService/insertPL", paramsMap, this, true);


        }

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            insertPL();
            return true;
        }
        // return true;
        return super.dispatchKeyEvent(event);
        // 必不可少，否则所有的组件都不会有TouchEvent了
    }
    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        try {
            switch (requestCode){
                case 1:
                    map.clear();
                    list.clear();
                    JSONObject object = new JSONObject(content);
                    String title = object.optString("title");
                    mtitle.setText(title);
                    addMap(object);
                    JSONArray array = object.optJSONArray("list");
                    addList(array);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    if ("1".equals(dataJo.optString("status"))){
                        msgEdittext.setText("");
                        Toast.makeText(this,"评论成功",Toast.LENGTH_LONG).show();
                        initData();
                    }

                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
