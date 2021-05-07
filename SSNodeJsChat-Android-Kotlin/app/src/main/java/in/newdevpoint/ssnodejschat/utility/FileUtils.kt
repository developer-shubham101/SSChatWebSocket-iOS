package `in`.newdevpoint.ssnodejschat.utility

import `in`.newdevpoint.ssnodejschat.webService.APIClient
import android.os.Environment
import android.util.Log
import java.io.*
import java.util.*

object FileUtils {
    fun getImageString(imageUrl: String): String {
        return APIClient.IMAGE_URL + imageUrl
    }

    fun isImage(fileUrl: String): Boolean {
        val lowerCaseFileUrl = fileUrl.toLowerCase()
        return lowerCaseFileUrl.endsWith("jpg") || lowerCaseFileUrl.endsWith("png") || lowerCaseFileUrl.endsWith("gif") || lowerCaseFileUrl.endsWith("PDF") || lowerCaseFileUrl.endsWith("jpeg")
    }

    fun isVideo(fileUrl: String): Boolean {
        val lowerCaseFileUrl = fileUrl.toLowerCase()
        return lowerCaseFileUrl.endsWith("mov") ||
                fileUrl.endsWith("ogg") ||
                fileUrl.endsWith("MP2") ||
                fileUrl.endsWith("mpeg") ||
                fileUrl.endsWith("mpe") ||
                fileUrl.endsWith("mpv") ||
                fileUrl.endsWith("mp4") ||
                fileUrl.endsWith("wmv") ||
                fileUrl.endsWith("m4p") ||
                fileUrl.endsWith("mpg")
    }

    private const val VIDEO_DIRECTORY = "/demonuts"
    fun saveVideoToInternalStorage(filePath: String?) {
        val newFile: File
        try {
            val currentFile = File(filePath)
            val wallpaperDirectory = File(Environment.getExternalStorageDirectory().toString() + VIDEO_DIRECTORY)
            newFile = File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".mp4")
            if (!wallpaperDirectory.exists()) {
                wallpaperDirectory.mkdirs()
            }
            if (currentFile.exists()) {
                val `in`: InputStream = FileInputStream(currentFile)
                val out: OutputStream = FileOutputStream(newFile)

                // Copy the bits from instream to outstream
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                Log.v("vii", "Video file saved successfully.")
            } else {
                Log.v("vii", "Video saving failed. Source file missing.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}