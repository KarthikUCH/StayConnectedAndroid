package com.stay.connected.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteUserActivity extends InjectableActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.input_lay_name)
    TextInputLayout inputLayName;

    @BindView(R.id.edt_name)
    EditText edtName;

    @BindView(R.id.input_lay_mobile)
    TextInputLayout inputLayMobile;

    @BindView(R.id.edt_mobile)
    EditText edtMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_user);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }


    @OnClick(R.id.btn_invite)
    public void onClickInvite() {

        boolean validationSuccess = true;
        inputLayName.setError(null);
        inputLayMobile.setError(null);

        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            inputLayName.setError(getResources().getString(R.string.text_empty_field));
            edtName.requestFocus();
            validationSuccess = false;
        }

        if (TextUtils.isEmpty(mobile)) {
            inputLayMobile.setError(getResources().getString(R.string.text_empty_field));
            if (validationSuccess) {
                edtMobile.requestFocus();
                validationSuccess = false;
            }
        }

        if (!validationSuccess) {
            return;
        }

        inviteContact(name, mobile);
    }

    @OnClick(R.id.btn_search_contact)
    public void OnClickSearchContact() {
        startContactsSearchActivity(false);
    }

    private void inviteContact(String name, String mobile) {

    }
}
