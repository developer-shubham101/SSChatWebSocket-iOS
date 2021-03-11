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
import in.newdevpoint.ssnodejschat.model.RoomNewResponseModel;
import in.newdevpoint.ssnodejschat.model.RoomResponseModel;
import in.newdevpoint.ssnodejschat.observer.ResponseType;
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

        roomListBinding.openAllUsers.setOnClickListener(v -> startActivity(new Intent(RoomListActivity.this, AllUsersListActivity.class)));
        roomListBinding.homNotification.setOnClickListener(v -> startActivity(new Intent(this, UpdateProfileActivity.class)));


        joinCommand();
    }


    private void initRecycler() {

        adapter = new RoomListAdapter(this, new RoomListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(FSRoomModel item) {

                Intent intent = new Intent(RoomListActivity.this, ChatActivity.class);
                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_IS_GROUP, item.isGroup());
                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_GROUP_DETAILS, item.getGroupDetails());
                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_ROOM_ID, item.getRoomId());
                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_SENDER_DETAILS, item.getSenderUserDetail());

                startActivity(intent);
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
    public void onWebSocketResponse(String response, String type, int statusCode, String message) {
        try {
            runOnUiThread(() -> {
                System.out.println("received message: " + response);

                Gson gson = new Gson();


                if (ResponseType.RESPONSE_TYPE_ROOM.equalsTo(type)) {


                    if (statusCode == 200) {

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
                        Toast.makeText(RoomListActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_MODIFIED.equalsTo(type)) {
                    if (statusCode == 200) {
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
                        Toast.makeText(RoomListActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                } else if (ResponseType.RESPONSE_TYPE_CREATE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {

                        Type type1 = new TypeToken<ResponseModel<RoomNewResponseModel>>() {
                        }.getType();
                        ResponseModel<RoomNewResponseModel> roomResponseModelResponseModel = gson.fromJson(response, type1);

                        HashMap<String, FSUsersModel> tmpUserList = roomResponseModelResponseModel.getData().getUserListMap();
                        for (String key : tmpUserList.keySet()) {
                            UserDetails.chatUsers.put(key, tmpUserList.get(key));
                        }

                        FSRoomModel element = roomResponseModelResponseModel.getData().getNewRoom();

                        for (String userId : element.getUserList()) {
                            if (!userId.equals(UserDetails.myDetail.getId())) {
                                element.setSenderUserDetail(UserDetails.chatUsers.get(userId));
                                break;
                            }
                        }

                        adapter.addOrUpdate(element);

                    } else if (ResponseType.RESPONSE_TYPE_USER_MODIFIED.equalsTo(type)) {
                        Log.d(TAG, "received message: " + response);

                        Type type1 = new TypeToken<ResponseModel<FSUsersModel>>() {
                        }.getType();

                        ResponseModel<FSUsersModel> fsUsersModelResponseModel = new Gson().fromJson(response, type1);

                        adapter.updateUserElement(fsUsersModelResponseModel.getData());



                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "onWebSocketResponse: " + type);
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


    @Override
    public ResponseType[] registerFor() {
        return new ResponseType[]{
                ResponseType.RESPONSE_TYPE_ROOM,
                ResponseType.RESPONSE_TYPE_ROOM_MODIFIED,
                ResponseType.RESPONSE_TYPE_CREATE_ROOM,
                ResponseType.RESPONSE_TYPE_USER_MODIFIED

        };
    }
}