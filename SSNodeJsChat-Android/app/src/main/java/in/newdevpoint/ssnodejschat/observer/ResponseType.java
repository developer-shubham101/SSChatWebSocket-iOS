package in.newdevpoint.ssnodejschat.observer;

public enum ResponseType {
    RESPONSE_TYPE_CHECK_ROOM("checkRoom"),
    RESPONSE_TYPE_CREATE_ROOM("createRoom"),
    RESPONSE_TYPE_USERS("allUsers"),
    RESPONSE_TYPE_ROOM_MODIFIED("roomsModified"),
    RESPONSE_TYPE_ROOM("allRooms"),
    RESPONSE_TYPE_MESSAGES("message"),
    RESPONSE_TYPE_LOGIN("login"),
    RESPONSE_TYPE_LOGIN_OR_CREATE("loginOrCreate"),
    RESPONSE_TYPE_USER_MODIFIED("userModified"),
    RESPONSE_TYPE_USER_BLOCK_MODIFIED("blockUser"),
    RESPONSE_TYPE_USER_ALL_BLOCK("allBlockUser");

    private final String name;

    ResponseType(String s) {
        name = s;
    }

    public boolean contains(String test) {

        for (ResponseType c : ResponseType.values()) {
            if (c.equalsTo(test)) {
                return true;
            }
        }

        return false;
    }

    public boolean equalsTo(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false 
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}