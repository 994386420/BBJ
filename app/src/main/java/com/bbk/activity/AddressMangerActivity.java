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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bbk.Bean.AddressMangerBean;
import com.bbk.Bean.OnAddressListioner;
import com.bbk.adapter.AddressChooseAdapter;
import com.bbk.adapter.AddressMangerAdapter;
import com.bbk.client.BaseObserver;
import com.bbk.client.ExceptionHandle;
import com.bbk.client.RetrofitClient;
import com.bbk.dialog.ActionSheetDialog;
import com.bbk.flow.DataFlow6;
import com.bbk.resource.NewConstants;
import com.bbk.util.DialogSingleUtil;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.bbk.view.MyScrollViewNew;
import com.logg.Logg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 地址管理
 * Created by Administrator on 2018/5/23/023.
 */

public class AddressMangerActivity extends BaseActivity implements View.OnClickListener, OnAddressListioner {
    @BindView(R.id.scrollView)
    MyScrollViewNew scrollView;
    private ImageButton mBackImag;
    private TextView mTitle;
    private DataFlow6 dataFlow;
    private RecyclerView recyclerView;
    private LinearLayout noAdressLayout;
    private Button addAddressbtn1, addAddressbtn2;
    private String original;
    AddressMangerAdapter addressMangerAdapter;
    public static String ACTION = "AddressMangerActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manger_layout);
        ButterKnife.bind(this);
        dataFlow = new DataFlow6(this);
        View topView = findViewById(R.id.topbar_layout);
        ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
        registerBoradcastReceiver();
        initView();
        queryAddro();
    }

    private void initView() {
        recyclerView = findViewById(R.id.address_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_line));
        recyclerView.addItemDecoration(divider);
        noAdressLayout = findViewById(R.id.no_address_layout);
        addAddressbtn1 = findViewById(R.id.add_address_btn);
        addAddressbtn2 = findViewById(R.id.add_address_btn1);
        mTitle = findViewById(R.id.title_text);
        mBackImag = findViewById(R.id.title_back_btn);
        addAddressbtn1.setOnClickListener(this);
        addAddressbtn2.setOnClickListener(this);
        mBackImag.setOnClickListener(this);
    }


    private void queryAddro() {
        if (NewConstants.address.equals("1")) {
            mTitle.setText("地址管理");
        }else {
            mTitle.setText("选择地址");
        }
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("addrid", "");
        RetrofitClient.getInstance(this).createBaseApi().queryAddro(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                if (!content.equals("[]")) {
                                    original = "0";
                                    //数据解析
                                    List<AddressMangerBean> addressMangerBeans = JSON.parseArray(content, AddressMangerBean.class);
                                    if (NewConstants.address.equals("1")) {
                                        //管理地址界面
                                        addressMangerAdapter = new AddressMangerAdapter(AddressMangerActivity.this, addressMangerBeans);
                                        recyclerView.setAdapter(addressMangerAdapter);
                                        addressMangerAdapter.setOnDeleteListioner(AddressMangerActivity.this);
                                    }else {
                                        //选择地址界面
                                        AddressChooseAdapter addressChooseAdapter = new AddressChooseAdapter(AddressMangerActivity.this, addressMangerBeans);
                                        recyclerView.setAdapter(addressChooseAdapter);
                                        addressChooseAdapter.setOnDeleteListioner(AddressMangerActivity.this);
                                    }
                                    recyclerView.setVisibility(View.VISIBLE);
                                    addAddressbtn2.setVisibility(View.VISIBLE);
                                    scrollView.setVisibility(View.VISIBLE);
                                    noAdressLayout.setVisibility(View.GONE);
                                } else {
                                    original = "1";
                                    recyclerView.setVisibility(View.GONE);
                                    scrollView.setVisibility(View.GONE);
                                    addAddressbtn2.setVisibility(View.GONE);
                                    noAdressLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(AddressMangerActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(AddressMangerActivity.this, e.message);
                    }
                });
    }


    private void removeAddr(String addrid) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("addrid", addrid);
        RetrofitClient.getInstance(this).createBaseApi().removeAddr(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                StringUtil.showToast(AddressMangerActivity.this, "删除成功");
                                queryAddro();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(AddressMangerActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(AddressMangerActivity.this, e.message);
                    }
                });
    }


    private void modifyAddr(String addrid) {
        String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(), "userInfor", "userID");
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("userid", userID);
        maps.put("addrid", addrid);
        maps.put("original", "1");
        maps.put("way", "1");
        RetrofitClient.getInstance(this).createBaseApi().modifyAddr(
                maps, new BaseObserver<String>(this) {
                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String content = jsonObject.optString("content");
                            if (jsonObject.optString("status").equals("1")) {
                                Logg.json(jsonObject);
                                queryAddro();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        DialogSingleUtil.dismiss(0);
                    }

                    @Override
                    protected void showDialog() {
                        DialogSingleUtil.show(AddressMangerActivity.this);
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponeThrowable e) {
                        DialogSingleUtil.dismiss(0);
                        StringUtil.showToast(AddressMangerActivity.this, e.message);
                    }
                });
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
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
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onDelete(final String addrid) {
        new ActionSheetDialog(this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem(12, "是否确认删除该地址?",
                ActionSheetDialog.SheetItemColor.Gray, null).addSheetItem(18, "删除", ActionSheetDialog.SheetItemColor.Red,
                new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        removeAddr(addrid);
                    }
                }).show();
    }

    @Override
    public void onDefaultAdressCheck(String addrid) {
        modifyAddr(addrid);
    }

    @Override
    public void onItemCick(String addrid, String phone, String receiver, String tag, String street, String area) {
        Intent intent;
        intent = new Intent(ACTION);
        intent.putExtra("addrid",addrid);
        intent.putExtra("phone",phone);
        intent.putExtra("receiver",receiver);
        intent.putExtra("tag",tag);
        intent.putExtra("street",street);
        intent.putExtra("area",area);
        sendBroadcast(intent);
        finish();
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
            queryAddro();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
