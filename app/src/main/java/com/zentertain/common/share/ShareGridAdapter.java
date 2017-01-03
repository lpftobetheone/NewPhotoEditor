package com.zentertain.common.share;

import android.content.Context;
import net.zenjoy.photoeditor.R;


public class ShareGridAdapter extends BaseShareAdapter {

    public ShareGridAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.item_share_grid;
    }

}
