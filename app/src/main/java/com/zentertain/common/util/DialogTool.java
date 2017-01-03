package com.zentertain.common.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.flurry.android.FlurryAgent;

import net.zenjoy.photoeditor.R;

public class DialogTool {

    /**
     * All of the context parameter should be Activity context
     */

    public static void showPhotoEditor(final Context context) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_photoeditor);
        dialog.findViewById(R.id.download_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FlurryAgent.logEvent("Click Photo Editor Pro Free Download.");
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.zentertain.photoeditor");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

    public static void showInstaSquare(final Context context) {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_instasquare);
        dialog.findViewById(R.id.download_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FlurryAgent.logEvent("Click InstaSquare Free Download.");
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.zenjoy.instasquare");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

    public static void showHelp(final Context context) {
        // custom dialog
        final Dialog dialog = new Dialog(context, R.style.DialogFullscreen);
        dialog.setContentView(R.layout.custom_dialog_help);
        dialog.findViewById(R.id.button_help_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showRate(final Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        final SharedPreferences.Editor editor = prefs.edit();
        // custom dialog
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_rate);
        dialog.findViewById(R.id.dialog_rate_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dont_show_again", true);
                    editor.commit();
                }
                dialog.dismiss();
                FlurryAgent.logEvent("5 star Rating.");
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });

        dialog.findViewById(R.id.dialog_cancel_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dont_show_again", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}