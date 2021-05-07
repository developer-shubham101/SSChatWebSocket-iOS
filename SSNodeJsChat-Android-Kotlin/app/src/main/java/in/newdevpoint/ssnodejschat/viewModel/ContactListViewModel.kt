package `in`.newdevpoint.ssnodejschat.viewModel

import `in`.newdevpoint.ssnodejschat.model.ContactModel
import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.util.*

class ContactListViewModel(private val application: Application) : AndroidViewModel(application) {
    private val tagUserViewModelLiveData: MutableLiveData<ArrayList<ContactModel>> = MutableLiveData<ArrayList<ContactModel>>()
    fun getTagUserViewModelLiveData(): MutableLiveData<ArrayList<ContactModel>> {
        return tagUserViewModelLiveData
    }

    fun getContactList() {
        val cr = application.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        val arrayListContactDetail: HashMap<String, ContactModel?> = HashMap<String, ContactModel?>()
        if (cur?.count ?: 0 > 0) {
            while (cur!!.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                var name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (name == null) {
                    name = ""
                }
                val arrOfStr = name.split(" ".toRegex()).toTypedArray()
                val firstName = arrOfStr[0]
                var lastName = ""
                lastName = if (arrOfStr.size == 2) {
                    arrOfStr[1]
                } else {
                    ""
                }

//				ArrayDeque
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            var phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            phoneNo = phoneNo.replace("+91", "")
                            phoneNo = phoneNo.replace(" ", "")
                            phoneNo = phoneNo.replace("-", "")
                            phoneNo = phoneNo.replace("+91", "")

                            //							Log.d("inv_name", "_ID: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
////							Log.d("inv_name", "Name: " + name);
////							Log.d("inv_name", "Phone Number: " + phoneNo);
////							Log.d("inv_name", " LAst NAme: " + lastName);
//							Log.d("inv_name", "PHONETIC_NAME: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHONETIC_NAME)));
//							Log.d("inv_name", "DISPLAY_NAME: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
//							Log.d("inv_name", "PREFERRED_PHONE_ACCOUNT_COMPONENT_NAME: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PREFERRED_PHONE_ACCOUNT_COMPONENT_NAME)));
//							Log.d("inv_name", "DISPLAY_NAME_ALTERNATIVE: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_ALTERNATIVE)));
//							Log.d("inv_name", "DISPLAY_NAME_PRIMARY: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)));
//							Log.d("inv_name", "DISPLAY_NAME_SOURCE: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_SOURCE)));
//							Log.d("inv_name", "NAME_RAW_CONTACT_ID: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID)));
////							Log.d("inv_name", "PHONETIC_NAME_STYLE: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHONETIC_NAME_STYLE)));
////							Log.d("inv_name", "SEARCH_DISPLAY_NAME_KEY: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.SEARCH_DISPLAY_NAME_KEY)));
////							Log.d("inv_name", "_COUNT: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone._COUNT)));
//							Log.d("inv_name", "ACCOUNT_TYPE_AND_DATA_SET: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.ACCOUNT_TYPE_AND_DATA_SET)));
////							Log.d("inv_name", "AGGREGATION_MODE: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.AGGREGATION_MODE)));
//							Log.d("inv_name", "CONTACT_ID: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
//							Log.d("inv_name", "CARRIER_PRESENCE: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CARRIER_PRESENCE)));
//							Log.d("inv_name", "CHAT_CAPABILITY: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CHAT_CAPABILITY)));
//							Log.d("inv_name", "CONTACT_STATUS: " + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_STATUS)));

                            // TODO: 11/25/2019 : case 1: send phone number without +91, without 0, without -, without " ", without any other mask

//							String rawContactId = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID));
                            val contact = ContactModel(firstName, "", lastName, phoneNo)
                            if (arrayListContactDetail[phoneNo] == null) {
                                arrayListContactDetail[phoneNo] = contact
                            }

//							arrayListContactDetail.add();
                        }
                        pCur.close()
                    }
                }
            }
        }
        tagUserViewModelLiveData.postValue(ArrayList<ContactModel>(arrayListContactDetail.values))
        cur?.close()
        //        contactSync();
    }
}