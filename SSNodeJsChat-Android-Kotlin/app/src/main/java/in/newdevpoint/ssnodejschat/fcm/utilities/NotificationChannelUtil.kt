package `in`.newdevpoint.ssnodejschat.fcm.utilities

object NotificationChannelUtil {
    var DEFAULT_CHANNEL = Names("default", "This is default channel", "tone_eventually")
    var ALERT_CHANNEL = Names("alert", "This is alert!", "tone_goes_without_saying")
    var ERROR_CHANNEL = Names("error", "We found some error in the account!", "tone_got_it_done")

    class Names(var id: String, var name: String, var tone: String)
}