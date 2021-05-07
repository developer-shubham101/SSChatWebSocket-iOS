package `in`.newdevpoint.ssnodejschat

import android.app.Application
import android.content.Context
import com.bumptech.glide.request.RequestOptions

// AAAA5sgZE7o:APA91bGdnxQsMAvPjrHmEdcjIWseqPMJA_89mq3Y-Wu9EZXiAtzUjGJx1ldPzEvu2YunT6xeFbzP2sEJkoZYP7Zqip0-bkzS3YX1KD8yVPm-DGL0RRHUso2Yrd1Rdjxc_B_sH56pnb4d
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Companion.applicationContext = applicationContext
    }

    companion object {
        
        var USER_PROFILE_DEFAULT_GLIDE_CONFIG = RequestOptions().placeholder(R.drawable.user_profile_image).error(R.drawable.user_profile_image)
        lateinit var applicationContext: Context
    }
}