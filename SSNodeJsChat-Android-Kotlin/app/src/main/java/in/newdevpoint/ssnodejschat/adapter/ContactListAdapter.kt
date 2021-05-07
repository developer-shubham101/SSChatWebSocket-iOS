package `in`.newdevpoint.ssnodejschat.adapter

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.RowContactListBinding
import `in`.newdevpoint.ssnodejschat.model.ContactModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ContactListAdapter(private val context: Context, private val onCheckBoxClickListener: OnCheckBoxClickListener, filteredTagUserListModelArrayList: ArrayList<ContactModel>) : RecyclerView.Adapter<ContactListAdapter.MyHolder>(), Filterable {
    //Two data sources, the original data and filtered data
    //    private ArrayList<TagUserListModel> originalData;
    private val originalTagListData: ArrayList<ContactModel>
    private var filteredTagUserListModelArrayList: ArrayList<ContactModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: RowContactListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_contact_list, parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val tagUserListModel: ContactModel = filteredTagUserListModelArrayList[position]
        holder.binding.tagUserName.setText(tagUserListModel.first_name + " " + tagUserListModel.last_name)
        if (tagUserListModel.isSelected ) {
            holder.binding.selectedCheckImage.visibility = View.VISIBLE
            holder.binding.unslectedUnCheckImage.visibility = View.GONE
        } else {
            holder.binding.selectedCheckImage.visibility = View.GONE
            holder.binding.unslectedUnCheckImage.visibility = View.VISIBLE
        }
        /*if (tagUserListModel.getProfile_image() != null) {
			if (!(tagUserListModel.getProfile_image().isEmpty())) {
				Glide.with(context).load(Utils.getImageString(tagUserListModel.getProfile_image())).into(holder.binding.tagUserImage);
			} else {
				holder.binding.tagUserImage.setImageResource(R.drawable.user_profile_image);
			}
		} else {
			holder.binding.tagUserImage.setImageResource(R.drawable.user_profile_image);
		}*/holder.binding.root.setOnClickListener { onCheckBoxClickListener.onCheckboxClick(tagUserListModel, !tagUserListModel.isSelected ) }
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

    fun addAll(arrayList: ArrayList<ContactModel>?) {
        filteredTagUserListModelArrayList.clear()
        filteredTagUserListModelArrayList.addAll(arrayList!!)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return filteredTagUserListModelArrayList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val results = FilterResults()

                //If there's nothing to filter on, return the original data for your list
                if (charSequence == null || charSequence.length == 0) {
                    results.values = originalTagListData
                    results.count = originalTagListData.size
                } else {
                    val filterResultsData: ArrayList<ContactModel> = ArrayList<ContactModel>()
                    for (data in filteredTagUserListModelArrayList) {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if (data.name.toLowerCase().contains(charSequence.toString().toLowerCase()) || data.mobile!!.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterResultsData.add(data)
                        }
                    }
                    results.values = filterResultsData
                    results.count = filterResultsData.size
                }
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredTagUserListModelArrayList = filterResults.values as ArrayList<ContactModel>
                notifyDataSetChanged()
            }
        }
    }

    interface OnCheckBoxClickListener {
        fun onCheckboxClick(tagUserListModel: ContactModel, isChecked: Boolean)
    }

    class MyHolder(val binding: RowContactListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any?) {
            binding.executePendingBindings()
        }
    }

    init {
        this.filteredTagUserListModelArrayList = filteredTagUserListModelArrayList
        originalTagListData = filteredTagUserListModelArrayList
    }
}