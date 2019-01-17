package com.rent.zona.baselib.event;


public class AppLifeCycleEvent implements Event {
    public static final int EVENT_ENTER_FOREGROUND = 0;
    public static final int EVENT_ENTER_BACKGROUND = 1;

    public int event;

    public AppLifeCycleEvent(int e) {
        event = e;
    }
}
