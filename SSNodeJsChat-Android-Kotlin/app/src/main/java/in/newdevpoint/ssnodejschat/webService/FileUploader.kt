package `in`.newdevpoint.ssnodejschat.webService

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.MimeTypeMap
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class FileUploader {
    private val uploadInterface: UploadInterface
    var fileUploaderCallback: FileUploaderCallback? = null
    private var files: File? = null
    private var thumb: File? = null
    private var uploadURL = ""
    private var totalFileLength: Long = 0
    private var totalFileUploaded: Long = 0
    private var filekey = ""
    private var auth_token = ""
    fun uploadFiles(url: String, filekey: String, files: File, thumb: File?, fileUploaderCallback: FileUploaderCallback?) {
        uploadFiles(url, filekey, files, thumb, fileUploaderCallback, "")
    }

    fun uploadFiles(url: String, filekey: String, files: File, thumb: File?, fileUploaderCallback: FileUploaderCallback?, auth_token: String) {
        this.fileUploaderCallback = fileUploaderCallback
        this.files = files
        this.thumb = thumb
        uploadURL = url
        this.filekey = filekey
        this.auth_token = auth_token
        totalFileUploaded = 0
        totalFileLength = 0
        totalFileLength = files.length()
        uploadNext()
    }

    private fun uploadNext() {
        uploadSingleFile()
    }

    private fun uploadSingleFile() {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val fileBody = PRRequestBody(files)
        val filePart = MultipartBody.Part.createFormData(filekey, files!!.name, fileBody)
        if (thumb != null) {
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), thumb)
            val thumbnail = MultipartBody.Part.createFormData("thumbnail", thumb!!.name, requestFile)
            builder.addPart(thumbnail)
        }


//        builder.addFormDataPart("channel_id", "sample");
        builder.addFormDataPart("room_id", "sample")
        builder.addPart(filePart)
        val call: Call<JsonElement>
        val requestBody = builder.build()
        call = if (auth_token.isEmpty()) {
            uploadInterface.uploadFile(requestBody)
        } else {
            uploadInterface.uploadFile(requestBody, auth_token)
        }
        call.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
                if (response.isSuccessful) {
                    val jsonElement = response.body()
                    Log.d(TAG, "onResponse: " + jsonElement.toString())
                    val responses = jsonElement.toString()
                    fileUploaderCallback!!.onFinish(responses)
                } else {
                    fileUploaderCallback!!.onError()
                }
                //				uploadNext();
            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                fileUploaderCallback!!.onError()
            }
        })
    }

    private interface UploadInterface {
        // @POST("upload")
        @POST("user-tryster-chat-file")
        fun uploadFile(@Body requestBody: MultipartBody?, @Header("Authorization") authorization: String?): Call<JsonElement>

        //@POST("upload")
        @POST("user-tryster-chat-file")
        fun uploadFile(@Body requestBody: MultipartBody?): Call<JsonElement>
    }

    interface FileUploaderCallback {
        fun onError()
        fun onFinish(responses: String?)
        fun onProgressUpdate(currentpercent: Int, totalpercent: Int)
    }

    inner class PRRequestBody(private val mFile: File?) : RequestBody() {
        override fun contentType(): MediaType? {
            // i want to upload only images
//			return MediaType.parse("image/*");
            val mimeType = getMimeType(mFile!!.absolutePath)
            return MediaType.parse(mimeType)
        }

        @Throws(IOException::class)
        override fun contentLength(): Long {
            return mFile!!.length()
        }

        @Throws(IOException::class)
        override fun writeTo(sink: BufferedSink) {
            val fileLength = mFile!!.length()
            val buffer = ByteArray(Companion.DEFAULT_BUFFER_SIZE)
            val `in` = FileInputStream(mFile)
            var uploaded: Long = 0
            try {
                var read: Int
                val handler = Handler(Looper.getMainLooper())
                while (`in`.read(buffer).also { read = it } != -1) {

                    // update progress on UI thread
                    handler.post(ProgressUpdater(uploaded, fileLength))
                    uploaded += read.toLong()
                    sink.write(buffer, 0, read)
                }
            } finally {
                `in`.close()
            }
        }


    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {
        override fun run() {
            val current_percent = (100 * mUploaded / mTotal).toInt()
            val total_percent = (100 * (totalFileUploaded + mUploaded) / totalFileLength).toInt()
            fileUploaderCallback!!.onProgressUpdate(current_percent, total_percent)
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
        private const val TAG = "FileUploader"

        // url = file path or whatever suitable URL you want.
        fun getMimeType(url: String?): String? {
            var type: String? = null
            val extension = MimeTypeMap.getFileExtensionFromUrl(url)
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
            return type
        }
    }

    //	private String responses;
    init {
        uploadInterface = APIClient.getClient()!!.create(UploadInterface::class.java)
    }
}