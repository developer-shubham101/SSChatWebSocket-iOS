package in.newdevpoint.ssnodejschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowSelectedContactListBinding;
import in.newdevpoint.ssnodejschat.model.ContactModel;

public class SelectedContactListAdapter extends RecyclerView.Adapter<SelectedContactListAdapter.MyHolder> {
	private Context context;
	private ArrayList<ContactModel> tagUserListModelArrayList;
	private OnRemoveIconClickListener onRemoveIconClickListener;

	public SelectedContactListAdapter(Context context, ArrayList<ContactModel> tagUserListModelArrayList, OnRemoveIconClickListener onRemoveIconClickListener) {
		this.context = context;
		this.tagUserListModelArrayList = tagUserListModelArrayList;
		this.onRemoveIconClickListener = onRemoveIconClickListener;
	}

	@NonNull
	@Override
	public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		RowSelectedContactListBinding binding = DataBindingUtil.inflate(
				LayoutInflater.from(parent.getContext()),
				R.layout.row_selected_contact_list, parent, false);
		return new MyHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
		ContactModel tagUserListModel = tagUserListModelArrayList.get(position);
		holder.binding.selectedUserName.setText(tagUserListModel.getFirst_name() + " " + tagUserListModel.getLast_name());
//        if (tagUserListModel.getProfile_image() != null) {
//            if (!(tagUserListModel.getProfile_image().isEmpty())) {
//                Glide.with(context).load(Utils.getImageString(tagUserListModel.getProfile_image())).into(holder.binding.selectedUserImage);
//            } else {
//                holder.binding.selectedUserImage.setImageResource(R.drawable.user_profile_image);
//            }
//        } else
//            holder.binding.selectedUserImage.setImageResource(R.drawable.user_profile_image);

		holder.binding.removeFromTagListbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onRemoveIconClickListener.OnRemoveUserIconClick(tagUserListModel, position);
			}
		});
	}

	public void addArrayListItem(ContactModel tagUserListModel) {
		this.tagUserListModelArrayList.add(tagUserListModel);
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return tagUserListModelArrayList.size();
	}

	public interface OnRemoveIconClickListener {
		void OnRemoveUserIconClick(ContactModel tagUserListModel, int position);
	}

	public class MyHolder extends RecyclerView.ViewHolder {

		private RowSelectedContactListBinding binding;

		public MyHolder(RowSelectedContactListBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}

		public void bind(Object obj) {
			binding.executePendingBindings();
		}
	}
}
