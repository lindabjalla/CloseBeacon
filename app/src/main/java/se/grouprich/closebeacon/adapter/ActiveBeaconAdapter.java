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

    public ActiveBeaconAdapter(Context context, Set<Beacon> beacons) {
        this.context = context;
        this.beacons = new ArrayList<>();
        this.beacons.addAll(beacons);
        Collections.sort(this.beacons, new DistanceComparator());

        for (Beacon b : this.beacons) {
            Log.d("distance", "---------" + b.getId1().toString() + ": " + String.valueOf(b.getDistance()));
        }
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_active_beacons, parent, false);
        return new DeviceViewHolder(view, context, beacons);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {

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

    public static final class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView proximityUuidView;
        public final TextView majorView;
        public final TextView minorView;
        List<Beacon> beacons;
        Context context;

        public DeviceViewHolder(View view, Context context, List<Beacon> beacons) {
            super(view);
            this.beacons = beacons;
            this.context = context;

            view.setOnClickListener(this);
            this.proximityUuidView = (TextView) view.findViewById(R.id.view_proximity_uuid);
            this.majorView = (TextView) view.findViewById(R.id.view_major);
            this.minorView = (TextView) view.findViewById(R.id.view_minor);
        }

        @Override
        public void onClick(View view) {

//            int position = getAdapterPosition();
//            Beacon beacon = this.beacons.get(position);
//            Intent intent = new Intent(this.context, DeviceDetailsActivity.class);
//            intent.putExtra("name", beacon.getName());
//            intent.putExtra("macAddress", beacon.getMacAddress());
//            intent.putExtra("rssi", beacon.getRssi());
//            intent.putExtra("serviceUuid", beacon.getServiceUuid());
//            intent.putExtra("serialNumber", beacon.getSerialNumber());
//            intent.putExtra("proximityUuid", beacon.getProximityUuid());
//            this.context.startActivity(intent);
        }
    }
}