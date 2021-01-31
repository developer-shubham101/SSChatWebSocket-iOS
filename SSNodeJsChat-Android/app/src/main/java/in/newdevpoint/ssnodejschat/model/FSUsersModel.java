package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class FSUsersModel {

    @SerializedName("userName")
	private String name = "";

	private String email = "";
    @SerializedName("_id")
	private String id;
	private String profile_image = "";
//    private boolean isOnline  = false;


	public FSUsersModel() {
	}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
