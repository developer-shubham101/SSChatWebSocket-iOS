package `in`.newdevpoint.ssnodejschat.utility

import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceUtils {
    private val sharedPreferences1: SharedPreferences? = null

    companion object {
        const val SHARED_PREF = "ah_firebase"
        private const val PREF_LOGIN_DATA = "PREF_LOGIN_DATA"
        private const val PREF_TOP_SCORER_DATA = "PREF_TOP_SCORER_DATA"
        private const val PREF_NOTFICATION_DATA = "PREF_NOTFICATION_DATA"
        private const val PREF_TOKEN = "PREF_TOKEN"
        private const val PREF_IS_LOGIN = "is_login"
        private const val USER_PREFS_NAME = "com.arka.fahmni.pre.user"
        private const val PREF_SAVE_TOP_SCORER_DATA = "top_scorer_data"
        private const val PREF_SAVE_NOIFICATION_COUNT_DATA = "notification_count_data"
        private const val PREF_START_TIME = "PREF_START_TIME"
        private const val PREF_START_TIME_SESSION_ID = "PREF_START_TIME_SESSION_ID"
        private const val PREF_LANGUAGE = "PREF_LANGUAGE"
        private const val PREF_CURRENCY = "PREF_CURRENCY"
        private const val PREF_IS_USER_LOGIN = false
        private var registrationModel: FSUsersModel? = null
        fun setPrefIsUserLogin(context: Context, isLogin: Boolean) {
            val editor = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).edit()
            editor.putBoolean(PREF_IS_LOGIN, isLogin)
            editor.apply()
        }

        fun isPrefIsUserLogin(context: Context): Boolean {
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            return sp.getBoolean(PREF_IS_LOGIN, false)
        }

        fun setUserData(context: Context, data: String?) {
            val editor = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_LOGIN_DATA, data)
            editor.apply()
            refresh(context)
        }

        fun saveTopScorerData(context: Context, data: String?) {
            val editor = context.getSharedPreferences(PREF_SAVE_TOP_SCORER_DATA, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_TOP_SCORER_DATA, data)
            editor.apply()
        }

        fun getTopScorerData(context: Context): String? {
            val sp = context.getSharedPreferences(PREF_SAVE_TOP_SCORER_DATA, Context.MODE_PRIVATE)
            return sp.getString(PREF_TOP_SCORER_DATA, "")
        }

        fun saveCurrencyList(context: Context, token: String?) {
            val editor = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_CURRENCY, token)
            editor.apply()
        }

        fun getCurrencyList(context: Context): String? {
            val sp = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return sp.getString(PREF_CURRENCY, "{}")
        }

        fun saveDeviceToken(context: Context, token: String?) {
            val editor = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_TOKEN, token)
            editor.apply()
        }

        fun getDeviceToken(context: Context): String? {
            val sp = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return sp.getString(PREF_TOKEN, "")
        }

        fun saveNotificationData(context: Context, data: String?) {
            val editor = context.getSharedPreferences(PREF_SAVE_NOIFICATION_COUNT_DATA, Context.MODE_PRIVATE).edit()
            editor.putString(PREF_NOTFICATION_DATA, data)
            editor.apply()
        }

        fun getNotificationData(context: Context): String? {
            val sp = context.getSharedPreferences(PREF_SAVE_NOIFICATION_COUNT_DATA, Context.MODE_PRIVATE)
            return sp.getString(PREF_NOTFICATION_DATA, "")
        }

        fun getStartTime(context: Context): Long {
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            return sp.getLong(PREF_START_TIME, 0)
            //        String userRow = sp.getString(PREF_START_TIME_SESSION_ID, 0);
        }

        fun getStartTimeSesionId(context: Context): Int {
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            return sp.getInt(PREF_START_TIME_SESSION_ID, 0)
            //        String userRow = sp.getString(PREF_START_TIME_SESSION_ID, 0);
        }

        fun getRegisterUser(context: Context?): FSUsersModel {
            if (registrationModel == null) {
                refresh(context)
            }
            //        if (registrationModel == null) {
//            registrationModel = new CurrentUserModel();
//        }
            return registrationModel!!
        }

        fun loginUser(context: Context, user: FSUsersModel?) {
            val gson = Gson()
            val string = gson.toJson(user)
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            sp.edit().putString(PREF_LOGIN_DATA, string).apply()
            registrationModel = user
        }

        private fun refresh(context: Context?) {
            val sp = context!!.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            val userRow = sp.getString(PREF_LOGIN_DATA, null)
            val gson = Gson()
            val type = object : TypeToken<FSUsersModel?>() {}.type
            registrationModel = gson.fromJson<FSUsersModel>(userRow, type)
        }

        fun isUserLogin(context: Context?): Boolean {
            val loginUser: FSUsersModel? = getRegisterUser(context)
            return loginUser != null
        }

        fun logout(context: Context) {
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            sp.edit().clear().apply()
            registrationModel = null
        }

        fun getDeleteTime(context: Context) {
            val sp = context.getSharedPreferences(USER_PREFS_NAME, Context.MODE_PRIVATE)
            sp.edit().remove(PREF_START_TIME).remove(PREF_START_TIME_SESSION_ID).apply()
        }
    }
}