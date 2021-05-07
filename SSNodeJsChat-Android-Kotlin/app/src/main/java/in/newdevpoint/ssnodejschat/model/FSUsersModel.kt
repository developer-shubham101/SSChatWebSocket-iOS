package `in`.newdevpoint.ssnodejschat.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FSUsersModel : Serializable {
    @SerializedName("userName")
    var userName = ""

    @SerializedName("firstName")
    var name = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("userId")
    var id: String = ""

    @SerializedName("profile_pic")
    var profile_image = ""

    @SerializedName("last_seen")
    var lastSeen = ""

    @SerializedName("is_online")
    var isOnline = false
    var isChecked = false
}