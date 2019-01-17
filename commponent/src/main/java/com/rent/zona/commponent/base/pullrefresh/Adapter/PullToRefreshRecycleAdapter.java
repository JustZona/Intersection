package com.rent.zona.commponent.base.pullrefresh.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.rent.zona.commponent.base.pullrefresh.bean.PageListDto;

import java.util.ArrayList;
import java.util.List;

public abstract class PullToRefreshRecycleAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  implements PullToRefreshAdapter<T>{

    protected List<T> mData = null;
    public PullToRefreshRecycleAdapter() {
        this(null);
    }
    public PullToRefreshRecycleAdapter(final List<T> data) {
        mData = data;
    }

    @Override
    public void replaceAllItems(PageListDto<T> listDto) {
        List<T> items = (listDto != null) ? listDto.getDataList() : null;
        replaceAllItems(items);
    }
    public void replaceAllItems(final List<T> items) {
        if (mData == null) {
            mData = new ArrayList<T>();
        } else {
            mData.clear();
        }
        if (items != null) {
            mData.addAll(items);
        }
        notifyDataSetChanged();
    }
    public void remove(T t){
        mData.remove(t);
        notifyDataSetChanged();
    }
    @Override
    public void add(PageListDto<T> listDto) {
        add((listDto != null) ? listDto.getDataList() : null);
    }

    @Override
    public void add(List<T> items) {
        if (items == null) {
            return;
        }

        if (mData == null) {
            mData = new ArrayList<T>();
        }
        mData.addAll(items);

        notifyDataSetChanged();
    }
     public T getItem(int position){
        if(mData!=null && mData.size()>=position){
            return mData.get(position);
        }
        return null;
     }
    @Override
    public boolean isDataEmpty() {
        return getItemCount() == 0;
    }

//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
    @Override
    public int getItemCount() {
        int count = 0;
        if (mData != null) {
            count = mData.size();
        }
        return count;
    }

    @Override
    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }
}
