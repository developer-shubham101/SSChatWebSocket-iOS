package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.adapter.RoomListAdapter
import `in`.newdevpoint.ssnodejschat.databinding.ActivityRoomListBinding
import `in`.newdevpoint.ssnodejschat.model.FSRoomModel
import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import `in`.newdevpoint.ssnodejschat.model.RoomNewResponseModel
import `in`.newdevpoint.ssnodejschat.model.RoomResponseModel
import `in`.newdevpoint.ssnodejschat.observer.ResponseType
import `in`.newdevpoint.ssnodejschat.observer.WebSocketObserver
import `in`.newdevpoint.ssnodejschat.observer.WebSocketSingleton
import `in`.newdevpoint.ssnodejschat.utility.UserDetails
import `in`.newdevpoint.ssnodejschat.webService.APIClient.KeyConstant
import `in`.newdevpoint.ssnodejschat.webService.ResponseModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RoomListActivity : AppCompatActivity(), WebSocketObserver {
    private val alterNativeUserList: HashMap<String, FSUsersModel> = HashMap<String, FSUsersModel>()
    private lateinit var adapter: RoomListAdapter
    private lateinit var roomListBinding: ActivityRoomListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomListBinding = DataBindingUtil.setContentView(this, R.layout.activity_room_list)
        WebSocketSingleton.Companion.getInstant()!!.register(this)
        initRecycler()
        roomListBinding.openAllUsers.setOnClickListener { v: View? -> startActivity(Intent(this@RoomListActivity, AllUsersListActivity::class.java)) }
        roomListBinding.homNotification.setOnClickListener { v: View? -> startActivity(Intent(this, UpdateProfileActivity::class.java)) }
        joinCommand()
    }

    private fun initRecycler() {
        adapter = RoomListAdapter(this, object : RoomListAdapter.CallBackForSinglePost {
            override fun onClick(position: Int) {}
            override fun onClick(item: FSRoomModel) {
                val intent = Intent(this@RoomListActivity, ChatActivity::class.java)
                intent.putExtra(ChatActivity.Companion.INTENT_EXTRAS_KEY_IS_GROUP, item.isGroup)
                intent.putExtra(ChatActivity.Companion.INTENT_EXTRAS_KEY_GROUP_DETAILS, item.groupDetails)
                intent.putExtra(ChatActivity.Companion.INTENT_EXTRAS_KEY_ROOM_ID, item.roomId)
                intent.putExtra(ChatActivity.Companion.INTENT_EXTRAS_KEY_SENDER_DETAILS, item.senderUserDetail)
                startActivity(intent)
            }
        })
        roomListBinding!!.usersList.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        roomListBinding!!.usersList.layoutManager = mLayoutManager
        roomListBinding!!.usersList.adapter = adapter
    }

    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            val userList = JSONArray()
            userList.put(UserDetails.myDetail.id)
            jsonObject.put("type", "allRooms")
            jsonObject.put("userList", userList)
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_ROOM)
            //			jsonObject.put("room", roomId);
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) {
        try {
            runOnUiThread {
                println("received message: $response")
                val gson = Gson()
                if (ResponseType.RESPONSE_TYPE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> = gson.fromJson(response, type1)
                        UserDetails.chatUsers = roomResponseModelResponseModel.getData().userListMap
                        for (element in roomResponseModelResponseModel.getData().roomList) {
                            for (userId in element.userList) {
                                if (userId != UserDetails.myDetail.id) {
                                    element.senderUserDetail = UserDetails.chatUsers[userId]
                                    break
                                }
                            }
                        }
                        adapter.addAll(roomResponseModelResponseModel.getData().roomList)
                        //                    startActivity(new Intent(RoomListActivity.this, RoomListActivity.class));
                    } else {
                        Toast.makeText(this@RoomListActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_ROOM_MODIFIED.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<FSRoomModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<FSRoomModel> = gson.fromJson<ResponseModel<FSRoomModel>>(response, type1)
                        for (userId in roomResponseModelResponseModel.getData().userList) {
                            if (userId != UserDetails.myDetail.id) {
                                roomResponseModelResponseModel.getData().senderUserDetail = UserDetails.chatUsers[userId]
                                break
                            }
                        }
                        adapter.updateElement(roomResponseModelResponseModel.getData())
                    } else {
                        Toast.makeText(this@RoomListActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } else if (ResponseType.RESPONSE_TYPE_CREATE_ROOM.equalsTo(type)) {
                    if (statusCode == 200) {
                        val type1 = object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                        val roomResponseModelResponseModel: ResponseModel<RoomNewResponseModel> = gson.fromJson(response, type1)
                        val tmpUserList: HashMap<String, FSUsersModel> = roomResponseModelResponseModel.getData().userListMap
                        for (key in tmpUserList.keys) {
                            UserDetails.chatUsers[key] = tmpUserList[key]!!
                        }
                        val element: FSRoomModel = roomResponseModelResponseModel.getData().newRoom!!
                        for (userId in element.userList) {
                            if (userId != UserDetails.myDetail.id) {
                                element.senderUserDetail = UserDetails.chatUsers[userId]
                                break
                            }
                        }
                        adapter.addOrUpdate(element)
                    } else if (ResponseType.RESPONSE_TYPE_USER_MODIFIED.equalsTo(type)) {
                        Log.d(TAG, "received message: $response")
                        val type1 = object : TypeToken<ResponseModel<FSUsersModel?>?>() {}.type
                        val fsUsersModelResponseModel: ResponseModel<FSUsersModel> = Gson().fromJson<ResponseModel<FSUsersModel>>(response, type1)
                        adapter.updateUserElement(fsUsersModelResponseModel.getData())
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d(TAG, "onWebSocketResponse: $type")
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override val activityName: String = RoomListActivity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
                ResponseType.RESPONSE_TYPE_ROOM,
                ResponseType.RESPONSE_TYPE_ROOM_MODIFIED,
                ResponseType.RESPONSE_TYPE_CREATE_ROOM,
                ResponseType.RESPONSE_TYPE_USER_MODIFIED
        )
    }

    companion object {
        private const val TAG = "RoomListActivity:"
    }
}