package com.phone.dialmate.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CallLogsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CallLogsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_logs, container, false);

        recyclerView = view.findViewById(R.id.recycler_call_logs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CallLogsAdapter(getCallLogs());
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<String> getCallLogs() {
        List<String> logs = new ArrayList<>();

        Cursor cursor = requireContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null, null, null,
                CallLog.Calls.DATE + " DESC"
        );

        int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
        int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);

        while (cursor != null && cursor.moveToNext()) {
            String number = cursor.getString(numberIndex);
            int type = cursor.getInt(typeIndex);
            long dateMillis = cursor.getLong(dateIndex);
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                    .format(new Date(dateMillis));

            String callType = "Unknown";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }

            logs.add(callType + ": " + number + "\n" + date);
        }

        if (cursor != null) {
            cursor.close();
        }

        return logs;
    }

    // Internal Adapter
    private static class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.ViewHolder> {

        private final List<String> callLogs;

        public CallLogsAdapter(List<String> callLogs) {
            this.callLogs = callLogs;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(callLogs.get(position));
        }

        @Override
        public int getItemCount() {
            return callLogs.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
