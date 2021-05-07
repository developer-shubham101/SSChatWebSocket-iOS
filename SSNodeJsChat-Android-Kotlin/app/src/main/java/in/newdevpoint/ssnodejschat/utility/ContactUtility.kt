package `in`.newdevpoint.ssnodejschat.utility

import android.content.Context
import android.provider.ContactsContract
import android.util.Log

object ContactUtility {
    fun getContactList(context: Context) {
        val cr = context.contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        if (cur?.count ?: 0 > 0) {
            while (cur!!.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val arrOfStr = name.split(" ".toRegex()).toTypedArray()
                val firstName = arrOfStr[0]
                var lastName = ""
                lastName = if (arrOfStr.size == 2) {
                    arrOfStr[1]
                } else {
                    ""
                }
                if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
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
                            Log.d("inv_name", "Name: $name")
                            Log.d("inv_name", "Phone Number: $phoneNo")
                            Log.d("inv_name", " LAst NAme: $lastName")
                            // TODO: 11/25/2019 : case 1: send phone nmber without +91, without 0, without -, without " ", without any other mask

                            //                        arrayListContactDetail.add(new ContactSyncModel(0, firstName, lastName, phoneNo.replace("+91", ""), "", "", false));
                        }
                        pCur.close()
                    }
                }
            }
        }
        cur?.close()
        //        contactSync();
    }
}