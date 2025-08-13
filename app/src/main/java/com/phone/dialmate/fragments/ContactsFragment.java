package com.phone.dialmate.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.ui.ContactsAdapter;
import com.phone.dialmate.model.Contact;
import com.phone.dialmate.util.ContactsHelper;
import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private static final int PERM_READ_CONTACTS = 101;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = v.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        contactsAdapter = new ContactsAdapter(getContext(), contactList, new ContactsAdapter.ContactClickListener() {
            @Override
            public void onCallClick(Contact contact) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(android.net.Uri.parse("tel:" + contact.getPhoneNumber()));
                startActivity(i);
            }

            @Override
            public void onEditClick(Contact contact) {
                Intent intent = new Intent(getActivity(), com.phone.dialmate.ui.EditContactActivity.class);
                intent.putExtra("contact_id", contact.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Contact contact) {
                ContactsHelper.deleteContact(getContext(), contact.getId());
                loadContacts();
            }
        });

        recyclerView.setAdapter(contactsAdapter);

        EditText searchBar = v.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c){
                contactsAdapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s){}
        });

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, PERM_READ_CONTACTS);
        } else {
            loadContacts();
        }

        return v;
    }

    private void loadContacts() {
        contactList.clear();
        contactList.addAll(ContactsHelper.getContacts(requireContext()));
        contactsAdapter.updateContacts(contactList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] results) {
        if (requestCode == PERM_READ_CONTACTS && results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts();
        }
    }
}
