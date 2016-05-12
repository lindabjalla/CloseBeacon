package se.grouprich.closebeacon;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class UuidAdapter extends RecyclerView.Adapter<UuidAdapter.UuidViewHolder> {

    private final Context context;
    private final List<String> uuidList;
    private OnItemClickListener onItemClickListener;

    public UuidAdapter(Context context, List<String> uuidList) {
        this.context = context;
        this.uuidList = uuidList;
    }

    @Override
    public UuidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uuid_list, parent, false);
        return new UuidViewHolder(view);
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

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
    }

    public final class UuidViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView uuidView;

        public UuidViewHolder(View view) {

            super(view);
            this.uuidView = (TextView) view.findViewById(R.id.uuid_text);
        }

        @Override
        public void onClick(View view) {

            onItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}
