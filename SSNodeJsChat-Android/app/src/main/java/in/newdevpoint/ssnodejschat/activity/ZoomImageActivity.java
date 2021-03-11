package in.newdevpoint.ssnodejschat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

import org.apache.commons.io.FilenameUtils;

import java.net.MalformedURLException;
import java.net.URL;

import in.newdevpoint.ssnodejschat.R;


public class ZoomImageActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";

    private ImageView zoomImage;
    private ConstraintLayout conBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        zoomImage = findViewById(R.id.zoomImage);
        conBackBtn = findViewById(R.id.conBackBtn);

        String imageUrl = getIntent().getStringExtra(INTENT_EXTRA_URL);
        Glide.with(this).load(imageUrl).transform( ).into(zoomImage);

//        try {
//            URL uRl = new URL(imageUrl);
//            String replaceStr = ".200x200_q100." + FilenameUtils.getExtension(uRl.getPath());
//            imageUrl.replace(replaceStr, "");
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        zoomImage.setOnTouchListener(new ImageMatrixTouchHandler(this));

        conBackBtn.setOnClickListener(v -> finish());
    }

    public void onClick(View v) {

    }
}
