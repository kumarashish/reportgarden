package viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import common.AppController;
import interfaces.OnSubscriberCompleted;
import model.SearchModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Search_ViewModel extends AndroidViewModel {
    SearchModel model;
    AppController controller;
    public Search_ViewModel(@NonNull Application application) {
        super(application);
        controller=(AppController)application;
    }
    public void getSearchData(final OnSubscriberCompleted callback,String value) {
            final Observable<SearchModel> reviewModelObservable = controller.getWebApi().getData(value);
            reviewModelObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SearchModel>() {
                @Override
                public void onCompleted() {

                    callback.onCompleted();
                }
                @Override
                public void onError(Throwable e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onNext(SearchModel searchModel) {
                    model=searchModel;

                }
            });

        }


    public SearchModel getModel() {
        return model;
    }
}
