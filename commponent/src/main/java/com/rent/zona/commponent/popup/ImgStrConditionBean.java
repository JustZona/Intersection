package com.rent.zona.commponent.popup;

public class ImgStrConditionBean extends ConditionBean{
    private String condition;
    private int imgId;
    public ImgStrConditionBean(int imgId,String condition) {
        this.imgId = imgId;
        this.condition = condition;
    }
    @Override
    public String getShowCondition() {
        return condition;
    }
    @Override
    public int getImgId(){
        return imgId;
    }
}
