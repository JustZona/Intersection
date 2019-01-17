package com.rent.zona.commponent.popup;

import java.util.ArrayList;

public abstract class ConditionBean {
    protected boolean selected=false;
    protected ArrayList<? super ConditionBean> subitems;
    protected int imgId;

    public abstract String getShowCondition();

    public ArrayList<? super ConditionBean> getSubitems() {
        return subitems;
    }

    public void setSubitems(ArrayList<? super ConditionBean> subitems) {
        this.subitems = subitems;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
