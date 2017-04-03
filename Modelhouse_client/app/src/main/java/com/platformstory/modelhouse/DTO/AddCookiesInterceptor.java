package com.platformstory.modelhouse.DTO;


import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.platformstory.modelhouse.Common.UtilLibs;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.Preferences;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//public class AddCookiesInterceptor implements Interceptor {
//
//    @Override
//    public Response intercept(Interceptor.Chain chain) throws IOException {
//        Request.Builder builder = chain.request().newBuilder();
//
//        // Preference에서 cookies를 가져오는 작업을 수행
//        Set<String> preferences = SharedPreferencesBase.getSharedPreference(APIPreferences.SHARED_PREFERENCE_NAME_COOKIE, new HashSet<String>());
//
//        for (String cookie : preferences) {
//            builder.addHeader("Cookie", cookie);
//        }
//
//        // Web,Android,iOS 구분을 위해 User-Agent세팅
//        builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");
//
//        return chain.proceed(builder.build());
//    }
//
//}

public class AddCookiesInterceptor implements Interceptor {
    public static final String PREF_COOKIES = "PREF_COOKIES";
    private Context context;

    public AddCookiesInterceptor(Context context){
        this.context = context;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) PreferenceManager.getDefaultSharedPreferences(context).getStringSet(PREF_COOKIES, new HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.e(UtilLibs.LOG_TAG, "OkHttp || Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
        }

        return chain.proceed(builder.build());
    }
}