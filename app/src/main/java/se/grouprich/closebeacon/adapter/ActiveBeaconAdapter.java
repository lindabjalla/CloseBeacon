package se.grouprich.closebeacon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import se.grouprich.closebeacon.R;
import se.grouprich.closebeacon.model.comparator.DistanceComparator;

public class ActiveBeaconAdapter extends RecyclerView.Adapter<ActiveBeaconAdapter.DeviceViewHolder> {

    private Context context;
    private List<Beacon> beacons;

    public ActiveBeaconAdapter(final Context context, final Set<Beacon> beacons) {

        this.context = context;
        this.beacons = new ArrayList<>();
        this.beacons.addAll(beacons);
        Collections.sort(this.beacons, new DistanceComparator());

        for (Beacon b : this.beacons) {
            Log.d("distance", "---------" + b.getId1().toString() + ": " + String.valueOf(b.getDistance()));
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_active_beacons, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {

        holder.proximityUuidView.setText(beacons.get(position).getId1().toString());
        holder.majorView.setText(beacons.get(position).getId2().toString());
        holder.minorView.setText(beacons.get(position).getId3().toString());

        if (position % 2 == 0) {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorEggshell));

        } else {

            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorVintageBlue));
        }
    }

    @Override
    public int getItemCount() {

        return beacons.size();
    }

    public static final class DeviceViewHolder extends RecyclerView.ViewHolder {

        public final TextView proximityUuidView;
        public final TextView majorView;
        public final TextView minorView;

        public DeviceViewHolder(View view) {

            super(view);
            this.proximityUuidView = (TextView) view.findViewById(R.id.view_proximity_uuid);
            this.majorView = (TextView) view.findViewById(R.id.view_major);
            this.minorView = (TextView) view.findViewById(R.id.view_minor);
        }
    }
}