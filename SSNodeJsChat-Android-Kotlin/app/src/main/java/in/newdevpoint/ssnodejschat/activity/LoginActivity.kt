package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.ActivityLoginBinding
import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import `in`.newdevpoint.ssnodejschat.observer.ResponseType
import `in`.newdevpoint.ssnodejschat.observer.WebSocketObserver
import `in`.newdevpoint.ssnodejschat.observer.WebSocketSingleton
import `in`.newdevpoint.ssnodejschat.utility.PreferenceUtils
import `in`.newdevpoint.ssnodejschat.utility.UserDetails
import `in`.newdevpoint.ssnodejschat.utility.Validate
import `in`.newdevpoint.ssnodejschat.webService.APIClient.KeyConstant
import `in`.newdevpoint.ssnodejschat.webService.ResponseModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import org.json.JSONObject

internal class TmpUserModel(var email: String, var password: String, var userId: String, var name: String)
class LoginActivity : AppCompatActivity(), WebSocketObserver {
    private val listOfTmpUsers = arrayOf(
            TmpUserModel("anil@yopmail.com", "123456", "1", "Anil"),
            TmpUserModel("amit@yopmail.com", "123456", "2", "Amit"),
            TmpUserModel("shubham@yopmail.com", "123456", "3", "Shubham"),
            TmpUserModel("ali@yopmail.com", "123456", "4", "Ali"),
            TmpUserModel("samreen@yopmail.com", "123456", "5", "Samreen")
    )
    private val tmpUserModel = listOfTmpUsers[1]

    //    private Waiting mWaitingDialog;
    private lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        //        mWaitingDialog = new Waiting(LoginActivity.this);
        WebSocketSingleton.Companion.getInstant()!!.register(this)
        loginBinding.loginUserEmail.setText(tmpUserModel.email)
        loginBinding.loginBtn.setOnClickListener { fetchLoginApi() }
    }

    fun fetchLoginApi() {
        if (loginBinding!!.loginUserEmail.text.toString().isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.validation_email), Toast.LENGTH_SHORT).show()
            return
        } else if (loginBinding!!.loginPassword.text.toString().isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.validation_password), Toast.LENGTH_SHORT).show()
            return
        } else if (!Validate.isEmail(loginBinding!!.loginUserEmail.text.toString())) {
            Toast.makeText(this, resources.getString(R.string.validation_valid_email), Toast.LENGTH_SHORT).show()
            return
        }
        //        mWaitingDialog.show();
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", tmpUserModel.userId)
            jsonObject.put("userName", loginBinding!!.loginUserEmail.text.toString())
            jsonObject.put("firstName", tmpUserModel.name)
            jsonObject.put("password", loginBinding!!.loginPassword.text.toString())
            jsonObject.put("fcm_token", PreferenceUtils.Companion.getDeviceToken(this))
            jsonObject.put("type", "loginOrCreate")
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_LOGIN)
            //            mWaitingDialog.show();
            WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            runOnUiThread {
                val gson = Gson()
                if (ResponseType.RESPONSE_TYPE_LOGIN.equalsTo(type) || ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE.equalsTo(type)) {
                    val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                    val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = gson.fromJson<ResponseModel<FSUsersModel>>(response, type1)
                    if (fsUsersModelResponseModel.getStatus_code() == 200) {
                        UserDetails.myDetail = fsUsersModelResponseModel.getData()
                        PreferenceUtils.Companion.loginUser(this@LoginActivity, fsUsersModelResponseModel.getData())
                        startActivity(Intent(this@LoginActivity, RoomListActivity::class.java))
                    } else {
                        Toast.makeText(this@LoginActivity, fsUsersModelResponseModel.getMessage(), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onWebSocketResponse: $type")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override val activityName: String = LoginActivity::class.java.name
    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
                ResponseType.RESPONSE_TYPE_LOGIN,
                ResponseType.RESPONSE_TYPE_LOGIN_OR_CREATE)
    }

    companion object {
        private const val EMAIL = "email"
        private const val TAG = "LoginActivity"
    }
}