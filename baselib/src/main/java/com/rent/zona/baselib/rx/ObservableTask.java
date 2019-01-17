package com.rent.zona.baselib.rx;

public interface ObservableTask<T> {
    T call() throws Throwable;
}
