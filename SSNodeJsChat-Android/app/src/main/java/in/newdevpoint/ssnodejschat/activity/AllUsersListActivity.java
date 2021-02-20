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
import java.util.ArrayList;
import java.util.HashMap;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.UsersListAdapter;
import in.newdevpoint.ssnodejschat.databinding.ActivityAllUsersListBinding;
import in.newdevpoint.ssnodejschat.model.FSRoomModel;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.RoomNewResponseModel;
import in.newdevpoint.ssnodejschat.observer.ResponseType;
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

        roomListBinding.switchCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.setGroupMode(roomListBinding.switchCreateGroup.isChecked());
                adapter.notifyDataSetChanged();

                roomListBinding.userListGroupName.setEnabled(roomListBinding.switchCreateGroup.isChecked());
                roomListBinding.homNotification.setEnabled(roomListBinding.switchCreateGroup.isChecked());

            }
        });

        roomListBinding.homNotification.setOnClickListener(v -> createGroupCommand());


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

    private void createGroupCommand() {
        ArrayList<FSUsersModel> usersLists = adapter.getAllList();
        ArrayList<FSUsersModel> selectedUsersLists = new ArrayList<>();

        for (FSUsersModel fsUsersModel : usersLists) {
            if (fsUsersModel.isChecked())
                selectedUsersLists.add(fsUsersModel);
        }
        if (selectedUsersLists.size() <= 1) {
            Toast.makeText(this, "Please Select More People", Toast.LENGTH_SHORT).show();
            return;
        } else if (roomListBinding.userListGroupName.getText().equals("")) {
            Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray usersList = new JSONArray();
            for (FSUsersModel fsUsersModel : selectedUsersLists) {
                usersList.put(fsUsersModel.getId());
            }

            usersList.put(UserDetails.myDetail.getId());
            jsonObject.put("userList", usersList);
            jsonObject.put("type", "createRoom");

            jsonObject.put("room_type", "group");
            JSONObject groupDetails = new JSONObject();
            groupDetails.put("group_name", roomListBinding.userListGroupName.getText());
            groupDetails.put("about_group", "This is Just a Sample Group");
            jsonObject.put("group_details", groupDetails);


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
    public void onWebSocketResponse(String response, String type, int statusCode, String message) {
        try {
            runOnUiThread(() -> {
                Log.d(TAG, "received message: " + response);

                Gson gson = new Gson();


                if (ResponseType.RESPONSE_TYPE_USERS.equalsTo(type)) {

                    if (statusCode == 200) {

                        Type typeUserList = new TypeToken<ResponseModel<ArrayList<FSUsersModel>>>() {
                        }.getType();

                        ResponseModel<ArrayList<FSUsersModel>> arrayListResponseModel = gson.fromJson(response, typeUserList);

                        ArrayList<FSUsersModel> fsUsersModels = new ArrayList<>();

                        for (FSUsersModel element : arrayListResponseModel.getData()) {
                            if (!element.getId().equals(UserDetails.myDetail.getId())) {
                                fsUsersModels.add(element);
                            }

                        }


                        adapter.addAll(fsUsersModels);
                    } else {
                        Toast.makeText(AllUsersListActivity.this, message, Toast.LENGTH_SHORT).show();
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


                        Intent intent = new Intent(this, ChatActivity.class);
                        intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_IS_GROUP, element.isGroup());

                        intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_ROOM_ID, element.getRoomId());
                        intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_SENDER_DETAILS, element.getSenderUserDetail());

                        startActivity(intent);


//                    startActivity(new Intent(RoomListActivity.this, RoomListActivity.class));
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
        return AllUsersListActivity.class.getName();
    }

    @Override
    public ResponseType[] registerFor() {
        return new ResponseType[]{
                ResponseType.RESPONSE_TYPE_USERS,
                ResponseType.RESPONSE_TYPE_CREATE_ROOM
        };
    }

}