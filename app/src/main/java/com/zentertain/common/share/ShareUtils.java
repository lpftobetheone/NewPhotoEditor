package com.zentertain.common.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShareUtils {

    public static Bitmap getBitmap(String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(imagePath, options);
    }

}
