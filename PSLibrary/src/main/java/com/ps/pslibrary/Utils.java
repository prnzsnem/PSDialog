package com.ps.pslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

public class Utils {
    @SuppressLint("HardwareIds")
    public synchronized static String id(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
