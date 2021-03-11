package in.newdevpoint.ssnodejschat.model;

import androidx.annotation.NonNull;

public class MediaMetaModel {
    public static String KEY_FILE_TYPE = "file_type";
    public static String KEY_FILE_NAME = "file_name";
    public static String KEY_FILE_SIZE = "file_size";
    public static String KEY_FILE_THUMB = "thumbnail";
    private String file_type = "";
    private String thumbnail = "";
    private String file_name = "";
    private Double file_size = 0.0;

    public static String getKeyFileType() {
        return KEY_FILE_TYPE;
    }

    public static void setKeyFileType(String keyFileType) {
        KEY_FILE_TYPE = keyFileType;
    }

    public static String getKeyFileName() {
        return KEY_FILE_NAME;
    }

    public static void setKeyFileName(String keyFileName) {
        KEY_FILE_NAME = keyFileName;
    }

    public static String getKeyFileSize() {
        return KEY_FILE_SIZE;
    }

    public static void setKeyFileSize(String keyFileSize) {
        KEY_FILE_SIZE = keyFileSize;
    }

    public static String getKeyFileThumb() {
        return KEY_FILE_THUMB;
    }

    public static void setKeyFileThumb(String keyFileThumb) {
        KEY_FILE_THUMB = keyFileThumb;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Double getFile_size() {
        return file_size;
    }

    public void setFile_size(Double file_size) {
        this.file_size = file_size;
    }


    //	enum MediaType: String {
//		case imageJPG = "jpg"
//		case imagePNG = "png"
//		case audioM4A = "m4a"
//		case videoMP4 = "mp4"
//		case none = ""
//	}
    public enum MediaType {
        imageJPG("jpg"),
        imagePNG("png"),
        audioM4A("m4a"),
        videoMP4("mp4"),
        none("");


        private final String name;

        MediaType(String s) {
            name = s;
        }

        public static MediaMetaModel.MediaType getValueFromEnum(String rawValue) {
            if (rawValue.equals(imageJPG.toString())) {
                return imageJPG;
            } else if (rawValue.equals(imagePNG.toString())) {
                return imagePNG;
            } else if (rawValue.equals(audioM4A.toString())) {
                return audioM4A;
            } else if (rawValue.equals(videoMP4.toString())) {
                return videoMP4;
            }
            return none;
        }

        public boolean equalsName(String otherName) {
            // (otherName == null) check is not needed because name.equals(null) returns false
            return name.equals(otherName);
        }

        @NonNull
        @Override
        public String toString() {
            return this.name;
        }


    }
}