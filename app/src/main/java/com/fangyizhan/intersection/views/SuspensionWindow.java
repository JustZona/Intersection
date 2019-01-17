package com.fangyizhan.intersection.views;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zy on 2017/7/17.
 * 悬浮窗操作.
 */
public class SuspensionWindow {

    private WindowManager mWindowManager = null;
    private Context mContext = null;
    private View mView = null;
    private SetParams setParams;
    private WindowManager.LayoutParams params;
    private boolean isDisplay = false;


    public SuspensionWindow(Context context){
        init(context);
    }

    private void init(Context context){
        this.mContext = context;
    }

    public void createWindow(){
        createWindow(0,0,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void createWindow(int x,int y,int width,int height){
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题
        params.width = width;
        params.height = height;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.alpha = 1f;
        params.x = x;
        params.y = y;
        if (setParams!=null){
            setParams.setParams(params);
        }
        isDisplay = true;
        //mWindowManager.addView(mView, params);
    }


    /**
     * 设置View.
     * @param layout
     * @return
     */
    public View setView(int layout) {
        View view = LayoutInflater.from(mContext).inflate(layout,
                null);
        this.mView = view;
        return view;
    }

    /**
     * 设置param.
     * @param setParams
     */
    public void setSetParams(SetParams setParams){
        this.setParams = setParams;
    }

    /**
     * 隐藏弹出框
     */
    public void hidePopupWindow() {
        if (null != mView&&mWindowManager!=null) {
            mWindowManager.removeView(mView);
            isDisplay = false;
        }
    }

    /**
     * 显示弹出框
     */
    public void PopupWindow() {
        if (null != mView&&mWindowManager!=null) {
            mWindowManager.addView(mView, params);
            isDisplay = true;
        }
    }

    /**
     * 更新悬浮窗.
     * @param layout
     * @return
     */
    public View UpdateView(int layout){
        if (mWindowManager!=null){
            if (setParams!=null){
                setParams.setParams(params);
            }
            setView(layout);
            mWindowManager.updateViewLayout(mView,params);
            isDisplay = true;
        }
        return mView;
    }

    public interface SetParams {
        public void setParams(WindowManager.LayoutParams params);
    }

    public boolean isDisplay() {
        return isDisplay;
    }
}
