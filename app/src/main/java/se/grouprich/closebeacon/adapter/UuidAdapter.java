package se.grouprich.closebeacon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.activity.DeviceDetailsActivity;

public class UuidAdapter extends RecyclerView.Adapter<UuidAdapter.UuidViewHolder> {

    private final Context context;
    private final List<String> uuidList;

    public UuidAdapter(Context context, List<String> uuidList) {

        this.context = context;
        this.uuidList = uuidList;
    }

    @Override
    public UuidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_uuid, parent, false);
        return new UuidViewHolder(view, context, uuidList);
    }

    @Override
    public void onBindViewHolder(final UuidViewHolder holder, int position) {

        holder.uuidView.setText(uuidList.get(position));

        if (position % 2 == 0) {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorEggshell));

        } else {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVintageBlue));
        }
    }

    @Override
    public int getItemCount() {

        return uuidList.size();
    }

    public final class UuidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final String SELECTED_UUID_KEY = "se.grouprich.closebeacon.SELECTED_UUID_KEY";

        public final TextView uuidView;
        public final Context context;
        public final List<String> uuidStringList;

        public UuidViewHolder(View view, Context context, List<String> uuidStringList) {

            super(view);
            view.setOnClickListener(this);
            this.uuidView = (TextView) view.findViewById(R.id.uuid_text);
            this.context = context;
            this.uuidStringList = uuidStringList;
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            String uuid = this.uuidStringList.get(position);
            Intent intent = new Intent(context, DeviceDetailsActivity.class);
            intent.putExtra(SELECTED_UUID_KEY, uuid);
            context.startActivity(intent);
        }
    }
}
