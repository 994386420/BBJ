package com.bbk.activity;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.ImmersedStatusbarUtils;
import com.bbk.util.SharedPreferencesUtil;
import com.bbk.util.StringUtil;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.utils.LogUtil;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import org.json.JSONObject;

import java.util.HashMap;

/*
 * 添加地址界面
 */
public class AddressActivity extends BaseActivity implements View.OnClickListener , OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener,ResultEvent{
	private BottomDialog dialog;
	private String provinceCode;
	private String cityCode;
	private String countyCode;
	private String streetCode;
	private int provincePosition;
	private int cityPosition;
	private int countyPosition;
	private int streetPosition;
	private TextView mAddress;
	private LinearLayout maddressLayout;
	private LinearLayout mAddPersonLayout;//添加联系人
	private EditText userPhone,userName,detarilAdress;
	private ImageButton mBackImag;
	private TextView mTitle;
	private TextView mHome,mGongsi,mSchool;
	private DataFlow6 dataFlow;
	private String tag;//地址标签
	private Button saveBtm;//保存
	private String original;
	public static String ACTION_NAME = "AdressActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_adress_layout);
		dataFlow = new DataFlow6(this);
		View topView = findViewById(R.id.topbar_layout);
		// 实现沉浸式状态栏
		ImmersedStatusbarUtils.initAfterSetContentView(this, topView);
		initView();
		if (getIntent().getStringExtra("original") != null){
			original = getIntent().getStringExtra("original");
		}
	}

	private void initView() {
		mAddress = findViewById(R.id.address_text);
		maddressLayout = findViewById(R.id.add_adress_layout);
		mAddPersonLayout = findViewById(R.id.add_person_layout);
		userName = findViewById(R.id.address_user_name);
		userPhone = findViewById(R.id.address_user_phone);
		mTitle = findViewById(R.id.title_text);
		mTitle.setText("新建联系人");
		mBackImag = findViewById(R.id.title_back_btn);
		mHome = findViewById(R.id.biaoqian_home);
		mGongsi = findViewById(R.id.biaoqian_gongsi);
		mSchool = findViewById(R.id.biaoqian_school);
		detarilAdress = findViewById(R.id.detail_address);
		saveBtm = findViewById(R.id.save_btn);
		mHome.setOnClickListener(this);
		mGongsi.setOnClickListener(this);
		mSchool.setOnClickListener(this);
		mBackImag.setOnClickListener(this);
		maddressLayout.setOnClickListener(this);
		mAddPersonLayout.setOnClickListener(this);
		saveBtm.setOnClickListener(this);
	}
	private void initData(boolean is) {
		String userID = SharedPreferencesUtil.getSharedData(MyApplication.getApplication(),"userInfor", "userID");
		String name = userName.getText().toString();
		String phone = userPhone.getText().toString();
		String area = mAddress.getText().toString();
		String street =detarilAdress.getText().toString();
		if (tag == null){
			tag = "";
		}
		if(validateMessage()) {
			HashMap<String, String> paramsMap = new HashMap<>();
			Log.i("参数",original+"==="+tag+"==="+name+"==="+phone+"===="+area+"===="+street);
			paramsMap.put("userid",userID);
			paramsMap.put("name", name);
			paramsMap.put("phone", phone);
			paramsMap.put("area", area);
			paramsMap.put("street", street);
			paramsMap.put("tag", tag);
			paramsMap.put("original", original);
			dataFlow.requestData(1, Constants.addAddress, paramsMap, this, is, "保存中...");
		}
	}
	//验证输入信息
	private Boolean validateMessage() {
		if (StringUtil.isNullOrEmpty(userName.getText().toString())) {
			StringUtil.showToast(this,"请输入收货人姓名");
			return false;
		}
		if (StringUtil.isNullOrEmpty(userPhone.getText().toString())) {
			StringUtil.showToast(this,"请输入收货人电话");
			return false;
		}
		if (StringUtil.isMobilePhoneVerify(userPhone.getText().toString())) {
			StringUtil.showToast(this,"请输入正确的手机号码");
			return false;
		}
		if (StringUtil.isNullOrEmpty(mAddress.getText().toString())) {
			StringUtil.showToast(this,"请选择所在区域");
			return false;
		}
		if (StringUtil.isNullOrEmpty(detarilAdress.getText().toString())) {
			StringUtil.showToast(this,"请输入详细地址");
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.title_back_btn:
				finish();
				break;
			case R.id.add_adress_layout:
			if (dialog != null) {
				dialog.show();
			} else {
				dialog = new BottomDialog(this);
				dialog.setOnAddressSelectedListener(this);
				dialog.setDialogDismisListener(this);
				dialog.setTextSize(14);//设置字体的大小
				dialog.setIndicatorBackgroundColor(R.color.address_color);//设置指示器的颜色
				dialog.setTextSelectedColor(R.color.black);//设置字体获得焦点的颜色
				dialog.setTextUnSelectedColor(R.color.address_color);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
				dialog.setSelectorAreaPositionListener(this);
				dialog.show();
			}
			break;
			case R.id.add_person_layout:
				//调起系统联系人
				startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
				break;
			case R.id.biaoqian_home:
				tag = "家";
				mHome.setBackgroundResource(R.drawable.bg_biaoqian1);
				mGongsi.setBackgroundResource(R.drawable.bg_biaoqian);
				mSchool.setBackgroundResource(R.drawable.bg_biaoqian);
				mHome.setTextColor(getResources().getColor(R.color.white));
				mGongsi.setTextColor(getResources().getColor(R.color.address_color1));
				mSchool.setTextColor(getResources().getColor(R.color.address_color1));
				break;
			case R.id.biaoqian_school:
				tag = "学校";
				mHome.setBackgroundResource(R.drawable.bg_biaoqian);
				mGongsi.setBackgroundResource(R.drawable.bg_biaoqian);
				mSchool.setBackgroundResource(R.drawable.bg_biaoqian1);
				mHome.setTextColor(getResources().getColor(R.color.address_color1));
				mGongsi.setTextColor(getResources().getColor(R.color.address_color1));
				mSchool.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.biaoqian_gongsi:
				tag = "公司";
				mHome.setBackgroundResource(R.drawable.bg_biaoqian);
				mGongsi.setBackgroundResource(R.drawable.bg_biaoqian1);
				mSchool.setBackgroundResource(R.drawable.bg_biaoqian);
				mHome.setTextColor(getResources().getColor(R.color.address_color1));
				mGongsi.setTextColor(getResources().getColor(R.color.white));
				mSchool.setTextColor(getResources().getColor(R.color.address_color1));
				break;
			case R.id.save_btn:
				initData(true);
				break;
		}
	}

	private void SetText(TextView home,TextView gongsi,TextView school){

	}
	@Override
	public void dialogclose() {
		if(dialog!=null){
			dialog.dismiss();
		}
	}

	@Override
	public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
		this.provincePosition = provincePosition;
		this.cityPosition = cityPosition;
		this.countyPosition = countyPosition;
		this.streetPosition = streetPosition;
		LogUtil.d("数据", "省份位置=" + provincePosition);
		LogUtil.d("数据", "城市位置=" + cityPosition);
		LogUtil.d("数据", "乡镇位置=" + countyPosition);
		LogUtil.d("数据", "街道位置=" + streetPosition);
	}

	@Override
	public void onAddressSelected(Province province, City city, County county, Street street) {
		provinceCode = (province == null ? "" : province.code);
		cityCode = (city == null ? "" : city.code);
		countyCode = (county == null ? "" : county.code);
		streetCode = (street == null ? "" : street.code);
		LogUtil.d("数据", "省份id=" + provinceCode);
		LogUtil.d("数据", "城市id=" + cityCode);
		LogUtil.d("数据", "乡镇id=" + countyCode);
		LogUtil.d("数据", "街道id=" + streetCode);
		String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
				(street == null ? "" : street.name);
		mAddress.setText(s);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
            // 获得DATA表中的名字
			String linkname = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			userName.setText(linkname);
            // 条件为联系人ID
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
			String phonenumber = "";
			while (phone.moveToNext()) {
				phonenumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim().replace(" ", "");
			}
			userPhone.setText(phonenumber.replace("-",""));
		}
	}

	@Override
	public void onResultData(int requestCode, String api, JSONObject dataJo, String content) {
		switch (requestCode){
			case 1:
				Intent intent;
//				intent = new Intent(this, AddressMangerActivity.class);
//				setResult(1,intent);
				intent = new Intent(ACTION_NAME);
				sendBroadcast(intent);
				finish();
				break;
		}
	}
}
