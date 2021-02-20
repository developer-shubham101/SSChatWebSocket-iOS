package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomNewResponseModel {


    @SerializedName("newRoom")
    private FSRoomModel newRoom;
    @SerializedName("userList")
    private ArrayList<FSUsersModel> userList = new ArrayList<>();

    public FSRoomModel getNewRoom() {
        return newRoom;
    }

    public void setNewRoom(FSRoomModel newRoom) {
        this.newRoom = newRoom;
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
