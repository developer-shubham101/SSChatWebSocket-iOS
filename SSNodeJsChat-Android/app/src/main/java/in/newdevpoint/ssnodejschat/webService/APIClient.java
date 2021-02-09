package in.newdevpoint.ssnodejschat.webService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static final String IMAGE_URL = "http://172.16.16.18:8005/storage/";
    public static final String BASE_URL_WEB_SOCKET = "ws://172.16.16.18:1337/v1";
    private static final String BASE_URL = "http://testapi.newdevpoint.in/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
            httpBuilder.connectTimeout(60, TimeUnit.SECONDS);
            httpBuilder.readTimeout(10, TimeUnit.MINUTES);
            httpBuilder.writeTimeout(10, TimeUnit.MINUTES);
            httpBuilder.retryOnConnectionFailure(true);
//            httpBuilder.addInterceptor(new CustomInterceptor(""));

            OkHttpClient okHttpClient = httpBuilder.build();

            //init retrofit
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL)
//                    .addConverterFactory(ScalarsConverterFactory.signUp())
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }
        return retrofit;
    }

    public static class KeyConstant {
        public static final String REQUEST_TYPE_KEY = "request";
        public static final String REQUEST_TYPE_LOGIN = "login";
        public static final String REQUEST_TYPE_CREATE_CONNECTION = "create_connection";
        public static final String RESPONSE_TYPE_LOGIN = "login";
        public static final String REQUEST_TYPE_ROOM = "room";
        public static final String RESPONSE_TYPE_ROOM = "allRooms";
        public static final String RESPONSE_TYPE_ROOM_MODIFIED = "allRoomsModified";
        public static final String REQUEST_TYPE_USERS = "users";
        public static final String RESPONSE_TYPE_USERS = "allUsers";
        public static final String REQUEST_TYPE_MESSAGE = "message";
        public static final String RESPONSE_TYPE_MESSAGES = "message";
    }
}