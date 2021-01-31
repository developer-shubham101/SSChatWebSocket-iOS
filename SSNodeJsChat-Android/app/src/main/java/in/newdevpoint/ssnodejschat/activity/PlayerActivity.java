package in.newdevpoint.ssnodejschat.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import in.newdevpoint.ssnodejschat.R;

/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends AppCompatActivity {

	public static final String INTENT_EXTRA_FILE_PATH = "INTENT_EXTRA_FILE_PATH";
	private static final String TAG = PlayerActivity.class.getName();
	private static String fileName = null;
	private PlaybackStateListener playbackStateListener;
	private PlayerView playerView;
	private SimpleExoPlayer player;
	private boolean playWhenReady = true;
	private int currentWindow = 0;
	private long playbackPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

//		fileName = getExternalCacheDir().getAbsolutePath();
//		fileName += "/video.mp4";

		if (getIntent().hasExtra(INTENT_EXTRA_FILE_PATH)) {
			fileName = getIntent().getStringExtra(INTENT_EXTRA_FILE_PATH);
		} else {
			Toast.makeText(this, "Video can't play", Toast.LENGTH_SHORT).show();
			finish();
		}

		playerView = findViewById(R.id.video_view);


		playbackStateListener = new PlaybackStateListener();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (Util.SDK_INT > 23) {
			initializePlayer();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		hideSystemUi();
		if ((Util.SDK_INT <= 23 || player == null)) {
			initializePlayer();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (Util.SDK_INT <= 23) {
			releasePlayer();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (Util.SDK_INT > 23) {
			releasePlayer();
		}
	}

	private void initializePlayer() {
		if (player == null) {
			DefaultTrackSelector trackSelector = new DefaultTrackSelector();
			trackSelector.setParameters(
					trackSelector.buildUponParameters().setMaxVideoSizeSd());
			player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
		}

		playerView.setPlayer(player);
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

	private MediaSource buildMediaSource(Uri uri) {
		return new ExtractorMediaSource(uri,
				new DefaultDataSourceFactory(this, "Firebase"),
				new DefaultExtractorsFactory(), null, null);

	}


	@SuppressLint("InlinedApi")
	private void hideSystemUi() {
		playerView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		);

		playerView.setUseArtwork(false);
		playerView.setControllerHideOnTouch(false);

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