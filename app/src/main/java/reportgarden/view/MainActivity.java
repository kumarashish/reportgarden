package reportgarden.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;


import com.google.gson.Gson;

import java.util.ArrayList;

import adapter.PackageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.ResponseBroadcastReceiver;
import common.ToastBroadcastReceiver;
import common.Utils;
import interfaces.OnSubscriberCompleted;
import model.DependencyModel;
import model.SearchModel;
import network.Service;
import okhttp3.internal.Util;
import reportgarden.toppackapp.R;
import viewModel.Import_ViewModel;
import viewModel.Search_ViewModel;

public class MainActivity extends FragmentActivity implements View.OnClickListener, OnSubscriberCompleted {
    @BindView(R.id.listView)
    ListView items_list;
    @BindView(R.id.search_btn)
    Button search_btn;
    @BindView(R.id.search_edt)
    EditText search_edt;
    @BindView(R.id.relative_layout)
    RelativeLayout  rel_lyout;
    @BindView(R.id.progress_bar)
    ProgressBar progress;
    @BindView(R.id.nodata)
    TextView noData;
    Search_ViewModel viewmodelInstance;
    Import_ViewModel import_viewModel;
    ResponseBroadcastReceiver broadcastReceiver;
    ArrayList<DependencyModel>listItems=new ArrayList<>();
    PackageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewmodelInstance = ViewModelProviders.of(this).get(Search_ViewModel.class);
        import_viewModel=ViewModelProviders.of(this).get(Import_ViewModel.class);
        search_btn.setOnClickListener(this);
        broadcastReceiver= new ResponseBroadcastReceiver();
        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction(Service.ACTION);

//        if (import_viewModel.getImportedRepositoryList().size() > 0) {
//            startService();
//            registerReceiver(new ResponseBroadcastReceiver(),intentFilter);
//            items_list.setVisibility(View.VISIBLE);
//            noData.setVisibility(View.GONE);
//            listItems= import_viewModel.getTop10Dependency();
//            adapter=new PackageAdapter(listItems,MainActivity.this);
//            items_list.setAdapter(adapter);
//            }else{
//            items_list.setVisibility(View.GONE);
//            noData.setVisibility(View.VISIBLE);
//            noData.setText("Please Import Some Repository");
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Service.ACTION);
        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public void updateUI()

    {   rel_lyout.setVisibility(View.VISIBLE);
        SearchModel model=viewmodelInstance.getModel();
        Intent intent = new Intent(MainActivity.this,SearchResult.class);
        intent.putExtra("resultdata", new Gson().toJson(model));
        startActivityForResult(intent,2);
        progress.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listItems.clear();
//        listItems= import_viewModel.getTop10Dependency();
//            adapter=new PackageAdapter(listItems,MainActivity.this);
//            items_list.setAdapter(adapter);
            if(listItems.size()>0)
            {
                items_list.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
            }

    }

    @Override
    public void onCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });

    }
    public  void hideKeyboardFrom( View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    @Override
    public void onError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (search_edt.getText().length() > 0) {
            if (Utils.isNetworkAvailable(MainActivity.this)) {
                progress.setVisibility(View.VISIBLE);
                viewmodelInstance.getSearchData(this, search_edt.getText().toString());
                rel_lyout.setVisibility(View.GONE);
                hideKeyboardFrom(v);
            }

        } else {
            Toast.makeText(MainActivity.this, "Please enter text to be searched", Toast.LENGTH_SHORT).show();
        }
    }
    public void startService()
    {
        Intent toastIntent= new Intent(getApplicationContext(), ToastBroadcastReceiver.class);
        PendingIntent toastAlarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, toastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        long startTime=System.currentTimeMillis(); //alarm starts immediately
        AlarmManager backupAlarmMgr=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        backupAlarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,5*60*1000,toastAlarmIntent); // alarm will repeat after every 5 minutes
    }
}
