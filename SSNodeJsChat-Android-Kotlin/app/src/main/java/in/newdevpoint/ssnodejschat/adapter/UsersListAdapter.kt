package `in`.newdevpoint.ssnodejschat.adapter

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.databinding.RowUserListBinding
import `in`.newdevpoint.ssnodejschat.model.FSUsersModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class UsersListAdapter(private val callBack: CallBackForSinglePost) : RecyclerView.Adapter<UsersListAdapter.MyViewHolder>() {
    private val list: ArrayList<FSUsersModel> = ArrayList<FSUsersModel>()
    private val TAG = UsersListAdapter::class.java.simpleName
    private var isGroupMode = false
    fun isGroupMode(): Boolean {
        return isGroupMode
    }

    fun setGroupMode(groupMode: Boolean) {
        isGroupMode = groupMode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: RowUserListBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_user_list, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: FSUsersModel = list[position]

        holder.binding.rowAllUserName.setText(item.name)
        holder.binding.rowAllUserCheck.isChecked = item.isChecked


//        if(item.isOnline()){
//            holder.onlineStatusOffline.setVisibility(View.GONE);
//            holder.onlineStatusOnline.setVisibility(View.VISIBLE);
//        }else{
//            holder.onlineStatusOffline.setVisibility(View.VISIBLE);
//            holder.onlineStatusOnline.setVisibility(View.GONE);
//        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addAll(list: ArrayList<FSUsersModel>?) {
        this.list.clear()
        this.list.addAll(list!!)
        notifyDataSetChanged()
    }

    fun getAllList(): ArrayList<FSUsersModel> {
        return list
    }

    interface CallBackForSinglePost {
        fun onClick(position: Int)
        fun onClick(item: FSUsersModel)
    }

    inner class MyViewHolder(val binding: RowUserListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v: View? ->
                if (isGroupMode) {
                    val tmpItem: FSUsersModel = list[adapterPosition]
                    tmpItem.isChecked = !tmpItem.isChecked
                    notifyDataSetChanged()
                } else {
                    callBack.onClick(list[adapterPosition])
                }
            }
        }
    }
}