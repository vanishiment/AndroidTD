package com.zhangyn.androidtd;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

public class RxJavaT {

  public static void main(String[] args){

    final CompositeDisposable compositeDisposable = new CompositeDisposable();

    Observer<String> observer = new Observer<String>() {
      @Override public void onSubscribe(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
      }

      @Override public void onNext(@NonNull String s) {
        System.out.println("onNext:" + s);
      }

      @Override public void onError(@NonNull Throwable throwable) {
        System.out.println("onError:" + throwable.getLocalizedMessage());
      }

      @Override public void onComplete() {
        System.out.println("onComplete");
      }
    };

    Observer<Integer> observerInt = new Observer<Integer>() {
      @Override public void onSubscribe(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
      }

      @Override public void onNext(@NonNull Integer s) {
        System.out.println("onNext:" + s);
      }

      @Override public void onError(@NonNull Throwable throwable) {
        System.out.println("onError:" + throwable.getLocalizedMessage());
      }

      @Override public void onComplete() {
        System.out.println("onComplete");
      }
    };

    Observable.create(new ObservableOnSubscribe<String>() {
      @Override public void subscribe(@NonNull ObservableEmitter<String> observableEmitter)
          throws Exception {
        observableEmitter.onNext("1");
        observableEmitter.onNext("2");
        observableEmitter.onNext("3");
        observableEmitter.tryOnError(new Throwable("have a try error."));
        observableEmitter.onComplete();
      }
    }).subscribe(observer);



    Observable.range(9,7).subscribe(observerInt);

    Observable.just("1","1","2","3","1","2").distinct(new Function<String, String>() {
      @Override public String apply(@NonNull String s) throws Exception {
        return s;
      }
    }).subscribe(observer);

    Observable.range(0,7).filter(new Predicate<Integer>() {
      @Override public boolean test(@NonNull Integer integer) throws Exception {
        return integer % 2 == 0;
      }
    }).subscribe(observerInt);

    Observable.just(0,"Alice",4,"Don").ofType(String.class).subscribe(observer);

    Observable.just("Alice").repeat(3).subscribe(observer);

    Observable.just("Alice","Bob","Cell","Dav","Echo","Fish").map(new Function<String, String>() {
      @Override public String apply(@NonNull String s) throws Exception {
        return s + " eat...";
      }
    }).subscribe(observer);

    Observable.just(1,2,3,4,5,6).flatMap(new Function<Integer, ObservableSource<Integer>>() {
      @Override public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {

        return Observable.merge(Observable.range(integer * 10,2),Observable.just(integer));
      }
    }).subscribe(observerInt);

    Observable.just(1,2,3,4,5,6).concatMap(new Function<Integer, ObservableSource<Integer>>() {
      @Override public ObservableSource<Integer> apply(@NonNull Integer integer) throws Exception {
        return Observable.merge(Observable.range(integer * 10,2),Observable.just(integer));
      }
    }).subscribe(observerInt);

    Observable<Long> o1 = Observable
        .interval(1000, TimeUnit.MILLISECONDS);
    o1.doOnNext(new Consumer<Long>() {
      @Override public void accept(Long aLong) throws Exception {
        System.out.println("o1===>"+aLong);
      }
    });
    Observable<Long> o2 = Observable
        .interval(200, TimeUnit.MILLISECONDS)
        .take(4)
        .subscribeOn(Schedulers.newThread());

    o2.doOnNext(new Consumer<Long>() {
      @Override public void accept(Long aLong) throws Exception {
        System.out.println("o2===>"+aLong);
      }
    });

    Observable.zip(o1, o2, new BiFunction<Long, Long, Long>() {
      @Override public Long apply(@NonNull Long aLong, @NonNull Long aLong2) throws Exception {
        System.out.println("aLong:" + aLong + "\t aLong2:" + aLong2+"\t");
        return aLong + aLong2;
      }
    }).subscribe(new Observer<Long>() {
      @Override public void onSubscribe(@NonNull Disposable disposable) {
        compositeDisposable.add(disposable);
      }

      @Override public void onNext(@NonNull Long aLong) {
        System.out.println("result: " + aLong);
      }

      @Override public void onError(@NonNull Throwable throwable) {
        System.out.println("result: " + throwable.getLocalizedMessage());
      }

      @Override public void onComplete() {
        System.out.println("zip onComplete ");
      }
    });

    Observable.intervalRange(1000,2,1000,1000,TimeUnit.MILLISECONDS,Schedulers.newThread()).doOnNext(new Consumer<Long>() {
      @Override public void accept(Long aLong) throws Exception {
        System.out.println("result: " + aLong);
      }
    });

    Observable.timer(1000,TimeUnit.MILLISECONDS,Schedulers.newThread()).doOnNext(new Consumer<Long>() {
      @Override public void accept(Long aLong) throws Exception {
        System.out.println("result: " + aLong);
      }
    }).doOnError(new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        System.out.println("result: " + throwable.getLocalizedMessage());
      }
    });

    //compositeDisposable.clear();

    new Thread(new Runnable() {
      @Override public void run() {
        try {
          Thread.sleep(20000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

}
