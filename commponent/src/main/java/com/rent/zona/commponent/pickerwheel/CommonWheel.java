package com.rent.zona.commponent.pickerwheel;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rent.zona.commponent.pickerwheel.adapters.ArrayWheelAdapter;
import com.rent.zona.commponent.pickerwheel.bean.AbstractPickerBean;
import com.rent.zona.commponent.pickerwheel.config.PickerConfig;
import com.rent.zona.commponent.pickerwheel.wheel.OnWheelChangedListener;
import com.rent.zona.commponent.pickerwheel.wheel.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonWheel<T extends AbstractPickerBean> {
    public interface OnUpdateNextDataListener<T extends AbstractPickerBean> {
        /**
         * 当前的数据源
         *
         * @param nowData
         * @return
         */
        List<T> updateNextData(T changeT, List<T> nowData);
    }

    Context mContext;
    private List<WheelView> mWheelViews;
    private Map<WheelView, OnWheelChangedListener> mWheelChangeListeners;

    PickerConfig mPickerConfig;

    private ViewGroup mWheelRootView;

    private OnUpdateNextDataListener<T> mUpdateListener;

    public CommonWheel(ViewGroup view, PickerConfig pickerConfig) {
        this.mWheelRootView = view;
        mPickerConfig = pickerConfig;
        mWheelViews = new ArrayList<>();
        mWheelChangeListeners = new HashMap<>();

        mContext = view.getContext();
    }

    public void setDataSource(List<T>... dataSources) {
        initWheelViews(dataSources);
    }

    public void setOnUpdateNextDataListener(OnUpdateNextDataListener listener) {
        this.mUpdateListener = listener;
    }

    private void initWheelViews(List<T>... dataSources) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.WHITE);

        for (final List<T> dataSource : dataSources) {
            WheelView wheelView = new WheelView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            linearLayout.addView(wheelView, lp);
            final OnWheelChangedListener listener = new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    int index = mWheelViews.indexOf(wheel);
                    if (index == mWheelViews.size() - 1) {

                    } else {
                        for (int i = index + 1; i < mWheelViews.size(); i++) {
                            if (mUpdateListener != null) {
                                WheelView view = mWheelViews.get(i);
//                                WheelView viewP = mWheelViews.get(i-1);
//                                Object[] objs = ((ArrayWheelAdapter) view.getViewAdapter()).getDataSource();
//                                List<T> list = new ArrayList<>();
//                                for (Object obj : objs) {
//                                    list.add((T) obj);
//                                }

                                ArrayWheelAdapter lastAdapter = (ArrayWheelAdapter) mWheelViews.get(i - 1).getViewAdapter();
                                T t = null;
                                if (lastAdapter.getDataSource() != null && lastAdapter.getDataSource().length > 0) {

                                    t = (T) lastAdapter.getDataSource()[mWheelViews.get(i - 1).getCurrentItem() % lastAdapter.getDataSource().length];
                                }

                                List<T> data = mUpdateListener.updateNextData(t, dataSource);
                                if (data == null) {
                                    return;
                                }
                                ArrayWheelAdapter newAdapter = new ArrayWheelAdapter(mContext, data.toArray());
                                view.setViewAdapter(newAdapter);
                                view.setCurrentItem(0);
                            }
                        }
                    }
                }
            };
            wheelView.addChangingListener(listener);
            ArrayWheelAdapter adapter = new ArrayWheelAdapter(mContext, dataSource.toArray());
            wheelView.setViewAdapter(adapter);
            mWheelChangeListeners.put(wheelView, listener);
            mWheelViews.add(wheelView);
        }

        mWheelRootView.addView(linearLayout);
    }

    /**
     * 选取的结果,依次是每一列的结果
     *
     * @return
     */
    public List<Object> getCurrentPickResult() {
        List<Object> results = new ArrayList<>();
        for (WheelView mWheelView : mWheelViews) {
//            results.add((T) ((ArrayWheelAdapter) mWheelView.getViewAdapter()).getDataSource()[mWheelView.getCurrentItem()]);
//            results.add(((ArrayWheelAdapter) mWheelView.getViewAdapter()).getDataSource()[mWheelView.getCurrentItem()]);
            Object[] datas=((ArrayWheelAdapter) mWheelView.getViewAdapter()).getDataSource();
            if(datas!=null && datas.length>mWheelView.getCurrentItem()){
                results.add(datas[mWheelView.getCurrentItem()]);
            }else{
                results.add(null);
            }
        }
        return results;
    }
}
