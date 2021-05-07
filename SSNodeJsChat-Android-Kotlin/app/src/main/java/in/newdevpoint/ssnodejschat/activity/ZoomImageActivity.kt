package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.Glide

class ZoomImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_image)
        val zoomImage: ImageView = findViewById(R.id.zoomImage)
        val conBackBtn: ConstraintLayout = findViewById(R.id.conBackBtn)
        val imageUrl = intent.getStringExtra(INTENT_EXTRA_URL)
        Glide.with(this).load(imageUrl).transform().into(zoomImage)

//        try {
//            URL uRl = new URL(imageUrl);
//            String replaceStr = ".200x200_q100." + FilenameUtils.getExtension(uRl.getPath());
//            imageUrl.replace(replaceStr, "");
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        zoomImage.setOnTouchListener(ImageMatrixTouchHandler(this))
        conBackBtn.setOnClickListener(View.OnClickListener { v: View? -> finish() })
    }

    fun onClick(v: View?) {}

    companion object {
        const val INTENT_EXTRA_URL = "INTENT_EXTRA_URL"
    }
}