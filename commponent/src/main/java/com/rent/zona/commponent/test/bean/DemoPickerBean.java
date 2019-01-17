package com.rent.zona.commponent.test.bean;

import com.rent.zona.commponent.pickerwheel.bean.AbstractPickerBean;

public class DemoPickerBean extends AbstractPickerBean {
    public String content;

    public DemoPickerBean(String content) {
        this.content = content;
    }

    @Override
    public String showContent() {
        return content;
    }

    @Override
    public String toString() {
        return "DemoPickerBean{" +
                "content='" + content + '\'' +
                '}';
    }
}
