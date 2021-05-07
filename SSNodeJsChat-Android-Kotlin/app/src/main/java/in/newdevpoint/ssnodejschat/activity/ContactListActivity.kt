package `in`.newdevpoint.ssnodejschat.activity

import `in`.newdevpoint.ssnodejschat.R
import `in`.newdevpoint.ssnodejschat.activity.ContactListActivity
import `in`.newdevpoint.ssnodejschat.adapter.ContactListAdapter
import `in`.newdevpoint.ssnodejschat.adapter.SelectedContactListAdapter
import `in`.newdevpoint.ssnodejschat.databinding.ActivityContactListBinding
import `in`.newdevpoint.ssnodejschat.model.ContactModel
import `in`.newdevpoint.ssnodejschat.utility.ContactUtility
import `in`.newdevpoint.ssnodejschat.utility.PermissionClass
import `in`.newdevpoint.ssnodejschat.viewModel.ContactListViewModel
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ContactListActivity : AppCompatActivity(), SelectedContactListAdapter.OnRemoveIconClickListener, ContactListAdapter.OnCheckBoxClickListener, PermissionClass.PermissionRequire {
    private val tagUserListModelArrayList: ArrayList<ContactModel> = ArrayList<ContactModel>()
    private val selectedTagUserArrayList: ArrayList<ContactModel> = ArrayList<ContactModel>()
    private lateinit var binding: ActivityContactListBinding
    private lateinit var contactListAdapter: ContactListAdapter
    private lateinit var selectedContactListAdapter: SelectedContactListAdapter
    private lateinit var viewModel: ContactListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_list)
        viewModel = ViewModelProviders.of(this@ContactListActivity).get<ContactListViewModel>(ContactListViewModel::class.java)

        //	private ArrayList<ContactModel> tempTravelerPostTagedArrayList = new ArrayList();
        val permissionClass = PermissionClass(this, this)
        permissionClass.askPermission(REQUEST_READ_CONTACTS)
        binding.conBackBtn.setOnClickListener { finish() }
        contactListAdapter = ContactListAdapter(this@ContactListActivity, this, tagUserListModelArrayList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ContactListActivity)
        binding.tagUserRecyclerView.layoutManager = mLayoutManager
        binding.tagUserRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.tagUserRecyclerView.adapter = contactListAdapter
        selectedContactListAdapter = SelectedContactListAdapter(this, selectedTagUserArrayList, this)
        binding.tagSelectedUserRecyclerView.layoutManager = LinearLayoutManager(this@ContactListActivity, RecyclerView.HORIZONTAL, true)
        binding.tagSelectedUserRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.tagSelectedUserRecyclerView.adapter = selectedContactListAdapter
        binding.tagUserBtn.setOnClickListener {
            if (selectedTagUserArrayList.size != 0) {
                val gson = Gson()
                val type = object : TypeToken<ArrayList<ContactModel?>?>() {}.type
                //					String data = response.body().string();
//					Log.d("Response : ", data);
//					ResponseModel<Object> obj = gson.fromJson(data, type);
                val dataToPass = gson.toJson(selectedTagUserArrayList, type)
                val intent = Intent()
                intent.putExtra(INTENT_SELECTED_CONTACTS, dataToPass)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this@ContactListActivity, "Please select any User for Tag !", Toast.LENGTH_SHORT).show()
            }
        }
        binding.searchUserEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val searchedKey = binding.searchUserEditText.text.toString()
                contactListAdapter.getFilter().filter(searchedKey)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        // TODO: 08/04/21 Fix The issues
//        viewModel.getTagUserViewModelLiveData().observe(this, Observer<ArrayList<Any?>?> { contactModels ->
//            contactListAdapter.addAll(contactModels)
//        })


    }

    override fun OnRemoveUserIconClick(tagUserListModel: ContactModel, position: Int) {
        if (selectedTagUserArrayList.size != 0) {
            for (i in selectedTagUserArrayList.indices) {
                if (selectedTagUserArrayList[i].mobile == tagUserListModel.mobile) {
                    tagUserListModel.isSelected = false
                    selectedTagUserArrayList.removeAt(i)
                    selectedContactListAdapter.notifyDataSetChanged()
                    contactListAdapter.notifyDataSetChanged()
                    break
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

    override fun onCheckboxClick(tagUserListModel: ContactModel, isChecked: Boolean) {
        if (isChecked) {
            // for add users in selected taged user adapter
            if (selectedTagUserArrayList.size != 0) {
                for (i in selectedTagUserArrayList.indices) {
                    if (selectedTagUserArrayList[i].mobile != tagUserListModel.mobile) {
                        tagUserListModel.isSelected = true
                        selectedTagUserArrayList.add(tagUserListModel)


//							tempTravelerPostTagedArrayList.add(new TravelerPostTagedModel(tagedPostId, tagUserListModel.getId(), "1"));
                        selectedContactListAdapter.notifyDataSetChanged()
                        contactListAdapter.notifyDataSetChanged()
                        break
                    }
                }
            } else {
                // for add data in tempTagUserListModelArrayList and  in selectedTagUserArrayList when no user is selected for tag.
                for (i in tagUserListModelArrayList.indices) {
                    if (tagUserListModelArrayList[i].mobile != tagUserListModel.mobile) {
                        tagUserListModel.isSelected = true
                        selectedTagUserArrayList.add(tagUserListModel)
                        selectedContactListAdapter.notifyDataSetChanged()
                        contactListAdapter.notifyDataSetChanged()
                        // add data in tempTagedUserArrayList for send taged user list to Destination or food or hotel tag api .

//							tempTravelerPostTagedArrayList.add(new TravelerPostTagedModel(tagedPostId, tagUserListModel.getId(), "1"));
                        break
                    }
                }
            }
            Log.d("arrayListSIzeOnAdd:", "" + selectedTagUserArrayList.size)
        } else {
            for (i in selectedTagUserArrayList.indices) {
                if (selectedTagUserArrayList[i].mobile == tagUserListModel.mobile) {
                    tagUserListModel.isSelected = false
                    selectedTagUserArrayList.removeAt(i)
                    selectedContactListAdapter.notifyDataSetChanged()
                    contactListAdapter.notifyDataSetChanged()
                    break
                }
            }
        }
    }

    override fun permissionDeny() {}
    override fun permissionGranted(flag: Int) {
        when (flag) {
            REQUEST_READ_CONTACTS -> {
                viewModel.getContactList()
                Toast.makeText(this, "Permission allow", Toast.LENGTH_SHORT).show()
                ContactUtility.getContactList(this)
            }
        }
    }

    override fun listOfPermission(flag: Int): Array<String> {
        return arrayOf(Manifest.permission.READ_CONTACTS)
    }

    companion object {
        const val INTENT_SELECTED_CONTACTS = "INTENT_SELECTED_CONTACTS"
        private const val TAG = "ContactListActivity:"
        private const val REQUEST_READ_CONTACTS = 23
    }
}