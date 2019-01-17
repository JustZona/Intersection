package com.rent.zona.baselib.network;


import com.google.gson.GsonBuilder;
import com.rent.zona.baselib.event.Event;
import com.rent.zona.baselib.event.EventBus;
import com.rent.zona.baselib.log.LibLogger;
import com.rent.zona.baselib.network.httpbean.TResponse;
import com.rent.zona.baselib.network.httpbean.TaskException;
import com.rent.zona.baselib.rx.SchedulersHelper;
import com.rent.zona.baselib.utils.GenericsUtils;
import com.rent.zona.baselib.utils.GsonFactory;

import org.reactivestreams.Subscription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class RXHelper {
    private CompositeDisposable mTaskDisposable;
    private CompositeDisposable mEventDisposable;
    private void addTaskSubscription(Disposable d) {
        if(mTaskDisposable==null){
            mTaskDisposable=new CompositeDisposable();
        }
        mTaskDisposable.add(d);
    }

    private void removeTaskSubscription(Disposable d) {
        if(mTaskDisposable!=null){
            mTaskDisposable.remove(d);
        }
    }

    public void unsubscribe() {
        unsubscribeTask();
        unsubscribeEvent();
    }

    public void unsubscribeTask() {
        if(mTaskDisposable!=null){
            mTaskDisposable.dispose();
            mTaskDisposable=null;
        }
    }

    public <T> Disposable execute(Observable<TResponse<T>> observable,
                                  final Consumer<TResponse<? super T>> onNext,
                                  final Consumer<Throwable> onError) {
        Disposable disposable = observable
                .flatMap(response -> {
                    if (response.isSuccess()) {
                        return Observable.just(response);
                    } else {
                        return Observable.error(new TaskException(0, response.msg));
                    }
                })
                .subscribeOn(SchedulersHelper.background())
                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(onNext,onError,()->{onNext.accept(null);});
                .subscribe(onNext, onError);


        addTaskSubscription(disposable);
        return disposable;
    }

    public <T> Disposable executeUITask(Observable<T> observable,
                                          final Consumer<? super T> onNext,
                                          final Consumer<Throwable> onError) {
        Disposable disposable = observable
                .subscribeOn(SchedulersHelper.ui())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
        addTaskSubscription(disposable);
        return disposable;
    }


    public <T> Disposable executeBkgTask(Observable<T> observable,
                                           final Consumer<? super T> onNext,
                                           final Consumer<Throwable> onError) {
        Disposable disposable = observable
                .subscribeOn(SchedulersHelper.background())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
        addTaskSubscription(disposable);
        return disposable;
    }


    private void addEventSubscription(Disposable d) {
        if (mEventDisposable == null) {
            mEventDisposable = new CompositeDisposable();
        }
        mEventDisposable.add(d);
    }

    private void removeEventSubscription(Disposable d) {
        if (mEventDisposable != null) {
            mEventDisposable.remove(d);
        }
    }

    public void unsubscribeEvent() {
        if (mEventDisposable != null) {
            mEventDisposable.dispose();
            mEventDisposable = null;
        }
    }

    public <E extends Event> Disposable observeEvent(
            Class<E> eventClass,
            final Consumer<? super E> onEvent,
            Scheduler scheduler) {
        Disposable disposable = EventBus.getDefault().observeEvents(eventClass)
                .observeOn(scheduler).subscribe(onEvent);
        addEventSubscription(disposable);
        return disposable;
    }

    public void unsubscribeEvent(Disposable d) {
        removeEventSubscription(d);
    }
}
