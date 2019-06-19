package reportgarden.view;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;
import reportgarden.toppackapp.R;

public class Register extends Activity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
    }
}
