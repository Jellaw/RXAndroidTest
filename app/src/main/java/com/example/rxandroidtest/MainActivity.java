package com.example.rxandroidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create observable
        Observable<String> footballPlayerObservable = Observable.just("Messi", "Ronaldo", "Modric", "Salah", "Mbappe");

        //obsever
        //Observer<String> footballPlayerObserver = getFootballPlayerObserver();
       DisposableObserver<String> mPlayer = getMFootballPlayerObserver();
       DisposableObserver<String> rPlayer = getRFootballPlayerObserver();

        //observer subscribing to observable with filter operator <loc ten cau thu bat dau bang "m">
        compositeDisposable.add(
        footballPlayerObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("m");
                    }
                })
                .subscribeWith(mPlayer));
        compositeDisposable.add(
                footballPlayerObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return s.toLowerCase().startsWith("r");
                    }
                }).subscribeWith(rPlayer));
    }
    private DisposableObserver<String> getMFootballPlayerObserver(){
            return new DisposableObserver<String>() {

                @Override
                public void onNext(@NonNull String s) {
                    Log.d("TaG","Name: "+s);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.d("TaG","onError: "+ e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.d("TaG","All");
                }
            };
    }
    private DisposableObserver<String> getRFootballPlayerObserver(){
        return new DisposableObserver<String>() {

            @Override
            public void onNext(@NonNull String s) {
                Log.d("TaG","Name: "+s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d("TaG","onError: "+ e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d("TaG","All");
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}