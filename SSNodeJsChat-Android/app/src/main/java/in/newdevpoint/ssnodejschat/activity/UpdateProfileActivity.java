package in.newdevpoint.ssnodejschat.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;

import in.newdevpoint.ssnodejschat.AppApplication;
import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.ActivityEditProfileBinding;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.UploadFileMode;
import in.newdevpoint.ssnodejschat.observer.ResponseType;
import in.newdevpoint.ssnodejschat.observer.WebSocketObserver;
import in.newdevpoint.ssnodejschat.observer.WebSocketSingleton;
import in.newdevpoint.ssnodejschat.utility.PreferenceUtils;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.utility.Utils;
import in.newdevpoint.ssnodejschat.webService.APIClient;
import in.newdevpoint.ssnodejschat.webService.ResponseModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class UpdateProfileActivity extends AppCompatActivity implements WebSocketObserver {
	private static final String EMAIL = "email";
	private static final String TAG = "LoginActivity";
	//    private Waiting mWaitingDialog;
	private ActivityEditProfileBinding loginBinding;
	private UploadInterface uploadInterface;
	private String userProfilePicUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
//        mWaitingDialog = new Waiting(LoginActivity.this);

		WebSocketSingleton.getInstant().register(this);

		loginBinding.editProfileEmail.setText(UserDetails.getInstant().getMyDetail().getEmail());
		loginBinding.editProfileUpdate.setOnClickListener(v -> updateUserProfile());

		loginBinding.editProfileProfile.setOnClickListener(v -> CropImage.activity().setAspectRatio(1, 1).start(UpdateProfileActivity.this));

		uploadInterface = APIClient.getClient().create(UploadInterface.class);


		loginBinding.editProfileEmail.setText(UserDetails.getInstant().getMyDetail().getEmail());
		loginBinding.editProfileName.setText(UserDetails.getInstant().getMyDetail().getName());
		Glide.with(this)
				.setDefaultRequestOptions(AppApplication.USER_PROFILE_DEFAULT_GLIDE_CONFIG)
				.load(Utils.getImageString(UserDetails.getInstant().getMyDetail().getProfile_image()))
				.into(loginBinding.editProfileProfile);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
			CropImage.ActivityResult result = CropImage.getActivityResult(data);
			if (resultCode == RESULT_OK) {
				Uri resultUri;
				if (result != null) {
					resultUri = result.getUri();
					if (resultUri != null) {
						uploadSingleFile(new File(resultUri.getPath()));
					}
				}
			} else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
				Exception error;
				if (result != null) {
					error = result.getError();
					error.printStackTrace();
				}

			}
		}
	}

	private void uploadSingleFile(File files) {

		MultipartBody.Builder builder = new MultipartBody.Builder();
		builder.setType(MultipartBody.FORM);

		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), files);
		MultipartBody.Part thumbnail = MultipartBody.Part.createFormData("file", files.getName(), requestFile);


//        builder.addFormDataPart("channel_id", "sample");

		builder.addFormDataPart("room_id", "user_profiles");
		builder.addPart(thumbnail);

		Call<JsonElement> call;

		MultipartBody requestBody = builder.build();


		call = uploadInterface.uploadFile(requestBody);


		call.enqueue(new Callback<JsonElement>() {
			@Override
			public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
				if (response.isSuccessful()) {
					JsonElement jsonElement = response.body();

					Gson gson = new Gson();
					Type type = new TypeToken<ResponseModel<UploadFileMode>>() {
					}.getType();


					ResponseModel<UploadFileMode> obj = gson.fromJson(jsonElement.toString(), type);
					userProfilePicUrl = obj.getData().getFile();

					updateUserProfile();


				} else {

				}
//				uploadNext();
			}

			@Override
			public void onFailure(Call<JsonElement> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void updateUserProfile() {
       /* if (loginBinding.editProfileEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
            return;
        } else if (!Validate.isEmail(loginBinding.editProfileEmail.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.validation_valid_email), Toast.LENGTH_SHORT).show();
            return;
        } else */
		if (loginBinding.editProfileName.getText().toString().isEmpty()) {
			Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
			return;
		}
//        mWaitingDialog.show();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", PreferenceUtils.getRegisterUser(this).getId());
			jsonObject.put("userName", loginBinding.editProfileEmail.getText().toString());
//            jsonObject.put("email", loginBinding.editProfileEmail.getText().toString());
			jsonObject.put("firstName", loginBinding.editProfileName.getText().toString());
			//https://tryste.ezxdemo.com/storage/
			if (userProfilePicUrl != null) {
				jsonObject.put("profile_pic", userProfilePicUrl);
			}


//            jsonObject.put("fcm_token", PreferenceUtils.getDeviceToken(this));
			jsonObject.put("type", "updateProfile");
			jsonObject.put(APIClient.KeyConstant.REQUEST_TYPE_KEY, APIClient.KeyConstant.REQUEST_TYPE_LOGIN);
//            mWaitingDialog.show();

			WebSocketSingleton.getInstant().sendMessage(jsonObject);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketResponse(String response, String type, int statusCode, String message) {
		runOnUiThread(() -> {
			if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) ||
					ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {

				Type type1 = new TypeToken<ResponseModel<FSUsersModel>>() {
				}.getType();

				ResponseModel<FSUsersModel> fsUsersModelResponseModel = new Gson().fromJson(response, type1);
				if (fsUsersModelResponseModel.getStatus_code() == 200) {
					UserDetails.getInstant().setMyDetail(fsUsersModelResponseModel.getData());

					PreferenceUtils.loginUser(UpdateProfileActivity.this, fsUsersModelResponseModel.getData());
					startActivity(new Intent(UpdateProfileActivity.this, RoomListActivity.class));
				} else {
					Toast.makeText(UpdateProfileActivity.this, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.d(TAG, "onWebSocketResponse: " + type);
			}
		});


	}

	@Override
	public String getActivityName() {
		return UpdateProfileActivity.class.getName();
	}

	@Override
	public ResponseType[] registerFor() {
		return new ResponseType[]{
				ResponseType.RESPONSE_TYPE_LOGIN,
				ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE,
		};
	}

	private interface UploadInterface {
		// @POST("upload")
		@POST("user-tryster-chat-file")
		Call<JsonElement> uploadFile(@Body MultipartBody requestBody, @Header("Authorization") String authorization);

		//@POST("upload")
		@POST("user-tryster-chat-file")
		Call<JsonElement> uploadFile(@Body MultipartBody requestBody);
	}
}
