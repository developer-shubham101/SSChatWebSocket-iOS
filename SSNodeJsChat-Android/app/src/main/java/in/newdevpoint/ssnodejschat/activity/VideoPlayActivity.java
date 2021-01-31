package in.newdevpoint.ssnodejschat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import in.newdevpoint.ssnodejschat.R;


public class VideoPlayActivity extends AppCompatActivity {
    public static final String VIDEO_PLAY_KEY = "VIDEO_PLAY_KEY";
    com.devbrackets.android.exomedia.ui.widget.VideoView video_view;
    private ConstraintLayout setBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        video_view = findViewById(R.id.video_view);
        setBackBtn = findViewById(R.id.conBackBtn);
        setBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (getIntent() != null) {
            String video_url = getIntent().getStringExtra(VIDEO_PLAY_KEY);
            video_view.setVideoURI(Uri.parse(video_url));
            video_view.start();
        }

    }
    }

