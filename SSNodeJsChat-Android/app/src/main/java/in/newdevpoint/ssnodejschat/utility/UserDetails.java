package in.newdevpoint.ssnodejschat.utility;


import java.util.HashMap;

import in.newdevpoint.ssnodejschat.model.FSUsersModel;

public class UserDetails {

	//	public static String roomId = "";
	public static HashMap<String, FSUsersModel> chatUsers = new HashMap<>();
	private FSUsersModel myDetail;
//	public static boolean isGroup = false;
//	public static FSGroupModel groupDetails;

	public FSUsersModel getMyDetail() {
		return myDetail;
	}

	public void setMyDetail(FSUsersModel myDetail) {
		this.myDetail = myDetail;
	}

	private static UserDetails userDetails = null;

	public static UserDetails getInstant() {
		if (userDetails == null) {
			userDetails = new UserDetails();
		}
		return userDetails;
	}

}
