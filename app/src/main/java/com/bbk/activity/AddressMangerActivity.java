package com.bbk.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.AddressMangerBean;
import com.bbk.Bean.OnAddressListioner;
import com.bbk.adapter.AddressMangerAdapter;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 地址管理
 * Created by Administrator on 2018/5/23/023.
 */

public class AddressMangerActivity extends BaseActivity implements View.OnClickListener,ResultEvent,OnAddressListioner {
    private ImageButton mBackImag;
    private TextView mTitle;
    private DataFlow6 dataFlow;
    private RecyclerView recyclerView;
    private LinearLayout noAdressLayout;
    private Button addAddressbtn1,addAddressbtn2;
    private String original;
    AddressMangerAdapter addressMangerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manger_layout);
        dataFlow = new DataFlow6(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        registerBoradcastReceiver();
        initView();
        initData(true);
    }

    private void initView() {
        recyclerView = findViewById(R.id.address_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider_line));
        recyclerView.addItemDecoration(divider);
        noAdressLayout = findViewById(R.id.no_address_layout);
        addAddressbtn1 = findViewById(R.id.add_address_btn);
        addAddressbtn2 = findViewById(R.id.add_address_btn1);
        mTitle = findViewById(R.id.title_text);
        mTitle.setText("地址管理");
        mBackImag = findViewById(R.id.title_back_btn);
        addAddressbtn1.setOnClickListener(this);
        addAddressbtn2.setOnClickListener(this);
        mBackImag.setOnClickListener(this);
    }

    //查询全部地址
    private void initData(boolean is) {
            String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("userid",userID);
            paramsMap.put("addrid", "");
            dataFlow.requestData(1, Constants.queryAddr, paramsMap, this, is);
    }
    //删除地址
    private void initDataDeleteAddress(boolean is,String addrid) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addrid",addrid);
        dataFlow.requestData(2, Constants.removeAddr, paramsMap, this, is,"删除中...");
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.title_back_btn:
                finish();
                break;
            case R.id.add_address_btn:
                intent = new Intent(this, AddressActivity.class);
                if (original != null) {
                    intent.putExtra("original", original);
                }
                startActivity(intent);
                break;
            case R.id.add_address_btn1:
                intent = new Intent(this, AddressActivity.class);
                if (original != null) {
                    intent.putExtra("original", original);
                }
//                startActivityForResult(intent,1);
                startActivity(intent);
                break;
        }
    }
//    @Override
//    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
//        switch (arg0) {
//            case 1:
//                initData(true);
//                break;
//        }
//        super.onActivityResult(arg0, arg1, arg2);
//    }

    @Override
    public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
        switch (requestCode){
            case 1:
//                Log.i("====",content+"===");
                if (!content.equals("[]")){
                    original = "0";
                    //数据解析
                    List<AddressMangerBean> addressMangerBeans = JSON.parseArray(content,AddressMangerBean.class);
                    addressMangerAdapter = new AddressMangerAdapter(this,addressMangerBeans);
                    recyclerView.setAdapter(addressMangerAdapter);
                    addressMangerAdapter.setOnDeleteListioner(AddressMangerActivity.this);
                    recyclerView.setVisibility(View.VISIBLE);
                    addAddressbtn2.setVisibility(View.VISIBLE);
                    noAdressLayout.setVisibility(View.GONE);
                }else {
                    original = "1";
                    recyclerView.setVisibility(View.GONE);
                    addAddressbtn2.setVisibility(View.GONE);
                    noAdressLayout.setVisibility(View.VISIBLE);
                }
            break;
            case 2:
                if (dataJo.optString("status").equals("1")){
                    StringUtil.showToast(AddressMangerActivity.this,"删除成功");
                    initData(false);
                }
                break;
        }
    }

    @Override
    public void onDelete(final String addrid) {
        new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(12,"是否确认删除该地址?",
                ActionSheetDialog.SheetItemColor.Gray,null).addSheetItem(18,"删除", ActionSheetDialog.SheetItemColor.Red,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        initDataDeleteAddress(true,addrid);
                    }
                }).show();
    }

    @Override
    public void onDefaultAdressCheck(int addrid) {

    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(AddressActivity.ACTION_NAME);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
    // 广播接收
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
