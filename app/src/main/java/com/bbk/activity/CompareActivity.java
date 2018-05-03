package com.bbk.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.XRefreshViewListener;
import com.bbk.adapter.ResultListAdapter;
import com.bbk.adapter.SecondAdapter5;
import com.bbk.flow.DataFlow;
import com.bbk.flow.DataFlow3;
import com.bbk.flow.DataFlow6;
import com.bbk.flow.ResultEvent;
import com.bbk.resource.Constants;
import com.bbk.util.DensityUtil;
import com.bbk.util.HttpUtil;
import com.bbk.util.JumpIntentUtil;
import com.bbk.view.HeaderView;
import com.bbk.view.MyFootView;
import com.bbk.view.MyGridView;
import com.bbk.view.MyListView;
import com.bbk.view.SuperScrollView;
import com.bbk.view.SuperScrollView.OnScrollListener;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CompareActivity extends BaseActivity implements ResultEvent,OnClickListener,OnScrollListener{
	private ImageView filter_img;
	private ImageView mtop;
	private boolean isprice = true;
	private String[] brandArr;
	private JSONObject abcBrand;
	private String sortway="0";
	private SecondAdapter5 adapter;
	private LinearLayout shopbox;
	private LinearLayout second_hei;
	private RelativeLayout second_bai;
	private int curposition=-1;
	private LinearLayout second;
	private RelativeLayout biankuang1;
	private RelativeLayout biankuang2;
	private LinearLayout view_box,viewbox;
	private TextView sellrank;
	private TextView tv1,select1;
	private MyGridView gridView;
	private View view;
	private TextView tv2;
	private List<Map<String, Object>> data;
	private TextView request;
	private TextView ensure;
	private TextView filter;
	private TextView topbar_search_input;
	private ImageButton topbar_goback_btn;
	private ImageButton topbar_search_btn;
	private RelativeLayout mComposite;
	private RelativeLayout mnumber;
	private RelativeLayout mprice;
	private RelativeLayout mfilter,hinttext;
	private XRefreshView xrefresh;
	private DataFlow6 dataFlow;
	private String rowkey;
	private int page=1;
	private String filterWay="";
	private String domain="";
	private String brand="";
	private RelativeLayout mComposite1;
	private RelativeLayout mnumber1;
	private RelativeLayout mprice1;
	private RelativeLayout mfilter1;
	private boolean istv1=false;
	private boolean istv2=false;
	private List<Map<String, Object>> itemList,itemList1;
	private ResultListAdapter listadapter;
	private MyListView mlistview;
	private SuperScrollView mscrollview;
	private TextView filter_price;
	private TextView compositerank;
	private TextView filter_price1;
	private TextView compositerank1;
	private TextView sellrank1;
	private ImageView mtop1;
	private JSONObject jsonObject;
	private int myScrollViewTop;
	private LinearLayout mfilterbox;
	private int mfilterboxHeight;
	private int hinttextHeight;
	private int viewboxHeight;
	private LinearLayout home_layout_bar1;
	private LinearLayout noresult;
	private DataFlow3 dataFlow1;
	private Thread thread;
	private boolean isrequest = false;
	private int requestnum = 0;
	private int removenum = 0;
	private int pagenum = 0;
	private boolean isrun = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compare_activity1);
		dataFlow = new DataFlow6(this);
		dataFlow1 = new DataFlow3(this);
		rowkey = getIntent().getStringExtra("rowkey");
		initView();
		initData1();
	}
	private void initData1(){
		if (viewbox!=null) {
			viewbox.removeAllViews();
		}
		if (view_box!=null) {
			view_box.removeAllViews();
		}
		if (shopbox!=null) {
			shopbox.removeAllViews();
		}
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("rowkey", rowkey);
		xrefresh.stopRefresh();
		dataFlow.requestData(1, "newApp/querySimilar", paramsMap, this);
	}
	private void initView() {
		data = new ArrayList<>();
		itemList = new ArrayList<>();
		itemList1 = new ArrayList<>();
		filter_img = (ImageView) findViewById(R.id.filter_img);
		mtop = (ImageView) findViewById(R.id.mtop);
		mtop1 = (ImageView) findViewById(R.id.mtop1);
		shopbox = (LinearLayout) findViewById(R.id.shopbox);
		noresult = (LinearLayout) findViewById(R.id.noresult);
		home_layout_bar1 = (LinearLayout) findViewById(R.id.home_layout_bar1);
		mfilterbox = (LinearLayout) findViewById(R.id.mfilterbox);
		second_hei = (LinearLayout) findViewById(R.id.second_hei);
		second_bai = (RelativeLayout) findViewById(R.id.second_bai);
		second = (LinearLayout) findViewById(R.id.second);
		biankuang1 = (RelativeLayout) findViewById(R.id.biankuang1);
		hinttext = (RelativeLayout) findViewById(R.id.hinttext);
		biankuang2 = (RelativeLayout) findViewById(R.id.biankuang2);
		view_box = (LinearLayout) findViewById(R.id.view_box);
		viewbox = (LinearLayout) findViewById(R.id.viewbox);
		sellrank = (TextView) findViewById(R.id.sellrank);
		sellrank1 = (TextView) findViewById(R.id.sellrank1);
		filter_price = (TextView) findViewById(R.id.filter_price);
		compositerank = (TextView) findViewById(R.id.compositerank);
		filter_price1 = (TextView) findViewById(R.id.filter_price1);
		compositerank1 = (TextView) findViewById(R.id.compositerank1);
		mscrollview = (SuperScrollView)findViewById(R.id.mscrollview);
		tv1 = (TextView) findViewById(R.id.tv1);
		mlistview = (MyListView)findViewById(R.id.mlistview);
		tv2 = (TextView) findViewById(R.id.tv2);
		request = (TextView) findViewById(R.id.request);
		request.setOnClickListener(this);
		ensure = (TextView) findViewById(R.id.ensure);
		ensure.setOnClickListener(this);
		filter = (TextView) findViewById(R.id.filter);
		topbar_search_input = (TextView) findViewById(R.id.topbar_search_input);
		topbar_goback_btn = (ImageButton) findViewById(R.id.topbar_goback_btn);
		topbar_search_btn = (ImageButton) findViewById(R.id.topbar_search_btn);
		mComposite = (RelativeLayout) findViewById(R.id.mComposite);
		mnumber = (RelativeLayout) findViewById(R.id.mnumber);
		mprice = (RelativeLayout) findViewById(R.id.mprice);
		mfilter = (RelativeLayout) findViewById(R.id.mfilter);
		mComposite1 = (RelativeLayout) findViewById(R.id.mComposite1);
		mnumber1 = (RelativeLayout) findViewById(R.id.mnumber1);
		mprice1 = (RelativeLayout) findViewById(R.id.mprice1);
		mfilter1 = (RelativeLayout) findViewById(R.id.mfilter1);
		xrefresh = (XRefreshView) findViewById(R.id.xrefresh);
		xrefresh.setPullRefreshEnable(false);
		xrefresh.setCustomHeaderView(new HeaderView(this));
		xrefresh.setXRefreshViewListener(new XRefreshViewListener() {
			
			@Override
			public void onRelease(float direction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRefresh(boolean isPullDown) {
				xrefresh.stopRefresh();
			}
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadMore(boolean isSilence) {
				page++;
				loadData();
			}
			
			@Override
			public void onHeaderMove(double headerMovePercent, int offsetY) {
				// TODO Auto-generated method stub
				
			}
		});
		MyFootView footView = new MyFootView(this);
		xrefresh.setCustomFooterView(footView);
		mComposite.setOnClickListener(this);
		mnumber.setOnClickListener(this);
		mprice.setOnClickListener(this);
		mfilter.setOnClickListener(this);
		mComposite1.setOnClickListener(this);
		mnumber1.setOnClickListener(this);
		mprice1.setOnClickListener(this);
		mfilter1.setOnClickListener(this);
		topbar_goback_btn.setOnClickListener(this);
		second_hei.setOnClickListener(this);
		second_bai.setOnClickListener(this);
		biankuang1.setOnClickListener(this);
		biankuang2.setOnClickListener(this);
		mscrollview.setOnScrollListener(this);
	}
	private void loadData(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		initstype();
		paramsMap.put("rowkey", rowkey);
		paramsMap.put("page", page+"");
		paramsMap.put("sortWay", sortway);
		paramsMap.put("filterWay", filterWay);
		paramsMap.put("domain", domain);
		paramsMap.put("brand", brand);
		dataFlow1.requestData(2, "newApp/querySimilarList", paramsMap, this);
	}
	private void initData(){
		Map<String, String> paramsMap = new HashMap<String, String>();
		initstype();
		paramsMap.put("rowkey", rowkey);
		paramsMap.put("page", page+"");
		paramsMap.put("sortWay", sortway);
		paramsMap.put("filterWay", filterWay);
		paramsMap.put("domain", domain);
		paramsMap.put("brand", brand);
		dataFlow.requestData(3, "newApp/querySimilarList", paramsMap, this);
	}
	private void initstype(){
		if (!istv1&&!istv2) {
			filterWay ="0";
		}else if(istv1&&!istv2){
			filterWay ="1";
		}else if(!istv1&&istv2){
			filterWay ="2";
		}else{
			filterWay ="3";
		}
		
	}
	/** 
     * 窗口有焦点的时候，即所有的布局绘制完毕的时候，我们来获取购买布局的高度和myScrollView距离父类布局的顶部位置 
     */  
    @Override    
    public void onWindowFocusChanged(boolean hasFocus) {    
        super.onWindowFocusChanged(hasFocus);    
        if(hasFocus){  
        	hinttextHeight = hinttext.getHeight();  
        	viewboxHeight = viewbox.getHeight();  
//            mfilterboxHeight = mfilterbox.getTop();  
//              
//            myScrollViewTop = mscrollview.getTop();  
        }  
    }
    //如果滑到一级菜单一开始消失，一级菜单二显示
   	@Override
   	public void onScroll(int scrollY) {
           if(scrollY > (hinttextHeight+viewboxHeight)){  
        	   home_layout_bar1.setVisibility(View.VISIBLE);
           }else if(scrollY <= hinttextHeight + viewboxHeight){  
        	   home_layout_bar1.setVisibility(View.GONE);
           } 
   	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.second_bai:
			break;
		case R.id.second_hei:
			second.setVisibility(View.GONE);
			break;
		case R.id.biankuang1:
			if (istv1) {
				istv1 = false;
				tv1.setTextColor(Color.parseColor("#222222"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview);
			}else{
				istv1 = true;
				tv1.setTextColor(Color.parseColor("#0098ff"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview2);
			}
			break;
		case R.id.biankuang2:
			if (istv2) {
				istv2 = false;
				tv2.setTextColor(Color.parseColor("#222222"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview);
			}else{
				istv2 = true;
				tv2.setTextColor(Color.parseColor("#0098ff"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview2);
			}
			break;
		case R.id.ensure:
				if ("".equals(brand) &&"".equals(domain)&& !istv1 && !istv2) {
					filter_img.setImageResource(R.mipmap.shaixuan_01);
					filter.setTextColor(Color.parseColor("#222222"));
				}else{
					filter_img.setImageResource(R.mipmap.shaixuan_02);
					filter.setTextColor(Color.parseColor("#0098ff"));
				}
				second.setVisibility(View.GONE);
				initData();
			
			break;
		case R.id.request:
				istv1 = false;
				tv1.setTextColor(Color.parseColor("#222222"));
				biankuang1.setBackgroundResource(R.drawable.shaixuan_textview);
				istv2 = false;
				tv2.setTextColor(Color.parseColor("#222222"));
				biankuang2.setBackgroundResource(R.drawable.shaixuan_textview);
				filter_img.setImageResource(R.mipmap.shaixuan_01);
				filter.setTextColor(Color.parseColor("#222222"));
				data.clear();
				filterWay = "0";
				domain = "";
				brand ="";
				if (view_box!=null) {
					view_box.removeAllViews();
				}
				if (shopbox!=null) {
					shopbox.removeAllViews();
				}
				loadFilterBrand(jsonObject);
				loadfiltershop(jsonObject);
			break;
		case R.id.mComposite:
			rankcomposite();
			break;
		case R.id.mnumber:
			ranknumber();
			break;
		case R.id.mprice:
			rankprice();
			break;
		case R.id.mfilter:
			second.setVisibility(View.VISIBLE);
			break;
		case R.id.mComposite1:
			rankcomposite();
			break;
		case R.id.mnumber1:
			ranknumber();
			break;
		case R.id.mprice1:
			rankprice();
			break;
		case R.id.mfilter1:
			second.setVisibility(View.VISIBLE);
			break;
		case R.id.topbar_goback_btn:
			finish();
			break;

		default:
			break;
		}
	}
	private void rankcomposite(){
		mtop.setImageResource(R.mipmap.gaodi_01);
		mtop1.setImageResource(R.mipmap.gaodi_01);
		compositerank.setTextColor(Color.parseColor("#0098ff"));
		sellrank.setTextColor(Color.parseColor("#222222"));
		filter_price.setTextColor(Color.parseColor("#222222"));
		compositerank1.setTextColor(Color.parseColor("#0098ff"));
		sellrank1.setTextColor(Color.parseColor("#222222"));
		filter_price1.setTextColor(Color.parseColor("#222222"));
		page=1;
		sortway = "0";
		initData();
	}
	private void ranknumber(){
		mtop.setImageResource(R.mipmap.gaodi_01);
		mtop1.setImageResource(R.mipmap.gaodi_01);
		sellrank.setTextColor(Color.parseColor("#0098ff"));
		compositerank.setTextColor(Color.parseColor("#222222"));
		filter_price.setTextColor(Color.parseColor("#222222"));
		sellrank1.setTextColor(Color.parseColor("#0098ff"));
		compositerank1.setTextColor(Color.parseColor("#222222"));
		filter_price1.setTextColor(Color.parseColor("#222222"));
		page=1;
		sortway = "1";
		initData();
	}
	private void rankprice(){
		filter_price.setTextColor(Color.parseColor("#0098ff"));
		sellrank.setTextColor(Color.parseColor("#222222"));
		compositerank.setTextColor(Color.parseColor("#222222"));
		filter_price1.setTextColor(Color.parseColor("#0098ff"));
		sellrank1.setTextColor(Color.parseColor("#222222"));
		compositerank1.setTextColor(Color.parseColor("#222222"));
		page=1;
		if (isprice) {
			sortway = "2";
			mtop.setImageResource(R.mipmap.gaodi_02);
			isprice = false;
		}else{
			sortway = "3";
			mtop.setImageResource(R.mipmap.gaodi_03);
			isprice = true;
		}
		initData();
	}
	public View inflate(int id) {
		return getLayoutInflater().inflate(id, null, false);
	}
	private void initthis(final JSONObject json) {
		View view = inflate(R.layout.listview_item_result2);
		ImageView img = (ImageView) view.findViewById(R.id.item_img);
		TextView title = (TextView) view.findViewById(R.id.item_title);
		TextView item_offer = (TextView) view.findViewById(R.id.item_offer);
		TextView mlittleprice = (TextView) view.findViewById(R.id.mlittleprice);
		TextView mbigprice = (TextView) view.findViewById(R.id.mbigprice);
		LinearLayout domainLayout = (LinearLayout) view.findViewById(R.id.domain_layout);
		RelativeLayout intentbuy1 = (RelativeLayout)view.findViewById(R.id.intentbuy1);
		title.setText(json.optString("thistitle").toString());
		 final String quote = json.optString("thisnumbercount").toString();
	     String price = json.optString("thisprice").toString();
	     String hnumber = json.optString("thiscomnum").toString();
	     
	     
	     if (price.contains(".")) {
	    	 int end = price.indexOf(".");
		        String bigprice = price.substring(0, end);
				String littleprice = price.substring(end, price.length());
		        mbigprice.setText(bigprice);
		}else{
			 mbigprice.setText(price);
		}
	     final String domain1 = json.optString("thisdomain").toString();
	     final String url = json.optString("thisurl").toString();
	     final String titel1 = json.optString("thistitle").toString();
	    if (!json.optString("thisurl").isEmpty()) {
	    	intentbuy1.setVisibility(View.VISIBLE);
		}
			
		if (Integer.valueOf(hnumber)>10000) {
			if (Integer.valueOf(hnumber)>100000000) {
				DecimalFormat df = new DecimalFormat("###.0");  
				String num = df.format(Double.valueOf(hnumber)/100000000);
				item_offer.setText("全网总评"+num+"亿条  "+quote+"条报价");
			}else{
				DecimalFormat df = new DecimalFormat("###.0");  
				String num = df.format(Double.valueOf(hnumber)/10000);
				item_offer.setText("全网总评"+num+"万条  "+quote+"条报价");
			}
		}else{
			item_offer.setText("全网总评"+hnumber+"条  "+quote+"条报价");
		}
		 Glide.with(this)
			.load(json.optString("thisimgurl").toString())
			.into(img);
		 view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent;
				if (JumpIntentUtil.isJump1(domain1)) {
					intent = new Intent(CompareActivity.this,IntentActivity.class);
					intent.putExtra("title", titel1);
					intent.putExtra("domain", domain1);
					intent.putExtra("url", url);
					intent.putExtra("groupRowKey", rowkey);
				}else{
					intent = new Intent(CompareActivity.this,WebViewActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("groupRowKey", rowkey);
				}
				startActivity(intent);
			}
		});
		 
		 String allDomain = json.optString("thisalldomain").toString();
	        if(allDomain != null){
	        	String[] domains = allDomain.split(" ");
	            int maxLength = 5;
	            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 15), DensityUtil.dip2px(this, 15));
	            for (int i=0;i<domains.length && i<maxLength;i++) {
	    			ImageView imageView = new ImageView(this);
	    			layoutParams.setMargins(0,0,DensityUtil.dip2px(this, 4),0);
	    			imageView.setLayoutParams(layoutParams);
	    	        int drawS = getResources().getIdentifier(domains[i],"domain", this.getPackageName());
	    	        imageView.setBackgroundResource(drawS);
	    	        domainLayout.removeAllViews();
	    	        domainLayout.addView(imageView);
	    		}
	            if(domains.length > maxLength){
	            	ImageView imageView = new ImageView(this);
	            	imageView.setLayoutParams(layoutParams);
	    	        imageView.setImageResource(R.mipmap.domain_more);
	    	        domainLayout.removeAllViews();
	    	        domainLayout.addView(imageView);
	            }
	        }
		 viewbox.addView(view);
	}
	private void initListView(JSONArray page) throws JSONException {
		for (int i = 0; i < page.length(); i++) {
			JSONObject object = page.getJSONObject(i);
			HashMap<String, Object> itemMap = new HashMap<String, Object>();
			itemMap.put("img", object.optString("imgUrl"));
			itemMap.put("title",object.optString("title"));
			String price = object.optString("price");
			itemMap.put("price", price);
			itemMap.put("hnumber", object.optString("comnum"));
			itemMap.put("domainCount", object.optString("domainCount"));
			itemMap.put("groupRowKey", object.optString("groupRowkey"));
			itemMap.put("quote", object.optString("numberCount"));
			itemMap.put("allDomain", object.optString("alldomain"));
			if (!object.optString("url").isEmpty()) {
				itemMap.put("url", object.optString("url"));
				itemMap.put("title", object.optString("title"));
				itemMap.put("domain1", object.optString("domain"));
			}else{
				itemMap.put("url", "1");
				itemMap.put("title", "1");
				itemMap.put("domain1", "1");
			}
			itemMap.put("hassimi", "2");
			itemList.add(itemMap);
			itemList1.add(itemMap);
		}
		if (page.length()<12) {
			xrefresh.setLoadComplete(true);
		}else{
			xrefresh.setAutoLoadMore(true);
		}
		listadapter = new ResultListAdapter(itemList, this);
		mlistview.setAdapter(listadapter);
		mlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent;
				if (JumpIntentUtil.isJump(itemList,position,"domain1")) {
					intent = new Intent(CompareActivity.this,IntentActivity.class);
					intent.putExtra("url", itemList.get(position).get("url").toString());
					intent.putExtra("title", itemList.get(position).get("title").toString());
					intent.putExtra("domain", itemList.get(position).get("domain1").toString());
					intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
				}else{
					intent = new Intent(CompareActivity.this,WebViewActivity.class);
					intent.putExtra("url", itemList.get(position).get("url").toString());
					intent.putExtra("groupRowKey", itemList.get(position).get("groupRowKey").toString());
				}
				startActivity(intent);
			}
		});
		
	}
	private void loadFilterBrand(JSONObject json) {
		try {
//			Iterator<String> keys = json.keys();
//			while (keys.hasNext()) {
//				String key = keys.next();
//				Log.e("========key=======", key+"");
//			}
			final String jsonObject = json.optString("brand");
			abcBrand = json.optJSONObject("abcBrand");
			brandArr = jsonObject.split("\\|");
			final List<Map<String, Object>> data1 = new ArrayList<>();
			for (int i = 0; i < brandArr.length; i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("item_text", brandArr[i]);
				map.put("item_selected", "no");
				data1.add(map);
			}
			if (brandArr.length>3) {
				data.add(data1.get(0));
				data.add(data1.get(1));
				data.add(data1.get(2));
			}else{
				data.addAll(data1);
			}
			LayoutInflater inflater = LayoutInflater.from(this);
			view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
			gridView = (MyGridView) view.findViewById(R.id.mgridView);
			select1 = (TextView) view.findViewById(R.id.select);
			adapter = new SecondAdapter5(CompareActivity.this, data);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					if (position==8) {
						Intent intent = new Intent(CompareActivity.this, ReslutBrandActivity.class);
						intent.putExtra("jsonObject", jsonObject);
						intent.putExtra("abcBrand", abcBrand.toString());
						startActivityForResult(intent, 1);
					}else{
						Map<String, Object> item = data.get(position);
						
						if ("yes".equals(item.get("item_selected").toString())) {
							
							item.put("item_selected", "no");
							adapter.notifyDataSetChanged();
							select1.setText("");
							curposition=-1;
						}else{
							if (select1!=null&&select1.getText().toString()!="") {
								Map<String, Object> item1 = data.get(curposition);
								item1.put("item_selected", "no");
								item.put("item_selected", "yes");
								adapter.notifyDataSetChanged();
								select1.setText(item.get("item_text").toString());
								curposition=position;
							}else{
								item.put("item_selected", "yes");
								adapter.notifyDataSetChanged();
								select1.setText(item.get("item_text").toString());
								curposition=position;
							}
						}
						brand = select1.getText().toString();
					}
				}
			});
			TextView title = (TextView) view.findViewById(R.id.mtext);
			title.setText("品牌");
			final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
			RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
			if (brandArr.length<4) {
				imgclick.setVisibility(View.GONE);
			}else{
				imgclick.setVisibility(View.VISIBLE);
			}
			
			imgclick.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if (data.size()>3) {
						foldTopAnimation(mimg);
						data.clear();
						data.add(data1.get(0));
						data.add(data1.get(1));
						data.add(data1.get(2));
						adapter.notifyDataSetChanged();
					}else{
						unfoldTopAnimation(mimg);
						if (brandArr.length>9) {
							data.clear();
							for (int i = 0; i < 8; i++) {
								Map<String, Object> map = new HashMap<>();
								map.put("item_text", brandArr[i]);
								map.put("item_selected", "no");
								data.add(map);
							}
							Map<String, Object> map = new HashMap<>();
							map.put("item_text", "全部品牌");
							map.put("item_selected", "no");
							data.add(map);
							adapter.notifyDataSetChanged();
						}else{
							data.clear();
							Map<String, Object> map = new HashMap<>();
							for (int i = 0; i < brandArr.length; i++) {
								map.put("item_text", brandArr[i]);
								map.put("item_selected", "no");
								data.add(map);
							}
							adapter.notifyDataSetChanged();
						}
					}
					
				}
			});
			view_box.addView(view);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}
	
	
	
	
	
	private void loadfiltershop(JSONObject jsonObject){
		String key = jsonObject.optString("domainChall");
		final List<Map<String, Object>> data = new ArrayList<>(); 
		final List<Map<String, Object>> data1 = new ArrayList<>();
		String[] vs = key.split("\\|");
		for (int i = 0; i < vs.length; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("item_text", vs[i]);
			map.put("item_selected", "no");
			data.add(map);
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.popup_window_sreach_result_filter, null);
		final ImageView mimg = (ImageView) view.findViewById(R.id.mimg);
		RelativeLayout imgclick = (RelativeLayout) view.findViewById(R.id.imgclick);
		TextView title = (TextView) view.findViewById(R.id.mtext);
		title.setText("商城");
		final TextView select = (TextView) view.findViewById(R.id.select);
		MyGridView mgridView = (MyGridView) view.findViewById(R.id.mgridView);
		if (data.size()>3) {
			data1.add(data.get(0));
			data1.add(data.get(1));
			data1.add(data.get(2));
			imgclick.setVisibility(View.VISIBLE);
		}else{
			data1.addAll(data);
			imgclick.setVisibility(View.GONE);
		}
		final SecondAdapter5 adapter = new SecondAdapter5(this, data1);
		mgridView.setAdapter(adapter);
		
		imgclick.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if (data1.size()<4) {
					data1.clear();
					unfoldTopAnimation(mimg);
					data1.addAll(data);
					adapter.notifyDataSetChanged();
				}else{
					data1.clear();
					foldTopAnimation(mimg);
					data1.add(data.get(0));
					data1.add(data.get(1));
					data1.add(data.get(2));
					adapter.notifyDataSetChanged();
				}
				
				
			}
		});
		mgridView.setOnItemClickListener(new OnItemClickListener() {

			private Toast toast;

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position, long arg3) {
				Map<String, Object> item = data.get(position);
				
				if ("yes".equals(item.get("item_selected").toString())) {
					
					item.put("item_selected", "no");
					adapter.notifyDataSetChanged();
					String str = null;
					for (int i = 0; i < data.size(); i++) {
						Map<String, Object> list = data.get(i);
						if ("yes".equals(list.get("item_selected").toString())) {
							String str1 = list.get("item_text").toString();
							if (str!=null) {
								str = str+","+str1;
							}else{
								str = ","+str1;
							}
							
						}
					}
					if (str!=null) {
						select.setText(str.subSequence(1, str.length()));
						domain = (String) str.subSequence(1, str.length());
					}else{
						select.setText(null);
						domain = "";
					}
					
					
				}else{
					int num=0;
					for (int i = 0; i < data.size(); i++) {
						Map<String, Object> list = data.get(i);
						if ("yes".equals(list.get("item_selected").toString())) {
							num++;
						}
					}
					if (num<5) {
						select.setVisibility(View.VISIBLE);
						item.put("item_selected", "yes");
						adapter.notifyDataSetChanged();
						if (select.getText().toString().isEmpty()) {
							domain = (String) item.get("item_text").toString();
							select.setText(item.get("item_text").toString());
						}else{
							domain = (String)select.getText()+","+item.get("item_text").toString();
							select.setText(select.getText()+","+item.get("item_text").toString());
						}
					}else{
						if (toast!=null) {
							toast.cancel();
						}
						toast = Toast.makeText(CompareActivity.this, "最多只能选5个", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		shopbox.addView(view);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data5) {
		super.onActivityResult(requestCode, resultCode, data5);
		switch (requestCode) {
		case 1:
			if (data5.getStringExtra("choseBrandName")!=null) {
				String choseBrandName = data5.getStringExtra("choseBrandName");
				if (curposition!=-1) {
					Map<String, Object> item1 = data.get(curposition);
					item1.put("item_selected", "no");
					curposition=-1;
				}
				adapter.notifyDataSetChanged();
			for (int i = 0; i < data.size(); i++) {
				if (choseBrandName.equals(data.get(i).get("item_text").toString())) {
					Map<String, Object> item = data.get(i);
					item.put("item_selected", "yes");
					curposition=i;
					adapter.notifyDataSetChanged();
					select1.setText(item.get("item_text").toString());
				}
			}
			brand = choseBrandName;
			}
			break;

		default:
			break;
		}
		
	}
	/**
	 * 展开弹窗效果
	 */
	private void unfoldTopAnimation(View v) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 0f, 180f);
		animator.setDuration(500);
		animator.start();
	}

	/**
	 * 收起弹窗效果
	 */
	private void foldTopAnimation(View v) {
		ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 180f, 0f);
		animator.setDuration(500);
		animator.start();
	}
	  private void NowPrice(){
	    	thread = new Thread(new Runnable() {
				public void run() {
					while (isrun) {
						if (isrequest == true) {
							try {
								Map<String, String> params = new HashMap<>();
								if (!"0".equals(itemList1.get(requestnum).get("purl"))) {
									String str;
									String content;
									params.put("domain", itemList1.get(requestnum).get("domain1").toString());
									params.put("rowkey", itemList1.get(requestnum).get("groupRowKey").toString());
									params.put("fromwhere", "android");
									if (itemList1.get(requestnum).get("purl").toString().contains("||")) {
										String url = itemList1.get(requestnum).get("purl").toString();
										String[] split = url.split("\\|\\|");
										String referrer=split[1];
										content = HttpUtil.getHttp1(params, split[0], CompareActivity.this, referrer);
										params.put("pcontent", content);
										str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", CompareActivity.this);
									}else{
										content = HttpUtil.getHttp1(params, itemList1.get(requestnum).get("purl").toString(), CompareActivity.this,null);
										params.put("pcontent", content);
										str = HttpUtil.getHttp(params, Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct", CompareActivity.this);
									}
									JSONObject object = new JSONObject(str);
									if ("3".equals(object.optString("type"))) {
										if ("".equals(object.optString("url"))) {
											content = HttpUtil.getHttp1(params, itemList1.get(requestnum).get("url").toString(), CompareActivity.this,null);
										}else{
											content = HttpUtil.getHttp1(params, object.optString("url"), CompareActivity.this,null);
										}
										params.put("pcontent", content);
										String url = Constants.MAIN_BASE_URL_MOBILE+"checkService/checkProduct";
										str = HttpUtil.getHttp(params, url, CompareActivity.this);
									}
									Message mes = handler.obtainMessage();
									mes.obj = str;
									mes.arg1 = requestnum;
									mes.what =0;
									handler.sendMessage(mes);
								}
								if (requestnum+1 >= itemList1.size()) {
									isrequest = false;
								}
								requestnum++;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			});
			thread.start();
	    }
		private Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					String str = msg.obj.toString();
					int i = msg.arg1;
					try {
						JSONObject object = new JSONObject(str);
						switch (object.optString("type")) {
						case "0":
							itemList.remove(i-removenum);
							listadapter.notifyDataSetChanged();
							removenum++;
							if (page == 1 && i%12 == 0 && removenum >6) {
								page++;
								loadData();
							}
							break;
						case "1":
							String price = object.optString("price");
							itemList.get(i-removenum).put("price", price);
							listadapter.notifyDataSetChanged();
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
		try {
		switch (requestCode) {
		case 1:
			xrefresh.stopRefresh();
			xrefresh.setVisibility(View.VISIBLE);
			jsonObject = new JSONObject(content);
			JSONArray page = jsonObject.getJSONArray("page");
			initListView(page);
			initthis(jsonObject);
			hinttext.setVisibility(View.VISIBLE);
			mscrollview.fullScroll(ScrollView.FOCUS_UP);
			loadfiltershop(jsonObject);
			loadFilterBrand(jsonObject);
			break;
		case 2:
			xrefresh.stopRefresh();
			xrefresh.stopLoadMore();
			JSONArray page1 = new JSONArray(content);
			for (int i = 0; i < page1.length(); i++) {
				JSONObject object = page1.getJSONObject(i);
				HashMap<String, Object> itemMap = new HashMap<String, Object>();
				itemMap.put("img", object.optString("imgUrl"));
				itemMap.put("title",object.optString("title"));
				String price = object.optString("price");
				itemMap.put("price", price);
				itemMap.put("hnumber", object.optString("comnum"));
				itemMap.put("domainCount", object.optString("domainCount"));
				itemMap.put("groupRowKey", object.optString("groupRowkey"));
				itemMap.put("quote", object.optString("numberCount"));
				itemMap.put("allDomain", object.optString("alldomain"));
				if (!object.optString("url").isEmpty()) {
					itemMap.put("url", object.optString("url"));
					itemMap.put("title", object.optString("title"));
					itemMap.put("domain1", object.optString("domain"));
				}else{
					itemMap.put("url", "1");
					itemMap.put("title", "1");
					itemMap.put("domain1", "1");
				}
				itemMap.put("hassimi", "2");
				itemList.add(itemMap);
			}
			if (page1.length()<12) {
				xrefresh.setLoadComplete(true);
			}
			listadapter.notifyDataSetChanged();
			break;
		case 3:
			xrefresh.stopLoadMore();
			itemList.clear();
			JSONArray page2 = new JSONArray(content);
			if (page2.length()==0) {
				listadapter.notifyDataSetChanged();
				noresult.setVisibility(View.VISIBLE);
				mlistview.setVisibility(View.GONE);
				xrefresh.setLoadComplete(true);
			}else{
				noresult.setVisibility(View.GONE);
				mlistview.setVisibility(View.VISIBLE);
				for (int i = 0; i < page2.length(); i++) {
					JSONObject object = page2.getJSONObject(i);
					HashMap<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("img", object.optString("imgUrl"));
					itemMap.put("title",object.optString("title"));
					String price = object.optString("price");
					itemMap.put("price", price);
					itemMap.put("hnumber", object.optString("comnum"));
					itemMap.put("domainCount", object.optString("domainCount"));
					itemMap.put("groupRowKey", object.optString("groupRowkey"));
					itemMap.put("quote", object.optString("numberCount"));
					itemMap.put("allDomain", object.optString("alldomain"));
					if (!object.optString("url").isEmpty()) {
						itemMap.put("url", object.optString("url"));
						itemMap.put("title", object.optString("title"));
						itemMap.put("domain1", object.optString("domain"));
					}else{
						itemMap.put("url", "1");
						itemMap.put("title", "1");
						itemMap.put("domain1", "1");
					}
					itemMap.put("hassimi", "2");
					itemList.add(itemMap);
					itemList1.add(itemMap);
				}
				if (page2.length()<12) {
					xrefresh.setLoadComplete(true);
				}
				listadapter.notifyDataSetChanged();
				mscrollview.fullScroll(ScrollView.FOCUS_UP);
			}
			
			
			break;
		default:
			break;
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {
		if (second.getVisibility()==View.VISIBLE) {
			second.setVisibility(View.GONE);
			return;
		}else{
			isrun = false;
			finish();
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		isrun = false;
		super.onDestroy();
	}
}
