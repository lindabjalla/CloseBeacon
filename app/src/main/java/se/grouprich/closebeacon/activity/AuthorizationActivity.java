package se.grouprich.closebeacon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.controller.RSAEncrypt;

public class AuthorizationActivity extends AppCompatActivity {

    private Button buttonAuth;
    private EditText editText_authCode;
    private String authorizationCode;
    private String authorizationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        buttonAuth = (Button) findViewById(R.id.button_auth);
        editText_authCode = (EditText) findViewById(R.id.editText_auth_code);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorizationCode.equals(editText_authCode.getText());
                if (authorizationCode.isEmpty()) {
                    editText_authCode.setText("Your field is empty");
                } else {
                    RSAEncrypt encryptRequest = new RSAEncrypt();

                    // authorizationRequest.equals(encryptRequest.encrypt(,encryptRequest.encodeBase64Authcode(authorizationCode)));

                }
            }
        });

    }
}
