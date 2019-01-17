package com.rent.zona.baselib.rx;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.Exceptions;

class RxCallOnSubscribe<T> implements ObservableOnSubscribe<T> {

    final RxCall<T> mRxCall;

    public RxCallOnSubscribe(RxCall<T> rxCall) {
        mRxCall = rxCall;
    }
    @Override
    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
        final RxCall<T> call = mRxCall.clone();
        emitter.setDisposable(Disposables.fromAction(() -> call.cancel()));
        if (emitter.isDisposed()) {
            return;
        }
        try {
            T ret = call.execute();
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



}
