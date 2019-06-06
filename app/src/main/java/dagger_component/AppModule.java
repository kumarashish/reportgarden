package dagger_component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import common.Constant;
import dagger.Module;
import dagger.Provides;
import network.ApiCall;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
@Singleton
Gson gson;

@Singleton
Retrofit retrofit;
@Provides
    public Gson getGson()
{
     gson=new GsonBuilder().setLenient().create();
    return gson;
}
@Provides
   public Retrofit getRetrofit()
{
 retrofit=new Retrofit.Builder().baseUrl(Constant.BaseUrl).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build();
return retrofit;
}
@Provides

    public ApiCall provideApiCallService(Gson gson, Retrofit retrofit)
{
    return new ApiCall(gson,retrofit);
}




}
