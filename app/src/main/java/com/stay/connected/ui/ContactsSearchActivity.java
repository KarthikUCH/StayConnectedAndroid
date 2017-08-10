package com.stay.connected.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.ui.adapter.ContactsListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsSearchActivity extends InjectableActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showContactList();
    }

    void showContactList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new ContactsListAdapter(this));
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }
}
