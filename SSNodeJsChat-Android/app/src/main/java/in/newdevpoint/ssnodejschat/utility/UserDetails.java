package in.newdevpoint.ssnodejschat.utility;


import java.util.HashMap;

import in.newdevpoint.ssnodejschat.model.FSUsersModel;

public class UserDetails {

	public static String roomId = "";
	public static HashMap<String, FSUsersModel> chatUsers = new HashMap<>();
	public static FSUsersModel myDetail;
	public static boolean isGroup = false;
}
