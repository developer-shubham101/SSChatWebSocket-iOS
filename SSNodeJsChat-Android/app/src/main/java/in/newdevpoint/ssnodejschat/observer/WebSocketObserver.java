package in.newdevpoint.ssnodejschat.observer;// The Observers update method is called when the Subject changes

import org.json.JSONObject;

public interface WebSocketObserver {
    void onWebSocketResponse(String response, String type, int statusCode, String message);
    String getActivityName();
    ResponseType[] registerFor();
}
