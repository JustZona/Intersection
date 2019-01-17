package com.rent.zona.commponent.base.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/7/3
 * @modifyTime： 2017/7/3
 * @explain：说明
 */


public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<TabInfo> tabs = null;

    public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<TabInfo> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = null;
        if (tabs != null && pos < tabs.size()) {
            TabInfo tab = tabs.get(pos);
            if (tab == null)
                return null;
            fragment = tab.createFragment();
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (tabs != null && tabs.size() > 0)
            return tabs.size();
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TabInfo tab = tabs.get(position);
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tab.setFragment(fragment);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getName();
    }
}
