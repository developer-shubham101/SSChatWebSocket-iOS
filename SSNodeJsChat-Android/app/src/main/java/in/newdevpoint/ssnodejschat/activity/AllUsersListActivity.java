package in.newdevpoint.ssnodejschat.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.UsersListAdapter;
import in.newdevpoint.ssnodejschat.databinding.ActivityAllUsersListBinding;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.observer.WebSocketObserver;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;

public class AllUsersListActivity extends AppCompatActivity implements WebSocketObserver {
    public static final String TAG = "UsersActivity:";

    private UsersListAdapter adapter;
    private ActivityAllUsersListBinding roomListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        roomListBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_users_list);
        WebSocketSingleton.getInstant().register(this);


        initRecycler();


        joinCommand();

    }


    private void initRecycler() {

        adapter = new UsersListAdapter(new UsersListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(FSUsersModel item) {

                createRoomCommand(item);
//                HashMap<String, FSUsersModel> chatUsersMap = new HashMap<>();
////                chatUsersMap.put(currentUser.getUid(), myDetail);
//                chatUsersMap.put(item.getSenderUserDetail().getId(), item.getSenderUserDetail());
//
//                UserDetails.roomId = item.getRoomId();
//                UserDetails.chatUsers = chatUsersMap;
//                UserDetails.myDetail = myDetail;
//                startActivity(new Intent(AllUsersListActivity.this, ChatActivity.class));
            }


        });


        roomListBinding.usersList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        roomListBinding.usersList.setLayoutManager(mLayoutManager);
        roomListBinding.usersList.setAdapter(adapter);


    }

    private void createRoomCommand(FSUsersModel connectWith) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray usersList = new JSONArray();
            usersList.put(connectWith.getId());
            usersList.put(UserDetails.myDetail.getId());
            jsonObject.put("userList", usersList);
            jsonObject.put("type", "createRoom");

            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_ROOM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketSingleton.getInstant().sendMessage(jsonObject);
    }

    private void joinCommand() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "allUsers");

            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_USERS);
//			jsonObject.put("room", roomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSocketSingleton.getInstant().sendMessage(jsonObject);
    }

    @Override
    public void onWebSocketResponse(String response) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("received message: " + response);

                    Gson gson = new Gson();
                    Type type = new TypeToken<ResponseModel<Object>>() {
                    }.getType();

                    ResponseModel<Object> objectResponseModel = gson.fromJson(response, type);

                    if (objectResponseModel.getType().equals(APIClient.KeyConstant.RESPONSE_TYPE_USERS)) {

                        if (objectResponseModel.getStatus_code() == 200) {

                            Type typeUserList = new TypeToken<ResponseModel<ArrayList<FSUsersModel>>>() {
                            }.getType();

                            ResponseModel<ArrayList<FSUsersModel>> arrayListResponseModel = gson.fromJson(response, typeUserList);


                            adapter.addAll(arrayListResponseModel.getData());
                        } else {
                            Toast.makeText(AllUsersListActivity.this, objectResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Log.d(TAG, "onWebSocketResponse: " + objectResponseModel.getType());
                    }
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getActivityName() {
        return AllUsersListActivity.class.getName();
    }

}