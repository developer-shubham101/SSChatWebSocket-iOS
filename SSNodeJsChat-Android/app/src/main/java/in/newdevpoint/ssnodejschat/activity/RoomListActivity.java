package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.RoomListAdapter;
import in.newdevpoint.ssnodejschat.databinding.ActivityRoomListBinding;
import in.newdevpoint.ssnodejschat.model.FSRoomModel;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.RoomResponseModel;
import in.newdevpoint.ssnodejschat.observer.WebSocketObserver;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;

public class RoomListActivity extends AppCompatActivity implements WebSocketObserver {
    private static final String TAG = "RoomListActivity:";


    private final HashMap<String, FSUsersModel> alterNativeUserList = new HashMap<>();

    private RoomListAdapter adapter;

    private ActivityRoomListBinding roomListBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roomListBinding = DataBindingUtil.setContentView(this, R.layout.activity_room_list);

        WebSocketSingleton.getInstant().register(this);

        initRecycler();

        roomListBinding.openAllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RoomListActivity.this, AllUsersListActivity.class));
            }
        });


        joinCommand();
    }


    private void initRecycler() {

        adapter = new RoomListAdapter(new RoomListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(FSRoomModel item) {

//                HashMap<String, FSUsersModel> chatUsersMap = new HashMap<>();
////                chatUsersMap.put(currentUser.getUid(), myDetail);
//                chatUsersMap.put(item.getUserList(), item.getSenderUserDetail());
//                Log.i(TAG, "onClick: " + item.getRoomId());
                UserDetails.roomId = item.getRoomId();
//                UserDetails.chatUsers = chatUsersMap;
//                UserDetails.myDetail = myDetail;
                startActivity(new Intent(RoomListActivity.this, ChatActivity.class));
            }

        });


        roomListBinding.usersList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        roomListBinding.usersList.setLayoutManager(mLayoutManager);
        roomListBinding.usersList.setAdapter(adapter);


    }

    private void joinCommand() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray userList = new JSONArray();
            userList.put(UserDetails.myDetail.getId());
            jsonObject.put("type", "allRooms");
            jsonObject.put("userList", userList);
            jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_ROOM);
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


                    if (objectResponseModel.getType().equals(APIClient.KeyConstant.RESPONSE_TYPE_ROOM)) {


                        if (objectResponseModel.getStatus_code() == 200) {

                            Type type1 = new TypeToken<ResponseModel<RoomResponseModel>>() {
                            }.getType();
                            ResponseModel<RoomResponseModel> roomResponseModelResponseModel = gson.fromJson(response, type1);
                            UserDetails.chatUsers = roomResponseModelResponseModel.getData().getUserListMap();
                            for (FSRoomModel element : roomResponseModelResponseModel.getData().getRoomList()) {

                                for (String userId : element.getUserList()) {
                                    if (!userId.equals(UserDetails.myDetail.getId())) {
                                        element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                        break;
                                    }
                                }
                            }
                            adapter.addAll(roomResponseModelResponseModel.getData().getRoomList());
//                    startActivity(new Intent(RoomListActivity.this, RoomListActivity.class));
                        } else {
                            Toast.makeText(RoomListActivity.this, objectResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (objectResponseModel.getType().equals(APIClient.KeyConstant.RESPONSE_TYPE_ROOM_MODIFIED)) {
                        if (objectResponseModel.getStatus_code() == 200) {
                            Type type1 = new TypeToken<ResponseModel<FSRoomModel>>() {
                            }.getType();
                            ResponseModel<FSRoomModel> roomResponseModelResponseModel = gson.fromJson(response, type1);

                            for (String userId : roomResponseModelResponseModel.getData().getUserList()) {
                                if (!userId.equals(UserDetails.myDetail.getId())) {
                                    roomResponseModelResponseModel.getData().setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                    break;
                                }
                            }

                            adapter.updateElement(roomResponseModelResponseModel.getData());

                        } else {
                            Toast.makeText(RoomListActivity.this, objectResponseModel.getMessage(), Toast.LENGTH_SHORT).show();

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
        return RoomListActivity.class.getName();
    }

}