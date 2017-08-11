package com.stay.connected.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import com.stay.connected.R;
import com.stay.connected.application.AppController;
import com.stay.connected.application.AppPreference;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.application.StayConnected;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by karthikeyan on 7/8/17.
 */

public abstract class InjectableActivity extends AppCompatActivity {


    protected static final int PICK_CONTACT_REQUEST = 0x00000011;

    protected static final int APP_PERMISSIONS_REQUEST_READ_CONTACTS = 0x00000001;

    private AlertDialog dialog;
    private ProgressDialog mProgressDialog;

    @Inject
    protected AppController mAppController;

    @Inject
    protected AppPreference mAppPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponent(((StayConnected) getApplication()).getComponent());
    }

    abstract void injectComponent(ApplicationComponent component);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case APP_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContactsSearchActivity();
                } else {
                    showContactsPermissionAlertDialog(R.string.text_alert_user_need_contact_permission);
                }
            }
            break;

        }
    }


    protected final void showProgressDialog(String message, boolean cancelable) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(cancelable);
            mProgressDialog.setIndeterminate(true);
        }
        if (TextUtils.isEmpty(message)) {
            mProgressDialog.setMessage(getString(R.string.text_loading));
        } else {
            mProgressDialog.setMessage(message);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    protected final void dismissProgressDialog(String toastMessage) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected boolean isUserAvaratExists() {
        String filePath = getApplicationContext().getFilesDir() + "/" + mAppPreference.getUserEmail();
        boolean exists = new File(filePath).exists();
        return exists;
    }

    protected void startVerifyOtpActivity(boolean finish) {
        Intent intent = new Intent(this, VerifyOtpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startSignInActivity(boolean finish) {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startMainActivity(boolean finish) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startInviteUserActivity(boolean finish) {
        Intent intent = new Intent(this, InviteUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startUploadAvatarActivity(boolean finish) {
        Intent intent = new Intent(this, UploadAvatarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }

    protected void startContactsSearchActivity() {
        Intent intent = new Intent(this, ContactsSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    protected void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    protected void sendInvitation(String body, String mobile) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + mobile));
        sendIntent.putExtra("sms_body", body);
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        }
    }

    protected void showAlertDialog(int messageId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppDialogTheme);
        dialogBuilder.setMessage(getResources().getString(messageId))
                .setPositiveButton(getResources().getString(R.string.text_btn_ok), (dialog, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();

    }

    protected void showUrlAlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppDialogTheme);
        final EditText input = new EditText(this);
        input.setText(mAppPreference.getAppUrl());
        input.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_grey_dark));
        dialogBuilder.setMessage("Add the URL")
                .setView(input)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.text_btn_ok), (dialog, which) -> {
                    String url = input.getText().toString().trim();
                    if (!TextUtils.isEmpty(url)) {
                        mAppPreference.setAppUrl(url);
                        mAppController.updateRestServiceFactory();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();

    }

    protected void showContactsPermissionAlertDialog(int messageId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppDialogTheme);
        dialogBuilder.setMessage(getResources().getString(messageId))
                .setPositiveButton(getResources().getString(R.string.text_btn_setting), (dialog1, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    startAppSettings();
                })
                .setNegativeButton(getResources().getString(R.string.text_btn_cancel), (dialog, which) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
        dialog = dialogBuilder.create();
        dialog.show();
    }


    protected boolean checkContactsPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        APP_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        APP_PERMISSIONS_REQUEST_READ_CONTACTS);
            }

            return false;
        } else {
            return true;
        }

    }
}
