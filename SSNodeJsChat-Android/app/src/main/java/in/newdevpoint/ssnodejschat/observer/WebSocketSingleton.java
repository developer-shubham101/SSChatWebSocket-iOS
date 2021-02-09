package in.newdevpoint.ssnodejschat.observer;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.activity.AllUsersListActivity;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

// Uses the Subject interface to update all Observers

public class WebSocketSingleton implements DownloadSubject {
    private static final int NORMAL_CLOSURE_STATUS = 10000;

    private static final String TAG = "DownloadUtility";
    //    public static HashMap<String, DownloadRequest> downloadList = new HashMap<>();
    private static WebSocketSingleton webSocketSingleTon;
    private final ArrayList<WebSocketObserver> webSocketObservers;
    private WebSocket webSocket;

    public WebSocketSingleton() {

        // Creates an ArrayList to hold all observers
        webSocketObservers = new ArrayList<>();
        startWebSocket();
    }

    public static WebSocketSingleton getInstant() {
        if (webSocketSingleTon == null) {
            webSocketSingleTon = new WebSocketSingleton();
        }
        return webSocketSingleTon;
    }

    @Override
    public void register(WebSocketObserver newWebSocketObserver) {

        // Adds a new observer to the ArrayList

        webSocketObservers.add(newWebSocketObserver);

    }

    @Override
    public void unregister(WebSocketObserver deleteWebSocketObserver) {

        // Get the index of the observer to delete

        int observerIndex = webSocketObservers.indexOf(deleteWebSocketObserver);

        // Print out message (Have to increment index to match)

        System.out.println("Observer " + (observerIndex + 1) + " deleted");

        // Removes observer from the ArrayList

        webSocketObservers.remove(observerIndex);

    }

    @Override
    public void notifyObserver(String response) {

        // Cycle through all observers and notifies them of
        // price changes

        for (WebSocketObserver webSocketObserver : webSocketObservers) {
            Log.d(TAG, "notifyObserver: " + (webSocketObserver.getActivityName()));
            webSocketObserver.onWebSocketResponse(response);

        }
    }

    private void startWebSocket() {

        // WebSocket
        Request request = new Request.Builder().url(APIClient.BASE_URL_WEB_SOCKET).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, listener);
        okHttpClient.dispatcher().executorService().shutdown();
    }


    public void sendMessage(JSONObject command) {
        webSocket.send(command.toString());
    }

    // WebSocket
    private final class EchoWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.d(TAG, "WebSocket connect stable");
//            joinCommand();
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            System.out.println("received message: " + text);
            notifyObserver(text);

           /* Gson gson = new Gson();
            Type type = new TypeToken<ResponseModel<FSUsersModel>>() {
            }.getType();


            ResponseModel<FSUsersModel> obj = gson.fromJson(text, type);


            if (obj.getStatus_code() == 200) {
                UserDetails.myDetail = obj.getData();
                startActivity(new Intent(LoginActivity.this, RoomListActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, obj.getMessage(), Toast.LENGTH_SHORT).show();
            }*/

        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            System.out.println("onMessage: " + bytes.hex());
//            Toast.makeText(LoginActivity.this, "onMessage:" + bytes.hex(), Toast.LENGTH_SHORT).show();
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