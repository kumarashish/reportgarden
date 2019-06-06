package network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import room.Data;
import room.Database;
import room.Dependencies;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Service extends IntentService {
    public static final String ACTION="ReportGarden.Receivers.ResponseBroadcastReceiver";
    AppController controller;
    Database db ;
    Context mContext ;
    // Must create a default constructor
    public  Service() {
        // Used to name the worker thread, important only for debugging.

        super("backgroundService");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        controller=(AppController)mContext;
        db = Room.databaseBuilder(mContext, Database .class, "db").allowMainThreadQueries().build();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // This describes what will happen when service is triggered
        Log.i("backgroundService", "updating repository");
        //create a broadcast to send the toast message
        List<Data> list = db.Dao().getImportedRepository();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(Utils.isNetworkAvailable(getApplicationContext())) {
                    getImportData(list.get(i).getFullName());
                }
            }
        }
    }


    public void getImportData( String value) {

        final Observable<ImportModel> reviewModelObservable = controller.getWebApi().getImportData(value);
        reviewModelObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<ImportModel>() {
            @Override
            public void onCompleted() {
                Intent toastIntent = new Intent(ACTION);
                toastIntent.putExtra("toastMessage", "Repository Updated");
                sendBroadcast(toastIntent);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError: " + e.getMessage());
            }
            @Override
            public void onNext(ImportModel im_model) {
                ImportModel  model = im_model;
                String val = Utils.getDecodedBase64(model.getContent());
                handleJson(val,value);
                Log.d("TAG", "updateDatabase: " + val);
            }
        });

    }

    public void handleJson(String json,String userName) {
        ArrayList<String> devdepkeys = new ArrayList<>();
        ArrayList<String> dependencieskeys = new ArrayList<>();
        Data model = db.Dao().getImportedRepository(userName);
        boolean isDependencyAdded=false;
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
                        isDependencyAdded=true;
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
                        isDependencyAdded=true;
                    }
                }
                List<Dependencies> dependencies = db.Dao().getDependency(model.getId(), Constant.dependency);
                /************************************removing removed dependency********************/
                removeDependency(dependencies, dependencieskeys, model.getId(), Constant.dependency);
            }
            db.Dao().updateLastSyncTime(Utils.getCurrentDateTime(),model.getId());
            List<Data> data=db.Dao().getImportedRepository();
            Log.d("TAG", "after sync: "+data);
            if(isDependencyAdded)
            {
                Toast.makeText(getApplication(), "New Dependency has been added", Toast.LENGTH_SHORT).show();
            }
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
}
