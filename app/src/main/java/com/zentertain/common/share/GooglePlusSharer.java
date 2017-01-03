package com.zentertain.common.share;

import android.app.Activity;

public class GooglePlusSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        super.doShare(activity, imagePath);
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.GOOGLE_PLUS_PACKAGE_NAME;
    }

}
