package com.phone.dialmate.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.phone.dialmate.R;
import com.phone.dialmate.fragments.CallLogsFragment;
import com.phone.dialmate.fragments.ContactsFragment;
import com.phone.dialmate.util.Logger;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.log("MainActivity: onCreate");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new ContactsFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment f = null;
            int id = item.getItemId();
            if (id == R.id.navigation_contacts) f = new ContactsFragment();
            else if (id == R.id.navigation_call_logs) f = new CallLogsFragment();

            if (f != null) {
                loadFragment(f);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(@NonNull Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, fragment);
        tx.commit();
    }
}
