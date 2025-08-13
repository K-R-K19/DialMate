package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;
import com.phone.dialmate.ui.CallLogsAdapter;

public class RecentsFragment extends Fragment {

    private static final int REQUEST_CALL_LOG = 100;
    private CallLogsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.recycler_call_logs);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CallLogsAdapter(requireContext());
        rv.setAdapter(adapter);

        checkPermissionsAndLoadLogs();
    }

    private void checkPermissionsAndLoadLogs() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_CALL_LOG);
        } else {
            adapter.loadCallLogs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_LOG) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                adapter.loadCallLogs();
            }
        }
    }
}
