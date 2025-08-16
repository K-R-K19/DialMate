package com.phone.dialmate.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phone.dialmate.R;
import com.phone.dialmate.model.Contact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private static final String TAG = "DialMate/ContactsAdp";

    private final Context context;
    private List<Contact> contactList;

    public ContactsAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textName.setText(contact.getName() != null ? contact.getName() : "");
        holder.textNumber.setText(contact.getPhoneNumber() != null ? contact.getPhoneNumber() : "");
        holder.btnEdit.setOnClickListener(v -> {
            Log.d(TAG, "Edit clicked: " + contact.getName() + " / " + contact.getPhoneNumber());
            if (contact != null) {
                Intent intent = new Intent(context, EditContactActivity.class);
                intent.putExtra(EditContactActivity.EXTRA_CONTACT_NAME, contact.getName());
                intent.putExtra(EditContactActivity.EXTRA_CONTACT_NUMBER, contact.getPhoneNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() { return contactList != null ? contactList.size() : 0; }

    public void updateContacts(List<Contact> newContacts) {
        Log.d(TAG, "updateContacts size=" + (newContacts == null ? 0 : newContacts.size()));
        this.contactList = newContacts;
        notifyDataSetChanged();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textNumber;
        ImageButton btnEdit;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_contact_name);
            textNumber = itemView.findViewById(R.id.text_contact_number);
            btnEdit = itemView.findViewById(R.id.btn_edit_contact);
        }
    }
}
