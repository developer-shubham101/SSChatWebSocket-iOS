package in.newdevpoint.ssnodejschat.viewModel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;



import java.util.ArrayList;
import java.util.HashMap;

import in.newdevpoint.ssnodejschat.model.ContactModel;

public class ContactListViewModel extends AndroidViewModel {

	private Application application;
	private MutableLiveData<ArrayList<ContactModel>> tagUserViewModelLiveData = new MutableLiveData<>();


	public ContactListViewModel(@NonNull Application application) {
		super(application);
		this.application = application;

	}

	public MutableLiveData<ArrayList<ContactModel>> getTagUserViewModelLiveData() {
		return tagUserViewModelLiveData;
	}

	public void getContactList() {
		ContentResolver cr = application.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		HashMap<String, ContactModel> arrayListContactDetail = new HashMap<>();
		if ((cur != null ? cur.getCount() : 0) > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (name == null) {
					name = "";
				}
				String[] arrOfStr = name.split(" ");
				String firstName = arrOfStr[0];
				String lastName = "";

				if (arrOfStr.length == 2) {
					lastName = arrOfStr[1];
				} else {
					lastName = "";
				}

//				ArrayDeque

				if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[]{id}, null);
					if (pCur != null) {
						while (pCur.moveToNext()) {
							String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneNo = phoneNo.replace("+91", "");
							phoneNo = phoneNo.replace(" ", "");
							phoneNo = phoneNo.replace("-", "");
							phoneNo = phoneNo.replace("+91", "");
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

							ContactModel contact = new ContactModel(firstName, "", lastName, phoneNo);
							if (arrayListContactDetail.get(phoneNo) == null) {
								arrayListContactDetail.put(phoneNo, contact);
							}

//							arrayListContactDetail.add();

						}


						pCur.close();


					}

				}

			}
		}
		tagUserViewModelLiveData.postValue(new ArrayList<>(arrayListContactDetail.values()));
		if (cur != null) {
			cur.close();
		}
//        contactSync();
	}
}
