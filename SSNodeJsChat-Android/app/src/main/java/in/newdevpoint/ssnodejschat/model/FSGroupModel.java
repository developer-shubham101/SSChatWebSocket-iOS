package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FSGroupModel implements Serializable {

    @SerializedName("group_name")
    private String group_name = "";


    @SerializedName("about_group")
    private String about_group = "";

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAbout_group() {
        return about_group;
    }

    public void setAbout_group(String about_group) {
        this.about_group = about_group;
    }
}
