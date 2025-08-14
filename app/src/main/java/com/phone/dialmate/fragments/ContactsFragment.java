package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.ui.ContactsAdapter;
import com.phone.dialmate.model.Contact;
import com.phone.dialmate.ui.EditContactActivity;
import com.phone.dialmate.util.Logger;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements ContactsAdapter.OnContactClickListener {

    private RecyclerView rv;
    private EditText search;
    private ContactsAdapter adapter;
    private final List<Contact> all = new ArrayList<>();

    private final ActivityResultLauncher<String> reqContacts =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                Logger.log("READ_CONTACTS granted=" + granted);
                if (granted) loadContacts();
            });

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        rv = v.findViewById(R.id.recycler_view_contacts);
        search = v.findViewById(R.id.search_box);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactsAdapter(new ArrayList<>(), this);
        rv.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        } else {
            reqContacts.launch(Manifest.permission.READ_CONTACTS);
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { filter(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });
        return v;
    }

    private void loadContacts() {
        Logger.log("Contacts: loading…");
        all.clear();
        String[] proj = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        try (Cursor c = requireContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, proj,
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")) {
            if (c != null) {
                int iName = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int iNum = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                while (c.moveToNext()) {
                    all.add(new Contact(c.getString(iName), c.getString(iNum)));
                }
            }
        }
        adapter.update(new ArrayList<>(all));
        Logger.log("Contacts: loaded=" + all.size());
    }

    private void filter(String q) {
        q = q == null ? "" : q.toLowerCase();
        List<Contact> filtered = new ArrayList<>();
        for (Contact ct : all) {
            if ((ct.getName() != null && ct.getName().toLowerCase().contains(q)) ||
                    (ct.getPhoneNumber() != null && ct.getPhoneNumber().contains(q))) {
                filtered.add(ct);
            }
        }
        adapter.update(filtered);
    }

    @Override public void onContactClick(@NonNull Contact contact) {
        Intent i = new Intent(requireContext(), EditContactActivity.class);
        i.putExtra("contact_name", contact.getName());
        i.putExtra("contact_number", contact.getPhoneNumber());
        startActivity(i);
    }
}
