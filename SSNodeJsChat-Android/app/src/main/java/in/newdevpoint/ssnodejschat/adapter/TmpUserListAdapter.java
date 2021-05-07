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
import in.newdevpoint.ssnodejschat.databinding.RowTmpUserListBinding;
import in.newdevpoint.ssnodejschat.model.FSRoomModel;
import in.newdevpoint.ssnodejschat.model.FSUsersModel;
import in.newdevpoint.ssnodejschat.model.TmpUserModel;
import in.newdevpoint.ssnodejschat.utility.TimeShow;
import in.newdevpoint.ssnodejschat.utility.UserDetails;
import in.newdevpoint.ssnodejschat.utility.Utils;


public class TmpUserListAdapter extends RecyclerView.Adapter<TmpUserListAdapter.MyViewHolder> {

    private final ArrayList<TmpUserModel> list = new ArrayList<>();


    private final CallBackForSinglePost callBack;
    private final String TAG = TmpUserListAdapter.class.getSimpleName();
    private final Context context;

    public TmpUserListAdapter(Context c, CallBackForSinglePost callback) {
        this.context = c;
        this.callBack = callback;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowTmpUserListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_tmp_user_list, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TmpUserModel item = list.get(position);



        holder.binding.rowTmpUserName.setText(item.getName());
        holder.binding.rowTmpEmail.setText(item.getEmail());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(ArrayList<TmpUserModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public void add(TmpUserModel item) {
        this.list.add(item);
        notifyDataSetChanged();
    }




    public ArrayList<TmpUserModel> getAllList() {
        return this.list;
    }

    public interface CallBackForSinglePost {
        void onClick(int position);

        void onClick(TmpUserModel item);

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final RowTmpUserListBinding binding;


        public MyViewHolder(RowTmpUserListBinding binding) {
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