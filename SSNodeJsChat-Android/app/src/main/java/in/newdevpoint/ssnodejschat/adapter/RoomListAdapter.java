package in.newdevpoint.ssnodejschat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowRoomListBinding;
import in.newdevpoint.ssnodejschat.model.FSRoomModel;
import in.newdevpoint.ssnodejschat.utility.UserDetails;


public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.MyViewHolder> {

    private final ArrayList<FSRoomModel> list = new ArrayList<>();


    private final CallBackForSinglePost callBack;
    private final String TAG = RoomListAdapter.class.getSimpleName();

    public RoomListAdapter(CallBackForSinglePost callback) {
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
        System.out.println(item);
        holder.binding.rowChatUserName.setText(item.getSenderUserDetail().getName());
        holder.binding.rowChatUserLastMessage.setText(item.getLastMessage());

        if (item.getUnread() != null && item.getUnread().get(UserDetails.myDetail.getId()) != null){
            holder.binding.rowChatUserPendingMessages.setText(Long.toString(item.getUnread().get(UserDetails.myDetail.getId())));
        }



        if(item.getSenderUserDetail().isOnline()){
            holder.binding.onlineStatusOffline.setVisibility(View.GONE);
            holder.binding.onlineStatusOnline.setVisibility(View.VISIBLE);
        }else{
            holder.binding.onlineStatusOffline.setVisibility(View.VISIBLE);
            holder.binding.onlineStatusOnline.setVisibility(View.GONE);
        }


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


    public void updateElement(FSRoomModel element) {
        for (int i = 0; i < this.list.size(); i++) {
            if (this.list.get(i).getRoomId().equals(element.getRoomId())) {
                this.list.set(i, element);
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