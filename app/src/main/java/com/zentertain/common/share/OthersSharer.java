package com.zentertain.common.share;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import net.zenjoy.photoeditor.R;

public class OthersSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        String title = activity.getResources().getString(R.string.share_with);
        Uri uri = Uri.parse(ShareConstants.FILE_URI + imagePath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(ShareConstants.SHARE_IMAGE_MIME_TYPE);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(Intent.createChooser(intent, title));
        } catch (Exception e) {
        }
    }

    @Override
    public boolean isExist(Activity context) {
        return true;
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.OTHERS_PACKAGE_NAME;
    }

}
