package com.app.kera.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


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

    public static String getAddress(Context context, float lat , float lon) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }


    }
}
