package grouprich.se.closebeacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class AuthorizationActivity extends AppCompatActivity {

    private Button buttonAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        buttonAuth = (Button)findViewById(R.id.button_auth);
        //buttonAuth.setOnClickListener();
    }
}
