package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.phone.dialmate.model.Contact;
import com.phone.dialmate.ui.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ContactsFragment extends Fragment {
    private static final String TAG = "DialMate/ContactsFrag";

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private final List<Contact> contactList = new ArrayList<>();

    private final androidx.activity.result.ActivityResultLauncher<String> reqContactsPerm =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                Log.d(TAG, "READ_CONTACTS result: " + granted);
                if (granted) loadContacts();
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = view.findViewById(R.id.recycler_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactsAdapter(requireContext(), contactList);
        recyclerView.setAdapter(adapter);

        ensurePermissionAndLoad();
        return view;
    }

    private void ensurePermissionAndLoad() {
        int state = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS);
        Log.d(TAG, "READ_CONTACTS check = " + state);
        if (state == PERMISSION_GRANTED) {
            loadContacts();
        } else {
            Log.d(TAG, "Requesting READ_CONTACTS");
            reqContactsPerm.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private void loadContacts() {
        Log.d(TAG, "loadContacts()");
        contactList.clear();
        try {
            ContentResolver cr = requireContext().getContentResolver();
            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };
            Cursor c = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, null, null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            if (c != null) {
                int idxName = 0, idxNum = 1;
                while (c.moveToNext()) {
                    String name = c.getString(idxName);
                    String number = c.getString(idxNum);
                    contactList.add(new Contact(name, number));
                }
                c.close();
            }
            Log.d(TAG, "Loaded contacts: " + contactList.size());
            adapter.updateContacts(new ArrayList<>(contactList));
        } catch (Exception e) {
            Log.e(TAG, "loadContacts error", e);
        }
    }
}
