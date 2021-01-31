package in.newdevpoint.ssnodejschat.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.UsersListAdapter;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class AllUsersListActivity extends AppCompatActivity {
    private static final String TAG = "UsersActivity:";
    //    private static final String TAG = "UsersActivity";
    private static final boolean BACK_PRESSED = false;
    private static final int NORMAL_CLOSURE_STATUS = 10000;
    private final HashMap<String, FSUsersModel> alterNativeUserList = new HashMap<>();
    private final String CHAT_URL = APIClient.BASE_URL_WEB_SOCKET + "/users";
    private RecyclerView recyclerView;
    private TextView noUsersText;
    //    -----------------------
    private UsersListAdapter adapter;
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

        adapter = new UsersListAdapter(new UsersListAdapter.CallBackForSinglePost() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClick(FSUsersModel item) {

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


        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);


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
                Type type = new TypeToken<ResponseModel<ArrayList<FSUsersModel>>>() {
                }.getType();


                ResponseModel<ArrayList<FSUsersModel>> obj = gson.fromJson(text, type);


                if (obj.getStatus_code() == 200) {

                    adapter.addAll(obj.getData());


                } else {
                    Toast.makeText(AllUsersListActivity.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("onMessage: " + bytes.hex());
            Toast.makeText(AllUsersListActivity.this, "onMessage:" + bytes.hex(), Toast.LENGTH_SHORT).show();
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