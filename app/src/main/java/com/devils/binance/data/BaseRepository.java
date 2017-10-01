package com.devils.binance.data;

import android.support.annotation.NonNull;

import com.devils.binance.net.NetCallback;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.Result;

/**
 * Created by AndyL on 2017/10/1.
 *
 */

public class BaseRepository {

    private final CompositeDisposable compositeDisposable;

    public BaseRepository() {
        this.compositeDisposable = new CompositeDisposable();
    }

    public <T> Disposable invokeRemote(@NonNull Observable<Result<T>> observable, @NonNull final NetCallback<T> netCallback){
        DisposableObserver<T> disposableObserver = new DisposableObserver<T>() {
            @Override
            public void onNext(T response) {
                netCallback.onSuccess(response);
            }

            @Override
            public void onError(Throwable e) {
                netCallback.onError(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
        compositeDisposable.add(observable.flatMap(this::extractResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposableObserver));

        return disposableObserver;
    }

    private  <T> Observable<T> extractResult(Result<T> t){
        if (t.isError()) {
            return Observable.error(new Throwable("网络错误"));
        }else if (t.response() == null) {
            return Observable.error(new Throwable("网络错误"));
        }else if (!t.response().isSuccessful()) {
            return Observable.error(new Throwable("网络错误"));
        }else if (t.response().body() == null){
            return Observable.error(new Throwable("网络错误"));
        }else{
            return Observable.just(t.response().body());
        }
    }

}
