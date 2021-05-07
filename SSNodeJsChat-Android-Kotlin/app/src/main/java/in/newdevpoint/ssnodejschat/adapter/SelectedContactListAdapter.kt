package `in`.newdevpoint.ssnodejschat.adapter

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.RowSelectedContactListBinding
import `in`.newdevpoint.ssnodejschat.model.ContactModel
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SelectedContactListAdapter(private val context: Context, tagUserListModelArrayList: ArrayList<ContactModel>, onRemoveIconClickListener: OnRemoveIconClickListener) : RecyclerView.Adapter<SelectedContactListAdapter.MyHolder>() {
    private val tagUserListModelArrayList: ArrayList<ContactModel>
    private val onRemoveIconClickListener: OnRemoveIconClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: RowSelectedContactListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_selected_contact_list, parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val tagUserListModel: ContactModel = tagUserListModelArrayList[position]
        holder.binding.selectedUserName.setText(tagUserListModel.first_name + " " + tagUserListModel.last_name)
        //        if (tagUserListModel.getProfile_image() != null) {
//            if (!(tagUserListModel.getProfile_image().isEmpty())) {
//                Glide.with(context).load(Utils.getImageString(tagUserListModel.getProfile_image())).into(holder.binding.selectedUserImage);
//            } else {
//                holder.binding.selectedUserImage.setImageResource(R.drawable.user_profile_image);
//            }
//        } else
//            holder.binding.selectedUserImage.setImageResource(R.drawable.user_profile_image);
        holder.binding.removeFromTagListbtn.setOnClickListener { onRemoveIconClickListener.OnRemoveUserIconClick(tagUserListModel, position) }
    }

    fun addArrayListItem(tagUserListModel: ContactModel) {
        tagUserListModelArrayList.add(tagUserListModel)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tagUserListModelArrayList.size
    }

    interface OnRemoveIconClickListener {
        fun OnRemoveUserIconClick(tagUserListModel: ContactModel, position: Int)
    }

    inner class MyHolder(val binding: RowSelectedContactListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: Any?) {
            binding.executePendingBindings()
        }
    }

    init {
        this.tagUserListModelArrayList = tagUserListModelArrayList
        this.onRemoveIconClickListener = onRemoveIconClickListener
    }
}