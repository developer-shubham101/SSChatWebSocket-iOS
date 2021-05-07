package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout


class VideoPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        val video_view: VideoView = findViewById(R.id.video_view)
        val setBackBtn: ConstraintLayout = findViewById(R.id.conBackBtn)
        setBackBtn.setOnClickListener { finish() }
        if (intent != null) {
            val video_url = intent.getStringExtra(VIDEO_PLAY_KEY)
            video_view.setVideoURI(Uri.parse(video_url))
            video_view.start()
        }
    }

    companion object {
        const val VIDEO_PLAY_KEY = "VIDEO_PLAY_KEY"
    }
}