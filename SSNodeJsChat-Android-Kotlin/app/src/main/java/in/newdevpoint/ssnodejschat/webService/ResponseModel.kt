package `in`.newdevpoint.ssnodejschat.webService

import com.google.gson.annotations.SerializedName

class ResponseModel<T> {
    private var message: String? = null

    @SerializedName("type")
    private var type: String? = null
    private var response: String? = null

    @SerializedName("statusCode")
    private var status_code = 0
    private var data: T? = null
    fun getType(): String? {
        return type
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getResponse(): String? {
        return response
    }

    fun setResponse(response: String?) {
        this.response = response
    }

    fun getStatus_code(): Int {
        return status_code
    }

    fun setStatus_code(status_code: Int) {
        this.status_code = status_code
    }

    fun getData(): T {
        return data!!
    }

    fun setData(data: T) {
        this.data = data
    }
}