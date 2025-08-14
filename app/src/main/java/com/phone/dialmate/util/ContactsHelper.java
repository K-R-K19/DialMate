package com.phone.dialmate.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.phone.dialmate.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsHelper {

    public static List<Contact> getContacts(Context context) {
        List<Contact> contactList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(new Contact(name, phoneNumber));
            }
            cursor.close();
        }
        return contactList;
    }

    public static boolean updateContact(Context context, Contact contact, String oldNumber) {
        try {
            ContentResolver resolver = context.getContentResolver();

            ContentValues values = new ContentValues();
            values.put(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, contact.getName());
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhoneNumber());

            String where = ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?";
            String[] params = new String[]{oldNumber};

            int updated = resolver.update(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, values, where, params);
            return updated > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
