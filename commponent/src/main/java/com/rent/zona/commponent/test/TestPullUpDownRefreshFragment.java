package com.rent.zona.commponent.test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.commponent.R;
import com.rent.zona.commponent.base.CommonFragmentActivity;
import com.rent.zona.commponent.base.pullrefresh.Adapter.PullToRefreshRecycleAdapter;
import com.rent.zona.commponent.base.pullrefresh.RefreshRecycleViewFragment;
import com.rent.zona.commponent.base.pullrefresh.bean.PageListDto;

import java.util.ArrayList;

public class TestPullUpDownRefreshFragment extends RefreshRecycleViewFragment{
    public static void launch(Context context) {
//        Bundle fragParams = new Bundle();
//        fragParams.putParcelable(EXTRA_DATA_USER, loginBean);
        Intent intent = CommonFragmentActivity.createIntent(
                context,
                null,
                null,
                TestPullUpDownRefreshFragment.class);
        context.startActivity(intent);
    }
    @Override
    protected PullToRefreshRecycleAdapter onCreateAdapter(Context context) {
        return new TestAdapter();
    }

    @Override
    protected void refresh(int pageSize) {
        loadMore(0,pageSize);
    }

    int index=-1;
    @Override
    protected void loadMore( int pageIndex, int pageSize) {
        index++;
        mSmartRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                PageListDto<String> listDto =obtainPageListDto(28);
                ArrayList<String> list = new ArrayList<String>();
                list.add("dfsdf");
                list.add("高大哥大哥");
                list.add("干豆腐干豆腐");
                list.add("和规范化");
                list.add("返回");
                listDto.dataList=list;
                if(index%7==0){
                    onLoadFailed(new TaskException(1,"没有数据了"));
                    getActivityHelper().toast(index+"失败",getActivity());
                }else
                if(index%4==0){
                    onLoadFailed(new Exception());
                    getActivityHelper().toast(index+"失败",getActivity());
                }else {
                    onLoadSuccess(listDto);
                    getActivityHelper().toast(index+"成功",getActivity());
                }
            }
        }, 2000);
    }

    @Override
    protected RecyclerView.LayoutManager forLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    protected RecyclerView.ItemDecoration forItemDecoration() {
        return null;
    }

    private class TestAdapter extends PullToRefreshRecycleAdapter<String>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(View.inflate(getActivity(), R.layout.test_item_refresh,null));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((MyViewHolder)holder).bind(position);
        }
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView textView;
            int position;
            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                textView=itemView.findViewById(R.id.showtext);
            }
            public void bind(int position){
                this.position=position;
                textView.setText(getItem(position));
            }
            @Override
            public void onClick(View v) {
                getActivityHelper().toast(""+position,getActivity());
            }
        }
    }

}
