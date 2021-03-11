package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserBlockModel implements Serializable {

    @SerializedName("blockedBy")
    private String blockedBy = "";


    @SerializedName("blockedTo")
    private String blockedTo = "";


    @SerializedName("isBlock")
    private boolean isBlock = false;


    public String getBlockedBy() {
        return blockedBy;
    }

    public void setBlockedBy(String blockedBy) {
        this.blockedBy = blockedBy;
    }

    public String getBlockedTo() {
        return blockedTo;
    }

    public void setBlockedTo(String blockedTo) {
        this.blockedTo = blockedTo;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public UserBlockModel() {
    }

}
