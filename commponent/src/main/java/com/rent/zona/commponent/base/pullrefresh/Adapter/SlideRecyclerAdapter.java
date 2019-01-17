package com.rent.zona.commponent.base.pullrefresh.Adapter;


import com.rent.zona.commponent.views.SlideLayout;

public interface SlideRecyclerAdapter {
    void holdOpenItem(SlideLayout openItem) ;
    void closeOpenItem();
    void setScrollingItem(SlideLayout scrollingItem);
    SlideLayout getScrollingItem();
}
