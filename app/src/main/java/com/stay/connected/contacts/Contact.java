package com.stay.connected.contacts;

/**
 * Created by karthikeyan on 9/8/17.
 */

public class Contact {

    public static final int TYPE_CONTACT_HEADER = 1;
    public static final int TYPE_CONTACT = 2;

    private String contactId;
    private String name;
    private String number;
    private int type;

    public Contact(String contactId, String name, int type) {
        this.contactId = contactId;
        this.name = name;
        this.type = type;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }
}
