package in.newdevpoint.ssnodejschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowContactListBinding;
import in.newdevpoint.ssnodejschat.model.ContactModel;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyHolder> implements Filterable {

    private final Context context;
    private ArrayList<ContactModel> filteredTagUserListModelArrayList;
    private final OnCheckBoxClickListener onCheckBoxClickListener;
    //Two data sources, the original data and filtered data
//    private ArrayList<TagUserListModel> originalData;
    private final ArrayList<ContactModel> originalTagListData;

    public ContactListAdapter(Context context, OnCheckBoxClickListener onCheckBoxClickListener, ArrayList<ContactModel> filteredTagUserListModelArrayList) {
        this.context = context;
        this.onCheckBoxClickListener = onCheckBoxClickListener;
        this.filteredTagUserListModelArrayList = filteredTagUserListModelArrayList;
        this.originalTagListData = filteredTagUserListModelArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowContactListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_contact_list, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        ContactModel tagUserListModel = filteredTagUserListModelArrayList.get(position);
        holder.binding.tagUserName.setText(tagUserListModel.getFirst_name() + " " + tagUserListModel.getLast_name());
        if (tagUserListModel.isSelected()) {

            holder.binding.selectedCheckImage.setVisibility(View.VISIBLE);
            holder.binding.unslectedUnCheckImage.setVisibility(View.GONE);
        } else {
            holder.binding.selectedCheckImage.setVisibility(View.GONE);
            holder.binding.unslectedUnCheckImage.setVisibility(View.VISIBLE);
        }
		/*if (tagUserListModel.getProfile_image() != null) {
			if (!(tagUserListModel.getProfile_image().isEmpty())) {
				Glide.with(context).load(Utils.getImageString(tagUserListModel.getProfile_image())).into(holder.binding.tagUserImage);
			} else {
				holder.binding.tagUserImage.setImageResource(R.drawable.user_profile_image);
			}
		} else {
			holder.binding.tagUserImage.setImageResource(R.drawable.user_profile_image);
		}*/

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCheckBoxClickListener.onCheckboxClick(tagUserListModel, !tagUserListModel.isSelected());
            }
        });
     /*   holder.binding.tagCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    onCheckBoxClickListener.onCheckboxClick(tagUserListModel,isChecked, position );
                } else {
                    onCheckBoxClickListener.onCheckboxClick(tagUserListModel,isChecked, position);
                }
            }
        });*/
    }


    public void addAll(ArrayList<ContactModel> arrayList) {
        this.filteredTagUserListModelArrayList.clear();
        this.filteredTagUserListModelArrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filteredTagUserListModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if (charSequence == null || charSequence.length() == 0) {
                    results.values = originalTagListData;
                    results.count = originalTagListData.size();
                } else {
                    ArrayList<ContactModel> filterResultsData = new ArrayList<ContactModel>();

                    for (ContactModel data : filteredTagUserListModelArrayList) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if (data.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) || data.getMobile().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredTagUserListModelArrayList = (ArrayList<ContactModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnCheckBoxClickListener {
        void onCheckboxClick(ContactModel tagUserListModel, boolean isChecked);
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        private final RowContactListBinding binding;

        public MyHolder(RowContactListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj) {
            binding.executePendingBindings();
        }

    }
}
