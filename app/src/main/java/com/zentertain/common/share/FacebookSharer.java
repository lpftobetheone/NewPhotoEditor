package com.zentertain.common.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import net.zenjoy.photoeditor.R;


public class FacebookSharer extends BaseSharer {

    private CallbackManager callbackManager;

    private FacebookCallback<Sharer.Result> shareCallback;

    private ShareDialog shareDialog;
    
    private boolean canPresentShareDialogWithPhotos;

    public FacebookSharer() {

    }

    public FacebookSharer(final Activity activity, final FacebookCallback<Sharer.Result> shareCallback) {
        this.callbackManager = CallbackManager.Factory.create();
        this.shareCallback = shareCallback;

        shareDialog = new ShareDialog(activity);
        shareDialog.registerCallback(callbackManager, this.shareCallback);
        canPresentShareDialogWithPhotos = ShareDialog.canShow(
                SharePhotoContent.class);
    }

    @Override
    public void doShare(Activity activity, String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            Toast.makeText(activity.getApplicationContext(), R.string.share_image_not_found, Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = Uri.parse("file://" + imagePath);
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setImageUrl(uri)
                .build();        
        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();
        
        if (canPresentShareDialogWithPhotos) {
            shareDialog.show(sharePhotoContent);
        } else {
            Toast.makeText(activity.getApplicationContext(), R.string.share_title_sharing, Toast.LENGTH_SHORT).show();
            ShareApi.share(sharePhotoContent, shareCallback);
        }

    }

    @Override
    public boolean isExist(Activity context) {
        return true;
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.FACEBOOK_PACKAGE_NAME;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
