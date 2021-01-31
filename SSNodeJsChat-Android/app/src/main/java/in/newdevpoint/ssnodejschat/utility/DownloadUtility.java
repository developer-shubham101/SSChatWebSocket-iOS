package in.newdevpoint.ssnodejschat.utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class DownloadUtility {

    private static final String TAG = "DownloadUtility";
    public static HashMap<String, DownloadRequest> downloadList = new HashMap<>();
    public static String FILE_PATH_FLAG = "flags";
    public static String FILE_PATH_WALLPAPER = "wallpaper";
    public static String FILE_PATH_CHAT_FILES = "chatFile";

    //	public static void downloadFile(Context context, String fileUrl, String fileName, String taskTag, OnDownloadListener onDownloadListener) {
//		File downloadDir = context.getExternalFilesDir(null);
//		downloadFile(context, downloadDir.getAbsolutePath(), fileUrl, fileName, taskTag, onDownloadListener);
//	}
    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    public static String getPath(Context c, String path) {
        File downloadDir = c.getExternalFilesDir(null);
        return downloadDir.getAbsolutePath() + "/" + path;
    }

    public static String createPath(Context c, String path) {
        File downloadDir = c.getExternalFilesDir(null);
        String destinationPath = downloadDir.getAbsolutePath() + "/" + path;
        File file = new File(destinationPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return downloadDir.getAbsolutePath() + "/" + path;
    }

    public static void downloadFile(Context context, String dir, String fileUrl, String fileName, String taskTag, OnDownloadListener onDownloadListener) {

        DownloadRequest task = PRDownloader.download(fileUrl,
                dir, fileName)
                .setTag(taskTag)
                .build();

        task.setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {

            }
        }).setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                Log.d(TAG, "onPause: ");
            }
        }).setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(Progress progress) {
                int currentProgress = (int) ((double) progress.currentBytes / (double) progress.totalBytes * 100);

                Log.d(TAG, "onProgress: " + progress.toString());

                Intent intent = new Intent();
                intent.setAction(BroadCastConstants.INTENT_REFRESH + taskTag);
                intent.putExtra(BroadCastConstants.INTENT_PROGRESS_COUNT, currentProgress);
                context.sendBroadcast(intent);


            }
        }).start(onDownloadListener);
        DownloadUtility.downloadList.put(taskTag, task);


    }
}