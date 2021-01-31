package in.newdevpoint.ssnodejschat.webService;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;

import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class FileUploader {

    private static final String TAG = "FileUploader";
    public FileUploaderCallback fileUploaderCallback;

    private File files;
    private @Nullable
    File thumb;
    private String uploadURL = "";
    private long totalFileLength = 0;
    private long totalFileUploaded = 0;
    private String filekey = "";
    private final UploadInterface uploadInterface;
    private String auth_token = "";
//	private String responses;

    public FileUploader() {
        uploadInterface = APIClient.getClient().create(UploadInterface.class);
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public void uploadFiles(String url, String filekey, File files, File thumb, FileUploaderCallback fileUploaderCallback) {
        uploadFiles(url, filekey, files, thumb, fileUploaderCallback, "");
    }

    public void uploadFiles(String url, String filekey, File files, @Nullable File thumb, FileUploaderCallback fileUploaderCallback, String auth_token) {
        this.fileUploaderCallback = fileUploaderCallback;
        this.files = files;

        this.thumb = thumb;

        this.uploadURL = url;
        this.filekey = filekey;
        this.auth_token = auth_token;
        totalFileUploaded = 0;
        totalFileLength = 0;


        totalFileLength = files.length();

        uploadNext();
    }

    private void uploadNext() {

        uploadSingleFile();

    }

    private void uploadSingleFile() {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        PRRequestBody fileBody = new PRRequestBody(files);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(filekey, files.getName(), fileBody);


        if (thumb != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), thumb);
            MultipartBody.Part thumbnail = MultipartBody.Part.createFormData("thumbnail", thumb.getName(), requestFile);
            builder.addPart(thumbnail);
        }


        builder.addFormDataPart("channel_id", "sample");
        builder.addPart(filePart);

        Call<JsonElement> call;

        MultipartBody requestBody = builder.build();

        if (auth_token.isEmpty()) {
            call = uploadInterface.uploadFile(requestBody);
        } else {
            call = uploadInterface.uploadFile(requestBody, auth_token);
        }

        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonElement jsonElement = response.body();
                    Log.d(TAG, "onResponse: " + jsonElement.toString());
                    String responses = jsonElement.toString();
                    fileUploaderCallback.onFinish(responses);
                } else {
                    fileUploaderCallback.onError();
                }
//				uploadNext();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                fileUploaderCallback.onError();
            }
        });
    }

    private interface UploadInterface {
        @POST("upload")
        Call<JsonElement> uploadFile(@Body MultipartBody requestBody, @Header("Authorization") String authorization);

        @POST("upload")
        Call<JsonElement> uploadFile(@Body MultipartBody requestBody);
    }

    public interface FileUploaderCallback {
        void onError();

        void onFinish(String responses);

        void onProgressUpdate(int currentpercent, int totalpercent);
    }

    public class PRRequestBody extends RequestBody {
        private static final int DEFAULT_BUFFER_SIZE = 2048;
        private final File mFile;

        public PRRequestBody(final File file) {
            mFile = file;

        }

        @Override
        public MediaType contentType() {
            // i want to upload only images
//			return MediaType.parse("image/*");

            String mimeType = getMimeType(mFile.getAbsolutePath());

            return MediaType.parse(mimeType);
        }

        @Override
        public long contentLength() throws IOException {
            return mFile.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            long fileLength = mFile.length();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            FileInputStream in = new FileInputStream(mFile);
            long uploaded = 0;

            try {
                int read;
                Handler handler = new Handler(Looper.getMainLooper());
                while ((read = in.read(buffer)) != -1) {

                    // update progress on UI thread
                    handler.post(new ProgressUpdater(uploaded, fileLength));
                    uploaded += read;
                    sink.write(buffer, 0, read);
                }
            } finally {
                in.close();
            }
        }
    }

    private class ProgressUpdater implements Runnable {
        private final long mUploaded;
        private final long mTotal;

        public ProgressUpdater(long uploaded, long total) {
            mUploaded = uploaded;
            mTotal = total;
        }

        @Override
        public void run() {
            int current_percent = (int) (100 * mUploaded / mTotal);
            int total_percent = (int) (100 * (totalFileUploaded + mUploaded) / totalFileLength);
            fileUploaderCallback.onProgressUpdate(current_percent, total_percent);
        }
    }
}