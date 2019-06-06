package common;

import android.app.Application;

import dagger_component.AppComponent;
import dagger_component.AppModule;
import dagger_component.DaggerAppComponent;
import network.ApiCall;
public class AppController extends Application {
    AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
      appComponent= DaggerAppComponent.builder().appModule(new AppModule()).build();


    }

    public ApiCall getWebApi() {
        return appComponent.getAPIService();

    }

}
