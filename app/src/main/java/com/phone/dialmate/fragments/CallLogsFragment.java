package com.phone.dialmate.fragments;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.model.CallLogItem;
import com.phone.dialmate.ui.CallLogsAdapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CallLogsFragment extends Fragment {
    private static final String TAG = "DialMate/CallLogsFrag";

    private RecyclerView recyclerView;
    private CallLogsAdapter adapter;
    private final List<CallLogItem> logs = new ArrayList<>();

    private final androidx.activity.result.ActivityResultLauncher<String> reqCallLogPerm =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                Log.d(TAG, "READ_CALL_LOG result: " + granted);
                if (granted) loadLogs();
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_call_logs, container, false);

        recyclerView = v.findViewById(R.id.recycler_call_logs);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CallLogsAdapter(logs);
        recyclerView.setAdapter(adapter);

        ensurePermissionAndLoad();
        return v;
    }

    private void ensurePermissionAndLoad() {
        int state = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG);
        Log.d(TAG, "READ_CALL_LOG check = " + state);
        if (state == PERMISSION_GRANTED) {
            loadLogs();
        } else {
            Log.d(TAG, "Requesting READ_CALL_LOG");
            reqCallLogPerm.launch(Manifest.permission.READ_CALL_LOG);
        }
    }

    private void loadLogs() {
        Log.d(TAG, "loadLogs()");
        logs.clear();
        Cursor c = null;
        try {
            String[] proj = {
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE,
                    CallLog.Calls.CACHED_NAME
            };
            c = requireContext().getContentResolver().query(
                    CallLog.Calls.CONTENT_URI,
                    proj, null, null,
                    CallLog.Calls.DATE + " DESC");

            if (c != null) {
                int idxNum = 0, idxType = 1, idxDate = 2, idxName = 3;
                while (c.moveToNext()) {
                    String number = c.getString(idxNum);
                    int type = c.getInt(idxType);
                    long millis = c.getLong(idxDate);
                    String name = c.getString(idxName);
                    String dateStr = DateFormat.format("dd MMM yyyy, HH:mm", new Date(millis)).toString();
                    logs.add(new CallLogItem(number, type, dateStr, name));
                }
                c.close();
            }
            Log.d(TAG, "Loaded logs: " + logs.size());
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e(TAG, "loadLogs error", e);
            if (c != null) c.close();
        }
    }
}
