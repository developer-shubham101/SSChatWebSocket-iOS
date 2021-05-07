package `in`.newdevpoint.ssnodejschat.utility

import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import java.util.*

object UserDetails {
    //	public static String roomId = "";
    var chatUsers: HashMap<String, FSUsersModel> = HashMap<String, FSUsersModel>()
    lateinit var myDetail: FSUsersModel
//	public static boolean isGroup = false;
    //	public static FSGroupModel groupDetails;
}