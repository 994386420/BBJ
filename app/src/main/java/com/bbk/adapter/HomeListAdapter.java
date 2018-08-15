package com.bbk.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bbk.fragment.HomeListFragment;
import com.bbk.fragment.TmpFragment;

import java.util.List;

/**
 * Created by yuexi on 2016/11/10.
 */

public class HomeListAdapter extends FragmentStatePagerAdapter {

    private List<String> groupList;
    private Context context;

    public HomeListAdapter(FragmentManager fm, List<String> groupList, Context context) {
        super(fm);
        this.groupList = groupList;
        this.context = context;

    }

    @Override
    public Fragment getItem(int position) {
        position = position % groupList.size();

        HomeListFragment fragment=new HomeListFragment();

        Bundle bundle=new Bundle();
//        bundle.putString("title",titleList.get(position));
        for (int i = 0; i < groupList.size(); i++) {
            bundle.putString("pic"+i,groupList.get(position));
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        final int itemsSize = groupList.size();
        return itemsSize;
    }

}
