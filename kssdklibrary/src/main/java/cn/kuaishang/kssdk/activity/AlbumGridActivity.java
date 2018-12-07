package cn.kuaishang.kssdk.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.album.AlbumHelper;
import cn.kuaishang.kssdk.album.ImageGridAdapter;
import cn.kuaishang.kssdk.album.ImageItem;
import cn.kuaishang.kssdk.widget.KsProgressDialog;
import cn.kuaishang.util.FileUtil;
import cn.kuaishang.util.KSKey;

@SuppressLint("HandlerLeak")
public class AlbumGridActivity extends FragmentActivity {

	/** 进度条 */
	protected KsProgressDialog progressDialog;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;
	AlbumHelper helper;
	Button bt;
	Class<?> targetClass;
	int maxNums = 8;		//最多选择几张
	boolean flag = false;	//是否正在发送

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					Toast.makeText(AlbumGridActivity.this,"最多选择"+maxNums+"张图片",Toast.LENGTH_LONG).show();
					break;
				case 1:
					setProgressVisibility(false,"");
					Map<String,Object> data = new HashMap<String, Object>();
					data.put(KSKey.TYPE, "image");
					data.put(KSKey.LIST, msg.obj);
					KSUIUtil.openActivityForClear(AlbumGridActivity.this, data, targetClass);
					break;
				case 2:
					setProgressVisibility(false,"");
					Toast.makeText(AlbumGridActivity.this,"发送图片出错，请重试！",Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ks_album_grid);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		Map<String,Object> data = (Map<String, Object>) getIntent().getSerializableExtra(KSKey.DATA);
		targetClass = (Class<?>) data.get(KSKey.CLASS);
		dataList = (List<ImageItem>) data.get(KSKey.LIST);
		//setActionBarTitle(StringUtil.getString(data.get(KSKey.TITLE)));
		
		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setText(getString(R.string.ks_send)+"(0/"+maxNums+")");
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(flag)return;
				final List<String> selectList = adapter.getSelectList();
				if(selectList==null || selectList.size()==0){
					Toast.makeText(AlbumGridActivity.this,"请先选择图片",Toast.LENGTH_LONG).show();
					return;
				}
				flag = true;
				//发送中，转换图片(压缩)
				setProgressVisibility(true,"图片发送中...");
				final ArrayList<String> list = new ArrayList<String>();
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							for(String path : selectList){
								Bitmap bm = KSUIUtil.revitionImageSize(path);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtil.saveUploadBitmap(bm, "" + newStr);
								list.add(path);
							}
							flag = false;
							Message msg = new Message();
							msg.what=1;
							msg.obj=list;
							mHandler.sendMessage(msg);
						} catch (IOException e) {
							mHandler.sendEmptyMessage(2);
						}
					}
				}).start();
			}

		});
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(AlbumGridActivity.this, dataList,mHandler,maxNums);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
			public void onListen(int count) {
				bt.setText(getString(R.string.ks_send)+"("+count+"/"+maxNums+")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});
		findViewById(R.id.back_rl).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(R.string.comm_cancel)
//			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	    return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if(super.onOptionsItemSelected(item))return true;
//		KSUIUtil.openActivityForClear(context, null, targetClass);
//		return true;
//	}

	/**
	 * 显示/隐藏进度条
	 * @param visible 是否显示
	 * @param disMsg 显示内容
	 */
	protected void setProgressVisibility(boolean visible, CharSequence disMsg){
		if(visible){
			if(progressDialog == null){
				progressDialog = new KsProgressDialog(this, null
						, disMsg, false);
			}else{
				progressDialog.setMessage(disMsg);
			}
			progressDialog.show();
		}else if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}
}
