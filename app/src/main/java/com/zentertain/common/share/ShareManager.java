package com.zentertain.common.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import net.zenjoy.photoeditor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShareManager {

    public List<Sharer> getSharerList(Activity activity) {
        List<Sharer> sharerList = new ArrayList<>();

        BaseSharer messengerSharer = SharerFactory.getSharer(ShareConstants.MESSENGER_PACKAGE_NAME);
        if (messengerSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_messenger);
            sharer.setIcon(R.mipmap.ic_share_messenger);
            sharer.setPackageName(ShareConstants.MESSENGER_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer facebookSharer = SharerFactory.getSharer(ShareConstants.FACEBOOK_PACKAGE_NAME);
        if (facebookSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_facebook);
            sharer.setIcon(R.mipmap.ic_share_facebook);
            sharer.setPackageName(ShareConstants.FACEBOOK_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer instagramSharer = SharerFactory.getSharer(ShareConstants.INSTAGRAM_PACKAGE_NAME);
        if (instagramSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_instagram);
            sharer.setIcon(R.mipmap.ic_share_instagram);
            sharer.setPackageName(ShareConstants.INSTAGRAM_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer whatsappSharer = SharerFactory.getSharer(ShareConstants.WHATSAPP_PACKAGE_NAME);
        if (whatsappSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_whatsapp);
            sharer.setIcon(R.mipmap.ic_share_whatsapp);
            sharer.setPackageName(ShareConstants.WHATSAPP_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer twitterSharer = SharerFactory.getSharer(ShareConstants.TWITTER_PACKAGE_NAME);
        if (twitterSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_twitter);
            sharer.setIcon(R.mipmap.ic_share_twitter);
            sharer.setPackageName(ShareConstants.TWITTER_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer googlePlusSharer = SharerFactory.getSharer(ShareConstants.GOOGLE_PLUS_PACKAGE_NAME);
        if (googlePlusSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_google_plus);
            sharer.setIcon(R.mipmap.ic_share_google_plus);
            sharer.setPackageName(ShareConstants.GOOGLE_PLUS_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        BaseSharer othersSharer = SharerFactory.getSharer(ShareConstants.OTHERS_PACKAGE_NAME);
        if (othersSharer.isExist(activity)) {
            Sharer sharer = new Sharer();
            sharer.setName(R.string.share_others);
            sharer.setIcon(R.mipmap.ic_share_others);
            sharer.setPackageName(ShareConstants.OTHERS_PACKAGE_NAME);
            sharerList.add(sharer);
        }

        return sharerList;
    }

    public Bitmap getShareVideoBitmap(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            File file = new File(imagePath);
            if (file.exists()) {
                try {
                    return ShareUtils.getBitmap(imagePath);
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public void doShare(Activity activity, String imagePath, FacebookSharer facebookSharer, Sharer sharer) {
        if (activity != null && !TextUtils.isEmpty(imagePath) && facebookSharer != null && sharer != null && !TextUtils.isEmpty(sharer.getPackageName())) {
            if (ShareConstants.FACEBOOK_PACKAGE_NAME.equals(sharer.getPackageName())) {
                facebookSharer.doShare(activity, imagePath);
            } else {
                BaseSharer baseSharer = SharerFactory.getSharer(sharer.getPackageName());
                baseSharer.doShare(activity, imagePath);
            }
        }
    }

}
