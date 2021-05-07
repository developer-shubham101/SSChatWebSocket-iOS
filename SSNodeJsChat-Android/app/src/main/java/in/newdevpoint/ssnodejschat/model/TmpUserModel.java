package in.newdevpoint.ssnodejschat.model;

public class TmpUserModel {
    String email;
    String password;
    String userId;
    String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TmpUserModel(String email, String password, String userId, String name) {
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.name = name;
    }
}