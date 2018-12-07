package cn.kuaishang.kssdk.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.kuaishang.kssdk.R;
import cn.kuaishang.kssdk.adapter.KSShowPhotoFragmentAdapter;
import cn.kuaishang.kssdk.fragment.KSShowPhotoFragment;
import cn.kuaishang.util.KSKey;
import cn.kuaishang.util.StringUtil;

/**
 * 显示图片
 * 
 * @author Administrator
 *
 */
public class KSShowPhotoActivity extends FragmentActivity{

	private KSShowPhotoFragmentAdapter mAdapter;
//	private KSPopup popup;		//操作按钮弹出框
	private ViewPager viewPager;
	private LinearLayout contentView;
	private TextView nameText;
	private TextView timeText;
	private List<String[]> datas;
 
    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_showphoto);
        
		Map<String, Object> data = (Map<String, Object>) getIntent().getSerializableExtra(KSKey.DATA);
    	datas = (List<String[]>) data.get(KSKey.KEY_CONTENT);
        final int curIndex = StringUtil.getInteger(data.get(KSKey.KEY_CURINDEX));
        List<String> imgPaths = new ArrayList<String>();
        for(String[] obj : datas){
        	String path = StringUtil.getString(obj[0]);
        	imgPaths.add(path);
        }
        mAdapter = new KSShowPhotoFragmentAdapter(getSupportFragmentManager(),imgPaths);

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(curIndex);
        contentView = (LinearLayout) findViewById(R.id.msgContentView);
        nameText = (TextView) findViewById(R.id.name);
        timeText = (TextView) findViewById(R.id.time);
        
        //setActionBarTitle((curIndex+1)+"/"+mAdapter.getCount());
        setMsgContent(curIndex);
        
        viewPager.post(new Runnable() {
			@Override
			public void run() {
				KSShowPhotoFragment fragment = mAdapter.getItem(curIndex);
				fragment.downLoad();
			}
		});
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//setActionBarTitle((position+1)+"/"+mAdapter.getCount());
		        setMsgContent(position);
				KSShowPhotoFragment fragment = mAdapter.getItem(position);
				fragment.downLoad();
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg1) {
			}
		});
        
        //初始化popup
//		popup = new KSPopup(context);
//		final List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
//		datas.add(KSUIUtil.newPopupData(getString(R.string.acbutton_saveToSdcard),R.drawable.actionic_save));
//		popup.setDatas(datas);
//		popup.getListView().setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
//				if (position == 0) {
//					//保存到手机
//					try {
//						int index = viewPager.getCurrentItem();
//						KSShowPhotoFragment fragment = mAdapter.getItem(index);
//						fragment.saveImage();
//					} catch (Exception e) {
//						KSUtil.printException("保存图片出错", e);
//					}
//				} else if (position == 1) {
//
//				}
//				popup.dismiss();
//			}
//		});
    }
    
    public void setMsgContent(int position){
    	String[] obj = datas.get(position);
    	String senderName = StringUtil.getString(obj[1]);
    	String sendTime = StringUtil.getString(obj[2]);
    	nameText.setText(senderName);
    	timeText.setText(sendTime);
    }
    
    public void checkStatusBar(){
		onBackPressed();
//    	if(contentView.getVisibility()==View.VISIBLE){
//    		contentView.setVisibility(View.GONE);
//    	}else{
//    		contentView.setVisibility(View.VISIBLE);
//    	}
    }
    
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(R.string.acbutton_oper)
//			.setIcon(R.drawable.actionic_overflow)
//			.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//	    return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	    if(super.onOptionsItemSelected(item))return true;
//	    String title = KSUtil.getString(item.getTitle());
//		if(title.equals(getString(R.string.acbutton_oper))){
//			//显示菜单
//			popup.show(findViewById(R.id.viewPop));
//		}
//		return true;
//	}
}
