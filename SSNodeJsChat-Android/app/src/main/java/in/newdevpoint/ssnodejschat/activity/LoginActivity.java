package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.ActivityLoginBinding;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.TmpUserModel;
import in.newdevpoint.ssnodejschat.observer.ResponseType;
import in.newdevpoint.ssnodejschat.observer.WebSocketObserver;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.PreferenceUtils;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.utility.Validate;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;



public class LoginActivity extends AppCompatActivity implements WebSocketObserver {
    private static final String EMAIL = "email";
    private static final String TAG = "LoginActivity";
    private final TmpUserModel[] listOfTmpUsers = {
            new TmpUserModel("anil@yopmail.com", "123456", "1", "Anil"),
            new TmpUserModel("amit@yopmail.com", "123456", "2", "Amit"),
            new TmpUserModel("shubham@yopmail.com", "123456", "3", "Shubham"),
            new TmpUserModel("ali@yopmail.com", "123456", "4", "Ali"),
            new TmpUserModel("samreen@yopmail.com", "123456", "5", "Samreen")
    };
    private final TmpUserModel tmpUserModel = listOfTmpUsers[2];
    //    private Waiting mWaitingDialog;
    private ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        mWaitingDialog = new Waiting(LoginActivity.this);

        WebSocketSingleton.getInstant().register(this);





        loginBinding.loginUserEmail.setText(tmpUserModel.getEmail());


        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLoginApi();
            }
        });




    }

    public void fetchLoginApi() {
        if (loginBinding.loginUserEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
            return;
        } else if (loginBinding.loginPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.validation_password), Toast.LENGTH_SHORT).show();
            return;
        } else if (!Validate.isEmail(loginBinding.loginUserEmail.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.validation_valid_email), Toast.LENGTH_SHORT).show();
            return;
        }
//        mWaitingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", tmpUserModel.getUserId());
            jsonObject.put("userName", loginBinding.loginUserEmail.getText().toString());
            jsonObject.put("firstName", tmpUserModel.getName());
            jsonObject.put("password", loginBinding.loginPassword.getText().toString());
            jsonObject.put("fcm_token", PreferenceUtils.getDeviceToken(this));
            jsonObject.put("type", "loginOrCreate");
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_LOGIN);
//            mWaitingDialog.show();

            WebSocketSingleton.getInstant().sendMessage(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebSocketResponse(String response, String type, int statusCode, String message) {
        try {
            runOnUiThread(() -> {
                Gson gson = new Gson();


                if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) || ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {


                    Type type1 = new TypeToken<ResponseModel<FSUsersModel>>() {
                    }.getType();

                    ResponseModel<FSUsersModel> fsUsersModelResponseModel = gson.fromJson(response, type1);
                    if (fsUsersModelResponseModel.getStatus_code() == 200) {
                        UserDetails.getInstant().setMyDetail(fsUsersModelResponseModel.getData());

                        PreferenceUtils.loginUser(LoginActivity.this, fsUsersModelResponseModel.getData());
                        startActivity(new Intent(LoginActivity.this, RoomListActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d(TAG, "onWebSocketResponse: " + type);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getActivityName() {
        return LoginActivity.class.getName();
    }

    @Override
    public ResponseType[] registerFor() {
        return new ResponseType[]{
                ResponseType.RESPONSE_TYPE_LOGIN,
                ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE,
        };
    }
}
