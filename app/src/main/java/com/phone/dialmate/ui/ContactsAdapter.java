package com.phone.dialmate.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.phone.dialmate.R;
import com.phone.dialmate.model.Contact;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    public interface OnContactClickListener { void onContactClick(@NonNull Contact contact); }

    private List<Contact> items;
    private final OnContactClickListener listener;

    public ContactsAdapter(List<Contact> items, OnContactClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void update(List<Contact> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Contact c = items.get(pos);
        h.name.setText(c.getName());
        h.num.setText(c.getPhoneNumber());
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onContactClick(c);
        });
    }

    @Override public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, num;
        VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            num  = itemView.findViewById(R.id.text_number);
        }
    }
}
