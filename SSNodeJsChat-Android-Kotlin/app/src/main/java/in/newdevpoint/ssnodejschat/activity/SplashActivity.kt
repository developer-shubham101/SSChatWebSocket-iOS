package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.activity.SplashActivity
import `in`.newdevpoint.ssnodejschat.observer.WebSocketSingleton
import `in`.newdevpoint.ssnodejschat.utility.PreferenceUtils
import `in`.newdevpoint.ssnodejschat.utility.UserDetails
import `in`.newdevpoint.ssnodejschat.webService.APIClient.KeyConstant
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (intent != null) {
            val intent = intent
            val handler = Handler()
            handler.postDelayed({
                if (PreferenceUtils.Companion.isUserLogin(this)) {
                    UserDetails.myDetail = PreferenceUtils.Companion.getRegisterUser(this)
                    startActivity(Intent(this@SplashActivity, RoomListActivity::class.java))
                    val jsonObject = JSONObject()
                    try {
                        Toast.makeText(this, PreferenceUtils.getRegisterUser(this).id + " User Id", Toast.LENGTH_SHORT).show()
                        jsonObject.put("user_id", PreferenceUtils.getRegisterUser(this).id)
                        jsonObject.put("type", "create")
                        jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_CREATE_CONNECTION)
                        //            mWaitingDialog.show();
                        WebSocketSingleton.getInstant()!!.sendMessage(jsonObject)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }


//                    if (PreferenceUtils.getRegisterUser(SplashActivity.this) != null) {
//                        boolean isLogin = PreferenceUtils.isPrefIsUserLogin(SplashActivity.this);
//                        int userStatus = PreferenceUtils.getRegisterUser(SplashActivity.this).getStatus();
//                        String Token = PreferenceUtils.getRegisterUser(SplashActivity.this).getAccess_token();
//                        Log.d(TAG, "onCreate: " + isLogin);
//                        Log.d(TAG, "onCreate: " + Token);
//
//                        if (PreferenceUtils.isPrefIsUserLogin(SplashActivity.this) && PreferenceUtils.getRegisterUser(SplashActivity.this).getStatus() == 1) {
//                            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
//                        } else if (PreferenceUtils.isPrefIsUserLogin(SplashActivity.this) && PreferenceUtils.getRegisterUser(SplashActivity.this).getStatus() == 0) {
//                            Intent homeActivity = new Intent(SplashActivity.this, RegistrationActivity.class);
//                            startActivity(homeActivity);
//                        } else {
//                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        }
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                    }
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish()
            }, SPLASH_DURATION)
        }
    }

    companion object {
        private const val SPLASH_DURATION: Long = 3000
        private const val TAG = "SplashActivity:"
    }
}