package in.newdevpoint.ssnodejschat.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.FragmentPlayAudioBinding;

public class PlayAudioFragment extends Fragment {

    private static final String TAG = "PlayAudioFragment:";
    private static String fileName = null;
    private PlaybackStateListener playbackStateListener;
    private SimpleExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private FragmentPlayAudioBinding binding;
    private PlayAudioCallback playAudioCallback;
    private boolean isPlay = false;

    public PlayAudioFragment() {
        Log.d(TAG, "UploadFileProgressFragment: ");
    }

    public void setPlayAudioCallback(PlayAudioCallback playAudioCallback) {
        this.playAudioCallback = playAudioCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_audio, container, false);


        binding.audioView.setBackgroundColor(getResources().getColor(R.color.transparent));
        binding.audioView.setShutterBackgroundColor(R.color.transparent);

        binding.closeAudioPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releasePlayer();
                playAudioCallback.closeAudioPlayer();

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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


        playbackStateListener = new PlaybackStateListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isPlay) {
//		if (Util.SDK_INT > 23) {
            initializePlayer();
//		}
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPlay) {
            hideSystemUi();
//		if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
//		}
        }
    }

    @Override
    public void onPause() {
        super.onPause();

//		if (Util.SDK_INT <= 23) {
        releasePlayer();
//		}
    }

    @Override
    public void onStop() {
        super.onStop();
//		if (Util.SDK_INT > 23) {
        releasePlayer();
//		}
    }


    private void initializePlayer() {
        if (player == null) {
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        }

        binding.audioView.setPlayer(player);
//		Uri uri = Uri.parse(getString(R.string.media_url_dash));
        Uri uri = Uri.parse(fileName);


        MediaSource mediaSource = buildMediaSource(uri);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.addListener(playbackStateListener);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(playbackStateListener);
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(getContext(), "Firebase"),
                new DefaultExtractorsFactory(), null, null);

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        binding.audioView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
//						| View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );

        binding.audioView.setUseArtwork(false);
        binding.audioView.setControllerHideOnTouch(false);

    }

    public void play(String filePath) {
        isPlay = true;
        fileName = filePath;

        initializePlayer();
    }

    public interface PlayAudioCallback {
        void closeAudioPlayer();
    }

    private static class PlaybackStateListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }
    }
}
