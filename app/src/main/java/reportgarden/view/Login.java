package reportgarden.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import reportgarden.toppackapp.R;


public class Login  extends Activity implements View.OnClickListener {
    @BindView(R.id.txtvw_register)
    TextView register_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        register_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtvw_register:
                startActivity(new Intent(Login.this, Register.class));
                break;
        }

    }
}