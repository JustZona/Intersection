package com.rent.zona.baselib.event;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class EventBus {
    private final Subject<Event> mSubject;

    public EventBus() {
        this(PublishSubject.<Event>create());
    }

    public EventBus(Subject<Event> subject) {
        this.mSubject = subject;
    }

    public <E extends Event> void post(E event) {
        mSubject.onNext(event);
    }

    public Observable<Event> observe() {
        return mSubject;
    }

    public <E extends Event> Observable<E> observeEvents(Class<E> eventClass) {
        return mSubject.ofType(eventClass);
    }

    private static volatile EventBus sInstance;

    public static EventBus getDefault() {
        if (sInstance == null) {
            synchronized (EventBus.class) {
                if (sInstance == null) {
                    sInstance = new EventBus();
                }
            }
        }
        return sInstance;
    }
}
