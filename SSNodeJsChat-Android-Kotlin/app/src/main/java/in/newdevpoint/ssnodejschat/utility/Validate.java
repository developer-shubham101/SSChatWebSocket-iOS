package in.newdevpoint.ssnodejschat.utility;

import android.text.TextUtils;

public class Validate {
    public static boolean validateMobile(String mobileNoString) {
        if(TextUtils.isEmpty(mobileNoString)){
            return true;
        }else{
            return isMobile(mobileNoString);
        }
    }
    public static boolean isMobile(String mobileNoString) {
        return mobileNoString.matches("[0-9]{3}-[0-9]{3}-[0-9]{4}");
    }

    public static boolean isEmail(String emailString) {
        return emailString.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
