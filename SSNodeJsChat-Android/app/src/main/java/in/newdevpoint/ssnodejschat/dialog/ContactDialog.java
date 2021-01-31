package in.newdevpoint.ssnodejschat.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import in.newdevpoint.ssnodejschat.R;


public class ContactDialog extends Dialog implements View.OnClickListener {
	private final DialogBuilder dialogBuilder;
	public Button dialogAddContactNew;
	private TextView dialogContactName;
	private TextView dialogTitle;
	private Button dialogAddContactExisting;
	private ImageView dialogIcon;

	ContactDialog(DialogBuilder dialogBuilder) {
		super(dialogBuilder.getActivity());
		this.dialogBuilder = dialogBuilder;
	}

	public void update() {
		dialogAddContactExisting.setOnClickListener(this);
		dialogAddContactNew.setOnClickListener(this);
		dialogTitle.setText(dialogBuilder.getTitle());
		if (dialogBuilder.getResourceId() != 0)
			dialogIcon.setImageResource(dialogBuilder.getResourceId());
	}

	public void updateContactName() {
		dialogContactName.setText(dialogBuilder.getContactNumber());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getWindow() != null) {
			getWindow().setBackgroundDrawableResource(R.color.transparent);

		}
		setContentView(R.layout.custom_dialog);
		dialogAddContactExisting = findViewById(R.id.dialogAddContactExisting);
		dialogAddContactNew = findViewById(R.id.dialogAddContactNew);
		dialogContactName = findViewById(R.id.dialogContactName);
		dialogTitle = findViewById(R.id.dialogTitle);
		dialogIcon = findViewById(R.id.dialogIcon);

		update();
		updateContactName();

		setCancelable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialogAddContactNew:
				if (dialogBuilder != null) {
					dialogBuilder.getOnItemClick().addContactNew(this);
				}
				break;
			case R.id.dialogAddContactExisting:
				if (dialogBuilder != null) {
					dialogBuilder.getOnItemClick().close(this);
				}
				break;
			default:
				break;
		}

	}

	public interface OnItemClick {
		void addContactNew(ContactDialog dialog);

		void close(ContactDialog dialog);
	}

	//Builder Class
	public static class DialogBuilder {
		private Activity activity;
		private String title = "Contact";
		private OnItemClick onItemClick;
		private int resourceId = 0;
		private String contactNumber = "";


		public DialogBuilder(Activity activity) {
			this.activity = activity;
		}

		public int getResourceId() {
			return resourceId;
		}

		public void setResourceId(int resourceId) {
			this.resourceId = resourceId;
		}

		public String getTitle() {
			return title;
		}

		public DialogBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public DialogBuilder setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
			return this;
		}


		public OnItemClick getOnItemClick() {
			return onItemClick;
		}

		public DialogBuilder setOnItemClick(OnItemClick onItemClick) {
			this.onItemClick = onItemClick;
			return this;
		}

		public Activity getActivity() {
			return activity;
		}

		public ContactDialog build() {
			return new ContactDialog(this);
		}

	}
}