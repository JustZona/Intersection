package com.rent.zona.baselib.rx;


public interface ObservableTask1<T, P> {
    T call(P obj) throws Throwable;
}
