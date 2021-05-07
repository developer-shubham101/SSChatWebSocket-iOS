package `in`.newdevpoint.ssnodejschat.fragment

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.FragmentPlayAudioBinding
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class PlayAudioFragment : Fragment() {
    private var playbackStateListener: PlaybackStateListener? = null
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var binding: FragmentPlayAudioBinding
    private var playAudioCallback: PlayAudioCallback? = null
    private var isPlay = false
    fun setPlayAudioCallback(playAudioCallback: PlayAudioCallback?) {
        this.playAudioCallback = playAudioCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_audio, container, false)
        binding.audioView.setBackgroundColor(resources.getColor(R.color.transparent))
        binding.audioView.setShutterBackgroundColor(R.color.transparent)
        binding.closeAudioPlayer.setOnClickListener {
            releasePlayer()
            playAudioCallback!!.closeAudioPlayer()
        }
        return binding.getRoot()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//		fileName = getExternalCacheDir().getAbsolutePath();
//		fileName += "/video.mp4";

//		if (getArguments() != null) {
//			if (getArguments().containsKey(BUNDLE_EXTRA_FILE_PATH)) {
//				fileName = getArguments().getString(BUNDLE_EXTRA_FILE_PATH);
//			} else {
//				Toast.makeText(getContext(), "Video can't play", Toast.LENGTH_SHORT).show();
//
//			}
//		}
        playbackStateListener = PlaybackStateListener()
    }

    override fun onStart() {
        super.onStart()
        if (isPlay) {
//		if (Util.SDK_INT > 23) {
            initializePlayer()
            //		}
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPlay) {
            hideSystemUi()
            //		if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
            //		}
        }
    }

    override fun onPause() {
        super.onPause()

//		if (Util.SDK_INT <= 23) {
        releasePlayer()
        //		}
    }

    override fun onStop() {
        super.onStop()
        //		if (Util.SDK_INT > 23) {
        releasePlayer()
        //		}
    }

    private fun initializePlayer() {
        if (player == null) {
            val trackSelector = DefaultTrackSelector()
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd())
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        }
        binding!!.audioView.player = player
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

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ExtractorMediaSource(uri,
                DefaultDataSourceFactory(context, "Firebase"),
                DefaultExtractorsFactory(), null, null)
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        binding!!.audioView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE //						| View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        binding!!.audioView.useArtwork = false
        binding!!.audioView.controllerHideOnTouch = false
    }

    fun play(filePath: String?) {
        isPlay = true
        fileName = filePath
        initializePlayer()
    }

    interface PlayAudioCallback {
        fun closeAudioPlayer()
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
        private const val TAG = "PlayAudioFragment:"
        private var fileName: String? = null
    }

    init {
        Log.d(TAG, "UploadFileProgressFragment: ")
    }
}