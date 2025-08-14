package com.phone.dialmate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.model.CallLogItem;
import java.util.List;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.VH> {
    private final List<CallLogItem> items;

    public CallLogsAdapter(List<CallLogItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        CallLogItem it = items.get(pos);
        h.number.setText(it.getNumber());
        h.type.setText(it.getCallTypeString());
        h.date.setText(it.getDate());
        h.duration.setText(it.getDuration() + " sec");
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView number, type, date, duration;

        VH(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.text_number);
            type = itemView.findViewById(R.id.text_type);
            date = itemView.findViewById(R.id.text_date);
            duration = itemView.findViewById(R.id.text_duration);
        }
    }
}
