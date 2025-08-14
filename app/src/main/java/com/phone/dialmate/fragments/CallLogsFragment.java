package com.phone.dialmate.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;
import com.phone.dialmate.model.CallLogItem;
import com.phone.dialmate.ui.CallLogsAdapter;

import java.util.ArrayList;
import java.util.List;

public class CallLogsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CallLogsAdapter adapter;
    private List<CallLogItem> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call_logs, container, false);

        recyclerView = view.findViewById(R.id.recycler_call_logs); // matches XML now
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        data = getCallLogs();

        adapter = new CallLogsAdapter(data);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<CallLogItem> getCallLogs() {
        List<CallLogItem> list = new ArrayList<>();
        Cursor cursor = requireContext().getContentResolver().query(
                CallLog.Calls.CONTENT_URI,
                null, null, null,
                CallLog.Calls.DATE + " DESC"
        );

        if (cursor != null) {
            int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

            while (cursor.moveToNext()) {
                String number = cursor.getString(numberIndex);
                int type = cursor.getInt(typeIndex);
                long dateMillis = cursor.getLong(dateIndex);
                String date = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm a", dateMillis).toString();
                String duration = cursor.getString(durationIndex);

                list.add(new CallLogItem(number, type, date, duration));
            }
            cursor.close();
        }
        return list;
    }
}
