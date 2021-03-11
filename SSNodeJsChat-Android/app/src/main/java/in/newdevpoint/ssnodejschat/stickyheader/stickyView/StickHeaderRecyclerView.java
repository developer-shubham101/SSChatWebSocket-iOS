package in.newdevpoint.ssnodejschat.stickyheader.stickyView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import in.newdevpoint.ssnodejschat.adapter.ChatAdapterBase;
import in.newdevpoint.ssnodejschat.adapter.HeaderDataImpl;
import in.newdevpoint.ssnodejschat.model.ChatModel;
import in.newdevpoint.ssnodejschat.model.StickyMainData;
import in.newdevpoint.ssnodejschat.stickyheader.stickyData.HeaderData;
import in.newdevpoint.ssnodejschat.utility.UserDetails;

public abstract class StickHeaderRecyclerView<D extends ChatModel, H extends HeaderDataImpl> extends ChatAdapterBase implements StickHeaderItemDecoration.StickyHeaderInterface {

	public static final int ROW_TYPE_HEADER = 1;
	public static final int ROW_TYPE_LEFT_TEXT = 2;
	public static final int ROW_TYPE_LEFT_IMAGE = 3;
	public static final int ROW_TYPE_LEFT_DOCUMENT = 4;
	public static final int ROW_TYPE_LEFT_LOCATION = 5;
	public static final int ROW_TYPE_LEFT_CONTACT = 6;
	public static final int ROW_TYPE_LEFT_VIDEO = 7;
	public static final int ROW_TYPE_LEFT_REPlAY = 8;


	public static final int ROW_TYPE_RIGHT_TEXT = 9;
	public static final int ROW_TYPE_RIGHT_IMAGE = 10;
	public static final int ROW_TYPE_RIGHT_DOCUMENT = 11;
	public static final int ROW_TYPE_RIGHT_LOCATION = 12;
	public static final int ROW_TYPE_RIGHT_CONTACT = 13;
	public static final int ROW_TYPE_RIGHT_VIDEO = 14;
	public static final int ROW_TYPE_RIGHT_REPlAY = 15;


	private List<StickyMainData> mData = new ArrayList<>();

	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		StickHeaderItemDecoration stickHeaderDecoration = new StickHeaderItemDecoration(this);
		recyclerView.addItemDecoration(stickHeaderDecoration);
	}

	@Override
	public final int getItemViewType(int position) {
		if (mData.get(position) instanceof HeaderData) {
			return ROW_TYPE_HEADER;//((HeaderData) mData.get(position)).getHeaderType();
		}
		return getViewId((ChatModel) mData.get(position));
	}

	private int getViewId(ChatModel item) {


		//For Right Side
		if (item.getSender_detail().getId().equals(UserDetails.myDetail.getId())) {
			switch (item.getMessage_type()) {
				case text:
					return ROW_TYPE_RIGHT_TEXT;
				case image:
					return ROW_TYPE_RIGHT_IMAGE;
				case video:
					return ROW_TYPE_RIGHT_VIDEO;
				case document:
					return ROW_TYPE_RIGHT_DOCUMENT;
				case contact:
					return ROW_TYPE_RIGHT_CONTACT;
				case location:
					return ROW_TYPE_RIGHT_LOCATION;
				case replay:
					return ROW_TYPE_RIGHT_REPlAY;
			}
		} else {
			switch (item.getMessage_type()) {
				case text:
					return ROW_TYPE_LEFT_TEXT;
				case image:
					return ROW_TYPE_LEFT_IMAGE;
				case video:
					return ROW_TYPE_LEFT_VIDEO;
				case document:
					return ROW_TYPE_LEFT_DOCUMENT;
				case contact:
					return ROW_TYPE_LEFT_CONTACT;
				case location:
					return ROW_TYPE_LEFT_LOCATION;
				case replay:
					return ROW_TYPE_LEFT_REPlAY;
			}
		}

		return ROW_TYPE_RIGHT_TEXT;

	}

	@Override
	public boolean isHeader(int itemPosition) {
		return mData.get(itemPosition) instanceof HeaderData;
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	@Override
	public int getHeaderLayout(int headerPosition) {
		return ((HeaderData) mData.get(headerPosition)).getHeaderLayout();
	}

	@Override
	public int getHeaderPositionForItem(int itemPosition) {
		int headerPosition = 0;
		do {
			if (this.isHeader(itemPosition)) {
				headerPosition = itemPosition;
				break;
			}
			itemPosition -= 1;
		} while (itemPosition >= 0);
		return headerPosition;
	}

	public void clearAll() {
		mData.clear();
	}

	public void setHeaderAndData(@NonNull List<D> datas, @Nullable HeaderData header) {
//		mData.clear();
//		if (mData == null) {
//			mData = new ArrayList<>();
//		}
		if (header != null) {
			mData.add(header);
		}

		mData.addAll(datas);

		notifyDataSetChanged();
	}


	protected D getDataInPosition(int position) {
		return (D) mData.get(position);
	}

	protected H getHeaderDataInPosition(int position) {
		return (H) mData.get(position);
	}
}
