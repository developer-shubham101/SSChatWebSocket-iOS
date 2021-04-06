package in.newdevpoint.ssnodejschat.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import in.newdevpoint.ssnodejschat.webService.APIClient;


public class FileUtils {


	public static String getImageString(String imageUrl) {
		return APIClient.IMAGE_URL + imageUrl;
	}

	public static boolean isImage(String fileUrl) {
		String lowerCaseFileUrl = fileUrl.toLowerCase();
		return lowerCaseFileUrl.endsWith("jpg") || lowerCaseFileUrl.endsWith("png") || lowerCaseFileUrl.endsWith("gif") || lowerCaseFileUrl.endsWith("PDF") || lowerCaseFileUrl.endsWith("jpeg");

	}

	public static boolean isVideo(String fileUrl) {
		String lowerCaseFileUrl = fileUrl.toLowerCase();
		return lowerCaseFileUrl.endsWith("mov") ||
				fileUrl.endsWith("ogg") ||
				fileUrl.endsWith("MP2") ||
				fileUrl.endsWith("mpeg") ||
				fileUrl.endsWith("mpe") ||
				fileUrl.endsWith("mpv") ||
				fileUrl.endsWith("mp4") ||
				fileUrl.endsWith("wmv") ||
				fileUrl.endsWith("m4p") ||
				fileUrl.endsWith("mpg");
	}

	private static final String VIDEO_DIRECTORY = "/demonuts";

	public static void saveVideoToInternalStorage(String filePath) {
		File newFile;
		try {
			File currentFile = new File(filePath);
			File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);
			newFile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");

			if (!wallpaperDirectory.exists()) {
				wallpaperDirectory.mkdirs();
			}
			if (currentFile.exists()) {
				InputStream in = new FileInputStream(currentFile);
				OutputStream out = new FileOutputStream(newFile);

				// Copy the bits from instream to outstream
				byte[] buf = new byte[1024];
				int len;

				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				Log.v("vii", "Video file saved successfully.");
			} else {
				Log.v("vii", "Video saving failed. Source file missing.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
