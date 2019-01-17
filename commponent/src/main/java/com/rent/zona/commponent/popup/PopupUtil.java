package com.rent.zona.commponent.popup;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.rent.zona.commponent.utils.DensityUtils;


/**
 * Created by Administrator on 2018/2/22.
 */

public class PopupUtil {
    public static void showPopup(Context context, PopupWindow popup, View anchor){
        showPopup(context,popup,anchor,0,0);
    }
    public static void showPopup(Context context, PopupWindow popup, View anchor, int xoff, int yoff){
        if (Build.VERSION.SDK_INT < 24) {
            popup.showAsDropDown(anchor,xoff,yoff);
        }else if(Build.VERSION.SDK_INT == 25 && popup.getHeight()== ViewGroup.LayoutParams.MATCH_PARENT){
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            popup.setHeight(DensityUtils.getScreenHeight(context)-a[1]-anchor.getHeight());
            popup.showAtLocation(anchor, Gravity.NO_GRAVITY,xoff+a[0], anchor.getHeight()+a[1]+yoff);
            popup.update();
        } else {
            int[] a = new int[2];
            anchor.getLocationInWindow(a);
            popup.showAtLocation(anchor, Gravity.NO_GRAVITY,xoff+a[0], anchor.getHeight()+a[1]+yoff);
            popup.update();
        }
    }
}
