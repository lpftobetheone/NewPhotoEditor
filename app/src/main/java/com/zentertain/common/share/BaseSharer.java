package com.zentertain.common.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public abstract class BaseSharer {

    public void doShare(Activity activity, String imagePath) {
        Uri uri = Uri.parse(ShareConstants.FILE_URI + imagePath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ShareConstants.SHARE_IMAGE_MIME_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage(getSharePackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            activity.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public boolean isExist(Activity activity) {
        try {
            activity.getPackageManager().getApplicationInfo(getSharePackageName(), 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected abstract String getSharePackageName();

}
