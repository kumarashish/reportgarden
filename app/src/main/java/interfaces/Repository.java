package interfaces;

import model.ImportModel;
import model.SearchModel;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface Repository {
    @GET("search/repositories?")
    Observable<SearchModel> getData(@Query(("q") ) String apiKey);
    @GET("repos/{fullname}/contents/package.json")
    Observable<ImportModel> getImportData(@Path(value = "fullname", encoded = true) String name );



}
