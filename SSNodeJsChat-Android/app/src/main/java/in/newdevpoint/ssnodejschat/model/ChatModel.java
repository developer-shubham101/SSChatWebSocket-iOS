package in.newdevpoint.ssnodejschat.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import in.newdevpoint.ssnodejschat.AppApplication;
import in.newdevpoint.ssnodejschat.utility.DownloadUtility;


public class ChatModel implements StickyMainData {

    private final Date messageDate;
    private String documentId;
    private String message = "";
    private String message_on = "";
    private FSUsersModel sender_detail = new FSUsersModel();
    private Object message_content;
    private DownloadStatus downloadStatus = DownloadStatus.PENDING;
    private Date createdDate;
    private ChatModel.MessageType message_type;

    public ChatModel(String documentId, JSONObject rawData, FSUsersModel sender_detail) throws JSONException {
        this.documentId = documentId;
        this.sender_detail = sender_detail;


        message = rawData.getString("message");

        message_type = MessageType.getValueFromEnum(rawData.getString("message_type"));


        switch (message_type) {
            case text: {
                break;
            }
            case image:
            case video:
            case document: {
                try {
                    JSONObject messageContent = rawData.getJSONObject("message_content");

                    Gson gson = new Gson();
                    Type type = new TypeToken<MediaModel>() {
                    }.getType();
                    message_content = gson.fromJson(messageContent.toString(), type);

                    System.out.println(message_content);

                    boolean isAppDownloaded = false;
                    try {
                        URL url = new URL(((MediaModel) message_content).getFile_url());
                        String downloadFileName = FilenameUtils.getName(url.getPath());


                        File downloadFile = new File(DownloadUtility.getPath(AppApplication.applicationContext, DownloadUtility.FILE_PATH_CHAT_FILES) + "/" + downloadFileName);
                        isAppDownloaded = downloadFile.exists();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (isAppDownloaded) {
                        setDownloadStatus(ChatModel.DownloadStatus.DOWNLOADED);
                    } else {
                        setDownloadStatus(ChatModel.DownloadStatus.PENDING);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case contact: {
                try {
                    JSONObject messageContent = rawData.getJSONObject("message_content");

                    Gson gson = new Gson();
                    Type type = new TypeToken<ContactModel>() {
                    }.getType();
                    message_content = gson.fromJson(messageContent.toString(), type);

                    System.out.println(message_content);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case location: {
                try {
                    JSONObject messageContent = rawData.getJSONObject("message_content");

                    Gson gson = new Gson();
                    Type type = new TypeToken<LocationModel>() {
                    }.getType();
                    message_content = gson.fromJson(messageContent.toString(), type);

                    System.out.println(message_content);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }


        }


      /*  Timestamp time = (Timestamp) rawData.getString("time");


        messageDate = new Date(time.getSeconds() * 1000);
//		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tempDate);


        message_on = new SimpleDateFormat("HH:mm:ss").format(messageDate);

        String tmpDate = new SimpleDateFormat("yyyy-MM-dd").format(messageDate);

        try {
            createdDate = new SimpleDateFormat("yyyy-MM-dd").parse(tmpDate + " 12:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

//		createdDate = tempDate;
//		createdDate = new SimpleDateFormat("yyyy-MM-dd").format(tempDate);

        createdDate = new Date();
        messageDate = new Date();

    }

    public Date getMessageDate() {
        return messageDate;
    }

    public ChatModel.MessageType getMessage_type() {
        return message_type;
    }

    public void setMessage_type(ChatModel.MessageType message_type) {
        this.message_type = message_type;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getMessage_on() {
        return message_on;
    }

    public void setMessage_on(String message_on) {
        this.message_on = message_on;
    }

    public FSUsersModel getSender_detail() {
        return sender_detail;
    }

    public void setSender_detail(FSUsersModel sender_detail) {
        this.sender_detail = sender_detail;
    }

    public Object getMessage_content() {
        return message_content;
    }

    public void setMessage_content(Object message_content) {
        this.message_content = message_content;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public enum MessageType {
        text("TEXT"),
        image("IMAGE"),
        document("DOCUMENT"),
        location("LOCATION"),
        contact("CONTACT"),
        video("VIDEO"),
        replay("REPlAY");


        private final String name;

        MessageType(String s) {
            name = s;
        }

        public static MessageType getValueFromEnum(String rawValue) {
            if (rawValue.equals(text.toString())) {
                return text;
            } else if (rawValue.equals(MessageType.image.toString())) {
                return MessageType.image;
            } else if (rawValue.equals(MessageType.document.toString())) {
                return MessageType.document;
            } else if (rawValue.equals(MessageType.location.toString())) {
                return MessageType.location;
            } else if (rawValue.equals(MessageType.contact.toString())) {
                return MessageType.contact;
            } else if (rawValue.equals(MessageType.video.toString())) {
                return MessageType.video;
            } else if (rawValue.equals(MessageType.replay.toString())) {
                return MessageType.replay;
            }
            return text;
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

    public enum DownloadStatus {
        PENDING,
        DOWNLOADING,
        DOWNLOADED
    }

    class ConvertData<T> {
        Object o;

        public ConvertData(Object o) {
            this.o = o;
        }

        public T getValue(Class<T> type) {
            T castValue = castIt(type);
            if (type == String.class && castValue == null) {
                return (T) "";
            }
            return castValue;
        }

        private @Nullable
        T castIt(Class<T> type) {
            try {
                return type.cast(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}

