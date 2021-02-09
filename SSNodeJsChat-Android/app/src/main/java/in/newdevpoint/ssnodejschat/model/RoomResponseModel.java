package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomResponseModel {


    @SerializedName("roomList")
    private ArrayList<FSRoomModel> roomList = new ArrayList<>();
    @SerializedName("userList")
    private ArrayList<FSUsersModel> userList = new ArrayList<>();


    public ArrayList<FSRoomModel> getRoomList() {
        return roomList;
    }

    public void setRoomList(ArrayList<FSRoomModel> roomList) {
        this.roomList = roomList;
    }

    public ArrayList<FSUsersModel> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<FSUsersModel> userList) {
        this.userList = userList;
    }

    public HashMap<String, FSUsersModel> getUserListMap() {
        HashMap<String, FSUsersModel> tmpHashList = new HashMap<>();

        for (FSUsersModel element :
                userList) {
            tmpHashList.put(element.getId(), element);
        }
        return tmpHashList;
    }
}
