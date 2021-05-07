package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.activity.AllUsersListActivity
import `in`.newdevpoint.ssnodejschat.adapter.UsersListAdapter
import `in`.newdevpoint.ssnodejschat.databinding.ActivityAllUsersListBinding
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

class AllUsersListActivity : AppCompatActivity(), WebSocketObserver {
    private lateinit var adapter: UsersListAdapter
    private lateinit var roomListBinding: ActivityAllUsersListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomListBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_users_list)
        WebSocketSingleton.Companion.getInstant()!!.register(this)
        roomListBinding.switchCreateGroup.setOnClickListener { v: View? ->
            adapter.setGroupMode(roomListBinding.switchCreateGroup.isChecked)
            adapter.notifyDataSetChanged()
            roomListBinding.userListGroupName.isEnabled = roomListBinding.switchCreateGroup.isChecked
            roomListBinding.homNotification.isEnabled = roomListBinding.switchCreateGroup.isChecked
        }
        roomListBinding.homNotification.setOnClickListener { v: View? -> createGroupCommand() }
        initRecycler()
        joinCommand()
    }

    private fun initRecycler() {
        adapter = UsersListAdapter(object : UsersListAdapter.CallBackForSinglePost {
            override fun onClick(position: Int) {}
            override fun onClick(item: FSUsersModel) {
                createRoomCommand(item)
                //                HashMap<String, FSUsersModel> chatUsersMap = new HashMap<>();
////                chatUsersMap.put(currentUser.getUid(), myDetail);
//                chatUsersMap.put(item.getSenderUserDetail().getId(), item.getSenderUserDetail());
//
//                UserDetails.roomId = item.getRoomId();
//                UserDetails.chatUsers = chatUsersMap;
//                UserDetails.myDetail = myDetail;
//                startActivity(new Intent(AllUsersListActivity.this, ChatActivity.class));
            }
        })
        roomListBinding.usersList.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        roomListBinding.usersList.layoutManager = mLayoutManager
        roomListBinding.usersList.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        WebSocketSingleton.Companion.getInstant()!!.register(this)
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
        WebSocketSingleton.Companion.getInstant()!!.unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
        WebSocketSingleton.Companion.getInstant()!!.unregister(this)
    }

    private fun createRoomCommand(connectWith: FSUsersModel) {
        val jsonObject = JSONObject()
        try {
            val usersList = JSONArray()
            usersList.put(connectWith.id)
            usersList.put(UserDetails.myDetail.id)
            jsonObject.put("userList", usersList)
            jsonObject.put("createBy", UserDetails.myDetail.id)
            jsonObject.put("type", "createRoom")
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_ROOM)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
    }

    private fun createGroupCommand() {
        val usersLists: ArrayList<FSUsersModel> = adapter.getAllList()
        val selectedUsersLists: ArrayList<FSUsersModel> = ArrayList<FSUsersModel>()
        for (fsUsersModel in usersLists) {
            if (fsUsersModel.isChecked) selectedUsersLists.add(fsUsersModel)
        }
        if (selectedUsersLists.size <= 1) {
            Toast.makeText(this, "Please Select More People", Toast.LENGTH_SHORT).show()
            return
        } else if (roomListBinding.userListGroupName.text?.equals("") == true) {
            Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show()
            return
        }
        val jsonObject = JSONObject()
        try {
            val usersList = JSONArray()
            for (fsUsersModel in selectedUsersLists) {
                usersList.put(fsUsersModel.id)
            }
            usersList.put(UserDetails.myDetail.id)
            jsonObject.put("userList", usersList)
            jsonObject.put("type", "createRoom")
            jsonObject.put("room_type", "group")
            val groupDetails = JSONObject()
            groupDetails.put("group_name", roomListBinding.userListGroupName.text)
            groupDetails.put("about_group", "This is Just a Sample Group")
            jsonObject.put("group_details", groupDetails)
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_ROOM)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
    }

    private fun joinCommand() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("type", "allUsers")
            jsonObject.put(KeyConstant.REQUEST_TYPE_KEY, KeyConstant.REQUEST_TYPE_USERS)
            //			jsonObject.put("room", roomId);
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        WebSocketSingleton.Companion.getInstant()!!.sendMessage(jsonObject)
    }

    override fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?) =
            try {
                runOnUiThread {
                    Log.d(TAG, "received message: $response")
                    val gson = Gson()
                    if (ResponseType.RESPONSE_TYPE_USERS.equalsTo(type)) {
                        if (statusCode == 200) {
                            val typeUserList = object : TypeToken<ResponseModel<ArrayList<FSUsersModel?>?>?>() {}.type
                            val arrayListResponseModel: ResponseModel<ArrayList<FSUsersModel>> = gson.fromJson<ResponseModel<ArrayList<FSUsersModel>>>(response, typeUserList)
                            val fsUsersModels: ArrayList<FSUsersModel> = ArrayList<FSUsersModel>()
                            for (element in arrayListResponseModel.getData()) {
                                if (element.id != UserDetails.myDetail.id) {
                                    fsUsersModels.add(element)
                                }
                            }
                            adapter.addAll(fsUsersModels)
                        } else {
                            Toast.makeText(this@AllUsersListActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else if (ResponseType.RESPONSE_TYPE_CREATE_ROOM.equalsTo(type)) {
                        if (statusCode == 200) {
                            val type1 = object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                            val roomResponseModelResponseModel: ResponseModel<RoomNewResponseModel> = gson.fromJson<ResponseModel<RoomNewResponseModel>>(response, type1)
                            val tmpUserList: HashMap<String, FSUsersModel> = roomResponseModelResponseModel.getData().userListMap
                            for (key in tmpUserList.keys) {
                                UserDetails.chatUsers[key] = tmpUserList[key]!!
                            }
                            val element: FSRoomModel = roomResponseModelResponseModel.getData().newRoom!!
                            if (element.createBy == UserDetails.myDetail.id) {
                                for (userId in element.userList) {
                                    if (userId != UserDetails.myDetail.id) {
                                        element.senderUserDetail = UserDetails.chatUsers[userId]
                                        break
                                    }
                                }
                                val intent = Intent(this, ChatActivity::class.java)
                                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_IS_GROUP, element.isGroup)
                                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_ROOM_ID, element.roomId)
                                intent.putExtra(ChatActivity.INTENT_EXTRAS_KEY_SENDER_DETAILS, element.senderUserDetail)
                                startActivity(intent)
                            }

                            //                    startActivity(new Intent(RoomListActivity.this, RoomListActivity.class));
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } else if (ResponseType.RESPONSE_TYPE_CHECK_ROOM.equalsTo(type)) {
                        if (statusCode == 200) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            val type1 = object : TypeToken<ResponseModel<RoomNewResponseModel?>?>() {}.type
                            val roomResponseModelResponseModel: ResponseModel<RoomResponseModel> = gson.fromJson<ResponseModel<RoomResponseModel>>(response, type1)
                        } else if (statusCode == 404) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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


    override val activityName: String = AllUsersListActivity::class.java.name

    override fun registerFor(): Array<ResponseType> {
        return arrayOf(
                ResponseType.RESPONSE_TYPE_USERS,
                ResponseType.RESPONSE_TYPE_CREATE_ROOM,
                ResponseType.RESPONSE_TYPE_CHECK_ROOM
        )
    }

    companion object {
        const val TAG = "UsersActivity:"
    }
}