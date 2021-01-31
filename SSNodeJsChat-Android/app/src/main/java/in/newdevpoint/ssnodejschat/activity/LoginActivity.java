package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
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
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.utility.Validate;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class LoginActivity extends AppCompatActivity {
    private static final String EMAIL = "email";
    private static final int NORMAL_CLOSURE_STATUS = 10000;

    private final String CHAT_URL = APIClient.BASE_URL_WEB_SOCKET + "/login";

    private ActivityLoginBinding loginBinding;
//    private Waiting mWaitingDialog;

    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
//        mWaitingDialog = new Waiting(LoginActivity.this);


        loginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLoginApi();
            }
        });


        start();
    }

    private void start() {

        // WebSocket
        Request request = new Request.Builder().url(CHAT_URL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
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
            jsonObject.put("userId", 1);
            jsonObject.put("userName", loginBinding.loginUserEmail.getText().toString());
            jsonObject.put("password", loginBinding.loginPassword.getText().toString());
            jsonObject.put("type", "login");
//            mWaitingDialog.show();

            webSocket.send(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void joinCommand() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", "join");
//			jsonObject.put("room", roomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonObject.toString());
    }


    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            joinCommand();
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            System.out.println("received message: " + text);
            runOnUiThread(() -> {

                Gson gson = new Gson();
                Type type = new TypeToken<ResponseModel<FSUsersModel>>() {
                }.getType();


                ResponseModel<FSUsersModel> obj = gson.fromJson(text, type);


                if (obj.getStatus_code() == 200) {
                    UserDetails.myDetail = obj.getData();
                    startActivity(new Intent(LoginActivity.this, RoomListActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("onMessage: " + bytes.hex());
            Toast.makeText(LoginActivity.this, "onMessage:" + bytes.hex(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            System.out.println("onClosing: " + code + " / " + reason);
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            System.out.println("onFailure: " + t.getMessage());
        }
    }

}
