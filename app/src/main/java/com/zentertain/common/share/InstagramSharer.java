package com.zentertain.common.share;

import android.app.Activity;

public class InstagramSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        super.doShare(activity, imagePath);
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.INSTAGRAM_PACKAGE_NAME;
    }

}
