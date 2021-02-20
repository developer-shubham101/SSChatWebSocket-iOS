package in.newdevpoint.ssnodejschat.model;

import in.newdevpoint.ssnodejschat.webService.APIClient;

public class MediaModel {
    String file_url = "";
    MediaMetaModel file_meta;

    public String getFile_url() {
        return APIClient.IMAGE_URL + file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public MediaMetaModel getFile_meta() {
        return file_meta;
    }

    public void setFile_meta(MediaMetaModel file_meta) {
        this.file_meta = file_meta;
    }
//		public MediaModel(HashMap<String, Object> rawData) {
////			file_url = new ConvertData<String>(rawData.get("file_url")).getValue(String.class);
//		}
}