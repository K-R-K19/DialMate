package com.phone.dialmate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;
import com.phone.dialmate.model.CallLogItem;

import java.util.List;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.VH> {
    private static final String TAG = "DialMate/CallLogsAdp";

    private final List<CallLogItem> callLogs;

    public CallLogsAdapter(List<CallLogItem> callLogs) {
        this.callLogs = callLogs;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_log, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        CallLogItem item = callLogs.get(pos);
        h.textName.setText(item.getName() != null ? item.getName() : "(Unknown)");
        h.textNumber.setText(item.getNumber() != null ? item.getNumber() : "");
        h.textType.setText(item.getTypeString());
        h.textDate.setText(item.getDate());
    }

    @Override public int getItemCount() { return callLogs != null ? callLogs.size() : 0; }

    static class VH extends RecyclerView.ViewHolder {
        TextView textName, textNumber, textType, textDate;
        VH(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_call_name);
            textNumber = itemView.findViewById(R.id.text_call_number);
            textType = itemView.findViewById(R.id.text_call_type);
            textDate = itemView.findViewById(R.id.text_call_date);
        }
    }
}
