package in.newdevpoint.ssnodejschat.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;


public class ContactUtility {

    public static void getContactList(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String[] arrOfStr = name.split(" ");
                String firstName = arrOfStr[0];
                String lastName = "";

                if (arrOfStr.length == 2) {
                    lastName = arrOfStr[1];
                } else {
                    lastName = "";
                }


                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
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
                            Log.d("inv_name", "Name: " + name);
                            Log.d("inv_name", "Phone Number: " + phoneNo);
                            Log.d("inv_name", " LAst NAme: " + lastName);
                            // TODO: 11/25/2019 : case 1: send phone nmber without +91, without 0, without -, without " ", without any other mask

                            //                        arrayListContactDetail.add(new ContactSyncModel(0, firstName, lastName, phoneNo.replace("+91", ""), "", "", false));

                        }
                        pCur.close();
                    }

                }

            }
        }

        if (cur != null) {
            cur.close();
        }
//        contactSync();
    }


}