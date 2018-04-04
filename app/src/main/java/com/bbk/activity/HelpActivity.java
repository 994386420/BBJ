package com.bbk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/*
 * 使用帮助界面
 */

public class HelpActivity extends BaseActivity implements OnClickListener {
	
	private ImageButton goBackBtn;
	
	private String[] titleArr = {
			"如何修改比比鲸密码", 
			"如何解绑手机", 
			"如何解绑邮箱", 
			"如何查看订单", 
			"交易遇到问题打不开商城", 
			"购买物品流程", 
			"比比鲸比价规则", 
			"购买商品需要退换怎么办"};
	
	private String[] textArr = {
			"进入<font color=\"#0098FF\">【个人中心】</font>，再次进入<font color=\"#0098FF\">【个人资料】</font>点击<font color=\"#0098FF\">【修改密码】</font>，可修改密码。",
			"进入<font color=\"#0098FF\">【个人中心】</font>，再次进入<font color=\"#0098FF\">【个人资料】</font>点击<font color=\"#0098FF\">【手机】</font>，可解绑手机号。",
			"进入<font color=\"#0098FF\">【个人中心】</font>，再次进入<font color=\"#0098FF\">【个人资料</font>点击<font color=\"#0098FF\">【邮箱】</font>，可更换邮箱地址。",
			"1.可通过浏览记录翻阅。<br/>2.可通过关注观看。<br/>3.直接通过第三方商城查阅。",
			"系统维护中，请重启比比鲸。",
			"1.通过我们的 <font color=\"#0098FF\">【搜索框】</font> 搜索任意物品，即出现<font color=\"#0098FF\">【 筛选条件】</font> ，筛选以后跳转至<font color=\"#0098FF\">【 详情页面 】</font>，可观看各个商城综合对比详情。对比过后点击<font color=\"#0098FF\">【 进入商城】</font> 可直接进入点三方商城购买页面进行购买。<br/>2.可通过首页下方的 <font color=\"#0098FF\">【分类比价】</font> 进行筛选，点击过后自动跳转至商品细分，有<font color=\"#0098FF\">【 热门品牌】</font> 和 <font color=\"#0098FF\">【热门分类】</font> 可供快速跳转。后跳转至筛选页面后和以上相同。",
			"1.通过 各个商城价格 比价。<br/>2.通过 商城内部 比价。<br/>3.通过 各个商城评论，售后 对比。<br/>4.通过 销量和信用 对比。",
			"由于比比鲸是比价软件，不参与报价与售卖。进入第三方商城后订单问题，请与第三方商城协商。"};
//	Html.fromHtml(“<font size='20'>网页内容</font>”)
	private int[] imgArr = {
			R.mipmap.img_password,
			R.mipmap.img_phone,
			R.mipmap.img_email,
			0,
			0,
			R.mipmap.img_shop,
			0,
			0};
	
	private RelativeLayout help1;
	private RelativeLayout help2;
	private RelativeLayout help3;
	private RelativeLayout help4;
	private RelativeLayout help5;
	private RelativeLayout help6;
	private RelativeLayout help7;
	private RelativeLayout help8;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		
		initView();
		initData();
	}
	
	@Override
	public <T extends View> T $(int resId) {
		return (T) super.findViewById(resId);
	}
	
	public void initView() {
		goBackBtn = $(R.id.topbar_goback_btn);
		goBackBtn.setOnClickListener(this);
		help1 = $(R.id.id1);
		help2 = $(R.id.id2);
		help3 = $(R.id.id3);
		help4 = $(R.id.id4);
		help5 = $(R.id.id5);
		help6 = $(R.id.id6);
		help7 = $(R.id.id7);
		help8 = $(R.id.id8);
		
		help1.setOnClickListener(this);
		help2.setOnClickListener(this);
		help3.setOnClickListener(this);
		help4.setOnClickListener(this);
		help5.setOnClickListener(this);
		help6.setOnClickListener(this);
		help7.setOnClickListener(this);
		help8.setOnClickListener(this);
	}
	
	public void initData() {
	}

	@Override
	public void onClick(View v) {
		
		Intent intent;
		
		switch (v.getId()) {
		case R.id.topbar_goback_btn:
			finish();
			break;
		case R.id.id1:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[0]);
			intent.putExtra("text", textArr[0]);
			intent.putExtra("img", imgArr[0]);
			startActivity(intent);
			break;
		case R.id.id2:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[1]);
			intent.putExtra("text", textArr[1]);
			intent.putExtra("img", imgArr[1]);
			startActivity(intent);
			break;
		case R.id.id3:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[2]);
			intent.putExtra("text", textArr[2]);
			intent.putExtra("img", imgArr[2]);
			startActivity(intent);
			break;
		case R.id.id4:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[3]);
			intent.putExtra("text", textArr[3]);
			intent.putExtra("img", imgArr[3]);
			startActivity(intent);
			break;
		case R.id.id5:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[4]);
			intent.putExtra("text", textArr[4]);
			intent.putExtra("img", imgArr[4]);
			startActivity(intent);
			break;
		case R.id.id6:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[5]);
			intent.putExtra("text", textArr[5]);
			intent.putExtra("img", imgArr[5]);
			startActivity(intent);
			break;
		case R.id.id7:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[6]);
			intent.putExtra("text", textArr[6]);
			intent.putExtra("img", imgArr[6]);
			startActivity(intent);
			break;
		case R.id.id8:
			intent = new Intent(HelpActivity.this, HelpContentActivity.class);
			intent.putExtra("title", titleArr[7]);
			intent.putExtra("text", textArr[7]);
			intent.putExtra("img", imgArr[7]);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
}
