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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyan on 9/8/17.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String sample = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public interface ContactClickListener {
        void onContactClick(Contact contact);
    }

    private final List<Contact> mContactList;
    private List<Contact> mDisplayContactList = new ArrayList<>();
    private final ContactClickListener mClickListener;
    private String oldSearchQuery = "";

    public ContactsListAdapter(Context context, ContactClickListener clickListener) {
        mContactList = ContactsProvider.load(context);
        this.mDisplayContactList.addAll(mContactList);
        this.mClickListener = clickListener;
        fillSections(mDisplayContactList);
    }

    private void fillSections(List<Contact> list) {

        List<String> index = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            String contact = list.get(x).getName();
            if (contact.length() > 0) {
                String ch = String.valueOf(contact.charAt(0));
                ch = ch.toUpperCase();

                if (!sample.contains(ch)) {
                    if (!index.contains("#")) {
                        index.add("#");
                        list.add(x, new Contact("-1", "#", Contact.TYPE_CONTACT_HEADER));
                        x++;
                    }
                } else if (!index.contains(ch)) {
                    index.add(ch);
                    list.add(x, new Contact("-1", ch, Contact.TYPE_CONTACT_HEADER));
                    x++;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDisplayContactList.get(position).getType();
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
        switch (mDisplayContactList.get(position).getType()) {
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
        holder.tvName.setText(mDisplayContactList.get(position).getName());
    }

    void onBindContactViewHolder(ContactsViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> mClickListener.onContactClick(mDisplayContactList.get(position)));
        holder.tvName.setText(mDisplayContactList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDisplayContactList.size();
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

    public void doSearch(String query) {
        if (query.trim().length() == 0 && oldSearchQuery.length() == 0) {
            closeSearch();
        }
        List<Contact> filteredContacts = new ArrayList();
        List<Contact> searchFromList;
        query = query.trim().toLowerCase();
        if (oldSearchQuery.length() > 0 && query.contains(oldSearchQuery)) {
            searchFromList = mDisplayContactList;
        } else {
            searchFromList = mContactList;
        }

        oldSearchQuery = query;
        for (Contact contact : searchFromList) {
            final String text = contact.getName().toLowerCase();
            if (text.contains(query) && contact.getType() == Contact.TYPE_CONTACT) {
                filteredContacts.add(contact);
            }
        }

        fillSections(filteredContacts);
        mDisplayContactList.clear();
        mDisplayContactList.addAll(filteredContacts);
        notifyDataSetChanged();
    }

    public void closeSearch() {
        mDisplayContactList.clear();
        mDisplayContactList.addAll(mContactList);
        fillSections(mDisplayContactList);
        notifyDataSetChanged();
    }
}
