package com.rent.zona.commponent.test;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.CommonFragmentActivity;
import com.rent.zona.commponent.base.pullrefresh.PllToRefreshBaseFragment;
import com.rent.zona.commponent.views.AppTitleBar;

public class TestPullRefreshFragment extends PllToRefreshBaseFragment<String> {
    TextView textView;
    public static void launch(Context context){
        Bundle fragParams = new Bundle();
        Intent intent = CommonFragmentActivity.createIntent(
                context,
                null,
                null,
                TestPullRefreshFragment.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutResId() {
        return -1;
    }

    @Override
    public int getContentView() {
        return R.layout.test_fragment_pull_refresh;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=view.findViewById(R.id.test_text);
        AppTitleBar titleBar=view.findViewById(R.id.titlebar);
        titleBar.setTitle("测试下拉刷新");
    }

    int index=1;
    @Override
    protected void refresh() {
        index++;
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(index%3==0){
                    onLoadFailed(new Exception());
                }else if(index%5==0){
                    onLoadFailed(new TaskException(1,"没有数据了"));
                }else{
                    textView.setText(textView.getText().toString()+index);
                    onLoadSuccess(textView.getText().toString());
                }
            }
        },2000);
    }

    @Override
    protected String cacheKey() {
        return "TestPullRefreshFragment";
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
