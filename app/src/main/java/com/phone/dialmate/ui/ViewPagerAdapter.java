package com.phone.dialmate.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.phone.dialmate.fragments.ContactsFragment;
import com.phone.dialmate.fragments.CallLogsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ContactsFragment();
            case 1:
                return new CallLogsFragment();
            default:
                return new ContactsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
