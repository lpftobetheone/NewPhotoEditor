package com.zentertain.common.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import net.zenjoy.photoeditor.R;

public class MessengerSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        Uri uri = Uri.parse(ShareConstants.FILE_URI + imagePath);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ShareConstants.SHARE_IMAGE_MIME_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setPackage(getSharePackageName());
        intent.putExtra(ShareConstants.EXTRA_PROTOCOL_VERSION, ShareConstants.PROTOCOL_VERSION);
        intent.putExtra(ShareConstants.EXTRA_APP_ID, activity.getString(R.string.facebook_app_id));

        try {
            activity.startActivityForResult(intent, ShareConstants.SHARE_TO_MESSENGER_REQUEST_CODE);
        } catch (Exception e) {

        }
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.MESSENGER_PACKAGE_NAME;
    }

}
