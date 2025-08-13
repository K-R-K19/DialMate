package com.phone.dialmate.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import com.phone.dialmate.model.Contact;
import java.util.ArrayList;
import java.util.List;

public class ContactsHelper {

    public static List<Contact> getContacts(Context ctx) {
        List<Contact> list = new ArrayList<>();
        ContentResolver cr = ctx.getContentResolver();

        Cursor cursor = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            int idIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            int nameIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String id = idIdx >= 0 ? cursor.getString(idIdx) : null;
                String name = nameIdx >= 0 ? cursor.getString(nameIdx) : "";
                String num = numIdx >= 0 ? cursor.getString(numIdx) : "";
                list.add(new Contact(id, name, num));
            }
            cursor.close();
        }
        return list;
    }

    public static boolean deleteContact(Context ctx, String contactId) {
        if (contactId == null) return false;
        try {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
            int rows = ctx.getContentResolver().delete(contactUri, null, null);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
