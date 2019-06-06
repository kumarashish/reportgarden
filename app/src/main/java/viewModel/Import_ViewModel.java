package viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import common.AppController;
import common.Utils;
import interfaces.OnSubscriberCompleted;
import model.ImportModel;
import model.SearchModel;
import room.Data;
import room.Database;
import room.Dependencies;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Import_ViewModel extends AndroidViewModel {
    ImportModel model;
    AppController controller;
    Database db;
    boolean isDataLoading=false;
    String userName;
    public Import_ViewModel(@NonNull Application application) {
        super(application);
        controller = (AppController) application;
       db= Room.databaseBuilder(application.getApplicationContext(),Database.class,"db").allowMainThreadQueries().build();
    }

    public void getImportData(final OnSubscriberCompleted callback, String value) {
        userName=value;
        final Observable<ImportModel> reviewModelObservable = controller.getWebApi().getImportData(value);
        reviewModelObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ImportModel>() {
            @Override
            public void onCompleted() {

                callback.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError: "+e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onNext(ImportModel im_model) {
                model = im_model;
                String val= Utils.getDecodedBase64(model.getContent());
                handleJson(val);

                Log.d("TAG", "updateDatabase: "+val );

                //db.Dao().addResult();

            }
        });

    }

    public void handleJson(String json) {
        ArrayList<String> devdepkeys=new ArrayList<>();
        ArrayList<String> dependencieskeys=new ArrayList<>();
        Data model=db.Dao().getImportedRepository(userName);
        if(model==null)
        {
            db.Dao().addRepo(new Data(userName,Utils.getCurrentDateTime()));
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(!jsonObject.isNull("devDependencies"))
            {
                JSONObject jsonObj=jsonObject.getJSONObject("devDependencies");

                Iterator<String> iterator = jsonObj.keys();
             do  {
                    String key = iterator.next();
                 devdepkeys.add(key);

                }  while (iterator.hasNext());

                Log.d("TAG", "updateDatabase: "+devdepkeys );

                for(int i=0;i<devdepkeys.size();i++)
                {
                    db.Dao().addDependency(new Dependencies(devdepkeys.get(i),model.getId()));
                }
          List<Dependencies> dependencies=db.Dao().getDependency(model.getId());
                Log.d("TAG", "updateDatabase: "+dependencies );

            }


            if(!jsonObject.isNull("dependencies"))
            {
             JSONObject jsonObj=jsonObject.getJSONObject("dependencies");
                Iterator<String> iterator = jsonObj.keys();
                do  {
                    String key = iterator.next();
                    dependencieskeys.add(key);

                }  while (iterator.hasNext());

                Log.d("TAG", "updateDatabase: "+dependencieskeys );


            }



        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
    public ImportModel getModel() {
        return model;
    }

    public ArrayList<String> getImportedRepositoryList()
    {List<Data> list=db.Dao().getImportedRepository();
    ArrayList<String>repoNameList=new ArrayList<>();
    for(int i=0;i<list.size();i++)
    {
        repoNameList.add(list.get(i).getFullName());
    }
        return repoNameList;
    }
}
