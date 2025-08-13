package com.phone.dialmate.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.model.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    public interface ContactClickListener {
        void onCallClick(Contact contact);
        void onEditClick(Contact contact);
        void onDeleteClick(Contact contact);
    }

    private final Context ctx;
    private List<Contact> contacts;
    private final List<Contact> original;
    private final ContactClickListener listener;

    public ContactsAdapter(Context ctx, List<Contact> contacts, ContactClickListener listener) {
        this.ctx = ctx;
        this.contacts = contacts != null ? new ArrayList<>(contacts) : new ArrayList<>();
        this.original = new ArrayList<>(this.contacts);
        this.listener = listener;
    }

    public void updateContacts(List<Contact> newContacts) {
        this.contacts = newContacts != null ? new ArrayList<>(newContacts) : new ArrayList<>();
        original.clear(); original.addAll(this.contacts);
        notifyDataSetChanged();
    }

    public void setContacts(List<Contact> newContacts) { updateContacts(newContacts); }

    public void filter(String q) {
        if (TextUtils.isEmpty(q)) {
            contacts = new ArrayList<>(original);
        } else {
            List<Contact> f = new ArrayList<>();
            String low = q.toLowerCase();
            for (Contact c : original) {
                if ((c.getName() != null && c.getName().toLowerCase().contains(low)) ||
                        (c.getPhoneNumber() != null && c.getPhoneNumber().contains(q))) {
                    f.add(c);
                }
            }
            contacts = f;
        }
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.contact_item, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Contact c = contacts.get(position);
        holder.name.setText(c.getName());
        holder.number.setText(c.getPhoneNumber());
        holder.btnCall.setOnClickListener(v -> {
            if (listener != null) listener.onCallClick(c);
        });
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(c);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(c);
        });
    }

    @Override public int getItemCount() { return contacts != null ? contacts.size() : 0; }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, number;
        ImageButton btnCall, btnEdit, btnDelete;
        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            number = itemView.findViewById(R.id.contact_number);
            btnCall = itemView.findViewById(R.id.btn_call);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
