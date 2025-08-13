package com.phone.dialmate.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.phone.dialmate.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallLogsActivity extends AppCompatActivity {

    private static final int REQUEST_CALL_LOG_PERMISSION = 2;
    private ListView callLogsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);

        callLogsListView = findViewById(R.id.callLogsListView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    REQUEST_CALL_LOG_PERMISSION);
        } else {
            loadCallLogs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_LOG_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCallLogs();
        } else {
            Toast.makeText(this, "Permission denied to read call logs", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadCallLogs() {
        List<String> callLogs = new ArrayList<>();

        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.DATE + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                long dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));

                String callType;
                switch (type) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "Outgoing";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "Incoming";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callType = "Missed";
                        break;
                    default:
                        callType = "Unknown";
                        break;
                }

                String callLog = "Number: " + number + "\nType: " + callType +
                        "\nDate: " + new Date(dateMillis) + "\nDuration: " + duration + " sec";
                callLogs.add(callLog);

            } while (cursor.moveToNext());

            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, callLogs);
        callLogsListView.setAdapter(adapter);
    }
}
