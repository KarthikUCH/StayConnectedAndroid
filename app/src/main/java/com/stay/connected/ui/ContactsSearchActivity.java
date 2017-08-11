package com.stay.connected.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.contacts.Contact;
import com.stay.connected.contacts.ContactsProvider;
import com.stay.connected.ui.adapter.ContactsListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsSearchActivity extends InjectableActivity implements ContactsListAdapter.ContactClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ContactsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpSearchView();
        showContactList();

    }

    private void setUpSearchView() {

        searchView.setIconifiedByDefault(false);
        ImageView searchCloseButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseButton.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.doSearch(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            mAdapter.closeSearch();
            return false;
        });
    }

    private void showContactList() {
        mAdapter = new ContactsListAdapter(getApplicationContext(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactClick(Contact contact) {
        Contact updatedContact = ContactsProvider.getContactNumber(getApplicationContext(), contact);
        returnChosenContact(updatedContact);
    }

    private void returnChosenContact(Contact contact) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(InviteUserActivity.ARG_EXTRA_CONTACT_NAME, contact.getName());
        returnIntent.putExtra(InviteUserActivity.ARG_EXTRA_CONTACT_NUMBER, contact.getNumber());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
