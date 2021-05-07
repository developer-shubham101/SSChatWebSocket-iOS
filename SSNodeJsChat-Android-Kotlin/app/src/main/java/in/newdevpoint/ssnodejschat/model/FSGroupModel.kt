package `in`.newdevpoint.ssnodejschat.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FSGroupModel : Serializable {
    @SerializedName("group_name")
    var group_name = ""

    @SerializedName("about_group")
    var about_group = ""
}