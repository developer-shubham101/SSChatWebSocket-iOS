package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.AppApplication
import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.activity.UpdateProfileActivity
import `in`.newdevpoint.ssnodejschat.databinding.ActivityEditProfileBinding
import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import `in`.newdevpoint.ssnodejschat.model.UploadFileMode
import `in`.newdevpoint.ssnodejschat.observer.ResponseType
import `in`.newdevpoint.ssnodejschat.observer.WebSocketObserver
import `in`.newdevpoint.ssnodejschat.observer.WebSocketSingleton
import `in`.newdevpoint.ssnodejschat.utility.*
import `in`.newdevpoint.ssnodejschat.webService.APIClient
import `in`.newdevpoint.ssnodejschat.webService.APIClient.KeyConstant
import `in`.newdevpoint.ssnodejschat.webService.ResponseModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.theartofdev.edmodo.cropper.CropImage
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.io.File

class UpdateProfileActivity : AppCompatActivity(), WebSocketObserver {
    //    private Waiting mWaitingDialog;
    private lateinit var loginBinding: ActivityEditProfileBinding
    private var uploadInterface: UploadInterface? = null
    private var userProfilePicUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        //        mWaitingDialog = new Waiting(LoginActivity.this);
        WebSocketSingleton.Companion.getInstant()!!.register(this)
        loginBinding.editProfileEmail.setText(UserDetails.myDetail.email)
        loginBinding.editProfileUpdate.setOnClickListener { v: View? -> updateUserProfile() }
        loginBinding.editProfileProfile.setOnClickListener { v: View? -> CropImage.activity().setAspectRatio(1, 1).start(this@UpdateProfileActivity) }
        uploadInterface = APIClient.getClient()!!.create(UploadInterface::class.java)
        loginBinding.editProfileEmail.setText(UserDetails.myDetail.email)
        loginBinding.editProfileName.setText(UserDetails.myDetail.name)
        Glide.with(this)
                .setDefaultRequestOptions(AppApplication.Companion.USER_PROFILE_DEFAULT_GLIDE_CONFIG)
                .load(Utils.getImageString(UserDetails.myDetail.profile_image))
                .into(loginBinding.editProfileProfile)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri?
                if (result != null) {
                    resultUri = result.uri
                    if (resultUri != null) {
                        uploadSingleFile(File(resultUri.path))
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception
                if (result != null) {
                    error = result.error
                    error.printStackTrace()
                }
            }
        }
    }

    private fun uploadSingleFile(files: File) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), files)
        val thumbnail = MultipartBody.Part.createFormData("file", files.name, requestFile)


//        builder.addFormDataPart("channel_id", "sample");
        builder.addFormDataPart("room_id", "user_profiles")
        builder.addPart(thumbnail)
        val call: Call<JsonElement>
        val requestBody = builder.build()
        call = uploadInterface!!.uploadFile(requestBody)
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful) {
                    val jsonElement = response.body()
                    val gson = Gson()
                    val type = object : TypeToken<ResponseModel<UploadFileMode?>?>() {}.type
                    val obj: ResponseModel<UploadFileMode> = gson.fromJson(jsonElement.toString(), type)
                    userProfilePicUrl = obj.getData().file
                    updateUserProfile()
                } else {
                }
                //				uploadNext();
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun updateUserProfile() {
        /* if (loginBinding.editProfileEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.validation_email), Toast.LENGTH_SHORT).show();
            return;
        } else if (!Validate.isEmail(loginBinding.editProfileEmail.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.validation_valid_email), Toast.LENGTH_SHORT).show();
            return;
        } else */
        if (loginBinding!!.editProfileName.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            return
        }
        //        mWaitingDialog.show();
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", PreferenceUtils.Companion.getRegisterUser(this).id)
            jsonObject.put("userName", loginBinding!!.editProfileEmail.text.toString())
            //            jsonObject.put("email", loginBinding.editProfileEmail.getText().toString());
            jsonObject.put("firstName", loginBinding!!.editProfileName.text.toString())
            //https://tryste.ezxdemo.com/storage/
            if (userProfilePicUrl != null) {
                jsonObject.put("profile_pic", userProfilePicUrl)
            }


//            jsonObject.put("fcm_token", PreferenceUtils.getDeviceToken(this));
            jsonObject.put("type", "updateProfile")
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_LOGIN)
            //            mWaitingDialog.show();
            WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        runOnUiThread {
            if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) ||
                    ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {
                val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = Gson().fromJson(response, type1)
                if (fsUsersModelResponseModel.getStatus_code() == 200) {
                    UserDetails.myDetail = fsUsersModelResponseModel.getData()
                    PreferenceUtils.Companion.loginUser(this@UpdateProfileActivity, fsUsersModelResponseModel.getData())
                    startActivity(Intent(this@UpdateProfileActivity, RoomListActivity::class.java))
                } else {
                    Toast.makeText(this@UpdateProfileActivity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(TAG, "onWebSocketResponse: $type")
            }
        }
    }


    override val activityName: String = UpdateProfileActivity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
                ResponseType.RESPONSE_TYPE_LOGIN,
                ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE)
    }

    private interface UploadInterface {
        // @POST("upload")
        @POST("user-tryster-chat-file")
        fun uploadFile(@Body requestBody: MultipartBody?, @Header("Authorization") authorization: String?): Call<JsonElement?>?

        //@POST("upload")
        @POST("user-tryster-chat-file")
        fun uploadFile(@Body requestBody: MultipartBody?): Call<JsonElement>
    }

    companion object {
        private const val EMAIL = "email"
        private const val TAG = "LoginActivity"
    }
}