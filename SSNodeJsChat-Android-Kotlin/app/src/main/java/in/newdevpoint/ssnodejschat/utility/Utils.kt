package `in`.newdevpoint.ssnodejschat.utility

import `in`.newdevpoint.ssnodejschat.webService.APIClient
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.RequiresApi

object Utils {
    fun getImageString(imageUrl: String): String {
        return APIClient.IMAGE_URL + imageUrl
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun blurRenderScript(context: Context?, srcBitmap: Bitmap, radius: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
                srcBitmap.width, srcBitmap.height,
                Bitmap.Config.ARGB_8888)
        val renderScript = RenderScript.create(context)
        val blurInput = Allocation.createFromBitmap(renderScript, srcBitmap)
        val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)
        val blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript))
        blur.setInput(blurInput)
        blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
        blur.forEach(blurOutput)
        blurOutput.copyTo(bitmap)
        renderScript.destroy()
        return bitmap
    }
}