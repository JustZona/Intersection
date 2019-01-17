package com.rent.zona.baselib.rx;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.exceptions.Exceptions;
public class ObservableHelper {

    /**
     * 普通的任务，不需要取消操作
     */
    public static <T> Observable<T> create(final ObservableTask<T> task) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                if (emitter.isDisposed()) {
                    return;
                }
                try {
                    T ret = task.call();
                    if (!emitter.isDisposed()) {
                        emitter.onNext(ret);
                    }
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    if (!emitter.isDisposed()) {
                       emitter.onError(t);
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    public static <T, P> Observable<T> create(final ObservableTask1<T, P> task, final P obj) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                if (emitter.isDisposed()) {
                    return;
                }
                try {
                    T ret = task.call(obj);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(ret);
                    }
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    if (!emitter.isDisposed()) {
                        emitter.onError(t);
                    }
                    return;
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });

    }

    /**
     * 可取消的任务，在ui中使用sendRequest可以自动管理取消操作
     */
    public static <T> Observable<T> create(RxCall<T> rxCall) {
        RxCallOnSubscribe<T> onSubscribe = new RxCallOnSubscribe<>(rxCall);
        return Observable.create(onSubscribe);
    }
}
