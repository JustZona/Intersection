package com.fangyizhan.intersection.activities;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.fangyizhan.intersection.R;
import com.rent.zona.commponent.base.BaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;
import com.rent.zona.commponent.views.CommonEmptyView;
import com.rent.zona.commponent.views.EmptyRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 黑名单
 */
public class BlackListActivity extends BaseActivity {

    @BindView(R.id.content_rv)
    EmptyRecyclerView contentRv;
    @BindView(R.id.empty)
    CommonEmptyView empty;
    @BindView(R.id.content_root)
    FrameLayout contentRoot;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.titlebar)
    AppTitleBar appTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        ButterKnife.bind(this);
        appTitleBar.setBackListener(() -> {
            finish();
            return false;
        });
        appTitleBar.setTitle("黑名单");
    }
}
