package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.phone.dialmate.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class DialerFragment extends Fragment {
    private static final String TAG = "DialMate/DialerFrag";
    private EditText input;

    private final androidx.activity.result.ActivityResultLauncher<String> reqCallPerm =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                Log.d(TAG, "CALL_PHONE result: " + granted);
                if (granted) doCall();
                else Log.w(TAG, "CALL_PHONE denied, falling back to ACTION_DIAL");
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_dialer, container, false);

        input = v.findViewById(R.id.input_number);

        int[] ids = { R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6,
                R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_0, R.id.btn_star, R.id.btn_hash };

        View.OnClickListener digitListener = view -> {
            Button b = (Button) view;
            String digit = b.getText().toString();
            Log.d(TAG, "digit: " + digit);
            input.append(digit);
        };

        for (int id : ids) {
            View btn = v.findViewById(id);
            if (btn instanceof Button) btn.setOnClickListener(digitListener);
        }

        v.findViewById(R.id.btn_delete).setOnClickListener(view -> {
            Editable e = input.getText();
            if (e.length() > 0) {
                e.delete(e.length()-1, e.length());
                Log.d(TAG, "delete last char, now=" + e);
            }
        });

        v.findViewById(R.id.btn_call).setOnClickListener(view -> callPressed());
        return v;
    }

    private void callPressed() {
        String number = input.getText().toString().trim();
        Log.d(TAG, "callPressed number=" + number);
        if (number.isEmpty()) {
            Log.w(TAG, "No number entered");
            return;
        }
        int state = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE);
        if (state == PERMISSION_GRANTED) {
            doCall();
        } else {
            Log.d(TAG, "Requesting CALL_PHONE");
            reqCallPerm.launch(Manifest.permission.CALL_PHONE);
        }
    }

    private void doCall() {
        String number = input.getText().toString().trim();
        try {
            Log.d(TAG, "doCall ACTION_CALL -> " + number);
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            startActivity(i);
        } catch (SecurityException se) {
            Log.e(TAG, "CALL_PHONE denied at runtime, fallback to ACTION_DIAL", se);
            Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
            startActivity(i);
        } catch (Exception e) {
            Log.e(TAG, "doCall error", e);
        }
    }
}
