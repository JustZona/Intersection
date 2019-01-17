package com.rent.zona.commponent.popup;

public class StrConditionBean extends ConditionBean{
    private String condition;
    public StrConditionBean(String condition) {

        this.condition = condition;
    }

    @Override
    public String getShowCondition() {
        return condition;
    }

    @Override
    public int getImgId() {
        return 0;
    }
}
