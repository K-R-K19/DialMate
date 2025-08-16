package com.phone.dialmate.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phone.dialmate.R;
import com.phone.dialmate.fragments.CallLogsFragment;
import com.phone.dialmate.fragments.ContactsFragment;
import com.phone.dialmate.fragments.DialerFragment;
import com.phone.dialmate.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DialMate/MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment selectedFragment = null;

            if (id == R.id.nav_contacts) {
                Log.d(TAG, "Switch -> ContactsFragment");
                selectedFragment = new ContactsFragment();
            } else if (id == R.id.nav_call_logs) {
                Log.d(TAG, "Switch -> CallLogsFragment");
                selectedFragment = new CallLogsFragment();
            } else if (id == R.id.nav_dialer) {
                Log.d(TAG, "Switch -> DialerFragment");
                selectedFragment = new DialerFragment();
            } else if (id == R.id.nav_profile) {
                Log.d(TAG, "Switch -> ProfileFragment");
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            } else {
                Log.e(TAG, "selectedFragment is null, ignoring click");
            }
            return true;
        });

        // Default tab
        bottomNav.setSelectedItemId(R.id.nav_contacts);
    }
}
