package com.rent.zona.commponent.base.pullrefresh.Adapter;

import com.rent.zona.commponent.base.pullrefresh.bean.PageListDto;

import java.util.List;

public interface PullToRefreshAdapter<T>{
    void replaceAllItems(PageListDto<T> listDto);

    void clear();

    void add(PageListDto<T> listDto);

    void add(List<T> listData);

    boolean isDataEmpty();
}
