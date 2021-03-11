package in.newdevpoint.ssnodejschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowRoomListBinding;
import in.newdevpoint.ssnodejschat.model.FSRoomModel;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.utility.TimeShow;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.utility.Utils;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.MyViewHolder> {

    private final ArrayList<FSRoomModel> list = new ArrayList<>();


    private final CallBackForSinglePost callBack;
    private final String TAG = RoomListAdapter.class.getSimpleName();
    private final Context context;

    public RoomListAdapter(Context c, CallBackForSinglePost callback) {
        this.context = c;
        this.callBack = callback;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowRoomListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_room_list, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        FSRoomModel item = list.get(position);
        if (item.isGroup()) {
            holder.binding.rowChatUserName.setText(item.getGroupDetails().getGroup_name());

        } else {
            holder.binding.rowChatUserName.setText(item.getSenderUserDetail().getName());

            if (item.getSenderUserDetail() != null) {
//            holder.binding.rowChatUserPic
                Glide.with(context).load(Utils.getImageString(item.getSenderUserDetail().getProfile_image())).into(holder.binding.rowChatUserPic);
            }
        }


        holder.binding.rowChatUserLastMessage.setText(item.getLastMessage());
        holder.binding.rowChatUserLastMessageTime.setText(TimeShow.TimeFormatYesterdayToDay(item.getLastMessageTime(), "yyyy-MM-dd'T'HH:mm:ss.SSS"));

        if (item.getUnread() != null && item.getUnread().get(UserDetails.myDetail.getId()) != null) {
            Integer unreadCount = item.getUnread().get(UserDetails.myDetail.getId());
            if (unreadCount > 0) {
                holder.binding.rowChatUserPendingMessages.setVisibility(View.VISIBLE);
            } else {
                holder.binding.rowChatUserPendingMessages.setVisibility(View.INVISIBLE);
            }

            if (unreadCount > 99) {
                holder.binding.rowChatUserPendingMessages.setText("99+");
            } else {
                holder.binding.rowChatUserPendingMessages.setText(unreadCount.toString());
            }


        } else {
            holder.binding.rowChatUserPendingMessages.setVisibility(View.INVISIBLE);
        }

//        if(item.getSenderUserDetail().isOnline()){
//            holder.binding.onlineStatusOffline.setVisibility(View.GONE);
//            holder.binding.onlineStatusOnline.setVisibility(View.VISIBLE);
//        }else{
//            holder.binding.onlineStatusOffline.setVisibility(View.VISIBLE);
//            holder.binding.onlineStatusOnline.setVisibility(View.GONE);
//        }


        holder.binding.onlineStatusOffline.setVisibility(View.GONE);
        holder.binding.onlineStatusOnline.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(ArrayList<FSRoomModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public void add(FSRoomModel item) {
        this.list.add(item);
        notifyDataSetChanged();
    }


    public void addOrUpdate(FSRoomModel item) {
        boolean isAlreadyAdded = false;
        for (FSRoomModel element : this.list) {
            if (element.getRoomId().equals(item.getRoomId())) {
                isAlreadyAdded = true;
                break;
            }
        }
        if (isAlreadyAdded) {
            updateElement(item);
        } else {
            this.list.add(item);
            notifyDataSetChanged();
        }
    }


    public void updateUserElement(FSUsersModel element) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getSenderUserDetail().getId().equals(element.getId())) {
                this.list.get(i).setSenderUserDetail(element);
                notifyDataSetChanged();
            }
        }
    }

    public void updateElement(FSRoomModel element) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getRoomId().equals(element.getRoomId())) {
//                this.list.set(i, element);
                this.list.remove(i);
                this.list.add(0, element);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public ArrayList<FSRoomModel> getAllList() {
        return this.list;

    }

    public interface CallBackForSinglePost {
        void onClick(int position);

        void onClick(FSRoomModel item);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowRoomListBinding binding;


        public MyViewHolder(RowRoomListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    callBack.onClick(list.get(getAdapterPosition()));
                }
            });

        }
    }

}