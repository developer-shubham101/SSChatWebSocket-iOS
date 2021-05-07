package `in`.newdevpoint.ssnodejschat.model

import com.google.gson.annotations.SerializedName
import java.util.*

class FSRoomModel {
    //    private boolean isOnline  = false;
    @SerializedName("_id")
    var roomId = ""

    @SerializedName("userList")
    var userList = ArrayList<String>()

    @SerializedName("users")
    var users = HashMap<String, Boolean>()

    @SerializedName("unread")
    var unread: HashMap<String, Int>? = null

    @SerializedName("last_message_time")
    var lastMessageTime = ""

    @SerializedName("last_message")
    var lastMessage = ""

    @SerializedName("type")
    private var type = ""
    var senderUserDetail: FSUsersModel? = null

    @SerializedName("createBy")
    var createBy = ""

    @SerializedName("group_details")
    var groupDetails: FSGroupModel? = null
    fun setType(type: String) {
        this.type = type
    }

    val isGroup: Boolean
        get() = type == "group"
}