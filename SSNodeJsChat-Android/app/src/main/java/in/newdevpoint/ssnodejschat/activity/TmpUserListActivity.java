package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.TmpUserListAdapter;
import in.newdevpoint.ssnodejschat.databinding.ActivityTmpListBinding;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.TmpUserModel;
import in.newdevpoint.ssnodejschat.observer.ResponseType;
import in.newdevpoint.ssnodejschat.observer.WebSocketObserver;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.PreferenceUtils;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;

public class TmpUserListActivity extends AppCompatActivity implements WebSocketObserver {
    private static final String TAG = "RoomListActivity:";
    private final TmpUserModel[] listOfTmpUsers = {
            new TmpUserModel("anil@yopmail.com", "123456", "1", "Anil"),
            new TmpUserModel("amit@yopmail.com", "123456", "2", "Amit"),
            new TmpUserModel("shubham@yopmail.com", "123456", "3", "Shubham"),
            new TmpUserModel("ali@yopmail.com", "123456", "4", "Ali"),
            new TmpUserModel("samreen@yopmail.com", "123456", "5", "Samreen")
    };
    private TmpUserListAdapter adapter;
    private ActivityTmpListBinding activityTmpListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTmpListBinding = DataBindingUtil.setContentView(this, R.layout.activity_tmp_list);

        WebSocketSingleton.getInstant().register(this);

        initRecycler();

    }


    private void initRecycler() {

        adapter = new TmpUserListAdapter(this, new TmpUserListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(TmpUserModel item) {
                fetchLoginApi(item);
            }

        });


        activityTmpListBinding.usersList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityTmpListBinding.usersList.setLayoutManager(mLayoutManager);
        activityTmpListBinding.usersList.setAdapter(adapter);


        adapter.addAll(new ArrayList<>(Arrays.asList(listOfTmpUsers)));

    }

    public void fetchLoginApi(TmpUserModel tmpUserModel) {

//        mWaitingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", tmpUserModel.getUserId());
            jsonObject.put("userName", tmpUserModel.getEmail());
            jsonObject.put("firstName", tmpUserModel.getName());
            jsonObject.put("password", "123456");
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

//                        PreferenceUtils.loginUser(TmpUserListActivity.this, fsUsersModelResponseModel.getData());
                        startActivity(new Intent(TmpUserListActivity.this, RoomListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(TmpUserListActivity.this, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
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
        return TmpUserListActivity.class.getName();
    }


    @Override
    public ResponseType[] registerFor() {
        return new ResponseType[]{
                ResponseType.RESPONSE_TYPE_LOGIN,
                ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE,
        };
    }
}