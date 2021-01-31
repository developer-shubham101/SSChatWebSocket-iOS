package in.newdevpoint.ssnodejschat.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import in.newdevpoint.ssnodejschat.R;
import in.newdevpoint.ssnodejschat.adapter.ContactListAdapter;
import in.newdevpoint.ssnodejschat.adapter.SelectedContactListAdapter;
import in.newdevpoint.ssnodejschat.databinding.ActivityContactListBinding;
import in.newdevpoint.ssnodejschat.model.ContactModel;
import in.newdevpoint.ssnodejschat.utility.ContactUtility;
import in.newdevpoint.ssnodejschat.utility.PermissionClass;
import in.newdevpoint.ssnodejschat.viewModel.ContactListViewModel;

public class ContactListActivity extends AppCompatActivity implements
        SelectedContactListAdapter.OnRemoveIconClickListener,
        ContactListAdapter.OnCheckBoxClickListener,
        PermissionClass.PermissionRequire {


    public static final String INTENT_SELECTED_CONTACTS = "INTENT_SELECTED_CONTACTS";
    private static final String TAG = "ContactListActivity:";
    private static final int REQUEST_READ_CONTACTS = 23;
    private final ArrayList<ContactModel> tagUserListModelArrayList = new ArrayList<>();
    private final ArrayList<ContactModel> selectedTagUserArrayList = new ArrayList<>();
    private ActivityContactListBinding binding;
    private ContactListAdapter contactListAdapter;
    private SelectedContactListAdapter selectedContactListAdapter;
    private ContactListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_list);

        viewModel = ViewModelProviders.of(ContactListActivity.this).get(ContactListViewModel.class);

        //	private ArrayList<ContactModel> tempTravelerPostTagedArrayList = new ArrayList();
        PermissionClass permissionClass = new PermissionClass(this, this);
        permissionClass.askPermission(REQUEST_READ_CONTACTS);


        binding.conBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        contactListAdapter = new ContactListAdapter(ContactListActivity.this, this, tagUserListModelArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ContactListActivity.this);
        binding.tagUserRecyclerView.setLayoutManager(mLayoutManager);
        binding.tagUserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.tagUserRecyclerView.setAdapter(contactListAdapter);

        selectedContactListAdapter = new SelectedContactListAdapter(this, selectedTagUserArrayList, this);
        binding.tagSelectedUserRecyclerView.setLayoutManager(new LinearLayoutManager(ContactListActivity.this, RecyclerView.HORIZONTAL, true));
        binding.tagSelectedUserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.tagSelectedUserRecyclerView.setAdapter(selectedContactListAdapter);

        binding.tagUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTagUserArrayList.size() != 0) {

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<ContactModel>>() {
                    }.getType();
//					String data = response.body().string();
//					Log.d("Response : ", data);
//					ResponseModel<Object> obj = gson.fromJson(data, type);

                    String dataToPass = gson.toJson(selectedTagUserArrayList, type);

                    Intent intent = new Intent();
                    intent.putExtra(INTENT_SELECTED_CONTACTS, dataToPass);
                    setResult(RESULT_OK, intent);

                    finish();
                } else {
                    Toast.makeText(ContactListActivity.this, "Please select any User for Tag !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.searchUserEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchedKey = binding.searchUserEditText.getText().toString();
                contactListAdapter.getFilter().filter(searchedKey);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        viewModel.getTagUserViewModelLiveData().observe(this, new Observer<ArrayList<ContactModel>>() {
            @Override
            public void onChanged(ArrayList<ContactModel> contactModels) {
//				tagUserListModelArrayList = contactModels;
//				contactListAdapter.notifyDataSetChanged();
                contactListAdapter.addAll(contactModels);
            }
        });

    }


    @Override
    public void OnRemoveUserIconClick(ContactModel tagUserListModel, int position) {

        if (selectedTagUserArrayList.size() != 0) {
            for (int i = 0; i < selectedTagUserArrayList.size(); i++) {
                if (selectedTagUserArrayList.get(i).getMobile().equals(tagUserListModel.getMobile())) {
                    tagUserListModel.setSelected(false);
                    selectedTagUserArrayList.remove(i);

                    selectedContactListAdapter.notifyDataSetChanged();
                    contactListAdapter.notifyDataSetChanged();
                    break;
                }
            }

//			for (int k = 0; k < tempTravelerPostTagedArrayList.size(); k++) {
//				if (tempTravelerPostTagedArrayList.get(k).getMobile().equals(tagUserListModel.getMobile())) {
//					tempTravelerPostTagedArrayList.get(k).setSelected(false);
//					/*  tempTagedHotelUserArrayList.remove(k);*/
//					Log.d(TAG, "onCheckboxClickHotelTag: " + tempTravelerPostTagedArrayList.size());
//					break;
//				}
//			}

        }
    }

    @Override
    public void onCheckboxClick(ContactModel tagUserListModel, boolean isChecked) {
        if (isChecked) {
            // for add users in selected taged user adapter
            if (selectedTagUserArrayList.size() != 0) {
                for (int i = 0; i < selectedTagUserArrayList.size(); i++) {
                    if (!(selectedTagUserArrayList.get(i).getMobile().equals(tagUserListModel.getMobile()))) {
                        tagUserListModel.setSelected(true);
                        selectedTagUserArrayList.add(tagUserListModel);


//							tempTravelerPostTagedArrayList.add(new TravelerPostTagedModel(tagedPostId, tagUserListModel.getId(), "1"));


                        selectedContactListAdapter.notifyDataSetChanged();
                        contactListAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {
                // for add data in tempTagUserListModelArrayList and  in selectedTagUserArrayList when no user is selected for tag.
                for (int i = 0; i < tagUserListModelArrayList.size(); i++) {
                    if (!(tagUserListModelArrayList.get(i).getMobile().equals(tagUserListModel.getMobile()))) {
                        tagUserListModel.setSelected(true);

                        selectedTagUserArrayList.add(tagUserListModel);
                        selectedContactListAdapter.notifyDataSetChanged();
                        contactListAdapter.notifyDataSetChanged();
                        // add data in tempTagedUserArrayList for send taged user list to Destination or food or hotel tag api .

//							tempTravelerPostTagedArrayList.add(new TravelerPostTagedModel(tagedPostId, tagUserListModel.getId(), "1"));

                        break;
                    }
                }
            }
            Log.d("arrayListSIzeOnAdd:", "" + selectedTagUserArrayList.size());

        } else {
            for (int i = 0; i < selectedTagUserArrayList.size(); i++) {
                if (selectedTagUserArrayList.get(i).getMobile().equals(tagUserListModel.getMobile())) {
                    tagUserListModel.setSelected(false);
                    selectedTagUserArrayList.remove(i);

                    selectedContactListAdapter.notifyDataSetChanged();
                    contactListAdapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }


    @Override
    public void permissionDeny() {

    }

    @Override
    public void permissionGranted(int flag) {
        switch (flag) {
            case REQUEST_READ_CONTACTS:
                viewModel.getContactList();
                Toast.makeText(this, "Permission allow", Toast.LENGTH_SHORT).show();
                ContactUtility.getContactList(this);
                break;

        }
    }

    @Override
    public String[] listOfPermission(int flag) {
        return new String[]{Manifest.permission.READ_CONTACTS};
    }

}
