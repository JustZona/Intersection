package com.rent.zona.commponent.test.event;

import com.rent.zona.baselib.event.Event;

public class AEvent implements Event {
    public String nameA;

    public AEvent(String nameA) {
        this.nameA = nameA;
    }

    public String getNameA() {
        return nameA;
    }
}
