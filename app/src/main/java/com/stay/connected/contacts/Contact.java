package com.stay.connected.contacts;

/**
 * Created by karthikeyan on 9/8/17.
 */

public class Contact {

    public static final int TYPE_CONTACT_HEADER = 1;
    public static final int TYPE_CONTACT = 2;

    private String name;
    private int type;

    public Contact(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
