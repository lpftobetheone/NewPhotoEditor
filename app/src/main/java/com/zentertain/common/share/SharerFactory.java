package com.zentertain.common.share;

public class SharerFactory {

    public static BaseSharer getSharer(String packageName) {
        switch (packageName) {
            case ShareConstants.MESSENGER_PACKAGE_NAME: {
                return new MessengerSharer();
            }

            case ShareConstants.FACEBOOK_PACKAGE_NAME: {
                return new FacebookSharer();
            }

            case ShareConstants.TWITTER_PACKAGE_NAME: {
                return new TwitterSharer();
            }

            case ShareConstants.WHATSAPP_PACKAGE_NAME: {
                return new WhatsAppSharer();
            }

            case ShareConstants.INSTAGRAM_PACKAGE_NAME: {
                return new InstagramSharer();
            }

            case ShareConstants.GOOGLE_PLUS_PACKAGE_NAME: {
                return new GooglePlusSharer();
            }

            default: {
                return new OthersSharer();
            }
        }
    }

}
