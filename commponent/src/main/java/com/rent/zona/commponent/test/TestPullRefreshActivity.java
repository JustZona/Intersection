package com.rent.zona.commponent.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.pullrefresh.PullToRefreshBaseActivity;
import com.rent.zona.commponent.views.AppTitleBar;

public class TestPullRefreshActivity extends PullToRefreshBaseActivity<String>{
    TextView textView;
    @Override
    protected int getLayoutResId() {
        return -1;
    }

    @Override
    public int getCusContentView() {
        return R.layout.test_fragment_pull_refresh;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textView= (TextView) findViewById(R.id.test_text);
        AppTitleBar titleBar= (AppTitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("测试下拉刷新");
    }

    int index=1;
    @Override
    protected void refresh() {
        index++;
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.VISIBLE);
                if(index%3==0){
                    onLoadFailed(new Exception());
                }else{
                    textView.setText(textView.getText().toString()+index);
                    onLoadSuccess(textView.getText().toString());
                }
            }
        },2000);
    }

    @Override
    protected String cacheKey() {
        return "TestPullRefreshActivity";
    }

    @Override
    protected boolean isDataEmpty() {
        return super.isDataEmpty();
    }

    @Override
    protected void onCacheLoaded(String cacheData) {
        super.onCacheLoaded(cacheData);
        textView.setText(cacheData);
    }
}
