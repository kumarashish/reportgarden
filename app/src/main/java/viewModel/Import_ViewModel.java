package viewModel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.Room;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import common.AppController;
import common.Constant;
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
    boolean isDataLoading = false;
    String userName;

    public Import_ViewModel(@NonNull Application application) {
        super(application);
        controller = (AppController) application;
        db = Room.databaseBuilder(application.getApplicationContext(), Database.class, "db").allowMainThreadQueries().build();
    }

    public void getImportData(final OnSubscriberCompleted callback, String value) {
        userName = value;
        final Observable<ImportModel> reviewModelObservable = controller.getWebApi().getImportData(value);
        reviewModelObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ImportModel>() {
            @Override
            public void onCompleted() {

                callback.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError: " + e.getMessage());
                callback.onError(e.getMessage());
            }
            @Override
            public void onNext(ImportModel im_model) {
                model = im_model;
                String val = Utils.getDecodedBase64(model.getContent());
                handleJson(val);
                Log.d("TAG", "updateDatabase: " + val);
            }
        });

    }

    public void handleJson(String json) {
        ArrayList<String> devdepkeys = new ArrayList<>();
        ArrayList<String> dependencieskeys = new ArrayList<>();
        Data model = db.Dao().getImportedRepository(userName);
        if (model == null) {
            db.Dao().addRepo(new Data(userName, Utils.getCurrentDateTime()));
        }
        try {
            JSONObject jsonObject = new JSONObject(json);

            /************************************devDependencies********************/
            if (!jsonObject.isNull("devDependencies")) {
                JSONObject jsonObj = jsonObject.getJSONObject("devDependencies");
                Iterator<String> iterator = jsonObj.keys();
                do {
                    String key = iterator.next();
                    devdepkeys.add(key);
                } while (iterator.hasNext());

                Log.d("TAG", "devdependencies: " + devdepkeys);
                /************************************adding new dependency to Repo********************/
                for (int i = 0; i < devdepkeys.size(); i++) {
                    if (db.Dao().getDependency(devdepkeys.get(i), Constant.devDependency) == null) {
                        db.Dao().addDependency(new Dependencies(devdepkeys.get(i), model.getId(), Constant.devDependency));
                       // Toast.makeText(getApplication(), "New Dependency : " + devdepkeys.get(i) + "has been added", Toast.LENGTH_SHORT).show();
                    }
                }

                List<Dependencies> dependencies = db.Dao().getDependency(model.getId(), Constant.devDependency);
                /************************************removing removed dependency********************/
                removeDependency(dependencies, devdepkeys, model.getId(), Constant.devDependency);
            }

            /************************************dependencies********************/
            if (!jsonObject.isNull("dependencies")) {
                JSONObject jsonObj = jsonObject.getJSONObject("dependencies");
                Iterator<String> iterator = jsonObj.keys();
                do {
                    String key = iterator.next();
                    dependencieskeys.add(key);

                } while (iterator.hasNext());

                Log.d("TAG", "updateDatabase: " + dependencieskeys);
                for (int i = 0; i < dependencieskeys.size(); i++) {
                    if (db.Dao().getDependency(dependencieskeys.get(i), Constant.dependency) == null) {
                        db.Dao().addDependency(new Dependencies(dependencieskeys.get(i), model.getId(), Constant.dependency));
                   //     Toast.makeText(getApplication(), "New Dependency : " + dependencieskeys.get(i) + "has been added", Toast.LENGTH_SHORT).show();
                    }
                }
                List<Dependencies> dependencies = db.Dao().getDependency(model.getId(), Constant.dependency);
                /************************************removing removed dependency********************/
                removeDependency(dependencies, dependencieskeys, model.getId(), Constant.dependency);
            }
                db.Dao().updateLastSyncTime(Utils.getCurrentDateTime(),model.getId());
        } catch (Exception ex) {
            ex.fillInStackTrace();
        }
    }
    /************************************removing removed dependency********************/
    public void removeDependency(List<Dependencies> olddependencies, ArrayList<String> newDependencies, int repoId,int type) {
        for (int i = 0; i < olddependencies.size(); i++) {
            String olddependencyName = olddependencies.get(i).getDependency();
            if (!newDependencies.contains(olddependencyName)) {
                db.Dao().deleteDependency(olddependencyName, repoId,type);
                Log.d("TAG", "removedDependency: " + olddependencyName);
                Toast.makeText(getApplication(),olddependencyName+"has been removed",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public ImportModel getModel() {
        return model;
    }
    /************************************getString arraylist of dependency********************/
    public ArrayList<String> getImportedRepositoryList() {
        List<Data> list = db.Dao().getImportedRepository();
        ArrayList<String> repoNameList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            repoNameList.add(list.get(i).getFullName());
        }
        return repoNameList;
    }
    /************************************getString arraylist of dependency********************/
    public    List<Dependencies> getImportedDependencyList() {
        List<Dependencies> list = db.Dao().getImportedDependency();

        return list;
    }


}
