package in.newdevpoint.ssnodejschat.stickyheader.stickyData;


import androidx.annotation.LayoutRes;

import in.newdevpoint.ssnodejschat.model.StickyMainData;


public interface HeaderData extends StickyMainData {
	@LayoutRes
	int getHeaderLayout();

}
