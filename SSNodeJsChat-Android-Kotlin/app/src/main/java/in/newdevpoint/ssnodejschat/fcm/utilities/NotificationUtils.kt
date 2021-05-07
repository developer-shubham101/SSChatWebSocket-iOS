package `in`.newdevpoint.ssnodejschat.fcm.utilities

import `in`.newdevpoint.ssnodejschat.R
import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NotificationUtils(private val mContext: Context) {
    @JvmOverloads
    fun showNotificationMessage(title: String, message: String?, intent: Intent, channelId: String, imageUrl: String? = null) {
        // Check for empty push message
        if (TextUtils.isEmpty(message)) return


        // notification icon
        val icon = R.mipmap.ic_launcher
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
                mContext,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
        )
        showSmallNotification(icon, title, message, resultPendingIntent, channelId, imageUrl)
    }

    private fun showSmallNotification(icon: Int, title: String, message: String?, resultPendingIntent: PendingIntent, channelId: String, imageUrl: String?) {
        val notificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel: NotificationChannelUtil.Names?
        channel = when (channelId) {
            "alert" -> {
                NotificationChannelUtil.ALERT_CHANNEL
            }
            "error" -> {
                NotificationChannelUtil.ERROR_CHANNEL
            }
            else -> NotificationChannelUtil.DEFAULT_CHANNEL
        }
        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.packageName + "/raw/" + channel.tone)
        playNotificationSound(alarmSound)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channel.id, channel.name, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
            notificationChannel.setSound(alarmSound, att)
        }
        val style: NotificationCompat.Style
        style = if (!TextUtils.isEmpty(imageUrl)) {
            if (imageUrl != null && imageUrl.length > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {
                val bitmap = getBitmapFromURL(imageUrl)
                if (bitmap != null) {
                    val bigPictureStyle = NotificationCompat.BigPictureStyle()
                    bigPictureStyle.setBigContentTitle(title)
                    bigPictureStyle.setSummaryText(Html.fromHtml(message).toString())
                    bigPictureStyle.bigPicture(bitmap)
                    bigPictureStyle
                } else {
                    createSimpleStyle(message)
                }
            } else {
                createSimpleStyle(message)
            }
        } else {
            createSimpleStyle(message)
        }
        val mBuilder = NotificationCompat.Builder(mContext, channel.id)
        val notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(style)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.mipmap.ic_launcher) //                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build()
        Log.d(TAG, "showSmallNotification: ")
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    fun getBitmapFromURL(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Playing notification sound
    fun playNotificationSound() {
        val alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + mContext.packageName + "/raw/tone_eventually")
        playNotificationSound(alarmSound)
    }

    // Playing notification sound
    fun playNotificationSound(alarmSound: Uri?) {
        try {
            val r = RingtoneManager.getRingtone(mContext, alarmSound)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = NotificationUtils::class.java.simpleName

        /**
         * Method checks if the app is in background or not
         */
        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses
                for (processInfo in runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (activeProcess in processInfo.pkgList) {
                            if (activeProcess == context.packageName) {
                                isInBackground = false
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo!!.packageName == context.packageName) {
                    isInBackground = false
                }
            }
            return isInBackground
        }

        // Clears notification tray messages
        fun clearNotifications(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        private fun createSimpleStyle(message: String?): NotificationCompat.Style {
            val style: NotificationCompat.Style
            val inboxStyle = NotificationCompat.InboxStyle()
            inboxStyle.addLine(message)
            style = inboxStyle
            return style
        }
    }
}