package com.phone.dialmate.ui;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.VH> {

    private final Context ctx;
    private final List<String> items = new ArrayList<>();

    public CallLogsAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void loadCallLogs() {
        items.clear();
        try {
            Cursor c = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    null, null, null, CallLog.Calls.DATE + " DESC");
            if (c != null) {
                int nIdx = c.getColumnIndex(CallLog.Calls.NUMBER);
                int tIdx = c.getColumnIndex(CallLog.Calls.TYPE);
                int dIdx = c.getColumnIndex(CallLog.Calls.DATE);
                while (c.moveToNext()) {
                    String num = nIdx >= 0 ? c.getString(nIdx) : "";
                    int type = tIdx >= 0 ? c.getInt(tIdx) : 0;
                    long millis = dIdx >= 0 ? c.getLong(dIdx) : 0L;
                    String date = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                            .format(new Date(millis));
                    String t = "Unknown";
                    if (type == CallLog.Calls.INCOMING_TYPE) t = "Incoming";
                    else if (type == CallLog.Calls.OUTGOING_TYPE) t = "Outgoing";
                    else if (type == CallLog.Calls.MISSED_TYPE) t = "Missed";
                    items.add(t + ": " + num + "\n" + date);
                }
                c.close();
            }
        } catch (SecurityException se) {
            se.printStackTrace(); // Permission denied
        } catch (Exception e) {
            e.printStackTrace(); // Any other error
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.text.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView text;

        VH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}
