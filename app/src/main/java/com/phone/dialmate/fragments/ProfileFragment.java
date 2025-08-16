package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.phone.dialmate.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ProfileFragment extends Fragment {
    private static final String TAG = "DialMate/ProfileFrag";

    private TextView textVersion, textPerms;

    private final androidx.activity.result.ActivityResultLauncher<String> reqContacts =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> updatePerms());
    private final androidx.activity.result.ActivityResultLauncher<String> reqCallLog =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> updatePerms());
    private final androidx.activity.result.ActivityResultLauncher<String> reqCall =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> updatePerms());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        textVersion = v.findViewById(R.id.text_app_version);
        textPerms = v.findViewById(R.id.text_permissions);

        Button b1 = v.findViewById(R.id.btn_req_contacts);
        Button b2 = v.findViewById(R.id.btn_req_calllog);
        Button b3 = v.findViewById(R.id.btn_req_call);

        b1.setOnClickListener(view -> reqContacts.launch(Manifest.permission.READ_CONTACTS));
        b2.setOnClickListener(view -> reqCallLog.launch(Manifest.permission.READ_CALL_LOG));
        b3.setOnClickListener(view -> reqCall.launch(Manifest.permission.CALL_PHONE));

        updateVersion();
        updatePerms();
        return v;
    }

    private void updateVersion() {
        try {
            PackageManager pm = requireContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(requireContext().getPackageName(), 0);
            textVersion.setText("DialMate v" + pi.versionName + " (" + pi.versionCode + ")");
        } catch (Exception e) {
            Log.e(TAG, "updateVersion error", e);
            textVersion.setText("DialMate");
        }
    }

    private void updatePerms() {
        int c = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        int l = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALL_LOG);
        int p = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE);
        String msg = "Permissions:\n" +
                "READ_CONTACTS: " + (c == PERMISSION_GRANTED) + "\n" +
                "READ_CALL_LOG: " + (l == PERMISSION_GRANTED) + "\n" +
                "CALL_PHONE: " + (p == PERMISSION_GRANTED);
        textPerms.setText(msg);
        Log.d(TAG, msg.replace("\n", " | "));
    }
}
