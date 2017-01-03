package com.zentertain.common.share;

import android.app.Activity;

public class TwitterSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        super.doShare(activity, imagePath);
    }

    @Override
    public boolean isExist(Activity activity) {
        return false;
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.TWITTER_PACKAGE_NAME;
    }

}
