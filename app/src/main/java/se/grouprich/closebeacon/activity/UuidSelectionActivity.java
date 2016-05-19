package se.grouprich.closebeacon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.adapter.UuidAdapter;

public class UuidSelectionActivity extends AppCompatActivity {

    public static final String SELECTED_UUID_KEY = "se.grouprich.closebeacon.SELECTED_UUID_KEY";

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uuid_selection);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        Intent intent = getIntent();
        List<String> uuidList = intent.getStringArrayListExtra(DeviceDetailsActivity.UUID_STRING_LIST_KEY);

        UuidAdapter adapter = new UuidAdapter(this, uuidList);

        if (recyclerView != null) {

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}
