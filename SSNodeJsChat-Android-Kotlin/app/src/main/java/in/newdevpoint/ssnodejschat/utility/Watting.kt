package `in`.newdevpoint.ssnodejschat.utility

import `in`.newdevpoint.ssnodejschat.R
import android.app.ProgressDialog
import android.content.Context

class Watting(context: Context) : ProgressDialog(context) {
    init {
        setTitle(context.getString(R.string.loading))
        setCancelable(false)
    }
}