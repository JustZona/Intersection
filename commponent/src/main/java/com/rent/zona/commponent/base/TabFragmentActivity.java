package com.rent.zona.commponent.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rent.zona.baselib.configs.LibConfigs;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.tab.TabFragmentPagerAdapter;
import com.rent.zona.commponent.base.tab.TabInfo;
import com.rent.zona.commponent.utils.DensityUtils;
import com.rent.zona.commponent.views.AppViewPager;

import java.util.ArrayList;

import butterknife.OnClick;

/**
 * @name：
 * @author： liuyun
 * @phone： 15723310658
 * @createTime： 2017/7/3
 * @modifyTime： 2017/7/3
 * @explain：说明
 */


public abstract class TabFragmentActivity extends BaseFragmentActivity {
    /**
     * 跳转到有tab页面的时候 默认显示的tab的index
     */
    public static final String EXTRA_TAB="tab_index";

    private static final String TAG = "TabFragmentActivity";
    private static final boolean DEBUG = LibConfigs.isDebugLog();
    private int mCurrentTab = 0;
    protected int mLastTab = -1;
    protected ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
    protected TabFragmentPagerAdapter mAdapter;
    protected TabLayout mTabLayout;
    protected AppViewPager mAppViewPager;
    private int tabIconMargin=2;//tablayout默认的icon和text之间的margin是8dp；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initContentView();

        initViews();

        enlargeIndicator();

        initData();
    }

    protected abstract void initData();


    public TabLayout getTabLayout(){
        return mTabLayout;
    }
    public TabLayout.Tab getTabAtIndex(int position){
        if(mTabLayout!=null && position<mTabLayout.getTabCount()){
            return mTabLayout.getTabAt(position);
        }
        return null;
    }
    public View getTabView(int position){
        TabLayout.Tab tab=getTabAtIndex(position);
        if(tab!=null){
            return tab.getCustomView();
        }
        return null;
    }


    private void initContentView(){
        int contentViewRes=getMainViewResId();
        if(contentViewRes==-1){
            contentViewRes= R.layout.activity_tab_fragment;
        }
        setContentView(contentViewRes);
    }
    public final void initViews() {
        // Show the main screen by default
         onPrepareTabInfoData(mTabs);
//        mCurrentTab =
        Intent intent = getIntent();
        if (intent != null) {
            mCurrentTab = intent.getIntExtra(EXTRA_TAB, mCurrentTab);
            if (mCurrentTab >= mTabs.size()) {
                mCurrentTab = 0;
            }
        }
        if (DEBUG) {
            Log.d(TAG, "mTabs.size() == " + mTabs.size() + ", cur: " + mCurrentTab);
        }
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), mTabs);

        // Setup the views
        mAppViewPager = (AppViewPager) findViewById(R.id.tab_viewpager1);
        mAppViewPager.setViewTouchMode(true);
        mAppViewPager.setAdapter(mAdapter);


        mTabLayout=(TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mAppViewPager);
        mTabLayout.removeAllTabs();
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        initTabViews();
        mAppViewPager.setOffscreenPageLimit(mTabs.size());
        mAppViewPager.setCurrentItem(mCurrentTab);
        mAppViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int tabId=getTabId(position);
                if(handlePageSelected(tabId)){
                    mAppViewPager.setCurrentItem(mLastTab);
                    return;
                }
                mLastTab=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 当选中的tab 需要登录或其他操作之后 才能展示此tab时  应重写此方法
     * 例如 当选中的tab为消息时  而当前还未登录，重写此方法 跳转到登录页面 并返回true
     * @param tabId
     * @return 是否选中此tab  ture不选中  false选中
     */
    protected boolean handlePageSelected(int tabId){
        return false;
    }

    protected void initTabViews(){
//        mTl.addTab(mTl.newTab().setCustomView(R.layout.common_tab_item).setText(tabs.get(0).getName()).setIcon(tabs.get(0).getIcon()));
        if(mTabs!=null && !mTabs.isEmpty()){

            for(TabInfo tab:mTabs){
                if (tab.getIcon() !=0) {
                    mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.common_tab_item).setText(tab.getName()).setIcon(tab.getIcon()));
                }else {
                    mTabLayout.addTab(mTabLayout.newTab().setCustomView(R.layout.common_tab_item).setText(tab.getName()));
                }
            }
        }
        //tablayout默认的icon和text之间的margin是8dp；即使你在布局里不是8dp tablayout也会动态改成8dp
        //所以要改变margin 必须等tab初始化之后  通过此种写法改变
        for(int i=0;i<mTabLayout.getTabCount();i++){
            ImageView icon=(ImageView) mTabLayout.getTabAt(i).getCustomView().findViewById(android.R.id.icon);
            ViewGroup.MarginLayoutParams lp = ((ViewGroup.MarginLayoutParams) icon.getLayoutParams());
            lp.bottomMargin = DensityUtils.dip2px(this,tabIconMargin);
            icon.requestLayout();
        }
    }
    protected abstract void onPrepareTabInfoData(ArrayList<TabInfo> tabs);
    protected void enlargeIndicator() {

    }
    /**
     * Navigate to target tab
     *
     * @param tabId the tab id, not the index
     */
    public void navigate(int tabId) {
        for (int index = 0, count = mTabs.size(); index < count; index++) {
            if (mTabs.get(index).getId() == tabId) {
                mAppViewPager.setCurrentItem(index, false);
            }
        }
    }
    private int  getTabId(int position){
        if(mTabs!=null && !mTabs.isEmpty()){

            return mTabs.get(position).getId();
        }
        return -1;
    }
    private int getTabPosition(int tabId){
        if(mTabs!=null && !mTabs.isEmpty()){

            for(int i=0;i<mTabs.size();i++){
                if(mTabs.get(i).getId()==tabId){
                    return i;
                }
            }
        }
        return -1;
    }
    private TabInfo getTabInfo(int tabId){
        for(TabInfo tabInfo:mTabs){
            if(tabInfo.getId()==tabId){
                return tabInfo;
            }
        }
        return null;
    }
    public void setHasTip(int tabId,boolean hasTip){
          TabInfo info=getTabInfo(tabId);
          if(info!=null){
              info.setHasTip(hasTip);
          }
          int position=getTabPosition(tabId);
        if(position>-1){
            View tip=getTabView(position).findViewById(R.id.tip);
            if(hasTip){
                tip.setVisibility(View.VISIBLE);
            }else{
                tip.setVisibility(View.GONE);
            }

        }
    }
    public void setTipCount(int tabId,int tipCount){
        TabInfo info=getTabInfo(tabId);
        if(info!=null){
            info.setTipCount(tipCount);
        }
        int position=getTabPosition(tabId);
        if(position>-1){
            TextView tip=(TextView)getTabView(position).findViewById(R.id.tip_text);
            if(tipCount>0){
                tip.setVisibility(View.VISIBLE);
                tip.setText(tipCount+"");
            }else{
                tip.setVisibility(View.GONE);
            }

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabs.clear();
        mTabs = null;
        mAdapter = null;
        mAppViewPager = null;
        mTabLayout = null;
    }
}
