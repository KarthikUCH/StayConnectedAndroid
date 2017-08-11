package com.stay.connected.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.stay.connected.R;
import com.stay.connected.application.ApplicationComponent;
import com.stay.connected.network.ResponseListener;
import com.stay.connected.util.ImageUtil;

import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadAvatarActivity extends InjectableActivity {

    protected static final int REQUEST_GET_IMAGE_GALLERY = 0x00000001;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);
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

        switch (requestCode) {
            case REQUEST_GET_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    uploadAvatar(data.getData());
                }
                break;
            default:
                break;
        }
    }

    private void uploadAvatar(Uri uri) {
        try {
            Bitmap avatarBitmap = getImagePreview(uri, imgAvatar.getHeight(), imgAvatar.getWidth());
            imgAvatar.setImageBitmap(avatarBitmap);
            mAppController.uploadAvatar(avatarBitmap, mAppPreference.getUserEmail(), new UploadAvatarListener(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        if (isUserAvaratExists()) {
            onBackPressed();
        } else {
            ChooseImage();
        }
    }

    @OnClick(R.id.tv_skip_now)
    public void onClickSkipNow() {
        onBackPressed();
    }


    @OnClick(R.id.img_avatar)
    public void onClickAvatar() {
        ChooseImage();
    }

    @OnClick(R.id.btn_update_avatar)
    public void onClickUpdateAvatar() {
        ChooseImage();
    }

    private void ChooseImage() {

        if (isFinishing()) {
            return;
        }

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GET_IMAGE_GALLERY);
    }

    private Bitmap getImagePreview(Uri uri, int maxHeight, int maxWidth) throws FileNotFoundException {
        Bitmap thumb = ImageUtil.getThumbnail(this, uri, maxHeight, maxWidth);
        int rotate = ImageUtil.getPhotoOrientation(this, uri);
        thumb = ImageUtil.rotate(thumb, rotate);
        return thumb;
    }

    /**
     * Listen to upload avatar request
     */
    private static class UploadAvatarListener implements ResponseListener<Boolean> {

        private final WeakReference<UploadAvatarActivity> mReference;

        public UploadAvatarListener(UploadAvatarActivity activity) {
            mReference = new WeakReference<UploadAvatarActivity>(activity);
        }

        @Override
        public void onResponse(Boolean response) {
            if (mReference.get() != null && response) {
                mReference.get().showSuccess();
            }
        }

        @Override
        public void onError(String errorMessage) {

        }

        @Override
        public void onCompleted() {

        }
    }

    private void showSuccess() {
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
    }
}
