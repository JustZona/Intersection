package com.rent.zona.commponent.base.pullrefresh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rent.zona.commponent.R;
import com.rent.zona.commponent.utils.DensityUtils;
import com.rent.zona.commponent.views.SlideLayout;

public abstract class SlidePullToRefreshRecyclerAdaper<T> extends PullToRefreshRecycleAdapter<T> implements SlideRecyclerAdapter{
    //侧滑相关
    private SlideLayout mOpenItem;
    private SlideLayout mScrollingItem;
    public SlideLayout getScrollingItem() {
        return mScrollingItem;
    }

    public void setScrollingItem(SlideLayout scrollingItem) {
        mScrollingItem = scrollingItem;
    }

    public void holdOpenItem(SlideLayout openItem) {
        mOpenItem = openItem;
    }

    public void closeOpenItem() {
        if (mOpenItem != null && mOpenItem.isOpen()) {
            mOpenItem.close();
            mOpenItem = null;
        }
    }
    public  abstract class SlideViewHolder extends RecyclerView.ViewHolder{
       public ViewGroup leftMenuView,rightMenuView,contentView;
        public SlideLayout slideLayout;
        public SlideViewHolder(Context context,ViewGroup recyclerView,int contentViewRes,int rightMenuRes){
            this(context,recyclerView,contentViewRes,-1,rightMenuRes);
        }
        public SlideViewHolder(Context context,ViewGroup recyclerView,int contentViewRes,int leftMenuRes,int rightMenuRes){
           super(LayoutInflater.from(context).inflate(R.layout.item_recyclerview_slide,recyclerView,false));
           leftMenuView=itemView.findViewById(R.id.left_menu_view);
           rightMenuView=itemView.findViewById(R.id.right_menu_view);
           contentView=itemView.findViewById(R.id.content_view);
            slideLayout=itemView.findViewById(R.id.yhaolpz_slideLayout);
           if(leftMenuRes>0){
               leftMenuView.addView(LayoutInflater.from(context).inflate(leftMenuRes,leftMenuView,false));
           }
            if(rightMenuRes>0){
                rightMenuView.addView(LayoutInflater.from(context).inflate(rightMenuRes,rightMenuView,false));
            }
            ViewGroup tempContentView= (ViewGroup) LayoutInflater.from(context).inflate(contentViewRes,contentView,false);
            contentView.addView(tempContentView);
            initView();
           itemView.post(new Runnable() {
               @Override
               public void run() {
                   LinearLayout.LayoutParams contentParams= (LinearLayout.LayoutParams) contentView.getLayoutParams();
                   contentParams.width= DensityUtils.getScreenWidth(context);
                   contentView.setLayoutParams(contentParams);
                   slideLayout.setLeftMenuWidth(leftMenuView.getWidth());
                   slideLayout.setRightMenuWidth(rightMenuView.getWidth());
               }
           });

        }
        public abstract void initView();
    }
}
