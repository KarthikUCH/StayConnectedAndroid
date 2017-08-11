package com.stay.connected.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ContactsProvider {
    public static List<Contact> load(Context context) {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                int hasNumber = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasNumber > 0) {
                    contacts.add(new Contact(id, name, Contact.TYPE_CONTACT));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        return contacts;
    }

    public static Contact getContactNumber(Context context, Contact contact) {
        String number = null;
        Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI, null,
                Phone.CONTACT_ID + " =? ", new String[]{contact.getContactId()}, null);
        if (cursor.moveToFirst()) {
            number = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        contact.setNumber(number);
        return contact;
    }
}
