package com.zentertain.common.share;

import android.app.Activity;

public class WhatsAppSharer extends BaseSharer {

    @Override
    public void doShare(Activity activity, String imagePath) {
        super.doShare(activity, imagePath);
    }

    @Override
    protected String getSharePackageName() {
        return ShareConstants.WHATSAPP_PACKAGE_NAME;
    }

}
