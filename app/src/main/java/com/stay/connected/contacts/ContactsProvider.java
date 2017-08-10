package com.stay.connected.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Alexandr Timoshenko <thick.tav@gmail.com> on 11/13/15.
 */
public class ContactsProvider {
    public static List<Contact> load(Context pContext) {
        List<Contact> contacts = new ArrayList<>();
        Cursor c = null;
        try {
            c = pContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                int hasNumber = c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasNumber > 0) {
                    contacts.add(new Contact(name, Contact.TYPE_CONTACT));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        return contacts;
    }
}
