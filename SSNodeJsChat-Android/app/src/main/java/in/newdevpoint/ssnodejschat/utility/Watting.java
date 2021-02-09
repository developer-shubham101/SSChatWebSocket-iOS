package in.newdevpoint.ssnodejschat.utility;

import android.content.Context;

import in.newdevpoint.ssnodejschat.R;


public class Watting extends android.app.ProgressDialog {
    public Watting(Context context) {
        super(context);
        setTitle(context.getString(R.string.loading));
        setCancelable(false);
    }
}
