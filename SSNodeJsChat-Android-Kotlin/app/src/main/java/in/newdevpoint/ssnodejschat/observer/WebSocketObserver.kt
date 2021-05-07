package `in`.newdevpoint.ssnodejschat.observer

// The Observers update method is called when the Subject changes
interface WebSocketObserver {
    fun onWebSocketResponse(response: String, type: String, statusCode: Int, message: String?)
    val activityName: String
    fun registerFor(): Array<ResponseType>
}