package com.example.kera.utils;


import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static androidx.core.content.ContextCompat.getSystemService;


public class CommonUtilsJava {


    @NotNull
    public String encodeImage(@Nullable Bitmap selectedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG,10,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    public static void expand(final View source, final View target,final View from ) {
        final int sourceHeight = source.getLayoutParams().height;
        final int targetHeight = target.getLayoutParams().height;
        DropDownAnim a = new DropDownAnim(source,sourceHeight,targetHeight,true);
        a.setDuration((int) (targetHeight / source.getContext().getResources().getDisplayMetrics().density));
        a.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {



            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        source.setVisibility(View.INVISIBLE);
        source.startAnimation(a);
    }





    public static   void collapse(final View v ,final View before ,final View taget) {
        final int sourceHeight = v.getLayoutParams().height;
        final int targetHeight = taget.getMeasuredHeight();
        DropDownAnim a = new DropDownAnim(v ,sourceHeight, targetHeight, false);
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);
    }

}
