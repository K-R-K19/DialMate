package com.phone.dialmate.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phone.dialmate.R;
import com.phone.dialmate.fragments.ContactsFragment;
import com.phone.dialmate.fragments.RecentsFragment;
import com.phone.dialmate.fragments.DialerFragment;
import com.phone.dialmate.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_contacts) selected = new ContactsFragment();
            else if (id == R.id.nav_recents) selected = new RecentsFragment();
            else if (id == R.id.nav_dialer) selected = new DialerFragment();
            else if (id == R.id.nav_profile) selected = new ProfileFragment();

            if (selected != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ContactsFragment())
                    .commit();
        }
    }
}
