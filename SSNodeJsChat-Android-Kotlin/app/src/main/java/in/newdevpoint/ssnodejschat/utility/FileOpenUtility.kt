package `in`.newdevpoint.ssnodejschat.utility

import android.app.Activity
import android.content.Intent
import android.net.Uri

object FileOpenUtility {
    fun openFile(a: Activity, url: String) {
        // Create URI
        val uri = Uri.parse(url)
        var intent: Intent? = null
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.contains(".doc") || url.contains(".docx")) {
            // Word document
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/msword")
        } else if (url.contains(".pdf")) {
            // PDF file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
        } else if (url.contains(".ppt") || url.contains(".pptx")) {
            // Powerpoint file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
        } else if (url.contains(".xls") || url.contains(".xlsx")) {
            // Excel file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (url.contains(".rtf")) {
            // RTF file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/rtf")
        } else if (url.contains(".wav")) {
            // WAV audio file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "audio/x-wav")
        } else if (url.contains(".gif")) {
            // GIF file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "imageUrl/gif")
        } else if (url.contains(".jpg") || url.contains(".jpeg")) {
            // JPG file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "imageUrl/jpeg")
        } else if (url.contains(".png")) {
            // PNG file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "imageUrl/png")
        } else if (url.contains(".txt")) {
            // Text file
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "text/plain")
        } else if (url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
            // Video files
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "video/*")
        } else if (url.contains(".zip") || url.contains(".rar")) {
            // ZIP Files
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/zip")
        } else {
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "*/*")
        }
        a.startActivity(intent)
    }

    fun isVideo(url: String): Boolean {
        return url.contains(".mpg") || url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")
    }

    fun isAudio(url: String): Boolean {
        return url.contains(".mp3") || url.contains(".m4a") || url.contains(".bin")
    }
}