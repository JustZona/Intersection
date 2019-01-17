package com.rent.zona.baselib.rx;


public interface RxCall<T> {

    T execute() throws Throwable;

    void cancel();

    RxCall<T> clone();
}
