package cn.kuaishang.kssdk.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.kuaishang.kssdk.KSUIUtil;
import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.album.AlbumHelper;
import cn.kuaishang.kssdk.album.ImageBucket;
import cn.kuaishang.kssdk.album.ImageBucketAdapter;
import cn.kuaishang.util.KSKey;

public class AlbumBucketActivity extends FragmentActivity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	Class<?> targetClass;
	public static Bitmap bimap;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ks_album_bucket);
		
		Map<String,Object> data = (Map<String, Object>) getIntent().getSerializableExtra(KSKey.DATA);
		targetClass = (Class<?>) data.get(KSKey.CLASS);
		
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		dataList = helper.getImagesBucketList(true);	
		bimap= BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(AlbumBucketActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				ImageBucket bucket = dataList.get(position);
				Map<String,Object> data = new HashMap<String, Object>();
				data.put(KSKey.CLASS, targetClass);
				data.put(KSKey.LIST, bucket.imageList);
				data.put(KSKey.TITLE, bucket.bucketName);
				KSUIUtil.openActivity(AlbumBucketActivity.this, data, AlbumGridActivity.class);
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
//		finish();
//		return true;
//	}
}
