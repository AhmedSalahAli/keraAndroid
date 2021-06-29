package com.example.kera.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


public class ForceUpdateChecker {

    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_NORMAL_UPDATE_REQUIRED = "normal_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";
    public static final String KEY_User_Type = "force_update_user_type";
    public static final String All_Users = "all";
    public static final String prod_base_url = "prod_base_url";
    private OnUpdateNeededListener onUpdateNeededListener;
    private onUpatePreferedListner onUpatePreferedListner;
    private onCheckConfigParamsListner onCheckConfigParamsListner;
    private Context context;
    private  String UserType;
    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }
    public interface onUpatePreferedListner {
        void onUpdatePrefered(String updateUrl);
    }
    public interface onCheckConfigParamsListner {
        void onCheckConfigParams (String BaseUrl);
    }
    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener ,onUpatePreferedListner onUpatePreferedListner, String UserType) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
        this.onUpatePreferedListner = onUpatePreferedListner;
        this.UserType = UserType;
    }
    public ForceUpdateChecker(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener ,onUpatePreferedListner onUpatePreferedListner,onCheckConfigParamsListner onCheckConfigParamsListner) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
        this.onUpatePreferedListner = onUpatePreferedListner;
        this.onCheckConfigParamsListner = onCheckConfigParamsListner;
    }
    public void config() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        String BaseUrl = remoteConfig.getString(prod_base_url);
        if (!BaseUrl.isEmpty()&&BaseUrl!=null){

            onCheckConfigParamsListner.onCheckConfigParams(BaseUrl);
        }


    }
    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);
            String userType = remoteConfig.getString(KEY_User_Type);

            if (!TextUtils.equals(currentVersion, appVersion)
                    && onUpdateNeededListener != null) {
                if (TextUtils.equals(userType, All_Users)) {
                    onUpdateNeededListener.onUpdateNeeded(updateUrl);

                } else {
                    if (TextUtils.equals(userType, UserType)) {
                        onUpdateNeededListener.onUpdateNeeded(updateUrl);

                    }
                }
            }
        } else if (remoteConfig.getBoolean(KEY_NORMAL_UPDATE_REQUIRED)) {
            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);
            String userType = remoteConfig.getString(KEY_User_Type);
            if (!TextUtils.equals(currentVersion, appVersion)
                    && onUpatePreferedListner != null) {
                if (TextUtils.equals(userType, All_Users)) {
                    onUpatePreferedListner.onUpdatePrefered(updateUrl);

                } else {
                    if (TextUtils.equals(userType, UserType)) {
                        onUpatePreferedListner.onUpdatePrefered(updateUrl);

                    }
                }
            }
        }
    }

    private String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;
        private onUpatePreferedListner onUpatePreferedListner;
        private onCheckConfigParamsListner onCheckConfigParams;
        private String UserType;
        public Builder(Context context) {
            this.context = context;

        }

        public Builder UserType(String UserType){
            this.UserType = UserType;
            return this;
        }
        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;

            return this;
        }
        public Builder onUpdatePrefered(onUpatePreferedListner onUpatePreferedListner) {

            this.onUpatePreferedListner = onUpatePreferedListner;
            return this;
        }
        public Builder onCheckConfigParams(onCheckConfigParamsListner onCheckConfigParams) {

            this.onCheckConfigParams = onCheckConfigParams;
            return this;
        }
        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener,onUpatePreferedListner,UserType);
        }
        public ForceUpdateChecker buildConfig() {
            return new ForceUpdateChecker(context, onUpdateNeededListener,onUpatePreferedListner,onCheckConfigParams);
        }
        public ForceUpdateChecker check() {
           ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
        public ForceUpdateChecker config() {
            ForceUpdateChecker forceUpdateChecker = buildConfig();
            forceUpdateChecker.config();

            return forceUpdateChecker;
        }

    }
}