package in.newdevpoint.ssnodejschat.model;

import java.util.Map;

public class FSChatModel {

	private String roomId = "";
	private String lastMessageTime = "";
	private String lastMessage = "";
	private long newMessage = 0;

	private FSUsersModel senderUserDetail;

//    private boolean isOnline  = false;

	public FSChatModel(Map<String, Object> usersData, FSUsersModel fsUsersModel) {

		roomId = (String) usersData.get("docId");
//		name = (String) usersData.get("name");
		lastMessage = (String) usersData.get("lastMessage");
		senderUserDetail = fsUsersModel;

	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getLastMessageTime() {
		return lastMessageTime;
	}

	public void setLastMessageTime(String lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public long getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(long newMessage) {
		this.newMessage = newMessage;
	}

	public FSUsersModel getSenderUserDetail() {
		return senderUserDetail;
	}

	public void setSenderUserDetail(FSUsersModel senderUserDetail) {
		this.senderUserDetail = senderUserDetail;
	}
}
