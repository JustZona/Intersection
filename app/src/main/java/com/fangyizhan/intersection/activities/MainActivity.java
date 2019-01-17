package com.fangyizhan.intersection.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyizhan.intersection.R;
import com.fangyizhan.intersection.adapter.MainActivityAdapter;
import com.fangyizhan.intersection.fragments.FragmentFind;
import com.fangyizhan.intersection.fragments.FragmentHome;
import com.fangyizhan.intersection.fragments.FragmentMsg;
import com.fangyizhan.intersection.views.SlideOrNoViewPager;
import com.rent.zona.commponent.base.BaseFragment;
import com.rent.zona.commponent.base.BaseFragmentActivity;
import com.rent.zona.commponent.base.HandleBackUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseFragmentActivity {

    @BindView(R.id.main_content_vp)
    SlideOrNoViewPager mainContentVp;
    @BindView(R.id.main_msg_tv)
    TextView mainMsgTv;
    @BindView(R.id.main_find_tv)
    TextView mainFindTv;
    @BindView(R.id.main_home_tv)
    TextView mainHomeTv;

    private long startTime = 0;
    private FragmentHome fragmentHome;
    private FragmentMsg fragmentMsg;
    private FragmentFind fragmentFind;
    private MainActivityAdapter mainActivityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mainContentVp.setOffscreenPageLimit(3);
        mainContentVp.setCanScroll(false);
        mainContentVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mainMsgTv.setSelected(position==0);
                mainFindTv.setSelected(position==1);
                mainHomeTv.setSelected(position==2);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mainMsgTv.setSelected(true);
        fragmentMsg=new FragmentMsg();
        fragmentFind=new FragmentFind();
        fragmentHome=new FragmentHome();
        List<BaseFragment> fragments=new ArrayList<>();
        fragments.add(fragmentMsg);
        fragments.add(fragmentFind);
        fragments.add(fragmentHome);
        mainActivityAdapter = new MainActivityAdapter(getSupportFragmentManager(), this, fragments);
        mainContentVp.setAdapter(mainActivityAdapter);

    }

    @OnClick({R.id.main_msg_tv, R.id.main_find_tv, R.id.main_home_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_msg_tv:
                mainMsgTv.setSelected(true);
                mainContentVp.setCurrentItem(0);
                break;
            case R.id.main_find_tv:
                mainFindTv.setSelected(true);
                mainContentVp.setCurrentItem(1);
                break;
            case R.id.main_home_tv:
                mainHomeTv.setSelected(true);
                mainContentVp.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!HandleBackUtil.handleBackPress(this)) {
            super.onBackPressed();
        }else {
            long cuttentTime = System.currentTimeMillis();
            if ((cuttentTime-startTime)>=2000){
                getActivityHelper().toast("再按一次退出程序",this);
                startTime=cuttentTime;
            }else {
                finish();
            }
        }
    }
}
