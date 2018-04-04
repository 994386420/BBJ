package com.bbk.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbk.adapter.GossipCommentListViewAdapter;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.view.CircleImageView1;
import com.bbk.view.MyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rtj on 2018/2/27.
 */

public class GossipCommentDetailActivity extends BaseActivity implements ResultEvent,View.OnClickListener{
    private ArrayList<Map<String,String>> list,list1;
    private int position;
    private ImageView mimg,topbar_goback_btn;
    private TextView mname,mtime,mcontent,mallcomment,mhuifusend;
    private MyListView mlistview;
    private GossipCommentListViewAdapter adapter;
    private EditText msgEdittext;
    private ScrollView mscrollview;
    private LinearLayout mhuifuBox;
    private DataFlow6 dataFlow;
    private String reid = "",plid = "",wenzhangid = "";
    private Toast toast;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gossip_comment_detail);
        try {
            dataFlow = new DataFlow6(this);
            ArrayList list1 =  getIntent().getStringArrayListExtra("list");
            list = list1;
            position = Integer.parseInt(getIntent().getStringExtra("position"));
            wenzhangid = getIntent().getStringExtra("blid");
            initView();
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        list1= new ArrayList<>();
        mhuifuBox = (LinearLayout) findViewById(R.id.mhuifuBox);
        mimg = (ImageView) findViewById(R.id.mimg);
        topbar_goback_btn = (ImageView) findViewById(R.id.topbar_goback_btn);
        mname = (TextView) findViewById(R.id.mname);
        mtime = (TextView) findViewById(R.id.mtime);
        mcontent = (TextView) findViewById(R.id.mcontent);
        mallcomment = (TextView) findViewById(R.id.mallcomment);
        mhuifusend = (TextView) findViewById(R.id.mhuifusend);
        mlistview = (MyListView) findViewById(R.id.mlistview);
        msgEdittext = (EditText) findViewById(R.id.msgEdittext);
        mscrollview = (ScrollView) findViewById(R.id.mscrollview);
        adapter = new GossipCommentListViewAdapter(this,list1);
        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                msgEdittext.setHint("回复 "+list1.get(position).get("name")+"：");
                reid = list1.get(position).get("uid");
               pupopSoftKey();
            }
        });
        topbar_goback_btn.setOnClickListener(this);
        mhuifusend.setOnClickListener(this);
        mhuifuBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topbar_goback_btn:
                finish();
                break;
            case R.id.mhuifusend:
                insertPL();
                break;
            case R.id.mhuifuBox:
                reid = "";
                pupopSoftKey();
                break;
            default:
                break;
        }
    }

    private void initData() throws JSONException {
        Map<String, String> map = list.get(position);
        String content = map.get("content");
        String uid = map.get("uid");
        String time = map.get("time");
        String iszan = map.get("iszan");
        String zan = map.get("zan");
        plid = map.get("plid");
        String name = map.get("name");
        String img = map.get("img");
        String lou = map.get("lou");
        String sublist = map.get("sublist");
        String count = map.get("count");
        mcontent.setText(content);
        mname.setText(name);
        mtime.setText(time);
        mallcomment.setText("全部评论（"+count+")");
        CircleImageView1.getImg(this,img,mimg);
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", userID);
        paramsMap.put("plid", plid);
        paramsMap.put("page", page+"");
        paramsMap.put("wzid", wenzhangid);
        dataFlow.requestData(1, "newService/querySubPlList", paramsMap, this, true);
    }
    private void pupopSoftKey(){
        msgEdittext.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 开启或者关闭软键盘
        imm.showSoftInput(msgEdittext, InputMethodManager.SHOW_FORCED);// 弹出软键盘时，焦点定位在输入框中
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
                        .hideSoftInputFromWindow(GossipCommentDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            //        （reid对谁回复，被回复人的id， plid 评论id  ，wenzhangid 文章id）
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("userid", userID);
            paramsMap.put("plid", plid);
            paramsMap.put("reid", reid);
            paramsMap.put("wenzhangid", wenzhangid);
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
                    list1.clear();
                    JSONArray array = new JSONArray(content);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Map<String,String> map1 = new HashMap<>();
                        map1.put("content",object.optString("content"));
                        map1.put("uid",object.optString("uid"));
                        map1.put("time",object.optString("time"));
                        map1.put("reuserid",object.optString("reuserid"));
                        if (object.has("rename")){
                            if (!list.get(position).get("name").equals(object.optString("rename"))){
                                map1.put("rename",object.optString("rename"));
                            }
                        }
                        map1.put("name",object.optString("name"));
                        map1.put("img",object.optString("img"));
                        list1.add(map1);
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    if ("1".equals(dataJo.optString("status"))){
                        msgEdittext.setText("");
                        reid = "";
                        msgEdittext.setHint("请输入要回复的话");
                        Toast.makeText(this,"评论成功",Toast.LENGTH_LONG).show();
                        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("userid", userID);
                        paramsMap.put("plid", plid);
                        paramsMap.put("page", page+"");
                        paramsMap.put("wzid", wenzhangid);
                        dataFlow.requestData(1, "newService/querySubPlList", paramsMap, this, true);
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
