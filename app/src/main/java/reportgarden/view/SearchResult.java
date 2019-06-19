package reportgarden.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.util.List;

import adapter.ListItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.ToastBroadcastReceiver;
import common.Utils;
import interfaces.OnImportClick;
import interfaces.OnSubscriberCompleted;
import model.ImportModel;
import model.SearchModel;
import reportgarden.toppackapp.R;
import viewModel.Import_ViewModel;
import viewModel.Search_ViewModel;

public class SearchResult  extends FragmentActivity implements OnImportClick, OnSubscriberCompleted {
    SearchModel model;
    @BindView(R.id.nodata)
    TextView nodataVew;
    @BindView(R.id.listView)
    com.twotoasters.jazzylistview.JazzyListView list;
    Import_ViewModel import_viewModelInstance;
    ProgressDialog pd ;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_details);
        ButterKnife.bind(this);
        model= new Gson().fromJson(getIntent().getStringExtra("resultdata"),SearchModel.class);
        import_viewModelInstance = ViewModelProviders.of(this).get(Import_ViewModel.class);
        Log.d("TAG", "onCreate: "+model);
        pd= new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("loading");
        updateUI();
    }

    public void updateUI() {
//        if (model.getTotalCount() > 0) {
//            list.setAdapter(new ListItemAdapter(model.getItems(), SearchResult.this, import_viewModelInstance.getImportedRepositoryList()));
//            list.setTransitionEffect(new SlideInEffect());
//            nodataVew.setVisibility(View.GONE);
//            list.setVisibility(View.VISIBLE);
//        } else {
//            nodataVew.setVisibility(View.VISIBLE);
//            list.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onClick(final String fullName) {
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        pd.show();
        import_viewModelInstance.getImportData(SearchResult.this,fullName);
    }
});

    }


    public void updateDatabase()
    {
        pd.cancel();
        Toast.makeText(SearchResult.this,"Repository Imported Sucessfully",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateDatabase();

            }
        });

    }

    @Override
    public void onError(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               pd.cancel();
                Toast.makeText(SearchResult.this,"package.json not present ! Details : "+message,Toast.LENGTH_SHORT).show();
            }
        });
    }


}
