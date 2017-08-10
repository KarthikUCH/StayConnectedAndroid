package com.stay.connected.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stay.connected.R;
import com.stay.connected.contacts.Contact;
import com.stay.connected.contacts.ContactsProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyan on 9/8/17.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String sample = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final List<Contact> mContactsList;
    private final List<String> index = new ArrayList<>();

    public ContactsListAdapter(Context context) {
        this.mContactsList = ContactsProvider.load(context);
        fillSections();
    }

    private void fillSections() {

        for (int x = 0; x < mContactsList.size(); x++) {
            String contact = mContactsList.get(x).getName();
            if (contact.length() > 0) {
                String ch = String.valueOf(contact.charAt(0));
                ch = ch.toUpperCase();

                if (!sample.contains(ch)) {
                    if (!index.contains("#")) {
                        index.add("#");
                        mContactsList.add(x, new Contact("#", Contact.TYPE_CONTACT_HEADER));
                        x++;
                    }
                } else if (!index.contains(ch)) {
                    index.add(ch);
                    mContactsList.add(x, new Contact(ch, Contact.TYPE_CONTACT_HEADER));
                    x++;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mContactsList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case Contact.TYPE_CONTACT_HEADER:
                view = inflater.inflate(R.layout.item_contact_header, parent, false);
                return new ContactsHeaderViewHolder(view);
            default:
                view = inflater.inflate(R.layout.item_contact, parent, false);
                return new ContactsViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mContactsList.get(position).getType()) {
            case Contact.TYPE_CONTACT_HEADER:
                ContactsHeaderViewHolder contactsHeaderViewHolder = (ContactsHeaderViewHolder) holder;
                onBindContactHeaderViewHolder(contactsHeaderViewHolder, position);
                break;
            default:
                ContactsViewHolder contactsViewHolder = (ContactsViewHolder) holder;
                onBindContactViewHolder(contactsViewHolder, position);
                break;
        }
    }

    void onBindContactHeaderViewHolder(ContactsHeaderViewHolder holder, int position) {
        holder.tvName.setText(mContactsList.get(position).getName());
    }

    void onBindContactViewHolder(ContactsViewHolder holder, int position) {
        holder.tvName.setText(mContactsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ContactsHeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;

        public ContactsHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
