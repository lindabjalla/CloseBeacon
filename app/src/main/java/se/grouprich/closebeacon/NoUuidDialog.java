package se.grouprich.closebeacon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public final class NoUuidDialog extends Dialog implements android.view.View.OnClickListener{

    private Dialog dialog;
    private Button ok;

    public NoUuidDialog(Context context) {

        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_no_uuid);
        ok = (Button)findViewById(R.id.button_ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        dismiss();
    }
}
