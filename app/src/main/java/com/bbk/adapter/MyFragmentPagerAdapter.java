package com.bbk.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.bbk.fragment.BaseViewPagerFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments;
	private FragmentManager fm;
	private OnTouchListener mListener;

	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public MyFragmentPagerAdapter(FragmentManager fm,
			List<Fragment> fragments) {
		super(fm);
		this.fm = fm;
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_UNCHANGED;
	}
	
	public void setOnTouchListener(OnTouchListener listener) {
		this.mListener = listener;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = container.getChildAt(position);
//		view.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(mListener != null) {
//					mListener.onTouch(v, event);
//				}
//				return false;
//			}
//		});
		return super.instantiateItem(container, position);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
//		super.destroyItem(container, position, object);
	}
//	public void setFragments(ArrayList<Fragment> fragments) {
//		
////		FragmentTransaction ft = fm.beginTransaction();
////		final long itemId = getItemId(position);
////        String name = makeFragmentName(container.getId(), itemId);
////        ft.remove(fm.findFragmentByTag(name));
////        ft.commit();
//        
//        
//		if (this.fragments != null) {
//			FragmentTransaction ft = fm.beginTransaction();
//			for (Fragment f : this.fragments) {
//				ft.remove(f);
//			}
//			ft.commit();
////			ft = null;
////			fm.executePendingTransactions();
//		}
//		this.fragments = fragments;
////		notifyDataSetChanged();
//	}
//	
//	@Override
//	public Object instantiateItem(ViewGroup container, int position) {
//		// TODO Auto-generated method stub
//		
//		return super.instantiateItem(container, position);
//	}
//	
//	private String makeFragmentName(int viewId, long id) {
//		return "android:switcher:" + viewId + ":" + id;
//	}

}
