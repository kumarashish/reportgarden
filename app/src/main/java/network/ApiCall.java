package network;


import com.google.gson.Gson;

import interfaces.Repository;
import model.ImportModel;
import model.SearchModel;
import retrofit2.Retrofit;

public class ApiCall {
    Gson gson;
    Retrofit retrofit;
    public ApiCall(Gson gson, Retrofit retrofit)
    {
        this.gson=gson;
        this.retrofit=retrofit;
    }

    public rx.Observable<SearchModel> getData(String value)
    {
        Repository call=  retrofit.create(Repository.class);
     rx.Observable<SearchModel> model= call.getData(value);
     return model;
    }

    public rx.Observable<ImportModel> getImportData(String value)
    {
        Repository call=  retrofit.create(Repository.class);
        rx.Observable<ImportModel> model= call.getImportData(value);

        return model;
    }
}
