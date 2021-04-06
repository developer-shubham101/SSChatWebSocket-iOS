package in.newdevpoint.ssnodejschat.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.RequiresApi;

import in.newdevpoint.ssnodejschat.webService.APIClient;


public class Utils {


	public static String getImageString(String imageUrl) {
		return APIClient.IMAGE_URL + imageUrl;
	}


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static Bitmap blurRenderScript(Context context, Bitmap srcBitmap, int radius) {
		Bitmap bitmap = Bitmap.createBitmap(
				srcBitmap.getWidth(), srcBitmap.getHeight(),
				Bitmap.Config.ARGB_8888);

		RenderScript renderScript = RenderScript.create(context);

		Allocation blurInput = Allocation.createFromBitmap(renderScript, srcBitmap);
		Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
				Element.U8_4(renderScript));
		blur.setInput(blurInput);
		blur.setRadius(radius); // radius must be 0 < r <= 25
		blur.forEach(blurOutput);

		blurOutput.copyTo(bitmap);
		renderScript.destroy();

		return bitmap;
	}
}
