package com.phone.dialmate.adapters;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.VH> {

    public static class Item {
        public final String number;
        public final int type;
        public final long date;
        public final long durationSec;

        public Item(String number, int type, long date, long durationSec) {
            this.number = number;
            this.type = type;
            this.date = date;
            this.durationSec = durationSec;
        }
    }

    private final List<Item> data = new ArrayList<>();

    public void submit(List<Item> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Item it = data.get(position);
        h.number.setText(it.number == null ? "" : it.number);
        String when = DateFormat.format("dd MMM, HH:mm", new Date(it.date)).toString();
        String typeText;
        switch (it.type) {
            case android.provider.CallLog.Calls.OUTGOING_TYPE: typeText = "Outgoing"; break;
            case android.provider.CallLog.Calls.INCOMING_TYPE: typeText = "Incoming"; break;
            case android.provider.CallLog.Calls.MISSED_TYPE:   typeText = "Missed";   break;
            case android.provider.CallLog.Calls.REJECTED_TYPE: typeText = "Rejected"; break;
            case android.provider.CallLog.Calls.VOICEMAIL_TYPE:typeText = "Voicemail";break;
            default: typeText = "Other";
        }
        h.meta.setText(typeText + " • " + when + " • " + it.durationSec + "s");
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView number, meta;
        VH(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.tv_number);
            meta = itemView.findViewById(R.id.tv_meta);
        }
    }
}
