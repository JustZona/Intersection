package com.fangyizhan.intersection.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.rent.zona.commponent.base.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */

public class MainActivityAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<BaseFragment> fragments;

    public MainActivityAdapter(FragmentManager fm, Context context, List<BaseFragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
