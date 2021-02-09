package in.newdevpoint.ssnodejschat.utility;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import in.newdevpoint.ssnodejschat.model.FSUsersModel;


public class PreferenceUtils {


    public static final String SHARED_PREF = "ah_firebase";
    private static final String PREF_LOGIN_DATA = "PREF_LOGIN_DATA";
    private static final String PREF_TOP_SCORER_DATA = "PREF_TOP_SCORER_DATA";
    private static final String PREF_NOTFICATION_DATA = "PREF_NOTFICATION_DATA";

    private static final String PREF_TOKEN = "PREF_TOKEN";
    private static final String PREF_IS_LOGIN = "is_login";
    private static final String USER_PREFS_NAME = "com.arka.fahmni.pre.user";
    private static final String PREF_SAVE_TOP_SCORER_DATA = "top_scorer_data";
    private static final String PREF_SAVE_NOIFICATION_COUNT_DATA = "notification_count_data";
    private static final String PREF_START_TIME = "PREF_START_TIME";
    private static final String PREF_START_TIME_SESSION_ID = "PREF_START_TIME_SESSION_ID";
    private static final String PREF_LANGUAGE = "PREF_LANGUAGE";
    private static final String PREF_CURRENCY = "PREF_CURRENCY";
    private static final boolean PREF_IS_USER_LOGIN = false;
    private static FSUsersModel registrationModel = null;
    private SharedPreferences sharedPreferences1;

    public static void setPrefIsUserLogin(Context context, boolean isLogin) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(PREF_IS_LOGIN, isLogin);
        editor.apply();
    }

    public static boolean isPrefIsUserLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(PREF_IS_LOGIN, false);
    }


    public static void setUserData(Context context, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_LOGIN_DATA, data);
        editor.apply();

        refresh(context);
    }

    public static void saveTopScorerData(Context context, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_SAVE_TOP_SCORER_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_TOP_SCORER_DATA, data);
        editor.apply();

    }

    public static String getTopScorerData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_SAVE_TOP_SCORER_DATA, Context.MODE_PRIVATE);
        return sp.getString(PREF_TOP_SCORER_DATA, "");

    }

    public static void saveCurrencyList(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_CURRENCY, token);
        editor.apply();
    }

    public static String getCurrencyList(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sp.getString(PREF_CURRENCY, "{}");
    }

    public static void saveDeviceToken(Context context, String token) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    public static String getDeviceToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sp.getString(PREF_TOKEN, "");
    }


    public static void saveNotificationData(Context context, String data) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_SAVE_NOIFICATION_COUNT_DATA, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_NOTFICATION_DATA, data);
        editor.apply();

    }

    public static String getNotificationData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_SAVE_NOIFICATION_COUNT_DATA, Context.MODE_PRIVATE);
        return sp.getString(PREF_NOTFICATION_DATA, "");

    }

    public static @Nullable
    long getStartTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getLong(PREF_START_TIME, 0);
//        String userRow = sp.getString(PREF_START_TIME_SESSION_ID, 0);
    }

    public static @Nullable
    int getStartTimeSesionId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        return sp.getInt(PREF_START_TIME_SESSION_ID, 0);
//        String userRow = sp.getString(PREF_START_TIME_SESSION_ID, 0);
    }

    public static @Nullable
    FSUsersModel getRegisterUser(Context context) {
        if (registrationModel == null) {
            refresh(context);
        }
//        if (registrationModel == null) {
//            registrationModel = new CurrentUserModel();
//        }
        return registrationModel;
    }


    public static void loginUser(Context context, FSUsersModel user) {
        Gson gson = new Gson();
        String string = gson.toJson(user);

        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(PREF_LOGIN_DATA, string).apply();

        registrationModel = user;
    }


    private static void refresh(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        String userRow = sp.getString(PREF_LOGIN_DATA, null);
        Gson gson = new Gson();
        Type type = new TypeToken<FSUsersModel>() {
        }.getType();
        registrationModel = gson.fromJson(userRow, type);
    }

    public static boolean isUserLogin(Context context) {
        FSUsersModel loginUser = PreferenceUtils.getRegisterUser(context);
        return loginUser != null;
    }

    public static void logout(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        registrationModel = null;
    }

    public static void getDeleteTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(PREF_START_TIME).remove(PREF_START_TIME_SESSION_ID).apply();
    }


}
