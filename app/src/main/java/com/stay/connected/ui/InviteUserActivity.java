package com.stay.connected.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.util.ImageUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteUserActivity extends InjectableActivity {

    public static final String TAG = InviteUserActivity.class.getName();
    public static final String ARG_EXTRA_CONTACT_NAME = "arg_extra_contact_name_" + TAG;
    public static final String ARG_EXTRA_CONTACT_NUMBER = "arg_extra_contact_number_" + TAG;

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


        if (!isUserAvaratExists()) {
            startUploadAvatarActivity(false);
        }
    }

    @Override
    void injectComponent(ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT_REQUEST:
                    addContact(data);
                    break;
            }
        }
    }

    private void addContact(Intent intent) {
        if (intent.hasExtra(ARG_EXTRA_CONTACT_NAME) && intent.hasExtra(ARG_EXTRA_CONTACT_NUMBER)) {
            edtName.setText(intent.getStringExtra(ARG_EXTRA_CONTACT_NAME));
            edtMobile.setText(intent.getStringExtra(ARG_EXTRA_CONTACT_NUMBER));
        }
    }

    @OnClick(R.id.btn_invite)

    public void onClickInvite() {

        boolean validationSuccess = true;
        inputLayName.setError(null);
        inputLayName.setErrorEnabled(false);
        inputLayMobile.setError(null);
        inputLayMobile.setErrorEnabled(false);

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
        if (checkContactsPermission()) {
            startContactsSearchActivity();
        }
    }

    private void inviteContact(String name, String mobile) {
        String msgBody = String.format(getResources().getString(R.string.text_user_invitation_msg_content), name);
        sendInvitation(msgBody, mobile);
    }
}
