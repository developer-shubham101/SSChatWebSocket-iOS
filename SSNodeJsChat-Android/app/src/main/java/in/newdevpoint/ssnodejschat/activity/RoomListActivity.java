package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.RoomListAdapter;
import in.newdevpoint.ssnodejschat.model.FSChatModel;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class RoomListActivity extends AppCompatActivity {
    private static final String TAG = "UsersActivity:";
    //    private static final String TAG = "UsersActivity";
    private static final boolean BACK_PRESSED = false;
    private static final int NORMAL_CLOSURE_STATUS = 10000;
    private final HashMap<String, FSUsersModel> alterNativeUserList = new HashMap<>();
    private final String CHAT_URL = APIClient.BASE_URL_WEB_SOCKET + "/users";
    private RecyclerView recyclerView;
    private TextView noUsersText;
    //    -----------------------
    private RoomListAdapter adapter;
    private FSUsersModel myDetail;
    private WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);


        recyclerView = findViewById(R.id.usersList);
        noUsersText = findViewById(R.id.noUsersText);


        start();
        initRecycler();

    }


    private void start() {

        // WebSocket
        Request request = new Request.Builder().url(CHAT_URL).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }


    private void initRecycler() {

        adapter = new RoomListAdapter(new RoomListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(FSChatModel item) {

                HashMap<String, FSUsersModel> chatUsersMap = new HashMap<>();
//                chatUsersMap.put(currentUser.getUid(), myDetail);
                chatUsersMap.put(item.getSenderUserDetail().getId(), item.getSenderUserDetail());

                UserDetails.roomId = item.getRoomId();
                UserDetails.chatUsers = chatUsersMap;
                UserDetails.myDetail = myDetail;
                startActivity(new Intent(RoomListActivity.this, ChatActivity.class));
            }

        });


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


    }

    /*

        @Override
        protected void onStart() {
            super.onStart();


            ArrayList<Map<String, Object>> chatRoomList = new ArrayList<>();
            ArrayList<String> alternateuserIdList = new ArrayList<>();

            db.collection("threads").whereEqualTo("users." + currentUser.getUid(), true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        for (DocumentChange dc : document.getDocumentChanges()) {
                            Log.d(TAG, "New city: " + dc.getDocument().getData());

                            Map<String, Object> threadData = dc.getDocument().getData();
                            threadData.put("docId", dc.getDocument().getId());
                            chatRoomList.add(threadData);

                            Map<String, Boolean> usersListMap = (Map<String, Boolean>) threadData.get("users");
                            Log.d(TAG, "usersListMap: " + usersListMap);

                            for (String key : usersListMap.keySet()) {
                                if (!key.equals(currentUser.getUid())) {
                                    alternateuserIdList.add(key);
                                }
                            }

                        }
                        alternateuserIdList.add(currentUser.getUid());

                        db.collection("users").whereIn("userID", alternateuserIdList).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot document = task.getResult();
                                    for (DocumentChange dc : document.getDocumentChanges()) {
                                        Log.d(TAG, "User Data: " + dc.getDocument().getData());

                                        Map<String, Object> usersData = dc.getDocument().getData();

                                        if (usersData.get("userID").equals(currentUser.getUid())) {
                                            myDetail = new FSUsersModel(usersData);
                                        } else {
                                            FSUsersModel otherUserObj = new FSUsersModel(usersData);
                                            alterNativeUserList.put(otherUserObj.getId(), otherUserObj);
                                        }

                                    }

                                    ArrayList<FSChatModel> chatList = new ArrayList<>();
                                    for (Map<String, Object> element : chatRoomList) {

                                        Map<String, Boolean> usersListMap = (Map<String, Boolean>) element.get("users");
                                        String otherUserId = "";
                                        for (String key : usersListMap.keySet()) {
                                            if (!key.equals(currentUser.getUid())) {
                                                otherUserId = key;
                                                break;
                                            }
                                        }


                                        chatList.add(new FSChatModel(element, alterNativeUserList.get(otherUserId)));
                                    }

                                    adapter.addAll(chatList);
    //								Iterator it = chatRoomList.iterator();
    //
    //								while (it.hasNext()) {
    //									Map.Entry pair = (Map.Entry)it.next();
    //									System.out.println(pair.getKey() + " = " + pair.getValue());
    //									it.remove(); // avoids a ConcurrentModificationException
    //								}
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            db.collection("threads").whereEqualTo("users." + currentUser.getUid(), true).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }


                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {

                            } else if (dc.getType() == DocumentChange.Type.MODIFIED) {
                                Log.d(TAG, "New city: " + dc.getDocument().getData());

                                Map<String, Object> modifyData = dc.getDocument().getData();

                                for (FSChatModel element : adapter.getAllList()) {
                                    if (element.getRoomId().equals(dc.getDocument().getId())) {
                                        try {
                                            HashMap<String, Object> chatUserData = (HashMap<String, Object>) modifyData.get(currentUser.getUid());
                                            if (chatUserData != null) {
                                                long newMessage = (long) chatUserData.get("newMessage");
                                                element.setNewMessage(newMessage);
                                            }
                                        } catch (ClassCastException ignored) {
                                            ignored.printStackTrace();
                                        }
                                        element.setLastMessage((String) modifyData.get("lastMessage"));
                                        break;
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            } else if (dc.getType() == DocumentChange.Type.REMOVED) {

                            }
                        }
                        alternateuserIdList.add(currentUser.getUid());

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });

        }*/
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
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(text);

                        if (jsonObject.has("message")) {
                            ChattingModel chattingModel = ChattingModel.getModel(jsonObject, PreferenceUtils.getLoginUser(ChatActivity.this).getId());
                            chattingAdapter.updateList(chattingModel);
//                            chatRecyclerView.add
                            binding.chatRecyclerView.scrollToPosition(chattingAdapter.items.size() - 1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });*/
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("onMessage: " + bytes.hex());
            Toast.makeText(RoomListActivity.this, "onMessage:" + bytes.hex(), Toast.LENGTH_SHORT).show();
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