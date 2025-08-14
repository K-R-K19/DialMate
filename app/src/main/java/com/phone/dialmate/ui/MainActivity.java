package com.phone.dialmate.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phone.dialmate.R;
import com.phone.dialmate.fragments.CallLogsFragment;
import com.phone.dialmate.fragments.ContactsFragment;
import com.phone.dialmate.fragments.DialerFragment;
import com.phone.dialmate.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_dialer) {
                selectedFragment = new DialerFragment();
            } else if (id == R.id.nav_recents) {
                selectedFragment = new CallLogsFragment();
            } else if (id == R.id.nav_contacts) {
                selectedFragment = new ContactsFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });


        // Set default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_contacts);
        }
    }
}
