package in.newdevpoint.ssnodejschat.observer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.AppApplication;
import in.newdevpoint.ssnodejschat.utility.PreferenceUtils;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

// Uses the Subject interface to update all Observers

public class WebSocketSingleton extends WebSocketListener implements DownloadSubject {
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
        int observerIndex = webSocketObservers.indexOf(newWebSocketObserver);
        if (observerIndex == -1) {
            // Adds a new observer to the ArrayList
            webSocketObservers.add(newWebSocketObserver);
        } else {
            Log.d(TAG, "Subscriber is already registered");
        }


    }

    @Override
    public void unregister(WebSocketObserver deleteWebSocketObserver) {

        // Get the index of the observer to delete

        int observerIndex = webSocketObservers.indexOf(deleteWebSocketObserver);

        // Print out message (Have to increment index to match)

        System.out.println("Observer " + (observerIndex + 1) + " deleted");

        // Removes observer from the ArrayList
        if (observerIndex != -1)
            webSocketObservers.remove(observerIndex);

    }

    @Override
    public void notifyObserver(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            String responseType = jsonObject.getString("type");
            String message = jsonObject.getString("message");
            int statusCode = jsonObject.getInt("statusCode");

            // Cycle through all observers and notifies them of
            // price changes

            for (WebSocketObserver webSocketObserver : webSocketObservers) {
                Log.d(TAG, "notifyObserver: " + (webSocketObserver.getActivityName()));
                ResponseType[] registeredFor = webSocketObserver.registerFor();
                for (ResponseType element : registeredFor) {
                    if (element.equalsTo(responseType)) {
                        webSocketObserver.onWebSocketResponse(response, responseType, statusCode, message);
                        break;
                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startWebSocket() {

        // WebSocket
        Request request = new Request.Builder().url(APIClient.BASE_URL_WEB_SOCKET).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, this);
        okHttpClient.dispatcher().executorService().shutdown();
    }

    private void reconnectWebSocket() {

        // WebSocket
        Request request = new Request.Builder().url(APIClient.BASE_URL_WEB_SOCKET).build();

        OkHttpClient okHttpClient = new OkHttpClient();
        webSocket = okHttpClient.newWebSocket(request, this);
//        okHttpClient.dispatcher().executorService().shutdown();
    }


    public void sendMessage(JSONObject command) {
        webSocket.send(command.toString());
    }


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.d(TAG, "WebSocket connect stable");
        joinCommand();
    }

    private void joinCommand() {
        if (PreferenceUtils.isUserLogin(AppApplication.applicationContext)) {
            UserDetails.myDetail = PreferenceUtils.getRegisterUser(AppApplication.applicationContext);

            JSONObject jsonObject = new JSONObject();
            try {

                jsonObject.put("user_id", PreferenceUtils.getRegisterUser(AppApplication.applicationContext).getId());
                jsonObject.put("type", "create");
                jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_CREATE_CONNECTION);

                WebSocketSingleton.getInstant().sendMessage(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, final String text) {
        System.out.println("received message: " + text);
        notifyObserver(text);
    }


    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        System.out.println("onClosing: " + code + " / " + reason);
        closeConnection(webSocket);
    }

    private void closeConnection(WebSocket webSocket) {
        if (webSocket != null)
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        System.out.println("onFailure: " + t.getMessage());
//        closeConnection(webSocket);

        new Handler(Looper.getMainLooper()).postDelayed(this::reconnectWebSocket, 3000);
    }
}