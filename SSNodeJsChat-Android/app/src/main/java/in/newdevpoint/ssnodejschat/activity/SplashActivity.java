package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.PreferenceUtils;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;


public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION = 3000;
    private static final String TAG = "SplashActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getIntent() != null) {
            Intent intent = getIntent();

            Handler handler = new Handler();
            handler.postDelayed(() -> {

                if (PreferenceUtils.isUserLogin(this)) {
                    UserDetails.getInstant().setMyDetail(PreferenceUtils.getRegisterUser(this));
                    startActivity(new Intent(SplashActivity.this, RoomListActivity.class));


                    JSONObject jsonObject = new JSONObject();
                    try {
                        Toast.makeText(this, PreferenceUtils.getRegisterUser(this).getId() + " User Id", Toast.LENGTH_SHORT).show();
                        jsonObject.put("user_id", PreferenceUtils.getRegisterUser(this).getId());

                        jsonObject.put("type", "create");
                        jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_CREATE_CONNECTION);
//            mWaitingDialog.show();

                        WebSocketSingleton.getInstant().sendMessage(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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
                finish();
            }, SPLASH_DURATION);

        }
    }
}
