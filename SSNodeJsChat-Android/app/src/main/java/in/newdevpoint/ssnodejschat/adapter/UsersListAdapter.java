package in.newdevpoint.ssnodejschat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.databinding.RowUserListBinding;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;


public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.MyViewHolder> {

    private final ArrayList<FSUsersModel> list = new ArrayList<>();


    private final CallBackForSinglePost callBack;
    private final String TAG = UsersListAdapter.class.getSimpleName();

    public UsersListAdapter(CallBackForSinglePost callback) {
        this.callBack = callback;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowUserListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_user_list, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        FSUsersModel item = list.get(position);
        System.out.println(item);
        holder.binding.rowAllUserName.setText(item.getName());


//        if(item.isOnline()){
//            holder.onlineStatusOffline.setVisibility(View.GONE);
//            holder.onlineStatusOnline.setVisibility(View.VISIBLE);
//        }else{
//            holder.onlineStatusOffline.setVisibility(View.VISIBLE);
//            holder.onlineStatusOnline.setVisibility(View.GONE);
//        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(ArrayList<FSUsersModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<FSUsersModel> getAllList() {
        return this.list;

    }

    public interface CallBackForSinglePost {
        void onClick(int position);

        void onClick(FSUsersModel item);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowUserListBinding binding;


        public MyViewHolder(RowUserListBinding binding) {
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