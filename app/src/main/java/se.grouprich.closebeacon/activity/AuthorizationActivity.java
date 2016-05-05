package se.grouprich.closebeacon.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import grouprich.se.closebeacon.R;
import se.grouprich.closebeacon.controller.RSAEncrypt;

public class AuthorizationActivity extends AppCompatActivity {

    private Button buttonAuth;
    private TextView textAuthCode;
    private String authorizationCode;
    private String authorizationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        buttonAuth = (Button) findViewById(R.id.button_auth);
        textAuthCode = (TextView) findViewById(R.id.text_authCode);
        buttonAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorizationCode.equals(textAuthCode.getText());
                if (authorizationCode.isEmpty()) {
                    textAuthCode.setText("Your field is empty");
                } else {
                    RSAEncrypt encryptRequest = new RSAEncrypt();

                    // authorizationRequest.equals(encryptRequest.encrypt(,encryptRequest.encodeBase64Authcode(authorizationCode)));

                }
            }
        });

    }
}
