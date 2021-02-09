package in.newdevpoint.ssnodejschat.webService;


import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {
    private String message;
    @SerializedName("type")
    private String type;
    private String response;
    @SerializedName("statusCode")
    private int status_code;
    private T data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
