package in.newdevpoint.ssnodejschat.model;

import com.google.gson.annotations.SerializedName;

public class UploadFileMode {
	@SerializedName("thumbnail")
	private String thumbnail;

	@SerializedName("file_path")
	private String file;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("channel_id")
	private String channelId;

	public String getThumbnail() {
		return thumbnail;
	}

	public String getFile() {
		return file;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public String getUserId() {
		return userId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getId() {
		return id;
	}

	public String getChannelId() {
		return channelId;
	}
}