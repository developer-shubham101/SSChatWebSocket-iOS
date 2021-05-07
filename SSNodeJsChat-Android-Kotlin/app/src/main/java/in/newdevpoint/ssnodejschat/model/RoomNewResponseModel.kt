package `in`.newdevpoint.ssnodejschat.model

import com.google.gson.annotations.SerializedName
import java.util.*

class RoomNewResponseModel {
    @SerializedName("newRoom")
    var newRoom: FSRoomModel? = null

    @SerializedName("userList")
    var userList = ArrayList<FSUsersModel>()
    val userListMap: HashMap<String, FSUsersModel>
        get() {
            val tmpHashList = HashMap<String, FSUsersModel>()
            for (element in userList) {
                tmpHashList[element.id] = element
            }
            return tmpHashList
        }
}