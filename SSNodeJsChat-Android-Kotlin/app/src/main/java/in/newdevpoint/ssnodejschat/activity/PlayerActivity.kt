package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity() {
    private var playbackStateListener: PlaybackStateListener? = null
    private var playerView: PlayerView? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

//		fileName = getExternalCacheDir().getAbsolutePath();
//		fileName += "/video.mp4";
        if (intent.hasExtra(INTENT_EXTRA_FILE_PATH)) {
            fileName = intent.getStringExtra(INTENT_EXTRA_FILE_PATH)
        } else {
            Toast.makeText(this, "Video can't play", Toast.LENGTH_SHORT).show()
            finish()
        }
        playerView = findViewById(R.id.video_view)
        playbackStateListener = PlaybackStateListener()
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector()
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd())
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        }
        playerView!!.player = player
        //		Uri uri = Uri.parse(getString(R.string.media_url_dash));
        val uri = Uri.parse(fileName)
        val mediaSource = buildMediaSource(uri)
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.addListener(playbackStateListener)
        player!!.prepare(mediaSource, false, false)
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            player!!.removeListener(playbackStateListener)
            player!!.release()
            player = null
        }
    }

    /* private MediaSource buildMediaSource(Uri uri) {
	   DataSource.Factory dataSourceFactory =
			   new DefaultDataSourceFactory(this, "exoplayer-codelab");
	   DashMediaSource.Factory mediaSourceFactory = new DashMediaSource.Factory(dataSourceFactory);
	   return mediaSourceFactory.createMediaSource(uri);
	 }*/
    //	private MediaSource buildMediaSource(Uri uri) {
    //		DataSource.Factory dataSourceFactory =
    //				new DefaultDataSourceFactory(this, "Firebase");
    //		DashMediaSource.Factory mediaSourceFactory = new DashMediaSource.Factory(dataSourceFactory);
    //		return mediaSourceFactory.createMediaSource(uri);
    //	}
    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource(uri,
                DefaultDataSourceFactory(this, "Firebase"),
                DefaultExtractorsFactory(), null, null)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        playerView!!.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        playerView!!.useArtwork = false
        playerView!!.controllerHideOnTouch = false
    }

    private class PlaybackStateListener : Player.EventListener {
        override fun onPlayerStateChanged(playWhenReady: Boolean,
                                          playbackState: Int) {
            val stateString: String
            stateString = when (playbackState) {
                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                else -> "UNKNOWN_STATE             -"
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady)
        }
    }

    companion object {
        const val INTENT_EXTRA_FILE_PATH = "INTENT_EXTRA_FILE_PATH"
        private val TAG = PlayerActivity::class.java.name
        private var fileName: String? = null
    }
}