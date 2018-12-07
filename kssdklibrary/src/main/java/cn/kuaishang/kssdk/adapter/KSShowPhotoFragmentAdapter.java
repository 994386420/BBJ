package cn.kuaishang.kssdk.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.kuaishang.kssdk.fragment.KSShowPhotoFragment;

public class KSShowPhotoFragmentAdapter extends FragmentStatePagerAdapter{

	private List<String> urls;
	private List<KSShowPhotoFragment> fragments;

	public KSShowPhotoFragmentAdapter(FragmentManager fm, List<String> urls) {
		super(fm);
		this.urls = urls;
		fragments = new ArrayList<KSShowPhotoFragment>();
		for (String url : urls) {
			KSShowPhotoFragment fragment = new KSShowPhotoFragment(url);
			fragments.add(fragment);
		}
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public KSShowPhotoFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	public void removeItem(int index) {
		fragments.remove(index);
		urls.remove(index);
		notifyDataSetChanged();
	}
}