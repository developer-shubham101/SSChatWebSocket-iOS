package `in`.newdevpoint.ssnodejschat.utility

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeShow {
    //2021-02-19T17:53:32.901+00:00
    @JvmOverloads
    fun TimeFormatYesterdayToDay(timeOfMomentUpload: String?, fromFormat: String? = "yyyy-MM-dd HH:mm:ss", toFormat: String? = "dd-MM-yyyy 'at' hh:mm:ss"): String {
        try {
            val format = SimpleDateFormat(fromFormat, Locale.getDefault())
            val past = format.parse(timeOfMomentUpload)
            val now = Date()
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
            val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

            /*         Log.d("dtaasec",""+TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");
*/
            return when {
                seconds < 60 -> {
                    "$seconds seconds ago"
                }
                minutes < 60 -> {
                    "$minutes minutes ago"
                }
                hours < 24 -> {
                    "$hours hours ago"
                }
                days == 1L -> {
                    "$days day ago"
                }
                days < 3 -> {
                    "$days days ago"
                }
                else -> {
                    val dateFormat: DateFormat = SimpleDateFormat(toFormat, Locale.getDefault())
                    dateFormat.format(past)
                }
            }
        } catch (j: Exception) {
            j.printStackTrace()
        }
        return ""
    }
}