package in.newdevpoint.ssnodejschat.adapter;

import androidx.annotation.LayoutRes;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.newdevpoint.ssnodejschat.stickyheader.stickyData.HeaderData;


public class HeaderDataImpl implements HeaderData {

	@LayoutRes
	private final int layoutResource;

	private String title;

	public HeaderDataImpl(@LayoutRes int layoutResource, Date date) {
		this.layoutResource = layoutResource;
		this.title = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@LayoutRes
	@Override
	public int getHeaderLayout() {
		return layoutResource;
	}


}
