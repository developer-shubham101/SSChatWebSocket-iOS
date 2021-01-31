package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import in.newdevpoint.ssnodejschat.R;


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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
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
                }
            }, SPLASH_DURATION);

        }
    }
}
