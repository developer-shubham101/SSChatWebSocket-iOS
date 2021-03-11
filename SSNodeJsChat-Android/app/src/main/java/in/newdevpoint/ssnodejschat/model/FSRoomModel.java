package in.newdevpoint.ssnodejschat.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class FSRoomModel {

    @SerializedName("_id")
    private String roomId = "";

    @SerializedName("userList")
    private ArrayList<String> userList = new ArrayList<>();

    @SerializedName("users")
    private HashMap<String, Boolean> users = new HashMap<>();


    @SerializedName("unread")
    private @Nullable
    HashMap<String, Integer> unread;

    @SerializedName("last_message_time")
    private String lastMessageTime = "";


    @SerializedName("last_message")
    private String lastMessage = "";

    @SerializedName("type")
    private String type = "";

    @Nullable
    private FSUsersModel senderUserDetail;

    @SerializedName("createBy")
    private String createBy = "";

    @SerializedName("group_details")
    private FSGroupModel groupDetails;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public FSGroupModel getGroupDetails() {
        return groupDetails;
    }

    public void setGroupDetails(FSGroupModel groupDetails) {
        this.groupDetails = groupDetails;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isGroup() {
        return this.type.equals("group");
    }

    @Nullable
    public HashMap<String, Integer> getUnread() {
        return unread;
    }

    public void setUnread(@Nullable HashMap<String, Integer> unread) {
        this.unread = unread;
    }

    public ArrayList<String> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<String> userList) {
        this.userList = userList;
    }

    public HashMap<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, Boolean> users) {
        this.users = users;
    }

//    private boolean isOnline  = false;

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


    public @Nullable
    FSUsersModel getSenderUserDetail() {
        return senderUserDetail;
    }

    public void setSenderUserDetail(FSUsersModel senderUserDetail) {
        this.senderUserDetail = senderUserDetail;
    }
}
